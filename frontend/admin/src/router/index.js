import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/AdminLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '仪表板', icon: 'Odometer' }
      },
      {
        path: 'user',
        name: 'UserManage',
        component: () => import('@/views/UserManage.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'community',
        name: 'CommunityManage',
        component: () => import('@/views/CommunityManage.vue'),
        meta: { title: '小区管理', icon: 'OfficeBuilding' }
      },
      {
        path: 'room',
        name: 'RoomManage',
        component: () => import('@/views/RoomManage.vue'),
        meta: { title: '房产管理', icon: 'House' }
      },
      {
        path: 'fee',
        name: 'FeeManage',
        component: () => import('@/views/FeeManage.vue'),
        meta: { title: '缴费管理', icon: 'Wallet' }
      },
      {
        path: 'repair',
        name: 'RepairManage',
        component: () => import('@/views/RepairManage.vue'),
        meta: { title: '报修工单', icon: 'Tools' }
      },
      {
        path: 'announcement',
        name: 'AnnouncementManage',
        component: () => import('@/views/AnnouncementManage.vue'),
        meta: { title: '公告管理', icon: 'Bell' }
      },
      {
        path: 'role',
        name: 'RoleManage',
        component: () => import('@/views/RoleManage.vue'),
        meta: { title: '角色权限', icon: 'Lock' }
      },
      {
        path: 'log',
        name: 'OperationLog',
        component: () => import('@/views/OperationLog.vue'),
        meta: { title: '操作日志', icon: 'Document' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 物业管理系统` : '物业管理系统'
  const token = getToken()
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
