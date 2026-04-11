<template>
  <div class="favorites-container">
    <el-card class="favorites-card">
      <template #header>
        <div class="card-header">
          <h2>我的收藏</h2>
          <div style="display: flex; gap: 10px; align-items: center;">
            <el-badge :value="cartTotalQuantity" :hidden="cartTotalQuantity === 0" type="primary">
              <el-button type="primary" size="small" @click="showCartDialog">
                购物车
              </el-button>
            </el-badge>
            <el-button
              type="success"
              size="small"
              @click="submitOrder"
              :disabled="cartTotalQuantity === 0"
            >
              提交订单
            </el-button>
            <el-button type="primary" @click="goToProducts">
              继续购物
            </el-button>
          </div>
        </div>
      </template>

      <div v-if="loading" class="loading-wrapper">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>加载中...</span>
      </div>

      <el-empty
        v-else-if="favorites.length === 0"
        description="还没有收藏任何商品"
      >
        <el-button type="primary" @click="goToProducts">
          去逛逛
        </el-button>
      </el-empty>

      <el-table
        v-else
        :data="favorites"
        stripe
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="productId" label="商品 ID" width="100" />
        <el-table-column label="商品名称" min-width="150">
          <template #default="{ row }">
            {{ getProductInfo(row.productId)?.name || '加载中...' }}
          </template>
        </el-table-column>
        <el-table-column label="描述" min-width="200">
          <template #default="{ row }">
            {{ getProductInfo(row.productId)?.description || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="价格" width="120">
          <template #default="{ row }">
            <span style="color: #f56c6c; font-weight: bold;">
              ¥{{ getProductInfo(row.productId)?.price?.toFixed(2) || '0.00' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="库存" width="120">
          <template #default="{ row }">
            <el-tag :type="(getProductInfo(row.productId)?.stock || 0) > 0 ? 'success' : 'danger'">
              {{ getProductInfo(row.productId)?.stock || 0 }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="分类" width="120">
          <template #default="{ row }">
            {{ getProductInfo(row.productId)?.category || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="购买数量" width="180">
          <template #default="{ row }">
            <el-input-number
              v-model="cart[row.productId]"
              :min="0"
              :max="999"
              size="small"
              controls-position="right"
              style="width: 120px"
              @change="handleQuantityChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="收藏时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              type="danger"
              size="small"
              @click="removeFavorite(row)"
            >
              取消收藏
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="cartDialogVisible"
      title="购物车"
      width="700px"
      :close-on-click-modal="false"
    >
      <div v-if="cartItems.length === 0" style="text-align: center; padding: 40px; color: #999;">
        <el-empty description="购物车是空的" />
      </div>
      <el-table
        v-else
        :data="cartItems"
        stripe
        style="width: 100%"
        max-height="400"
      >
        <el-table-column prop="name" label="商品名称" min-width="200" />
        <el-table-column prop="price" label="单价" width="120" align="right">
          <template #default="{ row }">
            <span style="color: #f56c6c; font-weight: bold;">¥{{ row.price?.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="120" align="center" />
        <el-table-column label="小计" width="140" align="right">
          <template #default="{ row }">
            <span style="color: #f56c6c; font-weight: bold; font-size: 15px;">
              ¥{{ (row.price * row.quantity).toFixed(2) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template #default="{ row }">
            <el-button
              type="danger"
              size="small"
              @click="removeFromCart(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="cartItems.length > 0" class="cart-footer">
        <div class="cart-total">
          <span>总计：</span>
          <span class="total-amount">¥{{ cartTotalAmount.toFixed(2) }}</span>
        </div>
        <div style="margin-top: 15px; text-align: right;">
          <el-button @click="cartDialogVisible = false">继续购物</el-button>
          <el-button type="primary" @click="submitOrderFromCart">去结算</el-button>
        </div>
      </div>
    </el-dialog>

    <el-dialog
      v-model="orderDialogVisible"
      title="确认订单信息"
      width="550px"
      :close-on-click-modal="false"
    >
      <el-form :model="orderForm" label-width="100px" size="default">
        <el-form-item label="收货人" required>
          <el-input
            v-model="orderForm.receiverName"
            placeholder="请输入收货人姓名"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="联系电话" required>
          <el-input
            v-model="orderForm.receiverPhone"
            placeholder="请输入联系电话"
            maxlength="20"
          />
        </el-form-item>
        <el-form-item label="收货地址" required>
          <el-input
            v-model="orderForm.receiverAddress"
            type="textarea"
            :rows="3"
            placeholder="请输入详细收货地址"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="orderForm.remark"
            type="textarea"
            :rows="2"
            placeholder="选填备注信息（如配送时间要求等）"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <div class="order-summary">
        <div class="summary-title">订单商品清单：</div>
        <ul class="order-items-list">
          <li v-for="item in cartItems" :key="item.id">
            <span>{{ item.name }}</span>
            <span class="item-quantity">x {{ item.quantity }}</span>
            <span class="item-subtotal">¥{{ (item.price * item.quantity).toFixed(2) }}</span>
          </li>
        </ul>
        <div class="order-total-amount">
          <span>订单总额：</span>
          <span class="amount">¥{{ cartTotalAmount.toFixed(2) }}</span>
        </div>
        <div class="order-tip">
          <el-icon><Info-Filled /></el-icon>
          <span>温馨提示：预购商品数量可以超过库存数量</span>
        </div>
      </div>

      <template #footer>
        <el-button @click="orderDialogVisible = false">返回修改</el-button>
        <el-button type="primary" @click="confirmOrder" :loading="submitting">
          {{ submitting ? '提交中...' : '确认提交订单' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Loading, InfoFilled } from '@element-plus/icons-vue'
import request from '@/utill/request'

const router = useRouter()
const favorites = ref([])
const products = ref({})
const loading = ref(false)
const currentUser = ref(null)
const cart = reactive({})
const cartDialogVisible = ref(false)
const orderDialogVisible = ref(false)
const submitting = ref(false)
const orderForm = ref({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  remark: ''
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

const loadCartFromStorage = () => {
  try {
    const savedCart = localStorage.getItem('shoppingCart')
    if (savedCart) {
      const cartData = JSON.parse(savedCart)
      Object.keys(cartData).forEach(key => {
        cart[key] = cartData[key]
      })
    }
  } catch (error) {
    console.error('加载购物车数据失败:', error)
  }
}

const saveCartToStorage = () => {
  try {
    localStorage.setItem('shoppingCart', JSON.stringify(cart))
  } catch (error) {
    console.error('保存购物车数据失败:', error)
  }
}

const loadFavorites = async () => {
  if (!currentUser.value || !currentUser.value.userId) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loading.value = true
  try {
    const res = await request.get('/favorite/list', {
      params: {
        userId: currentUser.value.userId
      }
    })

    if (res.code === 200) {
      favorites.value = Array.isArray(res.data) ? res.data : []
      await loadProductDetails()
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('加载收藏列表失败')
    favorites.value = []
  } finally {
    loading.value = false
  }
}

const loadProductDetails = async () => {
  if (favorites.value.length === 0) {
    return
  }

  try {
    const productIds = favorites.value.map(fav => fav.productId)
    const productMap = {}

    for (const productId of productIds) {
      try {
        const res = await request.get(`/product/${productId}`)
        if (res.code === 200 && res.data) {
          productMap[productId] = res.data
        }
      } catch (error) {
        console.error(`加载商品 ${productId} 失败:`, error)
      }
    }

    products.value = productMap
  } catch (error) {
    console.error('加载商品信息失败:', error)
  }
}

const getProductInfo = (productId) => {
  return products.value[productId] || null
}

const removeFavorite = async (row) => {
  ElMessageBox.confirm('确定要取消收藏该商品吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await request.delete('/favorite/remove', {
        params: {
          userId: currentUser.value.userId,
          productId: row.productId
        }
      })

      if (res.code === 200) {
        ElMessage.success('已取消收藏')
        loadFavorites()
      }
    } catch (error) {
      console.error(error)
      ElMessage.error('取消收藏失败')
    }
  }).catch(() => {
    ElMessage.info('已取消操作')
  })
}

const goToProducts = () => {
  router.push('/products')
}

const cartItems = computed(() => {
  return favorites.value
    .filter(p => cart[p.productId] && cart[p.productId] > 0)
    .map(p => {
      const productInfo = products.value[p.productId]
      return {
        id: p.productId,
        name: productInfo?.name || '未知商品',
        price: productInfo?.price || 0,
        quantity: cart[p.productId]
      }
    })
})

const cartTotalQuantity = computed(() => {
  return Object.values(cart).reduce((sum, qty) => sum + (qty || 0), 0)
})

const cartTotalAmount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + (item.price * item.quantity), 0)
})

const handleQuantityChange = (product) => {
  if (!cart[product.productId]) {
    cart[product.productId] = 0
  }
  if (cart[product.productId] < 0) {
    cart[product.productId] = 0
  }

  saveCartToStorage()

  const productInfo = products.value[product.productId]
  if (cart[product.productId] > 0 && productInfo) {
    ElMessage.success({
      message: `已将"${productInfo.name}"加入购物车，共 ${cart[product.productId]} 件`,
      duration: 1500
    })
  }
}

const showCartDialog = () => {
  if (cartTotalQuantity.value === 0) {
    ElMessage.info('购物车是空的，请先选择商品')
    return
  }
  cartDialogVisible.value = true
}

const removeFromCart = (product) => {
  cart[product.id] = 0
  saveCartToStorage()
  ElMessage.success('已删除')
}

const submitOrder = () => {
  if (cartTotalQuantity.value === 0) {
    ElMessage.warning('请选择至少一件商品')
    return
  }

  orderDialogVisible.value = true
}

const submitOrderFromCart = () => {
  cartDialogVisible.value = false
  submitOrder()
}

const confirmOrder = async () => {
  if (!orderForm.value.receiverName || !orderForm.value.receiverPhone || !orderForm.value.receiverAddress) {
    ElMessage.warning('请填写完整的收货信息')
    return
  }

  try {
    submitting.value = true

    const items = cartItems.value.map(item => ({
      productId: item.id,
      productName: item.name,
      productPrice: item.price,
      quantity: item.quantity
    }))

    const requestData = {
      userId: currentUser.value.userId,
      totalAmount: cartTotalAmount.value,
      receiverName: orderForm.value.receiverName,
      receiverPhone: orderForm.value.receiverPhone,
      receiverAddress: orderForm.value.receiverAddress,
      remark: orderForm.value.remark,
      items: items
    }

    const res = await request.post('/order/create', requestData)

    if (res.code === 200) {
      ElMessage.success({
        message: '预购单创建成功！即将跳转到订单页面',
        duration: 2000
      })
      cart.value = {}
      localStorage.removeItem('shoppingCart')
      orderForm.value = {
        receiverName: '',
        receiverPhone: '',
        receiverAddress: '',
        remark: ''
      }
      orderDialogVisible.value = false
      cartDialogVisible.value = false

      setTimeout(() => {
        router.push('/user-orders')
      }, 2000)
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('创建预购单失败：' + (error.response?.data?.message || error.message))
  } finally {
    submitting.value = false
  }
}

watch(cart, () => {
  saveCartToStorage()
}, { deep: true })

onMounted(() => {
  loadCurrentUser()
  loadCartFromStorage()
  loadFavorites()
})
</script>

<style scoped>
.favorites-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.favorites-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.loading-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  color: #909399;
  gap: 15px;
}

.loading-wrapper .el-icon {
  font-size: 40px;
}

.cart-footer {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
}

.cart-total {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-size: 16px;
  color: #606266;
}

.cart-total .total-amount {
  font-size: 22px;
  font-weight: bold;
  color: #f56c6c;
  margin-left: 10px;
}

.order-summary {
  margin-top: 15px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.summary-title {
  font-weight: bold;
  color: #606266;
  margin-bottom: 10px;
}

.order-items-list {
  list-style: none;
  padding: 0;
  margin: 0 0 10px 0;
  max-height: 150px;
  overflow-y: auto;
}

.order-items-list li {
  display: flex;
  justify-content: space-between;
  padding: 5px 0;
  border-bottom: 1px dashed #dcdfe6;
  font-size: 14px;
}

.order-items-list li:last-child {
  border-bottom: none;
}

.item-quantity {
  color: #909399;
  margin: 0 15px;
}

.item-subtotal {
  color: #f56c6c;
  font-weight: bold;
}

.order-total-amount {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-size: 16px;
  color: #606266;
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #dcdfe6;
}

.order-total-amount .amount {
  font-size: 20px;
  font-weight: bold;
  color: #f56c6c;
  margin-left: 10px;
}

.order-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  margin-top: 10px;
  padding: 8px;
  background-color: #fdf6ec;
  border-radius: 4px;
  color: #e6a23c;
  font-size: 13px;
}
</style>
