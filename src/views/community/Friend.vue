<script setup lang="ts">
import { onMounted, ref} from "vue";
import { userInfo } from "../../api/user.ts";
import {
  AcceptFriendRequest,
  allFriend,
  allRequest,
  RejectFriendRequest,
  searchFriend,
  SendFriendRequest
} from "../../api/friends.ts";
import {useRouter} from "vue-router";

const router = useRouter()
const userId = ref();
const userName = ref('');
const userAvatar = ref('');
const friends = ref([]);
const currentView = ref<'friends' | 'add_friends' | 'pending_requests'>('friends');
// 添加好友相关状态
const searchQuery = ref('');
const searchResult = ref('');
const showSearchResults = ref(false);

//好友请求列表
const requestList = ref([]);
// 获取用户信息和好友列表
async function getUserInfo() {
  const res = await userInfo();
  if (res.data.code === '000') {
    const result = res.data.result;
    userId.value = result.id;
    userName.value = result.name;
    userAvatar.value = result.avatar;
    allRequest(Number(userId.value)).then((res) => {
      requestList.value = res.data.result;
    })
  } else if (res.data.code === '400') {
    console.log('未登录');
  }

  // 获取好友列表
  allFriend(Number(userId.value)).then(res => {
    friends.value = res.data.result;
  });
}


onMounted(async () => {
  await getUserInfo();
});

function handleSearch() {
  searchFriend(searchQuery.value).then((res) => {
    console.log(res);
    searchResult.value = res.data.result;
    showSearchResults.value = true;
  })
}

function jumpToChat(friendId: number) {
  console.log(friendId);
  router.push({path:"chat", query:{friendId}})
}

function sendFriendRequest(friendId: Number) {
  console.log(friendId);
  console.log(userId.value);
  SendFriendRequest(Number(userId.value),Number(friendId)).then((res) => {
    console.log(res);
    if (res.result) {
      ElMessage({
        message: "成功发送好友请求！",
        type: 'success',
        center: true,
      })
    }
    else{
      ElMessage({
        message: "已发送好友请求或已是好友！",
        type: 'failure',
        center: true,
      })
    }
  })
}

function acceptRequest(friendId: Number) {
  AcceptFriendRequest(Number(userId.value),Number(friendId)).then((res) => {
    console.log(res);
    if (res.result) {
      ElMessage({
        message: "添加成功！",
        type: 'success',
        center: true,
      })
    }
    allRequest(Number(userId.value)).then((res) => {
      requestList.value = res.data.result;
      allFriend(Number(userId.value)).then(res => {
        friends.value = res.data.result;
      });
    })
  })
}

function rejectRequest(friendId: Number) {
  RejectFriendRequest(Number(userId.value),Number(friendId)).then((res) => {
    console.log(res);
    if (res.result) {
      ElMessage({
        message: "已拒绝！",
        type: 'success',
        center: true,
      })
    }
    allRequest(Number(userId.value)).then((res) => {
      requestList.value = res.data.result;
      allFriend(Number(userId.value)).then(res => {
        friends.value = res.data.result;
      });
    })
  })
}
</script>

