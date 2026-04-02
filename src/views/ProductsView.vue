<template>
  <div class="products-container">
    <el-card class="products-card">
      <template #header>
        <div class="card-header">
          <h2>商品列表</h2>
        </div>
      </template>

      <div style="display: flex; gap: 10px; margin-bottom: 20px;" v-if="isAdmin">
        <el-input
          v-model="searchText"
          placeholder="搜索商品名称"
          prefix-icon="Search"
          style="width: 300px"
          clearable
          @keyup.enter="loadProducts"
        />
        <el-button type="primary" @click="loadProducts">查询</el-button>
      </div>

      <el-table :data="products" stripe style="width: 100%">
        <el-table-column prop="id" label="商品 ID" width="100" />
        <el-table-column prop="name" label="商品名称" width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">
            ¥{{ row.price?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="120">
          <template #default="{ row }">
            <el-tag :type="row.stock > 0 ? 'success' : 'danger'">
              {{ row.stock || 0 }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" />
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utill/request'

const router = useRouter()
const route = useRoute()
const products = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchText = ref('')
const currentUser = ref(null)

const isAdmin = computed(() => {
  return currentUser.value && currentUser.value.role === 'admin'
})

// 加载用户信息
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

const loadProducts = async () => {
  try {
    const res = await request.get('/product/list', {
      params: {
        pageNum: currentPage.value,
        pageSize: pageSize.value
      }
    })

    if (res.code === 200) {
      if (res.data.list) {
        products.value = res.data.list
        total.value = res.data.total
      } else {
        products.value = Array.isArray(res.data) ? res.data : []
        total.value = products.value.length
      }
    }
  } catch (error) {
    console.error(error)
    products.value = []
    total.value = 0
  }
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('currentUser')
  ElMessage.success('已退出登录')
  router.push('/')
}

const navigateTo = (path) => {
  router.push(path)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadProducts()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadProducts()
}

// 每次进入页面都重新加载用户信息
onMounted(() => {
  loadCurrentUser()
  loadProducts()
})

// 监听路由参数变化（比如从其他页面返回）
watch(() => route.path, (newPath, oldPath) => {
  if (newPath !== oldPath) {
    loadCurrentUser()
  }
})
</script>

<style scoped>
.top-navigation {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 15px 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  margin-bottom: 20px;
}

.nav-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  color: white;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-right {
  display: flex;
  gap: 10px;
}

.products-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
  margin-top: 80px;
}

.products-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>

