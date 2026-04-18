<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2 class="login-title">账号登录</h2>
        <el-button type="danger" size="small" @click="goBackToHome" style="margin-top: 10px;">
          返回首页
        </el-button>
      </div>

      <el-alert
        title="登录说明"
        type="info"
        description="普通用户使用U开头的账号登录，员工和管理员使用数字ID登录。系统会自动识别您的身份。"
        show-icon
        style="margin-bottom: 20px"
      />

      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="账号" prop="userId">
          <el-input
            v-model="loginForm.userId"
            placeholder="请输入账号"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item label="验证码" prop="captcha">
          <el-input
            v-model="loginForm.captcha"
            placeholder="请输入验证码"
            prefix-icon="Key"
            size="large"
            @keyup.enter="handleLogin"
          />
          <img :src="captchaImage" @click="refreshCaptcha" class="captcha-image" />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            @click="handleLogin"
            style="width: 100%"
          >
            登录
          </el-button>
        </el-form-item>

        <div class="register-link">
          没有账号？
          <el-link type="primary" @click="goToRegister">立即注册</el-link>
          <span style="margin: 0 10px; color: #666">|</span>
          <el-link type="primary" @click="goToForgotPassword">忘记密码</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utill/request'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  userId: '',
  password: '',
  captcha: '',
  captchaToken: ''
})

const rules = {
  userId: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}

const captchaImage = ref('')

const refreshCaptcha = async () => {
  try {
    const res = await request.get('/user/captcha')
    if (res.code === 200) {
      captchaImage.value = res.data.image
      loginForm.captchaToken = res.data.token
      loginForm.captcha = ''
    } else {
      ElMessage.error(res.message || '获取验证码失败')
    }
  } catch (error) {
    console.error('获取验证码失败:', error)
    ElMessage.error('获取验证码失败：' + (error.response?.data?.message || '网络错误'))
  }
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      if (!loginForm.userId.startsWith('U') && !/^\d+$/.test(loginForm.userId)) {
        ElMessage.error('账号格式不正确：普通用户账号以U开头，员工/管理员使用数字ID')
        return
      }

      loading.value = true
      try {
        const res = await request.post('/user/login', {
          userId: loginForm.userId,
          password: loginForm.password,
          captchaToken: loginForm.captchaToken,
          captcha: loginForm.captcha
        })

        if (res.code === 200) {
          const token = res.data.token
          const userData = res.data.user

          localStorage.setItem('token', token)
          localStorage.setItem('currentUser', JSON.stringify(userData))
          ElMessage.success('登录成功')

          const userRole = userData.role
          if (userRole === 2) {
            router.push('/admin/dashboard')
          } else if (userRole === 1) {
            router.push('/products')
          } else {
            router.push('/products')
          }
        } else {
          refreshCaptcha()
        }
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error('登录失败：' + (error.response?.data?.message || '账号或密码错误'))
        refreshCaptcha()
      } finally {
        loading.value = false
      }
    }
  })
}

const goToRegister = () => {
  router.push('/register')
}

const goBackToHome = () => {
  router.push('/')
}

const goToForgotPassword = () => {
  router.push('/forgot-password')
}

// 页面加载时获取验证码
onMounted(() => {
  refreshCaptcha()
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 420px;
}

.login-header {
  text-align: center;
  margin-bottom: 20px;
}

.login-title {
  text-align: center;
  color: #333;
  margin-bottom: 10px;
  font-size: 24px;
}

.register-link {
  text-align: center;
  margin-top: 16px;
  color: #666;
}

.captcha-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
}

.captcha-image {
  height: 40px;
  cursor: pointer;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  transition: all 0.3s;
}

.captcha-image:hover {
  border-color: #409EFF;
  box-shadow: 0 0 5px rgba(64, 158, 255, 0.3);
}
</style>

