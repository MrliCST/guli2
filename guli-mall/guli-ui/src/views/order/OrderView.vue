<template>
  <div class="myOrderPage">
    <NavFloor />
    <HeaderFloor />
    <div class="myOrderPage__body">
      <div class="myOrderPage__inner">
        <OrderSidebar />
        <MyOrderFloor />
      </div>
    </div>
    <FooterFloor />
  </div>
</template>

<script setup lang="ts">
import NavFloor from '@/components/common/NavFloor.vue'
import HeaderFloor from '@/components/common/HeaderFloor.vue'
import OrderSidebar from '@/components/order/OrderSidebar.vue'
import MyOrderFloor from '@/components/order/MyOrderFloor.vue'
import FooterFloor from '@/components/common/FooterFloor.vue'
</script>

<style scoped>
.myOrderPage {
  display: grid;
  grid-template-rows: auto auto 1fr auto;
}
.myOrderPage__body { background: #f5f5f5; padding: 10px 0; }
.myOrderPage__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 12px;
}
</style>
