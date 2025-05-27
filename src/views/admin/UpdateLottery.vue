<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { createLottery, getLotteryList, addLotteryItem, createLotteryItem, deleteLottery, removeLotteryItemFromLottery, updateLotteryName } from '../../api/lottery.ts'
import { getAllProducts } from '../../api/product.ts'
import { ElMessage, ElMessageBox } from 'element-plus'

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
const loading = ref(false)

// 当前视图状态
const currentView = ref<'list' | 'create' | 'detail'>('list')
const selectedLottery = ref<Lottery | null>(null)

// 创建奖池表单
const createForm = ref({
  lotteryName: '',
  selectedProducts: [] as {productId: number; quantity: number}[]
})

// 添加商品表单
const addItemForm = ref({
  productId: null as number | null,
  quantity: 1
})

// 编辑状态
const editingName = ref(false)
const editNameValue = ref('')

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
      lotteryList.value = res.data.result
    }
  } catch (error) {
    console.error('获取奖池列表失败:', error)
  }
}

// 切换视图
function switchView(view: 'list' | 'create' | 'detail', lottery?: Lottery) {
  currentView.value = view
  if (lottery) {
    selectedLottery.value = lottery
  }
  if (view === 'create') {
    resetCreateForm()
  }
}

// 重置创建表单
function resetCreateForm() {
  createForm.value = {
    lotteryName: '',
    selectedProducts: []
  }
  addItemForm.value = {
    productId: null,
    quantity: 1
  }
}

// 添加商品到创建列表
function addProductToCreate() {
  if (!addItemForm.value.productId || addItemForm.value.quantity <= 0) {
    ElMessage.warning('请选择商品并输入有效数量')
    return
  }
  
  const existingIndex = createForm.value.selectedProducts.findIndex(
    p => p.productId === addItemForm.value.productId
  )
  
  if (existingIndex >= 0) {
    createForm.value.selectedProducts[existingIndex].quantity += addItemForm.value.quantity
  } else {
    createForm.value.selectedProducts.push({
      productId: addItemForm.value.productId,
      quantity: addItemForm.value.quantity
    })
  }
  
  addItemForm.value = { productId: null, quantity: 1 }
  ElMessage.success('商品已添加')
}

// 从创建列表移除商品
function removeProductFromCreate(index: number) {
  createForm.value.selectedProducts.splice(index, 1)
}

// 创建奖池
async function handleCreateLottery() {
  if (!createForm.value.lotteryName.trim()) {
    ElMessage.warning('请输入奖池名称')
    return
  }
  
  loading.value = true
  try {
    // 创建奖池
    const createRes = await createLottery(createForm.value.lotteryName)
    if (createRes.data.code !== '000') {
      ElMessage.error('创建奖池失败')
      return
    }
    
    // 直接使用返回的奖池对象
    const newLottery = createRes.data.result
    
    // 如果有选中的商品，则添加到奖池
    if (createForm.value.selectedProducts.length > 0) {
      let successCount = 0
      for (const item of createForm.value.selectedProducts) {
        try {
          const createItemRes = await createLotteryItem(item.productId, item.quantity)
          if (createItemRes.data.code === '000' && createItemRes.data.result) {
            const addRes = await addLotteryItem(newLottery.lotteryId, createItemRes.data.result)
            if (addRes.data.code === '000') {
              successCount++
            } else {
              console.error(`添加商品失败: ${addRes.data.message}`)
            }
          } else {
            console.error(`创建商品失败: ${createItemRes.data.message || '商品不存在'}`)
          }
        } catch (itemError) {
          console.error('添加商品时出错:', itemError)
        }
      }
      
      if (successCount > 0) {
        ElMessage.success(`奖池创建成功，成功添加 ${successCount}/${createForm.value.selectedProducts.length} 个商品`)
      } else {
        ElMessage.success('奖池创建成功，但没有成功添加任何商品')
      }
    } else {
      ElMessage.success('空奖池创建成功，可以稍后添加商品')
    }
    
    await fetchLotteryList()
    switchView('list')
  } catch (error: any) {
    console.error('创建奖池失败:', error)
    ElMessage.error('创建奖池失败')
  } finally {
    loading.value = false
  }
}

