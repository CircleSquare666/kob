<template>
    <Playground v-if="$store.state.pk.status === 'playing'" />
    <Matchground v-if="$store.state.pk.status === 'matching'" />
</template>

<script>
import Playground from "@/components/PlayGround";
import Matchground from "@/components/MatchGround";
import { onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";
export default {
    components: {
        Playground,
        Matchground,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}`;//WebSocket以ws开头，类似于http
        let socket = null;
        onMounted(() => {
            store.commit("updateOpponent",{
                username:"我的对手",
                photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
            socket = new WebSocket(socketUrl);
            socket.onopen = () => {
                console.log("WebSocket connected!");
                store.commit("updateSocket", socket);
            };
            socket.onmessage = (msg) => {
                const data = JSON.parse(msg.data);
                console.log(data);
                if(data.event === "start-matching"){ //匹配成功
                    store.commit("updateOpponent",{
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout(() =>{
                        store.commit("updateStatus", "playing");
                    }, 2000);//2000ms后执行
                    store.commit("updateGameMap", data.gamemap);
                }
            };
            socket.onclose = () => {
                console.log("WebSocket disconnected!");
            }
        });
        onUnmounted(() => {
            socket.close();
            store.commit("updateStatus", "matching");
        });
    }
}


</script>

<style scoped>
</style>