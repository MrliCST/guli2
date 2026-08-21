<template>
  <section class="seckillBannerFloor">
    <div class="seckillBannerFloor__inner">
      <div class="banner">
        <img src="/img/_/banner.png" alt="秒杀活动" />
      </div>
      <div class="goodsGrid">
        <SeckillGoodsCard
          v-for="(g, i) in goods"
          :key="i"
          :img="g.img"
          :intro="g.intro"
          :sec-price="g.secPrice"
          :ever-price="g.everPrice"
          :sold="g.sold"
          :rate="g.rate"
          :left="g.left"
          link="/seckill-item"
        />
      </div>
      <div class="cdTop">
        <div class="topBtn" @click="scrollTop">
          <img src="/img/_/gotop.png" alt="" />
          <b>TOP</b>
        </div>
        <div class="codeBox">
          <img src="/img/_/code.png" alt="" />
        </div>
        <div class="erweima">
          <img src="/img/_/erweima.jpg" alt="" />
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import SeckillGoodsCard from '@/components/seckill/SeckillGoodsCard.vue'

const goods = ref([
  { img: '/img/_/list.jpg', intro: 'Apple iPhone 6s (A1699) 64G 玫瑰金色 移动联通电信4G手机', secPrice: '4299', everPrice: '5299', sold: '823', rate: 82, left: '177' },
  { img: '/img/_/list.jpg', intro: '华为 HUAWEI Mate 60 Pro 12+512G 雅丹黑 麒麟9000S', secPrice: '5999', everPrice: '6999', sold: '654', rate: 65, left: '346' },
  { img: '/img/_/list.jpg', intro: '小米14 Pro 16+512G 黑色 骁龙8 Gen3 徕卡光学镜头', secPrice: '4599', everPrice: '4999', sold: '912', rate: 91, left: '88' },
  { img: '/img/_/list.jpg', intro: 'OPPO Find X7 16+512G 海阔天空 天玑9300', secPrice: '3499', everPrice: '3999', sold: '567', rate: 57, left: '433' },
  { img: '/img/_/list.jpg', intro: 'vivo X100 Pro 12+256G 白月光 蔡司APO超级长焦', secPrice: '3299', everPrice: '3699', sold: '445', rate: 44, left: '555' },
  { img: '/img/_/list.jpg', intro: '荣耀 Magic6 12+256G 绒黑色 骁龙8 Gen3', secPrice: '2799', everPrice: '2999', sold: '783', rate: 78, left: '217' },
  { img: '/img/_/list.jpg', intro: '一加 12 16+512G 留白 骁龙8 Gen3 哈苏全焦段', secPrice: '2299', everPrice: '2499', sold: '334', rate: 33, left: '666' },
  { img: '/img/_/list.jpg', intro: '三星 Galaxy S24 Ultra 12+256G 钛灰色 第三代骁龙8', secPrice: '7999', everPrice: '8999', sold: '456', rate: 46, left: '544' },
  { img: '/img/_/list.jpg', intro: '努比亚 Z60 Ultra 16+512G 黑色 屏下摄像 骁龙8 Gen3', secPrice: '4299', everPrice: '4699', sold: '621', rate: 62, left: '379' },
  { img: '/img/_/list.jpg', intro: 'Redmi K70 Pro 16+512G 墨羽 骁龙8 Gen3 2K屏', secPrice: '2999', everPrice: '3299', sold: '876', rate: 88, left: '124' }
])

function scrollTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
</script>

<style scoped>
.seckillBannerFloor { background: #fff5f5; padding: 10px 0; }
.seckillBannerFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto auto;
  gap: 12px;
}
.banner {
  border-radius: 8px;
  overflow: hidden;
}
.banner img { width: 100%; height: 200px; object-fit: cover; }
.goodsGrid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 10px;
}
.cdTop {
  display: grid;
  grid-template-columns: auto auto;
  gap: 10px;
  justify-content: end;
  padding: 10px 0;
}
.topBtn {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}
.topBtn img { width: 40px; height: 40px; }
.topBtn b { font-size: 12px; color: #999; }
.codeBox img { width: 40px; height: 40px; }
.erweima {
  position: relative;
}
.erweima img { width: 100px; height: 100px; border-radius: 4px; }
</style>
