<script setup lang="ts" xmlns:https="http://www.w3.org/1999/xhtml">
import { ref } from 'vue'

const currentHour = ref(new Date().getHours())
const activeTab = ref(0)

const tabs = [
  {
    name: '深渊之影',
    title: '深渊之影',
    images: [
      'https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/2100150/ss_b79e7c206636cf13f46b7b9f641141216b7a2ece.600x338.jpg',
      'https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/2100150/ss_b79e7c206636cf13f46b7b9f641141216b7a2ece.600x338.jpg',
    ],
  },
  {
    name: '幻境探险',
    title: '幻境探险',
    images: [
      'https://via.placeholder.com/600x338?text=幻境1',
      'https://via.placeholder.com/600x338?text=幻境2',
    ],
  },
  {
    name: '机械狂潮',
    title: '机械狂潮',
    images: [
      'https://via.placeholder.com/600x338?text=机械1',
      'https://via.placeholder.com/600x338?text=机械2',
    ],
  },
]
</script>

<template>
  <div class="app">
    <div class="app-background"></div>
    <div class="store-background"
         :class="{ 'store-background_day': currentHour >= 6 && currentHour < 18, 'store-background_night': currentHour < 6 || currentHour >= 18 }"></div>
    <!-- 导航栏 -->
    <div class="store-header">
<!--      <RouterLink v-if="token" class="wishlist" to="wishlist">愿望单<span v-if="wishlistSize > 0"> ({{ wishlistSize }})</span></RouterLink>-->
<!--      <div v-else class="wishlist-area"/>-->
      <div class="store-nav">
        <RouterLink class="store-nav-tab" to="">您的商店</RouterLink>
        <RouterLink class="store-nav-tab" to="">新鲜推荐</RouterLink>
        <RouterLink class="store-nav-tab" to="">类别</RouterLink>
        <RouterLink class="store-nav-tab" to="">点数商店</RouterLink>
        <RouterLink class="store-nav-tab" to="">新闻</RouterLink>
        <RouterLink class="store-nav-tab" to="">实验室</RouterLink>
<!--        <div class="search">-->
<!--          <input v-model="keyword" class="search-input" placeholder="搜索" @input="getSearchSuggestionsDebounce()">-->
<!--          <RouterLink class="search-button" to=""/>-->
<!--          &lt;!&ndash; 搜索建议 &ndash;&gt;-->
<!--          <div class="search-suggestions">-->
<!--            <RouterLink v-for="(item, index) in suggestions" :key="index" class="search-suggestion" :to="`/app/${item.appId}`">-->
<!--              <div class="search-suggestion-image">-->
<!--                <img :src="item.header" alt="">-->
<!--              </div>-->
<!--              <div class="search-suggestion-info">-->
<!--                <div class="search-suggestion-name">{{ item.name }}</div>-->
<!--                <div class="search-suggestion-price">{{ getPriceStr(item.finalPrice) }}</div>-->
<!--              </div>-->
<!--              <div v-if="item.status === 1" class="search-suggestion-on-wishlist">-->
<!--                <img src="../../assets/on_wishlist.png" alt="">-->
<!--                <span>已在愿望单中</span>-->
<!--              </div>-->
<!--            </RouterLink>-->
<!--          </div>-->
<!--        </div>-->
      </div>
    </div>
