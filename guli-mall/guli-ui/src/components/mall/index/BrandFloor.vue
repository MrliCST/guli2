<template>
  <section class="brandFloor">
    <div class="brandFloor_inner">
      <div class="brandFloor_item" v-for="(b, key) in brands" :key="key">
        <img :src="b" alt="" />
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 品牌图片路径表：key 为品牌名，value 为 public 下的路径
const brands = ref({
  anta: '/mall/brand/brand-anta.png',
  anward: '/mall/brand/brand-anward.png',
  blueAir: '/mall/brand/brand-blueAir.png',
  changhong: '/mall/brand/brand-changhong.png',
  flyco: '/mall/brand/brand-flyco.png',
  gree: '/mall/brand/brand-gree.png',
  hisense: '/mall/brand/brand-hisense.png',
  huawei: '/mall/brand/brand-huawei.png',
  leshi: '/mall/brand/brand-leshi.png',
  meizu: '/mall/brand/brand-meizu.png',
  midea: '/mall/brand/brand-midea.png',
  oppo: '/mall/brand/brand-oppo.png',
  philips: '/mall/brand/brand-philips.png',
  samsung: '/mall/brand/brand-samsung.png',
  siemens: '/mall/brand/brand-siemens.png',
  sony: '/mall/brand/brand-sony.png',
  supor: '/mall/brand/brand-supor.png',
  tcl: '/mall/brand/brand-tcl.png',
  vivo: '/mall/brand/brand-vivo.png',
  xiaomi: '/mall/brand/brand-xiaomi.png'
})
</script>

<style scoped lang="scss">
.brandFloor {
  margin: 10px 0;
  padding: 10px 0;

  .brandFloor_inner {
    display: grid;
    gap: 10px;
    grid-template-columns: repeat(10, 1fr);
    grid-template-rows: repeat(2, auto);
  }

  .brandFloor_item {
    display: grid;
    height: 50px;
    padding: 10px 0;
    place-items: center;
    border: 1px solid #dad9d9;


    img {
      max-width: 90%;
      max-height: 70px;
      object-fit: contain;
    }
  }
}
</style>
