<template>
  <view class="my-bills-page">
    <!-- 汇总 -->
    <view class="bills-summary">
      <view class="bills-summary__item">
        <text class="bills-summary__value">{{ unpaidTotal.toFixed(2) }}</text>
        <text class="bills-summary__label">待缴(元)</text>
      </view>
      <view class="bills-summary__divider"></view>
      <view class="bills-summary__item">
        <text class="bills-summary__value">{{ paidTotal.toFixed(2) }}</text>
        <text class="bills-summary__label">已缴(元)</text>
      </view>
    </view>

    <!-- 筛选 -->
    <view class="bills-tabs">
      <view
        v-for="tab in tabs"
        :key="tab.value"
        class="bills-tabs__item"
        :class="{ 'bills-tabs__item--active': currentTab === tab.value }"
        @click="switchTab(tab.value)"
      >
        <text>{{ tab.label }}</text>
      </view>
    </view>

    <!-- 列表 -->
    <view v-if="bills.length > 0" class="bills-list">
      <view v-for="bill in bills" :key="bill.id" class="bill-item card">
        <view class="bill-item__header">
          <text class="bill-item__type">{{ bill.feeTypeName }}</text>
          <view class="bill-item__status" :class="'bill-item__status--' + bill.status">
            <text>{{ bill.status === 'PAID' ? '已缴' : '待缴' }}</text>
          </view>
        </view>
        <view class="bill-item__body">
          <text class="bill-item__period">{{ bill.period }}</text>
          <text class="bill-item__amount">¥{{ bill.amount.toFixed(2) }}</text>
        </view>
        <view class="bill-item__footer">
          <text class="bill-item__room">{{ bill.roomName }}</text>
          <text class="bill-item__time">{{ bill.createTime }}</text>
        </view>
        <view v-if="bill.status !== 'PAID'" class="bill-item__action">
          <u-button type="primary" size="mini" shape="circle" @click="handlePay(bill)">立即缴费</u-button>
        </view>
      </view>
    </view>

    <empty-state v-else text="暂无缴费记录" />

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
      hasMore: true,
      roomId: ''
    }
  },
  onLoad(options) {
    if (options.roomId) this.roomId = options.roomId
  },
  onShow() {
    this.resetAndLoad()
  },
  onPullDownRefresh() {
    this.resetAndLoad().finally(() => uni.stopPullDownRefresh())
  },
  onReachBottom() {
    if (this.hasMore && this.loadStatus !== 'loading') this.loadMore()
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
        const params = { page: this.page, pageSize: this.pageSize }
        if (this.currentTab !== 'ALL') params.status = this.currentTab
        if (this.roomId) params.roomId = this.roomId
        const res = await getMyBills(params)
        const list = res.records || res.list || res || []
        this.bills = this.page === 1 ? list : [...this.bills, ...list]
        this.hasMore = list.length >= this.pageSize
        this.loadStatus = this.hasMore ? 'loadmore' : 'nomore'
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
      } catch (e) {}
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
          if (!res.confirm) return
          try {
            uni.showLoading({ title: '支付中...' })
            const payRes = await payBill(bill.id, 'WECHAT')
            uni.hideLoading()
            if (payRes.paymentParams) {
              uni.requestPayment({
                ...payRes.paymentParams,
                success: () => {
                  uni.showToast({ title: '支付成功', icon: 'success' })
                  this.resetAndLoad()
                },
                fail: () => uni.showToast({ title: '支付已取消', icon: 'none' })
              })
            } else {
              uni.showToast({ title: '支付成功', icon: 'success' })
              this.resetAndLoad()
            }
          } catch (e) {
            uni.hideLoading()
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.my-bills-page { padding-bottom: 30rpx; }

.bills-summary {
  display: flex;
  background: linear-gradient(135deg, #ff9900, #ff6600);
  padding: 40rpx 0;
  margin-bottom: 20rpx;

  &__item { flex: 1; display: flex; flex-direction: column; align-items: center; }
  &__value { font-size: 48rpx; font-weight: bold; color: #fff; margin-bottom: 8rpx; }
  &__label { font-size: 24rpx; color: rgba(255,255,255,0.8); }
  &__divider { width: 1rpx; background: rgba(255,255,255,0.3); }
}

.bills-tabs {
  display: flex; background: #fff; padding: 10rpx 30rpx; margin-bottom: 20rpx;
  &__item {
    flex: 1; text-align: center; padding: 16rpx 0; font-size: 28rpx; color: #666; position: relative;
    &--active {
      color: #ff9900; font-weight: bold;
      &::after { content: ''; position: absolute; bottom: 0; left: 50%; transform: translateX(-50%); width: 48rpx; height: 6rpx; background: #ff9900; border-radius: 3rpx; }
    }
  }
}

.bills-list { padding: 0 30rpx; }

.bill-item {
  margin-bottom: 20rpx;
  &__header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16rpx; }
  &__type { font-size: 30rpx; font-weight: bold; color: #333; }
  &__status {
    padding: 6rpx 16rpx; border-radius: 6rpx; font-size: 22rpx;
    &--UNPAID { background: #fff3e6; color: #ff9900; }
    &--PAID { background: #e6ffe6; color: #19be6b; }
  }
  &__body { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16rpx; }
  &__period { font-size: 26rpx; color: #666; }
  &__amount { font-size: 36rpx; font-weight: bold; color: #fa3534; }
  &__footer { display: flex; justify-content: space-between; margin-bottom: 16rpx; }
  &__room, &__time { font-size: 24rpx; color: #999; }
  &__action { display: flex; justify-content: flex-end; padding-top: 16rpx; border-top: 1rpx solid #f5f5f5; }
}
</style>
