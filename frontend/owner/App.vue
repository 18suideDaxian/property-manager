<script>
import { isLoggedIn } from './utils/auth.js'

export default {
  onLaunch() {
    console.log('物业业主端 App Launch')
    // 检查登录状态
    if (!isLoggedIn()) {
      // 未登录跳转到登录页（可选：部分页面允许未登录访问）
    }
  },
  onShow() {
    console.log('物业业主端 App Show')
  },
  onHide() {
    console.log('物业业主端 App Hide')
  }
}
</script>

<style lang="scss">
/* 引入 uView 基础样式 */
@import "uview-ui/index.scss";

/* 全局样式 */
page {
  background-color: #f5f6fa;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  font-size: 28rpx;
  color: #333;
}

/* 通用容器 */
.container {
  padding: 20rpx 30rpx;
}

/* 安全区域底部 */
.safe-bottom {
  padding-bottom: env(safe-area-inset-bottom);
}

/* 分割线 */
.divider {
  height: 1rpx;
  background-color: #eee;
  margin: 20rpx 0;
}

/* 文字省略 */
.text-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* flex 布局 */
.flex-row {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.flex-between {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.flex-center {
  display: flex;
  justify-content: center;
  align-items: center;
}

.flex-1 {
  flex: 1;
}

/* 卡片样式 */
.card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

/* 页面标题 */
.page-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 30rpx;
}
</style>
