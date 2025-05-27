<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getLotteryList, drawLottery } from '../../api/lottery.ts'
import { getAllProducts } from '../../api/product.ts'
import { ElMessage } from 'element-plus'

// 定义类型接口
interface Product {
  productId: number;
  productName: string;
  productPrice: number;
  productLogo: string;
}

interface LotteryItem {
  lotteryItemId?: number;
  productId: number;
  productQuantity: number;
  productValue?: number;
  lotteryItemProbability?: number;
}

interface Lottery {
  lotteryId: number;
  lotteryName: string;
  lotteryItems: LotteryItem[];
}

// 响应式数据
const products = ref<Product[]>([])
const lotteryList = ref<Lottery[]>([])
const selectedLottery = ref<Lottery | null>(null)
const loading = ref(false)
const isDrawing = ref(false)
const drawResult = ref<LotteryItem | null>(null)
const showResult = ref(false)

// 模拟用户ID（实际项目中应该从用户状态获取）
const userId = ref(1)

// 抽奖动画相关
const spinAngle = ref(0)
const animationDuration = ref(3000)

// 轮盘相关
const hoveredSector = ref(-1)

// 获取所有商品
async function fetchProducts() {
  try {
    const res = await getAllProducts()
    if (res.data.code === '000') {
      products.value = res.data.result
    }
  } catch (error) {
    console.error('获取商品列表失败:', error)
  }
}

// 获取所有奖池
async function fetchLotteryList() {
  try {
    const res = await getLotteryList()
    if (res.data.code === '000') {
      lotteryList.value = res.data.result.filter((lottery: Lottery) => 
        lottery.lotteryItems && lottery.lotteryItems.length > 0
      )
    }
  } catch (error) {
    console.error('获取奖池列表失败:', error)
  }
}

// 选择奖池
function selectLottery(lottery: Lottery) {
  selectedLottery.value = lottery
  drawResult.value = null
  showResult.value = false
}

// 执行抽奖
async function handleDraw() {
  if (!selectedLottery.value) {
    ElMessage.warning('请先选择一个奖池')
    return
  }

  if (isDrawing.value) {
    return
  }

  isDrawing.value = true
  showResult.value = false
  
  // 开始旋转动画
  const startTime = Date.now()
  const targetAngle = 360 * 5 + Math.random() * 360 // 旋转5圈加随机角度
  
  const animate = () => {
    const elapsed = Date.now() - startTime
    const progress = Math.min(elapsed / animationDuration.value, 1)
    
    // 使用缓动函数
    const easeOut = 1 - Math.pow(1 - progress, 3)
    spinAngle.value = targetAngle * easeOut
    
    if (progress < 1) {
      requestAnimationFrame(animate)
    } else {
      // 动画结束，执行抽奖
      performDraw()
    }
  }
  
  requestAnimationFrame(animate)
}

// 实际执行抽奖API调用
async function performDraw() {
  try {
    const res = await drawLottery(selectedLottery.value!.lotteryId, userId.value)
    
    if (res.data.code === '000') {
      drawResult.value = res.data.result
      
      // 延迟显示结果，增加悬念
      setTimeout(async () => {
        showResult.value = true
        isDrawing.value = false
        
        if (drawResult.value) {
          ElMessage.success('恭喜中奖！')
        } else {
          ElMessage.info('很遗憾，没有中奖，再试一次吧！')
        }
        
        // 刷新奖池数据（因为奖品数量可能已减少）
        await refreshCurrentLottery()
      }, 500)
    } else {
      ElMessage.error('抽奖失败，请重试')
      isDrawing.value = false
    }
  } catch (error: any) {
    console.error('抽奖失败:', error)
    ElMessage.error('抽奖失败，请重试')
    isDrawing.value = false
  }
}

