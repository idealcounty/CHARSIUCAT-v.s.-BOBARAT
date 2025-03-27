<script setup lang="ts">
import { ref, computed } from "vue"
import { router } from '../../router'
import { Back } from "@element-plus/icons-vue"
import { createProduct } from "../../api/product.ts"
const storeId = Number(sessionStorage.getItem("storeId"))
const name = ref('')
const price = ref()
const stock = ref()

const hasNameInput = computed(() => name.value != '')
const hasPriceInput = computed(() => price.value != '')
const hasStockInput = computed(() => stock.value != null)
const createProductDisabled = computed(() => {
  return !(hasNameInput.value && hasPriceInput.value && hasStockInput.value);
})

function handleCreateProduct() {
  const payload = {
    storeId: storeId,
    name: name.value,
    stock: stock.value,
    price: price.value,
  };
  createProduct(payload).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        message: '添加商品成功！',
        type: 'success',
        center: true,
      })
      name.value = ''
      stock.value = ''
      price.value = ''
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

function ReturnToStoreDetail(storeId: number) {
  router.push({name:"StoreDetail" ,params:{storeId:storeId}})
}
</script>

<template>
  <el-main>
    <el-card>
    <el-button @click="ReturnToStoreDetail(storeId)" type="primary" circle plain>
      <el-icon>
        <Back />
      </el-icon>
    </el-button>

    <h1 class="create-product-title">新建商品</h1>

    <el-form label-position="left" label-width="90px" size="large" class="create-product-form">
      <el-form-item label="商品名">
        <el-input id="name" v-model="name" required placeholder="请输入商品名" />
      </el-form-item>

      <el-form-item label="单价">
        <el-input id="price" v-model="price" required placeholder="请输入商品单价，单位（元）" />
      </el-form-item>

      <el-form-item label="库存">
        <el-input id="stock" v-model="stock" required placeholder="请输入商品库存，单位（件）" />
      </el-form-item>

      <el-form-item>
        <el-button @click.prevent="handleCreateProduct()" :disabled="createProductDisabled" type="primary"
                  plain>
          创建商品
        </el-button>
      </el-form-item>
    </el-form>
    </el-card>
  </el-main>
</template>

<style scoped>
.create-product-title {
  margin-left: 25%;
}

.create-product-form {
  margin-left: 25%;
  width: 50%;
}
</style>