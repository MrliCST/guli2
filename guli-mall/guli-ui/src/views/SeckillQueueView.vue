<template>
  <div class="seckillQueue-page">
    <NavFloor />
    <HeaderFloor />
    <SeckillQueueFloor />
    <FooterFloor />
  </div>
</template>

<script setup>
import NavFloor from '../components/common/NavFloor.vue'
import HeaderFloor from '../components/common/HeaderFloor.vue'
import SeckillQueueFloor from '../components/seckill/SeckillQueueFloor.vue'
import FooterFloor from '../components/common/FooterFloor.vue'
</script>

<style scoped>
.seckillQueue-page {
  display: grid;
  grid-template-rows: repeat(4, auto);
}
</style>