<!--    &lt;!&ndash; 精选与推荐 &ndash;&gt;-->
<!--    <div class="store-section">-->
<!--      <div class="store-section-title">精选与推荐</div>-->
<!--      <Swiper class="recommendations" :num="recommendations.length">-->
<!--        <template v-for="(item, index) in recommendations" :key="index" #[index]>-->
<!--          <RouterLink class="recommendation" :to="`/app/${item.appId}`"-->
<!--                      @mouseenter="recommendationHovered = true" @mouseleave="recommendationHovered = false">-->
<!--            <div class="recommendation-cover">-->
<!--              <img class="current" v-lazy="item.cover" alt="">-->
<!--              <template v-for="(item, index) in item.images" :key="index">-->
<!--                <img :class="{ current: recommendationImageIndex === index }" v-lazy="item" alt="">-->
<!--              </template>-->
<!--              <ExpandButton v-if="token" v-model:status="item.status" :app-id="item.appId"-->
<!--                            :show="recommendationHovered" @update:status="getWishlistSize()"/>-->
<!--              <div class="recommendation-on-wishlist" :class="{ show: item.status === 1 }">-->
<!--                <img src="../../assets/on_wishlist.png" alt="">-->
<!--                <span>已在愿望单中</span>-->
<!--              </div>-->
<!--            </div>-->
<!--            <div class="recommendation-info">-->
<!--              <div v-trunc class="recommendation-name">{{ item.name }}</div>-->
<!--              <div class="recommendation-images">-->
<!--                <div v-for="(item, index) in item.images.slice(0, 4)" :key="index" class="recommendation-image"-->
<!--                     @mouseenter="recommendationImageIndex = index" @mouseleave="recommendationImageIndex = -1">-->
<!--                  <img v-lazy="item" alt="">-->
<!--                </div>-->
<!--              </div>-->
<!--              <div>-->
<!--                <div class="recommendation-reason">现已推出</div>-->
<!--                <div class="recommendation-tags">-->
<!--                  <div class="recommendation-tag">热销商品</div>-->
<!--                </div>-->
<!--              </div>-->
<!--              <div class="recommendation-price-area">-->
<!--                <div v-if="item.discount === 0" class="recommendation-price">{{ getPriceStr(item.price) }}</div>-->
<!--                <div v-else class="recommendation-price_discounted">-->
<!--                  <span class="recommendation-discount">{{ getDiscountStr(item.discount) }}</span>-->
<!--                  <span class="recommendation-origin-price">{{ getPriceStr(item.price) }}</span>-->
<!--                  <span class="recommendation-final-price">{{ getPriceStr(item.finalPrice) }}</span>-->
<!--                </div>-->
<!--                <div class="recommendation-platforms">-->
<!--                  <div v-if="item.win" class="recommendation-platform" title="Windows">-->
<!--                    <img src="../../assets/icon_platform_win.png" alt="">-->
<!--                  </div>-->
<!--                  <div v-if="item.mac" class="recommendation-platform" title="MacOS">-->
<!--                    <img src="../../assets/icon_platform_mac.png" alt="">-->
<!--                  </div>-->
<!--                  <div v-if="item.linux" class="recommendation-platform" title="Linux">-->
<!--                    <img src="../../assets/icon_platform_linux.png" alt="">-->
<!--                  </div>-->
<!--                </div>-->
<!--              </div>-->
<!--            </div>-->
<!--          </RouterLink>-->
<!--        </template>-->
<!--      </Swiper>-->
<!--    </div>-->
    <!-- 按类别浏览 -->
    <!--    <div class="store-section">-->
    <!--      <div class="store-section-title">按类别浏览</div>-->
    <!--      <Swiper class="categories">-->
    <!--        <template>-->
    <!--          <div>-->
    <!--            <div>开放世界</div>-->
    <!--            <div>科幻及赛博朋克</div>-->
    <!--          </div>-->
    <!--        </template>-->
    <!--      </Swiper>-->
    <!--    </div>-->
    <!-- 浏览Steam -->
    <div class="store-section">
      <div class="store-section-title">浏览 STEAM</div>
      <div class="browses">
        <RouterLink class="browse" to="">新品</RouterLink>
        <RouterLink class="browse" to="">优惠</RouterLink>
        <RouterLink class="browse" to="">免费游戏</RouterLink>
        <RouterLink class="browse" to="">按用户标签</RouterLink>
      </div>
    </div>
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
            <a
              v-for="(tab, index) in tabs"
              :key="index"
              class="tab_item"
              @mouseenter="activeTab = index"
            >
              <div class="tab_item_cap">
                <img class="tab_item_cap_img" src="https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/2100150/capsule_184x69_schinese.jpg?t=1744250803">
              </div>
              <div class="discount_block tab_item_discount">
                <div class="discount_pct">-30%</div>
                <div class="discount_prices">
                  <div class="discount_original_price">48.00</div>
                  <div class="discount_final_price">33.60</div>
                </div>
              </div>
              <div class="tab_item_content">
                <div class="tab_item_name">深渊之影</div>
              </div>
              <div style="clear: both;"></div>
              <div class="ds_options"><div></div></div>
            </a>
          </div>
        </div>
        <div class="home_rightcol">
          <div class="tab_preview_container">
            <div class="tab_preview">
              <h2 class="tab_preview_title">{{ tabs[activeTab].title }}</h2>
              <img
                  v-for="(img, i) in tabs[activeTab].images"
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
  background-image: url("../../assets/Steam_Summer_Sale_pattern-04-day_lighter.gif");
  background-repeat: repeat;
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

.wishlist-area {
  height: 20px;
  margin-bottom: 4px;
}

