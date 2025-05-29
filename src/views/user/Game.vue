<script setup lang="ts">
import {onMounted, ref, computed} from "vue";
import {userInfo,getInventory,InventoryInfo,getUserGameCount} from "../../api/user.ts";
import {getProductByProductId, searchProducts} from "../../api/product.ts";

const userId = ref(0)
const userName = ref('')
const userPhone = ref('')
const userPassword = ref('')
const userAddress = ref('')
const userRole = ref('')
const userCreateTime = ref('')
const userBalance = ref(0)
const userAvatar = ref('')

const name = ref('')
const password = ref('')
const address = ref('')

const AvatarUrl = ref('')
const inventory = ref<InventoryInfo[]>([])
const gameList = ref<any[]>([])
const allGameList = ref<any[]>([]) // 存储所有游戏数据
const gameCount = ref(0)
const searchKeyword = ref('') // 搜索关键词

// 计算属性：根据搜索关键词过滤游戏列表
const filteredGameList = computed(() => {
  if (!searchKeyword.value.trim()) {
    return allGameList.value
  }
  return allGameList.value.filter(game => 
    game.productName.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

// 搜索功能
async function searchGames() {
  if (!searchKeyword.value.trim()) {
    gameList.value = allGameList.value
    return
  }
  
  try {
    // 从用户库存中搜索匹配的游戏
    gameList.value = allGameList.value.filter(game => 
      game.productName.toLowerCase().includes(searchKeyword.value.toLowerCase())
    )
  } catch (error) {
    console.error('搜索游戏失败:', error)
  }
}

// 实时搜索：监听搜索关键词变化
function onSearchInput() {
  searchGames()
}

async function getUserInfo() {
  const res = await userInfo()
  console.log(res.data.code)
  if (res.data.code === '000') {
    const result = res.data.result
    userId.value = result.id
    userName.value = result.name
    userPhone.value = result.userPhone
    userPassword.value = result.password
    userAddress.value = result.address
    userRole.value = result.role
    userCreateTime.value = result.createTime
    userBalance.value = result.balance
    userAvatar.value = result.avatar

    name.value = userName.value
    password.value = userPassword.value
    address.value = userAddress.value
    AvatarUrl.value = userAvatar.value || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'
  } else if (res.data.code === '400') {
    console.log('未登录')
  }
}

async function getProductName(productId: number) {
  const res = await getProductByProductId(productId)
  // console.log(res.data.result.productName)
  return res.data.result.productName
}

async function getProductLogo(productId: number) {
  const res = await getProductByProductId(productId)
  // console.log(res.data.result.productLogo)
  return res.data.result.productLogo
}

// 获取用户游戏种类数量
async function getGameCount() {
  try {
    const res = await getUserGameCount(userId.value)
    if (res.data.code === '000') {
      gameCount.value = res.data.result
    }
  } catch (error) {
    console.error('获取游戏种类数量失败:', error)
  }
}

onMounted(async () => {
  await getUserInfo()
  await getGameCount()
  const res = await getInventory(userId.value)
  const rawInventory = res.data.result

  // 统计每个 productId 的总数量（累加 productQuantity）
  const countMap = new Map<number, number>()
  rawInventory.forEach((item: InventoryInfo) => {
    const currentCount = countMap.get(item.productId) || 0
    countMap.set(item.productId, currentCount + item.productQuantity)
  })

  // 去重并获取产品信息
  const uniqueProductIds = Array.from(countMap.keys())
  allGameList.value = await Promise.all(
      uniqueProductIds.map(async (productId) => {
        const res = await getProductByProductId(productId)
        const product = res.data.result
        return {
          productId,
          count: countMap.get(productId),
          productName: product.productName,
          productLogo: product.productLogo
        }
      })
  )
  gameList.value = [...allGameList.value] // 初始化gameList
})


</script>

<template>
  <div class="app">
    <div class="app_background"></div>
    <div class="pagecontent">
      <div class="profile_small_header_bg">
        <div class="profile_small_header_texture">
          <img class="profile_small_header_avatar" :src="userAvatar || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'">
          <div class="profile_small_header_text">
            <span class="profile_small_header_name">{{ userName }}</span>
            <span class="profile_small_header_arrow">»</span>
            <span class="profile_small_header_location">游戏</span>
          </div>
          <div class="profile_small_header_additional">
          </div>
        </div>
      </div>
      <div class="gamelist-root">
        <div class="gamelist Panel Focusable">
          <div class="sectionTabs item responsive_tab_ctn sharedfiles_responsive_tab">
            <div class="sectionTab active">
              <span>拥有游戏总数: ({{ gameCount }})</span>
            </div>
          </div>
          <div class="responsive_tab_baseline baseline"></div>
          <div class="gamelist-nav">
            <div class="search">
              <input placeholder="寻找游戏" v-model="searchKeyword" @input="onSearchInput">
              <div class="search-icon">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 36 36" fill="none"><path d="M27.5 24C29.4972 21.1283 30.3471 17.6129 29.8823 14.146C29.4174 10.679 27.6716 7.5117 24.9884 5.26751C22.3052 3.02332 18.8792 1.86488 15.3846 2.02023C11.8901 2.17559 8.58036 3.63349 6.10692 6.10692C3.63349 8.58036 2.17559 11.8901 2.02023 15.3846C1.86488 18.8792 3.02332 22.3052 5.26751 24.9884C7.5117 27.6716 10.679 29.4174 14.146 29.8823C17.6129 30.3471 21.1283 29.4972 24 27.5L30.26 33.77L30.62 33.41L33.44 30.59L33.8 30.23L27.5 24ZM16 25C14.22 25 12.4799 24.4722 10.9999 23.4832C9.51983 22.4943 8.36628 21.0887 7.68509 19.4442C7.0039 17.7996 6.82567 15.99 7.17294 14.2442C7.52021 12.4984 8.37737 10.8947 9.63605 9.63605C10.8947 8.37737 12.4984 7.52021 14.2442 7.17294C15.99 6.82567 17.7996 7.0039 19.4442 7.68509C21.0887 8.36628 22.4943 9.51983 23.4832 10.9999C24.4722 12.4799 25 14.22 25 16C25 18.387 24.0518 20.6761 22.364 22.364C20.6761 24.0518 18.387 25 16 25Z" fill="#969696"></path></svg>
              </div>
            </div>
          </div>
          <div class="games Panel Focusable" style="height: 1650px;">
            <div
                class="game"
                v-for="(game, index) in filteredGameList"
                :key="game.productId"
            >
              <div class="game-ctn" tabindex="0" role="button">
                <router-link
                    :to="{ name: 'detail', params: { product_id: game.productId } }"
                    class="game-logo-link"
                >
                  <img class="game-logo" :src="game.productLogo" alt="logo" />
                </router-link>
                <span class="game-name">
                  <router-link
                      :to="{ name: 'detail', params: { product_id: game.productId } }"
                      class="game-name-link"
                  >
                    {{ game.productName }}
                  </router-link>
                  <span class="game-count" style="color:#ffffff">×{{ game.count }}</span>
                </span>
              </div>
            </div>
          </div>
        </div>
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

.app_background {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  z-index: -2;
  height: 100%;
  background: #1B2838;
}

div {
  display: block;
  unicode-bidi: isolate;
}

.pagecontent {
  padding-bottom: 128px;
}

.profile_small_header_bg {
  max-width: 1010px;
  height: 215px;
  margin: 0 auto -98px auto;
  background-position: center;
}

.profile_small_header_texture {
  background: rgba(58, 62, 71, 0.5);
  max-width: 978px;
  height: 102px;
  margin: 0 auto;
  position: relative;
}

.profile_small_header_avatar {
  width: 64px;
  height: 64px;
  position: absolute;
  left: 30px;
  top: 18px;
  color: white;
}

.profile_small_header_text {
  position: absolute;
  left: 120px;
  right: 30px;
  bottom: 31px;
  max-height: 65px;
  color: white;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.profile_small_header_name {
  font-size: 26px;
  margin-right: 6px;
}

.profile_small_header_arrow {
  font-size: 16px;
  color: #828282;
  margin-right: 4px;
}

.profile_small_header_additional {
  position: absolute;
  right: 30px;
  top: 0;
  bottom: 0;
}

.gamelist {
  --typography-font-size-small: 0.625rem;
  --typography-font-size-medium: 0.75rem;
  --typography-font-size-body: 0.9375rem;
  --typography-font-size-large: 1.0625rem;
  --typography-color-neutral: #B8BCBF;
  --typography-color-primary: white;
  --color-info: #1A9FFF;
  --color-backdrop: #3C3F49;
  --color-highlight: #199FFF;
  max-width: 936px;
  margin: auto;
}

.sectionTabs {
  position: relative;
  padding-top: 3px;
  margin-left: -5px;
  font-size: 14px;
  z-index: 10;
  display: flex;
}

.sectionTab {
  width: 165px;
  height: 29px;
  display: flex;
  align-items: center;
  justify-content: center;
  float: left;
  color: #FFE3ED;
  margin-left: 5px;
  white-space: nowrap;
  border-radius: 2px;
  border-bottom-left-radius: 0px;
  border-bottom-right-radius: 0px;
}

.sectionTab.active {
  height: 34px;
  padding: 0;
  text-decoration: none;
  border: none;
  background: linear-gradient(to bottom, #66C0F4 5%, #417B9C 95%);
}

.baseline {
  margin-bottom: 14px;
}

.responsive_tab_baseline {
  background-color: #417B9C;
  max-width: 958px;
  height: 5px;
}

.gamelist-nav {
  display: grid;
  grid-template-areas: "search bothOwned sort";
  grid-template-rows: 1fr;
  grid-template-columns: auto 1fr auto;
  padding-bottom: 10px;
}

.search {
  position: relative;
  display: flex;
  height: 33px;
  grid-area: search;
}

.search input {
  background: rgba(24,26,33,.5);
  color: var(--typography-color-primary);
  border: none;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  padding: 7px 8px 7px 14px;
}

.search-icon {
  position: absolute;
  width: 18px;
  height: 100%;
  display: flex;
  align-items: center;
  right: 4px;
  pointer-events: none;
}

.games {
  margin: auto;
  position: relative;
}

.game {
  margin-bottom: 10px;
  position: relative;
  width: 100%;
}

.game-ctn {
  background-color: #16202D;
  position: relative;
  --padding: 10px;
  display: grid;
  box-sizing: border-box;
  height: 140px;
  grid-template-areas: "image title title" "image playtime playtime" "image links download";
  grid-template-columns: 259px min-content auto;
  grid-template-rows: auto auto 1fr;
  gap: var(--padding);
  padding: var(--padding);
  row-gap: 4px;
}

.game-logo-link {
  grid-area: image;
  width: 259px;
  height: 121px;
}

.game-logo {
  position: relative;
  width: 100%;
  height: 100%;
}

.game-name {
  grid-area: title;
  display: inline-flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  height: calc(2.1*var(--typography-font-size-large));
  margin-right: 32px;
}

.game-name-link {
  font-weight: 400;
  font-size: var(--typography-font-size-large);
  color: var(--typography-color-primary);
  line-height: var(--typography-font-size-large);
  display: -webkit-inline-box;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  padding-bottom: .14em;
  text-decoration: none;
}
</style>