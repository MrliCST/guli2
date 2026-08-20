<template>
  <section class="categoryFloor">
    <div class="categoryFloor__inner">
      <div class="cell categoryFloor__allSorts">
        <b>全部商品分类</b>
      </div>
      <div class="categoryFloor__navList">
        <div class="navItem" v-for="item in navItems" :key="item">{{ item }}</div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

const navItems = ref(['服装城', '美妆馆', '超市', '生鲜', '全球购', '闪购', '拍卖'])
</script>

<style scoped>
.categoryFloor { background: Honeydew; padding: 10px 0; }
.categoryFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-columns: 210px 1fr;
  gap: 12px;
  min-height: 44px;
  align-items: center;
}
.categoryFloor__allSorts {
  background: var(--theme-red);
  color: #fff;
  padding: 10px 14px;
  border: none;
}
.categoryFloor__allSorts b { color: #fff; }
.categoryFloor__navList {
  background: LightGreen;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 8px;
}
.categoryFloor__navList .navItem {
  background: rgba(255, 255, 255, 0.7);
  text-align: center;
  padding: 8px 0;
  border-radius: 4px;
  font-size: 13px;
  color: #444;
}
</style>
