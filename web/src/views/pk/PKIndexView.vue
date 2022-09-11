<template>
    <Playground v-if="$store.state.pk.status === 'playing'" />
    <Matchground v-if="$store.state.pk.status === 'matching'" />
    <ResultBoard v-if="$store.state.pk.loser != 'none'"/>
</template>

<script>
import Playground from "@/components/PlayGround";
import Matchground from "@/components/MatchGround";
import ResultBoard from "@/components/ResultBoard";
import { onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";
export default {
    components: {
        Playground,
        Matchground,
        ResultBoard,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}`;//WebSocket以ws开头，类似于http
        let socket = null;
        onMounted(() => {
            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
            socket = new WebSocket(socketUrl);
            socket.onopen = () => {
                console.log("WebSocket connected!");
                store.commit("updateSocket", socket);
            };
            socket.onmessage = (msg) => {
                const data = JSON.parse(msg.data);
                console.log(data);
                if (data.event === "start-matching") { //匹配成功
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 2000);//2000ms后执行
                    store.commit("updateGame", data.game);
                } else if (data.event === 'move') {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                } else if (data.event === 'result') {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    if(data.loser === 'all' || data.loser === 'A'){
                        snake0.status = 'die';
                    }
                    if(data.loser === 'all' || data.loser === 'B'){
                        snake1.status = 'die';
                    }
                    store.commit("updateLoser", data.loser);
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