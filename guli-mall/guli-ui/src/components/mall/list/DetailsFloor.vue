<template>
  <section class="detailsFloor">
    <div class="detailsFloor_inner">
      <!-- 排序栏 -->
      <div class="detailsFloor_sortBar">
        <div
          class="sortItem"
          :class="{ active: isSortActive(idx) }"
          v-for="(item, idx) in sortItems"
          :key="item"
          @click="activeSort = idx"
        >{{ item }}</div>
      </div>

      <!-- 商品网格 -->
      <div class="detailsFloor_goods">
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
      <div class="detailsFloor_pagination">
        <span class="pageItem prev" :class="{ disabled: isPrevDisabled }">« 上一页</span>
        <span
          class="pageItem"
          :class="{ active: isPageActive(p) }"
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

<script setup lang="ts">
import { ref, computed } from 'vue'
import BaseGoodsCard from '@/components/common/BaseGoodsCard.vue'

const sortItems = ['综合', '销量', '新品', '评价', '价格 ↑', '价格 ↓']
const activeSort = ref(0)
const isSortActive = (idx: number) => idx === activeSort.value
const activePage = ref(1)
const isPrevDisabled = computed(() => activePage.value === 1)
const isPageActive = (p: number) => activePage.value === p
const pages = [1, 2, 3, 4, 5]

const goods = ref([
  { img: '/mall/list/mobile01.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699)', comment: '2000' },
  { img: '/mall/list/mobile02.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699)', comment: '2000' },
  { img: '/mall/list/mobile03.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699)', comment: '2000' },
  { img: '/mall/list/mobile04.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699) Apple苹果iPhone 6s (A1699)', comment: '2000' },
  { img: '/mall/list/mobile05.png', price: '5288.00', title: '华为 HUAWEI Mate 60 Pro 12+512G 雅丹黑 麒麟9000S', comment: '3500' },
  { img: '/mall/list/mobile06.png', price: '4999.00', title: '小米14 Pro 16+512G 黑色 骁龙8 Gen3 徕卡光学镜头', comment: '1800' },
  { img: '/mall/list/mobile01.png', price: '3999.00', title: 'OPPO Find X7 16+512G 海阔天空 天玑9300', comment: '1200' },
  { img: '/mall/list/mobile02.png', price: '3699.00', title: 'vivo X100 Pro 12+256G 白月光 蔡司APO超级长焦', comment: '900' },
  { img: '/mall/list/mobile03.png', price: '2999.00', title: '荣耀 Magic6 12+256G 绒黑色 骁龙8 Gen3', comment: '1500' },
  { img: '/mall/list/mobile04.png', price: '2499.00', title: '一加 12 16+512G 留白 骁龙8 Gen3 哈苏全焦段', comment: '800' }
])
</script>

<style scoped lang="scss">
.detailsFloor {
  padding: 10px 0;
}

.detailsFloor_inner {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto auto;
  gap: 12px;
}

/* 排序栏 */
.detailsFloor_sortBar {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  overflow: hidden;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);

  .sortItem {
    padding: 12px 0;
    text-align: center;
    font-size: 13px;
    color: #555;
    cursor: pointer;
    border-right: 1px solid #eee;
    transition: background 0.2s ease, color 0.2s ease;

    &:last-child {
      border-right: none;
    }

    &.active {
    color: red;
    font-weight: bold;
    }
  }
}

/* 商品网格 */
.detailsFloor_goods {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 12px;
}

/* 分页 */
.detailsFloor_pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  padding: 16px 0;

  .pageItem {
    padding: 6px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 13px;
    color: #555;
    cursor: pointer;
    transition: border-color 0.2s ease, color 0.2s ease, background 0.2s ease;

    &.active {
      color: red;
      font-weight: bold;
    }

    &:hover:not(.active):not(.disabled) {
      border-color: red;
      color: red;
      font-weight: bold;
    }

    &.disabled {
      color: #ccc;
      cursor: not-allowed;
    }
  }

  .pageDotted {
    color: #999;
  }

  .pageInfo {
    font-size: 12px;
    color: #999;
    margin-left: 8px;
  }
}
</style>
