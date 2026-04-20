<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import service from '@/api'

const router = useRouter()

const loginForm = ref({
  phone: '',
  password: ''
})

const loading = ref(false)

const handleLogin = async () => {
  if (!loginForm.value.phone || !loginForm.value.password) {
    ElMessage.error('请输入手机号和密码')
    return
  }
  
  loading.value = true
  try {
    const response = await service.post('/owner/auth/login', loginForm.value)
    if (response.code === 200) {
      // 保存token
      localStorage.setItem('token', response.data)
      localStorage.setItem('userType', 'owner')
      ElMessage.success('登录成功')
      router.push('/owner/dashboard')
    } else {
      ElMessage.error(response.message || '登录失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请检查网络连接')
  } finally {
    loading.value = false
  }
}

const handleAdminLogin = () => {
  router.push('/admin/login')
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="title">物业管理系统</h2>
      <el-form :model="loginForm" label-width="80px" class="login-form">
        <el-form-item label="手机号">
          <el-input v-model="loginForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading">登录</el-button>
          <el-button @click="handleAdminLogin">管理员登录</el-button>
        </el-form-item>
      </el-form>
      <div class="register-link">
        <span>还没有账号？</span>
        <el-button type="text" @click="router.push('/register')">立即注册</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.login-form {
  margin-bottom: 20px;
}

.register-link {
  text-align: center;
}

.register-link span {
  margin-right: 10px;
  color: #666;
}
</style>