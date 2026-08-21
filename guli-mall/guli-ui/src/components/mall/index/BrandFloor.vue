<template>
  <section class="brandFloor">
    <div class="brandFloor__inner">
      <div class="brandFloor__item" v-for="(b, idx) in brands" :key="idx">
        <img :src="b" alt="" />
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const brands = ref([
  '/img/brand_21.png', '/img/brand_03.png', '/img/brand_05.png', '/img/brand_07.png', '/img/brand_09.png',
  '/img/brand_11.png', '/img/brand_13.png', '/img/brand_15.png', '/img/brand_17.png', '/img/brand_19.png'
])
</script>

<style scoped>
.brandFloor { background: Azure; padding: 10px 0; }
.brandFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 10px;
}
.brandFloor__item {
  background: #fff;
  height: 90px;
  display: grid;
  place-items: center;
  border-radius: 4px;
  overflow: hidden;
  border: 1px solid #eee;
}
.brandFloor__item img { max-width: 90%; max-height: 70px; object-fit: contain; }
</style>
