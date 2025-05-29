<script setup lang="ts">
import { onMounted, ref, computed, watch, nextTick } from "vue";
import { userInfo } from "../../api/user.ts";
import { Send } from "../../api/chat.ts";

// 用户信息
const userId = ref('');
const userName = ref('');
const userAvatar = ref('');

// 消息相关
const messageText = ref('');
const messages = ref<Array<{
  id: string | number;
  messageContent: string;
  messageSentTime: Date;
  sender: {
    id: string;
    name: string;
    avatar: string;
  };
}>>([]);
const messageListRef = ref<HTMLElement | null>(null);

// AI客服信息
const aiInfo = ref({
  id: 'ai_customer_service',
  name: 'AI客服',
  avatar: 'https://cdn-icons-png.flaticon.com/512/4712/4712035.png'
});

// 处理消息列表显示逻辑
const processedMessages = computed(() => {
  return messages.value.map((msg, index) => {
    const prevMsg = messages.value[index - 1];
    const nextMsg = messages.value[index + 1];

    const formattedTime = new Date(msg.messageSentTime).toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });

    const showHeader = !prevMsg ||
        prevMsg.sender.id !== msg.sender.id ||
        (new Date(msg.messageSentTime).getTime() - new Date(prevMsg.messageSentTime).getTime()) > 120000;

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
    nextTick(() => {
      messageListRef.value!.scrollTop = messageListRef.value!.scrollHeight;
    });
  }
};

// 获取用户信息
async function getUserInfo() {
  try {
    const res = await userInfo();
    if (res.data.code === '000') {
      const result = res.data.result;
      userId.value = result.id;
      userName.value = result.name;
      userAvatar.value = result.avatar;

      // 添加AI欢迎消息
      addWelcomeMessage();
    } else if (res.data.code === '400') {
      console.log('未登录');
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
}

// 添加欢迎消息
function addWelcomeMessage() {
  messages.value.push({
    id: 'welcome_msg',
    messageContent: '您好！我是SBEAM的AI客服，请问有什么可以帮您的？',
    messageSentTime: new Date(),
    sender: aiInfo.value
  });
  scrollToBottom();
}

// 发送消息
function handleSend() {
  const content = messageText.value.trim();
  if (!content) return;

  // 添加用户消息
  const userMsg = {
    id: Date.now(),
    messageContent: content,
    messageSentTime: new Date(),
    sender: {
      id: userId.value,
      name: userName.value,
      avatar: userAvatar.value
    }
  };
  messages.value.push(userMsg);
  messageText.value = '';

  // 模拟AI思考状态
  const thinkingMsg = {
    id: 'thinking_' + Date.now(),
    messageContent: '正在思考...',
    messageSentTime: new Date(),
    sender: aiInfo.value
  };
  messages.value.push(thinkingMsg);
  scrollToBottom();

  // 发送请求获取AI回复
  Send({ message: content })
      .then(res => {
        // 移除"正在思考"消息
        messages.value = messages.value.filter(msg => msg.id !== thinkingMsg.id);

        // 添加AI回复
        if (res.data && res.data.reply) {
          const aiMsg = {
            id: 'ai_' + Date.now(),
            messageContent: res.data.reply,
            messageSentTime: new Date(),
            sender: aiInfo.value
          };
          messages.value.push(aiMsg);
        } else {
          // 默认回复
          const aiMsg = {
            id: 'ai_' + Date.now(),
            messageContent: '已收到您的消息，我会尽快处理您的问题。',
            messageSentTime: new Date(),
            sender: aiInfo.value
          };
          messages.value.push(aiMsg);
        }
      })
      .catch(error => {
        console.error('发送消息失败:', error);
        // 移除"正在思考"消息
        messages.value = messages.value.filter(msg => msg.id !== thinkingMsg.id);

        // 添加错误提示
        const errorMsg = {
          id: 'error_' + Date.now(),
          messageContent: '服务暂时不可用，请稍后再试。',
          messageSentTime: new Date(),
          sender: aiInfo.value
        };
        messages.value.push(errorMsg);
      });
}

onMounted(async () => {
  await getUserInfo();
});

// 监听消息变化，自动滚动
watch(messages, () => {
  scrollToBottom();
}, { deep: true });
</script>

<template>
  <div class="chat-root">
    <!-- 聊天内容区 -->
    <div class="chat-content">
      <div class="message-list" ref="messageListRef">
        <div
            v-for="msg in processedMessages"
            :key="msg.id"
            class="message-item"
            :class="{ 'ai-message': msg.sender.id === aiInfo.id }"
        >
          <!-- AI消息显示在左侧 -->
          <template v-if="msg.sender.id === aiInfo.id">
            <img
                v-if="msg.showHeader"
                :src="msg.sender.avatar"
                class="msg-avatar"
            />
            <div class="msg-content" :style="{ marginLeft: msg.showHeader ? '0' : '46px' }">
              <div v-if="msg.showHeader" class="msg-user">{{ msg.sender.name }}</div>
              <div class="msg-text">{{ msg.messageContent }}</div>
              <div v-if="msg.showTime" class="msg-time">{{ msg.formattedTime }}</div>
            </div>
          </template>

          <!-- 用户消息显示在右侧 -->
          <template v-else>
            <div class="msg-content user-msg" :style="{ marginRight: msg.showHeader ? '0' : '46px' }">
              <div v-if="msg.showHeader" class="msg-user user-name">{{ msg.sender.name }}</div>
              <div class="msg-text user-text">{{ msg.messageContent }}</div>
              <div v-if="msg.showTime" class="msg-time user-time">{{ msg.formattedTime }}</div>
            </div>
            <img
                v-if="msg.showHeader"
                :src="msg.sender.avatar"
                class="msg-avatar user-avatar"
            />
          </template>
        </div>
      </div>
    </div>

    <!-- 底部输入栏 -->
    <div class="chat-input-bar">
      <input
          class="chat-input"
          type="text"
          placeholder="输入您的问题..."
          v-model="messageText"
          @keyup.enter="handleSend"
      />
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

/* AI消息样式（左侧） */
.message-item:not(.user-message) {
  justify-content: flex-start;
}

/* 用户消息样式（右侧） */
.message-item .user-msg {
  align-items: flex-end;
  text-align: right;
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

.user-avatar {
  order: 2; /* 用户头像在右侧 */
}

.msg-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
  max-width: 420px;
  word-break: break-all;
}

.user-msg {
  margin-left: auto;
  align-items: flex-end;
}

.msg-user {
  font-size: 13px;
  color: #6ea9ff;
  margin-bottom: 2px;
}

.user-name {
  color: #ffa86d; /* 用户名称不同颜色 */
}

.msg-text {
  color: #b8bcbf;
  font-size: 15px;
  line-height: 1.3;
  padding: 8px 12px;
  border-radius: 4px;
  background: #2a2e38;
  display: inline-block;
}

.user-text {
  background: #3b4a5a; /* 用户消息不同背景 */
  color: #fff;
}

.msg-time {
  font-size: 11px;
  color: #6c7a89;
  margin-top: 2px;
}

.user-time {
  text-align: right;
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