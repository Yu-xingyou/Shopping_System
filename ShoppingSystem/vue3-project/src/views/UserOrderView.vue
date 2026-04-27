<template>
  <div class="orders-container">
    <el-card class="orders-card">
      <template #header>
        <div class="card-header">
          <h2>我的订单</h2>
        </div>
      </template>

      <el-table :data="orders" stripe style="width: 100%">
        <el-table-column prop="id" label="订单 ID" width="100" />
        <el-table-column prop="orderNum" label="订单号" width="200" />
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
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="viewOrderDetail(row)"
            >
              查看详情
            </el-button>
            <el-button
              v-if="row.status === 0"
              type="danger"
              size="small"
              @click="cancelOrder(row)"
            >
              取消订单
            </el-button>
            <el-button
              v-if="row.status === 2"
              type="success"
              size="small"
              @click="showConfirmDialog(row)"
            >
              确认收货
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="detailDialogVisible"
      title="订单详情"
      width="700px"
      :close-on-click-modal="false"
    >
      <div v-if="currentOrder" class="order-detail">
        <el-descriptions title="订单基本信息" :column="2" border>
          <el-descriptions-item label="订单 ID">{{ currentOrder.id }}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{ currentOrder.orderNum }}</el-descriptions-item>
          <el-descriptions-item label="订单金额">
            <span style="color: #f56c6c; font-weight: bold; font-size: 16px;">
              ¥{{ currentOrder.totalAmount?.toFixed(2) }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(currentOrder.status)">
              {{ getStatusText(currentOrder.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="收货人">{{ currentOrder.receiverName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentOrder.receiverPhone }}</el-descriptions-item>
          <el-descriptions-item label="收货地址" :span="2">{{ currentOrder.receiverAddress }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentOrder.createTime }}</el-descriptions-item>
          <el-descriptions-item label="完成时间">{{ currentOrder.finishTime || '未完成' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ currentOrder.remark || '无' }}</el-descriptions-item>
        </el-descriptions>

        <div v-if="orderItems.length > 0" class="order-items-section">
          <h3 style="margin: 20px 0 10px 0; color: #606266;">订单商品</h3>
          <el-table :data="orderItems" stripe size="small">
            <el-table-column prop="productName" label="商品名称" min-width="200" />
            <el-table-column prop="productPrice" label="单价" width="120" align="right">
              <template #default="{ row }">
                <span style="color: #f56c6c;">¥{{ row.productPrice?.toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" width="100" align="center" />
            <el-table-column label="小计" width="120" align="right">
              <template #default="{ row }">
                <span style="color: #f56c6c; font-weight: bold;">
                  ¥{{ (row.productPrice * row.quantity).toFixed(2) }}
                </span>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div v-else style="text-align: center; padding: 20px; color: #999;">
          暂无商品信息
        </div>
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="confirmDialogVisible"
      title="确认收货"
      width="500px"
      :close-on-click-modal="false"
    >
      <div v-if="currentOrderForConfirm" class="confirm-receipt-container">
        <el-alert
          title="请上传购物小票照片"
          type="info"
          :closable="false"
          style="margin-bottom: 20px;"
        >
          <template #default>
            <p>上传小票照片后，订单将标记为已完成，并将从您的账户扣除 ¥{{ currentOrderForConfirm.totalAmount?.toFixed(2) }}</p>
          </template>
        </el-alert>

        <el-upload
          ref="uploadRef"
          class="upload-demo"
          drag
          action="#"
          :auto-upload="false"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          :limit="1"
          accept="image/*"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            拖拽文件到此处或 <em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              只能上传 jpg/png 文件，且不超过 5MB
            </div>
          </template>
        </el-upload>

        <div v-if="previewUrl" class="image-preview">
          <p style="margin-top: 15px; color: #606266;">图片预览：</p>
          <img :src="previewUrl" alt="小票预览" style="max-width: 100%; max-height: 300px; margin-top: 10px; border-radius: 4px;" />
        </div>
      </div>
      <template #footer>
        <el-button @click="confirmDialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="submitConfirmReceipt"
          :loading="uploading"
          :disabled="!selectedFile"
        >
          确认提交
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import request from '@/utill/request'

const orders = ref([])
const detailDialogVisible = ref(false)
const currentOrder = ref(null)
const orderItems = ref([])

const confirmDialogVisible = ref(false)
const currentOrderForConfirm = ref(null)
const selectedFile = ref(null)
const previewUrl = ref(null)
const uploading = ref(false)
const uploadRef = ref(null)

const loadOrders = async () => {
  try {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'))
    const res = await request.get('/user/orders', {
      params: {
        userId: currentUser.userId
      }
    })

    if (res.code === 200) {
      orders.value = Array.isArray(res.data) ? res.data : []
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('加载订单失败')
    orders.value = []
  }
}

const viewOrderDetail = async (row) => {
  currentOrder.value = row
  orderItems.value = []
  detailDialogVisible.value = true

  try {
    const res = await request.get(`/order/${row.id}/items`)
    if (res.code === 200) {
      orderItems.value = Array.isArray(res.data) ? res.data : []
    }
  } catch (error) {
    console.error('加载订单商品失败:', error)
  }
}

const cancelOrder = async (row) => {
  let message = '确定要取消该订单吗？取消后无法恢复。'
  if (row.status === 2) {
    const penalty = row.totalAmount * 0.05
    message = `该订单已发货，取消将扣除 ${penalty.toFixed(2)} 元作为违约金（订单金额的 5%）。确定要取消吗？`
  }

  ElMessageBox.confirm(message, '提示', {
    confirmButtonText: '确定取消',
    cancelButtonText: '我再想想',
    type: 'warning'
  }).then(async () => {
    try {
      const currentUser = JSON.parse(localStorage.getItem('currentUser'))
      const res = await request.post(`/order/${row.id}/cancel`, null, {
        params: {
          userId: currentUser.userId
        }
      })

      if (res.code === 200) {
        ElMessage.success('订单已取消')
        loadOrders()
      }
    } catch (error) {
      console.error(error)
      ElMessage.error(error.response?.data?.message || '取消订单失败')
    }
  }).catch(() => {
    ElMessage.info('已取消操作')
  })
}

const showConfirmDialog = (row) => {
  currentOrderForConfirm.value = row
  selectedFile.value = null
  previewUrl.value = null
  confirmDialogVisible.value = true
}

const handleFileChange = (file) => {
  selectedFile.value = file.raw
  previewUrl.value = URL.createObjectURL(file.raw)
}

const handleFileRemove = () => {
  selectedFile.value = null
  previewUrl.value = null
}

const submitConfirmReceipt = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先上传小票照片')
    return
  }

  uploading.value = true
  try {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'))
    const formData = new FormData()
    formData.append('receiptImage', selectedFile.value)
    formData.append('userId', currentUser.userId)

    const res = await request.post(`/order/${currentOrderForConfirm.value.id}/confirm`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (res.code === 200) {
      ElMessage.success('确认收货成功，订单已完成')
      confirmDialogVisible.value = false
      selectedFile.value = null
      previewUrl.value = null
      loadOrders()
    }
  } catch (error) {
    console.error(error)
    ElMessage.error(error.response?.data?.message || '确认收货失败')
  } finally {
    uploading.value = false
  }
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
    1: '缺货等待',
    2: '已发货',
    3: '已完成',
    4: '已取消'
  }
  return statusMap[status] || '未知'
}

onMounted(() => {
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

.order-detail {
  padding: 10px;
}

.order-items-section {
  margin-top: 20px;
}

.confirm-receipt-container {
  padding: 10px;
}

.upload-demo {
  width: 100%;
}

.image-preview {
  margin-top: 15px;
}
</style>
