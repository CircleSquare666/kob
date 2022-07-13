<!-- 将公共的card区域单独拎出来做一个一个模块 -->
<template>
    <div class="containner content-field">
        <div class="card">
            <div class="card-body">
                <!-- slot放以后要填充的内容 -->
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script>

</script>

<style scoped>
div.content-field{
    margin-top: 20px;
}
</style>