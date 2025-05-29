<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { router } from '../../router/index.ts';
import { getUserCart, CartItem, userInfo, getUserCartVO } from "../../api/user.ts";
import { Cart, getProductByProductId, ProductInfo, checkProductStock } from "../../api/product.ts";
import { createOrder, goToPayment } from '../../api/alipay.ts';
import { getOrderStatus } from '../../api/alipay.ts';

const loading = ref(true);
const nickname = ref('')
const userId = ref()
const cart = ref<Cart>()
const wishlistItem = ref<CartItem[]>([])
const cartProducts = ref<ProductInfo[]>([])
const orderId = ref()
// 订单状态相关
const orderStatus = ref<'PENDING' | 'SUCCESS' | 'FAILED' | 'TIMEOUT' | null>(null);
const showOrderStatus = ref(false);
const isPolling = ref(false);
const pollingInterval = ref<number | null>(null);

// 开始轮询订单状态
const startPollingOrderStatus = (id: string) => {
  isPolling.value = true;
  showOrderStatus.value = true;
  
  // 立即检查一次
  checkOrderStatus(id);
  
  // 设置轮询间隔（每3秒检查一次）
  pollingInterval.value = window.setInterval(() => {
    checkOrderStatus(id);
  }, 3000) as unknown as number;
};

// 停止轮询
const stopPollingOrderStatus = () => {
  if (pollingInterval.value) {
    clearInterval(pollingInterval.value);
    pollingInterval.value = null;
  }
  isPolling.value = false;
};

// 检查订单状态
const checkOrderStatus = (id: string) => {
  getOrderStatus(parseInt(id)).then(res => {
    if (res.data && res.data.result) {
      orderStatus.value = res.data.result;
      // 如果状态不是PENDING，停止轮询
      if (orderStatus.value !== 'PENDING') {
        stopPollingOrderStatus();
      }
    }
  }).catch(err => {
    console.error('获取订单状态失败:', err);
  });
};

// 关闭订单状态页面
const closeOrderStatus = () => {
  showOrderStatus.value = false;
  orderStatus.value = null;
  stopPollingOrderStatus();
};

// 重试支付
const retryPayment = () => {
  if (orderId.value) {
    goToPayment(parseInt(orderId.value));
  }
};

const purchaseItems = async () => {
    if (selectedPayment.value != 'alipay') {
        ElMessage({
            message: "暂未支持该支付方式！",
            type: 'error',
            center: true,
        })
        return
    }
    if (!cart.value) {
        ElMessage({
            message: "购物车为空！",
            type: 'error',
            center: true,
        })
        return
    }
    
    // 检查所有商品库存
    try {
        loading.value = true
        for (const item of cartProducts.value) {
            const quantity = (item as any).productQuantity || 1
            const stockRes = await checkProductStock(item.productId, quantity)
            if (stockRes.data.code !== '000') {
                ElMessage({
                    message: `商品"${item.productName}"库存不足，当前库存：${stockRes.data.result || 0}，需要：${quantity}`,
                    type: 'error',
                    center: true,
                })
                return
            }
        }
        
        // 库存充足，创建订单
        const res = await createOrder(userId.value, cart.value)
        orderId.value = res.data.result.ordersId
        await goToPayment(orderId.value)
        // 开始轮询订单状态
        startPollingOrderStatus(orderId.value)
    } catch (error) {
        console.error('创建订单失败:', error)
        ElMessage({
            message: "创建订单失败，请稍后重试",
            type: 'error',
            center: true,
        })
    } finally {
        loading.value = false
    }
}

const getUserInfo = async () => {
  userInfo().then(res => {
    nickname.value = res.data.result.name
    userId.value = res.data.result.id
    getUserCartVO(parseInt(userId.value)).then(res=>{
      cart.value = res.data.result
    })
    getUserCart(parseInt(userId.value)).then(res => {
      wishlistItem.value = res.data
      const productIds = wishlistItem.value.map(item => item.productId)
      const productPromises = productIds.map(id => getProductByProductId(id))
      Promise.all(productPromises).then(productList => {
        const products = productList.map(item => {
          const product = item.data.result
          // 从wishlistItem中找到对应的商品数量
          const cartItem = wishlistItem.value.find(wi => wi.productId === product.productId)
          if (cartItem) {
            // 添加productQuantity属性
            // @ts-ignore - 忽略TypeScript错误
            product.productQuantity = cartItem.productQuantity || 1
          }
          return product
        })
        cartProducts.value = products
      })
    })
  }).then(()=>{
    loading.value = false
  })
}

