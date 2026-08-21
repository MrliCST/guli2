<template>
  <div class="item-page">
    <!-- 复用公共组件 -->
    <NavFloor />
    <HeaderFloor />
    <CategoryFloor />

    <!-- 详情页独有组件 -->
    <ProductInfoFloor />
    <ProductDetailFloor />

    <!-- 复用公共组件 -->
    <LikeFloor />
    <FooterFloor />

    <!-- 固定悬浮 -->
    <SideToolbar />
  </div>
</template>

<script setup lang="ts">
import NavFloor from '@/components/common/NavFloor.vue'
import HeaderFloor from '@/components/common/HeaderFloor.vue'
import CategoryFloor from '@/components/common/CategoryFloor.vue'
import ProductInfoFloor from '@/components/mall/item/ProductInfoFloor.vue'
import ProductDetailFloor from '@/components/mall/item/ProductDetailFloor.vue'
import LikeFloor from '@/components/common/LikeFloor.vue'
import FooterFloor from '@/components/common/FooterFloor.vue'
import SideToolbar from '@/components/common/SideToolbar.vue'
</script>

<style scoped>
.item-page {
  display: grid;
  grid-template-rows: repeat(7, auto);
}
</style>
