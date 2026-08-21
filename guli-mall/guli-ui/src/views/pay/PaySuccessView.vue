<template>
  <div class="paySuccessPage">
    <NavFloor />
    <HeaderFloor />
    <PaySuccessFloor />
    <FooterFloor />
  </div>
</template>

<script setup lang="ts">
import NavFloor from '@/components/common/NavFloor.vue'
import HeaderFloor from '@/components/common/HeaderFloor.vue'
import PaySuccessFloor from '@/components/pay/PaySuccessFloor.vue'
import FooterFloor from '@/components/common/FooterFloor.vue'
</script>

<style scoped>
.paySuccessPage { display: grid; grid-template-rows: auto auto 1fr auto; }
</style>
