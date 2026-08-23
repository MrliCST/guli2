<template>
  <section class="addCartSuccessFloor">
    <div class="addCartSuccessFloor__inner">
      <!-- 成功提示 + 商品信息 + 去结算 -->
      <div class="successBox">
        <div class="successBox__left">
          <div class="successIcon">✓</div>
          <div class="successText">商品已成功加入购物车！</div>
        </div>
        <div class="successBox__goods">
          <div class="goodsPic">
            <img src="/img/_/gocart.jpg" alt="" />
          </div>
          <div class="goodsInfo">
            <p class="goodsTitle">美的（Midea)电饭煲WFZ5099IH IH电磁加热 1250W大火力 钛金釜5L电饭锅</p>
            <p class="goodsAttr">颜色：WFZ5099IH/5L钛金釜内胆 数量：1</p>
          </div>
        </div>
        <div class="successBox__action">
          <a href="/item" class="btnDetail">查看商品详情</a>
          <a href="/cart" class="btnCart">去购物车结算 ></a>
        </div>
      </div>

      <!-- 购买该商品的朋友还购买了 -->
      <div class="recommendBox">
        <h4 class="recommendBox__title">购买该商品的朋友还购买了</h4>
        <div class="recommendBox__list">
          <BaseGoodsCard
            v-for="(g, i) in recommendGoods"
            :key="i"
            :img="g.img"
            :price="g.price"
            :title="g.title"
            :show-operate="true"
          />
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import BaseGoodsCard from '@/components/common/BaseGoodsCard.vue'

const recommendGoods = ref([
  { img: '/img/_/gocart01.jpg', price: '2299.00', title: '摩托罗拉 Moto Mods 摩眼-哈苏摄影模块' },
  { img: '/img/_/gocart01.jpg', price: '2299.00', title: '摩托罗拉 Moto Mods 摩眼-哈苏摄影模块' },
  { img: '/img/_/gocart01.jpg', price: '2299.00', title: '摩托罗拉 Moto Mods 摩眼-哈苏摄影模块' },
  { img: '/img/_/gocart01.jpg', price: '2299.00', title: '摩托罗拉 Moto Mods 摩眼-哈苏摄影模块' },
  { img: '/img/_/gocart01.jpg', price: '2299.00', title: '摩托罗拉 Moto Mods 摩眼-哈苏摄影模块' }
])
</script>

<style scoped>
.addCartSuccessFloor { background: Honeydew; padding: 10px 0; }
.addCartSuccessFloor__inner {
  width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto;
  gap: 16px;
}

/* 成功提示框 */
.successBox {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: grid;
  grid-template-columns: auto 1fr auto;
  gap: 20px;
  align-items: center;
}
.successBox__left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.successIcon {
  width: 40px;
  height: 40px;
  background: #4CAF50;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
}
.successText { font-size: 18px; font-weight: bold; color: #333; }
.successBox__goods {
  display: flex;
  align-items: center;
  gap: 12px;
  border-left: 1px solid #eee;
  padding-left: 20px;
}
.goodsPic img { width: 80px; height: 80px; object-fit: cover; border-radius: 4px; }
.goodsInfo .goodsTitle { font-size: 13px; color: #333; margin: 0 0 6px; }
.goodsInfo .goodsAttr { font-size: 12px; color: #999; margin: 0; }
.successBox__action { display: flex; flex-direction: column; gap: 8px; }
.btnDetail {
  border: 1px solid #ddd;
  padding: 8px 20px;
  border-radius: 4px;
  font-size: 13px;
  color: #666;
  text-decoration: none;
  text-align: center;
}
.btnCart {
  background: #c81623;
  color: #fff;
  padding: 8px 20px;
  border-radius: 4px;
  font-size: 13px;
  text-decoration: none;
  text-align: center;
}

/* 推荐区 */
.recommendBox {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
}
.recommendBox__title {
  margin: 0 0 12px;
  font-size: 14px;
  color: #333;
  border-left: 3px solid #c81623;
  padding-left: 8px;
}
.recommendBox__list {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 10px;
}
</style>
