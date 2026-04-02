<template>
  <div class="home-container">
    <div class="welcome-card">
      <h1 class="welcome-title">欢迎来到购物系统</h1>
      <p class="welcome-subtitle">选择您的登录方式</p>

      <div class="role-cards">
        <div
          class="role-card"
          :class="{ active: selectedRole === 'user' }"
          @click="selectRole('user')"
        >
          <el-icon :size="48" color="#409EFF"><User /></el-icon>
          <h3>用户</h3>
          <p>可以浏览和预购商品</p>
        </div>

        <div
          class="role-card"
          :class="{ active: selectedRole === 'staff' }"
          @click="selectRole('staff')"
        >
          <el-icon :size="48" color="#67C23A"><UserFilled /></el-icon>
          <h3>员工</h3>
          <p>管理用户和商品</p>
        </div>

        <div
          class="role-card"
          :class="{ active: selectedRole === 'admin' }"
          @click="selectRole('admin')"
        >
          <el-icon :size="48" color="#E6A23C"><Avatar /></el-icon>
          <h3>管理员</h3>
          <p>全面管理系统</p>
        </div>

        <div
          class="role-card"
          :class="{ active: selectedRole === 'guest' }"
          @click="selectRole('guest')"
        >
          <el-icon :size="48" color="#909399"><Tourist /></el-icon>
          <h3>游客</h3>
          <p>仅浏览商品</p>
        </div>
      </div>

      <el-button
        type="primary"
        size="large"
        :disabled="!selectedRole"
        @click="handleContinue"
        style="width: 200px; margin-top: 30px"
      >
        继续
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const selectedRole = ref('')

const selectRole = (role) => {
  selectedRole.value = role
}

const handleContinue = () => {
  if (selectedRole.value === 'guest') {
    // 游客直接跳转到商品页面
    router.push('/products?role=guest')
  } else {
    // 其他角色跳转到登录页
    router.push(`/login?role=${selectedRole.value}`)
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.welcome-card {
  background: white;
  padding: 50px;
  border-radius: 16px;
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.15);
  text-align: center;
  max-width: 900px;
  width: 100%;
}

.welcome-title {
  color: #333;
  font-size: 36px;
  margin-bottom: 10px;
}

.welcome-subtitle {
  color: #666;
  font-size: 18px;
  margin-bottom: 40px;
}

.role-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 24px;
  margin-bottom: 30px;
}

.role-card {
  padding: 30px 20px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
  background: white;
}

.role-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  border-color: #667eea;
}

.role-card.active {
  border-color: #667eea;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.role-card h3 {
  margin: 16px 0 8px;
  font-size: 20px;
}

.role-card p {
  font-size: 14px;
  opacity: 0.8;
}
</style>