// 删除奖池
async function handleDeleteLottery(lottery: Lottery) {
  try {
    await ElMessageBox.confirm(
      `确定要删除奖池"${lottery.lotteryName}"吗？此操作不可恢复！`,
      '确认删除',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning',
        customClass: 'steam-message-box',
        confirmButtonClass: 'steam-confirm-btn',
        cancelButtonClass: 'steam-cancel-btn'
      }
    )
    
    loading.value = true
    const res = await deleteLottery(lottery.lotteryId)
    
    if (res.data.code === '000') {
      ElMessage.success('奖池删除成功')
      await fetchLotteryList()
      if (selectedLottery.value?.lotteryId === lottery.lotteryId) {
        switchView('list')
      }
    } else {
      ElMessage.error('删除失败')
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除奖池失败:', error)
      ElMessage.error('删除奖池失败')
    }
  } finally {
    loading.value = false
  }
}

// 删除奖池中的商品
async function handleRemoveItem(lotteryId: number, itemId: number) {
  try {
    await ElMessageBox.confirm('确定要移除这个商品吗？', '确认移除', {
      confirmButtonText: '移除',
      cancelButtonText: '取消',
      type: 'warning',
      customClass: 'steam-message-box',
      confirmButtonClass: 'steam-confirm-btn',
      cancelButtonClass: 'steam-cancel-btn'
    })
    
    loading.value = true
    const res = await removeLotteryItemFromLottery(lotteryId, itemId)
    
    if (res.data.code === '000') {
      ElMessage.success('商品移除成功')
      await fetchLotteryList()
      // 更新当前选中的奖池数据
      if (selectedLottery.value) {
        selectedLottery.value = lotteryList.value.find(l => l.lotteryId === selectedLottery.value!.lotteryId) || null
      }
    } else {
      ElMessage.error('移除失败')
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('移除商品失败:', error)
      ElMessage.error('移除商品失败')
    }
  } finally {
    loading.value = false
  }
}

// 编辑奖池名称
function startEditName() {
  editingName.value = true
  editNameValue.value = selectedLottery.value?.lotteryName || ''
}

function cancelEditName() {
  editingName.value = false
  editNameValue.value = ''
}

async function saveEditName() {
  if (!editNameValue.value.trim() || !selectedLottery.value) {
    ElMessage.warning('请输入有效的奖池名称')
    return
  }
  
  try {
    loading.value = true
    const res = await updateLotteryName(selectedLottery.value.lotteryId, editNameValue.value)
    
    if (res.data.code === '000') {
      ElMessage.success('名称更新成功')
      await fetchLotteryList()
      selectedLottery.value = lotteryList.value.find(l => l.lotteryId === selectedLottery.value!.lotteryId) || null
      editingName.value = false
    } else {
      ElMessage.error('更新失败')
    }
  } catch (error: any) {
    console.error('更新名称失败:', error)
    ElMessage.error('更新名称失败')
  } finally {
    loading.value = false
  }
}

// 向现有奖池添加商品
async function addItemToLottery() {
  if (!addItemForm.value.productId || !selectedLottery.value) {
    ElMessage.warning('请选择商品')
    return
  }
  
  try {
    loading.value = true
    const createItemRes = await createLotteryItem(addItemForm.value.productId, addItemForm.value.quantity)
    
    if (createItemRes.data.code === '000' && createItemRes.data.result) {
      const addRes = await addLotteryItem(selectedLottery.value.lotteryId, createItemRes.data.result)
      
      if (addRes.data.code === '000') {
        ElMessage.success('商品添加成功')
        await fetchLotteryList()
        selectedLottery.value = lotteryList.value.find(l => l.lotteryId === selectedLottery.value!.lotteryId) || null
        addItemForm.value = { productId: null, quantity: 1 }
      } else {
        ElMessage.error('添加失败')
      }
    } else {
      ElMessage.error('商品不存在或创建失败')
    }
  } catch (error: any) {
    console.error('添加商品失败:', error)
    ElMessage.error('添加商品失败')
  } finally {
    loading.value = false
  }
}

// 获取商品信息
const getProduct = (productId: number) => {
  return products.value.find(p => p.productId === productId)
}

