<template>
  <view class="index-page">
    <!-- 顶部用户信息 -->
    <view class="index-header">
      <view class="index-header__user">
        <u-avatar :src="userInfo.avatar || '/static/default-avatar.png'" size="80" />
        <view class="index-header__info">
          <text class="index-header__name">{{ userInfo.name || '业主' }}</text>
          <text class="index-header__room">{{ mainRoom || '暂无房产' }}</text>
        </view>
      </view>
    </view>

    <!-- 待缴费用提醒 -->
    <view v-if="unpaidAmount > 0" class="index-notice" @click="goTo('/pages/fee/fee')">
      <u-icon name="bell-fill" color="#ff9900" size="36" />
      <text class="index-notice__text">您有 {{ unpaidAmount.toFixed(2) }} 元待缴费用</text>
      <u-icon name="arrow-right" color="#999" size="28" />
    </view>

    <!-- 功能入口 -->
    <view class="index-grid">
      <view class="index-grid__item" @click="goTo('/pages/fee/fee')">
        <view class="index-grid__icon-wrap" style="background: #e6f0ff">
          <u-icon name="rmb-circle" color="#2979ff" size="52" />
        </view>
        <text class="index-grid__text">物业缴费</text>
      </view>
      <view class="index-grid__item" @click="goTo('/pages/repair/repair')">
        <view class="index-grid__icon-wrap" style="background: #fff3e6">
          <u-icon name="edit-pen" color="#ff9900" size="52" />
        </view>
        <text class="index-grid__text">在线报修</text>
      </view>
      <view class="index-grid__item" @click="goTo('/pages/my-rooms/my-rooms')">
        <view class="index-grid__icon-wrap" style="background: #e6ffe6">
          <u-icon name="home" color="#19be6b" size="52" />
        </view>
        <text class="index-grid__text">我的房产</text>
      </view>
      <view class="index-grid__item" @click="goTo('/pages/my-repairs/my-repairs')">
        <view class="index-grid__icon-wrap" style="background: #ffe6e6">
          <u-icon name="list" color="#fa3534" size="52" />
        </view>
        <text class="index-grid__text">报修进度</text>
      </view>
    </view>

    <!-- 最新公告 -->
    <view class="card">
      <view class="flex-between" style="margin-bottom: 20rpx">
        <text class="card__title">最新公告</text>
        <text class="card__more" @click="goTo('/pages/announcement/announcement')">更多 ></text>
      </view>
      <view v-if="announcements.length > 0">
        <view
          v-for="item in announcements"
          :key="item.id"
          class="announcement-item"
          @click="goToDetail(item.id)"
        >
          <view class="announcement-item__tag" v-if="item.top">置顶</view>
          <text class="announcement-item__title text-ellipsis">{{ item.title }}</text>
          <text class="announcement-item__time">{{ item.createTime }}</text>
        </view>
      </view>
      <view v-else style="text-align: center; padding: 40rpx 0">
        <text style="color: #999; font-size: 26rpx">暂无公告</text>
      </view>
    </view>

    <!-- 我的房产概览 -->
    <view class="card" v-if="rooms.length > 0">
      <view class="flex-between" style="margin-bottom: 20rpx">
        <text class="card__title">我的房产</text>
        <text class="card__more" @click="goTo('/pages/my-rooms/my-rooms')">更多 ></text>
      </view>
      <view
        v-for="room in rooms.slice(0, 2)"
        :key="room.id"
        class="room-card"
        @click="goTo('/pages/my-rooms/my-rooms')"
      >
        <view class="room-card__info">
          <text class="room-card__name">{{ room.communityName }} {{ room.building }}栋{{ room.unit }}单元{{ room.roomNo }}</text>
          <text class="room-card__area">{{ room.area }}m² · {{ room.roomType }}</text>
        </view>
        <u-icon name="arrow-right" color="#ccc" size="28" />
      </view>
    </view>
  </view>
</template>

<script>
import { getLatestAnnouncements } from '../../api/announcement.js'
import { getMyRooms } from '../../api/room.js'
import { getUnpaidSummary } from '../../api/fee.js'
import { isLoggedIn, getUserInfo } from '../../utils/auth.js'

