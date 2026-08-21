<template>
  <section class="recommendFloor">
    <div class="recommendFloor__inner">
      <div class="recommendFloor__clock">
        <img src="/img/clock.png" alt="" />
        <h3>今日推荐</h3>
      </div>
      <div class="recommendFloor__item" v-for="item in items" :key="item.name">
        <img :src="item.img" alt="" />
        <div class="goodsInfo">
          <div class="name">{{ item.name }}</div>
          <div class="price">{{ item.price }}</div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const items = ref([
  { img: '/img/today01.png', name: '米家空气净化器Pro 高效除甲醛', price: '¥1299' },
  { img: '/img/today02.png', name: '华为 Mate 60 Pro 12+512G', price: '¥6999' },
  { img: '/img/today03.png', name: '戴森 V12 无线吸尘器', price: '¥3990' },
  { img: '/img/today04.png', name: 'Apple AirPods Pro 2 主动降噪', price: '¥1799' }
])
</script>

<style scoped>
.recommendFloor { background: MistyRose; padding: 10px 0; }
.recommendFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 2fr 2fr 2fr 2fr;
  gap: 10px;
}
.recommendFloor__clock {
  background: RosyBrown;
  color: #fff;
  min-height: 200px;
  display: grid;
  place-items: center;
  text-align: center;
  border-radius: 6px;
}
.recommendFloor__clock img {
  width: 80px;
  height: 80px;
  object-fit: contain;
  margin-bottom: 8px;
  filter: brightness(0) invert(1);
}
.recommendFloor__clock h3 { font-size: 18px; }
.recommendFloor__item {
  background: #fff;
  min-height: 200px;
  overflow: hidden;
  padding: 0;
  border: none;
  border-radius: 6px;
  display: grid;
  grid-template-rows: 1fr auto;
}
.recommendFloor__item img { width: 100%; height: 140px; object-fit: cover; }
.recommendFloor__item .goodsInfo { padding: 8px; }
.recommendFloor__item .name { font-size: 12px; color: #333; line-height: 1.4; }
.recommendFloor__item .price { color: var(--theme-red); font-weight: bold; margin-top: 4px; }
</style>
