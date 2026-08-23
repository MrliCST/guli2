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
  anta: '/mall/index/brand-anta.png',
  anward: '/mall/index/brand-anward.png',
  blueAir: '/mall/index/brand-blueAir.png',
  changhong: '/mall/index/brand-changhong.png',
  flyco: '/mall/index/brand-flyco.png',
  gree: '/mall/index/brand-gree.png',
  hisense: '/mall/index/brand-hisense.png',
  huawei: '/mall/index/brand-huawei.png',
  leshi: '/mall/index/brand-leshi.png',
  meizu: '/mall/index/brand-meizu.png',
  midea: '/mall/index/brand-midea.png',
  oppo: '/mall/index/brand-oppo.png',
  philips: '/mall/index/brand-philips.png',
  samsung: '/mall/index/brand-samsung.png',
  siemens: '/mall/index/brand-siemens.png',
  sony: '/mall/index/brand-sony.png',
  supor: '/mall/index/brand-supor.png',
  tcl: '/mall/index/brand-tcl.png',
  vivo: '/mall/index/brand-vivo.png',
  xiaomi: '/mall/index/brand-xiaomi.png'
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
