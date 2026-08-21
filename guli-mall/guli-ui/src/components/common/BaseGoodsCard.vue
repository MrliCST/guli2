<template>
  <div class="goodsCard">
    <div class="goodsCard__img">
      <img :src="img" alt="" />
    </div>
    <div class="goodsCard__price">
      <strong><em>¥</em><i>{{ price }}</i></strong>
    </div>
    <div class="goodsCard__attr">
      <a href="#">{{ title }}</a>
    </div>
    <div class="goodsCard__commit" v-if="comment">
      已有<span>{{ comment }}</span>人评价
    </div>
    <div class="goodsCard__operate" v-if="showOperate">
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

<style scoped>
.goodsCard {
  background: #fff;
  padding: 12px;
  border-radius: 6px;
  display: grid;
  grid-template-rows: auto auto auto auto auto;
  gap: 8px;
  transition: box-shadow 0.2s;
}
.goodsCard:hover { box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }
.goodsCard__img {
  overflow: hidden;
  border-radius: 4px;
}
.goodsCard__img img { width: 100%; height: 160px; object-fit: cover; }
.goodsCard__price strong { color: var(--theme-red); font-size: 18px; }
.goodsCard__price em { font-style: normal; font-size: 12px; margin-right: 2px; }
.goodsCard__price i { font-style: normal; }
.goodsCard__attr a {
  font-size: 12px; color: #333; line-height: 1.5;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
  overflow: hidden;
}
.goodsCard__attr a:hover { color: var(--theme-red); }
.goodsCard__commit { font-size: 12px; color: #999; }
.goodsCard__commit span { color: #e4393c; font-weight: bold; }
.goodsCard__operate {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 6px;
}
.goodsCard__operate .btn-cart {
  background: var(--theme-red); color: #fff;
  text-align: center; padding: 6px 0;
  border-radius: 4px; font-size: 12px;
}
.goodsCard__operate .btn-fav {
  border: 1px solid #ddd; color: #666;
  text-align: center; padding: 5px 0;
  border-radius: 4px; font-size: 12px;
}
</style>
