<template>
  <div class="chat-container">
    <div class="session-list">
      <div class="session-header">
        <h3>消息列表</h3>
        <el-button type="primary" size="small" @click="showContactDialog = true">
          <el-icon style="margin-right: 4px;"><Plus /></el-icon>
          新建聊天
        </el-button>
      </div>

      <div
        v-for="session in sessions"
        :key="session.sessionId"
        class="session-item"
        :class="{ active: currentSession === session.sessionId }"
        @click="selectSession(session)"
      >
        <img :src="session.targetAvatar || '/default-avatar.png'" class="avatar" />
        <div class="session-info">
          <div class="session-name">{{ session.targetName }}</div>
          <div class="last-message">{{ session.lastMessage || '暂无消息' }}</div>
        </div>
        <div v-if="session.unreadCount > 0" class="unread-badge">
          {{ session.unreadCount }}
        </div>
      </div>

      <div v-if="sessions.length === 0" class="empty-sessions">
        <p>暂无会话</p>
      </div>
    </div>

    <div class="chat-area" v-if="currentSession">
      <div class="chat-header">
        <img :src="currentTargetAvatar || '/default-avatar.png'" class="header-avatar" />
        <span class="chat-title">{{ currentTargetName }}</span>
      </div>

      <div class="message-list" ref="messageListRef">
        <div
          v-for="msg in messages"
          :key="msg.messageId"
          class="message-item"
          :class="{ 'self': msg.senderId === currentUser.userId }"
        >
          <img :src="msg.senderAvatar || '/default-avatar.png'" class="message-avatar" />
          <div class="message-content">
            <div class="message-text">{{ msg.content }}</div>
            <div class="message-time">{{ formatTime(msg.sendTime) }}</div>
          </div>
        </div>

        <div v-if="messages.length === 0" class="empty-messages">
          <p>开始聊天吧~</p>
        </div>
      </div>

      <div class="input-area">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="输入消息... (Ctrl+Enter 发送)"
          @keyup.enter.ctrl="sendMessage"
        />
        <el-button type="primary" @click="sendMessage" :disabled="!inputMessage.trim()">
          发送
        </el-button>
      </div>
    </div>

    <div class="empty-chat" v-else>
      <p>选择一个会话开始聊天</p>
    </div>
  </div>

  <el-dialog
    v-model="showContactDialog"
    title="选择联系人"
    width="500px"
  >
    <div class="contact-search">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索联系人"
        prefix-icon="Search"
        clearable
      />
    </div>

    <div class="contact-list">
      <div
        v-for="contact in filteredContacts"
        :key="contact.userId"
        class="contact-item"
        @click="startChat(contact)"
      >
        <img :src="contact.avatar || '/default-avatar.png'" class="contact-avatar" />
        <div class="contact-info">
          <div class="contact-name">{{ contact.name }}</div>
          <div class="contact-role">{{ getRoleName(contact.role) }}</div>
        </div>
      </div>

      <div v-if="filteredContacts.length === 0" class="empty-contacts">
        <p>暂无可聊天的联系人</p>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { chatApi } from '@/api/chat'
import ChatWebSocket from '@/utill/ChatWebSocket'
import request from '@/utill/request'

const currentUser = ref(JSON.parse(localStorage.getItem('currentUser')))
const sessions = ref([])
const messages = ref([])
const currentSession = ref(null)
const inputMessage = ref('')
const ws = ref(null)
const messageListRef = ref(null)

const showContactDialog = ref(false)
const contacts = ref([])
const searchKeyword = ref('')

