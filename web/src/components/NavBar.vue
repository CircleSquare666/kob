/**
    导航栏（用bootstrap或Element UI来做）
 */

<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <!-- <a class="navbar-brand" href="/">King Of Bots</a> -->
            <!-- 用router-link标签代替a标签可以避免点击时刷新页面，href变成了:to="" -->
            <router-link class="navbar-brand" :to="{ name: 'home' }">King Of Bots</router-link>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <!-- <a class="nav-link active" aria-current="page" href="/pk">对战</a> -->
                        <!-- 在某个属性里用表达式时，要在前面加个冒号（其实是v-bind:的缩写）-->
                        <router-link :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link'"
                            :to="{ name: 'pk_index' }">对战
                        </router-link>
                    </li>
                    <li class="nav-item">
                        <!-- <a class="nav-link" href="/record">对局列表</a> -->
                        <router-link :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link'"
                            :to="{ name: 'record_index' }">对局列表</router-link>
                    </li>
                    <li class="nav-item">
                        <!-- <a class="nav-link" href="/ranklist">排行榜</a> -->
                        <router-link :class="route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'"
                            :to="{ name: 'ranklist_index' }">排行榜</router-link>
                    </li>
                </ul>
                <ul class="navbar-nav" v-if="$store.state.user.is_login">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            {{ $store.state.user.username }}
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <!-- <li><a class="dropdown-item" href="/user/bot">我的Bot</a> -->
                            <li>
                                <router-link class="dropdown-item" :to="{ name: 'user_bot_index' }">我的Bot</router-link>
                            </li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
                        </ul>
                    </li>
                </ul>
                <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
                    <li class="nav-item ">
                        <router-link class="nav-link" :to="{name: 'user_account_login'}" role="button">
                            登录
                        </router-link>
                    </li>
                    <li class="nav-item dropdown">
                        <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">
                            注册
                        </router-link>
                    </li>
                </ul>

            </div>
        </div>
    </nav>
</template>

//加上scoped会使这个css加上一个随机字符串，避免影响到组件以外的部分
<style scoped>
</style>

<script>
//用于获取当前处在哪个页面
import { useRoute } from "vue-router";
//用于实时计算变量值
import { computed } from "vue";
import { useStore } from "vuex";

export default {
    setup() {
        const store = useStore();
        const route = useRoute();
        let route_name = computed(() => route.name);
        
        const logout = () =>{
            store.dispatch("logout");
        }
        return {
            route_name, 
            logout
        }
    }
}
</script>


