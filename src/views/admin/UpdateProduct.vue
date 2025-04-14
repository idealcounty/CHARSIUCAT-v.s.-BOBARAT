<script setup lang="ts">
import {ref, computed, onMounted} from 'vue'
import {uploadImage} from "../../api/tools.ts";
import {router} from '../../router'
import {getProductByProductId, updateProductByProductId} from "../../api/product.ts";

const productId = router.currentRoute.value.params.product_id
const productVO = ref()
const name = ref('')
const amount = ref()
const price = ref()
const discount = ref()
const description = ref('')
const logoUrl = ref('')

const imageUrlList = ref([])
const rawImages = ref([])
const logoUrlList = ref([])

const loadData = async () => {
  const res = await getProductByProductId(Number(productId))
  productVO.value = res.data.result
  name.value = productVO.value.productName
  amount.value = productVO.value.productAmount
  price.value = productVO.value.productPrice
  discount.value = productVO.value.productDiscount
  description.value = productVO.value.productDescription
  logoUrl.value = productVO.value.productLogo
  imageUrlList.value = productVO.value.productImages

}

onMounted(async () => {
  await loadData()
  console.log(logoUrl.value)
  console.log(imageUrlList.value)
})



// 商品名称是否为空
const hasNameInput = computed(() => name.value != '')
// 商品数量是否为空
const hasAmountInput = computed(() => amount.value != '')
// 商品价格是否为空
const hasPriceInput = computed(() => price.value != '')
// 商品描述是否为空
const hasDescriptionInput = computed(() => description.value != '')
// 商品logo是否为空
const hasLogoUrl = computed(() => logoUrlList.value != '')
// 商品图片是否达到4张
const hasImageUrl = computed(() => rawImages.value.length != 4)

// 创建按钮可用性
const updateDisabled = computed(() => {
  return !(hasNameInput.value && hasAmountInput && hasPriceInput.value && hasDescriptionInput.value && hasLogoUrl.value && hasImageUrl.value);
})

const loading = ref(false)

const logoIsUpdated = ref(false)
const imagesAreUpdated = ref(false)

async function generateLogoUrl() {
  let formData = new FormData()
  formData.append('file', logoUrlList.value[0].raw)
  const imageResponse = await uploadImage(formData);
  logoUrl.value = imageResponse.data.result;
  console.log(logoUrl.value)
}

async function generateImageUrls() {
  for (let i = 0; i < rawImages.value.length; i++) {
    let formData = new FormData()
    formData.append('file', rawImages.value[i].raw)
    const imageResponse = await uploadImage(formData);
    imageUrlList.value.push(imageResponse.data.result);
  }
  console.log(imageUrlList.value)
}

// 更新按钮触发
async function handleUpdateProduct() {
  // if (updateDisabled.value) {
  //   return
  // }
  if (logoIsUpdated.value) {
    await generateLogoUrl()
  }
  if (imagesAreUpdated.value) {
    await generateImageUrls()
  }
  console.log(logoUrl.value)
  await updateProductByProductId(Number(productId), {
    productName: name.value,
    productAmount: amount.value,
    productPrice: price.value,
    productDiscount: discount.value,
    productDescription: description.value,
    productLogo: logoUrl.value,
    productImages: imageUrlList.value,
  }).then(res => {
    if (res.data.code === '000') {  //类型守卫，它检查 res.data 对象中是否存在名为 code 的属性
      ElMessage({
        message: "更新成功！",
        type: 'success',
        center: true,
      })
      // router.push({path: "/admin"})
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

function handleUpdateLogo() {
  logoUrlList.value = []
  logoUrl.value = ''
  logoIsUpdated.value = true
}

function handleUpdateImages() {
  imageUrlList.value = []
  imagesAreUpdated.value = true
}

function uploadHttpRequest() {
  return new XMLHttpRequest()
}

function handleChangeLogo(file: any, fileList: any) {
  logoUrlList.value = fileList
  // console.log(logoUrlList.value)
}

function handleRemoveLogo(file: any, fileList: any) {
  logoUrlList.value = fileList
  // console.log(logoUrlList.value)
}

function handleExceedLogo() {
  ElMessage.warning(`仅允许上传一张图片`);
}

function handleChangeImages(file: any, fileList: any) {
  rawImages.value = fileList
  // console.log(rawImages.value)
}

function handleRemoveImages(file: any, fileList: any) {
  rawImages.value = fileList
  // console.log(rawImages.value)
}

function handleExceedImages() {
  ElMessage.warning(`最多上传四张图片`);
}


</script>

<template>
  <div class="app">
    <div class="create">
      <div class="create-content">
        <div class="create-title">
          更新商品信息
        </div>


        <div class="name">
          <div class="label">
            商品名称
          </div>
          <div class="input-area">
            <input v-model="name" class="input">
          </div>
        </div>


        <div class="amount">
          <div class="label">
            商品数量
          </div>
          <div class="input-area">
            <input v-model="amount" class="input" oninput="value=value.replace(/\D/g,'')">
          </div>
        </div>

        <div class="price">
          <div class="label">
            商品价格
          </div>
          <div class="input-area">
            <input v-model="price" class="input" oninput="value=value.replace(/\D/g,'')">
          </div>
        </div>

        <div class="price">
          <div class="label">
            商品折扣
          </div>
          <div class="input-area">
            <input v-model="discount" class="input" oninput="value=value.replace(/\D/g,'');if(value>100)value=100">
          </div>
        </div>

        <div class="description">
          <div class="label">
            商品描述
          </div>
          <div class="input-area">
            <input v-model="description" class="input">
          </div>
        </div>

        <div class="logo">
          <div class="label">
            商品logo
          </div>
          <div class="input-area" v-if="logoIsUpdated">
            <el-upload
                class="upload"
                drag
                multiple
                :http-request="uploadHttpRequest"
                :on-change="handleChangeLogo"
                :on-remove="handleRemoveLogo"
                :limit="1"
                :on-exceed="handleExceedLogo"
                :file-list="logoUrlList">

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
            <div v-loading="loading" class="update-button" @click="handleUpdateLogo">
              修改logo
            </div>
          </div>
        </div>

        <div class="images">
          <div class="label">
            商品图片
          </div>
          <div class="input-area" v-if="imagesAreUpdated">
            <el-upload
                class="upload"
                drag
                multiple
                :http-request="uploadHttpRequest"
                :on-change="handleChangeImages"
                :on-remove="handleRemoveImages"
                :limit="4"
                :on-exceed="handleExceedImages"
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
            <div v-loading="loading" class="update-button" @click="handleUpdateImages">
              修改图片
            </div>
          </div>
        </div>

        <div v-loading="loading" class="update-button" @click="handleUpdateProduct">
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

.error-warn {
  color: red;
  margin-bottom: 2px;
  font-size: 14px;
}

</style>