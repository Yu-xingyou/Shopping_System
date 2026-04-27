<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <h2>个人信息</h2>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>

      <!-- 头像展示区域 -->
      <div class="avatar-section">
        <div class="avatar-wrapper" @click="triggerFileInput">
          <img
            v-if="profileForm.avatar"
            :src="profileForm.avatar"
            alt="头像"
            class="avatar-img"
          />
          <div v-else class="avatar-placeholder">
            <el-icon :size="60"><User /></el-icon>
          </div>
          <div class="avatar-overlay">
            <el-icon><Camera /></el-icon>
            <span>更换头像</span>
          </div>
        </div>
        <input
          ref="fileInputRef"
          type="file"
          accept="image/*"
          style="display: none"
          @change="handleAvatarUpload"
        />
        <p class="avatar-tip">点击头像上传图片</p>
      </div>

      <el-form :model="profileForm" :rules="rules" ref="profileFormRef" label-width="100px">
        <el-form-item label="账号" prop="userId">
          <el-input v-model="profileForm.userId" disabled />
        </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-input v-model="profileForm.name" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="修改密码">
          <el-input
            v-model="profileForm.password"
            type="password"
            placeholder="留空则不修改密码"
            show-password
          />
          <div style="font-size: 12px; color: #999; margin-top: 5px;">
            如需修改密码，请输入新密码（至少 6 位），否则留空
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="updateProfile" :loading="loading">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utill/request'

const router = useRouter()
const loading = ref(false)
const profileFormRef = ref(null)
const fileInputRef = ref(null)
const currentUser = ref(null)

const profileForm = reactive({
  userId: '',
  name: '',
  password: '',
  avatar: ''
})

const rules = {
  userId: [
    { required: true, message: '账号不能为空', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '姓名不能为空', trigger: 'blur' },
    { min: 1, max: 50, message: '姓名长度在 1 到 50 个字符', trigger: 'blur' }
  ]
}

const loadUserProfile = () => {
  const savedUser = localStorage.getItem('currentUser')
  if (savedUser) {
    currentUser.value = JSON.parse(savedUser)
    profileForm.userId = currentUser.value.userId
    profileForm.name = currentUser.value.name || ''
    profileForm.avatar = currentUser.value.avatar || ''
  } else {
    ElMessage.error('请先登录')
    router.push('/login')
  }
}

const triggerFileInput = () => {
  fileInputRef.value.click()
}

const handleAvatarUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  if (!file.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件')
    return
  }

  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 5MB')
    return
  }

  loading.value = true
  try {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('adminId', profileForm.userId)

    const res = await request.post('/admin/avatar/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (res.code === 200) {
      ElMessage.success('头像上传成功')
      profileForm.avatar = res.data.avatarUrl

      const updatedUser = {
        ...currentUser.value,
        avatar: res.data.avatarUrl
      }
      localStorage.setItem('currentUser', JSON.stringify(updatedUser))
    }
  } catch (error) {
    console.error(error)
    ElMessage.error(error.response?.data?.message || '头像上传失败')
  } finally {
    loading.value = false
    event.target.value = ''
  }
}

const updateProfile = async () => {
  if (!profileFormRef.value) return

  await profileFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const url = `/admin/${profileForm.userId}`
        const requestData = {
          userId: profileForm.userId,
          name: profileForm.name
        }

        if (profileForm.password && profileForm.password.trim()) {
          if (profileForm.password.length < 6) {
            ElMessage.error('密码长度不能少于 6 位')
            loading.value = false
            return
          }
          requestData.password = profileForm.password
        }

        const res = await request.put(url, requestData)

        if (res.code === 200) {
          ElMessage.success('个人信息更新成功')

          const updatedUser = {
            ...currentUser.value,
            name: profileForm.name
          }
          localStorage.setItem('currentUser', JSON.stringify(updatedUser))

          setTimeout(() => {
            router.push('/products')
          }, 1000)
        }
      } catch (error) {
        console.error(error)
        ElMessage.error(error.response?.data?.message || '更新失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const goBack = () => {
  router.push('/products')
}

onMounted(() => {
  loadUserProfile()
})
</script>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.profile-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.avatar-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  border: 3px solid #e0e0e0;
  transition: all 0.3s;
}

.avatar-wrapper:hover {
  border-color: #667eea;
  transform: scale(1.05);
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  color: #909399;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  opacity: 0;
  transition: opacity 0.3s;
}

.avatar-overlay .el-icon {
  margin-bottom: 5px;
}

.avatar-tip {
  margin-top: 10px;
  font-size: 13px;
  color: #909399;
}
</style>

