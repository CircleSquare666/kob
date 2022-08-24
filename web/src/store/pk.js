export default {
    state: {
        status: "matching", //matching表示匹配截面，playing表示对战界面
        socket: null,
        opponent_username: "",
        opponent_photo: "",
        gamemap: null,
    },
    getters: {
    },
    mutations: { //用commit调用mutations，同步操作放到mutations
        updateSocket(state, socket){//创建websocket连接后，要将连接信息存储到全局变量里
            state.socket = socket;
        },
        updateOpponent(state, opponent){
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updateStatus(state, status){
            state.status = status;
        },
        updateGameMap(state, gamemap){
            state.gamemap = gamemap;
        }
    },
    actions: { //用dispatch调用actions，异步操作用actions
    },
    modules: {
    }
}
