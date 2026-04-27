<template>
  <div class="operation-log">
    <el-card>
      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="操作人">
          <el-input v-model="searchForm.operatorName" placeholder="请输入操作人" clearable />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="searchForm.action" placeholder="请选择类型" clearable>
            <el-option label="新增" value="create" />
            <el-option label="编辑" value="update" />
            <el-option label="删除" value="delete" />
            <el-option label="登录" value="login" />
            <el-option label="导出" value="export" />
          </el-select>
        </el-form-item>
        <el-form-item label="模块">
          <el-select v-model="searchForm.module" placeholder="请选择模块" clearable>
            <el-option label="用户管理" value="user" />
            <el-option label="小区管理" value="community" />
            <el-option label="房产管理" value="room" />
            <el-option label="缴费管理" value="fee" />
            <el-option label="报修工单" value="repair" />
            <el-option label="公告管理" value="announcement" />
            <el-option label="角色权限" value="role" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="operatorName" label="操作人" width="120" />
        <el-table-column prop="module" label="模块" width="100">
          <template #default="{ row }">
            {{ getModuleName(row.module) }}
          </template>
        </el-table-column>
        <el-table-column prop="action" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getActionTag(row.action)">{{ getActionName(row.action) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="操作描述" show-overflow-tooltip />
        <el-table-column prop="ip" label="IP地址" width="130" />
        <el-table-column prop="createdAt" label="操作时间" width="170" />
        <el-table-column label="详情" width="80">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleView(row)">查看</el-button>
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

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="操作详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="操作人">{{ detailData.operatorName }}</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ detailData.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="模块">{{ getModuleName(detailData.module) }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">{{ getActionName(detailData.action) }}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ detailData.ip }}</el-descriptions-item>
        <el-descriptions-item label="User-Agent" :span="2">{{ detailData.userAgent || '-' }}</el-descriptions-item>
        <el-descriptions-item label="操作描述" :span="2">{{ detailData.description }}</el-descriptions-item>
      </el-descriptions>
      <div v-if="detailData.requestData" class="detail-section">
        <h4>请求数据</h4>
        <pre>{{ formatJson(detailData.requestData) }}</pre>
      </div>
      <div v-if="detailData.responseData" class="detail-section">
        <h4>响应数据</h4>
        <pre>{{ formatJson(detailData.responseData) }}</pre>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getOperationLogs } from '@/api/role'
import Pagination from '@/components/Pagination.vue'

const loading = ref(false)
const tableData = ref([])
const detailDialogVisible = ref(false)
const detailData = ref({})

const searchForm = reactive({
  operatorName: '',
  action: '',
  module: '',
  dateRange: null
})

const pagination = reactive({
  page: 1,
  limit: 20,
  total: 0
})

function getModuleName(module) {
  const map = {
    user: '用户管理',
    community: '小区管理',
    room: '房产管理',
    fee: '缴费管理',
    repair: '报修工单',
    announcement: '公告管理',
    role: '角色权限'
  }
  return map[module] || module
}

function getActionName(action) {
  const map = {
    create: '新增',
    update: '编辑',
    delete: '删除',
    login: '登录',
    export: '导出'
  }
  return map[action] || action
}

function getActionTag(action) {
  const map = {
    create: 'success',
    update: '',
    delete: 'danger',
    login: 'info',
    export: 'warning'
  }
  return map[action] || 'info'
}

function formatJson(data) {
  if (typeof data === 'string') {
    try {
      return JSON.stringify(JSON.parse(data), null, 2)
    } catch {
      return data
    }
  }
  return JSON.stringify(data, null, 2)
}

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      limit: pagination.limit,
      operatorName: searchForm.operatorName,
      action: searchForm.action,
      module: searchForm.module
    }
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    const res = await getOperationLogs(params)
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.page = 1
  fetchData()
}

function handleReset() {
  Object.assign(searchForm, { operatorName: '', action: '', module: '', dateRange: null })
  handleSearch()
}

function handleView(row) {
  detailData.value = row
  detailDialogVisible.value = true
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.search-form {
  margin-bottom: 16px;
}

.detail-section {
  margin-top: 16px;
}

.detail-section h4 {
  margin-bottom: 8px;
  color: #333;
}

.detail-section pre {
  background: #f5f5f5;
  padding: 12px;
  border-radius: 4px;
  font-size: 12px;
  overflow-x: auto;
  max-height: 300px;
}
</style>
