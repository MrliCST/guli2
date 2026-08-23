<template>
  <div class="mall-page">
    <!-- 顶部导航栏 -->
    <NavFloor />
    <!-- 搜索头 -->
    <HeaderFloor />
    <!-- 种类 -->
    <CategoryFloor />
    <!-- banner -->
    <BannerFloor />
    <!-- 每日推荐 -->
    <RecommendFloor />
    <!-- 排行榜 -->
    <RankFloor />
    <!-- 猜你喜欢 -->
    <LikeFloor />
    <!-- 家用电器 -->
    <ApplianceFloor />
    <!-- 品牌墙 -->
    <BrandFloor />
    <!-- 底层信息 -->
    <FooterFloor />
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
import ApplianceFloor from '@/components/mall/index/ApplianceFloor.vue'
import BrandFloor from '@/components/mall/index/BrandFloor.vue'
import FooterFloor from '@/components/common/FooterFloor.vue'

</script>

<style scoped lang="scss">
/*
  把页面化为11个层，层的高度为 auto
  相当于: grid-template-rows: auto, auto, ...., auto; (共11个)
*/
.mall-page {
  width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-rows: repeat(11, auto);
}
</style>
