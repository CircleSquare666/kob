<template>
    <!-- ref用来与vue中创立的对象产生关联 -->
    <div ref="parent" class="gamemap">
        <!-- 游戏画到canvas画布里面 -->
        <!-- tabindex="0"可以让canvas获取用户的输入操作 -->
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>

<script>
import { GameMap } from "@/assets/scripts/GameMap";
import { ref, onMounted } from 'vue'
import { useStore } from "vuex";

export default {

    setup() {
        const store = useStore();
        let parent = ref(null);
        let canvas = ref(null);

        // onMounted表示挂载完之后，要加载什么对象
        onMounted(() => {
            //在vue中获取对象要用.value
            store.commit("updateGameObject", new GameMap(canvas.value.getContext('2d'), parent.value, store));
        });

        return {
            parent,
            canvas
        }
    }
}

</script>

<style scoped>
div.gamemap {
    width: 100%;
    height: 100%;
    /* 用来将画布居中 */
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>


