<template>
  <div class="tradePage">
    <NavFloor />
    <HeaderFloor />
    <div class="tradePage__body">
      <div class="tradePage__inner">
        <TradeFloor />
      </div>
    </div>
    <FooterFloor />
  </div>
</template>

<script setup lang="ts">
import NavFloor from '@/components/common/NavFloor.vue'
import HeaderFloor from '@/components/common/HeaderFloor.vue'
import TradeFloor from '@/components/order/TradeFloor.vue'
import FooterFloor from '@/components/common/FooterFloor.vue'
</script>

<style scoped>
.tradePage {
  display: grid;
  grid-template-rows: auto auto 1fr auto;
}
.tradePage__body { background: #f5f5f5; padding: 10px 0; }
.tradePage__inner {
  width: var(--page-width);
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
}
</style>
