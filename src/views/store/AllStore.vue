<script setup lang="ts">
import { ref } from "vue"
import { router } from '../../router'
import { getAllStore } from "../../api/store.ts"
import StoreItem from "../../components/StoreItem.vue"
import {Back} from "@element-plus/icons-vue";

const role = sessionStorage.getItem("role")

const storeList = ref()

// 获取商店列表
getAllStore().then(res => {
  storeList.value = res.data.result
})

function ReturnToDashBoard() {
  router.push({ path: "/dashboard" });
}

function JumpToCreateStore() {
  router.push({path: "/CreateStore"})
}

function JumpToStoreDetail(storeId: number) {
  router.push({name:"StoreDetail",params:{storeId:storeId}});
}
</script>

<template>
  <el-main>
    <el-button @click="ReturnToDashBoard" type="primary" circle plain>
      <el-icon>
        <Back />
      </el-icon>
    </el-button>

    <div v-if="role === 'MANAGER'">
      <el-button class="add-store-button" type="primary" plain @click="JumpToCreateStore">
        添加商店
      </el-button>
    </div>
    <div>
      <el-scrollbar max-height="750px" always>
        <div class="store-item-list">
          <StoreItem v-for="storeVO in storeList" :storeId="storeVO.id"
                     @click="JumpToStoreDetail(storeVO.id)" />
        </div>
      </el-scrollbar>
    </div>
  </el-main>
</template>

<style scoped>
.add-store-button {
  margin-left: 30px;
}

.store-item-list {
  display: flex;
  padding: 2px;
  flex-flow: wrap;
  justify-content: center;
  align-content: start;
}
</style>