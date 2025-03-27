<script setup lang="ts">
import { ref } from "vue"
import { router } from '../../router'
import { getStoreById } from "../../api/store.ts"
import { getProductsByStoreId } from "../../api/product.ts"
import { Back } from "@element-plus/icons-vue"
import ProductItem from "../../components/ProductItem.vue"

const role = sessionStorage.getItem("role")

const storeId = router.currentRoute.value.params.storeId
const actualStoreId = sessionStorage.getItem("storeId")
const storeVO = ref()
const name = ref('')
const logoUrl = ref('')
const location = ref('')
const productList = ref()

getStoreById(Number(storeId)).then(res => {
  storeVO.value = res.data.result
  name.value = storeVO.value.name
  logoUrl.value = storeVO.value.logoUrl
  location.value = storeVO.value.location
})

getProductsByStoreId(Number(storeId)).then(res => {
  productList.value = res.data.result
})

function JumpToCreateProduct() {
  router.push("/CreateProduct")
}

function ReturnToAllStore() {
  router.push({ path: "/AllStore" });
}
</script>

<template>
  <el-container>
    <el-aside width="25%" class="page-aside">
      <div class="store-detail-main">
        <el-button @click="ReturnToAllStore" type="primary" circle plain class="back-button">
          <el-icon>
            <Back />
          </el-icon>
        </el-button>

        <el-image class="logo-image" :src="logoUrl" />

        <p class="store-title">{{ name }}</p>

        <el-descriptions :column="1">
          <el-descriptions-item style="font-size: 10px" label="地址">
            {{ location }}
          </el-descriptions-item>
        </el-descriptions>

        <el-button class="create-product-button" type="primary" plain @click="JumpToCreateProduct"
                   v-if="role === 'STAFF' && storeId === actualStoreId">新增商品
        </el-button>
      </div>
    </el-aside>

    <el-main>
      <div>
        <span class="product-list-title">商品列表</span>
      </div>
      <div>
        <el-scrollbar max-height="750px" always>
          <div class="all-product-main">
            <ProductItem class="product-item-list" v-for="productVO in productList"
                         :productId="productVO.id"/>
          </div>
        </el-scrollbar>
      </div>
    </el-main>
  </el-container>

</template>


<style scoped>
.page-aside {
  border-right: lightgrey solid 1px;
}

.back-button {
  margin-top: 20px;
  margin-bottom: 20px;
}

.store-title {
  font-size: 30px;
  margin-bottom: 20px;
}

.store-detail-main {
  width: 80%;
  margin-left: 10%;
  display: flex;
  flex-direction: column;
  justify-content: start;
}

.logo-image {
  width: 100%;
}

.create-product-button {
  margin-top: 20px;
}

.product-list-title {
  font-size: 30px;
  margin-top: 20px;
  margin-bottom: 20px;
  margin-left: 20px;
}

.all-product-main {
  display: flex;
  flex-direction: row;
  padding: 20px;
  flex-flow: wrap;
  align-content: start;
  justify-content: start;
}
</style>
