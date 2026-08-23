<template>
  <section class="headerFloor">
    <div class="cell headerFloor_logo">
      <img src="/img/logo.jpg" alt="谷粒商城Logo" />
    </div>
    <div class="headerFloor_search">
      <input class="searchInput" v-model="keyword" type="text" placeholder="搜索商品" @keyup.enter="goSearch" />
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
  const kw = keyword.value.trim()
  router.push({
    name: 'list',
    query: kw ? { keyword: kw } : {}
    })
}

function goHome() {
  router.push({ name: 'index' })
}
</script>

<style scoped lang="scss">
/** 头部层 */
.headerFloor {
  display: grid;
  grid-template-columns: 260px 1fr;
  gap: 32px;
  align-items: center;
  margin: 10px 0;

  /** logo图片 */
  .headerFloor_logo {
    height: 80px;
    cursor: pointer;

    img {
      height: 80px;
      object-fit: contain;
    }
  }

  /** 搜索包裹者 */
  .headerFloor_search {
    display: grid;
    grid-template-columns: 1fr 110px;
    min-height: 44px;

    /** 搜索框 */
    .searchInput {
      background: #fff;
      border: 2px solid red;
      border-right: none;
      padding: 0 12px;
      font-size: 14px;
      outline: none;
    }

    /** 搜索按钮 */
    .searchBtn {
      background: red;
      color: #fff;
      border: none;
      font-size: 16px;
      cursor: pointer;
    }
  }
}
</style>
