<template>
  <section class="cartTableFloor">
    <div class="cartTableFloor__inner">
      <!-- 标题 -->
      <div class="cartTableFloor__title">
        <h4>全部商品 <span>{{ cartItems.length }}</span></h4>
      </div>

      <!-- 表头 -->
      <div class="cartHeader">
        <div class="col col-check">
          <input type="checkbox" v-model="allChecked" @change="toggleAll" /> 全选
        </div>
        <div class="col col-goods">商品</div>
        <div class="col col-attr">属性</div>
        <div class="col col-price">单价（元）</div>
        <div class="col col-qty">数量</div>
        <div class="col col-sub">小计（元）</div>
        <div class="col col-op">操作</div>
      </div>

      <!-- 商品列表 -->
      <div class="cartBody">
        <div class="cartRow" v-for="(item, i) in cartItems" :key="i">
          <div class="col col-check">
            <input type="checkbox" v-model="item.checked" />
          </div>
          <div class="col col-goods">
            <div class="goodsImg"><img :src="item.img" alt="" /></div>
            <div class="goodsMsg">{{ item.intro }}</div>
          </div>
          <div class="col col-attr">{{ item.property }}</div>
          <div class="col col-price"><span>¥{{ item.price }}</span></div>
          <div class="col col-qty">
            <div class="qtyBox">
              <button class="qtyBtn" @click="item.qty > 1 && item.qty--">-</button>
              <input class="qtyInput" v-model="item.qty" type="text" />
              <button class="qtyBtn" @click="item.qty++">+</button>
            </div>
          </div>
          <div class="col col-sub"><span class="subPrice">¥{{ (parseFloat(item.price) * item.qty).toFixed(2) }}</span></div>
          <div class="col col-op">
            <a href="#" @click.prevent="removeItem(i)">删除</a>
            <a href="#">移到收藏</a>
          </div>
        </div>
      </div>

      <!-- 工具栏 -->
      <div class="cartFooter">
        <div class="cartFooter__left">
          <div class="selectAllBox">
            <input type="checkbox" v-model="allChecked" @change="toggleAll" />
            <span>全选</span>
          </div>
          <div class="options">
            <a href="#" @click.prevent="removeSelected">删除选中的商品</a>
            <a href="#">移到我的关注</a>
            <a href="#">清除下柜商品</a>
          </div>
        </div>
        <div class="cartFooter__right">
          <div class="chosed">已选择 <span>{{ checkedCount }}</span> 件商品</div>
          <div class="sumprice">
            <div>总价（不含运费）：<b class="totalMoney">¥{{ totalMoney }}</b></div>
            <div class="saved">已节省：<b>-¥20.00</b></div>
          </div>
          <a href="#" class="checkoutBtn">结算</a>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'

const cartItems = reactive([
  { img: '/img/goods.png', intro: '美的（Midea)电饭煲WFZ5099IH IH电磁加热 1250W大火力 钛金釜5L电饭锅', property: '颜色：WFZ5099IH/5L钛金釜内胆', price: '2999.00', qty: 1, checked: true },
  { img: '/img/goods.png', intro: 'Apple iPhone 6s (A1699) 64G 玫瑰金色 移动联通电信4G手机', property: '颜色：玫瑰金 / 版本：公开版', price: '5299.00', qty: 1, checked: true },
  { img: '/img/goods.png', intro: '华为 HUAWEI Mate 60 Pro 12+512G 雅丹黑 麒麟9000S', property: '颜色：雅丹黑 / 版本：512G', price: '6999.00', qty: 1, checked: false },
  { img: '/img/goods.png', intro: '小米14 Pro 16+512G 黑色 骁龙8 Gen3 徕卡光学镜头', property: '颜色：黑色 / 版本：512G', price: '4999.00', qty: 1, checked: true }
])

const allChecked = computed({
  get: () => cartItems.every(i => i.checked),
  set: (val) => { cartItems.forEach(i => i.checked = val) }
})

function toggleAll(e) {
  cartItems.forEach(i => i.checked = e.target.checked)
}

