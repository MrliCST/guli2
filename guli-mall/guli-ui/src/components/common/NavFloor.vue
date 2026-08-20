<template>
  <section class="navFloor">
    <div class="navFloor__inner">
      <div class="cell navFloor__welcome">
        谷粒商城欢迎您!请 <a href="#">登录</a> <a href="#">免费注册</a>
      </div>
      <div class="cell navFloor__links">
        <template v-for="item in links" :key="item.label">
          <a v-if="item.href" :href="item.href">{{ item.label }}</a>
          <span v-else>{{ item.label }}</span>
        </template>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

// 顶部快捷导航:有 href 的渲染为链接,无 href 的渲染为纯文本
const links = ref([
  { label: '我的订单' },
  { label: '我的购物车', href: '#' },
  { label: '我的谷粒', href: '#' },
  { label: '谷粒会员' },
  { label: '企业采购' },
  { label: '关注谷粒' },
  { label: '合作招商', href: '#' },
  { label: '商家后台', href: '#' }
])
</script>

<style scoped>
.navFloor { background: AliceBlue; padding: 10px 0; }
.navFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  min-height: 40px;
  align-items: center;
  gap: 12px;
}
.navFloor__welcome { background: LightBlue; }
.navFloor__links   { background: PaleTurquoise; }
.navFloor__links a,
.navFloor__links span { margin: 0 6px; }
.navFloor__links span { color: #555; }
</style>
