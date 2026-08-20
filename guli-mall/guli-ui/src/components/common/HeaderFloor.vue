<template>
  <section class="headerFloor">
    <div class="headerFloor__inner">
      <div class="cell headerFloor__logo">
        <img src="/img/Logo1.png" alt="谷粒商城Logo" />
      </div>
      <div class="headerFloor__search">
        <input class="searchInput" v-model="keyword" type="text" placeholder="搜索商品" />
        <button class="searchBtn" @click="goSearch">搜索</button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const keyword = ref('')
const router = useRouter()

function goSearch() {
  router.push('/list')
}
</script>

<style scoped>
.headerFloor { background: LightYellow; padding: 10px 0; }
.headerFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-columns: 260px 1fr;
  gap: 16px;
  align-items: center;
  min-height: 100px;
}
.headerFloor__logo { height: 80px; }
.headerFloor__logo img { height: 80px; object-fit: contain; }
.headerFloor__search {
  background: Khaki;
  display: grid;
  grid-template-columns: 1fr 110px;
  gap: 0;
  min-height: 44px;
  align-items: stretch;
}
.headerFloor__search .searchInput {
  background: #fff;
  border: 2px solid var(--theme-red);
  border-right: none;
  padding: 0 12px;
  font-size: 14px;
}
.headerFloor__search .searchBtn {
  background: var(--theme-red);
  color: #fff;
  border: none;
  font-size: 16px;
  cursor: pointer;
}
</style>
