<template>
  <div class="wxPayPage">
    <NavFloor />
    <HeaderFloor />
    <WxPayFloor />
    <FooterFloor />
  </div>
</template>

<script setup>
import NavFloor from '../components/common/NavFloor.vue'
import HeaderFloor from '../components/common/HeaderFloor.vue'
import WxPayFloor from '../components/pay/WxPayFloor.vue'
import FooterFloor from '../components/common/FooterFloor.vue'
</script>

<style scoped>
.wxPayPage { display: grid; grid-template-rows: auto auto 1fr auto; }
</style>
