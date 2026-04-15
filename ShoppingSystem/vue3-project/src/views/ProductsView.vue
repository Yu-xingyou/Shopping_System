<template>
  <div class="products-container">
    <el-card class="products-card">
      <template #header>
        <div class="card-header">
          <h2>商品列表</h2>
          <div style="display: flex; gap: 10px; align-items: center;">
            <el-badge v-if="isUser" :value="unreadCount" :hidden="unreadCount === 0" :max="99" type="danger">
              <el-button circle @click="showNotificationDialog">
                <el-icon><Bell /></el-icon>
              </el-button>
            </el-badge>
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
            <el-button
              v-if="isUser"
              type="warning"
              size="small"
              @click="goToFavorites"
            >
              我的收藏
            </el-button>
          </div>
        </div>
      </template>

      <div style="display: flex; gap: 10px; margin-bottom: 20px;">
        <el-input
          v-model="searchText"
          placeholder="搜索商品（名称、种类）"
          prefix-icon="Search"
          style="width: 350px"
          clearable
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
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
        <el-table-column prop="stock" label="库存" width="180">
          <template #default="{ row }">
            <div v-if="isAdmin && editingProductId === row.id">
              <el-input-number
                v-model="editingStock"
                :min="0"
                :max="9999"
                size="small"
                controls-position="right"
                style="width: 100px"
              />
              <el-button
                type="primary"
                size="small"
                @click="confirmUpdateStock(row)"
                :loading="updatingStock"
                style="margin-left: 5px"
              >
                确定
              </el-button>
              <el-button
                size="small"
                @click="cancelEditStock"
                style="margin-left: 5px"
              >
                取消
              </el-button>
            </div>
            <div v-else>
              <el-tag :type="row.stock > 0 ? 'success' : 'danger'">
                {{ row.stock || 0 }}
              </el-tag>
              <el-button
                v-if="isAdmin"
                type="primary"
                link
                size="small"
                @click="startEditStock(row)"
                style="margin-left: 8px"
              >
                修改
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column label="收藏" width="100" v-if="isUser || isGuest">
          <template #default="{ row }">
            <el-button
              :type="favoritedProducts.has(row.id) ? 'warning' : 'info'"
              size="small"
              :icon="favoritedProducts.has(row.id) ? 'Star' : 'StarFilled'"
              @click="toggleFavorite(row)"
              :loading="favoritingProducts.has(row.id)"
            >
              {{ favoritedProducts.has(row.id) ? '已收藏' : '收藏' }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="预购数量" width="180" v-if="isUser">
          <template #default="{ row }">
            <el-input-number
              v-model="cart[row.id]"
              :min="0"
              :max="999"
              size="small"
              controls-position="right"
              style="width: 120px"
              @change="handleQuantityChange(row)"
            />
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

    <el-dialog
      v-model="notificationDialogVisible"
      title="消息通知"
      width="600px"
      :close-on-click-modal="false"
    >
      <div v-if="notifications.length === 0" style="text-align: center; padding: 40px; color: #999;">
        <el-empty description="暂无消息" />
      </div>
      <div v-else>
        <div style="margin-bottom: 15px; text-align: right;">
          <el-button type="primary" size="small" @click="handleMarkAllAsRead">
            全部标记已读
          </el-button>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="item in notifications"
            :key="item.id"
            :timestamp="item.createTime"
            :type="item.isRead === 0 ? 'warning' : 'info'"
            :color="item.isRead === 0 ? '#e6a23c' : '#909399'"
            :hollow="item.isRead === 1"
          >
            <div class="notification-item" :class="{ 'unread': item.isRead === 0 }">
              <div class="notification-content">
                <el-icon v-if="item.isRead === 0" style="color: #e6a23c; margin-right: 5px;"><Bell /></el-icon>
                <span>{{ item.content }}</span>
              </div>
              <el-button
                v-if="item.isRead === 0"
                type="primary"
                link
                size="small"
                @click="handleMarkAsRead(item)"
                style="margin-left: 10px;"
              >
                标记已读
              </el-button>
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
      <template #footer>
        <el-button @click="notificationDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Bell, InfoFilled } from '@element-plus/icons-vue'
import request from '@/utill/request'

const router = useRouter()
const route = useRoute()
const products = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchText = ref('')
const currentUser = ref(null)
const cart = reactive({})
const cartDialogVisible = ref(false)
const orderDialogVisible = ref(false)
const submitting = ref(false)
const editingProductId = ref(null)
const editingStock = ref(0)
const updatingStock = ref(false)
const favoritedProducts = ref(new Set())
const favoritingProducts = ref(new Set())
const notificationDialogVisible = ref(false)
const notifications = ref([])
const unreadCount = ref(0)
const orderForm = ref({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  remark: ''
})

const isAdmin = computed(() => {
  return currentUser.value && currentUser.value.role === 'admin'
})

const isUser = computed(() => {
  return currentUser.value && currentUser.value.role === 'user'
})

const isGuest = computed(() => {
  return currentUser.value && currentUser.value.role === 'guest'
})

const cartItems = computed(() => {
  return products.value
    .filter(p => cart[p.id] && cart[p.id] > 0)
    .map(p => ({
      id: p.id,
      name: p.name,
      price: p.price,
      quantity: cart[p.id]
    }))
})

const cartTotalQuantity = computed(() => {
  return Object.values(cart).reduce((sum, qty) => sum + (qty || 0), 0)
})

const cartTotalAmount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + (item.price * item.quantity), 0)
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

