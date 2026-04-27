<template>
  <view class="empty-state">
    <image class="empty-state__image" :src="imageSrc" mode="aspectFit" />
    <text class="empty-state__text">{{ text }}</text>
    <view v-if="showButton" class="empty-state__btn" @click="$emit('click')">
      <text class="empty-state__btn-text">{{ buttonText }}</text>
    </view>
  </view>
</template>

<script>
export default {
  name: 'EmptyState',
  props: {
    text: {
      type: String,
      default: '暂无数据'
    },
    imageSrc: {
      type: String,
      default: '/static/empty.png'
    },
    showButton: {
      type: Boolean,
      default: false
    },
    buttonText: {
      type: String,
      default: '去看看'
    }
  }
}
</script>

<style lang="scss" scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80rpx 40rpx;

  &__image {
    width: 280rpx;
    height: 280rpx;
    margin-bottom: 30rpx;
  }

  &__text {
    font-size: 28rpx;
    color: #999;
    margin-bottom: 30rpx;
  }

  &__btn {
    padding: 16rpx 60rpx;
    background-color: #2979ff;
    border-radius: 40rpx;

    &-text {
      font-size: 28rpx;
      color: #fff;
    }
  }
}
</style>
