<template>
  <div class="sidebar" :class="{ 'is-collapse': isCollapse }">
    <div class="logo">
      <img src="https://via.placeholder.com/32" alt="Logo" class="logo-img" />
      <span v-show="!isCollapse" class="logo-title">物业管理系统</span>
    </div>
    <el-scrollbar>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <template #title>仪表板</template>
        </el-menu-item>
        <el-menu-item index="/user">
          <el-icon><User /></el-icon>
          <template #title>用户管理</template>
        </el-menu-item>
        <el-menu-item index="/community">
          <el-icon><OfficeBuilding /></el-icon>
          <template #title>小区管理</template>
        </el-menu-item>
        <el-menu-item index="/room">
          <el-icon><House /></el-icon>
          <template #title>房产管理</template>
        </el-menu-item>
        <el-menu-item index="/fee">
          <el-icon><Wallet /></el-icon>
          <template #title>缴费管理</template>
        </el-menu-item>
        <el-menu-item index="/repair">
          <el-icon><Tools /></el-icon>
          <template #title>报修工单</template>
        </el-menu-item>
        <el-menu-item index="/announcement">
          <el-icon><Bell /></el-icon>
          <template #title>公告管理</template>
        </el-menu-item>
        <el-menu-item index="/role">
          <el-icon><Lock /></el-icon>
          <template #title>角色权限</template>
        </el-menu-item>
        <el-menu-item index="/log">
          <el-icon><Document /></el-icon>
          <template #title>操作日志</template>
        </el-menu-item>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const props = defineProps({
  isCollapse: {
    type: Boolean,
    default: false
  }
})

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  width: 220px;
  background-color: #304156;
  transition: width 0.3s;
  overflow: hidden;
  z-index: 1001;
}

.sidebar.is-collapse {
  width: 64px;
}

.logo {
  height: 50px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  background-color: #263445;
  overflow: hidden;
}

.logo-img {
  width: 32px;
  height: 32px;
  flex-shrink: 0;
}

.logo-title {
  margin-left: 10px;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
}

.el-menu {
  border-right: none;
}

:deep(.el-menu-item.is-active) {
  background-color: #263445 !important;
}
</style>
