<template>
  <view class="fee-page">
    <!-- 顶部汇总 -->
    <view class="fee-summary">
      <view class="fee-summary__item">
        <text class="fee-summary__value">{{ unpaidTotal.toFixed(2) }}</text>
        <text class="fee-summary__label">待缴金额(元)</text>
      </view>
      <view class="fee-summary__divider"></view>
      <view class="fee-summary__item">
        <text class="fee-summary__value">{{ paidTotal.toFixed(2) }}</text>
        <text class="fee-summary__label">已缴金额(元)</text>
      </view>
    </view>

    <!-- 筛选标签 -->
    <view class="fee-tabs">
      <view
        v-for="tab in tabs"
        :key="tab.value"
        class="fee-tabs__item"
        :class="{ 'fee-tabs__item--active': currentTab === tab.value }"
        @click="switchTab(tab.value)"
      >
        <text>{{ tab.label }}</text>
      </view>
    </view>

    <!-- 账单列表 -->
    <view v-if="bills.length > 0" class="fee-list">
      <view v-for="bill in bills" :key="bill.id" class="fee-item card" @click="showBillDetail(bill)">
        <view class="fee-item__header">
          <text class="fee-item__type">{{ bill.feeTypeName }}</text>
          <view class="fee-item__status" :class="'fee-item__status--' + bill.status">
            <text>{{ bill.status === 'PAID' ? '已缴' : '待缴' }}</text>
          </view>
        </view>
        <view class="fee-item__body">
          <text class="fee-item__period">{{ bill.period }}</text>
          <text class="fee-item__amount">¥{{ bill.amount.toFixed(2) }}</text>
        </view>
        <view class="fee-item__footer">
          <text class="fee-item__room">{{ bill.roomName }}</text>
          <text class="fee-item__time">{{ bill.createTime }}</text>
        </view>
        <view v-if="bill.status !== 'PAID'" class="fee-item__action">
          <u-button
            type="primary"
            size="mini"
            shape="circle"
            @click.stop="handlePay(bill)"
          >
            立即缴费
          </u-button>
        </view>
      </view>
    </view>

    <empty-state v-else text="暂无账单记录" />

    <!-- 加载更多 -->
    <u-loadmore v-if="bills.length > 0" :status="loadStatus" />
  </view>
</template>

<script>
import { getMyBills, payBill, getUnpaidSummary } from '../../api/fee.js'
import EmptyState from '../../components/empty-state/empty-state.vue'

export default {
  components: { EmptyState },
  data() {
    return {
      bills: [],
      currentTab: 'ALL',
      tabs: [
        { label: '全部', value: 'ALL' },
        { label: '待缴', value: 'UNPAID' },
        { label: '已缴', value: 'PAID' }
      ],
      unpaidTotal: 0,
      paidTotal: 0,
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
      return this.loadBills()
    },
    async loadBills() {
      this.loadStatus = 'loading'
      try {
        const params = {
          page: this.page,
          pageSize: this.pageSize
        }
        if (this.currentTab !== 'ALL') {
          params.status = this.currentTab
        }
        const res = await getMyBills(params)
        const list = res.records || res.list || res || []
        if (this.page === 1) {
          this.bills = list
        } else {
          this.bills = [...this.bills, ...list]
        }
        this.hasMore = list.length >= this.pageSize
        this.loadStatus = this.hasMore ? 'loadmore' : 'nomore'

        // 更新汇总
        this.loadSummary()
      } catch (e) {
        this.loadStatus = 'loadmore'
      }
    },
    async loadSummary() {
      try {
        const res = await getUnpaidSummary()
        this.unpaidTotal = res?.unpaidAmount || 0
        this.paidTotal = res?.paidAmount || 0
      } catch (e) {
        // 静默失败
      }
    },
    switchTab(tab) {
      if (this.currentTab === tab) return
      this.currentTab = tab
      this.resetAndLoad()
    },
    loadMore() {
      this.page++
      this.loadBills()
    },
    async handlePay(bill) {
      uni.showModal({
        title: '确认缴费',
        content: `确认缴纳 ${bill.feeTypeName} ${bill.period} 费用 ¥${bill.amount.toFixed(2)}？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              uni.showLoading({ title: '支付中...' })
              const payRes = await payBill(bill.id, 'WECHAT')
              uni.hideLoading()
              // 调用微信支付
              if (payRes.paymentParams) {
                uni.requestPayment({
                  ...payRes.paymentParams,
                  success: () => {
                    uni.showToast({ title: '支付成功', icon: 'success' })
                    this.resetAndLoad()
                  },
                  fail: () => {
                    uni.showToast({ title: '支付已取消', icon: 'none' })
                  }
                })
              } else {
                uni.showToast({ title: '支付成功', icon: 'success' })
                this.resetAndLoad()
              }
            } catch (e) {
              uni.hideLoading()
            }
          }
        }
      })
    },
    showBillDetail(bill) {
      // 可展开详情或跳转
      uni.showModal({
        title: bill.feeTypeName,
        content: `费用周期：${bill.period}\n金额：¥${bill.amount.toFixed(2)}\n状态：${bill.status === 'PAID' ? '已缴' : '待缴'}\n房产：${bill.roomName || ''}`,
        showCancel: bill.status !== 'PAID',
        confirmText: bill.status === 'PAID' ? '确定' : '去缴费',
        success: (res) => {
          if (res.confirm && bill.status !== 'PAID') {
            this.handlePay(bill)
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.fee-page {
  padding-bottom: 30rpx;
}

.fee-summary {
  display: flex;
  background: linear-gradient(135deg, #2979ff, #1a5cd7);
  padding: 40rpx 0;
  margin-bottom: 20rpx;

  &__item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  &__value {
    font-size: 48rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 8rpx;
  }

  &__label {
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.8);
  }

  &__divider {
    width: 1rpx;
    background: rgba(255, 255, 255, 0.3);
  }
}

.fee-tabs {
  display: flex;
  background: #fff;
  padding: 10rpx 30rpx;
  margin-bottom: 20rpx;

  &__item {
    flex: 1;
    text-align: center;
    padding: 16rpx 0;
    font-size: 28rpx;
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
        width: 48rpx;
        height: 6rpx;
        background: #2979ff;
        border-radius: 3rpx;
      }
    }
  }
}

.fee-list {
  padding: 0 30rpx;
}

.fee-item {
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

    &--UNPAID {
      background: #fff3e6;
      color: #ff9900;
    }

    &--PAID {
      background: #e6ffe6;
      color: #19be6b;
    }
  }

  &__body {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
  }

  &__period {
    font-size: 26rpx;
    color: #666;
  }

  &__amount {
    font-size: 36rpx;
    font-weight: bold;
    color: #fa3534;
  }

  &__footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
  }

  &__room, &__time {
    font-size: 24rpx;
    color: #999;
  }

  &__action {
    display: flex;
    justify-content: flex-end;
    padding-top: 16rpx;
    border-top: 1rpx solid #f5f5f5;
  }
}
</style>