const checkedCount = computed(() => cartItems.filter(i => i.checked).length)

const totalMoney = computed(() => {
  return cartItems
    .filter(i => i.checked)
    .reduce((sum, i) => sum + parseFloat(i.price) * i.qty, 0)
    .toFixed(2)
})

function removeItem(idx) {
  cartItems.splice(idx, 1)
}

function removeSelected() {
  for (let i = cartItems.length - 1; i >= 0; i--) {
    if (cartItems[i].checked) cartItems.splice(i, 1)
  }
}
</script>

<style scoped>
.cartTableFloor { background: AliceBlue; padding: 10px 0; }
.cartTableFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto auto auto;
  gap: 0;
}

/* 标题 */
.cartTableFloor__title { padding: 10px 0; }
.cartTableFloor__title h4 { margin: 0; font-size: 16px; color: #333; }
.cartTableFloor__title span { color: #999; font-size: 13px; margin-left: 8px; }

/* 表头 */
.cartHeader {
  display: grid;
  grid-template-columns: 60px 3fr 2fr 1fr 1fr 1fr 1fr;
  background: #f5f5f5;
  padding: 12px 0;
  border-radius: 6px 6px 0 0;
  font-size: 13px;
  color: #666;
  text-align: center;
}
.cartHeader .col-check { text-align: left; padding-left: 16px; display: flex; align-items: center; gap: 6px; }

/* 商品行 */
.cartBody { background: #fff; border-radius: 0 0 6px 6px; overflow: hidden; }
.cartRow {
  display: grid;
  grid-template-columns: 60px 3fr 2fr 1fr 1fr 1fr 1fr;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 13px;
  text-align: center;
}
.cartRow:last-child { border-bottom: none; }
.col-check { text-align: left; padding-left: 16px; }
.col-goods {
  display: flex;
  align-items: center;
  gap: 10px;
  text-align: left;
  padding: 0 10px;
}
.goodsImg img { width: 60px; height: 60px; object-fit: cover; border-radius: 4px; }
.goodsMsg { font-size: 12px; color: #333; line-height: 1.4; }
.col-attr { font-size: 12px; color: #999; padding: 0 10px; }
.col-price span { color: #333; }
.col-sub .subPrice { color: var(--theme-red); font-weight: bold; }
.col-op { display: flex; flex-direction: column; gap: 6px; }
.col-op a { font-size: 12px; color: #666; text-decoration: none; }
.col-op a:hover { color: var(--theme-red); }

/* 数量 */
.qtyBox {
  display: inline-flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}
.qtyBtn {
  width: 26px;
  height: 26px;
  background: #f5f5f5;
  border: none;
  cursor: pointer;
  font-size: 14px;
  color: #666;
}
.qtyBtn:hover { background: #eee; }
.qtyInput {
  width: 36px;
  height: 26px;
  text-align: center;
  border: none;
  border-left: 1px solid #ddd;
  border-right: 1px solid #ddd;
  font-size: 13px;
}

/* 工具栏 */
.cartFooter {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 20px;
  align-items: center;
  background: #fff;
  border-radius: 6px;
  padding: 16px 20px;
  margin-top: 12px;
}
.cartFooter__left { display: flex; align-items: center; gap: 20px; }
.selectAllBox { display: flex; align-items: center; gap: 6px; font-size: 13px; color: #666; }
.options { display: flex; gap: 16px; }
.options a { font-size: 12px; color: #666; text-decoration: none; }
.options a:hover { color: var(--theme-red); }
.cartFooter__right { display: flex; align-items: center; gap: 24px; }
.chosed { font-size: 13px; color: #666; }
.chosed span { color: var(--theme-red); font-weight: bold; }
.sumprice { text-align: right; }
.sumprice div { font-size: 12px; color: #999; }
.totalMoney { color: var(--theme-red); font-size: 20px; font-weight: bold; }
.saved b { color: #4CAF50; }
.checkoutBtn {
  background: var(--theme-red);
  color: #fff;
  padding: 10px 30px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  text-decoration: none;
}
.checkoutBtn:hover { opacity: 0.9; }
</style>
