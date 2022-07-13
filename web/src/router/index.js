import { createRouter, createWebHistory } from 'vue-router'
import PKIndexView from "@/views/pk/PKIndexView"
import RanklistIndexView from "@/views/ranklist/RanklistIndexView"
import RecordIndexView from "@/views/record/RecordIndexView"
import UserBotIndexView from "@/views/user/bot/UserBotIndexView"
import NotFound from "@/views/error/NotFound"

//routes用来指定访问路径的资源
const routes = [
  {
    path:"/pk",
    name:"pk_index",
    component: PKIndexView,
  },
  {
    path:"/record",
    name:"record_index",
    component: RecordIndexView,
  },{
    path:"/ranklist",
    name:"ranklist_index",
    component: RanklistIndexView,
  },{
    path:"/user/bot",
    name:"user_bot_index",
    component: UserBotIndexView,
  },{
    path:"/404",
    name:"404",
    component: NotFound,
  },
  {
    //用来将根路径重定向到pk路径
    path:"/",
    name:"home",
    redirect:"/pk",
  },
  {
    //用来将所有其它不合法页面重定向到404页面
    path: "/:catchAll(.*)",
    redirect:"/404",
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