// 刷新当前奖池数据
async function refreshCurrentLottery() {
  try {
    // 获取完整的奖池列表（不过滤空奖池）
    const res = await getLotteryList()
    if (res.data.code === '000') {
      // 更新过滤后的奖池列表（用于奖池选择页面）
      lotteryList.value = res.data.result.filter((lottery: Lottery) => 
        lottery.lotteryItems && lottery.lotteryItems.length > 0
      )
      
      // 如果有选中的奖池，从完整列表中查找并更新
      if (selectedLottery.value) {
        const updatedLottery = res.data.result.find(
          (lottery: Lottery) => lottery.lotteryId === selectedLottery.value!.lotteryId
        )
        
        if (updatedLottery) {
          // 更新当前选中的奖池数据
          selectedLottery.value = updatedLottery
        } else {
          // 奖池被删除的情况
          selectedLottery.value = null
          ElMessage.warning('当前奖池已被删除，请选择其他奖池')
        }
      }
    }
  } catch (error) {
    console.error('刷新奖池数据失败:', error)
    ElMessage.error('刷新奖池数据失败，请重试')
  }
}

// 重新抽奖
async function drawAgain() {
  drawResult.value = null
  showResult.value = false
  spinAngle.value = 0
  
  // 刷新当前奖池数据，确保显示最新的奖品信息
  await refreshCurrentLottery()
}

// 获取商品信息
const getProduct = (productId: number) => {
  return products.value.find(p => p.productId === productId)
}

// 计算奖池总价值
const getLotteryTotalValue = (lottery: Lottery) => {
  return lottery.lotteryItems.reduce((total, item) => {
    return total + (item.productValue || 0) * item.productQuantity
  }, 0)
}

// 计算中奖概率
const getTotalWinProbability = (lottery: Lottery) => {
  return lottery.lotteryItems.reduce((total, item) => {
    return total + (item.lotteryItemProbability || 0)
  }, 0)
}

// 生成SVG扇形路径
const getSectorPath = (index: number, totalItems: number) => {
  const centerX = 200
  const centerY = 200
  const radius = 180
  
  // 特殊处理：只有一个奖品时，创建完整的圆
  if (totalItems === 1) {
    return `M ${centerX} ${centerY} m -${radius}, 0 a ${radius},${radius} 0 1,0 ${radius * 2},0 a ${radius},${radius} 0 1,0 -${radius * 2},0`
  }
  
  const anglePerSector = 360 / totalItems
  const startAngle = index * anglePerSector
  const endAngle = (index + 1) * anglePerSector
  
  const startAngleRad = (startAngle * Math.PI) / 180
  const endAngleRad = (endAngle * Math.PI) / 180
  
  const x1 = centerX + radius * Math.cos(startAngleRad)
  const y1 = centerY + radius * Math.sin(startAngleRad)
  const x2 = centerX + radius * Math.cos(endAngleRad)
  const y2 = centerY + radius * Math.sin(endAngleRad)
  
  const largeArcFlag = anglePerSector > 180 ? 1 : 0
  
  return `M ${centerX} ${centerY} L ${x1} ${y1} A ${radius} ${radius} 0 ${largeArcFlag} 1 ${x2} ${y2} Z`
}

// 计算扇形内容的变换位置
const getSectorContentTransform = (index: number, totalItems: number) => {
  // 特殊处理：只有一个奖品时，显示在轮盘中央
  if (totalItems === 1) {
    return `translate(200, 200)`
  }
  
  const anglePerSector = 360 / totalItems
  const angle = index * anglePerSector + anglePerSector / 2
  const radius = 120 // 内容距离中心的距离
  
  const angleRad = (angle * Math.PI) / 180
  const x = 200 + radius * Math.cos(angleRad)
  const y = 200 + radius * Math.sin(angleRad)
  
  return `translate(${x}, ${y})`
}

onMounted(async () => {
  await fetchProducts()
  await fetchLotteryList()
})
</script>

