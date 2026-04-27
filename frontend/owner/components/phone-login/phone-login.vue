<template>
  <view class="phone-login">
    <view class="phone-login__header">
      <text class="phone-login__title">手机号登录</text>
      <text class="phone-login__subtitle">未注册的手机号验证后将自动注册</text>
    </view>

    <view class="phone-login__form">
      <!-- 手机号 -->
      <view class="phone-login__input-wrap">
        <text class="phone-login__prefix">+86</text>
        <input
          class="phone-login__input"
          type="number"
          v-model="phone"
          placeholder="请输入手机号"
          maxlength="11"
          @input="onPhoneInput"
        />
        <u-icon
          v-if="phone"
          name="close-circle-fill"
          color="#ccc"
          size="36"
          @click="phone = ''"
        />
      </view>

      <!-- 验证码 -->
      <view class="phone-login__input-wrap">
        <input
          class="phone-login__input"
          type="number"
          v-model="code"
          placeholder="请输入验证码"
          maxlength="6"
        />
        <view
          class="phone-login__sms-btn"
          :class="{ 'phone-login__sms-btn--disabled': countdown > 0 || !phoneValid }"
          @click="sendCode"
        >
          <text class="phone-login__sms-text">
            {{ countdown > 0 ? countdown + 's' : '获取验证码' }}
          </text>
        </view>
      </view>
    </view>

    <view class="phone-login__actions">
      <u-button
        type="primary"
        shape="circle"
        :loading="loading"
        :disabled="!canSubmit"
        @click="handleLogin"
      >
        登录 / 注册
      </u-button>
    </view>

    <view class="phone-login__agreement">
      <u-checkbox v-model="agreed" shape="circle" icon-size="24">
        <text class="phone-login__agreement-text">
          我已阅读并同意
          <text class="phone-login__link" @click.stop="openAgreement('user')">《用户协议》</text>
          和
          <text class="phone-login__link" @click.stop="openAgreement('privacy')">《隐私政策》</text>
        </text>
      </u-checkbox>
    </view>
  </view>
</template>

<script>
import { sendSmsCode, loginByPhone, register } from '../../api/auth.js'
import { setToken, setUserInfo } from '../../utils/auth.js'

export default {
  name: 'PhoneLogin',
  data() {
    return {
      phone: '',
      code: '',
      countdown: 0,
      timer: null,
      loading: false,
      agreed: false
    }
  },
  computed: {
    phoneValid() {
      return /^1[3-9]\d{9}$/.test(this.phone)
    },
    canSubmit() {
      return this.phoneValid && this.code.length >= 4 && this.agreed && !this.loading
    }
  },
  beforeDestroy() {
    if (this.timer) clearInterval(this.timer)
  },
  methods: {
    onPhoneInput() {
      this.phone = this.phone.replace(/[^\d]/g, '').slice(0, 11)
    },
    async sendCode() {
      if (this.countdown > 0 || !this.phoneValid) return
      try {
        await sendSmsCode(this.phone)
        uni.showToast({ title: '验证码已发送', icon: 'success' })
        this.countdown = 60
        this.timer = setInterval(() => {
          this.countdown--
          if (this.countdown <= 0) {
            clearInterval(this.timer)
            this.timer = null
          }
        }, 1000)
      } catch (e) {
        // 错误已由 request.js 处理
      }
    },
    async handleLogin() {
      if (!this.canSubmit) return
      this.loading = true
      try {
        const res = await loginByPhone(this.phone, this.code)
        // 保存 token 和用户信息
        setToken(res.token)
        if (res.userInfo) {
          setUserInfo(res.userInfo)
        }
        uni.showToast({ title: '登录成功', icon: 'success' })
        this.$emit('login-success', res)
        // 跳转首页
        setTimeout(() => {
          uni.switchTab({ url: '/pages/index/index' })
        }, 1000)
      } catch (e) {
        // 错误已由 request.js 处理
      } finally {
        this.loading = false
      }
    },
    openAgreement(type) {
      const url = type === 'user' ? '/pages/agreement/user' : '/pages/agreement/privacy'
      // 可跳转到协议页面或弹窗展示
      uni.showToast({ title: '协议页面开发中', icon: 'none' })
    }
  }
}
</script>

<style lang="scss" scoped>
.phone-login {
  padding: 60rpx 50rpx;

  &__header {
    margin-bottom: 60rpx;
  }

  &__title {
    display: block;
    font-size: 48rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 16rpx;
  }

  &__subtitle {
    font-size: 26rpx;
    color: #999;
  }

  &__form {
    margin-bottom: 40rpx;
  }

  &__input-wrap {
    display: flex;
    align-items: center;
    border-bottom: 1rpx solid #eee;
    padding: 24rpx 0;
    margin-bottom: 10rpx;
  }

  &__prefix {
    font-size: 32rpx;
    color: #333;
    margin-right: 20rpx;
    font-weight: 500;
  }

  &__input {
    flex: 1;
    font-size: 32rpx;
    color: #333;
  }

  &__sms-btn {
    padding: 10rpx 24rpx;
    border-left: 1rpx solid #eee;
    margin-left: 20rpx;

    &--disabled {
      opacity: 0.5;
    }
  }

  &__sms-text {
    font-size: 26rpx;
    color: #2979ff;
    white-space: nowrap;
  }

  &__actions {
    margin-top: 50rpx;
  }

  &__agreement {
    margin-top: 40rpx;
    display: flex;
    justify-content: center;
  }

  &__agreement-text {
    font-size: 24rpx;
    color: #999;
  }

  &__link {
    color: #2979ff;
  }
}
</style>
