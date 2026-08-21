<template>
  <section class="likeFloor">
    <div class="likeFloor__inner">
      <div class="likeFloor__title">
        <b>猜你喜欢</b>
      </div>
      <div class="likeFloor__content">
        <div class="goodsCard" v-for="g in goods" :key="g.name">
          <img :src="g.img" alt="" />
          <div class="name">{{ g.name }}</div>
          <div class="price">{{ g.price }}</div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const goods = ref([
  { img: '/img/like_01.png', name: '飞科电动剃须刀 三刀头', price: '¥159' },
  { img: '/img/like_02.png', name: '九阳破壁机 家用全自动', price: '¥599' },
  { img: '/img/like_03.png', name: '兰蔻小黑瓶 精华肌底液50ml', price: '¥780' },
  { img: '/img/like_04.png', name: '耐克运动鞋 男款透气跑步鞋', price: '¥499' },
  { img: '/img/like_05.png', name: '小米手环8 NFC版', price: '¥249' },
  { img: '/img/like_06.png', name: '膳魔师保温杯 316不锈钢', price: '¥189' }
])
</script>

<style scoped>
.likeFloor { background: FloralWhite; padding: 10px 0; }
.likeFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto;
  gap: 10px;
}
.likeFloor__title {
  background: Plum;
  padding: 10px 14px;
  color: #fff;
  border-radius: 6px;
}
.likeFloor__title b { color: #fff; font-size: 16px; }
.likeFloor__content {
  background: rgba(255, 255, 255, 0.5);
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 10px;
  padding: 10px;
  border-radius: 6px;
}
.likeFloor__content .goodsCard {
  background: #fff;
  padding: 8px;
  border-radius: 4px;
  font-size: 12px;
  display: grid;
  grid-template-rows: auto auto auto;
  gap: 6px;
}
.likeFloor__content .goodsCard img { width: 100%; height: 120px; object-fit: cover; border-radius: 4px; }
.likeFloor__content .goodsCard .name { color: #333; line-height: 1.4; min-height: 34px; }
.likeFloor__content .goodsCard .price { color: var(--theme-red); font-weight: bold; font-size: 14px; }
</style>
