NEW_FILE_CODE
<template>
  <div class="hot-products-container">
    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <h2>当月热销产品TOP {{ topN }}</h2>
          <div style="display: flex; gap: 10px; align-items: center;">
            <el-select v-model="topN" @change="loadHotProducts" style="width: 120px;">
              <el-option label="TOP 5" :value="5" />
              <el-option label="TOP 10" :value="10" />
              <el-option label="TOP 15" :value="15" />
              <el-option label="TOP 20" :value="20" />
            </el-select>
            <el-button type="primary" @click="goBack">
              返回仪表盘
            </el-button>
          </div>
        </div>
      </template>

      <div v-loading="loading" style="min-height: 400px;">
        <div v-if="!loading && chartData.length === 0" class="empty-tip">
          <el-empty description="本月暂无销售数据" />
        </div>
        <div v-else ref="chartRef" class="chart-container"></div>
      </div>

      <div class="data-table" v-if="!loading && chartData.length > 0">
        <h3>详细数据</h3>
        <el-table :data="chartData" border stripe>
          <el-table-column type="index" label="排名" width="80" align="center" />
          <el-table-column prop="productName" label="产品名称" min-width="200" />
          <el-table-column prop="totalQuantity" label="销售数量" width="120" align="center">
            <template #default="{ row }">
              <el-tag type="success" size="large">{{ row.totalQuantity }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import request from '@/utill/request'

const router = useRouter()
const loading = ref(false)
const chartRef = ref(null)
const chartInstance = ref(null)
const topN = ref(10)
const chartData = ref([])

const loadHotProducts = async () => {
  loading.value = true
  try {
    const res = await request.get('/order/top-selling', {
      params: { limit: topN.value }
    })

    if (res.code === 200) {
      chartData.value = res.data || []
      await nextTick()
      initChart()
    } else {
      ElMessage.error(res.message || '加载数据失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('加载热销产品数据失败')
  } finally {
    loading.value = false
  }
}

const initChart = () => {
  if (!chartRef.value) return

  if (chartInstance.value) {
    chartInstance.value.dispose()
  }

  chartInstance.value = echarts.init(chartRef.value)

  const productNames = chartData.value.map(item => item.productName)
  const quantities = chartData.value.map(item => item.totalQuantity)

  const option = {
    title: {
      text: `当月热销产品 TOP ${topN.value}`,
      left: 'center',
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: function(params) {
        const data = params[0]
        return `${data.name}<br/>销售数量: ${data.value} 件`
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: productNames,
      axisLabel: {
        interval: 0,
        rotate: 30,
        fontSize: 12
      },
      axisTick: {
        alignWithLabel: true
      }
    },
    yAxis: {
      type: 'value',
      name: '销售数量（件）',
      axisLabel: {
        fontSize: 12
      }
    },
    series: [
      {
        name: '销售数量',
        type: 'bar',
        data: quantities,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#2378f7' },
              { offset: 0.7, color: '#2378f7' },
              { offset: 1, color: '#83bff6' }
            ])
          }
        },
        barWidth: '60%',
        label: {
          show: true,
          position: 'top',
          fontSize: 12
        }
      }
    ]
  }

  chartInstance.value.setOption(option)

  window.addEventListener('resize', handleResize)
}

const handleResize = () => {
  if (chartInstance.value) {
    chartInstance.value.resize()
  }
}

const goBack = () => {
  router.push('/admin/dashboard')
}

onMounted(() => {
  loadHotProducts()
})

onUnmounted(() => {
  if (chartInstance.value) {
    chartInstance.value.dispose()
    chartInstance.value = null
  }
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.hot-products-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  width: 100%;
  height: 500px;
}

.empty-tip {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.data-table {
  margin-top: 30px;
}

.data-table h3 {
  margin-bottom: 15px;
  color: #333;
}
</style>
