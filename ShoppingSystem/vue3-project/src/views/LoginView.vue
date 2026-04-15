<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2 class="login-title">欢迎登录购物系统</h2>
        <el-button type="danger" size="small" @click="goBackToHome" style="margin-top: 10px;">
          返回选择身份
        </el-button>
      </div>

      <el-alert
        title="登录说明"
        type="info"
        description="普通用户使用注册的账号登录，员工和管理员使用分配的账号登录。"
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
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utill/request'

const router = useRouter()
const route = useRoute()
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
      loading.value = true
      try {
        const role = route.query.role || 'user'
        let url = '/user/login'

        if (role === 'staff') {
          url = '/staff/login'
        } else if (role === 'admin') {
          url = '/admin/login'
        }

        const requestData = {
          password: loginForm.password
        }

        // 根据角色传递不同的字段名
        if (role === 'staff') {
          requestData.staffId = loginForm.userId
        } else if (role === 'admin') {
          requestData.adminId = loginForm.userId
        } else {
          requestData.userId = loginForm.userId
        }

        const res = await request.post(url, requestData)

        if (res.code === 200) {
          let token = ''
          let userData = {}

          if (role === 'admin') {
            token = res.data.token
            userData = {
              ...res.data.admin,
              role: role
            }
          } else if (role === 'staff') {
            token = res.data.token
            userData = {
              ...res.data.staff,
              role: role
            }
          } else {
            token = res.data.token
            userData = {
              ...res.data.user,
              role: role
            }
          }

          localStorage.setItem('token', token)
          localStorage.setItem('currentUser', JSON.stringify(userData))
          ElMessage.success('登录成功')

          if (role === 'admin') {
            router.push('/admin/dashboard')
          } else if (role === 'staff') {
            router.push('/products')
          } else {
            router.push('/products')
          }
        }
      } catch (error) {
        console.error(error)
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

