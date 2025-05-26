<script setup lang="ts">
import { onMounted, ref } from "vue";
import { router } from "../../router";
import { userInfo } from "../../api/user.ts";

const friendId = ref('');
const userId = ref('');
const userName = ref('');
const userAvatar = ref('');
const friendName = ref('door田van氏'); // 示例
const friendAvatar = ref('https://avatars.cloudflare.steamstatic.com/1b1e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e_full.jpg'); // 示例

async function getUserInfo() {
  const res = await userInfo();
  if (res.data.code === '000') {
    const result = res.data.result;
    userId.value = result.id;
    userName.value = result.name;
    userAvatar.value = result.avatar;
    const queryId = router.currentRoute.value.query.friendId;
    friendId.value = typeof queryId === 'string' ? queryId : (Array.isArray(queryId) ? (queryId[0] ?? '') : '');
  } else if (res.data.code === '400') {
    console.log('未登录');
  }
}

onMounted(async () => {
  await getUserInfo();
});

// 示例消息
const messages = ref([
  { id: 1, user: 'door田van氏', avatar: friendAvatar.value, self: false, text: '打扰直说\n速速上号\n别扯有的没的', time: '21:48' },
  { id: 2, user: 'y=f（x）', avatar: '', self: true, text: '不学就上号\n3块\n急急急急急急就', time: '21:48' },
  { id: 3, user: 'door田van氏', avatar: friendAvatar.value, self: false, text: 'No\n大脑需要休息', time: '21:48' },
  { id: 4, user: 'y=f（x）', avatar: '', self: true, text: '我们随便玩\n不需要大脑\n放空你的大脑\n不用赢', time: '21:49' },
  { id: 5, user: 'door田van氏', avatar: friendAvatar.value, self: false, text: '你们加油', time: '21:49' },
  { id: 6, user: 'y=f（x）', avatar: '', self: true, text: '加不了\n火力不够', time: '22:03' },
]);
</script>

<template>
  <div class="chat-root">
    <!-- 顶部栏 -->
    <div class="chat-header">
      <button class="friend-list-btn">打开好友列表</button>
      <div class="chat-title">
        <img :src="friendAvatar" class="avatar" />
        <span class="friend-name">{{ friendName }}</span>
      </div>
    </div>
    <!-- 聊天内容区 -->
    <div class="chat-content">
      <div class="message-list">
        <div v-for="msg in messages" :key="msg.id" class="message-item">
          <img :src="msg.avatar || 'https://community.steamstatic.com/public/images/avatars/ee/eea1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1_full.jpg'" class="msg-avatar" />
          <div class="msg-bubble">
            <div class="msg-user">{{ msg.user }}</div>
            <div class="msg-text" v-html="msg.text.replace(/\n/g, '<br>')"></div>
            <div class="msg-time">{{ msg.time }}</div>
          </div>
        </div>
      </div>
    </div>
    <!-- 底部输入栏 -->
    <div class="chat-input-bar">
      <button class="icon-btn">😊</button>
      <button class="icon-btn">📎</button>
      <input class="chat-input" type="text" placeholder="输入消息..." />
      <button class="send-btn">发送</button>
    </div>
  </div>
</template>

<style scoped>
.chat-root {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 104px); /* 减去GlobalHeader高度 */
  background: #1f2126;
  min-width: 1024px;
}
.chat-header {
  display: flex;
  align-items: center;
  height: 48px;
  background: #23262e;
  border-bottom: 2px solid #373b41;
  padding: 0 16px;
}
.friend-list-btn {
  background: #23262e;
  color: #b8bcbf;
  border: none;
  border-radius: 3px;
  padding: 6px 14px;
  font-size: 14px;
  margin-right: 16px;
  cursor: pointer;
  transition: background 0.2s;
}
.friend-list-btn:hover {
  background: #2a2e38;
}
.chat-title {
  display: flex;
  align-items: center;
}
.avatar {
  width: 28px;
  height: 28px;
  border-radius: 4px;
  margin-right: 8px;
}
.friend-name {
  color: #fff;
  font-size: 16px;
  font-weight: bold;
}
.chat-content {
  flex: 1 1 auto;
  overflow-y: auto;
  padding: 0 0 16px 0;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  scrollbar-width: thin;
  scrollbar-color: #23262e #1a2c45;
  border-bottom: 2px solid #373b41;
}
.message-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 0 32px;
  margin-top: 12px;
}
.message-item {
  display: flex;
  align-items: flex-start;
  flex-direction: row;
  gap: 10px;
}
.msg-avatar {
  width: 36px;
  height: 36px;
  border-radius: 4px;
  background: #222c37;
  object-fit: cover;
  margin-top: 2px;
}
.msg-bubble {
  background: transparent;
  color: #b8bcbf;
  border-radius: 0;
  padding: 0;
  min-width: 60px;
  max-width: 420px;
  word-break: break-all;
  box-shadow: none;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.msg-user {
  font-size: 13px;
  color: #6ea9ff;
  margin-bottom: 2px;
  line-height: 36px;
  height: 36px;
  display: flex;
  align-items: center;
}
.msg-text {
  color: #b8bcbf;
  font-size: 15px;
  margin-bottom: 2px;
  background: none;
  border: none;
  box-shadow: none;
  padding: 0;
}
.msg-time {
  font-size: 11px;
  color: #6c7a89;
  text-align: right;
  margin-top: 2px;
  margin-left: 4px;
}
.chat-input-bar {
  display: flex;
  align-items: center;
  background: #23262e;
  border-top: 2px solid #373b41;
  padding: 10px 16px;
  gap: 8px;
  position: sticky;
  bottom: 0;
  z-index: 10;
}
.icon-btn {
  background: #23262e;
  border: none;
  color: #b8bcbf;
  font-size: 20px;
  border-radius: 3px;
  width: 36px;
  height: 36px;
  cursor: pointer;
  transition: background 0.2s;
}
.icon-btn:hover {
  background: #2a2e38;
}
.chat-input {
  flex: 1;
  background: #1a1d23;
  border: none;
  border-radius: 3px;
  color: #e6e6e6;
  font-size: 15px;
  padding: 8px 12px;
  margin: 0 8px;
  outline: none;
}
.send-btn {
  background: #3b4a5a;
  color: #fff;
  border: none;
  border-radius: 3px;
  padding: 8px 18px;
  font-size: 15px;
  cursor: pointer;
  transition: background 0.2s;
}
.send-btn:hover {
  background: #4e6a8a;
}
</style>