<template>
  <section class="paySuccessFloor">
    <div class="paySuccessFloor_inner">
      <div class="successCard">
        <div class="successCard_icon">
          <img :src="'/img/_/right.png'" alt="成功" />
        </div>
        <h3 class="successCard_title">恭喜您，支付成功啦！</h3>
        <div class="successCard_detail">
          <p>支付方式：微信</p>
          <p>支付金额：￥<em class="payMoney">{{ amount }}</em>元</p>
        </div>
        <div class="successCard_action">
          <router-link to="/myorder" class="btnOrder">查看订单</router-link>
          <router-link to="/" class="btnShop">继续购物</router-link>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
const amount = ref('17,654')
</script>

<style scoped lang="scss">
.paySuccessFloor {
  background: linear-gradient(135deg, #f0fff0 0%, #e8f5e9 100%);
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.paySuccessFloor_inner {
  width: 1200px;
  margin: 0 auto;
}

.successCard {
  background: #fff;
  border-radius: 12px;
  padding: 60px 40px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  display: grid;
  gap: 16px;
  justify-items: center;
}
.successCard_icon img { width: 48px; height: 48px; }
.successCard_title {
  margin: 0;
  font-size: 24px;
  color: #333;
  font-weight: bold;
}
.successCard_detail { display: grid; gap: 6px; }
.successCard_detail p { margin: 0; font-size: 14px; color: #666; }
.payMoney { font-style: normal; color: #c81623; font-weight: bold; font-size: 18px; }
.successCard_action { display: flex; gap: 20px; margin-top: 12px; }
.btnOrder {
  background: #c81623;
  color: #fff;
  padding: 10px 30px;
  border-radius: 4px;
  font-size: 14px;
  text-decoration: none;
  font-weight: bold;
}
.btnOrder:hover { opacity: 0.9; }
.btnShop {
  background: #f5f5f5;
  color: #666;
  padding: 10px 30px;
  border-radius: 4px;
  font-size: 14px;
  text-decoration: none;
  border: 1px solid #ddd;
}
.btnShop:hover { background: #eee; }
</style>