onMounted(async () => {
  await fetchProducts()
  await fetchLotteryList()
})
</script>

<template>
  <div class="steam-container">
    <div class="steam-content">
      <!-- 页面标题和导航 -->
      <div class="steam-header">
        <h1 class="steam-title">奖池管理系统</h1>
        <div class="steam-nav">
          <button 
            class="steam-nav-btn" 
            :class="{ active: currentView === 'list' }"
            @click="switchView('list')">
            奖池列表
          </button>
          <button 
            class="steam-nav-btn" 
            :class="{ active: currentView === 'create' }"
            @click="switchView('create')">
            创建奖池
          </button>
        </div>
      </div>

      <!-- 奖池列表视图 -->
      <div v-if="currentView === 'list'" class="steam-view">
        <div class="steam-panel">
          <div class="steam-panel-header">
            <h2>奖池列表 ({{ lotteryList.length }})</h2>
          </div>
          <div class="steam-panel-body">
            <div v-if="lotteryList.length === 0" class="steam-empty">
              <i class="steam-icon">🎰</i>
              <p>暂无奖池，点击"创建奖池"开始创建</p>
            </div>
            <div v-else class="steam-lottery-grid">
              <div 
                v-for="lottery in lotteryList" 
                :key="lottery.lotteryId" 
                class="steam-lottery-card"
                @click="switchView('detail', lottery)">
                <div class="steam-card-header">
                  <h3>{{ lottery.lotteryName || `奖池 #${lottery.lotteryId}` }}</h3>
                  <div class="steam-card-actions" @click.stop>
                    <button 
                      class="steam-btn steam-btn-danger steam-btn-sm"
                      @click="handleDeleteLottery(lottery)">
                      删除
                    </button>
                  </div>
                </div>
                <div class="steam-card-body">
                  <div class="steam-card-stat">
                    <span class="steam-stat-label">奖品数量:</span>
                    <span class="steam-stat-value">{{ lottery.lotteryItems?.length || 0 }}</span>
                  </div>
                </div>
                <div class="steam-card-footer">
                  <span class="steam-view-hint">点击查看详情</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 创建奖池视图 -->
      <div v-if="currentView === 'create'" class="steam-view">
        <div class="steam-panel">
          <div class="steam-panel-header">
            <h2>创建新奖池</h2>
          </div>
          <div class="steam-panel-body">
            <!-- 奖池基本信息 -->
            <div class="steam-form-section">
              <h3>奖池信息</h3>
              <div class="steam-form-group">
                <label>奖池名称</label>
                <input 
                  v-model="createForm.lotteryName" 
                  class="steam-input" 
                  placeholder="请输入奖池名称">
              </div>
            </div>

            <!-- 添加商品 -->
            <div class="steam-form-section">
              <h3>添加商品 <span class="steam-hint">（可选，也可以创建空奖池后再添加）</span></h3>
              <div class="steam-form-row">
                <div class="steam-form-group">
                  <label>选择商品</label>
                  <select v-model="addItemForm.productId" class="steam-select">
                    <option :value="null">请选择商品</option>
                    <option 
                      v-for="product in products" 
                      :key="product.productId" 
                      :value="product.productId">
                      {{ product.productName }} (￥{{ product.productPrice }})
                    </option>
                  </select>
                </div>
                <div class="steam-form-group">
                  <label>数量</label>
                  <input 
                    v-model="addItemForm.quantity" 
                    class="steam-input" 
                    type="number" 
                    min="1">
                </div>
                <div class="steam-form-group">
                  <label>&nbsp;</label>
                  <button 
                    class="steam-btn steam-btn-primary"
                    @click="addProductToCreate">
                    添加
                  </button>
                </div>
              </div>
            </div>

            <!-- 已选商品列表 -->
            <div v-if="createForm.selectedProducts.length > 0" class="steam-form-section">
              <h3>已选商品 ({{ createForm.selectedProducts.length }})</h3>
              <div class="steam-item-list">
                <div 
                  v-for="(item, index) in createForm.selectedProducts" 
                  :key="index" 
                  class="steam-item">
                  <div class="steam-item-info">
                    <img 
                      :src="getProduct(item.productId)?.productLogo" 
                      class="steam-item-image"
                      :alt="getProduct(item.productId)?.productName">
                    <div class="steam-item-details">
                      <div class="steam-item-name">{{ getProduct(item.productId)?.productName }}</div>
                      <div class="steam-item-meta">
                        数量: {{ item.quantity }} | 单价: ￥{{ getProduct(item.productId)?.productPrice }}
                      </div>
                    </div>
                  </div>
                  <button 
                    class="steam-btn steam-btn-danger steam-btn-sm"
                    @click="removeProductFromCreate(index)">
                    移除
                  </button>
                </div>
              </div>
            </div>

            <!-- 创建按钮 -->
            <div class="steam-form-actions">
              <div class="steam-create-info">
                <p class="steam-info-text">
                  <i class="steam-info-icon">💡</i>
                  可以先创建空奖池，然后在奖池详情页面添加商品
                </p>
              </div>
              <button 
                class="steam-btn steam-btn-success"
                :disabled="!createForm.lotteryName.trim()"
                @click="handleCreateLottery">
                创建奖池
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 奖池详情视图 -->
      <div v-if="currentView === 'detail' && selectedLottery" class="steam-view">
        <div class="steam-panel">
          <div class="steam-panel-header">
            <div class="steam-detail-header">
              <div v-if="!editingName" class="steam-title-display">
                <div class="steam-title-left">
                  <h2>{{ selectedLottery.lotteryName || `奖池 #${selectedLottery.lotteryId}` }}</h2>
                  <button class="steam-btn steam-btn-secondary steam-btn-sm" @click="startEditName">
                    编辑名称
                  </button>
                </div>
                <div class="steam-stat-inline">
                  <span class="steam-stat-number-inline">{{ selectedLottery.lotteryItems?.length || 0 }}</span>
                  <span class="steam-stat-label-inline">奖品总数</span>
                </div>
              </div>
              <div v-else class="steam-title-edit">
                <input 
                  v-model="editNameValue" 
                  class="steam-input"
                  @keyup.enter="saveEditName"
                  @keyup.esc="cancelEditName">
                <button class="steam-btn steam-btn-success steam-btn-sm" @click="saveEditName">保存</button>
                <button class="steam-btn steam-btn-secondary steam-btn-sm" @click="cancelEditName">取消</button>
              </div>
            </div>
          </div>
          <div class="steam-panel-body">
            <!-- 添加新商品 -->
            <div class="steam-form-section">
              <h3>添加商品到奖池</h3>
              <div class="steam-form-row">
                <div class="steam-form-group">
                  <select v-model="addItemForm.productId" class="steam-select">
                    <option :value="null">请选择商品</option>
                    <option 
                      v-for="product in products" 
                      :key="product.productId" 
                      :value="product.productId">
                      {{ product.productName }} (￥{{ product.productPrice }})
                    </option>
                  </select>
                </div>
                <div class="steam-form-group">
                  <input 
                    v-model="addItemForm.quantity" 
                    class="steam-input" 
                    type="number" 
                    min="1"
                    placeholder="数量">
                </div>
                <div class="steam-form-group">
                  <button 
                    class="steam-btn steam-btn-primary"
                    @click="addItemToLottery">
                    添加商品
                  </button>
                </div>
              </div>
            </div>

            <!-- 奖品列表 -->
            <div class="steam-form-section">
              <h3>奖品列表</h3>
              <div v-if="!selectedLottery.lotteryItems || selectedLottery.lotteryItems.length === 0" class="steam-empty">
                <p>暂无奖品</p>
              </div>
              <div v-else class="steam-item-list">
                <div 
                  v-for="(item, index) in selectedLottery.lotteryItems" 
                  :key="index" 
                  class="steam-item">
                  <div class="steam-item-info">
                    <img 
                      :src="getProduct(item.productId)?.productLogo" 
                      class="steam-item-image"
                      :alt="getProduct(item.productId)?.productName">
                    <div class="steam-item-details">
                      <div class="steam-item-name">
                        {{ getProduct(item.productId)?.productName }}
                      </div>
                      <div class="steam-item-meta">
                        <span>
                          数量: {{ item.productQuantity }} | 价值: ￥{{ item.productValue }}
                        </span>
                        <span class="steam-probability">
                          概率: {{ item.lotteryItemProbability ? (item.lotteryItemProbability * 100).toFixed(2) : '0.00' }}%
                        </span>
                      </div>
                    </div>
                  </div>
                  <button 
                    v-if="item.lotteryItemId"
                    class="steam-btn steam-btn-danger steam-btn-sm"
                    @click="handleRemoveItem(selectedLottery.lotteryId, item.lotteryItemId)">
                    移除
                  </button>
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
      <p>处理中...</p>
    </div>
  </div>
