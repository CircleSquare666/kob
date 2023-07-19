<template>
  <Nav-bar/>
  <router-view></router-view>
</template>

<script>
import NavBar from '@/components/NavBar.vue' //导入组件
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

export default{
  components:{
    NavBar //想用这个组件的话，就要放到components
  }
}
</script>

<style>
body {
  /* @/表示当前目录的根目录 */
  background-image: url("@/assets/images/background.jpg");
  background-size: cover;
}
</style>
