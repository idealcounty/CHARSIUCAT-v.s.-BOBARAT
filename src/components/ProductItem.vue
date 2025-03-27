<script setup lang="ts">
import { ref } from "vue"
import { getProductById, getRatingInfoById } from "../api/product.ts"

// 使用props接收父界面传来的数据
const props = defineProps({
  productId: {
    type: Number,
    required: true
  }
})

const storeId = ref(0)
const name = ref('')
const stock = ref(0)
const price = ref()

const rating = ref(0)
const numRated = ref(0)

initInfo(props.productId);

function initInfo(productId: number) {
  getProductById(productId).then(res => {
    storeId.value = res.data.result.storeId
    name.value = res.data.result.name
    stock.value = res.data.result.stock
    price.value = res.data.result.price
  })
  getRatingInfoById(productId).then(res => {
    rating.value = res.data.result.rating
    numRated.value = res.data.result.numRated
  })
}
</script>

<template>
  <el-card class="product-item-card" shadow="hover">
    <div style="padding: 14px">
      <h1> {{ name }} </h1>
      <el-descriptions :column="1">
        <el-descriptions-item style="font-size: 10px" label="价格">
          {{ price }} 元
        </el-descriptions-item>
        <el-descriptions-item style="font-size: 10px" label="库存">
          {{ stock }} 件
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </el-card>
</template>


<style scoped>
.product-item-card {
  margin: 8px;
  border-radius: 8px;
  min-width: 31%;
}
</style>
