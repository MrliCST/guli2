<template>
  <section class="hotSaleFloor">
    <div class="hotSaleFloor_inner">
      <div class="hotSaleFloor_title">
        <b>热卖商品</b>
      </div>
      <div class="hotSaleFloor_list">
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

<script setup lang="ts">
import { ref } from 'vue'
import BaseGoodsCard from '@/components/common/BaseGoodsCard.vue'

const goods = ref([
  { img: '/mall/index/like_01.png', price: '4088.00', title: 'Apple苹果iPhone 6s (A1699)', comment: '700' },
  { img: '/mall/index/like_03.png', price: '4088.00', title: '金属A面，360°翻转，APP下单省300！', comment: '700' },
  { img: '/mall/index/like_04.png', price: '4068.00', title: '256SSD商务大咖，完爆职场，APP下单立减200', comment: '20' },
  { img: '/mall/index/like_02.png', price: '4088.00', title: 'Apple苹果iPhone 6s (A1699)', comment: '700' }
])
</script>

<style scoped lang="scss">
.hotSaleFloor {
  padding: 10px 0;
}

.hotSaleFloor_inner {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto;
  gap: 10px;
}

/* 标题:左侧主题红竖条 + 加粗标题,无底色卡片化 */
.hotSaleFloor_title {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 4px;

  &::before {
    content: '';
    width: 4px;
    height: 18px;
    background: red;
    border-radius: 2px;
  }

  b {
    font-size: 18px;
    color: #333;
  }
}

.hotSaleFloor_list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}
</style>
