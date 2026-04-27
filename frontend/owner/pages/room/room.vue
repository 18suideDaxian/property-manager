<template>
  <view class="room-page">
    <view v-if="rooms.length > 0" class="room-list">
      <view v-for="room in rooms" :key="room.id" class="room-card card">
        <view class="room-card__header">
          <u-icon name="home-fill" color="#2979ff" size="40" />
          <text class="room-card__name">{{ room.communityName }}</text>
        </view>
        <view class="room-card__info">
          <view class="room-card__row">
            <text class="room-card__label">楼栋</text>
            <text class="room-card__value">{{ room.building }}栋</text>
          </view>
          <view class="room-card__row">
            <text class="room-card__label">单元</text>
            <text class="room-card__value">{{ room.unit }}单元</text>
          </view>
          <view class="room-card__row">
            <text class="room-card__label">房号</text>
            <text class="room-card__value">{{ room.roomNo }}</text>
          </view>
          <view class="room-card__row">
            <text class="room-card__label">面积</text>
            <text class="room-card__value">{{ room.area }}m²</text>
          </view>
          <view class="room-card__row">
            <text class="room-card__label">户型</text>
            <text class="room-card__value">{{ room.roomType }}</text>
          </view>
        </view>
        <view class="room-card__actions">
          <u-button size="mini" type="primary" plain @click="viewBills(room)">查看缴费</u-button>
          <u-button size="mini" type="success" plain @click="goRepair(room)">我要报修</u-button>
        </view>
      </view>
    </view>

    <empty-state v-else text="暂无房产信息" />
  </view>
</template>

<script>
import { getMyRooms } from '../../api/room.js'
import EmptyState from '../../components/empty-state/empty-state.vue'

export default {
  components: { EmptyState },
  data() {
    return {
      rooms: []
    }
  },
  onShow() {
    this.loadRooms()
  },
  methods: {
    async loadRooms() {
      try {
        uni.showLoading({ title: '加载中...' })
        const res = await getMyRooms()
        this.rooms = res || []
      } catch (e) {
        console.error('加载房产失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    viewBills(room) {
      uni.navigateTo({ url: '/pages/my-bills/my-bills?roomId=' + room.id })
    },
    goRepair(room) {
      uni.navigateTo({ url: '/pages/repair/repair?roomId=' + room.id })
    }
  }
}
</script>

<style lang="scss" scoped>
.room-page {
  padding: 20rpx 30rpx;
  padding-bottom: 30rpx;
}

.room-card {
  margin-bottom: 24rpx;

  &__header {
    display: flex;
    align-items: center;
    margin-bottom: 24rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid #f0f0f0;
  }

  &__name {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-left: 16rpx;
  }

  &__info {
    margin-bottom: 24rpx;
  }

  &__row {
    display: flex;
    justify-content: space-between;
    padding: 12rpx 0;
  }

  &__label {
    font-size: 26rpx;
    color: #999;
  }

  &__value {
    font-size: 26rpx;
    color: #333;
  }

  &__actions {
    display: flex;
    gap: 20rpx;
    padding-top: 20rpx;
    border-top: 1rpx solid #f0f0f0;
  }
}
</style>
