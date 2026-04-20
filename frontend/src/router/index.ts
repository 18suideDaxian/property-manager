import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/LoginView.vue')
    },
    {
      path: '/owner',
      name: 'OwnerLayout',
      component: () => import('@/layout/OwnerLayout.vue'),
      children: [
        {
          path: 'dashboard',
          name: 'OwnerDashboard',
          component: () => import('@/views/owner/DashboardView.vue')
        },
        {
          path: 'fee',
          name: 'OwnerFee',
          component: () => import('@/views/owner/FeeView.vue')
        },
        {
          path: 'repair',
          name: 'OwnerRepair',
          component: () => import('@/views/owner/RepairView.vue')
        },
        {
          path: 'announcement',
          name: 'OwnerAnnouncement',
          component: () => import('@/views/owner/AnnouncementView.vue')
        },
        {
          path: 'room',
          name: 'OwnerRoom',
          component: () => import('@/views/owner/RoomView.vue')
        },
        {
          path: 'profile',
          name: 'OwnerProfile',
          component: () => import('@/views/owner/ProfileView.vue')
        }
      ]
    },
    {
      path: '/admin',
      name: 'AdminLayout',
      component: () => import('@/layout/AdminLayout.vue'),
      children: [
        {
          path: 'dashboard',
          name: 'AdminDashboard',
          component: () => import('@/views/admin/DashboardView.vue')
        },
        {
          path: 'user',
          name: 'AdminUser',
          component: () => import('@/views/admin/UserView.vue')
        },
        {
          path: 'community',
          name: 'AdminCommunity',
          component: () => import('@/views/admin/CommunityView.vue')
        },
        {
          path: 'fee',
          name: 'AdminFee',
          component: () => import('@/views/admin/FeeView.vue')
        },
        {
          path: 'repair',
          name: 'AdminRepair',
          component: () => import('@/views/admin/RepairView.vue')
        },
        {
          path: 'announcement',
          name: 'AdminAnnouncement',
          component: () => import('@/views/admin/AnnouncementView.vue')
        },
        {
          path: 'log',
          name: 'AdminLog',
          component: () => import('@/views/admin/LogView.vue')
        }
      ]
    }
  ]
})

export default router