.wishlist {
  width: max-content;
  min-width: 50px;
  height: 20px;
  padding: 0 25px;
  margin: 0 0 4px auto;
  color: #ffffff;
  font-size: 11px;
  font-family: Arial, Helvetica, sans-serif;
  text-decoration: none;
  text-align: center;
  line-height: 20px;
  background-color: #6e96a0;
  background-image: url("../../assets/background_wishlist.jpg");
  background-size: cover;
  box-shadow: 0 0 3px #000000;

  &:hover {
    color: #111111;
    background-image: linear-gradient(135deg, #ffffff, rgba(0, 0, 0, 0.1), rgba(0, 0, 0, 0.3));
  }
}

.store-nav {
  display: flex;
  align-items: center;
  height: 35px;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.4);
  background: linear-gradient(90deg, rgba(62, 103, 150, 0.919) 11.38%, rgba(58, 120, 177, 0.8) 25.23%, rgb(15, 33, 110) 100%);
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

.recommendations {
  height: 353px;
  box-shadow: 0 0 7px 0 #000000;
}

.recommendation {
  display: flex;
  width: 940px;
  height: 353px;
  background-color: #000000;
  text-decoration: none;
}

.recommendation-cover {
  position: relative;
  box-shadow: 0 0 10px 5px #000000;
  width: 616px;
  height: 353px;
  overflow: hidden;

  & > img {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    background-color: #000000;
    opacity: 0;
    transition: opacity 0.2s;
    pointer-events: none;

    &.current {
      opacity: 1;
      transition: opacity 0s;
      pointer-events: auto;
    }
  }
}

.recommendation-on-wishlist {
  position: absolute;
  left: -50px;
  top: 28px;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px;
  box-shadow: 0 0 6px 0 #000000;
  color: #111111;
  background-color: #d3deea;
  font-size: 11px;
  line-height: 1;
  opacity: 0;
  transition: opacity 0.2s, left 0.2s;

  &.show {
    left: 0;
    opacity: 1;
  }
}

.recommendation-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
  box-sizing: border-box;
  width: 324px;
  height: 353px;
  padding: 0 12px 12px 12px;
  background: url("../../assets/background_maincap_2.jpg") right no-repeat;
}

.recommendation-name {
  display: flex;
  align-items: center;
  height: 69px;
  color: #ffffff;
  font-size: 24px;
  font-weight: lighter;
  text-overflow: ellipsis;
}

.recommendation-images {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 12px;
}

.recommendation-image {
  position: relative;
  height: 69px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  &::after {
    content: "";
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.2);
  }

  &:hover::after {
    background-color: transparent;
  }
}

.recommendation-reason {
  margin-bottom: 4px;
  color: #ffffff;
  font-size: 21px;
  font-weight: lighter;
}

.recommendation-tags {
  display: flex;
  flex-flow: wrap;
  row-gap: 4px;
  column-gap: 2px;
}

.recommendation-tag {
  padding: 0 7px;
  border-radius: 2px;
  color: #ffffff;
  background-color: rgba(255, 255, 255, 0.2);
  font-size: 11px;
  line-height: 20px;
  white-space: nowrap;
}

.recommendation-price-area {
  margin-top: auto;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.recommendation-price {
  color: #ffffff;
  font-size: 11px;
}

.recommendation-price_discounted {
  display: flex;
  height: 16px;
}

.recommendation-discount {
  padding: 0 4px;
  color: #beee11;
  background-color: #4c6b22;
  font-size: 12px;
  font-weight: bold;
}

.recommendation-origin-price {
  position: relative;
  padding: 0 4px;
  color: #738895;
  background-color: #344654;
  font-size: 11px;
  line-height: 16px;

  &::after {
    content: "";
    position: absolute;
    left: 6px;
    right: 4px;
    top: 50%;
    height: 1px;
    background-color: #738895;
    box-shadow: 0 0 2px #000000;
    transform: rotate(-8deg);
  }
}

.recommendation-final-price {
  padding-right: 4px;
  color: #beee11;
  background-color: #344654;
  font-size: 11px;
  line-height: 16px;
}

.recommendation-platforms {
  display: flex;
  opacity: 0.7;
}

.recommendation-platform {
  width: 20px;
  height: 20px;

  img {
    width: 100%;
    height: 100%;
  }
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
  overflow: visible;     /* 让伪元素能扩展出组件本体 */
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
  width: 600px;
}

.tab_item:hover .tab_item_name{
  color: #10161b;
}

.tab_item:hover .discount_final_price {
  color: #263645;
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