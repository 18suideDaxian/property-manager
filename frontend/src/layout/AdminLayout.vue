<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const activeMenu = ref(route.path)

const handleMenuClick = (path: string) => {
  router.push(path)
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userType')
  router.push('/login')
}

const menuItems = [
  { path: '/admin/dashboard', name: '仪表板', icon: 'DataAnalysis' },
  { path: '/admin/user', name: '用户管理', icon: 'User' },
  { path: '/admin/community', name: '楼盘管理', icon: 'OfficeBuilding' },
  { path: '/admin/fee', name: '缴费管理', icon: 'Money' },
  { path: '/admin/repair', name: '报修工单', icon: 'Tools' },
  { path: '/admin/announcement', name: '公告管理', icon: 'Document' },
  { path: '/admin/log', name: '操作日志', icon: 'List' }
]
</script>

<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <h3>物业管理后台</h3>
      </div>
      <el-menu
        :default-active="route.path"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        @select="handleMenuClick"
      >
        <el-menu-item
          v-for="item in menuItems"
          :key="item.path"
          :index="item.path"
        >
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.name }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <span>管理员控制台</span>
        </div>
        <div class="header-right">
          <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background-color: #304156;
  color: white;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b3a4b;
}

.logo h3 {
  margin: 0;
  color: white;
  font-size: 18px;
}

.header {
  background-color: white;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>