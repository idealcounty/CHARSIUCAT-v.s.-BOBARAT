<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { userInfo, getUserCart,getUserCartVO,CartItem} from '../../api/user.ts'
import { getProductByProductId, ProductInfo,updateCart,Cart } from '../../api/product.ts'

const sortList = ref(false)
const nickname = ref('')
const userId = ref()
const sortMethod = ref(1)
const cart=ref<Cart>(null)
const wishlistItem = ref<CartItem[]>([])
const wishlist = ref<ProductInfo[]>([])

const getUserInfo = async () => {
  userInfo().then(res => {
    nickname.value = res.data.result.name
    userId.value = res.data.result.id
    getUserCartVO(parseInt(userId.value)).then(res=>{
      cart.value = res.data.result
      console.log(cart.value)
    })
    getUserCart(parseInt(userId.value)).then(res => {
      wishlistItem.value = res.data
      const productIds = wishlistItem.value.map(item => item.productId)
      const productPromises = productIds.map(id => getProductByProductId(id))
      Promise.all(productPromises).then(productList => {
        wishlist.value = productList.map(item => item.data.result)
      })
    })
  })
}

onMounted(() => {
  getUserInfo()
})

function openSortMenu() {
  if (!sortList.value) sortList.value = true
}

function closeSortMenu() {
  if (sortList.value) sortList.value = false
}

function changeSortType(type: number) {
  closeSortMenu()
  sortMethod.value = type
  localStorage.setItem('steamWishlistSort', type.toString())
}

function addToCart(item: CartItem,now_cart: Cart) {
  updateCart(item.productId,item.productQuantity+1,now_cart).then(res =>{
    getUserCart(parseInt(userId.value)).then(res => {
      wishlistItem.value = res.data
      const productIds = wishlistItem.value.map(item => item.productId)
      const productPromises = productIds.map(id => getProductByProductId(id))
      Promise.all(productPromises).then(productList => {
        wishlist.value = productList.map(item => item.data.result)
      })
    })
  })
}

function deleteToCart(item: CartItem,now_cart: Cart) {
  updateCart(item.productId,item.productQuantity-1,now_cart).then(res =>{
    getUserCart(parseInt(userId.value)).then(res => {
      wishlistItem.value = res.data
      const productIds = wishlistItem.value.map(item => item.productId)
      const productPromises = productIds.map(id => getProductByProductId(id))
      Promise.all(productPromises).then(productList => {
        wishlist.value = productList.map(item => item.data.result)
      })
    })
  })
}

function removeFromWishlist(item: CartItem,now_cart: Cart) {
  updateCart(item.productId,0,now_cart).then(res =>{
    getUserCart(parseInt(userId.value)).then(res => {
      wishlistItem.value = res.data
      const productIds = wishlistItem.value.map(item => item.productId)
      const productPromises = productIds.map(id => getProductByProductId(id))
      Promise.all(productPromises).then(productList => {
        wishlist.value = productList.map(item => item.data.result)
      })
    })
  })
}

const totalPrice = computed(() => {
  return wishlistItem.value.reduce((sum, item, index) => {
    const product = wishlist.value[index]
    return sum + (product?.productPrice*(1-0.01*product?.productDiscount) || 0) * item.productQuantity
  }, 0)
})
</script>

<template>
  <div class="app">
    <div class="header">
      <div class="header-avatar"></div>
      {{ nickname }} 的愿望单
    </div>
    <div class="filter">
      <div class="sort" :class="{ focus: sortList }" tabindex="0" @click="openSortMenu()" @mouseleave="closeSortMenu()">
        排序依据：
        <div class="sort-type">{{ ['名称', '价格', '折扣', '添加日期', '发行日期'].at(sortMethod - 1) }}</div>
        <img src="../../assets/btn_arrow_down_padded_white.png" alt="">
        <div class="sort-menu">
          <div class="sort-menu-item" @click.stop="changeSortType(1)">名称</div>
          <div class="sort-menu-item" @click.stop="changeSortType(2)">价格</div>
          <div class="sort-menu-item" @click.stop="changeSortType(3)">折扣</div>
          <div class="sort-menu-item" @click.stop="changeSortType(4)">添加日期</div>
          <div class="sort-menu-item" @click.stop="changeSortType(5)">发行日期</div>
        </div>
      </div>
    </div>
    <div ref="wishlistRef" class="list" :style="{ minHeight: wishlist.length * 180 + 'px' }">
      <div v-if="wishlist.length === 0" class="empty">
        <p>哎呀，这里无内容可显示</p>
        <p>您的愿望单里有 {{ wishlist.length }} 件物品，但均不匹配您在上方应用的筛选条件。</p>
      </div>
      <div v-else>
        <div class="wishlist-item" v-for="(item, index) in wishlist" :key="item.productId">
          <div class="item-logo">
            <img :src="item.productLogo" alt="商品logo">
          </div>
          <div class="item-info">
            <div class="item-name">{{ item.productName }}</div>
            <div class="item-meta">
              <div class="item-rating">
                <span>总体评测：</span>
                <span>{{ '好评如潮' }}</span>
              </div>
              <div class="item-release-date">
                <span>发行日期：</span>
                <span>{{ '2025.4.15' }}</span>
              </div>
            </div>
            <div class="item-tags">
              <span class="tag" v-for="tag in item.tags" :key="tag">{{ tag }}</span>
            </div>
          </div>
          <div class="item-price-action">
            <div class="item-price-container">
              <div class="discount_original_price">¥{{ item.productPrice }}</div>
              <div class="price-and-quantity">
                <div class="discount_final_price">¥{{ item.productPrice*(1-0.01*item.productDiscount) }}</div>
                <div class="item—quantity">数量：{{ wishlistItem[index].productQuantity }}</div>
              </div>
            </div>
            <div>
              <button class="add-to-cart" @click="addToCart(wishlistItem[index],cart)">+1</button>
              <button class="add-to-cart" @click="deleteToCart(wishlistItem[index],cart)">-1</button>
            </div>
            <div class="item-added-info">
              <a href="#" class="remove-link" @click.prevent="removeFromWishlist(wishlistItem[index],cart)">(移除)</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="footer" v-if="wishlist.length > 0">
      <div class="total-price">
        总价格：¥{{ totalPrice }}
      </div>
      <button class="buy-button">购买</button>
    </div>
  </div>
