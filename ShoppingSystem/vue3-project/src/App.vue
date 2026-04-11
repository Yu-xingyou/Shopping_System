<script setup>
import { computed, watch, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const currentUser = ref(null)

// 判断是否显示导航栏
const showNav = computed(() => {
  return route.path !== '/' && route.path !== '/login' && route.path !== '/register'
})

const loadCurrentUser = () => {
  try {
    const savedUser = localStorage.getItem('currentUser')
    if (savedUser) {
      currentUser.value = JSON.parse(savedUser)
    } else {
      currentUser.value = null
    }
  } catch (error) {
    console.error('解析用户信息失败:', error)
    currentUser.value = null
  }
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('currentUser')
  localStorage.removeItem('shoppingCart')
  ElMessage.success('已退出登录')
  router.push('/')
}

const goToProfile = () => {
  if (currentUser.value?.role === 'user') {
    router.push('/profile')
  }
}

const navigateToRevenue = () => {
  router.push('/admin/revenue')
}

onMounted(() => {
  loadCurrentUser()
})

watch(() => route.path, () => {
  loadCurrentUser()
})
</script>

<template>
  <div class="app-container">
    <header v-if="showNav" class="app-header">
      <div class="header-content">
        <div class="user-info" @click="goToProfile" style="cursor: pointer;">
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
          <RouterLink
            v-if="currentUser?.role === 'admin'"
            to="/admin/revenue"
            class="nav-item revenue-link"
          >
            我的收益
          </RouterLink>
          <RouterLink
            v-if="currentUser?.role === 'admin'"
            to="/admin/hot-products"
            class="nav-item"
          >
            热销产品统计
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

<style scoped>
.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 15px 30px;
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
  color: white;
  font-size: 16px;
  font-weight: 500;
}

.nav-links {
  display: flex;
  gap: 20px;
  align-items: center;
}

.nav-item {
  color: white;
  text-decoration: none;
  font-size: 15px;
  padding: 8px 16px;
  border-radius: 6px;
  transition: all 0.3s;
}

.nav-item:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.nav-item.router-link-active {
  background-color: rgba(255, 255, 255, 0.3);
  font-weight: bold;
}

.revenue-link {
  background-color: rgba(245, 108, 108, 0.3);
  font-weight: 500;
}

.revenue-link:hover {
  background-color: rgba(245, 108, 108, 0.5) !important;
}

.main-content {
  flex: 1;
  background-color: #f5f7fa;
}
</style>
