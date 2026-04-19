<template>
  <div class="users-container">
    <el-card class="users-card">
      <template #header>
        <div class="card-header">
          <h2>用户管理</h2>
          <div style="display: flex; gap: 10px; align-items: center;">
            <span v-if="currentUser" style="color: #666; font-size: 14px;">
              欢迎您，{{ currentUser.name }}
            </span>
          </div>
        </div>
      </template>

      <div style="display: flex; gap: 10px; margin-bottom: 20px;">
        <el-input
          v-model="searchText"
          placeholder="搜索用户名称"
          prefix-icon="Search"
          style="width: 300px"
          clearable
          @keyup.enter="loadUsers"
        />
        <el-button type="primary" @click="loadUsers">查询</el-button>
      </div>

      <el-table :data="users" stripe style="width: 100%">
        <el-table-column prop="userId" label="用户 ID" width="180" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="sex" label="性别" width="80" />
        <el-table-column prop="money" label="余额" width="120">
          <template #default="{ row }">
            ¥{{ row.money?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 2 ? 'danger' : 'success'">
              {{ row.status === 2 ? '已拉黑' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 2"
              type="success"
              size="small"
              @click="handleUnblacklist(row.userId)"
            >
              解除拉黑
            </el-button>
            <el-button
              v-else
              type="danger"
              size="small"
              @click="handleBlacklist(row.userId)"
            >
              拉黑
            </el-button>
          </template>
        </el-table-column>
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utill/request'

const router = useRouter()
const users = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchText = ref('')
const currentUser = ref(null)

const loadUsers = async () => {
  try {
    let url = ''
    const params = {}

    if (searchText.value.trim()) {
      params.name = searchText.value.trim()
    }

    const role = currentUser.value?.role

    if (role === 2 || role === 'admin') {
      url = '/admin/users'
    } else if (role === 1 || role === 'staff') {
      url = '/staff/users'
    } else {
      ElMessage.error('权限不足')
      return
    }

    const res = await request.get(url, { params })

    if (res.code === 200) {
      if (Array.isArray(res.data)) {
        users.value = res.data
        total.value = res.data.length
      } else if (res.data.list) {
        users.value = res.data.list
        total.value = res.data.total
      } else {
        users.value = [res.data]
        total.value = 1
      }
    }
  } catch (error) {
    console.error(error)
    users.value = []
    total.value = 0
  }
}

const handleBlacklist = async (userId) => {
  try {
    await ElMessageBox.confirm('确定要拉黑该用户吗？拉黑后用户将无法进行除浏览商品外的任何操作。', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const role = currentUser.value?.role
    let url = ''

    if (role === 2 || role === 'admin') {
      url = `/admin/users/${userId}/blacklist`
    } else if (role === 1 || role === 'staff') {
      url = `/staff/users/${userId}/blacklist`
    }

    const res = await request.post(url)

    if (res.code === 200) {
      ElMessage.success('拉黑成功')
      loadUsers()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleUnblacklist = async (userId) => {
  try {
    await ElMessageBox.confirm('确定要解除拉黑该用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })

    const role = currentUser.value?.role
    let url = ''

    if (role === 2 || role === 'admin') {
      url = `/admin/users/${userId}/unblacklist`
    } else if (role === 1 || role === 'staff') {
      url = `/staff/users/${userId}/unblacklist`
    }

    const res = await request.post(url)

    if (res.code === 200) {
      ElMessage.success('解除拉黑成功')
      loadUsers()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadUsers()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadUsers()
}

onMounted(() => {
  currentUser.value = JSON.parse(localStorage.getItem('currentUser'))
  loadUsers()
})
</script>

<style scoped>
.users-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.users-card {
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

