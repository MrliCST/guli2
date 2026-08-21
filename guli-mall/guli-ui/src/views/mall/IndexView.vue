<template>
  <div class="mall-page">
    <!-- 主流程:11 个业务层,由整体 Grid 纵向排列 -->
    <NavFloor />
    <HeaderFloor />
    <CategoryFloor />
    <BannerFloor />
    <RecommendFloor />
    <RankFloor />
    <LikeFloor />
    <HomeApplianceFloor />
    <MobileFloor />
    <BrandFloor />
    <FooterFloor />

    <!-- 固定悬浮:不参与主 Grid -->
    <FloorIndex />
    <SideToolbar />
  </div>
</template>

<script setup lang="ts">
import NavFloor from '@/components/common/NavFloor.vue'
import HeaderFloor from '@/components/common/HeaderFloor.vue'
import CategoryFloor from '@/components/common/CategoryFloor.vue'
import BannerFloor from '@/components/mall/index/BannerFloor.vue'
import RecommendFloor from '@/components/mall/index/RecommendFloor.vue'
import RankFloor from '@/components/mall/index/RankFloor.vue'
import LikeFloor from '@/components/common/LikeFloor.vue'
import HomeApplianceFloor from '@/components/mall/index/HomeApplianceFloor.vue'
import MobileFloor from '@/components/mall/index/MobileFloor.vue'
import BrandFloor from '@/components/mall/index/BrandFloor.vue'
import FooterFloor from '@/components/common/FooterFloor.vue'
import FloorIndex from '@/components/mall/index/FloorIndex.vue'
import SideToolbar from '@/components/common/SideToolbar.vue'
</script>

<style scoped>
/*
  把页面化为11个层，层的高度为 auto
  相当于: grid-template-rows: auto, auto, ...., auto; (共11个)
*/
.mall-page {
  width: 80%;
  margin: 0 auto;
  display: grid;
  grid-template-rows: repeat(11, auto);
}
</style>