<template>
  <div class="pagecontent">
    <!-- 头部背景 -->
    <div class="friends_header_bg">
      <div class="friends_header_container">
        <div class="friends_header_ctn">
          <div class="friends_header_avatar">
            <a href="">
              <img
                  :src="userAvatar || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'"
                  class="avatar-image"
              >
            </a>
          </div>
          <div class="friends_header_name_ctn">
            <div class="friends_header_name" style="display:inline-flex">
              <div class="name-text">{{userName}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 好友主容器 -->
    <div class="friends_container">
      <!-- 左侧导航栏 -->
      <div class="friends_nav">
        <h4>好友</h4>
        <div
            class="icon_item icon_all_friends"
            v-if="currentView !== 'friends'"
            @click="currentView = 'friends'"
        >
          <span class="title">您的好友</span>
          <svg class="down_arrow_context_menu">
            <polygon points="50 59.49 13.21 22.89 4.74 31.39 50 76.41 95.26 31.39 86.79 22.89 50 59.49"></polygon>
          </svg>
        </div>
        <div
            class="icon_item_clicked icon_all_friends"
            v-if="currentView === 'friends'"
        >
          <span class="title">您的好友</span>
          <svg class="down_arrow_context_menu">
            <polygon points="50 59.49 13.21 22.89 4.74 31.39 50 76.41 95.26 31.39 86.79 22.89 50 59.49"></polygon>
          </svg>
        </div>
        <div
            class="icon_item icon_add_friends"
            v-if="currentView !== 'add_friends'"
            @click="currentView = 'add_friends'"
        >
          <span class="title">添加好友</span>
          <svg class="down_arrow_context_menu">
            <polygon points="50 59.49 13.21 22.89 4.74 31.39 50 76.41 95.26 31.39 86.79 22.89 50 59.49"></polygon>
          </svg>
        </div>
        <div
            class="icon_item_clicked icon_add_friends"
            v-if="currentView === 'add_friends'"
        >
          <span class="title">添加好友</span>
          <svg class="down_arrow_context_menu">
            <polygon points="50 59.49 13.21 22.89 4.74 31.39 50 76.41 95.26 31.39 86.79 22.89 50 59.49"></polygon>
          </svg>
        </div>
        <div
            class="icon_item icon_pending_requests"
            v-if="currentView !== 'pending_requests'"
            @click="currentView = 'pending_requests'"
        >
          <span class="title">未处理请求</span>
          <svg class="down_arrow_context_menu">
            <polygon points="50 59.49 13.21 22.89 4.74 31.39 50 76.41 95.26 31.39 86.79 22.89 50 59.49"></polygon>
          </svg>
        </div>
        <div
            class="icon_item_clicked icon_pending_requests"
            v-if="currentView === 'pending_requests'"
        >
          <span class="title">未处理请求</span>
          <svg class="down_arrow_context_menu">
            <polygon points="50 59.49 13.21 22.89 4.74 31.39 50 76.41 95.26 31.39 86.79 22.89 50 59.49"></polygon>
          </svg>
        </div>
      </div>

      <!-- 右侧内容区 -->
      <div class="friends_content" id="subpage_container">
        <!-- 好友列表视图 -->
        <div v-if="currentView === 'friends'">
          <div id="friends_list" class="friends_list_ctn pagecontent no_header">
            <div class="profile_friends title_bar">
              <div class="profile_friends title">
                您的好友
                <span class="friends_count">{{ friends.length }}</span>
                /
                <span class="friends_limit"></span>
              </div>
              <button
                  id="manage_friends_control"
                  class="profile_friends manage_link btnv6_blue_hoverfade btn_medium"
              >
                <span>管理好友列表</span>
              </button>
              <button
                  id="add_friends_button"
                  class="profile_friends manage_link btn_green_steamui btn_medium"
                  @click="currentView = 'add_friends'"
              >
                <span>
                  <i class="add_friend_icon"></i>
                  添加好友
                </span>
              </button>
            </div>

            <div class="searchBarContainer">
              <input
                  type="text"
                  name="search_text_box"
                  id="search_text_box"
                  class="friends_search_text_box gamepad_input"
                  placeholder="通过名称或游戏搜索好友"
              />
            </div>

            <div class="profile_friends search_results">
              <div
                  v-for="friend in friends"
                  :key="friend.id"
                  class="friend_item"
                  @click="jumpToChat(friend.id)"
              >
                <div class="friend_avatar">
                  <img
                      :src="friend.avatar || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'"
                      alt="friend avatar"
                      class="avatar-small"
                  >
                </div>

                <div class="friend_info">
                  <div class="friend_name">
                    <a>{{ friend.name }}</a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 添加好友视图 -->
        <div v-else-if="currentView==='add_friends'" class="add-friends-view">
          <div class="add-friends-header">
            <h3 class="add-friends-title">添加好友</h3>
            <button
                class="back-btn btnv6_blue_hoverfade btn_small"
                @click="currentView = 'friends'"
            >
              <span>返回</span>
            </button>
          </div>

          <div class="add-friends-form">
            <div class="form-group">
              <input
                  type="text"
                  v-model="searchQuery"
                  placeholder="通过电话搜索好友"
                  class="add-friends-input"
              >
            </div>
            <button class="btn_green_steamui btn_medium" @click="handleSearch">
              <span>
                <i class="add_friend_icon"></i>
                搜索好友
              </span>
            </button>
          </div>

          <!-- 搜索结果 -->
          <div v-if="showSearchResults" class="search-results">
            <div class="add-friend-item">
              <div class="friend_avatar">
                <img :src="searchResult.avatar || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'" alt="avatar" class="avatar-small">
              </div>
              <div class="friend_info">
                <div class="friend_name">
                  <a href="">{{ searchResult.name }}</a>
                </div>
              </div>
              <button class="add-btn" @click="sendFriendRequest(searchResult.id)">
                添加好友
              </button>
            </div>
          </div>
        </div>

        <div v-else-if="currentView === 'pending_requests'" class="pending-requests-view">
          <div class="pending-requests-header">
            <h3 class="pending-requests-title">未处理的好友请求</h3>
            <button
                class="back-btn btnv6_blue_hoverfade btn_small"
                @click="currentView = 'friends'"
            >
              <span>返回</span>
            </button>
          </div>

          <div class="requests-list">
            <div
                v-for="request in requestList"
                :key="request.id"
                class="friend_item request-item"
            >
              <div class="friend_avatar">
                <img
                    :src="request.avatar || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'"
                    alt="avatar"
                    class="avatar-small"
                >
              </div>

              <div class="friend_info">
                <div class="friend_name">
                  <a href="">{{ request.name }}</a>
                </div>
                <div class="request-message">{{ request.message || '请求添加你为好友' }}</div>
              </div>

              <div class="request-actions">
                <button class="btn-accept" @click="acceptRequest(request.id)">接受</button>
                <button class="btn-deny" @click="rejectRequest(request.id)">拒绝</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 全局布局 */
.pagecontent {
  margin: 0;
  width: 100%;
  height: 100%;
  min-width: 1024px;
  background: url("https://community.steamstatic.com/public/images/friends/colored_body_top2.png?v=2") center top repeat-y #1a2c45;
  background-size: 100% auto;
}

.friends_header_bg {
  height: 127px;
  margin: 0 auto;
  background: url(https://community.steamstatic.com/public/images/bg_highlight.png) center bottom no-repeat;
}

.friends_header_container {
  max-width: 1240px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
}

.friends_header_ctn {
  margin-left: 32px;
  display: flex;
  align-items: center;
}

.friends_header_avatar {
  position: relative;
  margin-right: 20px;
}

.friends_header_avatar::before {
  content: "";
  position: absolute;
  width: 74px;
  height: 74px;
  background: url(https://community.steamstatic.com/public/images/avatars/avatar_frame_6.png);
  background-size: contain;
  z-index: 1;
}

.avatar-image {
  width: 64px;
  height: 64px;
  margin: 5px;
  z-index: 2;
  border-radius: 3px;
  box-shadow: 0 0 4px rgba(0,0,0,0.3);
}

.friends_header_name {
  font-size: 26px;
  color: white;
  text-shadow: 1px 1px 0 #000;
  margin-bottom: 4px;
}

.friends_header_status {
  font-size: 14px;
  color: #4b69ff;
}

/* 导航栏 */
.friends_container {
  font-family: "Motiva Sans", Sans-serif;
  font-weight: 300;
  display: flex;
  justify-content: center;
  margin: 0 auto;
  padding-bottom: 128px;
  max-width: 1220px;
}

.friends_nav {
  min-width: 250px;
  margin: 0px 20px 0px 10px;
  display: block;
}

.friends_nav h4 {
  color: #7092a5;
  margin: 0;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 600;
  padding: 12px 0 0 8px;
  font-size: 11px;
}

.icon_item {
  display: block;
  padding: 10px 10px;
  color: #ebebeb;
  text-decoration: none;
  cursor: pointer;
  font-size: 14px;
}

.icon_item_clicked {
  display: block;
  padding: 10px 10px;
  color: #ebebeb;
  text-decoration: none;
  cursor: pointer;
  font-size: 14px;
  background-color: #2F536B;
}

.icon_item:hover,
.icon_item.active {
  background-color: #45697F;
}

.friends_nav .icon_add_friends .title {
  background-position: 0 -176px;
}

.icon_item .title {
  display: inline-block;
  vertical-align: middle;
}

.icon_item_clicked .title {
  display: inline-block;
  vertical-align: middle;
}

.count, .em_count {
  margin-left: 8px;
  padding: 2px 6px;
  background-color: #4b69ff;
  border-radius: 12px;
  font-size: 12px;
}

.down_arrow_context_menu {
  width: 16px;
  height: 16px;
  fill: #8f98a0;
  margin-left: 8px;
  transition: transform 0.2s;
  vertical-align: middle;
}

.icon_item:hover .down_arrow_context_menu {
  transform: rotate(180deg);
}

.hr {
  height: 1px;
  background: #2a4365;
  margin: 12px 0;
}

/* 内容区 */
.friends_container blockquote {
  margin: 5px 20px;
}

.friends_nav {
  min-width: 250px;
  margin: 0px 20px 0px 10px;
  display: block;
}

.friends_nav a {
  display: block;
  padding: 10px 10px;
}

.friends_nav a:hover {
  background-color: rgba(255,255,255,0.2);
}

.friends_nav h4 {
  color: #7092a5;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 600;
  padding: 12px 0 0 8px;
  font-size: 11px;
}

.friends_nav .hr {
  height: 1px;
  background-color: transparent;
  border-bottom: 1px solid #000;
  margin-top: 20px;
  margin-bottom: 12px;
}

.friends_nav div.icon_item .title {
  background-repeat: no-repeat;
  background-size: 16px 192px;
  display: inline-block;
  line-height: 16px;
  padding-left: 35px;
  background-image: url( 'https://community.steamstatic.com/public/images/iconsheet_friends.png?v=5' );
}

.friends_nav div.icon_item_clicked .title {
  background-repeat: no-repeat;
  background-size: 16px 192px;
  display: inline-block;
  line-height: 16px;
  padding-left: 35px;
  background-image: url( 'https://community.steamstatic.com/public/images/iconsheet_friends.png?v=5' );
}

.invite_row .actions .ico_approve {
  background-position: 0 0;
}

/* 好友内容容器 */
.friends_content {
  transition: opacity 500ms;
  opacity: 1.0;
  margin-top: 24px;
  width: 100%;
}

.friends_content.loading {
  opacity: 0.3;
}

.pagecontent.no_header {
  background-position: center top;
}

.friends_list_ctn.pagecontent {
  background: transparent;
  margin: 0 auto;
  max-width: 1220px;
  padding-bottom: 128px;
}

.friends_list_ctn {
  width: 100%;
  max-width: 1220px;
  margin: 0 auto;
}

/* 标题栏 */
.profile_friends.title_bar {
  width: 100%;
  display: flex;
  align-items: center;
  padding: 5px 10px;
  background-color: #015e80;
}

.profile_friends.title {
  text-transform: uppercase;
  letter-spacing: 1px;
  font-size: 14px;
  color: #fff;
  font-weight: normal;
  min-width: 250px;
}

.friends_count {
  font-weight: bold;
}

.friends_limit {
  font-size: 9px;
}

/* 管理面板 */
.manage_friends_panel {
  display: none;
  flex: 1 100%;
  flex-flow: row wrap;
  width: 100%;
  min-height: 90px;
  border-color: #825622;
  box-shadow: 0 0 0 1px #015e80 inset;
  background: linear-gradient(to bottom, rgba(22, 33, 46, 1.0) 5%, rgba(22, 33, 46, 0.5) 95%);
  transition: min-height 0.3s, opacity 0.3s ease-in;
}

.manage_friends_panel.manage {
  display: flex;
}

/* 搜索框 */
.friends_search_text_box {
  margin: 5px;
  background: #274e68 url(https://community.steamstatic.com/public/images/sharedfiles/searchbox_workshop_submit.gif);
  border-radius: 3px;
  border: 1px solid rgba(0, 0, 0, 0.3);
  box-shadow: 1px 1px 0px rgba(255, 255, 255, 0.2);
  color: #fff;
  outline: none;
  height: 28px;
  font-size: 14px;
  min-width: 250px;
  padding: 0 0 0 30px;
}

.row {
  margin: 8px 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 选择类型文本 */
.selection_type {
  color: white;
  cursor: pointer;
  text-transform: uppercase;
  margin: 4px 5px;
}

/* 管理操作按钮 */
.manage_action {
  margin: 5px;
  padding: 4px 12px;
  background-color: #4b69ff;
  color: white;
  border: none;
  border-radius: 3px;
  font-size: 12px;
}

/* 已选消息 */
.selected_msg {
  color: #fff;
  font-size: 14px;
}

/* 标题栏样式 */
.profile_friends.title_bar {
  width: 100%;
  display: flex;
  align-items: center;
  padding: 5px 10px;
  background-color: #015e80;
}

.profile_friends.title {
  text-transform: uppercase;
  letter-spacing: 1px;
  font-size: 14px;
  color: #fff;
  min-width: 250px;
  font-family: "Motiva Sans", Sans-serif;
  font-weight: 300;
}

#manage_friends_control {
  margin-left: 500px;
  margin-right: 10px;
}

.profile_friends.manage_link {
  text-align: right;
  margin-left: 10px;
  margin-right: 10px;
  flex-shrink: 0;
}

.btnv6_blue_hoverfade {
  border-radius: 2px;
  border: none;
  padding: 1px;
  display: inline-block;
  cursor: pointer;
  text-decoration: none;
  color: #67c1f5;
  background: rgba( 103, 193, 245, 0.2 );
}

.btnv6_blue_hoverfade:hover {
  color: #ffffff;
  background: linear-gradient(to right,#66bff3,#437d9e);
}

.btn_medium > span {
  padding: 0 15px;
  font-size: 15px;
  line-height: 30px;
}

.btnv6_blue_hoverfade > span {
  border-radius: 2px;
  display: block;
  background: transparent;
}

html.responsive #add_friends_button {
  text-align: center;
  margin: 0;
}

.profile_friends.manage_link {
  flex-shrink: 0;
}

.btn_green_steamui {
  border-radius: 2px;
  border: none;
  padding: 1px;
  display: inline-block;
  cursor: pointer;
  text-decoration: none;
  color: #d2efa9;
  background: transparent;
  text-shadow: 1px 1px 0px rgba( 0, 0, 0, 0.3 );
}

.btn_green_steamui:hover {
  color: #ffffff;
}

.btn_medium > span {
  padding: 0 15px;
  font-size: 15px;
  line-height: 30px;
}

.btn_green_steamui > span {
  border-radius: 2px;
  display: block;
  background: linear-gradient( to right, #75b022 5%, #588a1b 95%);
}

.btn_green_steamui > span:hover {
  background: linear-gradient(to right,#8ED629,#6AA621);
}

.add_friend_icon {
  background-repeat: no-repeat;
  background-size: 16px 192px;
  padding-left: 24px;
  background-image: url('https://community.cloudflare.steamstatic.com/public/images/iconsheet_friends.png?v=5');
  background-position: 0 -176px;
}

/* 管理面板样式 */
.manage_friends_panel {
  display: none;
  flex: 1 100%;
  flex-flow: row wrap;
  width: 100%;
  min-height: 90px;
  box-shadow: 0 0 0 1px #015e80 inset;
  background: linear-gradient(to bottom, rgba(22, 33, 46, 1.0) 5%, rgba(22, 33, 46, 0.5) 95%);
  transition: min-height 0.3s, opacity 0.3s ease-in;
  padding: 16px;
  color: #fff;
  font-family: "Motiva Sans", Sans-serif;
  font-weight: 300;
  margin-top: 10px;
}

.manage_friends_panel.manage {
  display: flex;
}

.manage_friends_panel .row {
  flex: 1 100%;
  margin-left: 16px;
  margin-top: 16px;
  font-size: 12px;
}

.selection_type {
  color: white;
  cursor: pointer;
  margin: 0 5px;
}

.dimmed {
  color: #bbb;
}

.manage_action {
  margin: 5px;
  padding: 5px 12px;
  border-radius: 3px;
  cursor: pointer;
}

/* 搜索栏样式 */
.searchBarContainer {
  display: flex;
  flex: 1 100%;
  margin: 10px 0 0 0;
  width: 50%;
}

.friends_search_text_box {
  margin: 0;
  float: left;
  background: #14364C url(https://community.steamstatic.com/public/images/sharedfiles/searchbox_workshop_submit.gif) no-repeat 1px;
  border: 1px solid #000;
  box-shadow: 1px 1px 0px rgba(255, 255, 255, 0.2);
  outline: none;
  height: 28px;
  font-size: 14px;
  min-width: 250px;
  padding: 0 0 0 30px;
  font-family: "Motiva Sans", Sans-serif;
  width: 100%;
}

.friends_search_text_box::-webkit-input-placeholder {
  color: #7092a5;
  font-style: italic;
}

/* 响应式样式 */
@media screen and (max-width: 910px) {
  .profile_friends.title_bar {
    flex-wrap: wrap;
    padding: 10px 0;
  }

  .action_btns {
    margin-top: 10px;
    width: 100%;
    justify-content: center;
  }

  .searchBarContainer input {
    width: 100%;
    max-width: none;
  }
}

.search_results {
  margin-top: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  width: 100%;
}

.friend_item {
  height: 50px;
  flex-basis: calc(30% - 16px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0px;
  border-radius: 0px;
  margin-bottom: 8px;
  background-color: rgba(28,33,40,0.9);
  transition: background-color 0.2s;
}

.friend_item:hover {
  background-color: rgba(30, 50, 70, 1);
}

.friend_avatar {
  width: 70px;
  height: 70px;
  margin-right: 16px;
  margin-left: 0px;
}

.avatar-small {
  width: 70px;
  height: 70px;
  border-radius: 3px;
  box-shadow: 0 0 4px rgba(0,0,0,0.3);
  border-right: 4px solid #67c1f5;
}

.friend_info {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.friend_name a {
  color: #67c1f5;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
}

.friend_name a:hover {
  text-decoration: underline;
}

/* 添加好友视图样式 */
.add-friends-view {
  padding: 20px;
  color: #fff;
  background: rgba(22, 33, 46, 0.8);
  border-radius: 4px;
}

.add-friends-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.add-friends-title {
  font-size: 18px;
  color: #67c1f5;
}

.back-btn {
  padding: 4px 12px;
}

.add-friends-form {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.add-friends-input {
  flex-grow: 1;
  padding: 8px 12px;
  background: #1a2c45;
  border: 1px solid #000;
  border-radius: 3px;
  color: #fff;
}

.search-results {
  margin-top: 20px;
}

.add-friend-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  margin-bottom: 8px;
  background-color: rgba(28, 33, 40, 0.9);
  border-radius: 4px;
  transition: background-color 0.2s;
}

.add-friend-item:hover {
  background-color: rgba(30, 50, 70, 1);
}

.add-btn {
  padding: 6px 12px;
  background-color: #4b69ff;
  color: #fff;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

.add-btn:hover {
  background-color: #6a87ff;
}

.pending-requests-view {
  padding: 20px;
  color: #fff;
  background: rgba(22, 33, 46, 0.8);
  border-radius: 4px;
}

.pending-requests-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.pending-requests-title {
  font-size: 18px;
  color: #67c1f5;
}

.request-item {
  position: relative;
  padding-right: 160px; /* 为按钮留出空间 */
}

.request-message {
  color: #8f98a0;
  font-size: 12px;
  margin-top: 4px;
}

.request-actions {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  gap: 10px;
}

.btn-accept {
  padding: 6px 12px;
  background-color: #4b9e4b;
  color: #fff;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

.btn-accept:hover {
  background-color: #5cb85c;
}

.btn-deny {
  padding: 6px 12px;
  background-color: #a74b4b;
  color: #fff;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

.btn-deny:hover {
  background-color: #d9534f;
}

.requests-list {
  margin-top: 20px;
}
</style>