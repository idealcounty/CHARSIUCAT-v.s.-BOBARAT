<script setup lang="ts">
import {onMounted, ref} from "vue"
import { router } from '../../router'
import { getProductByProductId } from "../../api/product.ts";

const product_id = router.currentRoute.value.params.product_id
const productVO = ref()
const productId = ref('')
const productName = ref('')
const productAmount = ref('')
const productPrice = ref('')
const productDiscount = ref('')
const productDescription = ref('')
const productLogo = ref('')
const productImages = ref([])

const loadData = async () => {
  const res = await getProductByProductId(Number(product_id))
  productVO.value = res.data.result
  productId.value = productVO.value.productId
  productName.value = productVO.value.productName
  productAmount.value = productVO.value.productAmount
  productPrice.value = productVO.value.productPrice
  productDiscount.value = productVO.value.productDiscount
  productDescription.value = productVO.value.productDescription
  productLogo.value = productVO.value.productLogo
  productImages.value = productVO.value.productImages
  console.log(productVO.value)
}

onMounted(async () => {
  await loadData()
})
</script>

<template>
  <div class="app">
    <div class="app-background"></div>
    <!-- 导航栏 -->
    <div class="store-header">
      <div class="content">
        <div class="store_controls">
          <div class="cart_status_flex">
            <div class="store_header_btn_gray store-header_btn">
              <div class="store_header_btn_caps store_header_btn_leftcap"></div>
              <div class="store_header_btn_caps store_header_btn_rightcap"></div>
              <router-link class="store_header_btn_content" to="">
                愿望单（24)
              </router-link>
            </div>
          </div>
        </div>
        <div class="store_nav_area">
          <div class="store_nav_leftcap"></div>
          <div class="store_nav_bg">
            <div class="store_nav">
              <RouterLink class="store-nav-tab" to="">您的商店</RouterLink>
              <RouterLink class="store-nav-tab" to="">新鲜推荐</RouterLink>
              <RouterLink class="store-nav-tab" to="">类别</RouterLink>
              <RouterLink class="store-nav-tab" to="">点数商店</RouterLink>
              <RouterLink class="store-nav-tab" to="">新闻</RouterLink>
              <RouterLink class="store-nav-tab" to="">实验室</RouterLink>
            </div>
          </div>
          <div class="store_nav_rightcap"></div>
        </div>
      </div>
    </div>
    <!--标题-->
    <div class="page_title_area game_title_area page_content">
      <div class="apphub_HomeHeaderContent">
        <div class="apphub_HeaderStandardTop">
          <div id="appHubAppName" class="apphub_AppName">{{ productName }}</div>
        </div>
      </div>
    </div>
    <!--主体-->
    <div class="block game_media_and_summary_ctn">
      <div class="game_background_glow">
        <div class="block_content page_content">
          <div class="leftcol">

          </div>
          <div class="rightcol">

          </div>
          <div style="clear: both;"></div>
        </div>
      </div>
      <div class="queue_overflow_ctn">

      </div>
    </div>
  </div>
</template>

<style scoped>
.app {
  position: relative;
  display: flex;
  flex-direction: column;
  min-height: max(calc(100vh - 104px), 765px);
  font-family: "Motiva Sans", sans-serif;
  overflow: hidden;
}

.app-background {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  z-index: -2;
  height: 100%;
  background-image: url("../../assets/Steam_Summer_Sale_pattern-04-day_lighter.gif");
  background-repeat: repeat;
}

.store-header {
  background-color: transparent;
  padding-left: 0;
  padding-right: 0;
  min-width: 940px;
  margin-bottom: 16px;
}

.content {
  height: 66px;
  position: relative;
  width: 940px;
  margin: 0 auto;
  z-index: 300;
}

.store_controls {
  top: 8px;
  position: absolute;
  right: 0;
  top: 10px;
  text-align: right;
  z-index: 300;
  font-size: 11px;
}

.store_controls:hover .store_header_btn_gray {
  background: linear-gradient(to right, #e4ebf1, #c7d0da);;
}

.store_controls:hover .store_header_btn_content {
  color: #000000;
}

.cart_status_flex {
  display: flex;
}

.store_header_btn_gray {
  background-image: url( '../../assets/background_wishlist.jpg' );
  background-color: rgba( 255, 255, 255, 0.4 );
  background-position: -34px 20px;
}

.store_header_btn_content {
  display: inline-block;
  padding: 0 25px;
  margin: 0 1px;
  line-height: 20px;
  text-align: center;
  font-size: 11px;
  color: #ffffff;
}

.store_nav_area {
  position: absolute;
  left: 0;
  right: 0;
  top: 24px;
  height: 49px;
}

.store_nav_bg {
  background: linear-gradient(90deg, rgba(62, 103, 150, 0.919) 11.38%, rgba(58, 120, 177, 0.8) 25.23%, rgb(15, 33, 110) 100%);
  box-shadow: 0 0 3px rgba( 0, 0, 0, 0.4);
  height: 35px;
  margin: 7px 0;
}

.store_nav {
  height: 35px;
  display: flex;
}

.store-nav-tab {
  height: 35px;
  padding: 0 15px;
  color: #e5e5e5;
  font-size: 13px;
  font-weight: bold;
  font-family: Arial, Helvetica, sans-serif;
  text-decoration: none;
  text-shadow: 0 2px 3px rgba(0, 0, 0, 0.3);
  line-height: 35px;

  &:hover {
    color: #ffffff;
    background: linear-gradient(90deg, rgba(33, 162, 255, 0.25) 0%, rgba(33, 162, 255, 0.15) 50%, rgba(50, 50, 51, 0) 100%);
  }
}

.page_content {
  width: 940px;
  margin: 0 auto;
}

.apphub_HomeHeaderContent {
  padding-top: 0px;
  max_width: 948px;
  margin: 0 auto;
}

.apphub_HeaderStandardTop {
  background-image: none;
  padding-top: 0px;
  position: relative;
  margin: 0px auto;
  text-align: left;
  min-height: 46px;
  background-repeat: repeat-x;
}

.apphub_AppName {
  font-family: "Motiva Sans", sans-serif;
  font-weight: normal;
  padding-left: 0px;
  color: white;
  font-size: 26px;
  line-height: 32px;
  text-overflow: ellipsis;
}


</style>