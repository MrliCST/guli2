<template>
  <div class="tradeFloor">
    <!-- 收件人信息 -->
    <div class="section">
      <div class="section__title"><h5>收件人信息</h5></div>
      <div class="section__body">
        <div class="addressList">
          <div
            class="addressItem"
            :class="{ selected: idx === selectedAddr }"
            v-for="(addr, idx) in addresses"
            :key="idx"
            @click="selectedAddr = idx"
          >
            <div class="addrName">
              <em>{{ addr.name }}</em>
              <span v-if="addr.isDefault" class="defaultTag">默认地址</span>
            </div>
            <div class="addrDetail">
              <span class="place">{{ addr.place }}</span>
              <span class="phone">{{ addr.phone }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="hr"></div>

    <!-- 支付方式 -->
    <div class="section">
      <div class="section__title"><h5>支付方式</h5></div>
      <div class="section__body">
        <div class="payTypes">
          <div
            class="payType"
            :class="{ selected: payType === idx }"
            v-for="(p, idx) in payTypes"
            :key="idx"
            @click="payType = idx"
          >{{ p }}</div>
        </div>
      </div>
    </div>

    <div class="hr"></div>

    <!-- 送货清单 -->
    <div class="section">
      <div class="section__title"><h5>送货清单</h5></div>
      <div class="section__body">
        <div class="deliveryInfo">
          <div class="deliveryType">
            <span class="label">配送方式：</span>
            <div class="express">天天快递</div>
            <div class="deliveryTime">配送时间：预计8月10日（周三）09:00-15:00送达</div>
          </div>
        </div>
        <div class="goodsList">
          <div class="goodsItem" v-for="(g, i) in goods" :key="i">
            <div class="goodsItem__img"><img :src="g.img" alt="" /></div>
            <div class="goodsItem__desc">
              <div class="desc">{{ g.name }}</div>
              <div class="seven">7天无理由退货</div>
            </div>
            <div class="goodsItem__price">￥{{ g.price }}</div>
            <div class="goodsItem__qty">X{{ g.qty }}</div>
            <div class="goodsItem__stock">有货</div>
          </div>
        </div>
        <div class="buyMessage">
          <span class="label">买家留言：</span>
          <textarea v-model="message" placeholder="建议留言前先与商家沟通确认" class="messageInput"></textarea>
        </div>
      </div>
    </div>

    <div class="hr"></div>

    <!-- 发票信息 -->
    <div class="section">
      <div class="section__title"><h5>发票信息</h5></div>
      <div class="section__body">
        <span class="invoiceTag">普通发票（电子）</span>
        <span class="invoiceTag">个人</span>
        <span class="invoiceTag">明细</span>
      </div>
    </div>

    <div class="hr"></div>

    <!-- 使用优惠/抵用 -->
    <div class="section">
      <div class="section__title"><h5>使用优惠/抵用</h5></div>
    </div>

    <!-- 订单汇总 -->
    <div class="summaryBar">
      <div class="summaryList">
        <div class="summaryRow">
          <span>{{ totalQty }}件商品，总商品金额</span>
          <em class="allPrice">¥{{ totalAmount }}</em>
        </div>
        <div class="summaryRow">
          <span>返现：</span>
          <em>0.00</em>
        </div>
        <div class="summaryRow">
          <span>运费：</span>
          <em>0.00</em>
        </div>
      </div>
    </div>

    <!-- 提交栏 -->
    <div class="submitBar">
      <div class="submitBar__left">
        <div class="amount">应付金额: <span class="price">¥{{ totalAmount }}</span></div>
        <div class="receiverInfo">
          寄送至：{{ addresses[selectedAddr].place }}
          收货人：{{ addresses[selectedAddr].name }}
          {{ addresses[selectedAddr].phone }}
        </div>
      </div>
      <a href="/cart" class="submitBtn">提交订单</a>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const selectedAddr = ref(0)
const addresses = ref([
  { name: '张三', place: '北京市海淀区', phone: '15988888882', isDefault: true },
  { name: '李四', place: '北京市昌平区洪福科技园', phone: '18745698888', isDefault: false },
  { name: '王路', place: '北京市大兴区瀛海镇', phone: '18398761234', isDefault: false }
])

const payType = ref(0)
const payTypes = ['在线支付', '货到付款']

const goods = ref([
  { img: '/img/goods.png', name: '荣耀V30 PRO 李现同款 DXO122分 5G双模 麒麟990 5GSOC芯片 双超级快充 游戏手机8GB+256GB魅海星蓝 双卡双待', price: '5399.00', qty: 1 },
  { img: '/img/goods.png', name: '荣耀V30 PRO 李现同款 DXO122分 5G双模 麒麟990 5GSOC芯片 双超级快充 游戏手机8GB+128GB冰岛幻境 双卡双待', price: '5399.00', qty: 1 }
])

const message = ref('')
const totalQty = computed(() => goods.value.reduce((s, g) => s + g.qty, 0))
const totalAmount = computed(() => goods.value.reduce((s, g) => s + parseFloat(g.price) * g.qty, 0).toFixed(2))
</script>

<style scoped>
.tradeFloor { display: grid; gap: 0; }

.section { padding: 16px 0; }
.section__title h5 {
  margin: 0 0 12px;
  font-size: 14px;
  color: #333;
  border-left: 3px solid #c81623;
  padding-left: 8px;
}
.section__body { padding-left: 11px; }
.hr { border-top: 1px solid #f0f0f0; }

/* 收件人地址 */
.addressList { display: grid; gap: 10px; }
.addressItem {
  display: grid;
  grid-template-columns: 100px 1fr;
  gap: 12px;
  padding: 10px 14px;
  border: 2px solid transparent;
  border-radius: 4px;
  cursor: pointer;
  background: #fafafa;
}
.addressItem.selected { border-color: #c81623; background: #fff5f5; }
.addrName { display: flex; align-items: center; gap: 8px; }
.addrName em { font-style: normal; font-weight: bold; font-size: 14px; color: #333; }
.defaultTag {
  background: #c81623;
  color: #fff;
  font-size: 10px;
  padding: 1px 6px;
  border-radius: 2px;
}
.addrDetail { display: flex; align-items: center; gap: 12px; font-size: 13px; color: #666; }
.place { color: #333; }
.phone { color: #666; }

/* 支付方式 */
.payTypes { display: flex; gap: 12px; }
.payType {
  padding: 8px 20px;
  border: 2px solid #ddd;
  border-radius: 4px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
}
.payType.selected { border-color: #c81623; color: #c81623; font-weight: bold; }

/* 送货清单 */
.deliveryInfo { margin-bottom: 16px; }
.deliveryType { display: flex; align-items: center; gap: 12px; font-size: 13px; }
.label { color: #999; }
.express { color: #333; font-weight: bold; }
.deliveryTime { color: #999; font-size: 12px; }

.goodsList { display: grid; gap: 12px; }
.goodsItem {
  display: grid;
  grid-template-columns: 80px 1fr 100px 60px 60px;
  gap: 12px;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px dashed #f0f0f0;
}
.goodsItem__img img { width: 70px; height: 70px; object-fit: cover; border-radius: 4px; }
.goodsItem__desc .desc { font-size: 12px; color: #333; line-height: 1.4; }
.goodsItem__desc .seven { font-size: 11px; color: #4CAF50; margin-top: 4px; }
.goodsItem__price { color: #c81623; font-size: 14px; font-weight: bold; }
.goodsItem__qty { font-size: 13px; color: #666; text-align: center; }
.goodsItem__stock { font-size: 12px; color: #4CAF50; text-align: center; }

.buyMessage {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin-top: 16px;
}
.messageInput {
  flex: 1;
  height: 50px;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 8px 12px;
  font-size: 13px;
  outline: none;
  resize: none;
  font-family: inherit;
}

/* 发票信息 */
.invoiceTag {
  display: inline-block;
  margin-right: 12px;
  font-size: 13px;
  color: #666;
}

/* 汇总 */
.summaryBar {
  display: flex;
  justify-content: flex-end;
  padding: 16px 0;
}
.summaryList { text-align: right; }
.summaryRow {
  font-size: 13px;
  color: #666;
  margin: 4px 0;
}
.summaryRow em { font-style: normal; color: #333; font-weight: bold; }
.allPrice { color: #c81623; font-size: 16px; }

/* 提交栏 */
.submitBar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fafafa;
  border-radius: 6px;
  padding: 20px;
  margin-top: 12px;
}
.submitBar__left { display: grid; gap: 8px; }
.amount { font-size: 14px; color: #333; }
.amount .price { color: #c81623; font-size: 22px; font-weight: bold; }
.receiverInfo { font-size: 12px; color: #999; }
.submitBtn {
  background: #c81623;
  color: #fff;
  padding: 12px 40px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  text-decoration: none;
}
.submitBtn:hover { opacity: 0.9; }
</style>
