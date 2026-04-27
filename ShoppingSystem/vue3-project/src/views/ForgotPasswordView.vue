<template>
  <div class="forgot-container">
    <div class="forgot-card">
      <div class="forgot-header">
        <h2 class="forgot-title">忘记密码</h2>
        <el-button type="danger" size="small" @click="goToLogin">
          返回登录
        </el-button>
      </div>

      <el-alert
        title="找回密码说明"
        type="info"
        description="请输入您注册时使用的邮箱，我们将发送重置密码链接到您的邮箱。链接 30 分钟内有效。"
        show-icon
        style="margin-bottom: 20px"
      />

      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="form.email"
            placeholder="请输入注册邮箱"
            prefix-icon="Message"
            size="large"
            @keyup.enter="handleSubmit"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            @click="handleSubmit"
            style="width: 100%"
          >
            发送重置链接
          </el-button>
        </el-form-item>
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
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  email: ''
})

const rules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await request.post('/user/password/forgot', {
          email: form.email
        })

        if (res.code === 200) {
          ElMessage.success(res.message || '重置邮件已发送，请查收')
          router.push('/login')
        }
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '发送失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.forgot-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.forgot-card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 420px;
}

.forgot-header {
  text-align: center;
  margin-bottom: 20px;
}

.forgot-title {
  text-align: center;
  color: #333;
  margin-bottom: 10px;
  font-size: 24px;
}
</style>