// 计算折扣后价格
const calculateDiscountedPrice = (price: number, discount: number) => {
    return price * (1 - discount / 100);
};

// 计算总价
const totalPrice = computed(() => {
    return cartProducts.value.reduce((sum, item) => {
        const discountedPrice = calculateDiscountedPrice(item.productPrice, item.productDiscount);
        const quantity = (item as any).productQuantity || 1;
        return sum + (discountedPrice * quantity);
    }, 0);
});

// 返回商店
const backToStore = () => {
    router.push('/');
};

// 选中的支付方式
const selectedPayment = ref('alipay');

onMounted(() => {
  getUserInfo() // 获取用户信息和 wishlist
})
</script>

<template>
    <div class="cart-page">
        <div class="cart-content">
            <div class="cart-header">
                <div class="cart-title">您的购物车</div>
                <div class="store-link" @click="backToStore">继续购物</div>
            </div>

            <div v-if="loading" class="loading-container">
                <div class="loading-spinner"></div>
                <div class="loading-text">加载中...</div>
            </div>

            <div class="cart-items-container" v-else-if="cartProducts.length > 0 && !showOrderStatus">
                <div class="home_tabs_content">
                    <div v-for="item in cartProducts" :key="item.productId" class="tab_item">
                        <div class="tab_item_cap">
                            <img class="tab_item_cap_img" :src="item.productLogo" alt="游戏封面">
                        </div>
                        <div class="discount_block tab_item_discount" v-if="item.productDiscount > 0">
                            <div class="discount_pct">-{{ item.productDiscount }}%</div>
                            <div class="discount_prices">
                                <div class="discount_original_price">¥{{ item.productPrice.toFixed(2) }}</div>
                                <div class="discount_final_price">¥{{ calculateDiscountedPrice(item.productPrice,
                                    item.productDiscount).toFixed(2) }}</div>
                                <div class="item_total_price">小计: ¥{{ (calculateDiscountedPrice(item.productPrice, item.productDiscount) * ((item as any).productQuantity || 1)).toFixed(2) }}</div>
                            </div>
                        </div>
                        <div class="discount_block tab_item_discount" v-else>
                            <div class="discount_pct_placeholder"></div>
                            <div class="discount_prices">
                                <div class="normal_price">¥{{ item.productPrice.toFixed(2) }}</div>
                                <div class="item_total_price">小计: ¥{{ (item.productPrice * ((item as any).productQuantity || 1)).toFixed(2) }}</div>
                            </div>
                        </div>
                        <div class="tab_item_content">
                            <div class="tab_item_name">{{ item.productName }}</div>
                            <div class="item-quantity">
                                数量: {{ (item as any).productQuantity || 1 }}
                            </div>
                        </div>
                        <div class="tab_item_actions">
                        </div>
                        <div style="clear: both;"></div>
                    </div>
                </div>

                <div class="payment-section">
                    <div class="payment-title">选择支付方式</div>
                    <div class="payment-methods">
                        <div class="payment-method" :class="{ active: selectedPayment === 'alipay' }"
                            @click="selectedPayment = 'alipay'">
                            <div class="payment-radio">
                                <div class="radio-inner" v-if="selectedPayment === 'alipay'"></div>
                            </div>
                            <div class="payment-name">支付宝</div>
                        </div>
                        <div class="payment-method" :class="{ active: selectedPayment === 'wechat' }"
                            @click="selectedPayment = 'wechat'">
                            <div class="payment-radio">
                                <div class="radio-inner" v-if="selectedPayment === 'wechat'"></div>
                            </div>
                            <div class="payment-name">微信支付</div>
                        </div>
                        <div class="payment-method" :class="{ active: selectedPayment === 'card' }"
                            @click="selectedPayment = 'card'">
                            <div class="payment-radio">
                                <div class="radio-inner" v-if="selectedPayment === 'card'"></div>
                            </div>
                            <div class="payment-name">银联/信用卡</div>
                        </div>
                    </div>
                </div>

                <div class="cart-summary">
                    <div class="summary-label">预计总价：</div>
                    <div class="summary-total">¥{{ totalPrice.toFixed(2) }}</div>
                </div>

                <div class="cart-agreement">
                    <div class="agreement-text">
                        通过点击"购买"，您同意<span class="agreement-link">《Sbeam订户协议》</span>并确认您已阅读<span
                            class="agreement-link">《Sbeam隐私政策》</span>。
                    </div>
                </div>

                <div class="cart-actions">
                    <button class="continue-shopping" @click="backToStore">继续购物</button>
                    <button class="purchase-button" @click="purchaseItems">购买</button>
                </div>
            </div>

            <!-- 订单状态页面 -->
            <div class="order-status-container" v-else-if="showOrderStatus">
                <div class="order-status-header">
                    <div class="order-status-title">订单支付状态</div>
                    <div class="close-button" @click="closeOrderStatus">&times;</div>
                </div>

                <!-- 处理中状态 -->
                <div class="order-status-content" v-if="orderStatus === 'PENDING'">
                    <div class="status-icon pending">
                        <div class="loading-spinner"></div>
                    </div>
                    <div class="status-title pending">处理中</div>
                    <div class="status-message">
                        您的订单正在处理中，请等待...
                    </div>
                    <div class="status-details">
                        <div class="detail-item">
                            <span class="detail-label">订单号:</span>
                            <span class="detail-value">{{ orderId }}</span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">总金额:</span>
                            <span class="detail-value">¥{{ totalPrice.toFixed(2) }}</span>
                        </div>
                    </div>
                    <div class="status-actions">
                        <button class="action-button" @click="backToStore">返回商店</button>
                    </div>
                </div>

                <!-- 成功状态 -->
                <div class="order-status-content" v-else-if="orderStatus === 'SUCCESS'">
                    <div class="status-icon success">✓</div>
                    <div class="status-title success">支付成功</div>
                    <div class="status-message">
                        您的订单已成功支付，感谢您的购买！
                    </div>
                    <div class="status-details">
                        <div class="detail-item">
                            <span class="detail-label">订单号:</span>
                            <span class="detail-value">{{ orderId }}</span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">总金额:</span>
                            <span class="detail-value">¥{{ totalPrice.toFixed(2) }}</span>
                        </div>
                    </div>
                    <div class="status-actions">
                        <button class="action-button success" @click="backToStore">返回商店</button>
                    </div>
                </div>

                <!-- 失败状态 -->
                <div class="order-status-content" v-else-if="orderStatus === 'FAILED'">
                    <div class="status-icon failed">✗</div>
                    <div class="status-title failed">支付失败</div>
                    <div class="status-message">
                        很遗憾，您的订单支付失败，请重试或选择其他支付方式。
                    </div>
                    <div class="status-details">
                        <div class="detail-item">
                            <span class="detail-label">订单号:</span>
                            <span class="detail-value">{{ orderId }}</span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">总金额:</span>
                            <span class="detail-value">¥{{ totalPrice.toFixed(2) }}</span>
                        </div>
                    </div>
                    <div class="status-actions">
                        <button class="action-button" @click="retryPayment">重新支付</button>
                        <button class="action-button" @click="closeOrderStatus">返回购物车</button>
                    </div>
                </div>

                <!-- 超时状态 -->
                <div class="order-status-content" v-else-if="orderStatus === 'TIMEOUT'">
                    <div class="status-icon timeout">⏱</div>
                    <div class="status-title timeout">支付超时</div>
                    <div class="status-message">
                        您的支付请求已超时，请重试。
                    </div>
                    <div class="status-details">
                        <div class="detail-item">
                            <span class="detail-label">订单号:</span>
                            <span class="detail-value">{{ orderId }}</span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">总金额:</span>
                            <span class="detail-value">¥{{ totalPrice.toFixed(2) }}</span>
                        </div>
                    </div>
                    <div class="status-actions">
                        <button class="action-button" @click="retryPayment">重新支付</button>
                        <button class="action-button" @click="closeOrderStatus">返回购物车</button>
                    </div>
                </div>
            </div>

            <div class="empty-cart" v-else>
                <div class="empty-message">您的购物车内没有物品</div>
                <button class="continue-shopping" @click="backToStore">浏览商店</button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.cart-page {
    background-color: #1b2838;
    color: #c6d4df;
    min-height: 100vh;
    padding: 20px;
    font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
}

