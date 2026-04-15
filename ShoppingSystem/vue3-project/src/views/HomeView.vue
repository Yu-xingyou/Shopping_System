<template>
  <div class="home-container">
    <div class="welcome-card">
      <h1 class="welcome-title">欢迎来到购物系统</h1>
      <p class="welcome-subtitle">选择登录方式</p>

      <div class="action-cards">
        <div
          class="action-card login-card"
          @click="goToLogin"
        >
          <el-icon :size="48" color="#409EFF"><User /></el-icon>
          <h3>账号登录</h3>
          <p>使用账号密码登录系统</p>
          <el-button type="primary" size="default" style="margin-top: 15px;">
            立即登录
          </el-button>
        </div>

        <div
          class="action-card guest-card"
          @click="guestLogin"
        >
          <el-icon :size="48" color="#909399"><Tourist /></el-icon>
          <h3>游客浏览</h3>
          <p>无需登录，仅浏览商品</p>
          <el-button type="info" size="default" style="margin-top: 15px;">
            游客进入
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToLogin = () => {
  router.push('/login')
}

const guestLogin = () => {
  router.push('/products?role=guest')
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
  max-width: 700px;
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

.action-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  margin-bottom: 30px;
}

.action-card {
  padding: 40px 30px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
  background: white;
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  border-color: #667eea;
}

.login-card:hover {
  border-color: #409EFF;
}

.guest-card:hover {
  border-color: #909399;
}

.action-card h3 {
  margin: 16px 0 8px;
  font-size: 22px;
  color: #333;
}

.action-card p {
  font-size: 14px;
  color: #666;
}

@media (max-width: 600px) {
  .action-cards {
    grid-template-columns: 1fr;
  }
}
</style>

