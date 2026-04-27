<template>
  <view class="login-page">
    <view class="login-page__logo">
      <image class="login-page__logo-img" src="/static/logo.png" mode="aspectFit" />
      <text class="login-page__logo-title">智慧物业</text>
      <text class="login-page__logo-desc">让生活更美好</text>
    </view>

    <phone-login @login-success="onLoginSuccess" />
  </view>
</template>

<script>
import PhoneLogin from '../../components/phone-login/phone-login.vue'

export default {
  components: { PhoneLogin },
  data() {
    return {}
  },
  methods: {
    onLoginSuccess(res) {
      console.log('登录成功', res)
    }
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  background-color: #fff;

  &__logo {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 120rpx;
    padding-bottom: 40rpx;
  }

  &__logo-img {
    width: 160rpx;
    height: 160rpx;
    margin-bottom: 24rpx;
  }

  &__logo-title {
    font-size: 44rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 12rpx;
  }

  &__logo-desc {
    font-size: 26rpx;
    color: #999;
  }
}
</style>
