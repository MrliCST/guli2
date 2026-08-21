<template>
  <section class="homeApplianceFloor">
    <BaseFloor
      title="家用电器"
      title-bg="Tan"
      :tabs="tabs"
      sorts-img="/img/floor-1-1.png"
      banner-img="/img/floor-1-b01.png"
      :double-imgs1="['/img/floor-1-2.png', '/img/floor-1-3.png']"
      single-img="/img/floor-1-4.png"
      :double-imgs2="['/img/floor-1-5.png', '/img/floor-1-6.png']"
    />
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import BaseFloor from '@/components/common/BaseFloor.vue'

const tabs = ref(['热门', '大家电', '生活电器', '厨房电器', '应季电器', '空气/净水', '高端电器'])
</script>

<style scoped>
.homeApplianceFloor { background: LightGoldenrodYellow; }
</style>
