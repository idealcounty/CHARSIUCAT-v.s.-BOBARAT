<script setup lang="ts">
import { router } from "../../router";
import { computed,ref } from "vue";
import { Back, UploadFilled } from "@element-plus/icons-vue"
import { createStore } from "../../api/store.ts"
import { uploadImage } from "../../api/tools.ts"

const name = ref('')
const location = ref('')
const imageFileList = ref([])
const logoUrl = ref('')

const hasNameInput = computed(() => name.value != '')
const hasLocationInput = computed(() => location.value != '')
const hasImageFile = computed(() => logoUrl.value != '')
const createDisabled = computed(() => {
  return !(hasNameInput.value && hasLocationInput.value && hasImageFile.value)
})

function handleCreateStore() {
  const payload = {
    name: name.value,
    location: location.value,
    logoUrl: logoUrl.value
  };
  createStore(payload).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        message: '添加商户成功！',
        type: 'success',
        center: true,
      })
      name.value = ''
      location.value = ''
      logoUrl.value = ''
      imageFileList.value.splice(0)
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

function updatePhoto(file: any, fileList: any) {
  imageFileList.value = fileList
  let formData = new FormData()
  formData.append('file', file.raw)
  uploadImage(formData).then(res => {
    console.log(res)
    if (res.data && res.data.result) {
      logoUrl.value = res.data.result
      ElMessage.success("图片上传成功！")
    } else {
      ElMessage.error("图片上传失败！")
    }
  })
}

function handleExceed() {
  ElMessage.warning(`当前限制选择 1 个文件`);
}

function uploadHttpRequest() {
  return new XMLHttpRequest()
}

function ReturnToAllStore() {
  router.push("/AllStore")
}
</script>

<template>
  <el-main>
    <el-card>
    <el-button @click="ReturnToAllStore" type="primary" circle plain><el-icon><Back /></el-icon></el-button>
    <h1 class="create-store-title">新建商店</h1>
    <el-form label-position="left" label-width="90px" size="large" class="create-store-form">

      <el-form-item label="商店名">
        <el-input id="name" v-model="name" required placeholder="请输入商店名" />
      </el-form-item>

      <el-form-item label="商店地址">
        <el-input id="location" v-model="location" required placeholder="楼层-门牌号 如：3楼-305" />
      </el-form-item>

      <el-form-item label="商店Logo">
        <el-upload v-model:file-list="imageFileList" :limit="1" :on-change="updatePhoto"
                   :on-exceed="handleExceed"  class="upload-demo" list-type="picture"
                   :http-request="uploadHttpRequest" drag>
          <el-icon class="el-icon--upload">
            <upload-filled />
          </el-icon>
          <div class="el-upload__text">将文件拖到此处或单击此处上传。仅允许上传一份文件。</div>
        </el-upload>
      </el-form-item>

      <el-form-item>
        <el-button @click.prevent="handleCreateStore()" :disabled="createDisabled" type="primary" plain>
          创建商店
        </el-button>
      </el-form-item>
    </el-form>
    </el-card>
  </el-main>
</template>

<style scoped>
.create-store-title {
  margin-left: 25%;
}

.create-store-form {
  margin-left: 25%;
  width: 50%;
}
</style>