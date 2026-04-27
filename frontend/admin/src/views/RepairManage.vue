<template>
  <div class="repair-manage">
    <el-card>
      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="小区">
          <el-select v-model="searchForm.communityId" placeholder="请选择小区" clearable>
            <el-option v-for="item in communityList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待处理" value="pending" />
            <el-option label="已分配" value="assigned" />
            <el-option label="处理中" value="processing" />
            <el-option label="已完成" value="completed" />
            <el-option label="已关闭" value="closed" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="searchForm.priority" placeholder="请选择优先级" clearable>
            <el-option label="低" value="low" />
            <el-option label="中" value="medium" />
            <el-option label="高" value="high" />
            <el-option label="紧急" value="urgent" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column prop="id" label="工单号" width="80" />
        <el-table-column prop="communityName" label="小区" width="120" />
        <el-table-column prop="roomInfo" label="房间" width="120" />
        <el-table-column prop="reporterName" label="报修人" width="100" />
        <el-table-column prop="category" label="类别" width="80" />
        <el-table-column prop="description" label="问题描述" show-overflow-tooltip />
        <el-table-column prop="priority" label="优先级" width="80">
          <template #default="{ row }">
            <el-tag :type="getPriorityType(row.priority)">{{ getPriorityText(row.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assigneeName" label="处理人" width="90" />
        <el-table-column prop="createdAt" label="报修时间" width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 'pending'" type="primary" link size="small" @click="handleAssign(row)">分配</el-button>
            <el-button v-if="row.status === 'assigned'" type="warning" link size="small" @click="handleProcess(row)">开始处理</el-button>
            <el-button v-if="row.status === 'processing'" type="success" link size="small" @click="handleComplete(row)">完成</el-button>
            <el-button type="info" link size="small" @click="handleView(row)">详情</el-button>
            <el-button v-if="row.status === 'completed'" type="danger" link size="small" @click="handleClose(row)">关闭</el-button>
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

    <!-- 分配对话框 -->
    <el-dialog v-model="assignDialogVisible" title="分配工单" width="400px">
      <el-form ref="assignFormRef" :model="assignForm" :rules="assignRules" label-width="80px">
        <el-form-item label="处理人" prop="assigneeId">
          <el-select v-model="assignForm.assigneeId" placeholder="请选择处理人">
            <el-option v-for="item in staffList" :key="item.id" :label="item.nickname" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="assignForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssign" :loading="assignLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 处理对话框 -->
    <el-dialog v-model="processDialogVisible" title="处理工单" width="500px">
      <el-form ref="processFormRef" :model="processForm" :rules="processRules" label-width="80px">
        <el-form-item label="处理说明" prop="processNote">
          <el-input v-model="processForm.processNote" type="textarea" :rows="4" placeholder="请输入处理说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="processDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitProcess" :loading="processLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 完成对话框 -->
    <el-dialog v-model="completeDialogVisible" title="完成工单" width="500px">
      <el-form ref="completeFormRef" :model="completeForm" :rules="completeRules" label-width="80px">
        <el-form-item label="完成说明" prop="completeNote">
          <el-input v-model="completeForm.completeNote" type="textarea" :rows="4" placeholder="请输入完成说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitComplete" :loading="completeLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="工单详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="工单号">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="小区">{{ detailData.communityName }}</el-descriptions-item>
        <el-descriptions-item label="房间">{{ detailData.roomInfo }}</el-descriptions-item>
        <el-descriptions-item label="报修人">{{ detailData.reporterName }}</el-descriptions-item>
        <el-descriptions-item label="类别">{{ detailData.category }}</el-descriptions-item>
        <el-descriptions-item label="优先级">
          <el-tag :type="getPriorityType(detailData.priority)">{{ getPriorityText(detailData.priority) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detailData.status)">{{ getStatusText(detailData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理人">{{ detailData.assigneeName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="问题描述" :span="2">{{ detailData.description }}</el-descriptions-item>
        <el-descriptions-item label="处理说明" :span="2">{{ detailData.processNote || '-' }}</el-descriptions-item>
        <el-descriptions-item label="完成说明" :span="2">{{ detailData.completeNote || '-' }}</el-descriptions-item>
        <el-descriptions-item label="报修时间">{{ detailData.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ detailData.completedAt || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRepairList, getRepairDetail, assignRepair, processRepair, completeRepair, closeRepair } from '@/api/repair'
import { getAllCommunities } from '@/api/community'
import { getUserList } from '@/api/user'
import Pagination from '@/components/Pagination.vue'

const loading = ref(false)
const assignLoading = ref(false)
const processLoading = ref(false)
const completeLoading = ref(false)
const tableData = ref([])
const communityList = ref([])
const staffList = ref([])
const assignDialogVisible = ref(false)
const processDialogVisible = ref(false)
const completeDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const assignFormRef = ref()
const processFormRef = ref()
const completeFormRef = ref()
const currentId = ref(null)
const detailData = ref({})

const searchForm = reactive({
  communityId: '',
  status: '',
  priority: ''
})

const pagination = reactive({
  page: 1,
  limit: 20,
  total: 0
})

const assignForm = reactive({
  assigneeId: null,
  remark: ''
})

const processForm = reactive({
  processNote: ''
})

const completeForm = reactive({
  completeNote: ''
})

const assignRules = {
  assigneeId: [{ required: true, message: '请选择处理人', trigger: 'change' }]
}

const processRules = {
  processNote: [{ required: true, message: '请输入处理说明', trigger: 'blur' }]
}

const completeRules = {
  completeNote: [{ required: true, message: '请输入完成说明', trigger: 'blur' }]
}

function getPriorityType(priority) {
  const map = { low: 'info', medium: '', high: 'warning', urgent: 'danger' }
  return map[priority] || 'info'
}

function getPriorityText(priority) {
  const map = { low: '低', medium: '中', high: '高', urgent: '紧急' }
  return map[priority] || priority
}

function getStatusType(status) {
  const map = { pending: 'warning', assigned: 'info', processing: '', completed: 'success', closed: 'danger' }
  return map[status] || 'info'
}

function getStatusText(status) {
  const map = { pending: '待处理', assigned: '已分配', processing: '处理中', completed: '已完成', closed: '已关闭' }
  return map[status] || status
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getRepairList({
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

async function fetchStaff() {
  try {
    const res = await getUserList({ role: 'property', limit: 100 })
    staffList.value = res.data.list || []
  } catch (error) {
    console.error(error)
  }
}

function handleSearch() {
  pagination.page = 1
  fetchData()
}

function handleReset() {
  Object.assign(searchForm, { communityId: '', status: '', priority: '' })
  handleSearch()
}

function handleAssign(row) {
  currentId.value = row.id
  Object.assign(assignForm, { assigneeId: null, remark: '' })
  assignDialogVisible.value = true
}

function handleProcess(row) {
  currentId.value = row.id
  processForm.processNote = ''
  processDialogVisible.value = true
}

function handleComplete(row) {
  currentId.value = row.id
  completeForm.completeNote = ''
  completeDialogVisible.value = true
}

async function handleView(row) {
  try {
    const res = await getRepairDetail(row.id)
    detailData.value = res.data || row
    detailDialogVisible.value = true
  } catch (error) {
    detailData.value = row
    detailDialogVisible.value = true
  }
}

async function handleClose(row) {
  try {
    await ElMessageBox.confirm('确定关闭该工单吗？', '提示', { type: 'warning' })
    await closeRepair(row.id)
    ElMessage.success('工单已关闭')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

async function submitAssign() {
  try {
    await assignFormRef.value.validate()
    assignLoading.value = true
    await assignRepair(currentId.value, assignForm)
    ElMessage.success('分配成功')
    assignDialogVisible.value = false
    fetchData()
  } catch (error) {
    if (error !== false) console.error(error)
  } finally {
    assignLoading.value = false
  }
}

async function submitProcess() {
  try {
    await processFormRef.value.validate()
    processLoading.value = true
    await processRepair(currentId.value, processForm)
    ElMessage.success('开始处理')
    processDialogVisible.value = false
    fetchData()
  } catch (error) {
    if (error !== false) console.error(error)
  } finally {
    processLoading.value = false
  }
}

async function submitComplete() {
  try {
    await completeFormRef.value.validate()
    completeLoading.value = true
    await completeRepair(currentId.value, completeForm)
    ElMessage.success('工单已完成')
    completeDialogVisible.value = false
    fetchData()
  } catch (error) {
    if (error !== false) console.error(error)
  } finally {
    completeLoading.value = false
  }
}

onMounted(() => {
  fetchData()
  fetchCommunities()
  fetchStaff()
})
</script>

<style scoped>
.search-form {
  margin-bottom: 16px;
}
</style>