<template>
  <div class="steam-lottery-container">
    <div class="steam-lottery-content">
      <!-- 页面标题 -->
      <div class="steam-lottery-header">
        <h1 class="steam-lottery-title">🎰 Steam 抽奖中心</h1>
        <p class="steam-lottery-subtitle">选择您喜欢的奖池，试试运气吧！</p>
      </div>

      <!-- 奖池选择区域 -->
      <div class="steam-lottery-pools" v-if="!selectedLottery">
        <h2 class="steam-section-title">选择奖池</h2>
        <div v-if="lotteryList.length === 0" class="steam-empty">
          <div class="steam-empty-icon">🎁</div>
          <p>暂无可用奖池</p>
        </div>
        <div v-else class="steam-pools-grid">
          <div 
            v-for="lottery in lotteryList" 
            :key="lottery.lotteryId"
            class="steam-pool-card"
            @click="selectLottery(lottery)">
            <div class="steam-pool-header">
              <h3>{{ lottery.lotteryName }}</h3>
              <div class="steam-pool-stats">
                <span class="steam-stat">{{ lottery.lotteryItems.length }} 种奖品</span>
              </div>
            </div>
            <div class="steam-pool-preview">
              <div class="steam-preview-items">
                <div 
                  v-for="(item, index) in lottery.lotteryItems.slice(0, 4)" 
                  :key="index"
                  class="steam-preview-item">
                  <img 
                    :src="getProduct(item.productId)?.productLogo" 
                    :alt="getProduct(item.productId)?.productName"
                    class="steam-preview-image">
                </div>
                <div v-if="lottery.lotteryItems.length > 4" class="steam-preview-more">
                  +{{ lottery.lotteryItems.length - 4 }}
                </div>
              </div>
            </div>
            <div class="steam-pool-footer">
              <div class="steam-pool-value">
                总价值: ￥{{ getLotteryTotalValue(lottery).toFixed(2) }}
              </div>
              <div class="steam-pool-probability">
                中奖率: {{ (getTotalWinProbability(lottery) * 100).toFixed(1) }}%
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 抽奖界面 -->
      <div class="steam-lottery-game" v-if="selectedLottery">
        <!-- 返回按钮 -->
        <button class="steam-back-btn" @click="selectedLottery = null">
          ← 返回奖池选择
        </button>

        <!-- 当前奖池信息 -->
        <div class="steam-current-pool">
          <h2>{{ selectedLottery.lotteryName }}</h2>
          <div class="steam-pool-info">
            <span>{{ selectedLottery.lotteryItems.length }} 种奖品</span>
            <span>中奖率: {{ (getTotalWinProbability(selectedLottery) * 100).toFixed(1) }}%</span>
          </div>
        </div>

        <!-- 奖品展示轮盘 -->
        <div class="steam-lottery-wheel-container" v-if="selectedLottery.lotteryItems && selectedLottery.lotteryItems.length > 0">
          <svg class="steam-lottery-wheel" :style="{ transform: `rotate(${spinAngle}deg)` }" viewBox="0 0 400 400">
            <!-- 定义渐变 -->
            <defs>
              <radialGradient 
                v-for="(item, index) in selectedLottery.lotteryItems" 
                :key="`gradient-${index}`"
                :id="`gradient-${index}`"
                cx="50%" cy="30%" r="70%">
                <stop offset="0%" :stop-color="`hsl(${(360 / selectedLottery.lotteryItems.length) * index}, 70%, 60%)`" />
                <stop offset="100%" :stop-color="`hsl(${(360 / selectedLottery.lotteryItems.length) * index}, 70%, 40%)`" />
              </radialGradient>
            </defs>
            
            <!-- 轮盘扇形 -->
            <g v-for="(item, index) in selectedLottery.lotteryItems" :key="`wheel-${item.lotteryItemId || item.productId}-${index}`">
              <!-- 扇形路径 -->
              <path 
                :d="getSectorPath(index, selectedLottery.lotteryItems.length)"
                :fill="`url(#gradient-${index})`"
                :stroke="'rgba(255, 255, 255, 0.4)'"
                stroke-width="2"
                class="steam-wheel-sector"
                @mouseover="hoveredSector = index"
                @mouseleave="hoveredSector = -1"
                :class="{ 'hovered': hoveredSector === index }"
              />
              
              <!-- 奖品内容 -->
              <g :transform="getSectorContentTransform(index, selectedLottery.lotteryItems.length)">
                <!-- 商品图片 -->
                <foreignObject 
                  :x="selectedLottery.lotteryItems.length === 1 ? -40 : -20" 
                  :y="selectedLottery.lotteryItems.length === 1 ? -50 : -35" 
                  :width="selectedLottery.lotteryItems.length === 1 ? 80 : 40" 
                  :height="selectedLottery.lotteryItems.length === 1 ? 80 : 40">
                  <div style="width: 100%; height: 100%; display: flex; align-items: center; justify-content: center;">
                    <img 
                      :src="getProduct(item.productId)?.productLogo" 
                      :alt="getProduct(item.productId)?.productName"
                      class="steam-wheel-image"
                      :style="`width: ${selectedLottery.lotteryItems.length === 1 ? '72px' : '36px'}; height: ${selectedLottery.lotteryItems.length === 1 ? '72px' : '36px'}; border-radius: 6px; border: 3px solid rgba(255,255,255,0.8); object-fit: cover;`">
                  </div>
                </foreignObject>
                
                <!-- 商品名称 -->
                <text 
                  x="0" 
                  :y="selectedLottery.lotteryItems.length === 1 ? 40 : 20" 
                  text-anchor="middle" 
                  class="steam-wheel-name"
                  fill="white"
                  :font-size="selectedLottery.lotteryItems.length === 1 ? '14' : '10'"
                  font-weight="bold">
                  {{ selectedLottery.lotteryItems.length === 1 ? getProduct(item.productId)?.productName : (getProduct(item.productId)?.productName?.substring(0, 8) || '') + ((getProduct(item.productId)?.productName?.length || 0) > 8 ? '...' : '') }}
                </text>
                
                <!-- 数量 -->
                <text 
                  x="0" 
                  :y="selectedLottery.lotteryItems.length === 1 ? 60 : 35" 
                  text-anchor="middle" 
                  class="steam-wheel-quantity"
                  fill="rgba(255,255,255,0.9)"
                  :font-size="selectedLottery.lotteryItems.length === 1 ? '12' : '8'">
                  x{{ item.productQuantity }}
                </text>
              </g>
            </g>
          </svg>
          
          <!-- 指针 -->
          <div class="steam-wheel-pointer"></div>
        </div>

        <!-- 空奖池提示 -->
        <div class="steam-empty-pool" v-if="!selectedLottery.lotteryItems || selectedLottery.lotteryItems.length === 0">
          <div class="steam-empty-pool-content">
            <div class="steam-empty-icon">🎁</div>
            <h3>奖池已空</h3>
            <p>该奖池中的所有奖品都已被抽完</p>
            <button class="steam-btn steam-btn-primary" @click="selectedLottery = null">
              选择其他奖池
            </button>
          </div>
        </div>

        <!-- 抽奖按钮 -->
        <div class="steam-draw-section" v-if="selectedLottery.lotteryItems && selectedLottery.lotteryItems.length > 0">
          <button 
            class="steam-draw-btn"
            :class="{ 'drawing': isDrawing }"
            :disabled="isDrawing"
            @click="handleDraw">
            <span v-if="!isDrawing">🎲 开始抽奖</span>
            <span v-else>🌀 抽奖中...</span>
          </button>
        </div>

        <!-- 抽奖结果 -->
        <div class="steam-result-modal" v-if="showResult">
          <div class="steam-result-content">
            <div v-if="drawResult" class="steam-result-win">
              <div class="steam-result-icon">🎉</div>
              <h3>恭喜中奖！</h3>
              <div class="steam-result-item">
                <img 
                  :src="getProduct(drawResult.productId)?.productLogo" 
                  :alt="getProduct(drawResult.productId)?.productName"
                  class="steam-result-image">
                <div class="steam-result-details">
                  <div class="steam-result-name">{{ getProduct(drawResult.productId)?.productName }}</div>
                  <div class="steam-result-value">价值: ￥{{ drawResult.productValue }}</div>
                  <div class="steam-result-quantity">数量: {{ drawResult.productQuantity }}</div>
                </div>
              </div>
            </div>
            <div v-else class="steam-result-lose">
              <div class="steam-result-icon">😔</div>
              <h3>很遗憾，没有中奖</h3>
              <p>不要灰心，再试一次吧！</p>
            </div>
            <div class="steam-result-actions">
              <button class="steam-btn steam-btn-primary" @click="drawAgain">再抽一次</button>
              <button class="steam-btn steam-btn-secondary" @click="selectedLottery = null">返回奖池</button>
            </div>
          </div>
        </div>

        <!-- 奖品列表 -->
        <div class="steam-prizes-list" v-if="selectedLottery.lotteryItems && selectedLottery.lotteryItems.length > 0">
          <h3>奖池内容</h3>
          <div class="steam-prizes-grid">
            <div 
              v-for="(item, index) in selectedLottery.lotteryItems" 
              :key="`prize-${item.lotteryItemId || item.productId}-${index}`"
              class="steam-prize-card">
              <img 
                :src="getProduct(item.productId)?.productLogo" 
                :alt="getProduct(item.productId)?.productName"
                class="steam-prize-image">
              <div class="steam-prize-info">
                <div class="steam-prize-name">{{ getProduct(item.productId)?.productName }}</div>
                <div class="steam-prize-meta">
                  <span>数量: {{ item.productQuantity }}</span>
                  <span>价值: ￥{{ item.productValue }}</span>
                  <span class="steam-prize-probability">
                    概率: {{ item.lotteryItemProbability ? (item.lotteryItemProbability * 100).toFixed(2) : '0.00' }}%
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 加载遮罩 -->
    <div v-if="loading" class="steam-loading">
      <div class="steam-spinner"></div>
      <p>加载中...</p>
    </div>
  </div>
