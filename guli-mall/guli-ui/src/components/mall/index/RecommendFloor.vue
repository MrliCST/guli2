<template>
  <section class="recommendFloor">
    <div class="recommendFloor_inner">
      <div class="recommendFloor_clock">
        <img src="/img/clock.png" alt="" />
        <h3>今日推荐</h3>
      </div>
      <div class="recommendFloor_item" v-for="item in items" :key="item.name">
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

<style scoped lang="scss">
.recommendFloor {
  /**各层负责与上层的空隙，不制造向下的空隙 */
  margin-top: 10px;

  .recommendFloor_inner {
    display: grid;
    grid-template-columns: 210px 1fr 1fr 1fr 1fr;

    /** 每日推荐logo */
    .recommendFloor_clock {
      background: RosyBrown;
      color: #fff;
      min-height: 210px;
      display: grid;
      align-content: center;  /** 两个子项先居中，然后在 sketch */
      justify-items: center;
      gap: 8px;
      text-align: center;

      img {
        width: 80px;
        height: 80px;
        object-fit: contain;
        filter: brightness(0) invert(1);
      }

      h3 {
        font-size: 18px;
      }
    }

    /** 每日推荐商品 */
    .recommendFloor_item {
      background: #fff;
      min-height: 200px;
      overflow: hidden;
      padding: 0;
      border: none;
      display: grid;
      grid-template-rows: 1fr auto;

      img {
        width: 100%;
        height: 140px;
        object-fit: cover;
      }

      .goodsInfo {
        padding: 8px;

        .name {
          font-size: 12px;
          color: #333;
          line-height: 1.4;
        }

        .price {
          font-weight: bold;
          margin-top: 4px;
        }
      }
    }
  }
}
</style>