</template>

<style scoped>
/* Steam 基础样式 */
.steam-container {
  background-color: #1b2838;
  min-height: 100vh;
  color: #c7d5e0;
  font-family: "Motiva Sans", Arial, Helvetica, sans-serif;
}

.steam-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 页面头部 */
.steam-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(85, 125, 149, 0.3);
}

.steam-title {
  font-size: 28px;
  color: #ffffff;
  margin: 0;
  font-weight: 300;
  text-shadow: 0 0 3px #67c1f5;
}

.steam-nav {
  display: flex;
  gap: 10px;
}

.steam-nav-btn {
  background: linear-gradient(90deg, #2a3f5a 0%, #1e2d3f 100%);
  border: 1px solid #16202d;
  color: #c7d5e0;
  padding: 10px 20px;
  border-radius: 3px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
}

.steam-nav-btn:hover {
  background: linear-gradient(90deg, #3c6c8c 0%, #2b5069 100%);
  color: #ffffff;
}

.steam-nav-btn.active {
  background: linear-gradient(90deg, #67c1f5 0%, #4e85ab 100%);
  color: #ffffff;
  border-color: #67c1f5;
}

/* 面板样式 */
.steam-panel {
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 3px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
  margin-bottom: 20px;
}

.steam-panel-header {
  background: linear-gradient(90deg, #356480 0%, #1e3b4b 100%);
  padding: 15px 20px;
  border-radius: 3px 3px 0 0;
}

.steam-panel-header h2 {
  color: #ffffff;
  font-size: 18px;
  font-weight: normal;
  margin: 0;
}

.steam-panel-body {
  padding: 20px;
}

/* 详情页头部 */
.steam-detail-header {
  display: flex;
  align-items: center;
  gap: 15px;
  width: 100%;
}

.steam-title-display {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.steam-title-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.steam-stat-inline {
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: rgba(24, 40, 55, 0.6);
  padding: 8px 12px;
  border-radius: 3px;
  border: 1px solid rgba(85, 125, 149, 0.15);
}

.steam-stat-number-inline {
  font-size: 18px;
  color: #67c1f5;
  font-weight: bold;
}

.steam-stat-label-inline {
  color: #8f98a0;
  font-size: 12px;
  text-transform: uppercase;
}

.steam-title-edit {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
}

/* 表单样式 */
.steam-form-section {
  margin-bottom: 30px;
}

.steam-form-section h3 {
  color: #67c1f5;
  font-size: 16px;
  margin-bottom: 15px;
  font-weight: normal;
}

.steam-hint {
  color: #8f98a0;
  font-size: 12px;
  font-weight: normal;
  font-style: italic;
}

.steam-form-row {
  display: flex;
  gap: 15px;
  align-items: end;
}

.steam-form-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.steam-form-group label {
  color: #8f98a0;
  font-size: 14px;
}

.steam-input, .steam-select {
  background-color: #2a3f5a;
  border: 1px solid #16202d;
  color: #c7d5e0;
  padding: 10px 12px;
  border-radius: 3px;
  font-size: 14px;
  min-width: 200px;
}

.steam-input:focus, .steam-select:focus {
  outline: none;
  border-color: #67c1f5;
  box-shadow: 0 0 3px #67c1f5;
}

/* 按钮样式 */
.steam-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
  text-decoration: none;
  display: inline-block;
  text-align: center;
}

.steam-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.steam-btn-sm {
  padding: 6px 12px;
  font-size: 12px;
}

.steam-btn-primary {
  background: linear-gradient(90deg, #3c6c8c 0%, #2b5069 100%);
  color: #c7d5e0;
}

.steam-btn-primary:hover:not(:disabled) {
  background: linear-gradient(90deg, #4e85ab 0%, #3a6a8a 100%);
  color: #ffffff;
}

.steam-btn-success {
  background: linear-gradient(90deg, #6bbb1a 0%, #4e8c12 100%);
  color: #ffffff;
}

.steam-btn-success:hover:not(:disabled) {
  background: linear-gradient(90deg, #7cd421 0%, #5ea515 100%);
}

.steam-btn-danger {
  background: linear-gradient(90deg, #c15755 0%, #a04543 100%);
  color: #ffffff;
}

.steam-btn-danger:hover:not(:disabled) {
  background: linear-gradient(90deg, #d66862 0%, #b85450 100%);
}

.steam-btn-secondary {
  background: linear-gradient(90deg, #8f98a0 0%, #6d7580 100%);
  color: #ffffff;
}

.steam-btn-secondary:hover:not(:disabled) {
  background: linear-gradient(90deg, #a5aeb6 0%, #8a9299 100%);
}

/* 奖池网格 */
.steam-lottery-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.steam-lottery-card {
  background-color: rgba(24, 40, 55, 0.6);
  border-radius: 3px;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid transparent;
}

.steam-lottery-card:hover {
  background-color: rgba(24, 40, 55, 0.8);
  border-color: #67c1f5;
  transform: translateY(-2px);
}

.steam-card-header {
  padding: 15px;
  border-bottom: 1px solid rgba(85, 125, 149, 0.15);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.steam-card-header h3 {
  color: #ffffff;
  font-size: 16px;
  margin: 0;
  font-weight: normal;
}

.steam-card-actions {
  display: flex;
  gap: 5px;
}

.steam-card-body {
  padding: 15px;
}

.steam-card-stat {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.steam-stat-label {
  color: #8f98a0;
  font-size: 14px;
}

.steam-stat-value {
  color: #67c1f5;
  font-size: 14px;
  font-weight: 500;
}

.steam-card-footer {
  padding: 10px 15px;
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 0 0 3px 3px;
  text-align: center;
}

.steam-view-hint {
  color: #8f98a0;
  font-size: 12px;
  font-style: italic;
}

/* 物品列表 */
.steam-item-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.steam-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: rgba(24, 40, 55, 0.6);
  padding: 15px;
  border-radius: 3px;
  border: 1px solid rgba(85, 125, 149, 0.15);
}

.steam-item-info {
  display: flex;
  align-items: center;
  gap: 15px;
  flex: 1;
}

.steam-item-image {
  width: 48px;
  height: 48px;
  object-fit: cover;
  border-radius: 3px;
  background-color: rgba(0, 0, 0, 0.3);
}

.steam-item-details {
  flex: 1;
}

.steam-item-name {
  color: #c7d5e0;
  font-size: 14px;
  margin-bottom: 5px;
  font-weight: 500;
}

.steam-item-meta {
  color: #8f98a0;
  font-size: 12px;
  display: flex;
  gap: 15px;
}

.steam-probability {
  color: #67c1f5 !important;
  font-weight: 500;
}

/* 表单操作 */
.steam-form-actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  padding-top: 20px;
  border-top: 1px solid rgba(85, 125, 149, 0.15);
}

.steam-create-info {
  text-align: center;
}

.steam-info-text {
  color: #8f98a0;
  font-size: 14px;
  margin: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.steam-info-icon {
  font-size: 16px;
}

/* 空状态 */
.steam-empty {
  text-align: center;
  padding: 40px 20px;
  color: #8f98a0;
}

.steam-empty .steam-icon {
  font-size: 48px;
  margin-bottom: 15px;
  display: block;
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
  width: 40px;
  height: 40px;
  border: 3px solid rgba(103, 193, 245, 0.3);
  border-top: 3px solid #67c1f5;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.steam-loading p {
  color: #c7d5e0;
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .steam-header {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }
  
  .steam-nav {
    justify-content: center;
  }
  
  .steam-form-row {
    flex-direction: column;
  }
  
  .steam-lottery-grid {
    grid-template-columns: 1fr;
  }
}
</style>
