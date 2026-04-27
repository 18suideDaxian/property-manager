import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, getUserInfo, logout as logoutApi } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken() || '')
  const userInfo = ref({
    id: null,
    username: '',
    nickname: '',
    avatar: '',
    role: '',
    communityId: null,
    communityName: ''
  })

  // 登录
  async function login(loginForm) {
    const res = await loginApi(loginForm)
    token.value = res.data.token
    setToken(res.data.token)
    return res
  }

  // 获取用户信息
  async function fetchUserInfo() {
    const res = await getUserInfo()
    userInfo.value = res.data
    return res
  }

  // 登出
  async function logout() {
    try {
      await logoutApi()
    } finally {
      token.value = ''
      userInfo.value = {
        id: null,
        username: '',
        nickname: '',
        avatar: '',
        role: '',
        communityId: null,
        communityName: ''
      }
      removeToken()
      router.push('/login')
    }
  }

  return {
    token,
    userInfo,
    login,
    fetchUserInfo,
    logout
  }
})
