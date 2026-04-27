<template>
  <div class="room-manage">
    <el-card>
      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="小区">
          <el-select v-model="searchForm.communityId" placeholder="请选择小区" clearable>
            <el-option v-for="item in communityList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="楼栋">
          <el-input v-model="searchForm.building" placeholder="请输入楼栋" clearable />
        </el-form-item>
        <el-form-item label="房间号">
          <el-input v-model="searchForm.roomNumber" placeholder="请输入房间号" clearable />
        </el-form-item>
        <el-form-item label="业主">
          <el-input v-model="searchForm.ownerName" placeholder="请输入业主姓名" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <div class="table-toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增房产
        </el-button>
        <el-button type="success" @click="handleImport">
          <el-icon><Upload /></el-icon>Excel导入
        </el-button>
        <el-button type="warning" @click="handleExport">
          <el-icon><Download /></el-icon>Excel导出
        </el-button>
        <el-button @click="handleDownloadTemplate">
          <el-icon><Document /></el-icon>下载模板
        </el-button>
      </div>

      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="communityName" label="小区" width="140" />
        <el-table-column prop="building" label="楼栋" width="80" />
        <el-table-column prop="unit" label="单元" width="60" />
        <el-table-column prop="roomNumber" label="房间号" width="80" />
        <el-table-column prop="floor" label="楼层" width="60" />
        <el-table-column prop="area" label="面积(㎡)" width="90" />
        <el-table-column prop="ownerName" label="业主" width="100" />
        <el-table-column prop="ownerPhone" label="业主电话" width="120" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 'occupied' ? 'success' : row.status === 'vacant' ? 'info' : 'warning'">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="小区" prop="communityId">
              <el-select v-model="form.communityId" placeholder="请选择小区">
                <el-option v-for="item in communityList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="楼栋" prop="building">
              <el-input v-model="form.building" placeholder="如：1栋" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单元" prop="unit">
              <el-input v-model="form.unit" placeholder="如：1单元" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房间号" prop="roomNumber">
              <el-input v-model="form.roomNumber" placeholder="如：101" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="楼层" prop="floor">
              <el-input-number v-model="form.floor" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="面积" prop="area">
              <el-input-number v-model="form.area" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="业主" prop="ownerName">
              <el-input v-model="form.ownerName" placeholder="请输入业主姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话" prop="ownerPhone">
              <el-input v-model="form.ownerPhone" placeholder="请输入业主电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="已入住" value="occupied" />
            <el-option label="空置" value="vacant" />
            <el-option label="装修中" value="renovating" />
          </el-select>
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

    <!-- 导入对话框 -->
    <el-dialog v-model="importDialogVisible" title="Excel导入房产" width="500px">
      <el-upload
        ref="uploadRef"
        :auto-upload="false"
        :limit="1"
        accept=".xlsx,.xls"
        :on-change="handleFileChange"
        drag
      >
        <el-icon size="40"><Upload /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">只能上传 xlsx/xls 文件，且不超过 5MB</div>
        </template>
      </el-upload>
      <template #footer>
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitImport" :loading="importLoading">开始导入</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoomList, createRoom, updateRoom, deleteRoom, importRooms, exportRooms, downloadTemplate } from '@/api/room'
import { getAllCommunities } from '@/api/community'
import Pagination from '@/components/Pagination.vue'

const loading = ref(false)
const submitLoading = ref(false)
const importLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const importDialogVisible = ref(false)
const dialogTitle = ref('新增房产')
const formRef = ref()
const uploadRef = ref()
const communityList = ref([])
const importFile = ref(null)

const searchForm = reactive({
  communityId: '',
  building: '',
  roomNumber: '',
  ownerName: ''
})

const pagination = reactive({
  page: 1,
  limit: 20,
  total: 0
})

const form = reactive({
  id: null,
  communityId: null,
  building: '',
  unit: '',
  roomNumber: '',
  floor: 1,
  area: 0,
  ownerName: '',
  ownerPhone: '',
  status: 'vacant',
  remark: ''
})

const rules = {
  communityId: [{ required: true, message: '请选择小区', trigger: 'change' }],
  building: [{ required: true, message: '请输入楼栋', trigger: 'blur' }],
  roomNumber: [{ required: true, message: '请输入房间号', trigger: 'blur' }]
}

function getStatusText(status) {
  const map = { occupied: '已入住', vacant: '空置', renovating: '装修中' }
  return map[status] || status
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getRoomList({
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
  Object.assign(searchForm, { communityId: '', building: '', roomNumber: '', ownerName: '' })
  handleSearch()
}

function handleAdd() {
  dialogTitle.value = '新增房产'
  Object.assign(form, { id: null, communityId: null, building: '', unit: '', roomNumber: '', floor: 1, area: 0, ownerName: '', ownerPhone: '', status: 'vacant', remark: '' })
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑房产'
  Object.assign(form, row)
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定删除该房产吗？', '提示', { type: 'warning' })
    await deleteRoom(row.id)
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
      await updateRoom(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createRoom(form)
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

function handleImport() {
  importFile.value = null
  importDialogVisible.value = true
}

function handleFileChange(file) {
  importFile.value = file.raw
}

async function submitImport() {
  if (!importFile.value) {
    ElMessage.warning('请选择要导入的文件')
    return
  }
  importLoading.value = true
  try {
    const res = await importRooms(importFile.value)
    ElMessage.success(`导入成功，共导入 ${res.data?.count || 0} 条记录`)
    importDialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error(error)
  } finally {
    importLoading.value = false
  }
}

async function handleExport() {
  try {
    const res = await exportRooms(searchForm)
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `房产列表_${new Date().toLocaleDateString()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    console.error(error)
  }
}

async function handleDownloadTemplate() {
  try {
    const res = await downloadTemplate()
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '房产导入模板.xlsx'
    link.click()
    window.URL.revokeObjectURL(url)
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
