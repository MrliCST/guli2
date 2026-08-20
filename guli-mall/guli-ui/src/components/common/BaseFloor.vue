<template>
  <div class="floorBlock">
    <div class="floorBlock__inner">
      <div class="floorBlock__title" :style="{ background: titleBg }">
        <div class="titleText">{{ title }}</div>
        <div class="titleTabs">
          <div
            class="tab"
            :class="{ active: idx === activeIdx }"
            v-for="(tab, idx) in tabs"
            :key="tab"
            @click="activeIdx = idx"
          >{{ tab }}</div>
        </div>
      </div>
      <div class="floorContent">
        <div class="floorContent__sorts">
          <img :src="sortsImg" alt="" />
        </div>
        <div class="floorContent__banner">
          <img :src="bannerImg" alt="" />
        </div>
        <div class="floorContent__doubleImg">
          <div class="img" v-for="(img, i) in doubleImgs1" :key="'a' + i">
            <img :src="img" alt="" />
          </div>
        </div>
        <div class="floorContent__singleImg">
          <img :src="singleImg" alt="" />
        </div>
        <div class="floorContent__doubleImg">
          <div class="img" v-for="(img, i) in doubleImgs2" :key="'b' + i">
            <img :src="img" alt="" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

defineProps({
  title: { type: String, required: true },
  titleBg: { type: String, default: '#c81623' },
  tabs: { type: Array, default: () => [] },
  sortsImg: { type: String, required: true },
  bannerImg: { type: String, required: true },
  doubleImgs1: { type: Array, default: () => [] },
  singleImg: { type: String, required: true },
  doubleImgs2: { type: Array, default: () => [] }
})

const activeIdx = ref(0)
</script>

<style scoped>
.floorBlock { padding: 10px 0; }
.floorBlock__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto;
  gap: 10px;
}
.floorBlock__title {
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 12px;
  align-items: center;
  border-radius: 6px;
  overflow: hidden;
}
.floorBlock__title .titleText {
  color: #fff;
  padding: 12px;
  font-size: 18px;
  font-weight: bold;
}
.floorBlock__title .titleTabs {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 6px;
  padding-right: 12px;
}
.floorBlock__title .titleTabs .tab {
  background: rgba(255, 255, 255, 0.7);
  text-align: center;
  padding: 8px 0;
  border-radius: 4px;
  font-size: 13px;
  color: #444;
  cursor: pointer;
}
.floorBlock__title .titleTabs .tab.active {
  background: var(--theme-red);
  color: #fff;
}
.floorContent {
  display: grid;
  grid-template-columns: 210px 330px 220px 218px 220px;
  gap: 0;
}
.floorContent__sorts,
.floorContent__banner,
.floorContent__singleImg {
  height: 400px;
  overflow: hidden;
}
.floorContent__sorts img,
.floorContent__banner img,
.floorContent__singleImg img {
  width: 100%;
  height: 400px;
  object-fit: cover;
}
.floorContent__doubleImg {
  display: grid;
  grid-template-rows: 1fr 1fr;
  gap: 1px;
  height: 400px;
}
.floorContent__doubleImg .img { overflow: hidden; }
.floorContent__doubleImg .img img { width: 100%; height: 100%; object-fit: cover; }
</style>
