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

<style scoped>
.loginPage {
  display: grid;
  grid-template-rows: 1fr auto;
  min-height: 100vh;
}
</style>
