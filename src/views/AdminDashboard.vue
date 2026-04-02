<template>
  <div class="dashboard-container">
    <el-card class="dashboard-card">
      <template #header>
        <div class="card-header">
          <h2>管理员仪表盘</h2>
          <div style="display: flex; gap: 10px; align-items: center;">
            <span v-if="currentUser" style="color: #666; font-size: 14px;">
              欢迎您，{{ currentUser.name }}
            </span>
            <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </template>

      <div class="quick-actions">
        <h3>快捷操作</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-button type="primary" @click="navigateTo('/users')" style="width: 100%">用户管理</el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="success" @click="navigateTo('/staffs')" style="width: 100%">员工管理</el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="warning" @click="navigateTo('/orders')" style="width: 100%">订单管理</el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="info" @click="navigateTo('/products')" style="width: 100%">商品管理</el-button>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const currentUser = ref(null)

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('currentUser')
  ElMessage.success('已退出登录')
  router.push('/')
}

const navigateTo = (path) => {
  router.push(path)
}

onMounted(() => {
  currentUser.value = JSON.parse(localStorage.getItem('currentUser'))
})
</script>

<style scoped>
.dashboard-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.dashboard-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.quick-actions {
  margin-top: 30px;
}

.quick-actions h3 {
  margin-bottom: 20px;
  color: #333;
}
</style>

