<template>
  <div class="orders-container">
    <el-card class="orders-card">
      <template #header>
        <div class="card-header">
          <h2>订单管理</h2>
          <div style="display: flex; gap: 10px; align-items: center;">
            <span v-if="currentUser" style="color: #666; font-size: 14px;">
              欢迎您，{{ currentUser.name || currentUser.userId }}
            </span>
            <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </template>

      <el-table :data="orders" stripe style="width: 100%">
        <el-table-column prop="id" label="订单 ID" width="100" />
        <el-table-column prop="orderNum" label="订单号" width="200" />
        <el-table-column prop="userId" label="用户 ID" width="150" />
        <el-table-column prop="totalAmount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.totalAmount?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="300" fixed="right" v-if="isStaffOrAdmin">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="showStatusDialog(row)"
            >
              修改状态
            </el-button>
            <el-button
              v-if="row.status === 0"
              type="danger"
              size="small"
              @click="cancelOrder(row)"
            >
              取消订单
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="statusDialogVisible"
      title="修改订单状态"
      width="400px"
    >
      <el-form :model="statusForm" label-width="100px">
        <el-form-item label="订单状态">
          <el-select v-model="statusForm.status" placeholder="请选择订单状态" style="width: 100%">
            <el-option :label="'待付款'" :value="0" />
            <el-option :label="'已付款'" :value="1" />
            <el-option :label="'已发货'" :value="2" />
            <el-option :label="'已完成'" :value="3" />
            <el-option :label="'已取消'" :value="4" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateOrderStatus" :loading="updating">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utill/request'

const router = useRouter()
const orders = ref([])
const currentUser = ref(null)
const statusDialogVisible = ref(false)
const updating = ref(false)
const statusForm = ref({
  orderId: null,
  status: null
})

const isStaffOrAdmin = computed(() => {
  return currentUser.value && (currentUser.value.role === 'staff' || currentUser.value.role === 'admin')
})

const loadOrders = async () => {
  try {
    if (!currentUser.value) {
      ElMessage.error('请先登录')
      router.push('/login')
      return
    }

    const role = currentUser.value.role

    if (role === 'admin' || role === 'staff') {
      const res = await request.get('/admin/orders')
      if (res.code === 200) {
        orders.value = Array.isArray(res.data) ? res.data : []
      }
    } else {
      const res = await request.get('/user/orders', {
        params: { userId: currentUser.value.userId }
      })
      if (res.code === 200) {
        orders.value = Array.isArray(res.data) ? res.data : []
      }
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('加载订单失败')
    orders.value = []
  }
}

const showStatusDialog = (row) => {
  statusForm.value.orderId = row.id
  statusForm.value.status = row.status
  statusDialogVisible.value = true
}

const updateOrderStatus = async () => {
  if (statusForm.value.status === null) {
    ElMessage.error('请选择订单状态')
    return
  }

  try {
    updating.value = true
    const res = await request.put(`/order/${statusForm.value.orderId}/status`, null, {
      params: {
        status: statusForm.value.status,
        staffId: currentUser.value.staffId || currentUser.value.adminId
      }
    })

    if (res.code === 200) {
      ElMessage.success('订单状态更新成功')
      statusDialogVisible.value = false
      loadOrders()
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('更新订单状态失败')
  } finally {
    updating.value = false
  }
}

const cancelOrder = (row) => {
  ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const cancelUserId = row.userId || currentUser.value.userId

      const res = await request.post(`/order/${row.id}/cancel`, null, {
        params: { userId: cancelUserId }
      })

      if (res.code === 200) {
        ElMessage.success('订单已取消')
        loadOrders()
      }
    } catch (error) {
      console.error(error)
      ElMessage.error('取消订单失败')
    }
  })
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('currentUser')
  ElMessage.success('已退出登录')
  router.push('/')
}

const getStatusType = (status) => {
  const statusMap = {
    0: 'info',
    1: 'warning',
    2: 'primary',
    3: 'success',
    4: 'danger'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    0: '待付款',
    1: '已付款',
    2: '已发货',
    3: '已完成',
    4: '已取消'
  }
  return statusMap[status] || '未知'
}

onMounted(() => {
  currentUser.value = JSON.parse(localStorage.getItem('currentUser'))
  loadOrders()
})
</script>

<style scoped>
.orders-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.orders-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

