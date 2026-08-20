<template>
  <div class="cart-page">
    <NavFloor />
    <HeaderFloor />
    <CartTableFloor />
    <LikeFloor />
    <FooterFloor />
  </div>
</template>

<script setup>
import NavFloor from '../components/common/NavFloor.vue'
import HeaderFloor from '../components/common/HeaderFloor.vue'
import CartTableFloor from '../components/cart/CartTableFloor.vue'
import LikeFloor from '../components/common/LikeFloor.vue'
import FooterFloor from '../components/common/FooterFloor.vue'
</script>

<style scoped>
.cart-page {
  display: grid;
  grid-template-rows: repeat(5, auto);
}
</style>
