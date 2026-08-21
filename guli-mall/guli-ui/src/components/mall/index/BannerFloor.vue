<template>
  <section class="bannerFloor">
    <div class="bannerFloor__inner">
      <div class="bannerFloor__carousel">
        <img src="/img/banner1.jpg" alt="Banner" />
      </div>
      <div class="bannerFloor__sidebar">
        <div class="cell bannerSidebar__news">
          <h4>谷粒快报 <span class="more">更多 &gt;</span></h4>
          <ul>
            <li v-for="n in news" :key="n.text">
              <span class="bold" :style="{ color: n.color }">[{{ n.tag }}]</span>{{ n.text }}
            </li>
          </ul>
        </div>
        <div class="cell bannerSidebar__services">
          <div class="serviceItem" v-for="s in services" :key="s">{{ s }}</div>
        </div>
        <div class="bannerSidebar__ad">
          <img src="/img/ad1.png" alt="广告" />
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const news = ref([
  { tag: '特惠', text: '备战开学季 全民半价购数码', color: '#c81623' },
  { tag: '公告', text: '谷粒会员日 爆品低至5折', color: '#c81623' },
  { tag: '特惠', text: '家电以旧换新 最高补贴800', color: '#c81623' },
  { tag: '公告', text: '部分地区物流时效调整通知', color: '#c81623' },
  { tag: '特惠', text: '美妆盛典 买一送一', color: '#c81623' }
])

const services = ref([
  '话费', '机票', '电影票', '游戏',
  '彩票', '加油', '酒店', '火车票',
  '众筹', '理财', '礼品卡', '白条'
])
</script>

<style scoped>
.bannerFloor { background: LightCyan; padding: 10px 0; }
.bannerFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-columns: 710px 1fr;
  gap: 12px;
}
.bannerFloor__carousel {
  background: LightSkyBlue;
  height: 380px;
  overflow: hidden;
  padding: 0;
  border: none;
  border-radius: 6px;
}
.bannerFloor__carousel img { width: 100%; height: 380px; object-fit: cover; }
.bannerFloor__sidebar {
  display: grid;
  grid-template-rows: auto auto 1fr;
  gap: 10px;
}
.bannerSidebar__news { background: PowderBlue; }
.bannerSidebar__news h4 { font-size: 14px; margin-bottom: 6px; }
.bannerSidebar__news h4 .more { float: right; color: #999; font-weight: normal; font-size: 12px; }
.bannerSidebar__news li { font-size: 12px; line-height: 1.9; }
.bannerSidebar__news li .bold { font-weight: bold; margin-right: 4px; }
.bannerSidebar__services {
  background: PaleTurquoise;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 6px;
  padding: 10px;
}
.bannerSidebar__services .serviceItem {
  background: rgba(255, 255, 255, 0.8);
  text-align: center;
  padding: 12px 0;
  border-radius: 4px;
  font-size: 12px;
  color: #555;
}
.bannerSidebar__ad {
  background: LightBlue;
  min-height: 110px;
  overflow: hidden;
  padding: 0;
  border: none;
  border-radius: 6px;
}
.bannerSidebar__ad img { width: 100%; height: 110px; object-fit: cover; }
</style>
