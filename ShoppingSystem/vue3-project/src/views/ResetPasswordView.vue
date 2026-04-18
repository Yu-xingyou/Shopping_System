<template>
  <div class="reset-container">
    <div class="reset-card">
      <div class="reset-header">
        <h2 class="reset-title">重置密码</h2>
        <el-button type="danger" size="small" @click="goToLogin">
          返回登录
        </el-button>
      </div>

      <el-alert
        title="设置新密码"
        type="warning"
        description="请输入您的新密码，密码长度至少 6 位。重置成功后将使用新密码登录。"
        show-icon
        style="margin-bottom: 20px"
      />

      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="form.newPassword"
            type="password"
            placeholder="请输入新密码（至少6位）"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            prefix-icon="Lock"
            size="large"
            show-password
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
            确认重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utill/request'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await request.post('/user/password/reset', {
          token: route.query.token,
          newPassword: form.newPassword,
          confirmPassword: form.confirmPassword
        })

        if (res.code === 200) {
          ElMessage.success(res.message || '密码重置成功')
          router.push('/login')
        }
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '重置失败')
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
  if (!route.query.token) {
    ElMessage.error('重置链接无效，请重新申请')
    router.push('/forgot-password')
  }
})
</script>

<style scoped>
.reset-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.reset-card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 420px;
}

.reset-header {
  text-align: center;
  margin-bottom: 20px;
}

.reset-title {
  text-align: center;
  color: #333;
  margin-bottom: 10px;
  font-size: 24px;
}
</style>
