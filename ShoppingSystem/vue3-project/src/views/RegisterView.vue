<template>
  <div class="register-container">
    <div class="register-card">
      <h2 class="register-title">用户注册</h2>

      <el-alert
        title="注册说明"
        type="info"
        description="系统会自动生成账号，请牢记您的账号和密码！"
        show-icon
        style="margin-bottom: 20px"
      />

      <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input
            v-model="registerForm.name"
            placeholder="请输入姓名"
            prefix-icon="UserFilled"
            size="large"
          />
        </el-form-item>

        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="registerForm.sex" size="large">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请设置密码（至少 6 位）"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>

        <el-form-item label="验证码" prop="captcha">
          <div class="captcha-wrapper">
            <el-input
              v-model="registerForm.captcha"
              placeholder="请输入验证码"
              prefix-icon="Key"
              size="large"
              style="width: 150px"
            />
            <img
              :src="captchaImage"
              @click="refreshCaptcha"
              class="captcha-image"
              title="点击刷新验证码"
            />
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            @click="handleRegister"
            style="width: 100%"
          >
            注册
          </el-button>
        </el-form-item>

        <div class="login-link">
          已有账号？
          <el-link type="primary" @click="goToLogin">返回登录</el-link>
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
const registerFormRef = ref(null)
const loading = ref(false)
const captchaImage = ref('')

const registerForm = reactive({
  name: '',
  sex: '男',
  password: '',
  confirmPassword: '',
  captcha: '',
  captchaToken: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}

const refreshCaptcha = async () => {
  try {
    const res = await request.get('/user/captcha')
    if (res.code === 200) {
      captchaImage.value = res.data.image
      registerForm.captchaToken = res.data.token
      registerForm.captcha = ''
    }
  } catch (error) {
    console.error('获取验证码失败:', error)
    ElMessage.error('获取验证码失败')
  }
}

const handleRegister = async () => {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const params = new URLSearchParams()
        params.append('name', registerForm.name)
        params.append('sex', registerForm.sex)
        params.append('password', registerForm.password)
        params.append('captchaToken', registerForm.captchaToken)
        params.append('captcha', registerForm.captcha)

        const res = await request.post('/user/register', params, {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        })

        if (res.code === 200) {
          ElMessage.success(res.message || '注册成功，请牢记您的账号！')
          router.push('/login')
        } else {
          refreshCaptcha()
        }
      } catch (error) {
        console.error('注册失败:', error)
        ElMessage.error('注册失败：' + (error.response?.data?.message || '请稍后重试'))
        refreshCaptcha()
      } finally {
        loading.value = false
      }
    }
  })
}

const goToLogin = () => {
  router.push('/login')
}

onMounted(() => {
  refreshCaptcha()
})
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 450px;
}

.register-title {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 24px;
}

.login-link {
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
