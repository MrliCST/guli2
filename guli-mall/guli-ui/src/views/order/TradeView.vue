<template>
  <div class="tradePage">
    <NavFloor />
    <HeaderFloor />
    <div class="tradePage_body">
      <div class="tradePage_inner">
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

<style scoped lang="scss">
.tradePage {
  display: grid;
  grid-template-rows: auto auto 1fr auto;
}
.tradePage_body { background: #f5f5f5; padding: 10px 0; }
.tradePage_inner {
  width: 1200px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
}
</style>
