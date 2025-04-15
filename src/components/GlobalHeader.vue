<script setup>
import { ref,onMounted,onBeforeUnmount } from 'vue'
import { router } from "../router/index.ts";

const storeMenuLocked = ref(false)
const mineMenuLocked = ref(false)
const actionMenuLocked = ref(false)

const userId = ref('')
const userName = ref('')
const userPhone = ref('')
const userPassword = ref('')
const userAddress = ref('')
const userRole = ref('')
const userCreateTime = ref('')
const userBalance = ref('')
const userAvatar = ref('')

const current = ref('')
const token = ref(false)

async function getUserInfo() {
  const { userInfo } = await import('../api/user.ts')
  const res = await userInfo()
  if (res.data.code === '000') {
    token.value = true
  } else if (res.data.code === '400') {
    return
  }
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
}

onMounted(() => {
  getUserInfo()
  window.addEventListener('user-logged-in', getUserInfo)
})

onBeforeUnmount(() => {
  window.removeEventListener('user-logged-in', getUserInfo)
})

function logout() {
  //待实现
  sessionStorage.removeItem('token')
  router.push({'path': '/login'})
}
</script>

<template>
  <div class="global-header">
    <div class="content">
      <div class="logo">
        <img src="../assets/logo_steam.svg" alt="steam" />
      </div>
      <div class="supernav_container">
        <RouterLink class="menuitem supernav store" :class="{ current: current === 0 }" to="/" @click="storeMenuLocked = true" @mouseenter="storeMenuLocked = false">
          商店
          <div class="submenu_Store">
            <RouterLink class="submenuitem" to="/" @click="storeMenuLocked = true">主页</RouterLink>
            <RouterLink class="submenuitem" to="/wishlist" @click="storeMenuLocked = true">愿望单</RouterLink>
          </div>
        </RouterLink>
        <RouterLink class="menuitem supernav" :class="{ current: current === 1 }" to="/community">社区</RouterLink>
        <RouterLink v-if="token" class="menuitem supernav nickname profile" :class="{ current: current === 2 }" :to="`/profile/${userId}`" @click="mineMenuLocked = true" @mouseenter="mineMenuLocked = false">
          {{ userName }}
          <div class="submenu_Profile">
            <RouterLink class="submenuitem" :to="`/profile/${userId}`" @click="mineMenuLocked = true">个人资料</RouterLink>
            <RouterLink class="submenuitem" to="/friends" @click="mineMenuLocked = true">好友</RouterLink>
          </div>
        </RouterLink>
<!--        <RouterLink v-else class="nav-item" :class="{ current: current === 3 }" to="/about">关于</RouterLink>-->
        <RouterLink v-if="token" class="menuitem" :class="{ current: current === 4 }" to="/chat">聊天</RouterLink>
        <RouterLink class="menuitem" :class="{ current: current === 5 }" to="">客服</RouterLink>
      </div>
      <div class="actions">
        <div class="action-menu">
          <RouterLink class="install" :class="{ light: token === null }" to="/about">
            <img src="../assets/btn_header_installsteam_download.png" alt="安装Steam">
            安装 SBEAM
          </RouterLink>
          <div v-if="token" class="account-pulldown" @mouseenter="actionMenuLocked = false">
            {{ userName }}
            <img src="../assets/btn_arrow_down_padded.png" alt="">
            <div v-show="!actionMenuLocked" class="account-pulldown-menu">
              <RouterLink class="account-pulldown-menu-item" :to="`/profile/${userId}`" @click="actionMenuLocked = true">查看个人资料</RouterLink>
              <div class="account-pulldown-menu-item" @click="logout(); actionMenuLocked = true">注销：<span>{{ userName }}</span></div>
            </div>
          </div>
        </div>

        <RouterLink v-if="token" class="user-avatar" :to="`/profile/${userId}`">
          <img :src="userAvatar || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'" alt="">
        </RouterLink>
        <RouterLink v-else class="login" to="/login">登录</RouterLink>
      </div>
    </div>
  </div>
</template>

<style scoped>
.global-header {
  position: relative;
  z-index: 100;
  display: flex;
  justify-content: center;
  width: 100%;
  min-width: 940px;
  background-color: #1f2428;
  font-family: "Motiva Sans", sans-serif;;
}