export default {
  data() {
    return {
      userInfo: {},
      announcements: [],
      rooms: [],
      unpaidAmount: 0,
      mainRoom: ''
    }
  },
  onShow() {
    if (!isLoggedIn()) {
      uni.reLaunch({ url: '/pages/login/login' })
      return
    }
    this.loadData()
  },
  onPullDownRefresh() {
    this.loadData().finally(() => {
      uni.stopPullDownRefresh()
    })
  },
  methods: {
    async loadData() {
      const info = getUserInfo()
      if (info) this.userInfo = info

      try {
        const [annRes, roomRes, unpaidRes] = await Promise.allSettled([
          getLatestAnnouncements(5),
          getMyRooms(),
          getUnpaidSummary()
        ])

        if (annRes.status === 'fulfilled') {
          this.announcements = annRes.value || []
        }
        if (roomRes.status === 'fulfilled') {
          this.rooms = roomRes.value || []
          if (this.rooms.length > 0) {
            const r = this.rooms[0]
            this.mainRoom = r.communityName + ' ' + r.building + '栋' + r.unit + '单元' + r.roomNo
          }
        }
        if (unpaidRes.status === 'fulfilled') {
          this.unpaidAmount = unpaidRes.value?.totalAmount || 0
        }
      } catch (e) {
        console.error('加载首页数据失败', e)
      }
    },
    goTo(url) {
      uni.navigateTo({ url })
    },
    goToDetail(id) {
      uni.navigateTo({ url: '/pages/announcement-detail/announcement-detail?id=' + id })
    }
  }
}
</script>

<style lang="scss" scoped>
.index-page {
  padding-bottom: 30rpx;
}

.index-header {
  background: linear-gradient(135deg, #2979ff, #1a5cd7);
  padding: 40rpx 30rpx 50rpx;
  margin-bottom: -20rpx;

  &__user {
    display: flex;
    align-items: center;
  }

  &__info {
    margin-left: 24rpx;
  }

  &__name {
    display: block;
    font-size: 34rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 8rpx;
  }

  &__room {
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.8);
  }
}

.index-notice {
  display: flex;
  align-items: center;
  background: #fffbe6;
  margin: 0 30rpx 20rpx;
  padding: 24rpx 30rpx;
  border-radius: 12rpx;

  &__text {
    flex: 1;
    margin-left: 16rpx;
    font-size: 26rpx;
    color: #ff9900;
  }
}

.index-grid {
  display: flex;
  flex-wrap: wrap;
  padding: 20rpx 30rpx;
  background: #fff;
  margin: 0 30rpx 20rpx;
  border-radius: 16rpx;

  &__item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx 0;
  }

  &__icon-wrap {
    width: 100rpx;
    height: 100rpx;
    border-radius: 24rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 16rpx;
  }

  &__text {
    font-size: 24rpx;
    color: #333;
  }
}

.card {
  background: #fff;
  margin: 0 30rpx 20rpx;
  padding: 30rpx;
  border-radius: 16rpx;

  &__title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
  }

  &__more {
    font-size: 24rpx;
    color: #999;
  }
}

.announcement-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f5f5f5;

  &:last-child {
    border-bottom: none;
  }

  &__tag {
    font-size: 20rpx;
    color: #fff;
    background: #fa3534;
    padding: 4rpx 12rpx;
    border-radius: 6rpx;
    margin-right: 16rpx;
    flex-shrink: 0;
  }

  &__title {
    flex: 1;
    font-size: 28rpx;
    color: #333;
  }

  &__time {
    font-size: 24rpx;
    color: #999;
    margin-left: 20rpx;
    flex-shrink: 0;
  }
}

.room-card {
  display: flex;
  align-items: center;
  padding: 24rpx;
  background: #f7f8fa;
  border-radius: 12rpx;
  margin-bottom: 16rpx;

  &:last-child {
    margin-bottom: 0;
  }

  &__info {
    flex: 1;
  }

  &__name {
    display: block;
    font-size: 28rpx;
    color: #333;
    font-weight: 500;
    margin-bottom: 8rpx;
  }

  &__area {
    font-size: 24rpx;
    color: #999;
  }
}
</style>
