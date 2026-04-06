import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建 axios 实例
const request = axios.create({
  baseURL: 'http://localhost:8080', // 后端 API 基础路径，可根据环境配置
  timeout: 5000, // 请求超时时间
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 从本地存储获取 token
    const token = localStorage.getItem('token')
    if (token) {
      // 在请求头中添加 token
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器 - 核心部分
request.interceptors.response.use(
  (response) => {
    const res = response.data

    // 如果返回的状态码不是 200，说明接口有错误
    if (res.code !== 200) {
      // 显示错误消息
      ElMessage({
        message: res.message || '请求失败',
        type: 'error',
        duration: 3000,
      })

      // 特殊错误码处理（如 401 未登录）
      if (res.code === 401) {
        // 清除 token 并跳转到登录页
        localStorage.removeItem('token')
        router.push('/login')
      }

      return Promise.reject(new Error(res.message || '请求失败'))
    } else {
      // 成功则直接返回数据
      return res
    }
  },
  (error) => {
    console.error('HTTP 错误:', error)

    // 根据 HTTP 状态码显示不同的错误提示
    let message = '网络错误，请稍后重试'

    if (error.response) {
      switch (error.response.status) {
        case 400:
          message = '请求参数错误'
          break
        case 401:
          message = '未授权，请重新登录'
          localStorage.removeItem('token')
          router.push('/login')
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求资源不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        case 502:
          message = '网关错误'
          break
        case 503:
          message = '服务不可用'
          break
        case 504:
          message = '网关超时'
          break
        default:
          message = `连接错误${error.response.status}`
      }
    } else if (error.request) {
      // 请求已发送但没有收到响应
      if (error.code === 'ECONNABORTED') {
        message = '请求超时，请稍后重试'
      } else {
        message = '无法连接到服务器，请检查网络连接'
      }
    }

    ElMessage({
      message: message,
      type: 'error',
      duration: 3000,
    })

    return Promise.reject(error)
  }
)

export default request
