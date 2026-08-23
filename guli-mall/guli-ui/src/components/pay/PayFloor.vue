<template>
  <section class="payFloor">
    <div class="payFloor__inner">
      <!-- 订单成功提示 -->
      <div class="noticeBox">
        <div class="noticeBox__left">
          <span class="successIcon">✓</span>
          <span class="successInfo">订单提交成功，请您及时付款，以便尽快为您发货~~</span>
        </div>
        <div class="noticeBox__right">
          <span>请您在提交订单<em class="time">4小时</em>之内完成支付，超时订单会自动取消。订单号：<em>145687</em></span>
          <em class="amount">应付金额：<b class="money">￥17,654</b></em>
        </div>
      </div>

      <!-- 重要说明 -->
      <div class="infoBox">
        <h4>重要说明：</h4>
        <ol>
          <li>谷粒商城支付平台目前支持<span class="zfb">支付宝</span>支付方式。</li>
          <li>其它支付渠道正在调试中，敬请期待。</li>
          <li>为了保证您的购物支付流程顺利完成，请保存以下支付宝信息。</li>
        </ol>
        <h4>支付宝账户信息：（很重要，<span class="save">请保存！！！</span>）</h4>
        <ul>
          <li>支付帐号：11111111</li>
          <li>密码：111111</li>
          <li>支付密码：111111</li>
        </ul>
      </div>

      <!-- 支付平台 -->
      <div class="section">
        <div class="section__title"><h5>支付平台</h5></div>
        <div class="section__body">
          <div class="payOptions">
            <div
              class="payOption"
              :class="{ selected: selectedPlatform === idx }"
              v-for="(p, idx) in platforms"
              :key="idx"
              @click="selectedPlatform = idx"
            >
              <img :src="p" alt="" />
            </div>
          </div>
        </div>
      </div>

      <div class="hr"></div>

      <!-- 支付网银 -->
      <div class="section">
        <div class="section__title"><h5>支付网银</h5></div>
        <div class="section__body">
          <div class="bankGrid">
            <div
              class="bankItem"
              :class="{ selected: selectedBank === idx }"
              v-for="(b, idx) in banks"
              :key="idx"
              @click="selectedBank = idx"
            >
              <img :src="b" alt="" />
            </div>
          </div>
        </div>
        <div class="hr"></div>
      </div>

      <!-- 立即支付 -->
      <div class="submitArea">
        <button class="payBtn" @click="$router.push('/paysuccess')">立即支付</button>
      </div>

      <!-- 其他支付方式 -->
      <div class="section">
        <div class="section__title"><h5>其他支付方式</h5></div>
        <div class="section__body">
          <router-link to="/wxpay" class="otherPay">微信支付</router-link>
          <span class="otherPay">中国银联</span>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const selectedPlatform = ref(0)
const platforms = ['/img/_/pay2.jpg', '/img/_/pay3.jpg']

const selectedBank = ref(0)
const banks = [
  '/img/_/pay10.jpg', '/img/_/pay11.jpg', '/img/_/pay12.jpg', '/img/_/pay13.jpg',
  '/img/_/pay14.jpg', '/img/_/pay15.jpg', '/img/_/pay16.jpg', '/img/_/pay17.jpg',
  '/img/_/pay18.jpg', '/img/_/pay19.jpg', '/img/_/pay20.jpg', '/img/_/pay21.jpg',
  '/img/_/pay22.jpg'
]
</script>

<style scoped>
.payFloor { background: #f5f5f5; padding: 10px 0; }
.payFloor__inner {
  width: 1200px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: grid;
  gap: 16px;
}

/* 提示框 */
.noticeBox {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 20px;
  align-items: center;
  background: #fffbe6;
  border: 1px solid #ffe58f;
  border-radius: 6px;
  padding: 16px;
}
.noticeBox__left { display: flex; align-items: center; gap: 12px; }
.successIcon {
  width: 32px; height: 32px;
  background: #4CAF50; color: #fff;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 16px; font-weight: bold;
}
.successInfo { font-size: 16px; font-weight: bold; color: #333; }
.noticeBox__right { text-align: right; display: grid; gap: 4px; font-size: 13px; color: #666; }
.noticeBox__right em { font-style: normal; }
.time { color: #c81623; font-weight: bold; }
.amount { font-size: 14px; }
.money { color: #c81623; font-size: 20px; font-weight: bold; }

/* 说明 */
.infoBox {
  background: #f9f9f9;
  border-radius: 6px;
  padding: 16px;
}
.infoBox h4 { margin: 0 0 8px; font-size: 14px; color: #333; }
.infoBox h4:last-of-type { margin-top: 16px; }
.infoBox ol { margin: 0 0 0 20px; padding: 0; font-size: 13px; color: #666; line-height: 1.8; }
.infoBox ul { list-style: none; padding: 0; margin: 0; font-size: 13px; color: #666; line-height: 1.8; }
.zfb { color: #1677ff; font-weight: bold; }
.save { color: #c81623; font-weight: bold; }

/* 通用 section */
.section { padding: 8px 0; }
.section__title h5 {
  margin: 0 0 12px;
  font-size: 14px;
  color: #333;
  border-left: 3px solid #c81623;
  padding-left: 8px;
}
.section__body { padding-left: 11px; }
.hr { border-top: 1px solid #f0f0f0; }

/* 支付平台选项 */
.payOptions { display: flex; gap: 12px; }
.payOption {
  border: 2px solid #eee;
  border-radius: 4px;
  padding: 4px;
  cursor: pointer;
}
.payOption.selected { border-color: #c81623; }
.payOption img { width: 120px; height: 40px; object-fit: contain; }

/* 网银网格 */
.bankGrid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 8px;
}
.bankItem {
  border: 2px solid #eee;
  border-radius: 4px;
  padding: 4px;
  cursor: pointer;
  text-align: center;
}
.bankItem.selected { border-color: #c81623; }
.bankItem img { width: 100%; height: 36px; object-fit: contain; }

/* 提交 */
.submitArea { text-align: center; padding: 12px 0; }
.payBtn {
  background: #c81623;
  color: #fff;
  border: none;
  padding: 12px 50px;
  border-radius: 4px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
}
.payBtn:hover { opacity: 0.9; }

/* 其他支付 */
.otherPay {
  display: inline-block;
  margin-right: 20px;
  font-size: 13px;
  color: #666;
  text-decoration: none;
  cursor: pointer;
}
.otherPay:hover { color: #c81623; }
</style>
