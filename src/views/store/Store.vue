<script setup lang="ts">
import { ref } from 'vue'
import { getAllProducts, ProductInfo } from "../../api/product.ts";
import { AdvertisementInfo,getAllAdvertisements } from "../../api/advertisement.ts";

const currentHour = ref(new Date().getHours())
const activeTab = ref(0)
const activeScreenshot = ref(-1)
const productList = ref<ProductInfo[]>([])
const advertisementList = ref<AdvertisementInfo[]>([])
const advertisementListsz = ref(0)
const current = ref(0)

getAllAdvertisements().then(res => {
  advertisementList.value = res.data.result
  advertisementListsz.value = advertisementList.value.length
})

getAllProducts().then(res => {
  productList.value = res.data.result
})
</script>

<template>
  <div class="app">
    <div class="app-background"></div>
    <div class="store-background"
         :class="{ 'store-background_day': currentHour >= 6 && currentHour < 18, 'store-background_night': currentHour < 6 || currentHour >= 18 }"></div>
    <!-- 导航栏 -->
    <div class="store-header">
      <div class="content">
        <div class="store_controls">
          <div class="cart_status_flex">
            <div class="store_header_btn_gray store-header_btn">
              <div class="store_header_btn_caps store_header_btn_leftcap"></div>
              <div class="store_header_btn_caps store_header_btn_rightcap"></div>
              <router-link class="store_header_btn_content" to="/wishlist">
                愿望单（24）
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
    <!-- 浏览SBEAM -->
    <div class="store-section">
      <div class="store-section-title">浏览 SBEAM</div>
      <div class="browses">
        <RouterLink class="browse" to="">新品</RouterLink>
        <RouterLink class="browse" to="">优惠</RouterLink>
        <RouterLink class="browse" to="">免费游戏</RouterLink>
        <RouterLink class="browse" to="">按用户标签</RouterLink>
      </div>
    </div>
    <!-- 精选和推荐 -->
    <div class="home_cluster_ctn home_ctn">
      <div class="home_page_content">
        <h2 class="home_page_content_title">精选与推荐</h2>
        <div class="carousel_container maincap">
          <div class="carousel_items reponsive_scroll_snap_ctn">
            <router-link
                :to="{ name:'detail',params:{ product_id:advertisementList[current].productId }}"
                class="store_main_capsule responsive_scroll_snap_start broadcast_capsule app_impression_tracked"
            >
              <div class="capsule main_capsule">
                <img v-if="activeScreenshot == -1" class="screenshot" :src="advertisementList[current].advertisementImageUrl">
                <img v-else class="screenshot" :src="productList[advertisementList[current].productId-1].productImages[activeScreenshot]">
              </div>
              <div class="info">
                <div class="app_name">
                  <div>{{ productList[advertisementList[current].productId-1].productName }}</div>
                </div>
                <div class="screenshots">
                  <div
                      v-for="(img, index) in productList[advertisementList[current].productId-1].productImages"
                      :key="index"
                      @mouseenter="activeScreenshot = index"
                      @mouseleave="activeScreenshot = -1"
                  >
                    <img :src="img">
                  </div>
                </div>
                <div class="reason">{{ advertisementList[current].advertisementContent }}</div>
                <div v-if="productList[advertisementList[current].productId-1].productDiscount == 0">
                  <div class="discount_block_ca discount_block_inline_ca">
                    <div class="discount_prices_ca">
                      <div class="discount_final_price_ca_nodiscount">¥ {{ (productList[advertisementList[current].productId-1].productPrice * (1 - productList[advertisementList[current].productId-1].productDiscount / 100)).toFixed(2) }}</div>
                    </div>
                  </div>
                </div>
                <div v-else>
                  <div class="discount_block_ca discount_block_inline_ca" style="display: flex; ">
                    <div class="discount_pct_ca">-{{ productList[advertisementList[current].productId-1].productDiscount }}%</div>
                    <div class="discount_prices_cap">
                      <div class="discount_original_price_ca">¥ {{ productList[advertisementList[current].productId-1].productPrice.toFixed(2) }}</div>
                      <div class="discount_final_price_ca">¥ {{ (productList[advertisementList[current].productId-1].productPrice * (1 - productList[advertisementList[current].productId-1].productDiscount / 100)).toFixed(2) }}</div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="ds_options"></div>
            </router-link>
          </div>
          <div class="carousel_thumbs">
            <div
                v-for="(item, index) in advertisementList"
                :key="index"
                @click = "current = index"
            >
              <div
                  v-if="current != index"
                  class="thumbs"
              ></div>
              <div
                  v-if="current == index"
                  class="thumbs_focus"
              ></div>
            </div>
          </div>
          <div class="arrow left" @click="current = (current + advertisementListsz - 1) % advertisementListsz">
            <div class="left_arrow"></div>
          </div>
          <div class="arrow right" @click="current = (current + 1) % advertisementListsz">
            <div class="right_arrow"></div>
          </div>
        </div>
      </div>
    </div>
    <!-- 商品列表 -->
    <div class="home_ctn tab_container" style="overflow: visible;">
      <div class="home_page_content home_tabs_row_ctn">
        <div class="store_horizontal_minislider_ctn" style="height: 31px;">
          <div class="home_tabs_row store_horizontal_minislider">
            <div class="home_tab">
              <div class="tab_content">商品列表</div>
            </div>
          </div>
        </div>
      </div>
      <div class="home_page_content flex_cols">
        <div class="home_leftcol home_tab_col">
          <div class="home_tabs_content">
            <router-link
              v-for="(product, index) in productList"
              :key="index"
              class="tab_item"
              @mouseenter="activeTab = index"
              :to="{ name:'detail',params:{ product_id:product.productId }}"
            >
              <div class="tab_item_cap">
                <img class="tab_item_cap_img" :src="product.productLogo">
              </div>
              <div class="discount_block tab_item_discount">
                <div class="discount_pct">-{{ product.productDiscount }}%</div>
                <div class="discount_prices">
                  <div class="discount_original_price">¥{{ (product.productPrice).toFixed(2) }}</div>
                  <div class="discount_final_price">¥{{ (product.productPrice * (1 - product.productDiscount / 100)).toFixed(2) }}</div>
                </div>
              </div>
              <div class="tab_item_content">
                <div class="tab_item_name">{{ product.productName }}</div>
              </div>
              <div style="clear: both;"></div>
              <div class="ds_options"><div></div></div>
            </router-link>
          </div>
        </div>
        <div class="home_rightcol">
          <div class="tab_preview_container">
            <div class="tab_preview">
              <h2 class="tab_preview_title" v-if="productList && productList[activeTab]">{{ productList[activeTab].productName }}</h2>
              <img
                  v-if="productList && productList[activeTab]"
                  v-for="(img, i) in productList[activeTab].productImages"
                  :key="i"
                  class="screenshot"
                  :src="img"
              />
            </div>
          </div>
        </div>
        <div style="clear: both;"></div>
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
  background: linear-gradient(to bottom, #1e3c57, #192534);
}

