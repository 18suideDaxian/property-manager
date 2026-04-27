<template>
  <div class="announcement-manage">
    <el-card>
      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="小区">
          <el-select v-model="searchForm.communityId" placeholder="请选择小区" clearable>
            <el-option v-for="item in communityList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="draft" />
            <el-option label="已发布" value="published" />
            <el-option label="已撤回" value="withdrawn" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <div class="table-toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增公告
        </el-button>
      </div>

      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="标题" show-overflow-tooltip />
        <el-table-column prop="communityName" label="小区" width="140" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTag(row.type)">{{ getTypeName(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="authorName" label="发布人" width="100" />
        <el-table-column prop="publishedAt" label="发布时间" width="160" />
        <el-table-column prop="createdAt" label="创建时间" width="160" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 'draft'" type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 'draft'" type="success" link size="small" @click="handlePublish(row)">发布</el-button>
            <el-button v-if="row.status === 'published'" type="warning" link size="small" @click="handleWithdraw(row)">撤回</el-button>
            <el-button type="info" link size="small" @click="handleView(row)">查看</el-button>
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="小区" prop="communityId">
          <el-select v-model="form.communityId" placeholder="请选择小区" clearable>
            <el-option label="全部小区" :value="null" />
            <el-option v-for="item in communityList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option label="通知" value="notice" />
            <el-option label="公告" value="announcement" />
            <el-option label="温馨提示" value="reminder" />
            <el-option label="紧急通知" value="urgent" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="10" placeholder="请输入公告内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">保存草稿</el-button>
      </template>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog v-model="viewDialogVisible" title="公告详情" width="600px">
      <div class="view-content">
        <h3>{{ viewData.title }}</h3>
        <div class="view-meta">
          <span>类型：{{ getTypeName(viewData.type) }}</span>
          <span>小区：{{ viewData.communityName || '全部小区' }}</span>
          <span>发布人：{{ viewData.authorName }}</span>
          <span>时间：{{ viewData.publishedAt || viewData.createdAt }}</span>
        </div>
        <el-divider />
        <div class="view-text">{{ viewData.content }}</div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getAnnouncementList,
  createAnnouncement,
  updateAnnouncement,
  deleteAnnouncement,
  publishAnnouncement,
  withdrawAnnouncement
} from '@/api/announcement'
import { getAllCommunities } from '@/api/community'
import Pagination from '@/components/Pagination.vue'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('新增公告')
const formRef = ref()
const communityList = ref([])
const viewData = ref({})

const searchForm = reactive({
  title: '',
  communityId: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  limit: 20,
  total: 0
})

const form = reactive({
  id: null,
  title: '',
  communityId: null,
  type: '',
  content: ''
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

function getTypeName(type) {
  const map = { notice: '通知', announcement: '公告', reminder: '温馨提示', urgent: '紧急通知' }
  return map[type] || type
}

function getTypeTag(type) {
  const map = { notice: '', announcement: 'success', reminder: 'info', urgent: 'danger' }
  return map[type] || ''
}

function getStatusType(status) {
  const map = { draft: 'info', published: 'success', withdrawn: 'warning' }
  return map[status] || 'info'
}

function getStatusText(status) {
  const map = { draft: '草稿', published: '已发布', withdrawn: '已撤回' }
  return map[status] || status
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getAnnouncementList({
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

function handleSearch() {
  pagination.page = 1
  fetchData()
}

function handleReset() {
  Object.assign(searchForm, { title: '', communityId: '', status: '' })
  handleSearch()
}

function handleAdd() {
  dialogTitle.value = '新增公告'
  Object.assign(form, { id: null, title: '', communityId: null, type: '', content: '' })
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑公告'
  Object.assign(form, row)
  dialogVisible.value = true
}

function handleView(row) {
  viewData.value = row
  viewDialogVisible.value = true
}

async function handlePublish(row) {
  try {
    await ElMessageBox.confirm('确定发布该公告吗？', '提示', { type: 'info' })
    await publishAnnouncement(row.id)
    ElMessage.success('发布成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

async function handleWithdraw(row) {
  try {
    await ElMessageBox.confirm('确定撤回该公告吗？', '提示', { type: 'warning' })
    await withdrawAnnouncement(row.id)
    ElMessage.success('已撤回')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定删除该公告吗？', '提示', { type: 'warning' })
    await deleteAnnouncement(row.id)
    ElMessage.success('删除成功')
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
      await updateAnnouncement(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createAnnouncement(form)
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
}

.view-content h3 {
  text-align: center;
  margin-bottom: 16px;
}

.view-meta {
  display: flex;
  justify-content: center;
  gap: 20px;
  color: #999;
  font-size: 14px;
}

.view-text {
  line-height: 1.8;
  white-space: pre-wrap;
}
</style>
