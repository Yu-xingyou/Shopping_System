<template>
  <div class="staffs-container">
    <el-card class="staffs-card">
      <template #header>
        <div class="card-header">
          <h2>员工列表</h2>
          <div style="display: flex; gap: 10px; align-items: center;">
            <span v-if="currentUser" style="color: #666; font-size: 14px;">
              欢迎您，{{ currentUser.name }}
            </span>
            <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </template>

      <div style="display: flex; gap: 10px; margin-bottom: 20px;" v-if="isAdmin">
        <el-input
          v-model="searchText"
          placeholder="搜索员工名称"
          prefix-icon="Search"
          style="width: 300px"
          clearable
          @keyup.enter="loadStaffs"
        />
        <el-button type="primary" @click="loadStaffs">查询</el-button>
        <el-button type="success" @click="showAddDialog">添加员工</el-button>
      </div>

      <el-table :data="staffs" stripe style="width: 100%">
        <el-table-column prop="staffId" label="员工 ID" width="120" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right" v-if="isAdmin">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="showEditDialog(row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="deleteStaff(row)"
            >
              删除
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

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑员工' : '添加员工'"
      width="400px"
    >
      <el-form :model="staffForm" :rules="rules" ref="staffFormRef" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="staffForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input v-model="staffForm.password" type="password" placeholder="请输入密码（至少 6 位）" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="staffForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitStaff" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utill/request'

const router = useRouter()
const staffs = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchText = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const staffFormRef = ref(null)
const currentUser = ref(null)

const isAdmin = computed(() => {
  return currentUser.value && currentUser.value.role === 'admin'
})

const staffForm = reactive({
  staffId: null,
  name: '',
  password: '',
  status: 1
})

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
  ]
}

const loadStaffs = async () => {
  try {
    let url = '/admin/staffs'
    const params = {}

    if (searchText.value.trim()) {
      params.name = searchText.value.trim()
    }

    const res = await request.get(url, { params })

    if (res.code === 200) {
      if (Array.isArray(res.data)) {
        staffs.value = res.data
        total.value = res.data.length
      } else if (res.data.list) {
        staffs.value = res.data.list
        total.value = res.data.total
      } else {
        staffs.value = [res.data]
        total.value = 1
      }
    }
  } catch (error) {
    console.error(error)
    staffs.value = []
    total.value = 0
  }
}

const showAddDialog = () => {
  isEdit.value = false
  staffForm.staffId = null
  staffForm.name = ''
  staffForm.password = ''
  staffForm.status = 1
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  isEdit.value = true
  staffForm.staffId = row.staffId
  staffForm.name = row.name
  staffForm.password = ''
  staffForm.status = row.status
  dialogVisible.value = true
}

const submitStaff = async () => {
  if (!staffFormRef.value) return

  await staffFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (isEdit.value) {
          const res = await request.put(`/admin/staffs/${staffForm.staffId}`, {
            name: staffForm.name,
            status: staffForm.status
          })

          if (res.code === 200) {
            ElMessage.success('更新成功')
            dialogVisible.value = false
            loadStaffs()
          }
        } else {
          const res = await request.post('/admin/staffs', {
            name: staffForm.name,
            password: staffForm.password,
            status: staffForm.status
          })

          if (res.code === 200) {
            ElMessage.success('添加成功')
            dialogVisible.value = false
            loadStaffs()
          }
        }
      } catch (error) {
        console.error(error)
      } finally {
        submitting.value = false
      }
    }
  })
}

const deleteStaff = (row) => {
  ElMessageBox.confirm('确定要删除该员工吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/admin/staffs/${row.staffId}`)
      ElMessage.success('删除成功')
      loadStaffs()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('currentUser')
  ElMessage.success('已退出登录')
  router.push('/')
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadStaffs()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadStaffs()
}

onMounted(() => {
  const savedUser = localStorage.getItem('currentUser')
  currentUser.value = savedUser ? JSON.parse(savedUser) : null
  loadStaffs()
})
</script>

<style scoped>
.staffs-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.staffs-card {
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

