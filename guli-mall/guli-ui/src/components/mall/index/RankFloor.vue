<template>
  <section class="rankFloor">
    <div class="rankFloor_inner">
      <!-- 选择图标 -->
      <div class="rankFloor_tabs" @click="onTabClick">
        <!-- 热卖排行 -->
        <div
          class="tabItem"
          :class="{ active: isActive(0) }"
          data-index="0"
        >
          <div class="tabBg hotRank"></div>
          <span>热卖排行</span>
        </div>
        <!-- 特价排行 -->
        <div
          class="tabItem"
          :class="{ active: isActive(1) }"
          data-index="1"
        >
          <div class="tabBg onSaleRank"></div>
          <span>特价排行</span>
        </div>
        <!-- 新品排行 -->
        <div
          class="tabItem"
          :class="{ active: isActive(2) }"
          data-index="2"
        >
          <div class="tabBg newRank"></div>
          <span>新品排行</span>
        </div>
      </div>
      <!-- 展示内容 -->
      <div class="rankFloor_content">
        <div class="goodsCard" v-for="g in goods" :key="g.name">
          <img :src="g.img" alt="" />
          <span class="name">{{ g.name }}</span>
          <span class="price">{{ g.price }}</span>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const activeTab = ref(0)

const isActive = (idx: number) => activeTab.value === idx

const onTabClick = (e: MouseEvent) => {
  const el = (e.target as HTMLElement).closest('.tabItem') as HTMLElement | null
  if (el) {
    activeTab.value = Number(el.dataset.index)
  }
}

const goods = ref([
  { img: '/mall/index/rank-1.jpg', name: 'Apple iPhone 15 Pro 256G 银色', price: '¥8999' },
  { img: '/mall/index/rank-2.jpg', name: '小米电视6 65英寸 4K高清', price: '¥2999' },
  { img: '/mall/index/rank-1.jpg', name: '联想拯救者 R9000P 游戏本', price: '¥6499' },
  { img: '/mall/index/rank-2.jpg', name: '美的变频空调 1.5匹 一级能效', price: '¥2299' },
  { img: '/mall/index/rank-1.jpg', name: '华为 MatePad Pro 13.2英寸', price: '¥3699' }
])
</script>

<style scoped lang="scss">
/** 排行榜层 */
.rankFloor {
  margin-top: 10px;

  /** 总体排版 */
  .rankFloor_inner {
    display: grid;
    grid-template-rows: auto auto;
    gap: 12px;
  }

  /** 排行按钮 */
  .rankFloor_tabs {
    display: grid;
    grid-template-columns: repeat(3, 70px);
    justify-content: center;
    gap: 20px;
    background: #fff;

    .tabItem {
      display: flex;
      flex-direction: column;
      align-items: center;
      font-size: 14px;
      color: #444;
      cursor: pointer;

      .tabBg {
        width: 35px;
        height: 35px;
        background-size: 70px 35px;
        background-position: left center;
        background-repeat: no-repeat;

        &.hotRank {
          background-image: url('@/assets/mall/index/hotRank.png');
        }

        &.onSaleRank {
          background-image: url('@/assets/mall/index/onSaleRank.png');
        }

        &.newRank {
          background-image: url('@/assets/mall/index/newRank.png');
        }
      }

      &.active {
        color: red;

        .tabBg {
          background-position: right center;
        }
      }
    }
  }

  /** 商品展示内容 */
  .rankFloor_content {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 10px;

    /** 商品展示 */
    .goodsCard {
      display: grid;
      background: #fff;
      padding: 10px;
      border-radius: 4px;
      border: 1px solid #c81623;
      font-size: 12px;

      grid-template-rows: auto auto auto;
      gap: 6px;

      img {
        width: 100%;
        height: 130px;
        object-fit: cover;
        border-radius: 4px;
      }

      .name {
        color: #333;
        line-height: 1.4;
      }

      .price {
        color: #c81623;
        font-weight: bold;
        font-size: 14px;
      }
    }
  }
}
</style>