.cart-content {
    max-width: 940px;
    margin: 0 auto;
    background-color: #171a21;
    border-radius: 3px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.cart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
    background-color: #222933;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
}

.cart-title {
    font-size: 24px;
    color: #ffffff;
}

.store-link {
    color: #67c1f5;
    cursor: pointer;
    text-decoration: underline;
}

.store-link:hover {
    color: #00aff0;
}

.cart-items-container {
    padding: 20px;
}

/* 加载中样式 */
.loading-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 50px 0;
}

.loading-spinner {
    width: 40px;
    height: 40px;
    border: 4px solid rgba(103, 193, 245, 0.2);
    border-top: 4px solid #67c1f5;
    border-radius: 50%;
    animation: spin 1s linear infinite;
}

.loading-text {
    margin-top: 15px;
    color: #67c1f5;
    font-size: 16px;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}

/* 商品列表样式 */
.home_tabs_content {
    width: 100%;
}

.tab_item {
    position: relative;
    display: block;
    background: rgba(0, 0, 0, 0.2);
    height: 69px;
    margin-bottom: 5px;
    padding-left: 198px;
    -webkit-text-size-adjust: none;
    overflow: hidden;
    z-index: 0;
    text-decoration: none;
    width: 100%;
    box-sizing: border-box;
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
    width: 100%;
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
    object-fit: cover;
}

