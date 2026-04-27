NEW_FILE_CODE
<template>
  <div class="revenue-container">
    <el-card class="revenue-card">
      <template #header>
        <div class="card-header">
          <h2>我的收益</h2>
          <el-button type="primary" @click="goBack">
            返回仪表盘
          </el-button>
        </div>
      </template>

      <div class="filter-section">
        <el-radio-group v-model="selectedDays" @change="handleDaysChange" size="large">
          <el-radio-button :value="1">1天内</el-radio-button>
          <el-radio-button :value="7">7天内</el-radio-button>
          <el-radio-button :value="30">一个月内</el-radio-button>
          <el-radio-button :value="null">全部</el-radio-button>
        </el-radio-group>
      </div>

      <div class="revenue-display" v-loading="loading">
        <div class="revenue-card-inner">
          <div class="revenue-label">总收益</div>
          <div class="revenue-amount">
            <span class="currency">¥</span>
            <span class="amount">{{ revenue.toFixed(2) }}</span>
          </div>
          <div class="revenue-tip">
            <el-icon><Info-Filled /></el-icon>
            <span>仅统计已完成（已收货）订单的金额</span>
          </div>
        </div>
      </div>

      <div class="revenue-details">
        <h3>收益说明</h3>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="统计范围">
            <el-tag :type="getDaysTagType(selectedDays)">
              {{ getDaysText(selectedDays) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="统计条件">订单状态为"已完成"（已收货）</el-descriptions-item>
          <el-descriptions-item label="当前收益">
            <span style="color: #f56c6c; font-weight: bold; font-size: 18px;">
              ¥{{ revenue.toFixed(2) }}
            </span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { InfoFilled } from '@element-plus/icons-vue'
import request from '@/utill/request'

const router = useRouter()
const revenue = ref(0)
const loading = ref(false)
const selectedDays = ref(null)

const getDaysText = (days) => {
  if (days === null) return '全部时间'
  if (days === 1) return '1天内'
  if (days === 7) return '7天内'
  if (days === 30) return '一个月内'
  return '全部时间'
}

const getDaysTagType = (days) => {
  if (days === null) return 'info'
  if (days === 1) return 'danger'
  if (days === 7) return 'warning'
  if (days === 30) return 'success'
  return 'info'
}

const loadRevenue = async (days) => {
  loading.value = true
  try {
    const params = {}
    if (days !== null) {
      params.days = days
    }

    const res = await request.get('/admin/revenue', { params })

    if (res.code === 200) {
      revenue.value = res.data.revenue || 0
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('加载收益数据失败')
    revenue.value = 0
  } finally {
    loading.value = false
  }
}

const handleDaysChange = (days) => {
  loadRevenue(days)
}

const goBack = () => {
  router.push('/admin/dashboard')
}

onMounted(() => {
  loadRevenue(null)
})
</script>

<style scoped>
.revenue-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.revenue-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-section {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.revenue-display {
  display: flex;
  justify-content: center;
  margin-bottom: 40px;
}

.revenue-card-inner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 60px;
  border-radius: 16px;
  text-align: center;
  color: white;
  min-width: 400px;
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.3);
}

.revenue-label {
  font-size: 18px;
  opacity: 0.9;
  margin-bottom: 15px;
}

.revenue-amount {
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 5px;
}

.currency {
  font-size: 32px;
  font-weight: bold;
}

.amount {
  font-size: 56px;
  font-weight: bold;
  letter-spacing: 2px;
}

.revenue-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  margin-top: 15px;
  font-size: 14px;
  opacity: 0.85;
}

.revenue-details {
  margin-top: 30px;
}

.revenue-details h3 {
  margin-bottom: 15px;
  color: #333;
}
</style>