.store-background {
  position: absolute;
  left: 0;
  right: 0;
  height: 765px;
  background-position: center;
  background-repeat: no-repeat;
  z-index: -1;
}

.store-background_day {
  background-image: url("../../assets/home_header_bg_day_schinese.gif");
}

.store-background_night {
  background-image: url("../../assets/home_header_bg_night_schinese.gif");
}

.store-header {
  display: flex;
  flex-direction: column;
  width: 940px;
  margin: 7px auto 550px auto;
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
  text-decoration: none;
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

.search {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 202px;
  box-sizing: border-box;
  padding-left: 12px;
  border: 1px solid rgba(0, 0, 0, 0.3);
  border-radius: 3px;
  margin: 0 4px 0 auto;
  background-color: #316282;

  &:hover {
    color: #0e1c25;
    border-color: #4c9acc;
  }
}

.search-input {
  width: 0;
  flex-grow: 1;
  box-sizing: border-box;
  margin-right: 8px;
  border: none;
  outline: none;
  background: none;
  color: #ffffff;
  font-size: 13px;

  &:focus::placeholder {
    opacity: 0;
  }

  &::placeholder {
    color: #0e1c25;
    font-size: 14px;
    font-style: italic;
  }
}

.search-button {
  position: relative;
  flex-shrink: 0;
  width: 25px;
  height: 25px;
  margin: 2px;
  border-radius: 3px;
  overflow: hidden;
  background-image: url("../../assets/search_icon_btn_over.png");
  background-size: cover;

  &:hover::before {
    opacity: 0;
  }

  &::before {
    content: "";
    position: absolute;
    left: 0;
    top: 0;
    z-index: 1;
    width: 100%;
    height: 100%;
    background-color: #316282;
    background-image: url("../../assets/search_icon_btn.png");
    background-size: cover;
  }
}

.search-suggestions {
  position: absolute;
  right: 0;
  top: 100%;
  box-shadow: 0 0 12px #000000;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s;
}

.search-input:focus ~ .search-suggestions {
  opacity: 1;
  pointer-events: auto;
}

.search-suggestion {
  width: 400px;
  display: inline-flex;
  gap: 8px;
  padding: 4px 8px;
  color: #f5f5f5;
  background-color: #3d4450;
  text-decoration: none;

  &:hover {
    color: #151515;
    background-color: #dcdedf;
  }
}

.search-suggestion-image {
  width: 130px;
  height: 61px;

  & > img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.search-suggestion-info {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.search-suggestion-name {
  font-size: 14px;
  font-weight: bold;
}

.search-suggestion-price {
  font-size: 13px;
}

.search-suggestion-on-wishlist {
  position: absolute;
  left: 4px;
  top: 50%;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 0 4px 4px;
  box-shadow: 0 0 6px 0 #000000;
  color: #111111;
  background-color: #d3deea;
  font-size: 11px;
  line-height: 1;
  transform: translateY(-50%);
  transition: opacity 0.2s, left 0.2s;

  & > span {
    position: relative;
    width: 0;
    white-space: nowrap;
    overflow: hidden;
    transition: width 0.2s;
  }
}

.search-suggestion:hover .search-suggestion-on-wishlist > span {
  width: 72px;
}

.store-section {
  align-self: center;
  width: 940px;
  margin-bottom: 50px;
}

.store-section-title {
  margin-bottom: 12px;
  color: #ffffff;
  font-size: 14px;
}

.browses {
  align-self: center;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  grid-gap: 8px;
  width: 940px;
}

.browse {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 58px;
  border-radius: 2px;
  color: #ffffff;
  background: linear-gradient(90deg, #06BFFF 0%, #2D73FF 100%);
  font-size: 16px;
  text-decoration: none;

  &:hover {
    background: linear-gradient(90deg, #06BFFF 30%, #2D73FF 100%);
  }
}

.home_cluster_ctn {
  background: url( '../../assets/cluster_bg.png' ) bottom center no-repeat;
  position: relative;
}

.home_page_content_title {
  font-family: "Motiva Sans", Sans-serif;
  font-size: 14px;
  text-transform: uppercase;
  color: #fff;
  margin: 0 0 10px;
  letter-spacing: 0.03em;
  font-weight: normal;
  padding-top: 2px;
  display: block;
  margin-block-start: 0.83em;
  margin-block-end: 0.83em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  unicode-bidi: isolate;
}

.carousel_container {
  position: relative;
}

.maincap {
  height: 380px;
  margin-top: 5px;
}

.carousel_items {
  height: 353px;
  overflow: hidden;
  box-shadow: 0 0 7px 0px #000;
  position: relative;
  clear: both;
}

.store_main_capsule, .store_main_capsule:hover {
  background-image: url( '../../assets/background_maincap_2.jpg');
  background-repeat: no-repeat;
  background-position: right;
  color: #fff;
  display: flex;
  text-decoration: none;
}

.store_main_capsule .capsule {
  flex-shrink: 0;
  background-color: #000;
  position: relative;
  z-index: 2;
  margin-right: 10px;
  width: 616px;
  height: 353px;
  background-position: center center;
  background-size: cover;
  box-shadow: 0 0 10px 5px #000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.capsule > .screenshot {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.store_main_capsule .info {
  flex-shrink: 1;
}

.app_name {
  display: flex;
  align-items: center;
  padding-top: 0px;
  font-family: "Motiva Sans", Sans-serif;
  font-weight: 300;
  height: 69px;
  padding-left: 6px;
  padding-right: 6px;
}

.store_main_capsule .app_name > div {
  max-height: 62px;
  font-size: 24px;
  padding-bottom: 4px;
  padding-top: 4px;
  line-height: 28px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.store_main_capsule .screenshots {
  width: 344px;
  display: inline-block;
  margin-left: -30px;
  max-height: 176px;
  position: relative;
}

.store_main_capsule .screenshots > div {
  width: 162px;
  height: 69px;
  padding: 10px 10px 0 0;
  display: inline-block;
}

.store_main_capsule .screenshots > div > img {
  width: 162px;
  height: 69px;
  background-size: cover;
  background-position: center center;
  display: inline-block;
  opacity: 0.6;

  &:hover {
    opacity: 1;
  }
}

.store_main_capsule .reason {
  margin-top: 8px;
  padding-left: 6px;
  display: inline-block;
  width: 298px;
  font-size: 14px;
}

.store_main_capsule .discount_block_ca {
  position: absolute;
  bottom: 10px;
  left: 632px;
  font-family: sans-serif;
}

.discount_block_inline_ca {
  line-height: 15px;
}

.discount_pct_ca {
  padding: 0 3px;
  font-family: "Motiva Sans", Sans-serif;
  font-weight: 500;
  color: #BEEE11;
  background: #4c6b22;
  display: inline-block;
  font-size: 11px;
}

.discount_original_price_ca {
  position: relative;
  width: fit-content;
  color: #738895;
  font-size: 11px;
  line-height: 12px;
  margin-left: 4px;
}

.discount_original_price_ca:before {
  content: '';
  left: 0px;
  right: 0px;
  position: absolute;
  top: 43%;
  border-bottom: 1.5px solid #738895;
  transform: skewY(-8deg);
  box-shadow: 0 0 2px black;
}

.discount_prices_ca {
  display: flex;
  align-items: center;
  flex-direction: row;
  flex-shrink: 1;
  overflow: hidden;
  justify-content: end;
}

.discount_prices_cap {
  display: flex;
  align-items: center;
  flex-direction: row;
  flex-shrink: 1;
  overflow: hidden;
  justify-content: end;
  background: #344654;
}

.discount_final_price_ca_nodiscount {
  background: none;
  padding: 0 6px;
  color: white;
  font-size: 11px;
  line-height: 12px;
}

.discount_final_price_ca {
  color: #BEEE11;
  font-size: 11px;
  line-height: 12px;
  margin-left: 4px;
  margin-right: 4px;
}

.carousel_thumbs {
  text-align: center;
  min-height: 37px;
  padding: 4px;
}

.thumbs {
  display: inline-block;
  margin: 12px 2px;
  width: 15px;
  height: 9px;
  border-radius: 2px;
  transition: background-color 0.2s;
  background-color: hsla(202,60%,100%,0.2);
  cursor: pointer;

  &:hover {
    background-color: hsla(202,60%,100%,0.3);
  }
}

.thumbs_focus {
  display: inline-block;
  margin: 12px 2px;
  width: 15px;
  height: 9px;
  border-radius: 2px;
  transition: background-color 0.2s;
  background-color: hsla(202,60%,100%,0.4);
  cursor: pointer;
}

.arrow {
  top: 122px;
  position: absolute;
  width: 23px;
  height: 36px;
  padding: 36px 11px;
  cursor: pointer;
  z-index: 3;
}

.left {
  left: -46px;
  background: linear-gradient( to right, rgba( 0, 0, 0, 0.3) 5%,rgba( 0, 0, 0, 0) 95%);

  &:hover {
    background: linear-gradient( to right, #48617b ,rgba( 0, 0, 0, 0) 95%);
  }
}

.right {
  right: -46px;
  background: linear-gradient( to right, rgba( 0, 0, 0, 0) 5%,rgba( 0, 0, 0, 0.3) 95%);

  &:hover {
    background: linear-gradient( to right, rgba( 0, 0, 0, 0) 5%, #48617b);
  }
}

.left_arrow {
  background-position-x: 23px;
  background-image: url('../../assets/arrows.png');
  width: 23px;
  height: 36px;
}

.right_arrow {
  background-image: url('../../assets/arrows.png');
  width: 23px;
  height: 36px;
}

.home_ctn {
  padding-left: 2%;
  padding-right: 2%;
  padding-bottom: 20px;
  padding-top: 20px;
  overflow: hidden;
}

.home_ctn.tab_container {
  background: linear-gradient( to bottom, rgba(42,71,94,1.0) 5%, rgba(42,71,94,0.0) 70%);
  padding-top: 1px;
  margin-top: 60px;
}

.home_page_content {
  position: relative;
  width: 940px;
  margin: 0 auto;
}

.home_tabs_row_ctn {
  margin-bottom: 38px;
  width: 940px;
  margin-left: auto;
  margin-right: auto;
}

.store_horizontal_minislider_ctn {
  position: relative;
  overflow: hidden;
}

.home_tab {
  border: none;
  display: inline-block;
  margin-right: 0px;
  border-top-left-radius: 3px;
  border-top-right-radius: 3px;
  font-size: 13px;
  color: #2f89bc;
  line-height: 24px;
  cursor: pointer;
  vertical-align: top;
  background: transparent;
  margin-top: 0;
  box-shadow: none;
  padding: 1px;
}

.tab_content {
  border: none;
  background-color: #2a475e;
  line-height: 29px;
  font-size: 14px;
  color: #ffffff;
  background: #1a2737;
  text-shadow: -1px -1px rgba( 0, 0, 0, 0.25 );
  cursor: default;
  margin-top: 0;
  box-shadow: none;
  border-top-left-radius: 3px;
  border-top-right-radius: 3px;
  padding: 0 10px;
  vertical-align: top;
}

.flex_cols {
  display: flex;
  flex-direction: row;
}

.home_leftcol {
  float: none;
  min-width: 0;
  display: flex;
  flex-direction: column;
  width: 618px;
}

.home_tab_col {
  position: relative;
}

.home_tabs_content {
  min-height: 775px;
}

.tab_item {
  position: relative;
  display: block;
  background: rgba(0, 0, 0, 0.2);
  height: 69px;
  margin-bottom: 5px;
  padding-left: 198px;
  -webkit-text-size-adjust: none;
  overflow: visible;
  z-index: 0;
  text-decoration: none;
}

.tab_item::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;

  z-index: -1;
  transition: width 0s ease;
  border-radius: 8px;
  pointer-events: none;
}

.tab_item:hover::before {
  background: linear-gradient(to right, #c6e6f8 5%, #95bcd3 95%);
  width: 650px;
}

.tab_item:hover .tab_item_name{
  color: #10161b;
}

.tab_item:hover .discount_final_price {
  color: #263645;
}

.tab_item_cap_img {
  width: 184px;
  height: 69px;
}

.tab_item_cap {
  position: absolute;
  left: 0;
  top: 0;
  z-index: 3;
  line-height: 69px;
  transition: opacity 0.25s;
}

img {
  border: none;
  overflow-clip-margin: content-box;
  overflow: clip;
}

.discount_block {
  position: relative;
  display: flex;
}

.tab_item_discount {
  display: flex;
  justify-content: space-between;
  float: right;
  margin-right: 16px;
  background: none;
  margin-top: 23px;
  width: 133px;
  text-align: right;
}

.tab_item_discount
.discount_pct {
  display: flex;
  align-items: center;
  font-size: 14px;
  line-height: 18px;
  padding: 0 4px;
  border-radius: 1px;
}

.discount_block
.discount_pct,
.discount_pct {
  font-family: "Motiva Sans", sans-serif;
  font-weight: 500;
  color: #BEEE11;
  background: #4c6b22;
}

.discount_prices {
  display: flex;
  flex-direction: column;
  align-items: end;
  justify-content: center;
  padding-left: 4px;
}

.discount_original_price {
  position: relative;
  width: fit-content;
  color: #738895;
  font-size: 11px;
  line-height: 12px;
}

.discount_original_price::before {
  content: '';
  left: 0px;
  right: 0px;
  position: absolute;
  top: 43%;
  border-bottom: 1.5px solid #738895;
  transform: skewY(-8deg);
  box-shadow: 0 0 2px black;
}

.discount_final_price {
  color: #BEEE11;
  line-height: 16px;
  font-size: 15px;
}

.tab_item_content {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-top: 6px;
  font-size: 12px;
  width: 217px;
}

.tab_item_name {
  color: #c7d5e0;
  font-size: 1.25em;
  line-height: 18px;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: block;
  overflow: hidden;
  transition: color 0.25s;
}

.ds_options {
  display: block;
  position: absolute;
  top: 0px;
  right: -5px;
  cursor: pointer;
  opacity: 0;
  padding: 5px 5px 0 0;
  transition: opacity 0.2s, right 0.2s;
  z-index: 15;
}

h2 {
  font-family: "Motiva Sans", sans-serif;
  text-transform: uppercase;
  color: #fff;
  margin: 0 0 10px;
  font-weight: normal;
  padding-top: 2px;
}

.home_rightcol {
  flex: 1;
  margin-left: 14px;
  float: none;
  min-width: 0;
  display: flex;
  flex-direction: column;
  width: 308px;
}

.tab_preview_container {
  flex: 1;
  position: relative;
  background: radial-gradient(69% 62% at 100% 16%, #b4cfe1 0%, #95bbd4 100%);
  border-radius: 5px;
  margin-bottom: 5px;
}

.tab_preview {
  position: absolute;
  top: 9px;
  left: 16px;
  opacity: 0;
  transition: opacity 300ms;
  pointer-events: none;
  width: 292px;

  opacity: 1.0;
  pointer-events: auto;
}

.tab_preview_title {
  text-transform: none;
  font-size: 21px;
  letter-spacing: 0px;
  color: #263645;
  height: 30px;
  padding-right: 8px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  display: block;
  margin-block-start: 0.83em;
  margin-block-end: 0.83em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  unicode-bidi: isolate;
}

.screenshot {
  width: 258px;
  height: 134px;
  margin-top: 3px;
  background-size: cover;
  background-position: center center;
  padding: 8px;
}
</style>