</template>

<style scoped>
/* Steam 基础样式 */
.steam-lottery-container {
  background: linear-gradient(135deg, #1b2838 0%, #2a475e 100%);
  min-height: 100vh;
  color: #c7d5e0;
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
  position: relative;
  overflow-x: hidden;
}

.steam-lottery-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

/* 页面标题 */
.steam-lottery-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 40px 0;
  background: linear-gradient(90deg, rgba(103, 193, 245, 0.1) 0%, rgba(67, 133, 245, 0.1) 100%);
  border-radius: 10px;
  border: 1px solid rgba(103, 193, 245, 0.2);
}

.steam-lottery-title {
  font-size: 48px;
  color: #ffffff;
  margin: 0 0 10px 0;
  font-weight: 300;
  text-shadow: 0 0 20px #67c1f5;
  animation: glow 2s ease-in-out infinite alternate;
}

@keyframes glow {
  from { text-shadow: 0 0 20px #67c1f5; }
  to { text-shadow: 0 0 30px #67c1f5, 0 0 40px #67c1f5; }
}

.steam-lottery-subtitle {
  font-size: 18px;
  color: #8f98a0;
  margin: 0;
}

/* 区域标题 */
.steam-section-title {
  color: #67c1f5;
  font-size: 24px;
  margin-bottom: 20px;
  font-weight: normal;
  text-align: center;
}

/* 奖池网格 */
.steam-pools-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.steam-pool-card {
  background: linear-gradient(145deg, rgba(24, 40, 55, 0.9) 0%, rgba(16, 29, 44, 0.9) 100%);
  border-radius: 8px;
  border: 1px solid rgba(85, 125, 149, 0.3);
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
  position: relative;
}

.steam-pool-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(103, 193, 245, 0.1), transparent);
  transition: left 0.5s;
}