const filteredContacts = computed(() => {
  if (!searchKeyword.value) {
    return contacts.value
  }
  return contacts.value.filter(contact =>
    contact.name.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

const currentTargetName = computed(() => {
  const session = sessions.value.find(s => s.sessionId === currentSession.value)
  return session?.targetName || ''
})

const currentTargetAvatar = computed(() => {
  const session = sessions.value.find(s => s.sessionId === currentSession.value)
  return session?.targetAvatar || ''
})

const getRoleName = (role) => {
  const roleMap = { 0: '普通用户', 1: '员工', 2: '管理员' }
  return roleMap[role] || '未知'
}

const loadContacts = async () => {
  try {
    const res = await request.get('/chat/contacts', {
      params: {
        userId: currentUser.value.userId,
        role: currentUser.value.role
      }
    })
    if (res.code === 200) {
      contacts.value = res.data || []
    }
  } catch (error) {
    console.error('加载联系人失败:', error)
    ElMessage.error('加载联系人失败')
  }
}

const startChat = async (contact) => {
  try {
    const res = await request.post('/chat/session/create', null, {
      params: {
        userId: currentUser.value.userId,
        targetId: contact.userId,
        role: currentUser.value.role
      }
    })

    if (res.code === 200) {
      showContactDialog.value = false
      searchKeyword.value = ''
      await loadSessions()

      const newSession = sessions.value.find(s => s.targetId === contact.userId)
      if (newSession) {
        await selectSession(newSession)
      }

      ElMessage.success(`已开始与 ${contact.name} 的聊天`)
    }
  } catch (error) {
    console.error('发起聊天失败:', error)
    ElMessage.error('发起聊天失败')
  }
}

const loadSessions = async () => {
  try {
    const res = await chatApi.getSessions(currentUser.value.userId, currentUser.value.role)
    if (res.code === 200) {
      const data = res.data || []

      sessions.value = data.map(session => {
        if (session.targetId === currentUser.value.userId) {
          session.unreadCount = 0
        }
        return session
      })

      if (currentSession.value) {
        const currentSessionObj = sessions.value.find(s => s.sessionId === currentSession.value)
        if (currentSessionObj) {
          currentSessionObj.unreadCount = 0
        }
      }
    }
  } catch (error) {
    console.error('加载会话失败:', error)
  }
}

const selectSession = async (session) => {
  currentSession.value = session.sessionId

  try {
    const res = await chatApi.getMessages(session.sessionId)
    if (res.code === 200) {
      messages.value = res.data || []
      nextTick(() => {
        scrollToBottom()
      })
    }
  } catch (error) {
    console.error('加载消息失败:', error)
    ElMessage.error('加载消息失败')
  }
}

const sendMessage = async () => {
  if (!inputMessage.value.trim()) {
    ElMessage.warning('请输入消息内容')
    return
  }

  if (!currentSession.value) {
    ElMessage.warning('请选择一个会话')
    return
  }

  const session = sessions.value.find(s => s.sessionId === currentSession.value)
  if (!session) {
    ElMessage.error('会话不存在')
    return
  }

  const messageContent = inputMessage.value.trim()

  try {
    const res = await request.post('/chat/send', {
      senderId: currentUser.value.userId,
      senderRole: String(currentUser.value.role),
      receiverId: session.targetId === currentUser.value.userId ? session.userId : session.targetId,
      content: messageContent
    })

    if (res.code === 200) {
      inputMessage.value = ''

      const newMessage = {
        messageId: Date.now(),
        senderId: currentUser.value.userId,
        receiverId: session.targetId === currentUser.value.userId ? session.userId : session.targetId,
        content: messageContent,
        sendTime: new Date().toISOString(),
        senderAvatar: currentUser.value.avatar
      }

      messages.value.push(newMessage)
      nextTick(() => {
        scrollToBottom()
      })

      loadSessions()

      const currentSessionObj = sessions.value.find(s => s.sessionId === currentSession.value)
      if (currentSessionObj) {
        currentSessionObj.unreadCount = 0
      }
    }
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送消息失败')
  }
}

const handleIncomingMessage = (data) => {
  console.log('📩 收到 WebSocket 消息:', data)

  if (data.type === 'new_message' && data.data) {
    const newMessage = data.data
    console.log('📨 新消息内容:', newMessage)

    // 如果当前打开了聊天窗口
    if (currentSession.value) {
      const session = sessions.value.find(s => s.sessionId === currentSession.value)
      if (session) {
        // 判断消息是否属于当前会话
        const isMessageInCurrentSession =
          (newMessage.senderId === currentUser.value.userId && newMessage.receiverId === session.targetId) ||
          (newMessage.senderId === session.targetId && newMessage.receiverId === currentUser.value.userId)

        if (isMessageInCurrentSession) {
          console.log('✅ 消息属于当前会话，添加到消息列表')

          // 如果消息没有头像，从当前会话中获取
          if (!newMessage.senderAvatar) {
            if (newMessage.senderId === currentUser.value.userId) {
              newMessage.senderAvatar = currentUser.value.avatar
            } else {
              newMessage.senderAvatar = session.targetAvatar
            }
          }

          messages.value.push(newMessage)
          nextTick(() => {
            scrollToBottom()
          })

          // 如果收到的是对方的消息，刷新会话列表更新未读数
          if (newMessage.senderId !== currentUser.value.userId) {
            loadSessions()
          }
        } else {
          console.log('⚠️ 消息不属于当前会话')
          // 即使不属于当前会话，也要刷新会话列表（更新左侧消息列表）
          loadSessions()
        }
      }
    } else {
      console.log('⚠️ 当前没有打开的会话')
      // 没有打开会话时，刷新会话列表显示新消息
      loadSessions()
    }

    // 只在收到别人的消息时才提示
    if (newMessage.senderId !== currentUser.value.userId) {
      ElMessage.info('收到新消息')
    }
  }
}

const scrollToBottom = () => {
  if (messageListRef.value) {
    messageListRef.value.scrollTop = messageListRef.value.scrollHeight
  }
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date

  if (diff < 60000) {
    return '刚刚'
  } else if (diff < 3600000) {
    return `${Math.floor(diff / 60000)}分钟前`
  } else if (diff < 86400000) {
    return `${Math.floor(diff / 3600000)}小时前`
  } else {
    return date.toLocaleString('zh-CN', {
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  }
}

const connectWebSocket = () => {
  try {
    ws.value = new ChatWebSocket(currentUser.value.userId)

    ws.value.onMessage((data) => {
      handleIncomingMessage(data)
    })

    ws.value.connect()
  } catch (error) {
    console.error('WebSocket 连接失败:', error)
  }
}

onMounted(() => {
  if (!currentUser.value) {
    ElMessage.error('请先登录')
    return
  }

  connectWebSocket()
  loadSessions()
  loadContacts()
})

onUnmounted(() => {
  if (ws.value) {
    ws.value.disconnect()
  }

})
</script>

<style scoped>
.chat-container {
  display: flex;
  height: calc(100vh - 100px);
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.session-list {
  width: 320px;
  border-right: 1px solid #e8e8e8;
  display: flex;
  flex-direction: column;
  background: #fafafa;
}

.session-header {
  padding: 15px 20px;
  border-bottom: 1px solid #e8e8e8;
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.session-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.session-list {
  overflow-y: auto;
  flex: 1;
}

.session-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  cursor: pointer;
  transition: all 0.3s;
  border-bottom: 1px solid #f0f0f0;
  position: relative;
}

.session-item:hover {
  background: #f0f0f0;
}

.session-item.active {
  background: #e6f7ff;
  border-left: 3px solid #1890ff;
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  margin-right: 12px;
  object-fit: cover;
}

.session-info {
  flex: 1;
  min-width: 0;
}

.session-name {
  font-size: 15px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.last-message {
  font-size: 13px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.unread-badge {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  background: #ff4d4f;
  color: white;
  border-radius: 10px;
  padding: 2px 8px;
  font-size: 12px;
  min-width: 20px;
  text-align: center;
}

.empty-sessions {
  padding: 40px 20px;
  text-align: center;
  color: #999;
}

.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #fff;
}

.chat-header {
  padding: 15px 20px;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
  background: #fafafa;
}

.header-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
  object-fit: cover;
}

.chat-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f5f5f5;
}

.message-item {
  display: flex;
  margin-bottom: 16px;
  animation: fadeIn 0.3s;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message-item.self {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  margin: 0 10px;
  object-fit: cover;
}

.message-content {
  max-width: 60%;
}

.message-text {
  background: #fff;
  padding: 10px 14px;
  border-radius: 8px;
  word-wrap: break-word;
  line-height: 1.5;
  color: #333;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message-item.self .message-text {
  background: #1890ff;
  color: white;
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
  padding: 0 4px;
}

.message-item.self .message-time {
  text-align: right;
}

.empty-messages {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.input-area {
  padding: 15px 20px;
  border-top: 1px solid #e8e8e8;
  display: flex;
  gap: 10px;
  background: #fff;
}

.input-area :deep(.el-textarea__inner) {
  resize: none;
}

.empty-chat {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 16px;
}

.contact-search {
  margin-bottom: 15px;
}

.contact-list {
  max-height: 400px;
  overflow-y: auto;
}

.contact-item {
  display: flex;
  align-items: center;
  padding: 12px 15px;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.3s;
  margin-bottom: 5px;
}

.contact-item:hover {
  background: #f0f0f0;
}

.contact-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
  object-fit: cover;
}

.contact-info {
  flex: 1;
}

.contact-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.contact-role {
  font-size: 12px;
  color: #999;
}

.empty-contacts {
  text-align: center;
  padding: 40px 20px;
  color: #999;
}
</style>
