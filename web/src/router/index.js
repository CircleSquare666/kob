import { createRouter, createWebHistory } from 'vue-router'
import PKIndexView from "@/views/pk/PKIndexView"
import RanklistIndexView from "@/views/ranklist/RanklistIndexView"
import RecordIndexView from "@/views/record/RecordIndexView"
import UserBotIndexView from "@/views/user/bot/UserBotIndexView"
import NotFound from "@/views/error/NotFound"
import UserAccountLoginView from "@/views/user/account/UserAccountLoginView"
import UserAccountRegisterView from "@/views/user/account/UserAccountRegisterView"
import store from '@/store'
//routes用来指定访问路径的资源
const routes = [
  {
    path: "/pk",
    name: "pk_index",
    component: PKIndexView,
    meta: {
      requestAuth: true,//访问该页面是否要授权
    }
  },
  {
    path: "/record",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requestAuth: true,//访问该页面是否要授权
    }
  },
  {
    path: "/ranklist",
    name: "ranklist_index",
    component: RanklistIndexView,
    meta: {
      requestAuth: true,//访问该页面是否要授权
    }
  },
  {
    path: "/user/bot",
    name: "user_bot_index",
    component: UserBotIndexView,
    meta: {
      requestAuth: true,//访问该页面是否要授权
    }
  },
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,//访问该页面是否要授权
    }
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,//访问该页面是否要授权
    }
  },
  {
    path: "/404",
    name: "404",
    component: NotFound,
    meta: {
      requestAuth: false,//访问该页面是否要授权
    }
  },
  {
    path: "/",
    name: "home",
    redirect: "/pk", //用来将根路径重定向到pk路径
    meta: {
      requestAuth: true,//访问该页面是否要授权
    }
  },
  {
    //用来将所有其它不合法页面重定向到404页面
    path: "/:catchAll(.*)",
    redirect: "/404",
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to,from,next)=>{
  if(to.meta.requestAuth && !store.state.user.is_login){//如果访问的页面需要授权并且未登录
    next({name:"user_account_login"});
  }else{
    next();
  }
})

export default router
