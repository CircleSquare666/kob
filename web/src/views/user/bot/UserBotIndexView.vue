<template>
    <ContentField>
        我的Bot
    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField"
import $ from 'jquery'
import { useStore } from "vuex"

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        $.ajax({
            url: "http://localhost:3000/user/bot/add",
            type: "post",
            data: {
                title: "Bot的标题",
                // description: "Bot的描述",
                content: "import java.util.*;"
            },
            headers: {
                Authorization: "Wuhudsm" + store.state.user.token,
            },
            success(resp) {
                console.log(resp);
            },
            error(resp) {
                console.log(resp);
            }
        })

        // $.ajax({
        //     url: "http://localhost:3000/user/bot/remove",
        //     type: "delete",
        //     data: {
        //         bot_id: 3,
        //     },
        //     headers: {
        //         Authorization: "Wuhudsm" + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })

        // $.ajax({
        //     url: "http://localhost:3000/user/bot/update",
        //     type: "put",
        //     data: {
        //         bot_id: 4,
        //         title: "芜湖大司马",
        //         description: "韩金龙，人称芜湖大司马",
        //         content:"#include<bits/std++.h>",
        //     },
        //     headers: {
        //         Authorization: "Wuhudsm" + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })

        // $.ajax({
        //     url: "http://localhost:3000/user/bot/getlist",
        //     type: "get",
        //     headers: {
        //         Authorization: "Wuhudsm" + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })
    }
}


</script>
<style scoped>
</style>