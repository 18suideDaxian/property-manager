<template>
  <view class="announcement-page">
    <view v-if="announcements.length > 0" class="announcement-list">
      <view
        v-for="item in announcements"
        :key="item.id"
        class="announcement-item card"
        @click="goDetail(item.id)"
      >
        <view class="announcement-item__header">
          <view v-if="item.top" class="announcement-item__tag">置顶</view>
          <view v-if="item.type" class="announcement-item__type-tag">{{ item.typeName || '通知' }}</view>
          <text class="announcement-item__title">{{ item.title }}</text>
        </view>
        <text class="announcement-item__summary">{{ item.summary || item.content }}</text>
        <view class="announcement-item__footer">
          <text class="announcement-item__time">{{ item.createTime }}</text>
          <u-icon name="arrow-right" color="#ccc" size="24" />
        </view>
      </view>
    </view>

    <empty-state v-else text="暂无公告" />

    <u-loadmore v-if="announcements.length > 0" :status="loadStatus" />
  </view>
</template>

<script>
import { getAnnouncements, markAsRead } from '../../api/announcement.js'
import EmptyState from '../../components/empty-state/empty-state.vue'

export default {
  components: { EmptyState },
  data() {
    return {
      announcements: [],
      page: 1,
      pageSize: 10,
      loadStatus: 'loadmore',
      hasMore: true
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
      return this.loadList()
    },
    async loadList() {
      this.loadStatus = 'loading'
      try {
        const res = await getAnnouncements({ page: this.page, pageSize: this.pageSize })
        const list = res.records || res.list || res || []
        if (this.page === 1) {
          this.announcements = list
        } else {
          this.announcements = [...this.announcements, ...list]
        }
        this.hasMore = list.length >= this.pageSize
        this.loadStatus = this.hasMore ? 'loadmore' : 'nomore'
      } catch (e) {
        this.loadStatus = 'loadmore'
      }
    },
    loadMore() {
      this.page++
      this.loadList()
    },
    goDetail(id) {
      // 标记已读
      markAsRead(id).catch(() => {})
      uni.navigateTo({ url: '/pages/announcement-detail/announcement-detail?id=' + id })
    }
  }
}
</script>

<style lang="scss" scoped>
.announcement-page {
  padding: 20rpx 30rpx;
  padding-bottom: 30rpx;
}

.announcement-list {
  .announcement-item {
    margin-bottom: 20rpx;

    &__header {
      display: flex;
      align-items: center;
      margin-bottom: 16rpx;
    }

    &__tag {
      font-size: 20rpx;
      color: #fff;
      background: #fa3534;
      padding: 4rpx 12rpx;
      border-radius: 6rpx;
      margin-right: 12rpx;
      flex-shrink: 0;
    }

    &__type-tag {
      font-size: 20rpx;
      color: #2979ff;
      background: #e6f0ff;
      padding: 4rpx 12rpx;
      border-radius: 6rpx;
      margin-right: 12rpx;
      flex-shrink: 0;
    }

    &__title {
      font-size: 30rpx;
      font-weight: bold;
      color: #333;
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    &__summary {
      font-size: 26rpx;
      color: #666;
      line-height: 1.6;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
      margin-bottom: 16rpx;
    }

    &__footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    &__time {
      font-size: 24rpx;
      color: #999;
    }
  }
}
</style>
