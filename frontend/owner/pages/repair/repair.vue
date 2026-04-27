<template>
  <view class="repair-page">
    <view class="repair-form card">
      <text class="page-title">提交报修</text>

      <!-- 选择房产 -->
      <view class="repair-form__item">
        <text class="repair-form__label">报修房产</text>
        <view class="repair-form__picker" @click="showRoomPicker = true">
          <text :class="{ 'repair-form__placeholder': !selectedRoom }">
            {{ selectedRoom ? selectedRoom.label : '请选择房产' }}
          </text>
          <u-icon name="arrow-right" color="#ccc" size="28" />
        </view>
      </view>

      <!-- 报修类型 -->
      <view class="repair-form__item">
        <text class="repair-form__label">报修类型</text>
        <view class="repair-form__types">
          <view
            v-for="type in repairTypes"
            :key="type.value"
            class="repair-form__type-tag"
            :class="{ 'repair-form__type-tag--active': form.repairType === type.value }"
            @click="form.repairType = type.value"
          >
            <text>{{ type.label }}</text>
          </view>
        </view>
      </view>

      <!-- 问题描述 -->
      <view class="repair-form__item">
        <text class="repair-form__label">问题描述</text>
        <textarea
          class="repair-form__textarea"
          v-model="form.description"
          placeholder="请详细描述您的问题，方便维修人员了解情况"
          maxlength="500"
          :auto-height="true"
        />
        <text class="repair-form__count">{{ form.description.length }}/500</text>
      </view>

      <!-- 上传图片 -->
      <view class="repair-form__item">
        <text class="repair-form__label">拍照上传</text>
        <view class="repair-form__images">
          <view
            v-for="(img, index) in form.images"
            :key="index"
            class="repair-form__image-item"
          >
            <image :src="img" mode="aspectFill" class="repair-form__image" @click="previewImage(index)" />
            <view class="repair-form__image-delete" @click="removeImage(index)">
              <u-icon name="close" color="#fff" size="24" />
            </view>
          </view>
          <view
            v-if="form.images.length < 6"
            class="repair-form__image-add"
            @click="chooseImage"
          >
            <u-icon name="plus" color="#ccc" size="48" />
            <text class="repair-form__image-add-text">添加照片</text>
          </view>
        </view>
        <text class="repair-form__tip">最多上传6张照片</text>
      </view>

      <!-- 预约日期 -->
      <view class="repair-form__item">
        <text class="repair-form__label">预约日期</text>
        <picker mode="date" :value="form.appointmentDate" :start="today" @change="onDateChange">
          <view class="repair-form__picker">
            <text :class="{ 'repair-form__placeholder': !form.appointmentDate }">
              {{ form.appointmentDate || '请选择预约日期' }}
            </text>
            <u-icon name="arrow-right" color="#ccc" size="28" />
          </view>
        </picker>
      </view>

      <!-- 联系电话 -->
      <view class="repair-form__item">
        <text class="repair-form__label">联系电话</text>
        <input
          class="repair-form__input"
          type="number"
          v-model="form.contactPhone"
          placeholder="请输入您的联系电话"
          maxlength="11"
        />
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="repair-submit">
      <u-button
        type="primary"
        shape="circle"
        :loading="submitting"
        :disabled="!canSubmit"
        @click="submitForm"
      >
        提交报修
      </u-button>
    </view>

    <!-- 房产选择弹窗 -->
    <u-picker
      mode="selector"
      v-model="showRoomPicker"
      :range="roomOptions"
      range-key="label"
      @confirm="onRoomConfirm"
    />
  </view>
</template>

<script>
import { submitRepair, uploadRepairImage } from '../../api/repair.js'
import { getMyRooms } from '../../api/room.js'
import { getUserInfo } from '../../utils/auth.js'

