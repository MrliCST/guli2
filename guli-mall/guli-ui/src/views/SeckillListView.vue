<template>
  <div class="seckillList-page">
    <NavFloor />
    <HeaderFloor />
    <SeckillBannerFloor />
    <FooterFloor />
  </div>
</template>

<script setup>
import NavFloor from '../components/common/NavFloor.vue'
import HeaderFloor from '../components/common/HeaderFloor.vue'
import SeckillBannerFloor from '../components/seckill/SeckillBannerFloor.vue'
import FooterFloor from '../components/common/FooterFloor.vue'
</script>

<style scoped>
.seckillList-page {
  display: grid;
  grid-template-rows: repeat(4, auto);
}
</style>
