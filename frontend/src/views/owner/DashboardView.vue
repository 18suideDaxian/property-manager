<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElCard, ElRow, ElCol, ElStatistic } from 'element-plus'

const dashboardData = ref({
  roomCount: 0,
  unpaidBills: 0,
  unpaidAmount: 0,
  pendingRepairs: 0
})

onMounted(async () => {
  // TODO: 调用 API 获取仪表板数据
  // const response = await service.get('/owner/dashboard')
  // dashboardData.value = response.data
  
  // 模拟数据
  dashboardData.value = {
    roomCount: 1,
    unpaidBills: 2,
    unpaidAmount: 350.00,
    pendingRepairs: 0
  }
})
</script>

<template>
  <div class="dashboard">
    <h2>欢迎回家</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <span>我的房产</span>
          </template>
          <div class="statistic">
            <el-statistic :value="dashboardData.roomCount" />
            <span>套</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <span>待缴账单</span>
          </template>
          <div class="statistic">
            <el-statistic :value="dashboardData.unpaidBills" />
            <span>笔</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <span>待缴金额</span>
          </template>
          <div class="statistic">
            <el-statistic :value="dashboardData.unpaidAmount" precision="2" />
            <span>元</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <span>待处理报修</span>
          </template>
          <div class="statistic">
            <el-statistic :value="dashboardData.pendingRepairs" />
            <span>单</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>最新公告</span>
          </template>
          <el-empty description="暂无公告" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>待办事项</span>
          </template>
          <el-empty description="暂无待办" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard h2 {
  margin-bottom: 20px;
}

.statistic {
  display: flex;
  align-items: baseline;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.statistic span {
  margin-left: 5px;
  font-size: 14px;
  color: #909399;
}
</style>