.steam-pool-card:hover::before {
  left: 100%;
}

.steam-pool-card:hover {
  transform: translateY(-5px);
  border-color: #67c1f5;
  box-shadow: 0 10px 30px rgba(103, 193, 245, 0.3);
}

.steam-pool-header {
  padding: 20px;
  border-bottom: 1px solid rgba(85, 125, 149, 0.2);
}

.steam-pool-header h3 {
  color: #ffffff;
  font-size: 20px;
  margin: 0 0 10px 0;
  font-weight: normal;
}

.steam-pool-stats {
  color: #8f98a0;
  font-size: 14px;
}

.steam-pool-preview {
  padding: 15px 20px;
}

.steam-preview-items {
  display: flex;
  gap: 10px;
  align-items: center;
}

.steam-preview-item {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  overflow: hidden;
  border: 1px solid rgba(85, 125, 149, 0.3);
}

.steam-preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.steam-preview-more {
  color: #67c1f5;
  font-size: 14px;
  font-weight: bold;
}

.steam-pool-footer {
  padding: 15px 20px;
  background: rgba(0, 0, 0, 0.2);
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}

.steam-pool-value {
  color: #6bbb1a;
  font-weight: bold;
}

.steam-pool-probability {
  color: #67c1f5;
  font-weight: bold;
}

