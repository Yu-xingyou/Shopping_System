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
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utill/request'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  userId: '',
  password: ''
})

const rules = {
  userId: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      // 验证账号格式
      if (!loginForm.userId.startsWith('U') && !/^\d+$/.test(loginForm.userId)) {
        ElMessage.error('账号格式不正确：普通用户账号以U开头，员工/管理员使用数字ID')
        return
      }

      loading.value = true
      try {
        // 调用统一登录接口，后端自动识别角色
        const res = await request.post('/user/login', {
          userId: loginForm.userId,
          password: loginForm.password
        })

        if (res.code === 200) {
          const token = res.data.token
          const userData = res.data.user

          // 保存用户信息和token
          localStorage.setItem('token', token)
          localStorage.setItem('currentUser', JSON.stringify(userData))
          ElMessage.success('登录成功')

          // 根据用户角色跳转到不同页面
          const userRole = userData.role
          if (userRole === 2) {
            // 管理员
            router.push('/admin/dashboard')
          } else if (userRole === 1) {
            // 员工
            router.push('/products')
          } else {
            // 普通用户
            router.push('/products')
          }
        }
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error('登录失败：' + (error.response?.data?.message || '账号或密码错误'))
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
</style>

