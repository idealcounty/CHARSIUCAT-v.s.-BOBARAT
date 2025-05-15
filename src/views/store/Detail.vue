<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import { router } from "../../router";
import { Cart, getProductByProductId, updateCart, productExists } from "../../api/product.ts";
import { CartItem, getUserCartVO, userInfo } from "../../api/user.ts";

const product_id = router.currentRoute.value.params.product_id;
const productVO = ref();
const productId = ref('');
const productName = ref('');
const productAmount = ref('');
const productPrice = ref('');
const productDiscount = ref('');
const productDescription = ref('');
const productLogo = ref('');
const productImages = ref([]);
const userId = ref();
const cartId = ref();
const cart = ref<Cart>(null);

const existsResult = ref<boolean | null>(null);

const activeImage = ref(1);


const loadData = async () => {
  const res = await getProductByProductId(Number(product_id));
  productVO.value = res.data.result;
  productId.value = productVO.value.productId;
  productName.value = productVO.value.productName;
  productAmount.value = productVO.value.productAmount;
  productPrice.value = productVO.value.productPrice;
  productDiscount.value = productVO.value.productDiscount;
  productDescription.value = productVO.value.productDescription;
  productLogo.value = productVO.value.productLogo;
  productImages.value = productVO.value.productImages;
};

onMounted(async () => {
  await loadData();
  const userRes = await userInfo();
  userId.value = userRes.data.result.id;
  const cartRes = await getUserCartVO(userId.value);
  cart.value = cartRes.data.result;
  cartId.value = cartRes.data.result.cartId;
  checkExistence();
});

const checkExistence = async () => {
  if (!productId.value || !cartId.value) {
    existsResult.value = null; // 参数未准备好
    return;
  }
  try {
    const res = await productExists(Number(productId.value), cartId.value); // 转换productId为数字
    existsResult.value = res.data; // 假设接口返回布尔值
  } catch (error) {
    console.error("检查失败", error);
    existsResult.value = false;
  }
};

watch([productId, cartId], () => {
  checkExistence();
});

function addToCart(product_id:number,now_cart: Cart) {
  updateCart(product_id,1,now_cart).then( res => {
    if(res.data.code==='000'){
      ElMessage({
        message: "添加成功！",
        type: 'success',
        center: true,
      })
    }
    existsResult.value = true;
  })
}
function deleteToCart(product_id:number,now_cart: Cart) {
  updateCart(product_id,0,now_cart).then( res => {
    if(res.data.code==='000'){
      ElMessage({
        message: "移除成功！",
        type: 'success',
        center: true,
      })
    }
  })
  existsResult.value = false;
}
</script>

