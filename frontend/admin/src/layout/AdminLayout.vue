<template>
  <div class="admin-layout">
    <Sidebar :is-collapse="isCollapse" />
    <div class="main-container" :class="{ 'is-collapse': isCollapse }">
      <Header :is-collapse="isCollapse" @toggle-collapse="toggleCollapse" />
      <div class="content-container">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Sidebar from './Sidebar.vue'
import Header from './Header.vue'

const isCollapse = ref(false)

function toggleCollapse() {
  isCollapse.value = !isCollapse.value
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-left: 220px;
  transition: margin-left 0.3s;
  overflow: hidden;
}

.main-container.is-collapse {
  margin-left: 64px;
}

.content-container {
  flex: 1;
  padding: 20px;
  background: #f0f2f5;
  overflow-y: auto;
}
</style>