</template>

<style scoped>
.app {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: calc(100vh - 104px);
  background-color: #2c2f33;
}
.header {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 940px;
  padding: 20px 0;
  color: #ffffff;
  font-size: 24px;
}
.filter {
  display: flex;
  align-items: center;
  gap: 16px;
  width: 940px;
  padding: 10px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}
.sort {
  position: relative;
  z-index: 100;
  display: flex;
  align-items: center;
  height: 34px;
  padding: 0 10px;
  margin-right: 12px;
  color: #9099a1;
  font-size: 12px;
  letter-spacing: 1px;
  cursor: pointer;
  transition: color 0.2s, background-color 0.2s;
}
.sort:hover {
  color: #ffffff;
  background-color: rgba(255, 255, 255, 0.1);
}
.sort.focus {
  color: #ffffff;
  background-color: #808a9c;
}
.sort-type {
  color: #b5babc;
  margin-right: 8px;
}
.sort img {
  width: 16px;
  height: 16px;
}
.sort-menu {
  position: absolute;
  left: 0;
  right: 0;
  top: 100%;
  padding: 4px 0 12px 0;
  background-color: #808a9c;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s;
}
.sort.focus > .sort-menu {
  opacity: 1;
  pointer-events: auto;
}
.sort-menu-item {
  display: flex;
  align-items: center;
  height: 30px;
  padding: 0 12px;
  color: #ffffff;
  font-size: 12px;
  transition: background-color 0.1s;
}
.sort-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.2);
}
.list {
  position: relative;
  width: 940px;
  padding: 10px 0 60px 0;
}
.empty {
  padding: 80px 0;
  color: #e5e5e5;
  font-size: 14px;
  text-align: center;
}
.empty :last-child {
  font-size: 12px;
}
.wishlist-item {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  background-color: #363a40;
  border-radius: 4px;
}
.item-logo {
  width: 180px;
  height: 100px;
  margin-right: 12px;
}
.item-logo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}
.item-info {
  flex: 1;
}
.item-name {
  font-size: 16px;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 6px;
}
.item-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 8px;
}
.item-rating,
.item-release-date {
  font-size: 12px;
  color: #8e9297;
}
.item-tags {
  display: flex;
  gap: 8px;
}
.tag {
  font-size: 11px;
  color: #9099a1;
  background-color: rgba(255, 255, 255, 0.1);
  padding: 3px 6px;
  border-radius: 12px;
}
.discount_original_price {
  position: relative;
  width: fit-content;
  color: #738895;
  font-size: 11px;
  line-height: 12px;
  text-decoration: line-through;
}
.price-and-quantity {
  display: flex;
  align-items: center;
  gap: 10px;
}
.discount_final_price {
  color: #BEEE11;
  line-height: 16px;
  font-size: 15px;
}
.item—quantity {
  color: #ffffff;
  font-size: 12px;
}
.add-to-cart {
  margin-right: 10px;
  padding: 6px 12px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}
.add-to-cart:hover {
  background-color: #3d9c40;
}
.item-added-info {
  font-size: 11px;
  color: #9099a1;
  display: flex;
  align-items: center;
  gap: 4px;
}
.remove-link {
  color: #9099a1;
  text-decoration: none;
}
.remove-link:hover {
  text-decoration: underline;
}
.footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 940px;
  padding: 16px 0;
  margin-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}
.total-price {
  font-size: 18px;
  font-weight: bold;
  color: #ffffff;
}
.buy-button {
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.2s;
}
.buy-button:hover {
  background-color: #3d9c40;
}
</style>