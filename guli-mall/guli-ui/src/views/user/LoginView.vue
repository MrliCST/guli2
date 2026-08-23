<template>
  <div class="loginPage">
    <LoginFloor />
    <SimpleFooter />
  </div>
</template>

<script setup lang="ts">
import LoginFloor from '@/components/user/LoginFloor.vue'
import SimpleFooter from '@/components/common/SimpleFooter.vue'
</script>

<style scoped lang="scss">
.loginPage {
  height: 100%;
  display: grid;
  grid-template-rows: 1fr auto;
}
</style>
