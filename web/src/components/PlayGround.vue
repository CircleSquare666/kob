/**
    游戏区域
*/

<template>
    <div class="playground">
        <GameMap/>
    </div>
</template>

<script>
import GameMap from "@/components/GameMap"
export default{
    components:{
        GameMap
    }
}


</script>

<style scoped>
div.playground {
    /*设置宽度为60%的浏览器宽度 */
    width: 60vw;
    /*设置高度为70%的浏览器高度 */
    height: 70vh;
    /*居中*/
    margin: 40px auto;
}
</style>