<template>
  <div class="payPage">
    <NavFloor />
    <HeaderFloor />
    <PayFloor />
    <FooterFloor />
  </div>
</template>

<script setup>
import NavFloor from '../components/common/NavFloor.vue'
import HeaderFloor from '../components/common/HeaderFloor.vue'
import PayFloor from '../components/pay/PayFloor.vue'
import FooterFloor from '../components/common/FooterFloor.vue'
</script>

<style scoped>
.payPage { display: grid; grid-template-rows: auto auto 1fr auto; }
</style>
