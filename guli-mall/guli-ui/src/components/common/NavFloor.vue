<template>
  <section class="navFloor">
    <div class="welcome">
      <span>谷粒商城欢迎您!</span><a href="#">请登录</a><a href="#">免费注册</a>
    </div>
    <div class="links">
      <template v-for="item in links" :key="item.label">
        <a v-if="item.href" :href="item.href">{{ item.label }}</a>
        <span v-else>{{ item.label }}</span>
      </template>
    </div>
  </section>
</template>

<script setup lang="ts">
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

<style scoped lang="scss">
/** 顶部导航 */
.navFloor {
  background-color: #eaeaea;
  height: 30px;
  display: grid;
  grid-template-columns: auto auto;
  align-items: center; /** column */
  justify-content: space-between; /** row */
  font-size: 13px;

  /** span和a的样式 */
  a,span {
    margin-right: 12px;
    padding-right: 12px;
    border-right: 1px solid #ccc;

    /** 首尾子元素的样式 */
    &:last-child {
      margin-right: 12px;
      padding-right: 0;
      border-right: none;
    }
  }

  /** 取消a标签默认样式 */
  a {
    color: inherit;
    text-decoration: none;
  }

  /** 欢迎语 */
  .welcome {
    span:first-child {
      margin-left: 12px;
    }
  }
}
</style>
