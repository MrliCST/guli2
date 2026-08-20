<template>
  <div class="seckillCard">
    <div class="seckillCard__pic">
      <img :src="img" alt="" />
    </div>
    <div class="seckillCard__intro">{{ intro }}</div>
    <div class="seckillCard__price">
      <b class="secPrice">￥{{ secPrice }}</b>
      <b class="everPrice">￥{{ everPrice }}</b>
    </div>
    <div class="seckillCard__num">
      <div class="sold">已售{{ sold }}</div>
      <div class="progress">
        <div class="progressBar" :style="{ width: rate + '%' }"></div>
      </div>
      <div class="left">剩余<b>{{ left }}</b>件</div>
    </div>
    <a class="seckillCard__btn" :href="link">立即抢购</a>
  </div>
</template>

<script setup>
defineProps({
  img: { type: String, default: '/img/_/list.jpg' },
  intro: { type: String, default: '' },
  secPrice: { type: String, default: '0' },
  everPrice: { type: String, default: '0' },
  sold: { type: String, default: '0' },
  rate: { type: Number, default: 70 },
  left: { type: String, default: '0' },
  link: { type: String, default: '#' }
})
</script>

<style scoped>
.seckillCard {
  background: #fff;
  border-radius: 6px;
  padding: 12px;
  display: grid;
  grid-template-rows: auto auto auto auto auto;
  gap: 8px;
  transition: box-shadow 0.2s;
}
.seckillCard:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.12); }
.seckillCard__pic {
  overflow: hidden;
  border-radius: 4px;
}
.seckillCard__pic img { width: 100%; height: 180px; object-fit: cover; }
.seckillCard__intro {
  font-size: 12px;
  color: #333;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.seckillCard__price { display: flex; align-items: baseline; gap: 8px; }
.secPrice { color: var(--theme-red); font-size: 18px; }
.everPrice { color: #999; font-size: 13px; text-decoration: line-through; font-weight: normal; }
.seckillCard__num { display: grid; grid-template-columns: auto 1fr auto; gap: 6px; align-items: center; font-size: 11px; color: #999; }
.sold { white-space: nowrap; }
.progress {
  height: 10px;
  background: #ffe0e0;
  border-radius: 5px;
  overflow: hidden;
}
.progressBar {
  height: 100%;
  background: linear-gradient(90deg, #ff6b6b, var(--theme-red));
  border-radius: 5px;
  transition: width 0.3s;
}
.left b { color: var(--theme-red); }
.seckillCard__btn {
  background: linear-gradient(135deg, #ff6b6b, var(--theme-red));
  color: #fff;
  text-align: center;
  padding: 8px 0;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  text-decoration: none;
}
.seckillCard__btn:hover { opacity: 0.9; }
</style>
