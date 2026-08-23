<template>
  <section class="applianceFloor">
    <div class="applianceFloor_inner">
      <!-- 导航标签 -->
      <div class="applianceFloor_title">
        <div class="titleText">{{ title }}</div>
        <div class="titleTabs" @click="onTabClick">
          <div class="tab" :class="{ active: isActive(idx) }" v-for="(tab, idx) in tabs" :key="tab" :data-index="idx"
          >{{ tab }}</div>
        </div>
      </div>
      <!-- 图片集 -->
      <div class="applianceFloor_content">
        <img class="contentImg" :src="images.appliances1" alt="" style="grid-area: a1" />
        <img class="contentImg" :src="images.appliances2" alt="" style="grid-area: a2" />
        <img class="contentImg" :src="images.appliances3Top" alt="" style="grid-area: a3top" />
        <img class="contentImg" :src="images.appliances3Down" alt="" style="grid-area: a3down" />
        <img class="contentImg" :src="images.appliances4" alt="" style="grid-area: a4" />
        <img class="contentImg" :src="images.appliances5Top" alt="" style="grid-area: a5top" />
        <img class="contentImg" :src="images.appliances5Down" alt="" style="grid-area: a5down" />
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const title = '家用电器'
const tabs = ref(['热门', '大家电', '生活电器', '厨房电器', '应季电器', '空气/净水', '高端电器'])

// 家用电器图片路径表：key 与图片文件名对应，value 为 public 下的路径
const images = {
  appliances1: '/mall/index/appliances-1.png',
  appliances2: '/mall/index/appliances-2-1.png',
  appliances3Top: '/mall/index/appliances-3-top.png',
  appliances3Down: '/mall/index/appliances-3-down.png',
  appliances4: '/mall/index/appliances-4.png',
  appliances5Top: '/mall/index/appliances-5-top.png',
  appliances5Down: '/mall/index/appliances-5-down.png'
}

const activeIdx = ref(0)

const isActive = (idx: number) => activeIdx.value === idx

const onTabClick = (e: MouseEvent) => {
  const el = (e.target as HTMLElement).closest('.tab') as HTMLElement | null
  if (el) {
    activeIdx.value = Number(el.dataset.index)
  }
}
</script>

<style scoped lang="scss">
.applianceFloor {
  margin-top: 10px;

  .applianceFloor_inner {
    display: grid;
    grid-template-rows: auto auto;
  }

  .applianceFloor_title {
    display: grid;
    grid-template-columns: 200px 500px;
    gap: 12px;
    align-items: center;
    justify-content: space-between;
    border-bottom: 4px solid red;

    .titleText {
      color: red;
      padding: 12px 0;
      font-size: 20px;
      font-weight: bold;
    }

    .titleTabs {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      gap: 6px;

      .tab {
        text-align: center;
        font-size: 13px;
        border-right: 1px solid #ccc;
        color: #444;
        cursor: pointer;

        &:last-child {
          border-right: none;
        }

        &.active {
          color: red;
        }
      }
    }
  }

  .applianceFloor_content {
    display: grid;
    grid-template-columns: 210px 1fr 220px 218px 220px;
    grid-template-rows: 200px 200px;
    /** 二维布局，同名合并 */
    grid-template-areas:
      "a1 a2 a3top a4 a5top"
      "a1 a2 a3down a4 a5down";

    .contentImg {
      width: 100%;
      height: 100%;
      object-fit: cover;
      border: 1px solid #ccc;
    }
  }
}
</style>
