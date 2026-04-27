import request from '@/utill/request'

export const chatApi = {
  getSessions(userId, role) {
    return request.get('/chat/sessions', { params: { userId, role } })
  },

  getMessages(sessionId) {
    return request.get('/chat/messages', { params: { sessionId } })
  },

  sendMessage(data) {
    return request.post('/chat/send', data)
  },

  markAsRead(messageIds) {
    return request.post('/chat/read', messageIds)
  },

  getUnreadMessages(userId) {
    return request.get('/chat/unread', { params: { userId } })
  }
}
