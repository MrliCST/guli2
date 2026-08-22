<template>
  <section class="categoryFloor">
    <div class="categoryFloor_inner">
      <div class="cell categoryFloor_placeholder">
        全部商品分类
      </div>
      <div class="categoryFloor_navList">
        <div class="navItem" v-for="item in navItems" :key="item">{{ item }}</div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const navItems = ref(['服装城', '美妆馆', '超市', '生鲜', '全球购', '闪购', '拍卖','秒杀','团购','有趣'])
</script>

<style scoped lang="scss">
/** 种类层 */
.categoryFloor {
  border-bottom: 4px solid red;

  .categoryFloor_inner {
    display: grid;
    grid-template-columns: 210px 1fr;
    gap: 24px;
    min-height: 44px;
    align-items: center;

    /** 种类占位 */
    .categoryFloor_placeholder {
      background: red;
      text-align: center;
      font-weight: bold;
      color: #fff;
      padding: 10px 14px;
      border: none;
    }

    /** 导航条 */
    .categoryFloor_navList {
      display: grid;
      grid-template-columns: repeat(10, 1fr);
      gap: 8px;

      /** 导航项 */
      .navItem {
        background: rgba(255, 255, 255, 0.7);
        text-align: center;
        padding: 8px 0;
        font-size: 18px;
        color: #444;
      }
    }
  }
}
</style>
