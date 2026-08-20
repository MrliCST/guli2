<template>
  <section class="hotSaleFloor">
    <div class="hotSaleFloor__inner">
      <div class="hotSaleFloor__title">
        <b>热卖商品</b>
      </div>
      <div class="hotSaleFloor__list">
        <BaseGoodsCard
          v-for="(g, i) in goods"
          :key="i"
          :img="g.img"
          :price="g.price"
          :title="g.title"
          :comment="g.comment"
          :show-operate="false"
        />
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import BaseGoodsCard from '../common/BaseGoodsCard.vue'

const goods = ref([
  { img: '/img/like_01.png', price: '4088.00', title: 'Apple苹果iPhone 6s (A1699)', comment: '700' },
  { img: '/img/like_03.png', price: '4088.00', title: '金属A面，360°翻转，APP下单省300！', comment: '700' },
  { img: '/img/like_04.png', price: '4068.00', title: '256SSD商务大咖，完爆职场，APP下单立减200', comment: '20' },
  { img: '/img/like_02.png', price: '4088.00', title: 'Apple苹果iPhone 6s (A1699)', comment: '700' }
])
</script>

<style scoped>
.hotSaleFloor { background: FloralWhite; padding: 10px 0; }
.hotSaleFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto;
  gap: 10px;
}
.hotSaleFloor__title {
  background: Plum;
  padding: 10px 14px;
  border-radius: 6px;
}
.hotSaleFloor__title b { color: #fff; font-size: 16px; }
.hotSaleFloor__list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}
</style>
