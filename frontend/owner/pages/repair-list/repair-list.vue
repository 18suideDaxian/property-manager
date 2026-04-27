<template>
  <view class="repair-list-page">
    <!-- 状态筛选 -->
    <view class="repair-tabs">
      <view
        v-for="tab in tabs"
        :key="tab.value"
        class="repair-tabs__item"
        :class="{ 'repair-tabs__item--active': currentTab === tab.value }"
        @click="switchTab(tab.value)"
      >
        <text>{{ tab.label }}</text>
      </view>
    </view>

    <!-- 报修列表 -->
    <view v-if="repairs.length > 0" class="repair-list">
      <view v-for="item in repairs" :key="item.id" class="repair-item card" @click="goDetail(item.id)">
        <view class="repair-item__header">
          <text class="repair-item__type">{{ getRepairTypeLabel(item.repairType) }}</text>
          <view class="repair-item__status" :class="'repair-item__status--' + item.status">
            <text>{{ getStatusLabel(item.status) }}</text>
          </view>
        </view>
        <text class="repair-item__desc text-ellipsis">{{ item.description }}</text>
        <view class="repair-item__footer">
          <text class="repair-item__room">{{ item.roomName }}</text>
          <text class="repair-item__time">{{ item.createTime }}</text>
        </view>
        <view v-if="item.images && item.images.length > 0" class="repair-item__images">
          <image
            v-for="(img, idx) in item.images.slice(0, 3)"
            :key="idx"
            :src="img"
            mode="aspectFill"
            class="repair-item__img"
            @click.stop="previewImages(item.images, idx)"
          />
          <view v-if="item.images.length > 3" class="repair-item__img-more">
            <text>+{{ item.images.length - 3 }}</text>
          </view>
        </view>
      </view>
    </view>

    <empty-state v-else text="暂无报修记录" />

    <u-loadmore v-if="repairs.length > 0" :status="loadStatus" />
  </view>
</template>

<script>
import { getMyRepairs } from '../../api/repair.js'
import EmptyState from '../../components/empty-state/empty-state.vue'

export default {
  components: { EmptyState },
  data() {
    return {
      repairs: [],
      currentTab: 'ALL',
      tabs: [
        { label: '全部', value: 'ALL' },
        { label: '待处理', value: 'PENDING' },
        { label: '处理中', value: 'PROCESSING' },
        { label: '已完成', value: 'COMPLETED' }
      ],
      page: 1,
      pageSize: 10,
      loadStatus: 'loadmore',
      hasMore: true,
      typeMap: {
        'WATER_ELECTRIC': '水电维修',
        'DOOR_WINDOW': '门窗维修',
        'APPLIANCE': '家电故障',
        'PIPELINE': '管道疏通',
        'OTHER': '其他'
      },
      statusMap: {
        'PENDING': '待处理',
        'PROCESSING': '处理中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
    }
  },
  onShow() {
    this.resetAndLoad()
  },
  onPullDownRefresh() {
    this.resetAndLoad().finally(() => {
      uni.stopPullDownRefresh()
    })
  },
  onReachBottom() {
    if (this.hasMore && this.loadStatus !== 'loading') {
      this.loadMore()
    }
  },
  methods: {
    resetAndLoad() {
      this.page = 1
      this.hasMore = true
      this.loadStatus = 'loadmore'
      return this.loadRepairs()
    },
    async loadRepairs() {
      this.loadStatus = 'loading'
      try {
        const params = { page: this.page, pageSize: this.pageSize }
        if (this.currentTab !== 'ALL') params.status = this.currentTab
        const res = await getMyRepairs(params)
        const list = res.records || res.list || res || []
        if (this.page === 1) {
          this.repairs = list
        } else {
          this.repairs = [...this.repairs, ...list]
        }
        this.hasMore = list.length >= this.pageSize
        this.loadStatus = this.hasMore ? 'loadmore' : 'nomore'
      } catch (e) {
        this.loadStatus = 'loadmore'
      }
    },
    switchTab(tab) {
      if (this.currentTab === tab) return
      this.currentTab = tab
      this.resetAndLoad()
    },
    loadMore() {
      this.page++
      this.loadRepairs()
    },
    getRepairTypeLabel(type) {
      return this.typeMap[type] || type
    },
    getStatusLabel(status) {
      return this.statusMap[status] || status
    },
    goDetail(id) {
      // 可扩展为详情页
      uni.showToast({ title: '详情页开发中', icon: 'none' })
    },
    previewImages(images, index) {
      uni.previewImage({ current: index, urls: images })
    }
  }
}
</script>

<style lang="scss" scoped>
.repair-list-page {
  padding-bottom: 30rpx;
}

.repair-tabs {
  display: flex;
  background: #fff;
  padding: 10rpx 20rpx;
  margin-bottom: 20rpx;

  &__item {
    flex: 1;
    text-align: center;
    padding: 16rpx 0;
    font-size: 26rpx;
    color: #666;
    position: relative;

    &--active {
      color: #2979ff;
      font-weight: bold;

      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 40rpx;
        height: 6rpx;
        background: #2979ff;
        border-radius: 3rpx;
      }
    }
  }
}

.repair-list {
  padding: 0 30rpx;
}

.repair-item {
  margin-bottom: 20rpx;

  &__header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
  }

  &__type {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
  }

  &__status {
    padding: 6rpx 16rpx;
    border-radius: 6rpx;
    font-size: 22rpx;

    &--PENDING {
      background: #fff3e6;
      color: #ff9900;
    }

    &--PROCESSING {
      background: #e6f0ff;
      color: #2979ff;
    }

    &--COMPLETED {
      background: #e6ffe6;
      color: #19be6b;
    }

    &--CANCELLED {
      background: #f5f5f5;
      color: #999;
    }
  }

  &__desc {
    font-size: 26rpx;
    color: #666;
    margin-bottom: 16rpx;
    display: block;
  }

  &__footer {
    display: flex;
    justify-content: space-between;
    margin-bottom: 16rpx;
  }

  &__room, &__time {
    font-size: 24rpx;
    color: #999;
  }

  &__images {
    display: flex;
    gap: 12rpx;
    padding-top: 16rpx;
    border-top: 1rpx solid #f5f5f5;
  }

  &__img {
    width: 160rpx;
    height: 160rpx;
    border-radius: 8rpx;
  }

  &__img-more {
    width: 160rpx;
    height: 160rpx;
    border-radius: 8rpx;
    background: rgba(0, 0, 0, 0.4);
    display: flex;
    justify-content: center;
    align-items: center;
    color: #fff;
    font-size: 28rpx;
  }
}
</style>