.content {
  position: relative;
  display: flex;
  align-items: center;
  width: 940px;
  height: 104px;
}

.logo {
  width: 176px;
  height: 44px;
  margin-right: 24px;
  img {
    width: 100%;
    height: 100%;
  }
}

.supernav_container {
  position: absolute;
  left: 200px;
}

.menuitem {
  display: block;
  position: relative;
  padding-top: 7px;
  padding-left: 7px;
  padding-right: 7px;
  padding-bottom: 7px;
  line-height: 16px;
  float: left;
  font-size: 14px;
  color: #dcdedf;
  text-transform: uppercase;
  font-size: 16px;
  font-family: "Motiva Sans", "Twemoji", "Noto Sans", Helvetica, sans-serif;
  font-weight: 500;
  text-decoration: none;
}

.store {
  &:hover>.submenu_Store {
    opacity: 1;
    pointer-events: auto;
  }
}

.menuitem:hover {
  text-decoration: underline;
  color: #1a9fff
}

.nickname {
  max-width: 250px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  font-weight: 600;
}

.profile {
  &:hover>.submenu_Profile {
    opacity: 1;
    pointer-events: auto;
  }
}

.submenu_Store {
  width: 78px;
  position: absolute;
  z-index: 1500;
  left: 1px;
  top: 31px;
  opacity: 0;
  pointer-events: none;
  background: #3D4450;
  box-shadow: 3px 3px 5px -3px #000;
  text-align: left;
}

.submenu_Profile {
  width: 92.7px;
  position: absolute;
  z-index: 1500;
  left: 1px;
  top: 31px;
  opacity: 0;
  pointer-events: none;
  background: #3D4450;
  box-shadow: 3px 3px 5px -3px #000;
  text-align: left;
}

.submenuitem {
  text-decoration: none;
  text-transform: none;
  font-size: 12px;
  color: #dcdedf;
  padding-right: 15px;
  padding-left: 15px;
  display: inline-block;
  padding: 6px 15px;
  text-align: center;
  &:hover {
    color: #171a21;
    background-color: #dcdedf;
  }
}

.actions {
  position: absolute;
  right: 0;
  top: 6px;
  display: flex;
}

.action-menu {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin-right: 8px;
}

.install {
  display: flex;
  align-items: center;
  padding: 0 9px;
  color: #e5e4dc;
  background-color: rgba(103, 112, 123, 0.2);
  font-family: "Motiva Sans", sans-serif;
  font-size: 11px;
  line-height: 24px;
  text-decoration: none;
  &:hover {
    color: #ffffff;
  }
  &.light {
    background-color: #5c7e10;
  }
  img {
    flex-shrink: 0;
    margin-right: 9px;
  }
}

.message {
  background-color: #5c7e10;

  &:hover {
    background-color: #7ea64b;
  }
}
.account-pulldown {
  position: relative;
  display: flex;
  align-items: center;
  height: 24px;
  color: #b8b6b4;
  font-size: 11px;
  cursor: pointer;
  &:hover {
    color: #ffffff;
    &>.account-pulldown-menu {
      opacity: 1;
      pointer-events: auto;
    }
  }
}
.account-pulldown-menu {
  position: absolute;
  right: 0;
  top: 100%;
  border: 1px solid #3D4450;
  box-shadow: 0 0 12px #000000;
  background-color: #3d4450;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s;
}
.account-pulldown-menu-item {
  display: inline-block;
  width: 148px;
  padding: 5px 12px;
  color: #b8b6b4;
  font-size: 12px;
  font-family: Motiva Sans, Arial, Helvectica, Verdana, sans-serif;
  text-decoration: none;
  &:hover {
    color: #171d25;
    background-color: #dcdedf;
  }
  span {
    color: #57cbde;
  }
}
.user-avatar {
  display: inline-block;
  width: 32px;
  height: 32px;
  border: 2px solid #474747;
  img {
    width: 32px;
    height: 32px;
    background: linear-gradient( to bottom, #41778f 5%, #3d697b 95%);
  }
}
.login {
  color: #b8b6b4;
  font-size: 11px;
  line-height: 24px;
  text-decoration: none;
  &:hover {
    color: #ffffff;
  }
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
.fade-enter-active,
.fade-leave-active {
  transition: 0.2s;
}
</style>