<template>
  <div class="app">
    <div class="app-background"></div>
    <!-- 导航栏（完全保留原有代码） -->
    <div class="store-header">
      <div class="content">
        <div class="store_controls">
          <div class="cart_status_flex">
            <div class="store_header_btn_gray store-header_btn">
              <div class="store_header_btn_caps store_header_btn_leftcap"></div>
              <div class="store_header_btn_caps store_header_btn_rightcap"></div>
              <router-link class="store_header_btn_content" to="/wishlist">
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
    <!-- 标题 -->
    <div class="page_title_area game_title_area page_content">
      <div class="apphub_HomeHeaderContent">
        <div class="apphub_HeaderStandardTop">
          <div id="appHubAppName" class="apphub_AppName">{{ productName }}</div>
        </div>
      </div>
    </div>
    <div style="clear: left;"></div>
    <!-- 主体内容 -->
    <div class="block game_media_and_summary_ctn">
      <div class="game_background_glow">
        <div class="block_content page_content">
          <!-- 右栏 -->
          <div class="rightcol">
            <div class="glance_ctn">
              <div class="game_header_image_ctn">
                <img class="game_header_image_full" :src="productLogo">
              </div>
              <div class="game_description_snippet">{{ productDescription }}</div>
              <div class="glance_ctn_resposive_left">
                <div class="product_amount">
                  <div class="subtitle colomn">剩余库存:</div>
                  <div class="amount">{{ productAmount }}</div>
                </div>
              </div>
            </div>
          </div>
          <!-- 左栏 -->
          <div class="leftcol">
            <div class="highlight_ctn">
              <div class="highlight_overflow">
                <div class="highlight_player_area">
                  <img
                      class="highlight_player_item highlight_screenshot"
                      :src="productImages[activeImage]"
                  >
                </div>
                <div class="highlight_strip">
                  <div class="highlight_strip_scroll">
                    <img
                        class="highlight_strip_item highlight_strip_screenshot"
                        v-for="(img,i) in productImages"
                        :src="img"
                        :key="i"
                        @mouseenter="activeImage = i"
                    >
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div style="clear: both;"></div>
        </div>
      </div>
      <div class="queue_overflow_ctn">
        <div class="queue_ctn">
          <div class="queue_actions_ctn">
            <div class="add_to_wishlist_area">
              <!-- 原有v-if改为基于existsResult -->
              <div
                  class="btnv6_blue_hoverfade btn_medium add_to_wishlist"
                  v-if="existsResult === false"
                  @click="addToCart(product_id, cart)"
              >添加至您的愿望单</div>

              <div
                  class="btnv6_blue_hoverfade btn_medium add_to_wishlist"
                  v-if="existsResult === true"
                  @click="deleteToCart(product_id, cart)"
              >从愿望单中移除</div>
            </div>
          </div>
        </div>
      </div>
      <!--库中已有-->
      <div class="game_area_already_owned page_content">
        <div class="game_area_already_owned_ctn">
          <div class="ds_owned_flag ds_flag">在库中&nbsp;&nbsp;</div>
          <div class="already_in_library">您的 Steam 库中已有《{{ productName }}》</div>
        </div>
      </div>
      <!--撰写评测-->
      <div class="game_area_play_stats">
        <div class="review_container">
          <div class="review_create">
            <h1 class="review_title">为 {{ productName }} 撰写一篇评测</h1>
            <p id="review_describe">
              您认为这款游戏有什么优点或缺点？您是否会将这款游戏推荐给他人？请写下您的看法。<br>请注意保持礼貌并遵守
              <a class="review_rules" href="https://help.steampowered.com/zh-cn/faqs/view/6862-8119-C23E-EA7B">《规则及指引》</a>。
            </p>
            <div style=" display: flex; align-items: flex-start;">
              <div class="avatar_block">
                <router-link to="">
                  <div class="avatar avatar_block_status_online">
                    <img src="">
                  </div>
                </router-link>
              </div>
              <div class="content">
                <textarea class="input_box" maxlength="8000"></textarea>
                <div class="controls">
                  <div class="writeReviewTable">
                    <div class="review_controls">
                      <div class="controlblock has_dselect">
                        <span class="controltext">可见性：公开</span>
                      </div>
                      <div class="controlblock has_dselect">
                        <span class="controltext">语言：简体中文</span>
                      </div>
                      <div class="controlblock enable_review_comments">
                        <input type="checkbox" id="EnableReviewComments"><label for="EnableReviewComments">允许留言</label>
                      </div>
                      <div class="controlblock formattinghelp">
                        <a onclick="window.open( 'https://steamcommunity.com/comment/Recommendation/formattinghelp','formattinghelp','height=640,width=640,resize=yes,scrollbars=yes' );">格式帮助</a>
                      </div>
                    </div>
                  </div>
                  <div class="review_controls_row">
                    <div class="review_controls_left">
                      <div class="review_control_didyouenjoy">
                        您推荐这款游戏吗？							</div>
                      <div class="controlblock review_create_vote_controls" id="VoteUpDownBtnCtn">
                        <div class="btnv6_blue_hoverfade btn_medium ico_hover " onclick="">
                          <span>
                            <i class="ico18 thumb_up"></i>
                            是
                          </span>
                        </div>
                        <div class="btnv6_blue_hoverfade btn_medium ico_hover " onclick="">
                          <span>
                            <i class="ico18 thumb_down"></i>
                            否
                          </span>
                        </div>
                      </div>
                    </div>
                    <div class="review_controls_right">
                      <div class="btnv6_lightblue_blue btnv6_border_2px btn_medium" onclick="">
                        <span>发布评测</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div style="clear: both"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

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

.game_background_glow {
  background: url( '../../assets/game_page_background_shadow.png?v=2' );
  background-position: bottom;
  padding-bottom: 1px;
  background-repeat: no-repeat;
  margin: 0px auto;
}

.block_content {
  background: linear-gradient(to right,  rgba(0,0,0,0) 50%,rgba(0,0,0,0.4) 100%);
}

.leftcol {
  width: 616px;
  float: left;
}

.highlight_overflow {
  overflow: hidden;
  margin-right: 16px;
  position: relative;
}

.highlight_player_area {
  background-color: #000000;
  overflow: hidden;
  position: relative;
  width: 600px;
  height: 337px;
}

.highlight_player_item {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  text-align: center;
  width: 600px;
  height: 337px;
}

.highlight_strip {
  margin-top: 4px;
  position: relative;
  height: 69px;
  margin-bottom: 4px;
  z-index: 40;
}

