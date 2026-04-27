<template>
  <view class="edit-name-page">
    <view class="edit-name card">
      <text class="edit-name__label">当前昵称</text>
      <view class="edit-name__input-wrap">
        <input
          class="edit-name__input"
          v-model="name"
          placeholder="请输入昵称"
          maxlength="20"
          focus
        />
        <u-icon
          v-if="name"
          name="close-circle-fill"
          color="#ccc"
          size="36"
          @click="name = ''"
        />
      </view>
      <text class="edit-name__tip">昵称长度为2-20个字符</text>
    </view>

    <view class="edit-name__actions">
      <u-button
        type="primary"
        shape="circle"
        :loading="saving"
        :disabled="!canSave"
        @click="saveName"
      >
        保存
      </u-button>
    </view>
  </view>
</template>

<script>
import { updateName } from '../../api/profile.js'
import { getUserInfo, setUserInfo } from '../../utils/auth.js'

export default {
  data() {
    return {
      name: '',
      saving: false
    }
  },
  computed: {
    canSave() {
      return this.name.trim().length >= 2 && this.name.trim().length <= 20 && !this.saving
    }
  },
  onLoad(options) {
    if (options.name) {
      this.name = decodeURIComponent(options.name)
    }
  },
  methods: {
    async saveName() {
      if (!this.canSave) return
      this.saving = true
      try {
        await updateName(this.name.trim())
        // 更新本地缓存
        const user = getUserInfo()
        if (user) {
          user.name = this.name.trim()
          setUserInfo(user)
        }
        uni.showToast({ title: '修改成功', icon: 'success' })
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        // 错误已处理
      } finally {
        this.saving = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-name-page {
  padding: 20rpx 30rpx;
}

.edit-name {
  &__label {
    display: block;
    font-size: 28rpx;
    color: #666;
    margin-bottom: 16rpx;
  }

  &__input-wrap {
    display: flex;
    align-items: center;
    border-bottom: 2rpx solid #2979ff;
    padding-bottom: 16rpx;
  }

  &__input {
    flex: 1;
    font-size: 34rpx;
    color: #333;
  }

  &__tip {
    display: block;
    font-size: 24rpx;
    color: #999;
    margin-top: 16rpx;
  }

  &__actions {
    margin-top: 60rpx;
  }
}
</style>
