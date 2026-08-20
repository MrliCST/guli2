<template>
  <aside class="floorIndex">
    <div class="floorIndex__item" v-for="f in floors" :key="f.id">
      {{ f.id }}F {{ f.name }}
    </div>
  </aside>
</template>

<script setup>
import { ref } from 'vue'

const floors = ref([
  { id: 1, name: '家用' },
  { id: 2, name: '手机' },
  { id: 3, name: '电脑' },
  { id: 4, name: '家居' },
  { id: 5, name: '服装' }
])
</script>

<style scoped>
.floorIndex {
  position: fixed;
  left: 20px;
  top: 50%;
  transform: translateY(-50%);
  background: PaleGreen;
  padding: 10px 8px;
  border-radius: 6px;
  display: grid;
  grid-template-rows: repeat(5, auto);
  gap: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
  z-index: 50;
}
.floorIndex__item {
  background: rgba(255, 255, 255, 0.8);
  padding: 10px 12px;
  border-radius: 4px;
  text-align: center;
  font-size: 12px;
  color: #444;
}
</style>
