<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import {userInfo} from '../../api/user.ts'

const sortList = ref(false)
const wishlist= ref([])
const nickname = ref('')
const userId=ref()
const sortMethod=ref(1)

getUserInfo()
function getUserInfo() {
  userInfo().then(res => {
    nickname.value = res.data.result.name
    userId.value = res.data.result.id
  })
}

/** 打开排序菜单 */
function openSortMenu() {
  if (!sortList.value)
    sortList.value = true
}

/** 关闭排序菜单 */
function closeSortMenu() {
  if (sortList.value)
    sortList.value = false
}

function changeSortType(type:number) {
  closeSortMenu()
  sortMethod.value = type
  localStorage.setItem('steamWishlistSort', type)
}
</script>

<template>
  <div class="app" @mousemove="handleDrag" @mouseup="handleDragEnd">
    <div class="header">
      <div class="header-avatar">
        <img :src="avatar" alt="">
      </div>
      {{ nickname }} 的愿望单
    </div>

    <div class="filter">
      <input v-model="keyword" class="search" placeholder="按名称搜索">
      <div class="sort" :class="{ focus: sortList }" tabindex="0"
           @click="openSortMenu()" @mouseleave="closeSortMenu()">
        排序依据：
        <div class="sort-type">{{ ['名称', '价格', '折扣', '添加日期', '发行日期'].at(sortMethod-1) }}</div>
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
    </div>
  </div>


</template>

<style scoped>
.app {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: calc(100vh - 104px);
  background-color: #202326;
}

.header {
  display: flex;
  align-items: center;
  gap: 16px;
  width: 940px;
  padding: 25px 0;
  color: #ffffff;
  font-size: 26px;
}

.filter {
  display: flex;
  align-items: center;
  gap: 20px;
  width: 940px;
  padding: 12px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.sort {
  position: relative;
  z-index: 100;
  display: flex;
  align-items: center;
  height: 38px;
  padding: 0 12px;
  margin-right: 12px;
  color: #9099a1;
  font-size: 13px;
  letter-spacing: 1px;
  cursor: pointer;
  transition: color 0.2s, background-color 0.2s;

  &:hover {
    color: #ffffff;
    background-color: rgba(255, 255, 255, 0.1);
  }

  &.focus {
    color: #ffffff;
    background-color: #808a9c;
  }
}

.sort-type {
  color: #ffffff;
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

  .sort.focus > & {
    opacity: 1;
    pointer-events: auto;
  }
}

.sort-menu-item {
  display: flex;
  align-items: center;
  height: 32px;
  padding: 0 12px;
  color: #ffffff;
  font-size: 12px;
  transition: background-color 0.1s;

  &:hover {
    background-color: rgba(255, 255, 255, 0.2);
  }
}

.list {
  position: relative;
  width: 940px;
  padding: 12px 0 80px 0;
}

.empty {
  padding: 80px 0;
  color: #e5e5e5;
  font-size: 14px;
  text-align: center;

  :last-child {
    font-size: 12px;
  }
}
</style>