const handleSearch = async () => {
  try {
    const searchParams = {}
    if (searchText.value.trim()) {
      searchParams.name = searchText.value.trim()
      searchParams.category = searchText.value.trim()
    }

    const res = await request.post('/product/search', searchParams, {
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
        products.value = []
        total.value = 0
      }
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('搜索失败')
    products.value = []
    total.value = 0
  }
}

const handleReset = () => {
  searchText.value = ''
  currentPage.value = 1
  loadProducts()
}

const handleQuantityChange = (product) => {
  if (!cart[product.id]) {
    cart[product.id] = 0
  }
  if (cart[product.id] < 0) {
    cart[product.id] = 0
  }

  saveCartToStorage()

  if (cart[product.id] > 0) {
    ElMessage.success({
      message: `已将"${product.name}"加入购物车，共 ${cart[product.id]} 件`,
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
  if (!isUser.value) {
    ElMessage.warning('只有用户角色可以创建预购单')
    return
  }

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
      Object.keys(cart).forEach(key => {
        delete cart[key]
      })
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

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('currentUser')
  localStorage.removeItem('shoppingCart')
  ElMessage.success('已退出登录')
  router.push('/')
}

const navigateTo = (path) => {
  router.push(path)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  if (searchText.value.trim()) {
    handleSearch()
  } else {
    loadProducts()
  }
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  if (searchText.value.trim()) {
    handleSearch()
  } else {
    loadProducts()
  }
}

const startEditStock = (row) => {
  editingProductId.value = row.id
  editingStock.value = row.stock
}

const cancelEditStock = () => {
  editingProductId.value = null
  editingStock.value = 0
}

const confirmUpdateStock = async (row) => {
  if (editingStock.value < 0) {
    ElMessage.error('库存数量不能为负数')
    return
  }

  try {
    updatingStock.value = true
    const res = await request.put(`/admin/products/${row.id}/stock`, {
      stock: editingStock.value
    })

    if (res.code === 200) {
      ElMessage.success('库存更新成功')
      editingProductId.value = null
      loadProducts()
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('更新库存失败：' + (error.response?.data?.message || error.message))
  } finally {
    updatingStock.value = false
  }
}

const goToFavorites = () => {
  router.push('/favorites')
}

const loadFavoriteStatus = async () => {
  if (!currentUser.value || currentUser.value.role !== 'user') {
    favoritedProducts.value = new Set()
    return
  }

  if (!currentUser.value.userId) {
    console.warn('用户ID不存在，无法加载收藏状态')
    favoritedProducts.value = new Set()
    return
  }

  try {
    const res = await request.get('/favorite/list', {
      params: {
        userId: currentUser.value.userId
      }
    })

    if (res.code === 200 && Array.isArray(res.data)) {
      const favoriteSet = new Set()
      res.data.forEach(fav => {
        favoriteSet.add(fav.productId)
      })
      favoritedProducts.value = favoriteSet
    } else {
      favoritedProducts.value = new Set()
    }
  } catch (error) {
    console.error('加载收藏状态失败:', error)
    favoritedProducts.value = new Set()
  }
}

const toggleFavorite = async (product) => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  if (currentUser.value.role === 'guest') {
    ElMessage.warning('游客无法使用收藏功能，请先登录')
    router.push('/login')
    return
  }

  if (currentUser.value.role !== 'user') {
    ElMessage.warning('只有用户角色可以使用收藏功能')
    return
  }

  if (!currentUser.value.userId) {
    ElMessage.error('用户信息异常，请重新登录')
    router.push('/login')
    return
  }

  if (favoritingProducts.value.has(product.id)) {
    return
  }

  favoritingProducts.value.add(product.id)

  try {
    const isFavorited = favoritedProducts.value.has(product.id)

    if (isFavorited) {
      const res = await request.delete('/favorite/remove', {
        params: {
          userId: currentUser.value.userId,
          productId: product.id
        }
      })

      if (res.code === 200) {
        favoritedProducts.value.delete(product.id)
        ElMessage.success('已取消收藏')
      }
    } else {
      const res = await request.post('/favorite/add', null, {
        params: {
          userId: currentUser.value.userId,
          productId: product.id
        }
      })

      if (res.code === 200) {
        favoritedProducts.value.add(product.id)
        ElMessage.success('收藏成功')
      }
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('操作失败：' + (error.response?.data?.message || error.message))
  } finally {
    favoritingProducts.value.delete(product.id)
  }
}

const loadUnreadCount = async () => {
  if (!currentUser.value || currentUser.value.role !== 'user') {
    unreadCount.value = 0
    return
  }

  if (!currentUser.value.userId) {
    unreadCount.value = 0
    return
  }

  try {
    const res = await request.get('/notification/unread-count', {
      params: {
        userId: currentUser.value.userId
      }
    })

    if (res.code === 200) {
      unreadCount.value = res.data.count || 0
    }
  } catch (error) {
    console.error('加载未读数量失败:', error)
  }
}

const showNotificationDialog = async () => {
  notificationDialogVisible.value = true
  await loadNotifications()
}

const loadNotifications = async () => {
  if (!currentUser.value || currentUser.value.role !== 'user') {
    return
  }

  if (!currentUser.value.userId) {
    return
  }

  try {
    const res = await request.get('/notification/all', {
      params: {
        userId: currentUser.value.userId
      }
    })

    if (res.code === 200) {
      notifications.value = Array.isArray(res.data) ? res.data : []
    }
  } catch (error) {
    console.error('加载通知失败:', error)
  }
}

const handleMarkAsRead = async (item) => {
  if (!currentUser.value.userId) {
    ElMessage.error('用户信息异常，请重新登录')
    return
  }

  try {
    const res = await request.post(`/notification/${item.id}/read`, null, {
      params: {
        userId: currentUser.value.userId
      }
    })

    if (res.code === 200) {
      item.isRead = 1
      unreadCount.value = Math.max(0, unreadCount.value - 1)
      ElMessage.success('已标记为已读')
    }
  } catch (error) {
    console.error('标记已读失败:', error)
    ElMessage.error('操作失败')
  }
}

const handleMarkAllAsRead = async () => {
  if (!currentUser.value.userId) {
    ElMessage.error('用户信息异常，请重新登录')
    return
  }

  try {
    const res = await request.post('/notification/read-all', null, {
      params: {
        userId: currentUser.value.userId
      }
    })

    if (res.code === 200) {
      notifications.value.forEach(item => {
        item.isRead = 1
      })
      unreadCount.value = 0
      ElMessage.success('已全部标记为已读')
    }
  } catch (error) {
    console.error('标记全部已读失败:', error)
    ElMessage.error('操作失败')
  }
}

watch(cart, () => {
  saveCartToStorage()
}, { deep: true })

onMounted(() => {
  loadCurrentUser()
  loadCartFromStorage()
  loadProducts()
  loadFavoriteStatus()
  loadUnreadCount()
})

watch(() => route.path, (newPath, oldPath) => {
  if (newPath !== oldPath) {
    loadCurrentUser()
    loadCartFromStorage()
    loadFavoriteStatus()
    loadUnreadCount()
  }
})

watch(() => products.value, (newProducts) => {
  if (newProducts.length > 0 && currentUser.value && currentUser.value.role === 'user') {
    loadFavoriteStatus()
  }
}, { deep: true })
</script>

<style scoped>
.products-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
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

.notification-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 5px 0;
}

.notification-item.unread {
  font-weight: bold;
  color: #e6a23c;
}

.notification-content {
  display: flex;
  align-items: center;
  flex: 1;
}
</style>

