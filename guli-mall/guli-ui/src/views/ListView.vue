<template>
  <div class="list-page">
    <!-- 复用首页组件 -->
    <NavFloor />
    <HeaderFloor />
    <CategoryFloor />

    <!-- 列表页独有组件 -->
    <BreadcrumbFloor />
    <SelectorFloor />
    <DetailsFloor />
    <HotSaleFloor />

    <!-- 复用首页组件 -->
    <FooterFloor />

    <!-- 固定悬浮:不参与主 Grid -->
    <SideToolbar />
  </div>
</template>

<script setup>
import NavFloor from '../components/common/NavFloor.vue'
import HeaderFloor from '../components/common/HeaderFloor.vue'
import CategoryFloor from '../components/common/CategoryFloor.vue'
import BreadcrumbFloor from '../components/list/BreadcrumbFloor.vue'
import SelectorFloor from '../components/list/SelectorFloor.vue'
import DetailsFloor from '../components/list/DetailsFloor.vue'
import HotSaleFloor from '../components/list/HotSaleFloor.vue'
import FooterFloor from '../components/common/FooterFloor.vue'
import SideToolbar from '../components/common/SideToolbar.vue'
</script>

<style scoped>
/* 列表页大框架:纵向 Grid 划分 8 层;fixed 侧栏不占轨道 */
.list-page {
  display: grid;
  grid-template-rows: repeat(8, auto);
}
</style>
