<script setup>
import { computed, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

// 判断是否显示导航栏
const showNav = computed(() => {
  return route.path !== '/' && route.path !== '/login' && route.path !== '/register'
})
</script>

<template>
  <div class="app-container">
    <header v-if="showNav" class="app-header">
      <div class="header-content">
        <div class="user-info">
          <el-icon :size="20" style="margin-right: 8px"><User /></el-icon>
          <span>{{ currentUser?.name || '未登录' }}</span>
        </div>
        <div class="nav-links">
          <RouterLink to="/products" class="nav-item">商品列表</RouterLink>
          <RouterLink
            v-if="currentUser?.role === 'user'"
            to="/user-orders"
            class="nav-item"
          >
            我的订单
          </RouterLink>
          <RouterLink
            v-if="currentUser?.role === 'staff' || currentUser?.role === 'admin'"
            to="/orders"
            class="nav-item"
          >
            订单管理
          </RouterLink>
          <RouterLink
            v-if="currentUser?.role === 'admin'"
            to="/staffs"
            class="nav-item"
          >
            员工管理
          </RouterLink>
          <RouterLink
            v-if="currentUser?.role === 'staff'"
            to="/users"
            class="nav-item"
          >
            用户管理
          </RouterLink>
          <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
        </div>
      </div>
    </header>

    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentUser: null
    }
  },
  created() {
    this.loadCurrentUser()
    // 监听路由变化，重新加载用户信息
    this.$watch(
      () => this.$route.path,
      () => {
        this.loadCurrentUser()
      }
    )
  },
  methods: {
    loadCurrentUser() {
      try {
        const savedUser = localStorage.getItem('currentUser')
        if (savedUser) {
          this.currentUser = JSON.parse(savedUser)
        } else {
          this.currentUser = null
        }
      } catch (error) {
        console.error('解析用户信息失败:', error)
        this.currentUser = null
      }
    },
    handleLogout() {
      localStorage.removeItem('currentUser')
      localStorage.removeItem('token')
      this.$router.push('/')
      this.currentUser = null
    }
  }
}
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 1rem 2rem;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 20px;
}

.nav-item {
  color: white;
  text-decoration: none;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s;
}

.nav-item:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.main-content {
  padding: 20px;
}
</style>
