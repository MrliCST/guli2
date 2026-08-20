<template>
  <section class="rankFloor">
    <div class="rankFloor__inner">
      <div class="rankFloor__tabs">
        <div
          class="tabItem"
          :class="{ active: idx === activeTab }"
          v-for="(tab, idx) in tabs"
          :key="tab"
          @click="activeTab = idx"
        >{{ tab }}</div>
      </div>
      <div class="rankFloor__content">
        <div class="goodsCard" v-for="g in goods" :key="g.name">
          <img :src="g.img" alt="" />
          <div class="name">{{ g.name }}</div>
          <div class="price">{{ g.price }}</div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

const tabs = ref(['热卖排行', '特价排行', '新品排行'])
const activeTab = ref(0)

const goods = ref([
  { img: '/img/1.jpg', name: 'Apple iPhone 15 Pro 256G 银色', price: '¥8999' },
  { img: '/img/2.jpg', name: '小米电视6 65英寸 4K高清', price: '¥2999' },
  { img: '/img/1.jpg', name: '联想拯救者 R9000P 游戏本', price: '¥6499' },
  { img: '/img/2.jpg', name: '美的变频空调 1.5匹 一级能效', price: '¥2299' },
  { img: '/img/1.jpg', name: '华为 MatePad Pro 13.2英寸', price: '¥3699' }
])
</script>

<style scoped>
.rankFloor { background: Seashell; padding: 10px 0; }
.rankFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto;
  gap: 12px;
}
.rankFloor__tabs {
  background: Wheat;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  padding: 10px;
  border-radius: 6px;
}
.rankFloor__tabs .tabItem {
  background: rgba(255, 255, 255, 0.8);
  text-align: center;
  padding: 14px 0;
  border-radius: 4px;
  font-size: 14px;
  color: #444;
  cursor: pointer;
}
.rankFloor__tabs .tabItem.active { background: var(--theme-red); color: #fff; }
.rankFloor__content {
  background: Bisque;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 10px;
  padding: 10px;
  border-radius: 6px;
}
.rankFloor__content .goodsCard {
  background: #fff;
  padding: 10px;
  border-radius: 4px;
  font-size: 12px;
  display: grid;
  grid-template-rows: auto auto auto;
  gap: 6px;
}
.rankFloor__content .goodsCard img { width: 100%; height: 130px; object-fit: cover; border-radius: 4px; }
.rankFloor__content .goodsCard .name { color: #333; line-height: 1.4; min-height: 34px; }
.rankFloor__content .goodsCard .price { color: var(--theme-red); font-weight: bold; font-size: 14px; }
</style>
