<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <h2>个人信息</h2>
        </div>
      </template>

      <el-form :model="profileForm" :rules="rules" ref="profileFormRef" label-width="100px">
        <el-form-item label="账号" prop="userId">
          <el-input v-model="profileForm.userId" disabled />
        </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-input v-model="profileForm.name" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="性别" prop="sex" v-if="isUser">
          <el-radio-group v-model="profileForm.sex">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
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
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utill/request'

const router = useRouter()
const loading = ref(false)
const profileFormRef = ref(null)
const currentUser = ref(null)

const profileForm = reactive({
  userId: '',
  name: '',
  sex: '',
  password: ''
})

const isUser = computed(() => {
  return currentUser.value && currentUser.value.role === 'user'
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
    profileForm.userId = currentUser.value.userId || currentUser.value.staffId || currentUser.value.adminId
    profileForm.name = currentUser.value.name || ''
    profileForm.sex = currentUser.value.sex || '男'
  } else {
    ElMessage.error('请先登录')
    router.push('/login')
  }
}

const updateProfile = async () => {
  if (!profileFormRef.value) return

  await profileFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        let url = ''
        let requestData = {}

        if (currentUser.value.role === 'user') {
          url = `/user/${profileForm.userId}`
          requestData = {
            userId: profileForm.userId,
            name: profileForm.name,
            sex: profileForm.sex
          }
          if (profileForm.password && profileForm.password.trim()) {
            if (profileForm.password.length < 6) {
              ElMessage.error('密码长度不能少于 6 位')
              loading.value = false
              return
            }
            requestData.password = profileForm.password
          }
        } else if (currentUser.value.role === 'staff') {
          url = `/staff/${profileForm.userId}`
          requestData = {
            staffId: profileForm.userId,
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
        } else if (currentUser.value.role === 'admin') {
          url = `/admin/${profileForm.userId}`
          requestData = {
            adminId: profileForm.userId,
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
        }

        const res = await request.put(url, requestData)

        if (res.code === 200) {
          ElMessage.success('个人信息更新成功')

          const updatedUser = {
            ...currentUser.value,
            name: profileForm.name
          }
          if (currentUser.value.role === 'user') {
            updatedUser.sex = profileForm.sex
          }
          localStorage.setItem('currentUser', JSON.stringify(updatedUser))

          setTimeout(() => {
            router.go(-1)
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
  router.go(-1)
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
</style>
