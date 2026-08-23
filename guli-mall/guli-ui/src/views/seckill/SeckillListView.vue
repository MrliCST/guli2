<template>
  <div class="seckillList-page">
    <NavFloor />
    <HeaderFloor />
    <SeckillBannerFloor />
    <FooterFloor />
  </div>
</template>

<script setup lang="ts">
import NavFloor from '@/components/common/NavFloor.vue'
import HeaderFloor from '@/components/common/HeaderFloor.vue'
import SeckillBannerFloor from '@/components/seckill/SeckillBannerFloor.vue'
import FooterFloor from '@/components/common/FooterFloor.vue'
</script>

<style scoped lang="scss">
.seckillList-page {
  display: grid;
  grid-template-rows: repeat(4, auto);
}
</style>
