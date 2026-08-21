<template>
  <div class="addCart-page">
    <NavFloor />
    <HeaderFloor />
    <AddCartSuccessFloor />
    <FooterFloor />
  </div>
</template>

<script setup lang="ts">
import NavFloor from '@/components/common/NavFloor.vue'
import HeaderFloor from '@/components/common/HeaderFloor.vue'
import AddCartSuccessFloor from '@/components/cart/AddCartSuccessFloor.vue'
import FooterFloor from '@/components/common/FooterFloor.vue'
</script>

<style scoped>
.addCart-page {
  display: grid;
  grid-template-rows: repeat(4, auto);
}
</style>
