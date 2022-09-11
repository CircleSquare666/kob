export default {
    state: {
        status: "matching", //matching表示匹配截面，playing表示对战界面
        socket: null,
        opponent_username: "",
        opponent_photo: "",
        gamemap: null,
        a_id: 0,
        a_sx: 0,
        a_sy: 0,
        b_id: 0,
        b_sx: 0,
        b_sy: 0,
        gameObject: null,
        loser: "none", //none、all、A、B
    },
    getters: {
    },
    mutations: { //用commit调用mutations，同步操作放到mutations
        updateSocket(state, socket) {//创建websocket连接后，要将连接信息存储到全局变量里
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateGame(state, game) {
            state.gamemap = game.map;
            state.a_id = game.a_id;
            state.a_sx = game.a_sx;
            state.a_sy = game.a_sy;
            state.b_id = game.b_id;
            state.b_sx = game.b_sx;
            state.b_sy = game.b_sy;
        },
        updateGameObject(state, gameObject){
            state.gameObject = gameObject;
        },
        updateLoser(state, loser){
            state.loser = loser;
        }
    },
    actions: { //用dispatch调用actions，异步操作用actions
    },
    modules: {
    }
}
