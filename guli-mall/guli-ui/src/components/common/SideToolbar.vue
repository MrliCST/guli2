<template>
  <aside class="sideToolbar">
    <div class="sideToolbar__item" v-for="t in tools" :key="t">{{ t }}</div>
  </aside>
</template>

<script setup>
import { ref } from 'vue'

const tools = ref(['购物车', '关注', '足迹', '顶部'])
</script>

<style scoped>
.sideToolbar {
  position: fixed;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  background: PeachPuff;
  padding: 10px 8px;
  border-radius: 6px;
  display: grid;
  grid-template-rows: repeat(4, auto);
  gap: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
  z-index: 50;
}
.sideToolbar__item {
  background: rgba(255, 255, 255, 0.8);
  padding: 12px;
  border-radius: 4px;
  text-align: center;
  font-size: 12px;
  color: #444;
}
</style>
