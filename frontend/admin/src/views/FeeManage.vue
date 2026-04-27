<template>
  <div class="fee-manage">
    <el-card>
      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="小区">
          <el-select v-model="searchForm.communityId" placeholder="请选择小区" clearable>
            <el-option v-for="item in communityList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="费用类型">
          <el-select v-model="searchForm.feeType" placeholder="请选择类型" clearable>
            <el-option label="物业费" value="property" />
            <el-option label="水费" value="water" />
            <el-option label="电费" value="electricity" />
            <el-option label="停车费" value="parking" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="缴费状态">
          <el-select v-model="searchForm.payStatus" placeholder="请选择状态" clearable>
            <el-option label="未缴" value="unpaid" />
            <el-option label="已缴" value="paid" />
            <el-option label="逾期" value="overdue" />
          </el-select>
        </el-form-item>
        <el-form-item label="账单月份">
          <el-date-picker v-model="searchForm.billMonth" type="month" placeholder="选择月份" value-format="YYYY-MM" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <div class="table-toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增账单
        </el-button>
        <el-button type="success" @click="handleBatchGenerate">
          <el-icon><MagicStick /></el-icon>批量生成
        </el-button>
        <el-button type="warning" @click="handleExport">
          <el-icon><Download /></el-icon>导出
        </el-button>
      </div>

      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="communityName" label="小区" width="120" />
        <el-table-column prop="roomInfo" label="房间" width="120" />
        <el-table-column prop="ownerName" label="业主" width="100" />
        <el-table-column prop="feeType" label="费用类型" width="100">
          <template #default="{ row }">
            <el-tag>{{ getFeeTypeName(row.feeType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额(元)" width="100" />
        <el-table-column prop="billMonth" label="账单月份" width="100" />
        <el-table-column prop="payStatus" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.payStatus === 'paid' ? 'success' : row.payStatus === 'overdue' ? 'danger' : 'warning'">
              {{ getPayStatusText(row.payStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payTime" label="缴费时间" width="160" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.payStatus !== 'paid'" type="success" link size="small" @click="handleMarkPaid(row)">标记已缴</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <Pagination
        v-model:page="pagination.page"
        v-model:limit="pagination.limit"
        :total="pagination.total"
        @pagination="fetchData"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="小区" prop="communityId">
          <el-select v-model="form.communityId" placeholder="请选择小区" @change="handleCommunityChange">
            <el-option v-for="item in communityList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="房间" prop="roomId">
          <el-select v-model="form.roomId" placeholder="请选择房间" filterable>
            <el-option v-for="item in roomList" :key="item.id" :label="item.label" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="费用类型" prop="feeType">
          <el-select v-model="form.feeType" placeholder="请选择类型">
            <el-option label="物业费" value="property" />
            <el-option label="水费" value="water" />
            <el-option label="电费" value="electricity" />
            <el-option label="停车费" value="parking" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="账单月份" prop="billMonth">
          <el-date-picker v-model="form.billMonth" type="month" placeholder="选择月份" value-format="YYYY-MM" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 批量生成对话框 -->
    <el-dialog v-model="batchDialogVisible" title="批量生成账单" width="500px">
      <el-form ref="batchFormRef" :model="batchForm" :rules="batchRules" label-width="90px">
        <el-form-item label="小区" prop="communityId">
          <el-select v-model="batchForm.communityId" placeholder="请选择小区">
            <el-option v-for="item in communityList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="费用类型" prop="feeType">
          <el-select v-model="batchForm.feeType" placeholder="请选择类型">
            <el-option label="物业费" value="property" />
            <el-option label="水费" value="water" />
            <el-option label="电费" value="electricity" />
            <el-option label="停车费" value="parking" />
          </el-select>
        </el-form-item>
        <el-form-item label="账单月份" prop="billMonth">
          <el-date-picker v-model="batchForm.billMonth" type="month" placeholder="选择月份" value-format="YYYY-MM" style="width: 100%" />
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input-number v-model="batchForm.unitPrice" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatch" :loading="batchLoading">生成</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getFeeList, createFee, updateFee, deleteFee, markAsPaid, batchGenerate, exportFees } from '@/api/fee'
import { getAllCommunities } from '@/api/community'
import { getRoomList } from '@/api/room'
import Pagination from '@/components/Pagination.vue'

const loading = ref(false)
const submitLoading = ref(false)
const batchLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const batchDialogVisible = ref(false)
const dialogTitle = ref('新增账单')
const formRef = ref()
const batchFormRef = ref()
const communityList = ref([])
const roomList = ref([])

const searchForm = reactive({
  communityId: '',
  feeType: '',
  payStatus: '',
  billMonth: ''
})

const pagination = reactive({
  page: 1,
  limit: 20,
  total: 0
})

const form = reactive({
  id: null,
  communityId: null,
  roomId: null,
  feeType: '',
  amount: 0,
  billMonth: '',
  remark: ''
})

const batchForm = reactive({
  communityId: null,
  feeType: '',
  billMonth: '',
  unitPrice: 0
})

const rules = {
  communityId: [{ required: true, message: '请选择小区', trigger: 'change' }],
  roomId: [{ required: true, message: '请选择房间', trigger: 'change' }],
  feeType: [{ required: true, message: '请选择费用类型', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  billMonth: [{ required: true, message: '请选择账单月份', trigger: 'change' }]
}

const batchRules = {
  communityId: [{ required: true, message: '请选择小区', trigger: 'change' }],
  feeType: [{ required: true, message: '请选择费用类型', trigger: 'change' }],
  billMonth: [{ required: true, message: '请选择账单月份', trigger: 'change' }],
  unitPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }]
}

function getFeeTypeName(type) {
  const map = { property: '物业费', water: '水费', electricity: '电费', parking: '停车费', other: '其他' }
  return map[type] || type
}

function getPayStatusText(status) {
  const map = { unpaid: '未缴', paid: '已缴', overdue: '逾期' }
  return map[status] || status
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getFeeList({
      page: pagination.page,
      limit: pagination.limit,
      ...searchForm
    })
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

async function fetchCommunities() {
  try {
    const res = await getAllCommunities()
    communityList.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

async function handleCommunityChange(communityId) {
  if (!communityId) {
    roomList.value = []
    return
  }
  try {
    const res = await getRoomList({ communityId, limit: 1000 })
    roomList.value = (res.data.list || []).map(item => ({
      id: item.id,
      label: `${item.building}${item.unit ? item.unit + '单元' : ''}${item.roomNumber}`
    }))
  } catch (error) {
    console.error(error)
  }
}

function handleSearch() {
  pagination.page = 1
  fetchData()
}

function handleReset() {
  Object.assign(searchForm, { communityId: '', feeType: '', payStatus: '', billMonth: '' })
  handleSearch()
}

function handleAdd() {
  dialogTitle.value = '新增账单'
  Object.assign(form, { id: null, communityId: null, roomId: null, feeType: '', amount: 0, billMonth: '', remark: '' })
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑账单'
  Object.assign(form, row)
  if (row.communityId) handleCommunityChange(row.communityId)
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定删除该账单吗？', '提示', { type: 'warning' })
    await deleteFee(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

async function handleMarkPaid(row) {
  try {
    await ElMessageBox.confirm('确认标记为已缴费？', '提示', { type: 'info' })
    await markAsPaid(row.id)
    ElMessage.success('标记成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

async function submitForm() {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    if (form.id) {
      await updateFee(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createFee(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    if (error !== false) console.error(error)
  } finally {
    submitLoading.value = false
  }
}

function handleBatchGenerate() {
  Object.assign(batchForm, { communityId: null, feeType: '', billMonth: '', unitPrice: 0 })
  batchDialogVisible.value = true
}

async function submitBatch() {
  try {
    await batchFormRef.value.validate()
    batchLoading.value = true
    const res = await batchGenerate(batchForm)
    ElMessage.success(`批量生成成功，共生成 ${res.data?.count || 0} 条账单`)
    batchDialogVisible.value = false
    fetchData()
  } catch (error) {
    if (error !== false) console.error(error)
  } finally {
    batchLoading.value = false
  }
}

async function handleExport() {
  try {
    const res = await exportFees(searchForm)
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `缴费记录_${new Date().toLocaleDateString()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchData()
  fetchCommunities()
})
</script>

<style scoped>
.search-form {
  margin-bottom: 16px;
}

.table-toolbar {
  margin-bottom: 16px;
  display: flex;
  gap: 10px;
}
</style>
