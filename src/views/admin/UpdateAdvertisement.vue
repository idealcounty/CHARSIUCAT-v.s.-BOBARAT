<script setup lang="ts">
import {ref, computed, onMounted} from 'vue'
import {uploadImage} from "../../api/tools.ts";
import {router} from '../../router'
import { getAdvertisementByAdvertisementId, updateAdvertisementByAdvertisementId } from "../../api/advertisement.ts";
import {getAllProducts, ProductInfo} from "../../api/product.ts";

const advertisementId = router.currentRoute.value.params.advertisement_id
const title = ref('')
const content = ref('')
const imageUrl = ref('')
const productId = ref(0)
const imageUrlList = ref([])
const productList = ref<ProductInfo[]>([])
const advertisementVO = ref()

const loadData = async () => {
  const res = await getAdvertisementByAdvertisementId(Number(advertisementId))
  advertisementVO.value = res.data.result
  title.value = advertisementVO.value.advertisementTitle
  content.value = advertisementVO.value.advertisementContent
  imageUrl.value = advertisementVO.value.advertisementImageUrl
  productId.value = advertisementVO.value.productId
}

onMounted(async () => {
  await loadData()
  console.log(imageUrl.value)
})

getAllProducts().then(res => {
  productList.value = res.data.result
})

const hasTitleInput = computed(() => title.value != '')
const hasContentInput = computed(() => content.value != '')
const hasImageUrl = computed(() => imageUrlList.value[0] != '')
const hasProductIdInput = computed(() => productId.value != 0)

const updateDisabled = computed(() => {
  return !(hasTitleInput && hasContentInput.value && hasImageUrl.value && hasProductIdInput.value);
})

const loading = ref(false)
const imageIsUpdated = ref(false)

async function generateImageUrl() {
  let formData = new FormData()
  formData.append('file', imageUrlList.value[0].raw)
  const imageResponse = await uploadImage(formData);
  imageUrl.value = imageResponse.data.result;
  console.log(imageUrl.value)
}

// 更新按钮触发
async function handleUpdateAdvertisement() {
  if (imageIsUpdated.value) {
    await generateImageUrl()
  }
  console.log(imageUrl.value)
  await updateAdvertisementByAdvertisementId(Number(advertisementId), {
    advertisementTitle: title.value,
    advertisementContent: content.value,
    advertisementImageUrl: imageUrl.value,
    productId: productId.value,
  }).then(res => {
    if (res.data.code === '000') {  //类型守卫，它检查 res.data 对象中是否存在名为 code 的属性
      ElMessage({
        message: "更新成功！",
        type: 'success',
        center: true,
      })
      router.push({path:'./admin'})
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

function handleUpdateImage() {
  imageUrlList.value = []
  imageUrl.value = ''
  imageIsUpdated.value = true
}

function uploadHttpRequest() {
  return new XMLHttpRequest()
}

function handleChangeImage(file: any, fileList: any) {
  imageUrlList.value = fileList
}

function handleRemoveImage(file: any, fileList: any) {
  imageUrlList.value = fileList
}

function handleExceedImage() {
  ElMessage.warning(`仅允许上传一张图片`);
}
</script>

<template>
  <div class="app">
    <div class="create">
      <div class="create-content">
        <div class="create-title">
          更新商品信息
        </div>

        <div class="title">
          <div class="label">
            广告标题
          </div>
          <div class="input-area">
            <input v-model="title" class="input">
          </div>
        </div>

        <div class="content">
          <div class="label">
            广告内容
          </div>
          <div class="input-area">
            <input v-model="content" class="input">
          </div>
        </div>

        <div class="price">
          <div class="label">
            推广商品
          </div>
          <div class="input-area">
            <select v-model="productId"
                    class="select">
              <option
                  v-for="(product, index) in productList"
                  :key="index"
                  :value="product.productId"
              >
                {{ product.productName }}
              </option>
            </select>
          </div>
        </div>

        <div class="logo">
          <div class="label">
            广告图片
          </div>
          <div class="input-area" v-if="imageIsUpdated">
            <el-upload
                class="upload"
                drag
                multiple
                :http-request="uploadHttpRequest"
                :on-change="handleChangeImage"
                :on-remove="handleRemoveImage"
                :limit="1"
                :on-exceed="handleExceedImage"
                :file-list="imageUrlList">

              <div class="el-upload__text">
                Drop file here or <em>click to upload</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  jpg/png files
                </div>
              </template>
            </el-upload>
          </div>
          <div class="update-images-confirm" v-else>
            <div v-loading="loading" class="update-button" @click="handleUpdateImage">
              修改广告图片
            </div>
          </div>
        </div>

        <div v-loading="loading" class="update-button" @click="handleUpdateAdvertisement">
          完成
        </div>

      </div>
    </div>
  </div>

</template>


<style scoped>
.app {
  background-color: #1f2428;
  min-height: calc(100vh);
  font-family: "Motiva Sans", sans-serif;
}

.create {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  row-gap: 32px;
  padding: 80px 0 150px 0;
  background-image: url("../../assets/acct_creation_bg.png");
  background-position: left top;
  background-repeat: no-repeat;
  background-size: cover;
}

.create-content {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 25px;
  width: 800px;
  padding: 0 36px;
  margin: 0 auto;
}

.create-title {
  margin-bottom: 10px;
  color: #ffffff;
  font-size: 32px;
  font-weight: 200;
}

.label {
  margin-bottom: 2px;
  color: #b8b6b4;
  font-size: 14px;
}

.input-area {
  position: relative;
  display: inline-block;
}

.input {
  box-sizing: border-box;
  width: 300px;
  padding: 8px;
  border: none;
  border-radius: 2px;
  color: #ffffff;
  font-size: 15px;
  font-family: Arial, sans-serif;
  background-color: #32353c;

  &:hover {
    background-color: #393c44;
  }

  &.error {
    outline: 1px solid #c15755;
  }
}

.upload {
  box-sizing: border-box;
  width: 300px;
  padding: 8px;
  border: none;
  border-radius: 2px;
  color: #ffffff;
  font-size: 15px;
  font-family: Arial, sans-serif;
  background-color: #32353c;
}

:deep(.el-upload-dragger) {
  background: #32353c;
}

:deep(.el-upload__text) {
  color: #b8b6b4;
}

.update-button {
  box-sizing: border-box;
  width: 130px;
  border-radius: 2px;
  margin-top: 10px;
  color: #ffffff;
  font-size: 15px;
  line-height: 36px;
  text-align: center;
  background: linear-gradient(90deg, #06BFFF 0%, #2D73FF 100%);
  cursor: pointer;

  &:hover {
    background: linear-gradient(90deg, #06BFFF 30%, #2D73FF 100%);
  }
}

.select {
  background-color: #32353c;
  color: #b8b6b4;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 3px;
  box-shadow: 1px 1px 0px #000;
  width: 300px;
  padding: 8px;
}
</style>