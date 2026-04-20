<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElDatePicker, ElButton, ElTable, ElTableColumn } from 'element-plus'

const repairForm = ref({
  repairType: '',
  title: '',
  description: '',
  contactName: '',
  contactPhone: '',
  appointmentDate: ''
})

const repairTypes = [
  { value: '1', label: '水电维修' },
  { value: '2', label: '门窗维修' },
  { value: '3', label: '家电故障' },
  { value: '4', label: '管道疏通' },
  { value: '5', label: '其他' }
]

const repairList = ref([])
const dialogVisible = ref(false)
const loading = ref(false)

const handleSubmit = async () => {
  if (!repairForm.value.repairType || !repairForm.value.title || !repairForm.value.description) {
    ElMessage.error('请填写完整信息')
    return
  }
  
  loading.value = true
  try {
    // TODO: 调用 API 提交报修
    // await service.post('/owner/repair/create', repairForm.value)
    ElMessage.success('报修提交成功')
    dialogVisible.value = false
    // 重置表单
    repairForm.value = {
      repairType: '',
      title: '',
      description: '',
      contactName: '',
      contactPhone: '',
      appointmentDate: ''
    }
  } catch (error) {
    ElMessage.error('提交失败')
  } finally {
    loading.value = false
  }
}

const handleViewList = () => {
  // TODO: 加载报修列表
}
</script>

<template>
  <div class="repair-view">
    <div class="header">
      <h2>报修服务</h2>
      <el-button type="primary" @click="dialogVisible = true">提交报修</el-button>
    </div>
    
    <el-card style="margin-top: 20px">
      <template #header>
        <span>我的报修记录</span>
      </template>
      <el-table :data="repairList" style="width: 100%">
        <el-table-column prop="orderNumber" label="工单号" width="180" />
        <el-table-column prop="title" label="报修内容" />
        <el-table-column prop="repairType" label="类型" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 0 ? 'warning' : scope.row.status === 3 ? 'success' : 'info'">
              {{ scope.row.status === 0 ? '待处理' : scope.row.status === 3 ? '已完成' : '处理中' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="180" />
      </el-table>
      <el-empty v-if="repairList.length === 0" description="暂无报修记录" />
    </el-card>
    
    <!-- 提交报修对话框 -->
    <el-dialog v-model="dialogVisible" title="提交报修" width="600px">
      <el-form :model="repairForm" label-width="100px">
        <el-form-item label="报修类型" required>
          <el-select v-model="repairForm.repairType" placeholder="请选择报修类型" style="width: 100%">
            <el-option
              v-for="item in repairTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="报修标题" required>
          <el-input v-model="repairForm.title" placeholder="简要描述问题" />
        </el-form-item>
        <el-form-item label="详细描述" required>
          <el-input
            v-model="repairForm.description"
            type="textarea"
            :rows="4"
            placeholder="详细描述问题情况"
          />
        </el-form-item>
        <el-form-item label="联系人" required>
          <el-input v-model="repairForm.contactName" placeholder="联系人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" required>
          <el-input v-model="repairForm.contactPhone" placeholder="联系电话" />
        </el-form-item>
        <el-form-item label="预约日期">
          <el-date-picker
            v-model="repairForm.appointmentDate"
            type="date"
            placeholder="选择预约日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="上传照片">
          <el-button>上传图片</el-button>
          <span style="font-size: 12px; color: #999">可上传问题照片</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h2 {
  margin: 0;
}
</style>