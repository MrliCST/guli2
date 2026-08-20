<template>
  <section class="detailsFloor">
    <div class="detailsFloor__inner">
      <!-- 排序栏 -->
      <div class="detailsFloor__sortBar">
        <div
          class="sortItem"
          :class="{ active: idx === activeSort }"
          v-for="(item, idx) in sortItems"
          :key="item"
          @click="activeSort = idx"
        >{{ item }}</div>
      </div>

      <!-- 商品网格 -->
      <div class="detailsFloor__goods">
        <BaseGoodsCard
          v-for="(g, i) in goods"
          :key="i"
          :img="g.img"
          :price="g.price"
          :title="g.title"
          :comment="g.comment"
          :show-operate="true"
        />
      </div>

      <!-- 分页 -->
      <div class="detailsFloor__pagination">
        <span class="pageItem prev" :class="{ disabled: activePage === 1 }">« 上一页</span>
        <span
          class="pageItem"
          :class="{ active: activePage === p }"
          v-for="p in pages"
          :key="p"
          @click="activePage = p"
        >{{ p }}</span>
        <span class="pageDotted">...</span>
        <span class="pageItem next" @click="activePage = Math.min(activePage + 1, 10)">下一页 »</span>
        <span class="pageInfo">共10页</span>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import BaseGoodsCard from '../common/BaseGoodsCard.vue'

const sortItems = ['综合', '销量', '新品', '评价', '价格 ↑', '价格 ↓']
const activeSort = ref(0)
const activePage = ref(1)
const pages = [1, 2, 3, 4, 5]

const goods = ref([
  { img: '/img/_/mobile01.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699)', comment: '2000' },
  { img: '/img/_/mobile02.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699)', comment: '2000' },
  { img: '/img/_/mobile03.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699)', comment: '2000' },
  { img: '/img/_/mobile04.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699)', comment: '2000' },
  { img: '/img/_/mobile05.png', price: '5288.00', title: '华为 HUAWEI Mate 60 Pro 12+512G 雅丹黑 麒麟9000S', comment: '3500' },
  { img: '/img/_/mobile06.png', price: '4999.00', title: '小米14 Pro 16+512G 黑色 骁龙8 Gen3 徕卡光学镜头', comment: '1800' },
  { img: '/img/_/mobile01.png', price: '3999.00', title: 'OPPO Find X7 16+512G 海阔天空 天玑9300', comment: '1200' },
  { img: '/img/_/mobile02.png', price: '3699.00', title: 'vivo X100 Pro 12+256G 白月光 蔡司APO超级长焦', comment: '900' },
  { img: '/img/_/mobile03.png', price: '2999.00', title: '荣耀 Magic6 12+256G 绒黑色 骁龙8 Gen3', comment: '1500' },
  { img: '/img/_/mobile04.png', price: '2499.00', title: '一加 12 16+512G 留白 骁龙8 Gen3 哈苏全焦段', comment: '800' }
])
</script>

<style scoped>
.detailsFloor { background: Seashell; padding: 10px 0; }
.detailsFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto auto;
  gap: 12px;
}

/* 排序栏 */
.detailsFloor__sortBar {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 6px;
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  overflow: hidden;
}
.detailsFloor__sortBar .sortItem {
  padding: 12px 0;
  text-align: center;
  font-size: 13px;
  color: #555;
  cursor: pointer;
  border-right: 1px solid #eee;
}
.detailsFloor__sortBar .sortItem:last-child { border-right: none; }
.detailsFloor__sortBar .sortItem.active {
  background: var(--theme-red);
  color: #fff;
}
.detailsFloor__sortBar .sortItem:hover:not(.active) {
  background: #f5f5f5;
}

/* 商品网格 */
.detailsFloor__goods {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 10px;
}

/* 分页 */
.detailsFloor__pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  padding: 16px 0;
}
.detailsFloor__pagination .pageItem {
  padding: 6px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 13px;
  color: #555;
  cursor: pointer;
}
.detailsFloor__pagination .pageItem.active {
  background: var(--theme-red);
  color: #fff;
  border-color: var(--theme-red);
}
.detailsFloor__pagination .pageItem:hover:not(.active):not(.disabled) {
  border-color: var(--theme-red);
  color: var(--theme-red);
}
.detailsFloor__pagination .pageItem.disabled {
  color: #ccc;
  cursor: not-allowed;
}
.detailsFloor__pagination .pageDotted { color: #999; }
.detailsFloor__pagination .pageInfo { font-size: 12px; color: #999; margin-left: 8px; }
</style>
