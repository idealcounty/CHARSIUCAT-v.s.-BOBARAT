<template>
  <div class="buy-chances-container">
    <div class="buy-chances-content">
      <!-- 页面标题 -->
      <div class="page-header">
        <button class="back-btn" @click="goBack">
          ← 返回抽奖
        </button>
        <h1 class="page-title">购买抽奖次数</h1>
      </div>

      <!-- 购买界面 -->
      <div v-if="!showOrderStatus">
        <!-- 当前抽奖次数显示 -->
        <div class="current-chances">
          <div class="chances-card">
            <div class="chances-icon">🎫</div>
            <div class="chances-info">
              <div class="chances-label">当前抽奖次数</div>
              <div class="chances-count">{{ currentChances }} 次</div>
            </div>
          </div>
        </div>

        <!-- 购买区域 -->
        <div class="purchase-section">
          <div class="purchase-card">
            <h2 class="section-title">购买抽奖次数</h2>
            
            <!-- 价格说明 -->
            <div class="price-info">
              <div class="price-item">
                <span class="price-label">单价</span>
                <span class="price-value">¥17/次</span>
              </div>
            </div>

            <!-- 数量选择器 -->
            <div class="quantity-selector">
              <label class="quantity-label">购买数量</label>
              <div class="quantity-controls">
                <button 
                  class="quantity-btn decrease" 
                  @click="decreaseQuantity"
                  :disabled="quantity <= 1">
                  -
                </button>
                <input 
                  type="number" 
                  v-model.number="quantity" 
                  class="quantity-input"
                  min="1"
                  max="100"
                  @input="validateQuantity">
                <button 
                  class="quantity-btn increase" 
                  @click="increaseQuantity"
                  :disabled="quantity >= 100">
                  +
                </button>
              </div>
            </div>

            <!-- 支付方式选择 -->
            <div class="payment-section">
              <div class="payment-title">选择支付方式</div>
              <div class="payment-methods">
                <div class="payment-method active">
                  <div class="payment-radio">
                    <div class="radio-inner"></div>
                  </div>
                  <div class="payment-name">支付宝</div>
                </div>
              </div>
            </div>

            <!-- 总价显示 -->
            <div class="total-price">
              <div class="total-label">总计</div>
              <div class="total-value">¥{{ totalPrice.toFixed(2) }}</div>
            </div>

            <!-- 协议 -->
            <div class="cart-agreement">
              <div class="agreement-text">
                通过点击"购买"，您同意<span class="agreement-link">《Sbeam订户协议》</span>并确认您已阅读<span class="agreement-link">《Sbeam隐私政策》</span>。
              </div>
            </div>

            <!-- 错误信息 -->
            <div class="error-message" v-if="errorMessage">
              <div class="error-content">
                <span class="error-icon">⚠️</span>
                <span class="error-text">{{ errorMessage }}</span>
                <button class="error-close" @click="errorMessage = ''">&times;</button>
              </div>
            </div>

            <!-- 购买按钮 -->
            <button 
              class="purchase-btn" 
              @click="handlePurchase"
              :disabled="purchasing || quantity <= 0">
              <span v-if="purchasing">处理中...</span>
              <span v-else>💰 立即支付</span>
            </button>
          </div>
        </div>
      </div>

      <!-- 订单状态页面 -->
      <div class="order-status-container" v-else-if="showOrderStatus" @click="handleBackdropClick">
        <div class="order-status-modal" @click.stop>
          <div class="order-status-header">
            <div class="order-status-title">订单支付状态</div>
            <div class="close-button" @click="closeOrderStatus" v-if="orderStatus === 'FAILED' || orderStatus === 'TIMEOUT' || orderStatus === null">&times;</div>
          </div>

          <!-- 处理中状态 -->
          <div class="order-status-content" v-if="orderStatus === 'PENDING'">
            <div class="status-icon pending">
              <div class="loading-spinner"></div>
            </div>
            <div class="status-title pending">支付处理中</div>
            <div class="status-message">
              您的订单正在处理中，请等待支付完成...<br>
              <small class="processing-notice">⚠️ 支付处理中请勿关闭窗口</small>
            </div>
            <div class="status-details">
              <div class="detail-item">
                <span class="detail-label">订单号:</span>
                <span class="detail-value">{{ orderId }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">购买数量:</span>
                <span class="detail-value">{{ quantity }} 次</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">总金额:</span>
                <span class="detail-value">¥{{ totalPrice.toFixed(2) }}</span>
              </div>
            </div>
            <div class="status-actions">
              <button class="action-button" @click="refreshOrderStatus">🔄 立即刷新</button>
              <button class="action-button" @click="continueWaiting" v-if="!isPolling">▶️ 继续等待</button>
              <button class="action-button" @click="goBack">返回抽奖</button>
            </div>
          </div>

          <!-- 成功状态 -->
          <div class="order-status-content" v-else-if="orderStatus === 'SUCCESS'">
            <div class="status-icon success">✓</div>
            <div class="status-title success">支付成功！</div>
            <div class="status-message">
              恭喜您！订单已成功支付，{{ quantity }} 次抽奖次数已到账！
            </div>
            <div class="status-details">
              <div class="detail-item">
                <span class="detail-label">订单号:</span>
                <span class="detail-value">{{ orderId }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">购买数量:</span>
                <span class="detail-value">{{ quantity }} 次</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">支付金额:</span>
                <span class="detail-value">¥{{ totalPrice.toFixed(2) }}</span>
              </div>
              <div class="detail-item success-highlight">
                <span class="detail-label">当前抽奖次数:</span>
                <span class="detail-value">{{ currentChances }} 次</span>
              </div>
            </div>
            <div class="status-actions">
              <button class="action-button success" @click="goBack">立即去抽奖</button>
            </div>
          </div>

          <!-- 失败状态 -->
          <div class="order-status-content" v-else-if="orderStatus === 'FAILED'">
            <div class="status-icon failed">✗</div>
            <div class="status-title failed">支付失败</div>
            <div class="status-message">
              很遗憾，您的订单支付失败，请重试或联系客服。
            </div>
            <div class="status-details">
              <div class="detail-item">
                <span class="detail-label">订单号:</span>
                <span class="detail-value">{{ orderId }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">失败原因:</span>
                <span class="detail-value">支付失败或被取消</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">订单金额:</span>
                <span class="detail-value">¥{{ totalPrice.toFixed(2) }}</span>
              </div>
            </div>
            <div class="status-actions">
              <button class="action-button" @click="retryPayment">重新支付</button>
              <button class="action-button" @click="closeOrderStatus">修改订单</button>
            </div>
          </div>

          <!-- 超时状态 -->
          <div class="order-status-content" v-else-if="orderStatus === 'TIMEOUT'">
            <div class="status-icon failed">✗</div>
            <div class="status-title failed">支付超时</div>
            <div class="status-message">
              支付超时，请重试或联系客服。
            </div>
            <div class="status-details">
              <div class="detail-item">
                <span class="detail-label">订单号:</span>
                <span class="detail-value">{{ orderId }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">订单金额:</span>
                <span class="detail-value">¥{{ totalPrice.toFixed(2) }}</span>
              </div>
            </div>
            <div class="status-actions">
              <button class="action-button" @click="retryPayment">重新支付</button>
              <button class="action-button" @click="closeOrderStatus">修改订单</button>
            </div>
          </div>

          <!-- 未知状态 -->
          <div class="order-status-content" v-else>
            <div class="status-icon pending">
              <div class="loading-spinner"></div>
            </div>
            <div class="status-title">查询中...</div>
            <div class="status-message">
              正在查询订单状态，请稍候...
            </div>
            <div class="status-actions">
              <button class="action-button" @click="closeOrderStatus">关闭</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { createLotteryOrder, goToLotteryPayment, getLotteryOrderStatus } from '../../api/lottery'
import { userInfo } from '../../api/user'

const router = useRouter()

// 响应式数据
const currentChances = ref(0)
const quantity = ref(1)
const purchasing = ref(false)
const userId = ref(0)
const orderId = ref<number | null>(null)
const errorMessage = ref('')

// 订单状态相关
const orderStatus = ref<'PENDING' | 'SUCCESS' | 'FAILED' | 'TIMEOUT' | null>(null)
const showOrderStatus = ref(false)
const isPolling = ref(false)
const pollingInterval = ref<number | null>(null)

// 计算属性
const totalPrice = computed(() => quantity.value * 17)

// 方法
function goBack() {
  // 停止轮询再跳转
  stopPollingOrderStatus()
  // 重置购买状态
  purchasing.value = false
  router.push('/lottery')
}

function increaseQuantity() {
  if (quantity.value < 100) {
    quantity.value++
  }
}

function decreaseQuantity() {
  if (quantity.value > 1) {
    quantity.value--
  }
}

function validateQuantity() {
  if (quantity.value < 1) {
    quantity.value = 1
  } else if (quantity.value > 100) {
    quantity.value = 100
  }
}

// 开始轮询订单状态
const startPollingOrderStatus = (id: number) => {
  // 如果已经在轮询，先停止
  if (isPolling.value) {
    stopPollingOrderStatus()
  }
  
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
const checkOrderStatus = (id: number) => {
  getLotteryOrderStatus(id).then(res => {
    if (res.data && res.data.code === '000') {
      const newStatus = res.data.result;
      orderStatus.value = newStatus;
      
      // 如果状态不是PENDING，停止轮询并刷新用户信息
      if (newStatus !== 'PENDING') {
        stopPollingOrderStatus();
        purchasing.value = false; // 重置购买状态
        
        // 清理localStorage中的订单ID
        localStorage.removeItem('pendingLotteryOrderId');
        
        if (newStatus === 'SUCCESS') {
          // 支付成功，刷新用户抽奖次数
          fetchUserInfo();
        }
      }
    } else {
      console.error('查询订单状态失败:', res.data.msg)
    }
  }).catch(err => {
    console.error('获取订单状态失败:', err);
  });
};

// 手动刷新订单状态
const refreshOrderStatus = () => {
  if (orderId.value) {
    checkOrderStatus(orderId.value)
  }
}

// 继续等待（不关闭弹窗，继续轮询）
const continueWaiting = () => {
  // 如果没有在轮询，重新启动轮询
  if (!isPolling.value && orderId.value) {
    startPollingOrderStatus(orderId.value)
  }
}

// 关闭订单状态页面
const closeOrderStatus = () => {
  // 在关闭前最后检查一次订单状态（如果还有订单ID的话）
  if (orderId.value && orderStatus.value === 'PENDING') {
    // 最后检查一次状态，不等待结果
    getLotteryOrderStatus(orderId.value).then(res => {
      if (res.data && res.data.code === '000') {
        const finalStatus = res.data.result;
        if (finalStatus === 'SUCCESS') {
          // 如果最终状态是成功，刷新用户信息
          fetchUserInfo();
        }
      }
    }).catch(() => {
      // 忽略错误，因为窗口已经关闭
    });
  }
  
  // 重置所有状态
  showOrderStatus.value = false;
  orderStatus.value = null;
  purchasing.value = false;
  errorMessage.value = '';
  
  // 清理localStorage
  localStorage.removeItem('pendingLotteryOrderId');
  
  stopPollingOrderStatus();
};

// 重试支付
const retryPayment = () => {
  if (orderId.value) {
    // 重置状态
    purchasing.value = true;
    // 先停止当前轮询
    stopPollingOrderStatus()
    goToLotteryPayment(orderId.value).then(() => {
      // 重新开始轮询订单状态
      if (orderId.value) {
        startPollingOrderStatus(orderId.value);
      }
    }).catch(error => {
      console.error('重试支付失败:', error);
      purchasing.value = false;
    });
  }
};

// 购买处理函数
const handlePurchase = () => {
  if (quantity.value <= 0 || purchasing.value) {
    return;
  }

  // 清理之前的错误信息
  errorMessage.value = '';
  purchasing.value = true;
  
  createLotteryOrder(userId.value, quantity.value).then(res => {
    if (res.data.code === '000') {
      orderId.value = res.data.result.lotteryOrderId;
      if (orderId.value) {
        // 保存订单ID到localStorage以便恢复
        localStorage.setItem('pendingLotteryOrderId', orderId.value.toString());
        
        setTimeout(() => {
          if (orderId.value) {
            goToLotteryPayment(orderId.value).then(() => {
              if (orderId.value) {
                startPollingOrderStatus(orderId.value);
                
                setTimeout(() => {
                  if (isPolling.value && orderStatus.value === 'PENDING') {
                    // 支付超时处理
                  }
                }, 300000); // 5分钟
              }
            }).catch(error => {
              console.error('支付页面跳转失败:', error);
              purchasing.value = false;
              localStorage.removeItem('pendingLotteryOrderId');
              errorMessage.value = '支付页面跳转失败，请重试';
            });
          }
        }, 500);
      }
    } else {
      console.error('创建订单失败:', res.data.msg);
      purchasing.value = false;
      errorMessage.value = res.data.msg;
    }
  }).catch(error => {
    console.error('购买失败:', error);
    purchasing.value = false;
    errorMessage.value = '购买失败，请重试';
  });
};

// 获取用户信息
async function fetchUserInfo() {
  try {
    const res = await userInfo()
    if (res.data.code === '000') {
      const result = res.data.result
      userId.value = result.id
      currentChances.value = result.lotteryChances || 0
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 恢复未完成的订单状态
const restorePendingOrder = () => {
  const pendingOrderId = localStorage.getItem('pendingLotteryOrderId');
  if (pendingOrderId) {
    orderId.value = parseInt(pendingOrderId);
    // 检查这个订单的状态
    getLotteryOrderStatus(orderId.value).then(res => {
      if (res.data && res.data.code === '000') {
        const status = res.data.result;
        if (status === 'PENDING') {
          // 如果还在处理中，恢复轮询
          purchasing.value = true;
          startPollingOrderStatus(orderId.value!);
        } else {
          // 如果已完成，清理缓存
          localStorage.removeItem('pendingLotteryOrderId');
          purchasing.value = false;
          if (status === 'SUCCESS') {
            fetchUserInfo();
          }
        }
      } else {
        // 订单查询失败，清理缓存
        localStorage.removeItem('pendingLotteryOrderId');
        purchasing.value = false;
      }
    }).catch(() => {
      // 查询失败，清理缓存
      localStorage.removeItem('pendingLotteryOrderId');
      purchasing.value = false;
    });
  }
};

// 添加键盘事件处理
const handleKeydown = (event: KeyboardEvent) => {
  // 防止在PENDING状态时按ESC关闭窗口
  if (event.key === 'Escape' && showOrderStatus.value && orderStatus.value !== 'PENDING') {
    closeOrderStatus()
  }
}

// 添加点击事件处理
const handleBackdropClick = () => {
  // 防止在PENDING状态时点击外部关闭窗口
  if (orderStatus.value !== 'PENDING') {
    closeOrderStatus()
  }
}

onMounted(() => {
  fetchUserInfo()
  restorePendingOrder()
  // 添加键盘事件监听
  document.addEventListener('keydown', handleKeydown)
})

// 组件卸载时清理
onUnmounted(() => {
  stopPollingOrderStatus()
  // 如果还有pending状态的订单，保留localStorage，否则清理
  if (!purchasing.value) {
    localStorage.removeItem('pendingLotteryOrderId');
  }
  // 移除键盘事件监听
  document.removeEventListener('keydown', handleKeydown)
})
</script>

<style scoped>
.buy-chances-container {
  background-color: #1b2838;
  color: #c6d4df;
  min-height: 100vh;
  padding: 20px;
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
}

.buy-chances-content {
  max-width: 940px;
  margin: 0 auto;
  background-color: #171a21;
  border-radius: 3px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.page-header {
  display: flex;
  align-items: center;
  padding: 16px;
  background-color: #222933;
  border-top-left-radius: 3px;
  border-top-right-radius: 3px;
  gap: 15px;
}

.back-btn {
  background-color: #2b4157;
  border: 1px solid #495663;
  color: #67c1f5;
  padding: 8px 16px;
  border-radius: 2px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
}

.back-btn:hover {
  background-color: #386088;
  color: #ffffff;
}

.page-title {
  color: #ffffff;
  font-size: 24px;
  font-weight: normal;
  margin: 0;
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
}

.current-chances {
  padding: 20px;
  border-bottom: 1px solid #2a3f5a;
}

.chances-card {
  background: linear-gradient(135deg, rgba(103, 193, 245, 0.15), rgba(103, 193, 245, 0.08));
  border: 1px solid rgba(103, 193, 245, 0.4);
  border-radius: 3px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  transition: all 0.2s ease;
  box-shadow: 0 2px 8px rgba(103, 193, 245, 0.1);
}

.chances-card:hover {
  border-color: rgba(103, 193, 245, 0.6);
  box-shadow: 0 4px 12px rgba(103, 193, 245, 0.2);
  transform: translateY(-1px);
}

.chances-icon {
  font-size: 32px;
}

.chances-info {
  flex: 1;
}

.chances-label {
  color: #8f98a0;
  font-size: 14px;
  margin-bottom: 5px;
}

.chances-count {
  color: #67c1f5;
  font-size: 20px;
  font-weight: bold;
}

.purchase-section {
  padding: 20px;
}

.purchase-card {
  background: linear-gradient(135deg, #1a3353, #152b47);
  border-radius: 3px;
  padding: 20px;
  border: 1px solid #2a3f5a;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.section-title {
  color: #ffffff;
  font-size: 18px;
  font-weight: normal;
  margin-bottom: 20px;
  text-align: left;
  border-bottom: 1px solid #2a3f5a;
  padding-bottom: 10px;
}

.price-info {
  background: linear-gradient(135deg, rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.2));
  border-radius: 3px;
  padding: 15px;
  margin-bottom: 20px;
  border: 1px solid #2a3f5a;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.3);
}

.price-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price-label {
  color: #8f98a0;
  font-size: 14px;
}

.price-value {
  color: #BEEE11;
  font-size: 16px;
  font-weight: bold;
}

.quantity-selector {
  margin-bottom: 20px;
}

.quantity-label {
  display: block;
  color: #c7d5e0;
  font-size: 14px;
  margin-bottom: 10px;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: flex-start;
}

.quantity-btn {
  width: 32px;
  height: 32px;
  border: 1px solid #495663;
  background: #2b4157;
  color: #67c1f5;
  border-radius: 2px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.2s ease;
}

.quantity-btn:hover:not(:disabled) {
  background: #386088;
  color: #ffffff;
}

.quantity-btn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.quantity-input {
  width: 60px;
  height: 32px;
  text-align: center;
  border: 1px solid #495663;
  border-radius: 2px;
  background-color: #1a3353;
  color: #c7d5e0;
  font-size: 14px;
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
}

.quantity-input:focus {
  outline: none;
  border-color: #67c1f5;
}

.payment-section {
  margin-bottom: 20px;
}

.payment-title {
  color: #c7d5e0;
  font-size: 14px;
  margin-bottom: 10px;
}

.payment-methods {
  display: flex;
  align-items: center;
  gap: 10px;
}

.payment-method {
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid #2a3f5a;
  border-radius: 3px;
  padding: 10px 15px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.payment-method.active {
  background: linear-gradient(to right, #1677ff, #0958d9);
  border-color: #1677ff;
  box-shadow: 0 2px 8px rgba(22, 119, 255, 0.3);
}

.payment-radio {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  border: 2px solid #495663;
  display: flex;
  align-items: center;
  justify-content: center;
}

.payment-method.active .payment-radio {
  border-color: #ffffff;
}

.radio-inner {
  width: 6px;
  height: 6px;
  background-color: #ffffff;
  border-radius: 50%;
}

.payment-name {
  font-size: 14px;
  color: #c7d5e0;
}

.payment-method.active .payment-name {
  color: #ffffff;
  font-weight: bold;
}

.total-price {
  background: linear-gradient(135deg, rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.2));
  border: 1px solid #2a3f5a;
  border-radius: 3px;
  padding: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.3);
}

.total-label {
  color: #8f98a0;
  font-size: 16px;
}

.total-value {
  color: #BEEE11;
  font-size: 18px;
  font-weight: bold;
}

.cart-agreement {
  margin-bottom: 20px;
  padding: 0 5px;
}

.agreement-text {
  color: #8f98a0;
  font-size: 12px;
  line-height: 1.4;
}

.agreement-link {
  color: #67c1f5;
  cursor: pointer;
  text-decoration: none;
}

.agreement-link:hover {
  color: #00aff0;
  text-decoration: underline;
}

.error-message {
  margin-bottom: 20px;
  padding: 12px 15px;
  background: linear-gradient(135deg, rgba(204, 51, 51, 0.15), rgba(204, 51, 51, 0.08));
  border-radius: 3px;
  border: 1px solid rgba(204, 51, 51, 0.4);
  box-shadow: 0 2px 8px rgba(204, 51, 51, 0.1);
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.error-content {
  display: flex;
  align-items: center;
  gap: 10px;
}

.error-icon {
  color: #cc3333;
  font-size: 16px;
  flex-shrink: 0;
}

.error-text {
  color: #cc3333;
  font-size: 14px;
  flex: 1;
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
}

.error-close {
  background: none;
  border: none;
  color: #8f98a0;
  font-size: 18px;
  cursor: pointer;
  padding: 4px;
  border-radius: 2px;
  transition: all 0.2s ease;
  flex-shrink: 0;
}

.error-close:hover {
  color: #cc3333;
  background-color: rgba(204, 51, 51, 0.1);
}

.purchase-btn {
  width: 100%;
  height: 40px;
  border-radius: 2px;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  text-align: center;
  text-decoration: none;
  color: #67c1f5;
  background: rgba(103, 193, 245, 0.2);
  white-space: nowrap;
  font-size: 16px;
  font-weight: bold;
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
  position: relative;
  overflow: hidden;
  transition: all 0.2s ease;
}

.purchase-btn:hover:not(:disabled) {
  color: #ffffff;
  background: linear-gradient(to right, #66bff3, #437d9e);
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(103, 193, 245, 0.3);
}

.purchase-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  background: #495663;
  color: #8f98a0;
  transform: none;
  box-shadow: none;
}

.purchase-btn:disabled::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 20px;
  width: 16px;
  height: 16px;
  margin-top: -8px;
  border: 2px solid transparent;
  border-top: 2px solid #8f98a0;
  border-radius: 50%;
  animation: buttonSpin 1s linear infinite;
}

@keyframes buttonSpin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 订单状态页面样式 */
.order-status-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.order-status-modal {
  background-color: #171a21;
  border-radius: 3px;
  max-width: 500px;
  width: 90%;
  position: relative;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
  border: 1px solid #2a3f5a;
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from {
    transform: translateY(30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.order-status-header {
  background-color: #222933;
  border-top-left-radius: 3px;
  border-top-right-radius: 3px;
  padding: 16px;
  margin: 0;
  border-bottom: 1px solid #2a3f5a;
  position: relative;
}

.order-status-title {
  color: #ffffff;
  font-size: 18px;
  font-weight: normal;
  margin: 0;
}

.close-button {
  position: absolute;
  top: 12px;
  right: 16px;
  font-size: 20px;
  cursor: pointer;
  color: #8f98a0;
  transition: color 0.2s ease;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-button:hover {
  color: #67c1f5;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
}

.order-status-content {
  padding: 30px;
  text-align: center;
}

.status-icon {
  width: 60px;
  height: 60px;
  margin: 0 auto 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  font-size: 28px;
  font-weight: bold;
}

.status-icon.pending {
  background-color: rgba(103, 193, 245, 0.1);
  border: 2px solid #67c1f5;
  color: #67c1f5;
}

.status-icon.success {
  background-color: rgba(190, 238, 17, 0.1);
  border: 2px solid #BEEE11;
  color: #BEEE11;
  animation: successPulse 0.6s ease;
}

@keyframes successPulse {
  0% {
    transform: scale(0.8);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.status-icon.failed {
  background-color: rgba(255, 123, 123, 0.1);
  border: 2px solid #ff7b7b;
  color: #ff7b7b;
}

.loading-spinner {
  width: 24px;
  height: 24px;
  border: 3px solid rgba(103, 193, 245, 0.2);
  border-top: 3px solid #67c1f5;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.status-title {
  color: #ffffff;
  font-size: 20px;
  font-weight: normal;
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

.status-message {
  color: #c7d5e0;
  font-size: 14px;
  margin-bottom: 25px;
  line-height: 1.5;
  padding: 0 10px;
}

.processing-notice {
  color: #ffcb41;
  font-size: 12px;
  font-weight: bold;
  display: block;
  margin-top: 8px;
  padding: 5px 10px;
  background: rgba(255, 203, 65, 0.1);
  border-radius: 3px;
  border: 1px solid rgba(255, 203, 65, 0.3);
}

.status-details {
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid #2a3f5a;
  border-radius: 3px;
  padding: 15px;
  margin-bottom: 25px;
  text-align: left;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  padding: 4px 0;
}

.detail-item:last-child {
  margin-bottom: 0;
}

.detail-item.success-highlight {
  background: rgba(190, 238, 17, 0.1);
  border-radius: 3px;
  padding: 8px;
  margin-top: 8px;
  border: 1px solid rgba(190, 238, 17, 0.3);
}

.detail-item.success-highlight .detail-label {
  color: #BEEE11;
  font-weight: bold;
}

.detail-item.success-highlight .detail-value {
  color: #BEEE11;
  font-weight: bold;
  font-size: 14px;
}

.detail-label {
  color: #8f98a0;
  font-size: 13px;
}

.detail-value {
  color: #c7d5e0;
  font-size: 13px;
  font-weight: bold;
}

.status-actions {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-top: 25px;
  flex-wrap: wrap;
}

.action-button {
  padding: 10px 20px;
  border: none;
  border-radius: 2px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.2s ease;
  min-width: 100px;
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
  background-color: #2b4157;
  color: #67c1f5;
  border: 1px solid #495663;
}

.action-button:hover {
  background-color: #386088;
  color: #ffffff;
  transform: translateY(-1px);
}

.action-button.success {
  background: linear-gradient(to right, #75b022, #588a1b);
  color: #ffffff;
  border: 1px solid #75b022;
  animation: buttonGlow 2s ease-in-out infinite alternate;
}

@keyframes buttonGlow {
  from {
    box-shadow: 0 0 5px rgba(117, 176, 34, 0.3);
  }
  to {
    box-shadow: 0 0 15px rgba(117, 176, 34, 0.6);
  }
}

.action-button.success:hover {
  background: linear-gradient(to right, #8ed629, #6db320);
  animation: none;
  box-shadow: 0 0 20px rgba(117, 176, 34, 0.8);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .buy-chances-container {
    padding: 10px;
  }
  
  .purchase-card {
    padding: 15px;
  }
  
  .page-title {
    font-size: 20px;
  }
  
  .quantity-controls {
    justify-content: center;
  }
  
  .order-status-header {
    width: 95%;
    margin: 10px;
  }
  
  .order-status-content {
    padding: 20px;
  }
}
</style> 