.highlight_strip_scroll {
  position: absolute;
}

.highlight_strip_item {
  float: left;
  height: 65px;
  width: 116px;
  cursor: pointer;
  text-align: center;
  margin: 2px;
  background-color: #000000;
  position: relative;
}

.rightcol {
  width: 324px;
  margin-left: 0px;
  float: right;
}

img {
  -webkit-user-drag: none;
  border: none;
}

.glance_ctn {
  padding: 0px 0px 0px 0px;
}

.game_header_image_ctn {
  margin-bottom: 7px;
}

.game_header_image_full {
  width: 324px;
  height: 151px;
}

.game_description_snippet {
  max-height: 111px;
  overflow: hidden;
  font-size: 13px;
  line-height: 18px;
  padding-right: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 6;
  -webkit-box-orient: vertical;
  color: #ffffff;
}

.glance_ctn_resposive_left {
  display: flex;
  line-height: 16px;
  padding-top: 9px;
  padding-bottom: 13px;
}

.product_amount {
  display: flex;
  line-height: 16px;
  padding-top: 9px;
  padding-bottom: 13px;
}

.subtitle {
  color: #556772;
  text-transform: uppercase;
  font-size: 10px;
  padding-right: 10px;
  min-width: 94px;
}

.colomn {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.amount {
  max-height: 30px;
  overflow: hidden;
  color: #8f98a0;
  font-size: 12px;
}

.queue_ctn {
  width: 940px;
  background: rgba( 0, 0, 0, 0.2 );
  margin: 0px auto;
  margin-bottom: 16px;
}

.queue_actions_ctn {
  padding: 16px;
}

.add_to_wishlist_area {
  display: inline-block;
  position: relative;
}

.btnv6_blue_hoverfade {
  width: 152px;
  height: 32px;
  border-radius: 2px;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  text-align: center;
  text-decoration: none;
  color: #67c1f5;
  background: rgba( 103, 193, 245, 0.2 );

  &:hover {
    color: #ffffff;
    background: linear-gradient(to right,#66bff3,#437d9e);
  }
}

.btn_medium {
  margin-bottom: 4px;
}

.game_area_already_owned.page_content {
  height: 40px;
  padding: 0px 15px 0px 0px;
  position: relative;
  background: linear-gradient(to right, rgba(163, 207, 6, 0.6) 0%, rgba(75, 106, 33, 0.6) 100%);
  filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#99a3cf06', endColorstr='#994b6a21',GradientType=1 );
  font-size: 18px;
  font-family: "Motiva Sans", Sans-serif;
  font-weight: 300;
  line-height: 40px;
  color: #a3cf06;
  margin-bottom: 1px;
  width: 925px;
}

.game_area_already_owned .ds_flag {
  position: relative;
  float: left;
  top: 10px;
  left: -8px;
  box-shadow: 1px 1px 2px #000000;
  background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAAKCAYAAABi8KSDAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OUNDNzBFNTUyMUM0MTFFNDk1REVFODRBNUU5RjA2MUYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OUNDNzBFNTYyMUM0MTFFNDk1REVFODRBNUU5RjA2MUYiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo5Q0M3MEU1MzIxQzQxMUU0OTVERUU4NEE1RTlGMDYxRiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo5Q0M3MEU1NDIxQzQxMUU0OTVERUU4NEE1RTlGMDYxRiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pv3vUKAAAAAlSURBVHjaYvz//z8DsYARpFhISAivjnfv3jGSp3jUGeQ4AyDAADZHNe2nyOBrAAAAAElFTkSuQmCC') no-repeat 4px 4px #a3cf06;
}

.ds_flag {
  position: absolute;
  left: 0;
  top: 14px;
  font-size: 10px;
  color: #111111;
  box-shadow: 0 0 10px rgba(0,0,0,0.9);
  height: 18px;
  line-height: 19px;
  padding: 0 0 0 18px;
  white-space: nowrap;
  z-index: 5;
}

.game_area_already_owned .already_in_library {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.game_area_play_stats {
  background: linear-gradient(135deg,  rgba(97,100,101,0.3) 0%,rgba(226,244,255,0.3) 100%);
  padding: 0px 16px 16px 16px;
  margin: 0px auto;
  margin-bottom: 40px;
  max-width: 908px;
}

.review_create {
  max-width: 908px;
  position: relative;
  overflow: visible;
  padding-top: 16px;
}

.review_title {
  font-size: 18px;
  margin-bottom: 3px;
  color: #66c0f4;
  font-family: "Motiva Sans", Sans-serif;
  font-weight: 300;
  width: 100%;
  border-top: 1px solid #212c3d;
  padding-top: 10px;
  display: block;
  margin-block-start: 0.67em;
  margin-block-end: 0.67em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  unicode-bidi: isolate;
}

.review_create p {
  font-size: 13px;
  margin-bottom: 15px;
  line-height: 17px;
  color: #8f98a0;
}

p {
  display: block;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  unicode-bidi: isolate;
}

.review_rules {
  text-decoration: none;
  color: #ffffff;

  &:hover {
    color: #66c0f4;
  }
}

.avatar {
  width: 94px;
  height: 94px;
  background-repeat: no-repeat;
  padding: 1px;
  margin-right: 16px;
  border-radius: 0px;
  float: left;
}

.avatar_block_status_online {
  background: linear-gradient( to bottom, rgba(83,164,196,1) 5%, rgba(69,128,151,1) 95%);
  filter: none;
}

.review_create .content {
  height: auto;
  overflow: visible;
  width: 796px;
  margin: 0px;
}

.input_box {
  border: 1px solid #233c51;
  border-radius: 3px;
  width: 100%;
  max-width: 765px;
  height: 112px;
  background-color: #222b35;
  color: #d6d7d8;
  padding: 10px 11px;
  font-size: 13px;
  font-family: "Motiva Sans", sans-serif;
  font-weight: normal;
  box-shadow: inset 0px 0px 7px rgba( 0, 0, 0, 0.5 );
}

.controls {
  margin-top: 4px;
}

.review_controls {
  padding: 8px;
  background-color: rgba( 0, 0, 0, 0.2 );
  margin-bottom: 10px;
  max-width: 772px;
}

.review_controls .controlblock {
  display: inline-block;
  margin-right: 30px;
  position: relative;
  min-height: 100%;
  font-family: Arial, Helvetica, sans-serif;
  color: #c6d4df;
  font-size: 12px;
}

.content .controls .dselect_container {
  display: inline-block;
  position: relative;
  vertical-align: middle;
  margin-top: -3px;
  color: #acb2b8;
  font-size: 12px;
}

.review_controls .controlblock .dselect_container {
  background: rgba( 103, 193, 245, 0.1 );
  border-radius: 3px;
  padding: 0px 0px 0px 0px;
}

.review_controls .formattinghelp {
  padding: 4px;
  float: right;
  margin-right: 0px;
  position: relative;
  min-height: 100%;
  font-family: Arial, Helvetica, sans-serif;
  color: #ffffff;
  font-size: 12px;
  text-decoration: none;
}

.review_controls_row {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-content: flex-end;
}

.review_controls_left {
  display: flex;
  flex-direction: column;
  position: relative;
  min-height: 100%;
  font-family: Arial, Helvetica, sans-serif;
  color: #c6d4df;
  font-size: 12px;
  max-width: 150px;
}

.review_control_didyouenjoy {
  margin-bottom: 4px;
}

.review_create_vote_controls {
  color: #acb2b8;
  font-size: 13px;
  font-family: "Motiva Sans", Sans-serif;
  font-weight: normal;
  display: flex;
}

.btnv6_blue_hoverfade {
  border-radius: 2px;
  border: none;
  padding: 1px;
  margin-right: 4px;
  cursor: pointer;
  text-decoration: none;
  color: #67c1f5;
  font-size: 13px;
  font-weight: normal;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba( 103, 193, 245, 0.2 );
  width: 69.32px;
  height: 32px;
}

.ico18 {
  display: inline-block;
  width: 18px;
  height: 18px;
  margin: 0 0px;
  background: url(https://store.cloudflare.steamstatic.com/public/shared/images/buttons/icons_18.png?v=3);
  vertical-align: text-top;
}

.ico18.thumb_up {
  background-position: 0px 0px;
}

.ico18.thumb_down {
  background-position: -18px 0px;
}

.btnv6_blue_hoverfade:hover .ico18.thumb_up {
  background-position: -72px 0px;
}

.btnv6_blue_hoverfade:hover .ico18.thumb_down {
  background-position: -90px 0px;
}

.review_controls_right {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: flex-end;
  gap: 8px;
}

.btnv6_border_2px {
  border-radius: 4px;
  border: 2px solid #17202f;
}

.btnv6_lightblue_blue {
  padding: 1px;
  cursor: pointer;
  text-decoration: none !important;
  color: #2B5066 !important;
  background: linear-gradient( to bottom, rgba(193,228,249,1) 5%, rgba(148,183,202,1) 95%);
  width: 88px;
  height: 32px;
  font-weight: normal;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>