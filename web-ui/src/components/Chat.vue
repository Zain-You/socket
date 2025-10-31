<template>
  <div class="chat-container">
    <h2>WebSocket 聊天室</h2>

    <!-- 连接配置区域 -->
    <div class="config-box">
      <input v-model="serverUrl" placeholder="服务器地址 (例如 ws://localhost:8066/ws)" />
      <input v-model="username" placeholder="发送者名称" />
      <button class="btn" @click="connectWs" :disabled="connected">
        {{ connected ? "已连接" : "连接" }}
      </button>
    </div>

    <!-- 聊天记录显示 -->
    <div class="chat-box">
      <div
          v-for="(msg, index) in messages"
          :key="index"
          class="chat-item"
          :class="{ me: msg.sender === username }"
      >
        <div class="sender">{{ msg.sender }}</div>
        <div class="content">{{ msg.content }}</div>
        <div class="time">{{ msg.timestamp }}</div>
      </div>
    </div>

    <!-- 输入框 -->
    <div class="input-box">
      <input
          v-model="input"
          @keyup.enter="sendMessage"
          placeholder="输入消息后回车发送"
          :disabled="!connected"
      />
      <button class="btn" @click="sendMessage" :disabled="!connected">发送</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onUnmounted } from "vue";

const serverUrl = ref("ws://localhost:8066/ws"); // 默认服务器地址
const username = ref("前端用户"); // 默认用户名
const ws = ref(null);
const input = ref("");
const messages = ref([]);
const connected = ref(false);

// 连接 WebSocket
function connectWs() {
  if (!serverUrl.value.startsWith("ws://") && !serverUrl.value.startsWith("wss://")) {
    alert("请输入正确的 WebSocket 地址！");
    return;
  }

  ws.value = new WebSocket(serverUrl.value);

  ws.value.onopen = () => {
    connected.value = true;
    console.log("WebSocket 已连接");
  };

  ws.value.onmessage = (event) => {
    const msg = JSON.parse(event.data);
    messages.value.push(msg);
  };

  ws.value.onclose = () => {
    connected.value = false;
    console.log(" WebSocket 已关闭");
  };
}

// 发送消息
function sendMessage() {
  if (!input.value.trim()) return;

  const msg = {
    sender: username.value,
    content: input.value,
    timestamp: new Date().toLocaleString(),
  };

  ws.value.send(JSON.stringify(msg));
  input.value = "";
}

onUnmounted(() => {
  if (ws.value) ws.value.close();
});
</script>

<style scoped>
.btn{border:1px solid gray;}
.chat-container {
  width: 600px;
  margin: 40px auto;
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 16px;
  background: #fafafa;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.config-box {
  display: flex;
  gap: 6px;
}
.chat-box {
  flex: 1;
  border: 1px solid #ccc;
  padding: 10px;
  overflow-y: auto;
  height: 300px;
  background: #fff;
  min-height: 250px;
}
.chat-item {
  margin-bottom: 8px;
  text-align: left;
}
.chat-item.me {
  text-align: right;
}
.input-box {
  display: flex;
  gap: 8px;
}
</style>