.tab_item_cap {
    position: absolute;
    left: 0;
    top: 0;
    z-index: 3;
    line-height: 69px;
    transition: opacity 0.25s;
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
    margin-top: 17px;
    width: 133px;
    text-align: right;
    position: relative;
    z-index: 1;
}

.tab_item_discount .discount_pct {
    display: flex;
    align-items: center;
    font-size: 14px;
    line-height: 18px;
    padding: 0 4px;
    border-radius: 1px;
}

.discount_block .discount_pct,
.discount_pct {
    font-family: "Motiva Sans", sans-serif;
    font-weight: 500;
    color: #BEEE11;
    background: #4c6b22;
}

.discount_pct_placeholder {
    display: flex;
    align-items: center;
    font-size: 14px;
    line-height: 18px;
    padding: 0 4px;
    border-radius: 1px;
    /* 占位空间，与discount_pct大小一致但透明 */
    visibility: hidden;
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

.normal_price {
    color: #ffffff;
    line-height: 16px;
    font-size: 15px;
}

.item_total_price {
    color: #67c1f5;
    line-height: 14px;
    font-size: 12px;
    margin-top: 4px;
    font-weight: bold;
}

.tab_item_content {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    padding-top: 6px;
    font-size: 12px;
    width: calc(100% - 150px);
    float: left;
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

.platform-icon {
    width: 20px;
    height: 20px;
    margin-right: 5px;
    margin-top: 4px;
}

.item-quantity {
    color: #8f98a0;
    font-size: 12px;
    margin-top: 4px;
}

.tab_item_actions {
    position: absolute;
    right: 10px;
    bottom: 5px;
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    z-index: 2;
}

.item-remove {
    color: #ff7b7b;
    cursor: pointer;
    font-size: 12px;
}

.item-remove:hover {
    color: #ff3030;
    text-decoration: underline;
}

.payment-section {
    margin-top: 20px;
    padding: 15px;
    background-color: #1a3353;
    border-radius: 3px;
}

.payment-title {
    font-size: 16px;
    color: #ffffff;
    margin-bottom: 10px;
}

.payment-methods {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.payment-method {
    display: flex;
    align-items: center;
    padding: 10px;
    border-radius: 2px;
    cursor: pointer;
    transition: background-color 0.2s;
}

.payment-method:hover {
    background-color: #1e3b5e;
}

.payment-method.active {
    background-color: #203c5c;
}

.payment-radio {
    width: 16px;
    height: 16px;
    border: 1px solid #67c1f5;
    border-radius: 50%;
    margin-right: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.radio-inner {
    width: 8px;
    height: 8px;
    background-color: #67c1f5;
    border-radius: 50%;
}

.payment-name {
    color: #c7d5e0;
}

.cart-summary {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    padding: 20px 0;
    margin-top: 20px;
    border-top: 1px solid #2a3f5a;
}

.summary-label {
    color: #8f98a0;
    font-size: 15px;
    margin-right: 10px;
}

.summary-total {
    color: #ffffff;
    font-size: 18px;
    font-weight: bold;
}

.cart-agreement {
    margin: 15px 0;
    padding: 0 10px;
}

.agreement-text {
    color: #8f98a0;
    font-size: 12px;
    line-height: 1.4;
}

.agreement-link {
    color: #67c1f5;
    cursor: pointer;
}

.agreement-link:hover {
    color: #00aff0;
    text-decoration: underline;
}

.cart-actions {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
}

.continue-shopping,
.purchase-button {
    padding: 10px 20px;
    border-radius: 2px;
    font-size: 16px;
    cursor: pointer;
    border: none;
}

.continue-shopping {
    background-color: #2b4157;
    color: #67c1f5;
}

.continue-shopping:hover {
    background-color: #386088;
    color: #ffffff;
}

.purchase-button {
    background: linear-gradient(to right, #75b022, #588a1b);
    color: #ffffff;
    padding: 12px 24px;
}

.purchase-button:hover {
    background: linear-gradient(to right, #8ed629, #6db320);
}

.empty-cart {
    padding: 50px 20px;
    text-align: center;
}

.empty-message {
    font-size: 18px;
    color: #8f98a0;
    margin-bottom: 20px;
}

/* 订单状态样式 */
.order-status-container {
    padding: 30px;
}

.order-status-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
}

.order-status-title {
    font-size: 24px;
    color: #ffffff;
}

.close-button {
    font-size: 28px;
    color: #67c1f5;
    cursor: pointer;
}

.close-button:hover {
    color: #00aff0;
}

.order-status-content {
    background-color: #1a3353;
    border-radius: 3px;
    padding: 30px;
    text-align: center;
}

.status-icon {
    width: 70px;
    height: 70px;
    margin: 0 auto 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    font-size: 36px;
}

.status-icon.pending {
    background-color: #2a3f5a;
}

.status-icon.success {
    background-color: #4c6b22;
    color: #BEEE11;
}

.status-icon.failed {
    background-color: #892020;
    color: #ff7b7b;
}

.status-icon.timeout {
    background-color: #7e5f20;
    color: #ffcb41;
}

.status-title {
    font-size: 24px;
    margin-bottom: 15px;
}

.status-title.pending {
    color: #67c1f5;
}

.status-title.success {
    color: #BEEE11;
}

.status-title.failed {
    color: #ff7b7b;
}

.status-title.timeout {
    color: #ffcb41;
}

.status-message {
    color: #c7d5e0;
    font-size: 16px;
    margin-bottom: 25px;
}

.status-details {
    background-color: rgba(0, 0, 0, 0.2);
    padding: 15px;
    border-radius: 3px;
    margin-bottom: 30px;
    text-align: left;
}

.detail-item {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
}

.detail-item:last-child {
    margin-bottom: 0;
}

.detail-label {
    color: #8f98a0;
}

.detail-value {
    color: #ffffff;
    font-weight: bold;
}

.status-actions {
    display: flex;
    gap: 15px;
    justify-content: center;
}

.action-button {
    padding: 10px 20px;
    border-radius: 2px;
    font-size: 16px;
    cursor: pointer;
    border: none;
    background-color: #2b4157;
    color: #67c1f5;
}

.action-button:hover {
    background-color: #386088;
    color: #ffffff;
}

.action-button.success {
    background: linear-gradient(to right, #75b022, #588a1b);
    color: #ffffff;
}

.action-button.success:hover {
    background: linear-gradient(to right, #8ed629, #6db320);
}
</style>
