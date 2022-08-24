import { createStore } from 'vuex'
import ModuleUser from '@/store/user'
import ModulePk from '@/store/pk'//用Module将全局变量分类
export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    pk: ModulePk,
  }
})
