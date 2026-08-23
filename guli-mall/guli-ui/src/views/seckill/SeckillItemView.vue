<template>
  <div class="seckillItem-page">
    <NavFloor />
    <HeaderFloor />
    <SeckillInfoFloor />
    <SeckillDetailFloor />
    <LikeFloor />
    <FooterFloor />
    <SideToolbar />
  </div>
</template>

<script setup lang="ts">
import NavFloor from '@/components/common/NavFloor.vue'
import HeaderFloor from '@/components/common/HeaderFloor.vue'
import SeckillInfoFloor from '@/components/seckill/SeckillInfoFloor.vue'
import SeckillDetailFloor from '@/components/seckill/SeckillDetailFloor.vue'
import LikeFloor from '@/components/common/LikeFloor.vue'
import FooterFloor from '@/components/common/FooterFloor.vue'
import SideToolbar from '@/components/common/SideToolbar.vue'
</script>

<style scoped lang="scss">
.seckillItem-page {
  display: grid;
  grid-template-rows: repeat(6, auto);
}
</style>