/* 抽奖游戏界面 */
.steam-back-btn {
  background: linear-gradient(90deg, #8f98a0 0%, #6d7580 100%);
  border: 1px solid #6d7580;
  color: #ffffff;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  margin-bottom: 20px;
  transition: all 0.2s;
}

.steam-back-btn:hover {
  background: linear-gradient(90deg, #a5aeb6 0%, #8a9299 100%);
  transform: translateY(-1px);
}

.steam-current-pool {
  text-align: center;
  margin-bottom: 40px;
  padding: 20px;
  background: rgba(24, 40, 55, 0.6);
  border-radius: 8px;
  border: 1px solid rgba(85, 125, 149, 0.3);
}

.steam-current-pool h2 {
  color: #ffffff;
  font-size: 28px;
  margin: 0 0 10px 0;
  font-weight: normal;
}

.steam-pool-info {
  color: #8f98a0;
  font-size: 16px;
  display: flex;
  justify-content: center;
  gap: 30px;
}

/* 抽奖轮盘 */
.steam-lottery-wheel-container {
  position: relative;
  width: 400px;
  height: 400px;
  margin: 40px auto;
}

.steam-lottery-wheel {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  position: relative;
  border: 4px solid #67c1f5;
  box-shadow: 0 0 30px rgba(103, 193, 245, 0.5);
  transition: transform 3s cubic-bezier(0.25, 0.1, 0.25, 1);
  background: #1b2838;
}

/* SVG扇形样式 */
.steam-wheel-sector {
  transition: all 0.3s ease;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.steam-wheel-sector:hover,
.steam-wheel-sector.hovered {
  filter: brightness(1.2) drop-shadow(0 4px 8px rgba(0, 0, 0, 0.5));
  transform-origin: 200px 200px;
}

/* SVG文本样式 */
.steam-wheel-name {
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.8);
  dominant-baseline: middle;
}

.steam-wheel-quantity {
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.8);
  dominant-baseline: middle;
}

/* 图片样式 */
.steam-wheel-image {
  object-fit: cover;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.5));
}

.steam-wheel-pointer {
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 15px solid transparent;
  border-right: 15px solid transparent;
  border-top: 30px solid #ff6b6b;
  z-index: 10;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.5));
}

/* 抽奖按钮 */
.steam-draw-section {
  text-align: center;
  margin: 40px 0;
}

