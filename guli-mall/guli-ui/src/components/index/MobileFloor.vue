<template>
  <section class="mobileFloor">
    <BaseFloor
      title="手机通讯"
      title-bg="MediumOrchid"
      :tabs="tabs"
      sorts-img="/img/floor-1-1.png"
      banner-img="/img/floor-1-b02.png"
      :double-imgs1="['/img/floor-1-2.png', '/img/floor-1-3.png']"
      single-img="/img/floor-1-4.png"
      :double-imgs2="['/img/floor-1-5.png', '/img/floor-1-6.png']"
    />
  </section>
</template>

<script setup>
import { ref } from 'vue'
import BaseFloor from '../common/BaseFloor.vue'

const tabs = ref(['热门', '品质优选', '新机尝鲜', '高性价比', '合约机', '手机卡', '配件'])
</script>

<style scoped>
.mobileFloor { background: LavenderBlush; }
</style>
