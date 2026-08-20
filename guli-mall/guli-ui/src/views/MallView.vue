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

<script setup>
import NavFloor from '../components/common/NavFloor.vue'
import HeaderFloor from '../components/common/HeaderFloor.vue'
import CategoryFloor from '../components/common/CategoryFloor.vue'
import BannerFloor from '../components/index/BannerFloor.vue'
import RecommendFloor from '../components/index/RecommendFloor.vue'
import RankFloor from '../components/index/RankFloor.vue'
import LikeFloor from '../components/common/LikeFloor.vue'
import HomeApplianceFloor from '../components/index/HomeApplianceFloor.vue'
import MobileFloor from '../components/index/MobileFloor.vue'
import BrandFloor from '../components/index/BrandFloor.vue'
import FooterFloor from '../components/common/FooterFloor.vue'
import FloorIndex from '../components/index/FloorIndex.vue'
import SideToolbar from '../components/common/SideToolbar.vue'
</script>

<style scoped>
/* 整体大框架:纵向 Grid 划分 11 层;fixed 侧栏不占轨道 */
.mall-page {
  display: grid;
  grid-template-rows: repeat(11, auto);
}
</style>