.steam-draw-btn {
  background: linear-gradient(90deg, #67c1f5 0%, #4e85ab 100%);
  border: 2px solid #67c1f5;
  color: #ffffff;
  padding: 15px 40px;
  border-radius: 8px;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.steam-draw-btn:hover:not(:disabled) {
  background: linear-gradient(90deg, #7dd1ff 0%, #5e95bb 100%);
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(103, 193, 245, 0.4);
}

.steam-draw-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.steam-draw-btn.drawing {
  animation: pulse 1.5s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

/* 结果模态框 */
.steam-result-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.steam-result-content {
  background: linear-gradient(145deg, #1b2838 0%, #2a475e 100%);
  border-radius: 10px;
  border: 2px solid #67c1f5;
  padding: 40px;
  text-align: center;
  max-width: 500px;
  width: 90%;
  animation: slideIn 0.5s ease;
}

@keyframes slideIn {
  from { transform: translateY(-50px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

.steam-result-icon {
  font-size: 60px;
  margin-bottom: 20px;
}

.steam-result-win h3 {
  color: #6bbb1a;
  font-size: 28px;
  margin-bottom: 20px;
}

.steam-result-lose h3 {
  color: #ff6b6b;
  font-size: 28px;
  margin-bottom: 20px;
}

.steam-result-item {
  display: flex;
  align-items: center;
  gap: 20px;
  margin: 20px 0;
  padding: 20px;
  background: rgba(24, 40, 55, 0.6);
  border-radius: 8px;
}

.steam-result-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
  border: 2px solid #67c1f5;
}

.steam-result-details {
  text-align: left;
  flex: 1;
}

.steam-result-name {
  color: #ffffff;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 5px;
}

.steam-result-value {
  color: #6bbb1a;
  font-size: 16px;
  margin-bottom: 3px;
}

.steam-result-quantity {
  color: #8f98a0;
  font-size: 14px;
}

.steam-result-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-top: 30px;
}

/* 奖品列表 */
.steam-prizes-list {
  margin-top: 60px;
}

.steam-prizes-list h3 {
  color: #67c1f5;
  font-size: 20px;
  margin-bottom: 20px;
  text-align: center;
}

.steam-prizes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 15px;
}

.steam-prize-card {
  background: rgba(24, 40, 55, 0.6);
  border-radius: 6px;
  border: 1px solid rgba(85, 125, 149, 0.3);
  padding: 15px;
  display: flex;
  align-items: center;
  gap: 15px;
  transition: all 0.2s;
}

.steam-prize-card:hover {
  background: rgba(24, 40, 55, 0.8);
  border-color: #67c1f5;
}

.steam-prize-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid rgba(85, 125, 149, 0.3);
}

.steam-prize-info {
  flex: 1;
}

.steam-prize-name {
  color: #ffffff;
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 5px;
}

.steam-prize-meta {
  display: flex;
  flex-direction: column;
  gap: 2px;
  font-size: 12px;
  color: #8f98a0;
}

.steam-prize-probability {
  color: #67c1f5 !important;
  font-weight: bold;
}

/* 按钮样式 */
.steam-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
}

.steam-btn-primary {
  background: linear-gradient(90deg, #67c1f5 0%, #4e85ab 100%);
  color: #ffffff;
}

.steam-btn-primary:hover {
  background: linear-gradient(90deg, #7dd1ff 0%, #5e95bb 100%);
  transform: translateY(-1px);
}

.steam-btn-secondary {
  background: linear-gradient(90deg, #8f98a0 0%, #6d7580 100%);
  color: #ffffff;
}

.steam-btn-secondary:hover {
  background: linear-gradient(90deg, #a5aeb6 0%, #8a9299 100%);
  transform: translateY(-1px);
}

/* 空状态 */
.steam-empty {
  text-align: center;
  padding: 60px 20px;
  color: #8f98a0;
}

.steam-empty-icon {
  font-size: 80px;
  margin-bottom: 20px;
  opacity: 0.5;
}

/* 空奖池提示 */
.steam-empty-pool {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  margin: 40px 0;
}

.steam-empty-pool-content {
  text-align: center;
  padding: 60px 40px;
  background: rgba(24, 40, 55, 0.6);
  border-radius: 10px;
  border: 1px solid rgba(85, 125, 149, 0.3);
  max-width: 500px;
}

.steam-empty-pool-content .steam-empty-icon {
  font-size: 100px;
  margin-bottom: 30px;
  opacity: 0.7;
}

.steam-empty-pool-content h3 {
  color: #ffffff;
  font-size: 28px;
  margin-bottom: 15px;
  font-weight: normal;
}

.steam-empty-pool-content p {
  color: #8f98a0;
  font-size: 16px;
  margin-bottom: 30px;
  line-height: 1.5;
}

/* 加载状态 */
.steam-loading {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.steam-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(103, 193, 245, 0.3);
  border-top: 4px solid #67c1f5;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .steam-lottery-content {
    padding: 10px;
  }
  
  .steam-lottery-title {
    font-size: 32px;
  }
  
  .steam-pools-grid {
    grid-template-columns: 1fr;
  }
  
  .steam-lottery-wheel-container {
    width: 300px;
    height: 300px;
  }
  
  .steam-wheel-name {
    font-size: 8px;
  }
  
  .steam-wheel-quantity {
    font-size: 6px;
  }
  
  .steam-pool-info {
    flex-direction: column;
    gap: 10px;
  }
  
  .steam-result-content {
    padding: 20px;
  }
  
  .steam-result-item {
    flex-direction: column;
    text-align: center;
  }
  
  .steam-result-details {
    text-align: center;
  }
  
  .steam-prizes-grid {
    grid-template-columns: 1fr;
  }
}
</style>  