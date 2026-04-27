class ChatWebSocket {
  constructor(userId) {
    this.userId = userId
    this.ws = null
    this.messageHandlers = []
    this.reconnectTimer = null
    this.isManualClose = false
  }

  connect() {
    if (this.ws && (this.ws.readyState === WebSocket.CONNECTING || this.ws.readyState === WebSocket.OPEN)) {
      console.log('WebSocket 已连接或正在连接')
      return
    }

    this.isManualClose = false
    const wsUrl = `ws://localhost:8080/ws/chat/${this.userId}`

    try {
      this.ws = new WebSocket(wsUrl)

      this.ws.onopen = () => {
        console.log('WebSocket 连接成功')
        if (this.reconnectTimer) {
          clearTimeout(this.reconnectTimer)
          this.reconnectTimer = null
        }
      }

      this.ws.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data)

          if (data.type === 'new_message' && data.data) {
            this.messageHandlers.forEach(handler => handler(data))
          }
        } catch (error) {
          console.error('解析消息失败:', error)
        }
      }

      this.ws.onerror = (error) => {
        console.error('WebSocket 错误:', error)
      }

      this.ws.onclose = () => {
        console.log('WebSocket 连接关闭')

        if (!this.isManualClose) {
          this.reconnectTimer = setTimeout(() => {
            console.log('尝试重新连接 WebSocket...')
            this.connect()
          }, 5000)
        }
      }
    } catch (error) {
      console.error('创建 WebSocket 连接失败:', error)
    }
  }

  onMessage(handler) {
    if (typeof handler === 'function') {
      this.messageHandlers.push(handler)
    }
  }

  disconnect() {
    this.isManualClose = true

    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer)
      this.reconnectTimer = null
    }

    if (this.ws) {
      this.ws.close()
      this.ws = null
    }
  }
}

export default ChatWebSocket
