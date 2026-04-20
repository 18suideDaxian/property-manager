<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMenu, ElMenuItem, ElHeader, ElAside, ElMain, ElContainer } from 'element-plus'

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
  { path: '/owner/dashboard', name: '首页', icon: 'HomeFilled' },
  { path: '/owner/fee', name: '缴费', icon: 'Money' },
  { path: '/owner/repair', name: '报修', icon: 'Tools' },
  { path: '/owner/announcement', name: '公告', icon: 'Document' },
  { path: '/owner/room', name: '家业', icon: 'OfficeBuilding' },
  { path: '/owner/profile', name: '我的', icon: 'User' }
]
</script>

<template>
  <el-container class="layout-container">
    <el-aside width="200px" class="aside">
      <div class="logo">
        <h3>物业管理系统</h3>
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
          <span>欢迎使用物业管理系统</span>
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