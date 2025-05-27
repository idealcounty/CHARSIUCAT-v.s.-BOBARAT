<script setup lang="ts">
import { onMounted, ref, computed, watch, nextTick } from "vue";
import { router } from "../../router";
import { User, userInfo } from "../../api/user.ts";
import { FriendInfo, GetMessages, messageInfo, SendMessage } from "../../api/message.ts";

const friendId = ref('');
const userId = ref('');
const userName = ref('');
const userAvatar = ref('');
const friendName = ref('');
const friendAvatar = ref('');
const self = ref<User>()
const receiver = ref<User>()
const messageText = ref('')
const messages = ref([])
const messageListRef = ref(null);

// 处理消息列表，添加显示控制
const processedMessages = computed(() => {
  if (!messages.value || messages.value.length === 0) return [];

  return messages.value.map((msg, index) => {
    const prevMsg = messages.value[index - 1];
    const nextMsg = messages.value[index + 1];

    // 格式化时间为时分秒（HH:MM:SS）
    const formattedTime = new Date(msg.messageSentTime).toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });

    // 判断是否需要显示头像和用户名
    // 如果是第一条消息 或 发送者不同 或 时间间隔超过2分钟，则显示头像和用户名
    const showHeader = !prevMsg ||
        prevMsg.sender.id !== msg.sender.id ||
        (new Date(msg.messageSentTime).getTime() - new Date(prevMsg.messageSentTime).getTime()) > 120000; // 120000ms = 2分钟

    // 判断是否需要显示时间（在连续消息中只在最后一条显示时间）
    const showTime = !nextMsg ||
        nextMsg.sender.id !== msg.sender.id ||
        (new Date(nextMsg.messageSentTime).getTime() - new Date(msg.messageSentTime).getTime()) > 120000;

    return {
      ...msg,
      showHeader,
      showTime,
      formattedTime,
      isFirst: showHeader,
      isLast: showTime
    };
  });
});

// 自动滚动到底部
const scrollToBottom = () => {
  if (messageListRef.value) {
    messageListRef.value.scrollTop = messageListRef.value.scrollHeight;
  }
};

async function getUserInfo() {
  const res = await userInfo();
  if (res.data.code === '000') {
    const result = res.data.result;
    userId.value = result.id;
    userName.value = result.name;
    userAvatar.value = result.avatar;
    self.value = result;
    friendId.value = router.currentRoute.value.query.friendId;
    FriendInfo(Number(friendId.value)).then(res => {
      receiver.value = res.data.result;
      friendName.value = res.data.result.name;
      friendAvatar.value = res.data.result.avatar;
      GetMessages(Number(userId.value), Number(friendId.value)).then(res => {
        messages.value = res.data.result;
        scrollToBottom(); // 加载消息后滚动到底部
      })
    })
  } else if (res.data.code === '400') {
    console.log('未登录');
  }
}

onMounted(async () => {
  await getUserInfo();
});

function handleSend() {
  if (!messageText.value.trim()) return; // 防止发送空消息

  SendMessage({
    messageContent: messageText.value,
    sender: self.value,
    receiver: receiver.value,
  }).then(res => {
    console.log(res);
    messageText.value = "";
    // 发送消息后刷新消息列表
    GetMessages(Number(userId.value), Number(friendId.value)).then(res => {
      messages.value = res.data.result;
      scrollToBottom(); // 发送消息后滚动到底部
    });
  }).catch(error => {
    console.error('发送消息失败:', error);
    // 可以添加错误提示
  })
}

// 监听消息变化，自动滚动
watch(messages, () => {
  nextTick(() => {
    scrollToBottom();
  });
});

function handleList(){
  router.push({path: '/friend'});
}
</script>

<template>
  <div class="chat-root">
    <!-- 顶部栏 -->
    <div class="chat-header">
      <button class="friend-list-btn" @click="handleList">打开好友列表</button>
      <div class="chat-title">
        <img :src="friendAvatar" class="avatar" />
        <span class="friend-name">{{ friendName }}</span>
      </div>
    </div>
    <!-- 聊天内容区 -->
    <div class="chat-content">
      <div class="message-list" ref="messageListRef">
        <div
            v-for="msg in processedMessages"
            :key="msg.id"
            class="message-item"
        >
          <!-- 头像（仅首条消息显示） -->
          <img
              v-if="msg.showHeader"
              :src="msg.sender.avatar || 'https://community.steamstatic.com/public/images/avatars/ee/eea1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1e1_full.jpg'"
              class="msg-avatar"
          />

          <!-- 消息内容 -->
          <div class="msg-content" :style="{ marginLeft: msg.showHeader ? '0' : '46px' }">
            <div v-if="msg.showHeader" class="msg-user">{{ msg.sender.name }}</div>
            <div class="msg-text">{{ msg.messageContent }}</div>
            <div v-if="msg.showTime" class="msg-time">{{ msg.formattedTime }}</div>
          </div>
        </div>
      </div>
    </div>
    <!-- 底部输入栏 -->
    <div class="chat-input-bar">
      <input
          class="chat-input"
          type="text"
          placeholder="输入消息..."
          v-model="messageText"
          @keyup.enter="handleSend"
      />
      <button class="icon-btn">😊</button>
      <button class="icon-btn">📎</button>
      <button class="send-btn" @click="handleSend">发送</button>
    </div>
  </div>
</template>

<style scoped>
.chat-root {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 104px);
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
  scrollbar-width: thin;
  scrollbar-color: #23262e #1a2c45;
  border-bottom: 2px solid #373b41;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 1px;
  padding: 0 32px;
  margin-top: 12px;
}

.message-item {
  display: flex;
  flex-direction: row;
  gap: 8px;
  padding: 1px 0;
}

.msg-avatar {
  width: 36px;
  height: 36px;
  border-radius: 4px;
  background: #222c37;
  object-fit: cover;
  margin-top: 2px;
  flex-shrink: 0;
}

.msg-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
  max-width: 420px;
  word-break: break-all;
}

.msg-user {
  font-size: 13px;
  color: #6ea9ff;
  margin-bottom: 2px;
}

.msg-text {
  color: #b8bcbf;
  font-size: 15px;
  line-height: 1.3;
  padding: 2px 0;
}

.msg-time {
  font-size: 11px;
  color: #6c7a89;
  text-align: right;
  margin-top: 2px;
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

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: #1f2126;
}

::-webkit-scrollbar-thumb {
  background: #373b41;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #444a54;
}
</style>