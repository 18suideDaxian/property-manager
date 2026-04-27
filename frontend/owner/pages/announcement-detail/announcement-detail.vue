<template>
  <view class="detail-page">
    <view v-if="detail" class="detail-content">
      <view class="detail-header">
        <text class="detail-header__title">{{ detail.title }}</text>
        <view class="detail-header__meta">
          <text class="detail-header__time">{{ detail.createTime }}</text>
          <text v-if="detail.author" class="detail-header__author">{{ detail.author }}</text>
        </view>
      </view>
      <view class="detail-body">
        <rich-text :nodes="detail.content" />
      </view>
    </view>
    <view v-else class="detail-loading">
      <u-loading mode="circle" size="48" />
      <text class="detail-loading__text">加载中...</text>
    </view>
  </view>
</template>

<script>
import { getAnnouncementDetail } from '../../api/announcement.js'

export default {
  data() {
    return {
      id: '',
      detail: null
    }
  },
  onLoad(options) {
    this.id = options.id
    this.loadDetail()
  },
  methods: {
    async loadDetail() {
      try {
        uni.showLoading({ title: '加载中...' })
        const res = await getAnnouncementDetail(this.id)
        this.detail = res
        if (res.title) {
          uni.setNavigationBarTitle({ title: res.title })
        }
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-page {
  min-height: 100vh;
  background: #fff;
}

.detail-content {
  padding: 30rpx;
}

.detail-header {
  padding-bottom: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
  margin-bottom: 30rpx;

  &__title {
    display: block;
    font-size: 38rpx;
    font-weight: bold;
    color: #333;
    line-height: 1.5;
    margin-bottom: 20rpx;
  }

  &__meta {
    display: flex;
    align-items: center;
    gap: 20rpx;
  }

  &__time, &__author {
    font-size: 24rpx;
    color: #999;
  }
}

.detail-body {
  font-size: 30rpx;
  color: #333;
  line-height: 1.8;
}

.detail-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 0;

  &__text {
    font-size: 26rpx;
    color: #999;
    margin-top: 20rpx;
  }
}
</style>
