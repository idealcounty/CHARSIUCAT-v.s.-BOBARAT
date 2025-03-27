<script setup lang="ts">
import { ref } from "vue"
import { getStoreById } from "../api/store.ts"

// 使用props接收父界面传来的数据
const props = defineProps({
  storeId: {
    type: Number,
    required: true
  }
})

const name = ref('')
const logoUrl = ref('')
const location = ref('')

initInfo(props.storeId);

function initInfo(storeId: number) {
  getStoreById(storeId).then(res => {
    name.value = res.data.result.name
    logoUrl.value = res.data.result.logoUrl
    location.value = res.data.result.location
  })
}

</script>


<template>
  <el-card class="store-item-card" :body-style="{ padding: '0px' }" shadow="hover">
    <el-image class="logo-image" :src="logoUrl" />

    <div style="padding: 14px">
      <h1> {{ name }} </h1>
      <el-descriptions :column="1">
        <el-descriptions-item style="font-size: 10px" label="地址">
          {{ location }}
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </el-card>
</template>


<style scoped>
.store-item-card {
  margin: 20px;
  border-radius: 8px;
  min-width: 25%;
  max-width: 30px;
}

.logo-image {
  height: 250px;
  display: block;
}
</style>
