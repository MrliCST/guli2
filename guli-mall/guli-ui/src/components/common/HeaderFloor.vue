<template>
  <section class="headerFloor">
    <div class="cell headerFloor_logo">
      <img src="/img/Logo1.png" alt="谷粒商城Logo" />
    </div>
    <div class="headerFloor_search">
      <input class="searchInput" v-model="keyword" type="text" placeholder="搜索商品" />
      <button class="searchBtn" @click="goSearch">搜索</button>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const keyword = ref('')
const router = useRouter()

function goSearch() {
  router.push('/list')
}
</script>

<style scoped>
.headerFloor {
  display: grid;
  grid-template-columns: 260px 1fr;
  gap: 16px;
  align-items: center;
  min-height: 100px;
}
.headerFloor_logo { height: 80px; }
.headerFloor_logo img { height: 80px; object-fit: contain; }
.headerFloor_search {
  display: grid;
  grid-template-columns: 1fr 110px;
  gap: 0;
  min-height: 44px;
  align-items: stretch;
}
.headerFloor_search .searchInput {
  background: #fff;
  border: 2px solid red;
  border-right: none;
  padding: 0 12px;
  font-size: 14px;
}
.headerFloor_search .searchBtn {
  background: red;
  color: #fff;
  border: none;
  font-size: 16px;
  cursor: pointer;
}
</style>
