<template>
  <div class="registerPage">
    <RegisterFloor />
    <SimpleFooter />
  </div>
</template>

<script setup>
import RegisterFloor from '../components/user/RegisterFloor.vue'
import SimpleFooter from '../components/common/SimpleFooter.vue'
</script>

<style scoped>
.registerPage {
  display: grid;
  grid-template-rows: 1fr auto;
  min-height: 100vh;
}
</style>
