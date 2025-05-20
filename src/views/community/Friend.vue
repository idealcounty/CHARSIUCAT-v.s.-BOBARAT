<script setup lang="ts">
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
                  src="https://avatars.cdn.queniuqe.com/d6f589740ff68f9dc8aac061d7e5615acb283418_full.jpg"
                  class="avatar-image"
              >
            </a>
          </div>
          <div class="friends_header_name_ctn">
            <div class="friends_header_name" style="display:inline-flex">
              <a href="" class="name-text">y=f（x）</a>
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
        <a
            data-navid="friends"
            class="icon_item icon_all_friends"
        >
          <span class="title">您的好友</span>
          <svg class="down_arrow_context_menu">
            <polygon points="50 59.49 13.21 22.89 4.74 31.39 50 76.41 95.26 31.39 86.79 22.89 50 59.49"></polygon>
          </svg>
        </a>
        <a
            class="icon_item icon_add_friends"
            href="https://steamcommunity.com/profiles/76561198963541554/friends/add"
        >
          <span class="title">添加好友</span>
          <svg class="down_arrow_context_menu">
            <polygon points="50 59.49 13.21 22.89 4.74 31.39 50 76.41 95.26 31.39 86.79 22.89 50 59.49"></polygon>
          </svg>
        </a>


      </div>

      <!-- 右侧内容区 -->
      <div class="friends_content" id="subpage_container">
        <div  id="friends_list" class="pagecontent no_header">
          <div class="profile_friends title_bar">
            <div class="profile_friends title">
              您的好友 <span class="friends_count">{{ friendsCount }}</span>  <span class="friends_limit"></span>
            </div>
            <div class="action_btns">
              <button
                  id="manage_friends_control"
                  class="btnv6_blue_hoverfade btn_medium"
                  @click="toggleManageFriends"
              >
                管理好友列表
              </button>
              <button
                  id="add_friends_button"
                  class="btn_green_steamui btn_medium"
                  @click="window.location.href='https://steamcommunity.com/profiles/76561198963541554/friends/add'"
              >
                <i class="icon_addfriend"></i> 添加好友
              </button>
            </div>
          </div>

          <div
              id="manage_friends"
              class="manage_friends_panel"
              v-show="isManageMode"
          >
            <div class="row">
              从以下选择好友进行操作
              <span class="row">
                <span class="dimmed">选择：</span>
                <span class="selection_type" @click="selectAll"><a>全部</a></span>
                <span class="selection_type" @click="selectNone"><a>未选择</a></span>
                <span class="selection_type" @click="selectInverse"><a>反选</a></span>
              </span>
            </div>

            <div class="row">
              <div class="manage_friend_actions_ctn">
                <span
                    class="manage_action btnv6_lightblue_blue btn_small"
                    @click="execFriendAction('remove')"
                >
                  移除好友
                </span>
                <span
                    class="manage_action btnv6_lightblue_blue btn_small"
                    @click="execFriendAction('block')"
                >
                  屏蔽
                </span>
              </div>
              <span id="selected_msg" class="selected_msg">
                <span id="selected_count">{{ selectedCount }}</span> 个已选择。
              </span>
            </div>
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
  background: url(https://community.steamstatic.com/public/images/bg_highlight.png) center/cover no-repeat;

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
  display: flex;
  max-width: 1240px;
  margin: 0 auto;
  padding: 20px 10px;
  gap: 20px;
}

.friends_nav {
  min-width: 250px;
  font-family: "Motiva Sans", sans-serif;
}

.icon_item {
  display: block;
  padding: 8px 16px;
  margin: 4px 0;
  text-decoration: none;
  color: #fff;
  border-radius: 3px;
  transition: background-color 0.2s;
  position: relative;
}

.icon_item:hover,
.icon_item.active {
  background-color: #2a4365;
}

.icon_item .title {
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
.friends_container {
  font-family: "Motiva Sans", Sans-serif;
  font-weight: 300; /* light */

  display: flex;
  margin: 0px 10px;
}

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

.friends_nav a.icon_item .title {
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

/* 按钮样式 - 蓝色悬停渐变 */
.btnv6_blue_hoverfade {
  background-color: #4b69ff;
  color: #fff;
  padding: 6px 18px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
}
.btnv6_blue_hoverfade:hover {
  background-color: #3a5bdb;
}

/* 按钮样式 - 绿色Steam风格 */
.btn_green_steamui {
  background-color: #51c851;
  color: #fff;
  padding: 6px 18px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-weight: 500;
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

.action_btns {
  display: flex;
  align-items: center;
  margin-left: auto;
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
}

.friends_search_text_box {
  margin: 0;
  background: #274e68 url(https://community.steamstatic.com/public/images/sharedfiles/searchbox_workshop_submit.gif) no-repeat 1px center;
  border-radius: 3px;
  border: 1px solid rgba(0, 0, 0, 0.3);
  box-shadow: 1px 1px 0px rgba(255, 255, 255, 0.2);
  color: #fff;
  outline: none;
  height: 28px;
  font-size: 14px;
  min-width: 250px;
  padding: 0 0 0 30px;
  font-family: "Motiva Sans", Sans-serif;
  font-weight: 300;
  width: 100%;
  max-width: 500px;
}

.friends_search_text_box::-webkit-input-placeholder {
  color: #7092a5;
  font-style: italic;
}

/* 响应式样式（可选） */
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
</style>