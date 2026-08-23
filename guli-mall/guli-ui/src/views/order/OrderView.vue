<template>
  <div class="myOrderPage">
    <NavFloor />
    <HeaderFloor />
    <div class="myOrderPage_body">
      <div class="myOrderPage_inner">
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

<style scoped lang="scss">
.myOrderPage {
  display: grid;
  grid-template-rows: auto auto 1fr auto;
}
.myOrderPage_body { background: #f5f5f5; padding: 10px 0; }
.myOrderPage_inner {
  width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 12px;
}
</style>
