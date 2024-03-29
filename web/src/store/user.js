//store是用来存储全局信息的
import $ from 'jquery'

export default {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
        pulling_info: true,//是否正在拉取信息，当在拉取信息的时候，不加载页面
    },
    getters: {
    },
    mutations: { //用commit调用mutations，同步操作放到mutations
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
        logout(state) {
            state.token = "";
            state.id = "";
            state.username = "";
            state.photo = "";
            state.is_login = "";
        },
        updatePullingInfo(state, pulling_info){
            state.pulling_info = pulling_info;
        }
    },
    actions: { //用dispatch调用actions，异步操作用actions
        login(context, data) {
            $.ajax({
                url: "http://localhost:3000/user/account/token",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        localStorage.setItem("jwt_token", resp.token);
                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            });
        },
        getInfo(context, data) {
            $.ajax({
                url: "http://localhost:3000/user/account/info",
                type: "get",
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    if (resp.error_message === 'success') {
                        context.commit("updateUser", {
                            //...resp是把resp解构出来
                            ...resp,
                            is_login: true,
                        });
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            })
        },
        logout(context) {
            localStorage.removeItem("jwt_token");
            context.commit("logout");
        }
    },
    modules: {
    }
}
