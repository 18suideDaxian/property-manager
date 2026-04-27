<template>
  <view class="profile-page">
    <!-- 顶部用户信息 -->
    <view class="profile-header">
      <u-avatar :src="userInfo.avatar || '/static/default-avatar.png'" size="120" />
      <view class="profile-header__info">
        <text class="profile-header__name">{{ userInfo.name || '业主' }}</text>
        <text class="profile-header__phone">{{ maskedPhone }}</text>
      </view>
      <u-icon name="arrow-right" color="rgba(255,255,255,0.7)" size="28" @click="goEditName" />
    </view>

    <!-- 菜单列表 -->
    <view class="profile-menu card">
      <view class="profile-menu__item" @click="goTo('/pages/my-rooms/my-rooms')">
        <view class="profile-menu__left">
          <u-icon name="home" color="#2979ff" size="40" />
          <text class="profile-menu__text">我的房产</text>
        </view>
        <view class="profile-menu__right">
          <text class="profile-menu__badge" v-if="roomCount > 0">{{ roomCount }}</text>
          <u-icon name="arrow-right" color="#ccc" size="28" />
        </view>
      </view>

      <view class="profile-menu__item" @click="goTo('/pages/my-bills/my-bills')">
        <view class="profile-menu__left">
          <u-icon name="rmb-circle" color="#ff9900" size="40" />
          <text class="profile-menu__text">我的缴费</text>
        </view>
        <view class="profile-menu__right">
          <text class="profile-menu__badge" v-if="unpaidCount > 0">{{ unpaidCount }}</text>
          <u-icon name="arrow-right" color="#ccc" size="28" />
        </view>
      </view>

      <view class="profile-menu__item" @click="goTo('/pages/my-repairs/my-repairs')">
        <view class="profile-menu__left">
          <u-icon name="edit-pen" color="#19be6b" size="40" />
          <text class="profile-menu__text">我的报修</text>
        </view>
        <view class="profile-menu__right">
          <u-icon name="arrow-right" color="#ccc" size="28" />
        </view>
      </view>
    </view>

    <view class="profile-menu card">
      <view class="profile-menu__item" @click="goEditName">
        <view class="profile-menu__left">
          <u-icon name="account" color="#909399" size="40" />
          <text class="profile-menu__text">修改昵称</text>
        </view>
        <u-icon name="arrow-right" color="#ccc" size="28" />
      </view>

      <view class="profile-menu__item" @click="handleLogout">
        <view class="profile-menu__left">
          <u-icon name="logout" color="#fa3534" size="40" />
          <text class="profile-menu__text" style="color: #fa3534">退出登录</text>
        </view>
        <u-icon name="arrow-right" color="#ccc" size="28" />
      </view>
    </view>
  </view>
</template>

<script>
import { getProfile } from '../../api/profile.js'
import { getUserInfo, clearAuth } from '../../utils/auth.js'
import { logout } from '../../api/auth.js'

export default {
  data() {
    return {
      userInfo: {},
      roomCount: 0,
      unpaidCount: 0
    }
  },
  computed: {
    maskedPhone() {
      const phone = this.userInfo.phone || ''
      if (phone.length === 11) {
        return phone.slice(0, 3) + '****' + phone.slice(7)
      }
      return phone
    }
  },
  onShow() {
    this.loadProfile()
  },
  methods: {
    async loadProfile() {
      // 先读缓存
      const cached = getUserInfo()
      if (cached) this.userInfo = cached

      try {
        const res = await getProfile()
        this.userInfo = res || {}
        this.roomCount = res?.roomCount || 0
        this.unpaidCount = res?.unpaidBillCount || 0
      } catch (e) {
        console.error('加载个人信息失败', e)
      }
    },
    goTo(url) {
      uni.navigateTo({ url })
    },
    goEditName() {
      uni.navigateTo({ url: '/pages/edit-name/edit-name?name=' + (this.userInfo.name || '') })
    },
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定退出登录吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await logout()
            } catch (e) {
              // 静默
            }
            clearAuth()
            uni.reLaunch({ url: '/pages/login/login' })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.profile-page {
  padding-bottom: 30rpx;
}

.profile-header {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #2979ff, #1a5cd7);
  padding: 60rpx 30rpx 50rpx;

  &__info {
    flex: 1;
    margin-left: 24rpx;
  }

  &__name {
    display: block;
    font-size: 36rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 8rpx;
  }

  &__phone {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.8);
  }
}

.profile-menu {
  margin: 20rpx 30rpx;
  padding: 0;
  overflow: hidden;

  &__item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 30rpx;
    border-bottom: 1rpx solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }
  }

  &__left {
    display: flex;
    align-items: center;
  }

  &__text {
    font-size: 28rpx;
    color: #333;
    margin-left: 20rpx;
  }

  &__right {
    display: flex;
    align-items: center;
  }

  &__badge {
    font-size: 22rpx;
    color: #fff;
    background: #fa3534;
    min-width: 32rpx;
    height: 32rpx;
    line-height: 32rpx;
    text-align: center;
    border-radius: 16rpx;
    padding: 0 10rpx;
    margin-right: 12rpx;
  }
}
</style>
