<template>
  <section class="likeFloor">
    <div class="likeFloor_inner">
      <span class="likeFloor_title">猜你喜欢</span>
      <div class="likeFloor_content">
        <div class="goodsCard" v-for="g in goods" :key="g.name">
          <img :src="g.img" alt="" />
          <div class="info">
            <span class="name">{{ g.name }}</span>
            <span class="price">{{ g.price }}</span>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const goods = ref([
  { img: '/mall/index/like_01.png', name: '飞科电动剃须刀 三刀头', price: '¥159' },
  { img: '/mall/index/like_02.png', name: '九阳破壁机 家用全自动', price: '¥599' },
  { img: '/mall/index/like_03.png', name: '兰蔻小黑瓶 精华肌底液50ml', price: '¥780' },
  { img: '/mall/index/like_04.png', name: '耐克运动鞋 男款透气跑步鞋', price: '¥499' },
  { img: '/mall/index/like_05.png', name: '小米手环8 NFC版', price: '¥249' },
  { img: '/mall/index/like_06.png', name: '膳魔师保温杯 316不锈钢', price: '¥189' }
])
</script>

<style scoped lang="scss">
/** 猜你喜欢层 */
.likeFloor {
  margin-top: 20px;

  .likeFloor_inner {
    display: grid;
    grid-template-rows: auto auto;
    gap: 10px;
  }

  /** 标题 */
  .likeFloor_title {
    padding-top: 10px;
    font-weight: bold;
    font-size: 20px;
  }

  /** 内容 */
  .likeFloor_content {
    background: rgba(255, 255, 255, 0.5);
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 10px;
    padding: 10px;
    border: 1px solid #ccc;

    .goodsCard {
      background: #fff;
      padding: 10px;
      font-size: 12px;
      // display: grid 会将其子元素（含 span）强转为块级，保证每个商品信息占满一行
      display: grid;
      grid-template-rows: auto auto;
      gap: 6px;

      img {
        width: 100%;
        height: 150px;
        object-fit: cover;
      }

      .info {
        // display: grid 让 name/price 纵向排列，右边框放在容器上贯穿两行、不断口，且不含图片
        display: grid;
        grid-template-rows: auto auto;
        gap: 6px;
        border-right: 1px solid #ccc;

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

      // 最后一个商品卡右侧不露出边框
      &:last-child {
        .info {
          border-right: none;
        }
      }
    }
  }
}
</style>
