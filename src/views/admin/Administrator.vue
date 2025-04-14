<script setup lang="ts">
import {ref} from 'vue'
import {router} from '../../router'
import {getAllProducts, ProductInfo} from "../../api/product.ts";
import {deleteProductById} from "../../api/product.ts";

const activeTab = ref(0)
const productList = ref<ProductInfo[]>([])
const loading = ref(false)

getAllProducts().then(res => {
  productList.value = res.data.result
})

function jumpToUpdate(product_id: number) {
  router.push({path: '/updateProduct/' + product_id})
}

function jumpToCreate() {
  router.push({path: '/createProduct'})
}

async function deleteProduct(product_id: number) {
  await deleteProductById(product_id)
  const res = await getAllProducts()
  productList.value = res.data.result
}
</script>

<template>
  <div class="app">
    <div class="app-background"></div>
    <div class="home_ctn tab_container" style="overflow: visible;">
      <div class="home_page_content home_tabs_row_ctn">
        <div class="store_horizontal_minislider_ctn" style="height: 31px;">
          <div class="home_tabs_row store_horizontal_minislider">
            <div class="home_tab">
              <div class="tab_content">商品列表</div>
            </div>
          </div>
        </div>
        <div v-loading="loading" class="create-button" @click.prevent="jumpToCreate">
          创建商品
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
                :to="{name:'detail',params:{ product_id:product.productId }}"
            >
              <div class="tab_item_cap">
                <img class="tab_item_cap_img" :src="product.productLogo">
              </div>
              <div class="discount_block tab_item_discount">
                <div class="discount_pct">-{{ product.productDiscount }}%</div>
                <div class="discount_prices">
                  <div class="discount_original_price">{{ (product.productPrice).toFixed(2) }}</div>
                  <div class="discount_final_price">
                    {{ (product.productPrice * (1 - product.productDiscount / 100)).toFixed(2) }}
                  </div>
                </div>
              </div>
              <div class="tab_item_content">
                <div class="tab_item_name">{{ product.productName }}</div>
              </div>
              <div v-loading="loading" class="update-button" @click.prevent="jumpToUpdate(product.productId)">
                更新信息
              </div>
              <div v-loading="loading" class="delete-button" @click.prevent="deleteProduct(product.productId)">
                删除商品
              </div>
              <div style="clear: both;"></div>
              <div class="ds_options">
                <div></div>
              </div>
            </router-link>
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
  background-image: url("../../assets/Steam_Summer_Sale_pattern-04-day_lighter.gif");
  background-repeat: repeat;
}


.search-suggestion:hover .search-suggestion-on-wishlist > span {
  width: 72px;
}


.home_ctn {
  padding-left: 2%;
  padding-right: 2%;
  padding-bottom: 20px;
  padding-top: 20px;
  overflow: hidden;
}

.home_ctn.tab_container {
  background: linear-gradient(to bottom, rgba(42, 71, 94, 1.0) 5%, rgba(42, 71, 94, 0.0) 70%);
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
  text-shadow: -1px -1px rgba(0, 0, 0, 0.25);
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
  width: 1000px;
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
  background: rgba(0, 0, 0, 0.2); /* 默认背景 */
  height: 69px;
  margin-bottom: 5px;
  padding-left: 198px;
  -webkit-text-size-adjust: none;
  overflow: visible; /* 让伪元素能扩展出组件本体 */
  z-index: 0;
}

/* 背景伪元素 */
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
  width: 950px;
}

.tab_item:hover .tab_item_name {
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

.update-button {
  box-sizing: border-box;
  width: 100px;
  border-radius: 2px;
  position: absolute;
  right: 350px;
  top: 50%;
  transform: translateY(-50%);
  margin-top: 0;
  z-index: 2;
  color: #ffffff;
  font-size: 14px;
  line-height: 25px;
  text-align: center;
  background: linear-gradient(90deg, #06BFFF 0%, #2D73FF 100%);
  cursor: pointer;

  &:hover {
    background: linear-gradient(90deg, #06BFFF 30%, #2D73FF 100%);
  }
}

.delete-button {
  box-sizing: border-box;
  width: 100px;
  border-radius: 2px;
  position: absolute;
  right: 200px; /* 距离右侧20px */
  top: 50%; /* 垂直居中定位 */
  transform: translateY(-50%); /* 垂直偏移补偿 */
  margin-top: 0; /* 清除原有margin */
  z-index: 2; /* 确保按钮显示在顶层 */
  color: #ffffff;
  font-size: 14px;
  line-height: 25px;
  text-align: center;
  background: linear-gradient(90deg, #bd5068 0%, #c15755 100%);
  cursor: pointer;

  &:hover {
    background: #ff3b2c
  }
}

.create-button{
  box-sizing: border-box;
  width: 100px;
  border-radius: 2px;
  position: absolute;
  right: 0;
  top: 100%;
  transform: translateY(-50%);
  z-index: 2;
  color: #ffffff;
  font-size: 14px;
  line-height: 35px;
  text-align: center;
  background: linear-gradient(90deg, #06BFFF 0%, #2D73FF 100%);
  cursor: pointer;
}


</style>