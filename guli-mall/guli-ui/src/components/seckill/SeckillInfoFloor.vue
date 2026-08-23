<template>
  <section class="seckillInfoFloor">
    <div class="seckillInfoFloor_inner">
      <!-- 面包屑 -->
      <div class="crumb">
        <a href="#">Apple苹果</a>
        <span>></span>
        <span class="active">iphone 6S系类</span>
      </div>

      <!-- 主体:左放大镜+收藏 / 右秒杀商品信息 -->
      <div class="main">
        <!-- 左:图片预览 + 收藏 -->
        <div class="previewWrap">
          <div class="previewWrap_main">
            <img :src="mainImg" alt="" />
          </div>
          <div class="previewWrap_collect">
            <img src="/img/_/shi_heart.png" alt="" />
            <span>收藏</span>
          </div>
        </div>

        <!-- 右:商品信息 -->
        <div class="itemInfo">
          <h4 class="itemInfo_name">Apple iPhone 6s（A1700）64G玫瑰金色 移动通信电信4G手机</h4>

          <!-- 秒杀倒计时 -->
          <div class="itemInfo_seckillBar">
            <span class="seckillIcon">
              <img src="/img/_/clock.png" alt="" />
              谷粒秒杀
            </span>
            <span class="countdown">距离结束：<b>{{ countdown }}</b></span>
          </div>

          <!-- 价格区 -->
          <div class="itemInfo_summary">
            <div class="summaryRow">
              <div class="summaryRow_title">价　　格</div>
              <div class="summaryRow_price">
                <i>¥</i><em>5299.00</em>
                <span class="notice">降价通知</span>
              </div>
              <div class="summaryRow_remark">
                <i>累计评价</i><em>612188</em>
              </div>
            </div>
            <div class="summaryRow">
              <div class="summaryRow_title">促　　销</div>
              <div class="summaryRow_promo">
                <i class="redTag">加价购</i>
                <em>满999.00另加20.00元，或满1999.00另加30.00元，即可换购热销商品</em>
              </div>
            </div>
          </div>

          <!-- 支持区 -->
          <div class="itemInfo_support">
            <div class="summaryRow">
              <div class="summaryRow_title">支　　持</div>
              <div class="summaryRow_value">以旧换新，闲置手机回收 4G套餐超值抢 礼品购</div>
            </div>
            <div class="summaryRow">
              <div class="summaryRow_title">配 送 至</div>
              <div class="summaryRow_value">北京市 昌平区</div>
            </div>
          </div>

          <!-- 数量 + 加入购物车 -->
          <div class="itemInfo_action">
            <div class="qtyBox">
              <button class="qtyBtn" @click="qty > 1 && qty--">-</button>
              <input class="qtyInput" v-model="qty" type="text" />
              <button class="qtyBtn" @click="qty++">+</button>
            </div>
            <button class="cartBtn">加入购物车</button>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const mainImg = ref('/img/_/b1.png')
const qty = ref(1)
const countdown = ref('01:56:78')

let timer = null
onMounted(() => {
  let total = 3600 * 2 + 56 * 60 + 78
  timer = setInterval(() => {
    if (total <= 0) { clearInterval(timer); return }
    total--
    const h = String(Math.floor(total / 3600)).padStart(2, '0')
    const m = String(Math.floor((total % 3600) / 60)).padStart(2, '0')
    const s = String(total % 60).padStart(2, '0')
    countdown.value = `${h}:${m}:${s}`
  }, 1000)
})
onUnmounted(() => clearInterval(timer))
</script>

<style scoped lang="scss">
.seckillInfoFloor { background: #fff0f5; padding: 10px 0; }
.seckillInfoFloor_inner {
  width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto;
  gap: 10px;
}

/* 面包屑 */
.crumb { display: flex; align-items: center; gap: 6px; font-size: 12px; color: #999; padding: 6px 0; }
.crumb a { color: #666; }
.crumb .active { color: #333; }

/* 主体:左右分栏 */
.main { display: grid; grid-template-columns: 400px 1fr; gap: 20px; }

/* 左:图片预览 + 收藏 */
.previewWrap { display: grid; grid-template-rows: 400px auto; gap: 10px; }
.previewWrap_main {
  background: #fff;
  border-radius: 6px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}
.previewWrap_main img { width: 100%; height: 100%; object-fit: contain; }
.previewWrap_collect {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 8px;
  background: #fff;
  border-radius: 6px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
}
.previewWrap_collect:hover { color: #c81623; }
.previewWrap_collect img { width: 16px; height: 16px; }

/* 右:商品信息 */
.itemInfo {
  background: #fff;
  border-radius: 6px;
  padding: 16px;
  display: grid;
  grid-template-rows: auto auto auto auto auto;
  gap: 12px;
}
.itemInfo_name { font-size: 16px; color: #333; margin: 0; line-height: 1.4; }

/* 秒杀倒计时条 */
.itemInfo_seckillBar {
  background: linear-gradient(135deg, #ff6b6b, #c81623);
  border-radius: 6px;
  padding: 8px 14px;
  display: flex;
  align-items: center;
  gap: 16px;
  color: #fff;
}
.seckillIcon { display: flex; align-items: center; gap: 4px; font-size: 14px; font-weight: bold; }
.seckillIcon img { width: 16px; height: 16px; filter: brightness(0) invert(1); }
.countdown { font-size: 14px; }
.countdown b { font-size: 18px; font-weight: bold; margin-left: 4px; font-family: monospace; }

/* summary 行 */
.itemInfo_summary, .itemInfo_support { display: grid; gap: 4px; }
.summaryRow {
  display: grid;
  grid-template-columns: 80px 1fr auto;
  gap: 8px;
  align-items: center;
  font-size: 13px;
}
.summaryRow_title { color: #999; text-align: right; }
.summaryRow_price { color: #c81623; font-size: 24px; font-weight: bold; }
.summaryRow_price i { font-style: normal; font-size: 14px; }
.summaryRow_price em { font-style: normal; }
.summaryRow_price .notice { font-size: 12px; color: #999; margin-left: 12px; cursor: pointer; }
.summaryRow_remark { font-size: 13px; color: #999; }
.summaryRow_remark em { color: #333; font-style: normal; font-weight: bold; margin-left: 4px; }
.summaryRow_promo { grid-column: 2 / 4; }
.summaryRow_promo .redTag {
  background: #c81623;
  color: #fff;
  padding: 1px 6px;
  border-radius: 3px;
  font-style: normal;
  font-size: 12px;
  margin-right: 6px;
}
.summaryRow_promo em { font-style: normal; color: #666; font-size: 12px; }
.summaryRow_value { color: #666; font-size: 12px; }

/* 数量 + 购物车 */
.itemInfo_action {
  display: flex;
  align-items: center;
  gap: 16px;
  border-top: 1px solid #eee;
  padding-top: 12px;
}
.qtyBox {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}
.qtyBtn {
  width: 30px;
  height: 30px;
  background: #f5f5f5;
  border: none;
  cursor: pointer;
  font-size: 16px;
  color: #666;
}
.qtyBtn:hover { background: #eee; }
.qtyInput {
  width: 40px;
  height: 30px;
  text-align: center;
  border: none;
  border-left: 1px solid #ddd;
  border-right: 1px solid #ddd;
  font-size: 14px;
}
.cartBtn {
  background: linear-gradient(135deg, #ff6b6b, #c81623);
  color: #fff;
  border: none;
  padding: 8px 30px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  font-weight: bold;
}
.cartBtn:hover { opacity: 0.9; }
</style>
