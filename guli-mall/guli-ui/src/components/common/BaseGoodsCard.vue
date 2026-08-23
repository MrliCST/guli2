<template>
  <div class="goodsCard">
    <div class="goodsCard_img">
      <img :src="img" alt="" />
    </div>
    <div class="goodsCard_price">
      <strong><em>¥</em><i>{{ price }}</i></strong>
    </div>
    <div class="goodsCard_attr">
      <a href="#">{{ title }}</a>
    </div>
    <div class="goodsCard_commit" v-if="comment">
      已有<span>{{ comment }}</span>人评价
    </div>
    <div class="goodsCard_operate" v-if="showOperate">
      <a href="#" class="btn-cart">加入购物车</a>
      <a href="#" class="btn-fav">收藏</a>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  img: { type: String, required: true },
  price: { type: String, default: '0.00' },
  title: { type: String, default: '' },
  comment: { type: String, default: '' },
  showOperate: { type: Boolean, default: true }
})
</script>

<style scoped lang="scss">
.goodsCard {
  background: #fff;
  padding: 12px;
  border-radius: 6px;
  display: grid;
  grid-template-rows: auto auto auto auto auto;
  gap: 8px;
  transition: box-shadow 0.2s;

  &:hover { box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }

  .goodsCard_img {
    overflow: hidden;
    border-radius: 4px;

    img { width: 100%; height: 160px; object-fit: cover; }
  }

  .goodsCard_price {
    strong { color: #c81623; font-size: 18px; }
    em { font-style: normal; font-size: 12px; margin-right: 2px; }
    i { font-style: normal; }
  }

  .goodsCard_attr {
    a {
      font-size: 12px;
      color: #333;
      line-height: 1.5;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      overflow: hidden;

      &:hover { color: #c81623; }
    }
  }

  .goodsCard_commit {
    font-size: 12px; color: #999;

    span { color: #e4393c; font-weight: bold; }
  }

  .goodsCard_operate {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 6px;

    .btn-cart {
      background: #c81623; color: #fff;
      text-align: center; padding: 6px 0;
      border-radius: 4px; font-size: 12px;
    }

    .btn-fav {
      border: 1px solid #ddd; color: #666;
      text-align: center; padding: 5px 0;
      border-radius: 4px; font-size: 12px;
    }
  }
}
</style>
