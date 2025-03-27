<script setup lang="ts">

</script>


<template>
  <el-main class="main-frame">
    <el-result
        icon="error"
        title="错误"
        sub-title="您访问的页面不存在"
    >
      <template #extra>
        <router-link to="/home">
          <el-button type="primary">回到主页</el-button>
        </router-link>
      </template>
    </el-result>
  </el-main>
</template>


<style scoped>
.main-frame {
  width: 100%;
  height: 100%;

  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