export default {
  data() {
    const today = new Date()
    const todayStr = today.getFullYear() + '-' + String(today.getMonth() + 1).padStart(2, '0') + '-' + String(today.getDate()).padStart(2, '0')
    return {
      form: {
        roomId: '',
        repairType: '',
        description: '',
        images: [],
        appointmentDate: '',
        contactPhone: ''
      },
      repairTypes: [
        { label: '水电维修', value: 'WATER_ELECTRIC' },
        { label: '门窗维修', value: 'DOOR_WINDOW' },
        { label: '家电故障', value: 'APPLIANCE' },
        { label: '管道疏通', value: 'PIPELINE' },
        { label: '其他', value: 'OTHER' }
      ],
      rooms: [],
      roomOptions: [],
      selectedRoom: null,
      showRoomPicker: false,
      submitting: false,
      today: todayStr
    }
  },
  computed: {
    canSubmit() {
      return this.form.roomId &&
        this.form.repairType &&
        this.form.description.trim() &&
        this.form.contactPhone.length >= 11 &&
        !this.submitting
    }
  },
  created() {
    this.loadRooms()
    // 自动填充手机号
    const user = getUserInfo()
    if (user && user.phone) {
      this.form.contactPhone = user.phone
    }
  },
  methods: {
    async loadRooms() {
      try {
        const res = await getMyRooms()
        this.rooms = res || []
        this.roomOptions = this.rooms.map(r => ({
          label: r.communityName + ' ' + r.building + '栋' + r.unit + '单元' + r.roomNo,
          value: r.id
        }))
      } catch (e) {
        console.error('加载房产失败', e)
      }
    },
    onRoomConfirm(e) {
      const index = e[0]
      this.selectedRoom = this.roomOptions[index]
      this.form.roomId = this.rooms[index].id
    },
    onDateChange(e) {
      this.form.appointmentDate = e.detail.value
    },
    chooseImage() {
      const remaining = 6 - this.form.images.length
      uni.chooseImage({
        count: remaining,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.form.images = [...this.form.images, ...res.tempFilePaths]
        }
      })
    },
    previewImage(index) {
      uni.previewImage({
        current: index,
        urls: this.form.images
      })
    },
    removeImage(index) {
      this.form.images.splice(index, 1)
    },
    async submitForm() {
      if (!this.canSubmit) return
      this.submitting = true
      try {
        // 先上传图片
        let imageUrls = []
        if (this.form.images.length > 0) {
          uni.showLoading({ title: '上传图片中...' })
          const uploadPromises = this.form.images.map(img => uploadRepairImage(img))
          const uploadResults = await Promise.all(uploadPromises)
          imageUrls = uploadResults.map(r => r.url || r)
        }

        // 提交报修
        uni.showLoading({ title: '提交中...' })
        await submitRepair({
          roomId: this.form.roomId,
          repairType: this.form.repairType,
          description: this.form.description,
          images: imageUrls,
          appointmentDate: this.form.appointmentDate,
          contactPhone: this.form.contactPhone
        })

        uni.hideLoading()
        uni.showToast({ title: '报修提交成功', icon: 'success' })
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        uni.hideLoading()
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.repair-page {
  padding: 20rpx 30rpx;
  padding-bottom: 120rpx;
}

.repair-form {
  &__item {
    margin-bottom: 30rpx;
  }

  &__label {
    display: block;
    font-size: 28rpx;
    font-weight: 500;
    color: #333;
    margin-bottom: 16rpx;
  }

  &__picker {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 24rpx;
    background: #f7f8fa;
    border-radius: 12rpx;
  }

  &__placeholder {
    color: #999;
  }

  &__types {
    display: flex;
    flex-wrap: wrap;
    gap: 16rpx;
  }

  &__type-tag {
    padding: 14rpx 30rpx;
    background: #f7f8fa;
    border-radius: 8rpx;
    font-size: 26rpx;
    color: #666;
    border: 2rpx solid transparent;

    &--active {
      background: #e6f0ff;
      color: #2979ff;
      border-color: #2979ff;
    }
  }

  &__textarea {
    width: 100%;
    min-height: 160rpx;
    padding: 20rpx 24rpx;
    background: #f7f8fa;
    border-radius: 12rpx;
    font-size: 28rpx;
    color: #333;
    box-sizing: border-box;
  }

  &__count {
    display: block;
    text-align: right;
    font-size: 22rpx;
    color: #999;
    margin-top: 8rpx;
  }

  &__images {
    display: flex;
    flex-wrap: wrap;
    gap: 16rpx;
  }

  &__image-item {
    position: relative;
    width: 200rpx;
    height: 200rpx;
  }

  &__image {
    width: 100%;
    height: 100%;
    border-radius: 12rpx;
  }

  &__image-delete {
    position: absolute;
    top: -12rpx;
    right: -12rpx;
    width: 40rpx;
    height: 40rpx;
    background: rgba(0, 0, 0, 0.5);
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  &__image-add {
    width: 200rpx;
    height: 200rpx;
    background: #f7f8fa;
    border: 2rpx dashed #ddd;
    border-radius: 12rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  &__image-add-text {
    font-size: 22rpx;
    color: #999;
    margin-top: 8rpx;
  }

  &__tip {
    font-size: 22rpx;
    color: #999;
    margin-top: 12rpx;
  }

  &__input {
    padding: 20rpx 24rpx;
    background: #f7f8fa;
    border-radius: 12rpx;
    font-size: 28rpx;
  }
}

.repair-submit {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  background: #fff;
  box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.06);
}
</style>
