<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-icon" style="background: #409EFF">
              <el-icon size="28"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-icon" style="background: #67C23A">
              <el-icon size="28"><House /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.roomCount }}</div>
              <div class="stat-label">房产总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-icon" style="background: #E6A23C">
              <el-icon size="28"><Wallet /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.pendingFeeCount }}</div>
              <div class="stat-label">待缴费用</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-icon" style="background: #F56C6C">
              <el-icon size="28"><Tools /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.repairCount }}</div>
              <div class="stat-label">待处理工单</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷操作和最近数据 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>最近报修工单</span>
          </template>
          <el-table :data="recentRepairs" stripe style="width: 100%">
            <el-table-column prop="id" label="工单号" width="80" />
            <el-table-column prop="communityName" label="小区" />
            <el-table-column prop="roomInfo" label="房间" />
            <el-table-column prop="description" label="问题描述" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="提交时间" width="160" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>快捷操作</span>
          </template>
          <div class="quick-actions">
            <el-button type="primary" @click="$router.push('/user')">
              <el-icon><User /></el-icon>用户管理
            </el-button>
            <el-button type="success" @click="$router.push('/community')">
              <el-icon><OfficeBuilding /></el-icon>小区管理
            </el-button>
            <el-button type="warning" @click="$router.push('/fee')">
              <el-icon><Wallet /></el-icon>缴费管理
            </el-button>
            <el-button type="danger" @click="$router.push('/repair')">
              <el-icon><Tools /></el-icon>报修工单
            </el-button>
            <el-button @click="$router.push('/announcement')">
              <el-icon><Bell /></el-icon>公告管理
            </el-button>
            <el-button @click="$router.push('/room')">
              <el-icon><House /></el-icon>房产管理
            </el-button>
          </div>
        </el-card>

        <el-card style="margin-top: 20px">
          <template #header>
            <span>最新公告</span>
          </template>
          <div v-if="recentAnnouncements.length === 0" class="empty-text">暂无公告</div>
          <div v-for="item in recentAnnouncements" :key="item.id" class="announcement-item">
            <div class="announcement-title">{{ item.title }}</div>
            <div class="announcement-time">{{ item.createdAt }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const stats = ref({
  userCount: 0,
  roomCount: 0,
  pendingFeeCount: 0,
  repairCount: 0
})

const recentRepairs = ref([])
const recentAnnouncements = ref([])

function getStatusType(status) {
  const map = {
    pending: 'warning',
    assigned: 'info',
    processing: '',
    completed: 'success',
    closed: 'danger'
  }
  return map[status] || 'info'
}

function getStatusText(status) {
  const map = {
    pending: '待处理',
    assigned: '已分配',
    processing: '处理中',
    completed: '已完成',
    closed: '已关闭'
  }
  return map[status] || status
}

onMounted(async () => {
  try {
    const res = await request.get('/admin/dashboard')
    if (res.data) {
      stats.value = res.data.stats || stats.value
      recentRepairs.value = res.data.recentRepairs || []
      recentAnnouncements.value = res.data.recentAnnouncements || []
    }
  } catch (error) {
    console.error('获取仪表板数据失败:', error)
  }
})
</script>

<style scoped>
.stat-cards .stat-item {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-right: 16px;
}

.stat-info .stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #333;
}

.stat-info .stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 4px;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.quick-actions .el-button {
  width: calc(50% - 5px);
  margin-left: 0;
}

.announcement-item {
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}

.announcement-time {
  font-size: 12px;
  color: #999;
}

.empty-text {
  text-align: center;
  color: #999;
  padding: 20px;
}
</style>
