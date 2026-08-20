<template>
  <section class="breadcrumbFloor">
    <div class="breadcrumbFloor__inner">
      <div class="cell breadcrumbFloor__path">
        <a href="#">全部结果</a>
      </div>
      <div class="breadcrumbFloor__tags">
        <span class="tag" v-for="tag in tags" :key="tag.text">
          {{ tag.text }}
          <i v-if="tag.closable" @click="removeTag(tag)">×</i>
        </span>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

const tags = ref([
  { text: '手机', closable: false },
  { text: 'iphone', closable: true },
  { text: '华为', closable: true },
  { text: 'OPPO', closable: true }
])

function removeTag(tag) {
  tags.value = tags.value.filter(t => t.text !== tag.text)
}
</script>

<style scoped>
.breadcrumbFloor { background: Lavender; padding: 10px 0; }
.breadcrumbFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 12px;
  align-items: center;
  min-height: 40px;
}
.breadcrumbFloor__path {
  background: rgba(255, 255, 255, 0.7);
  padding: 8px 14px;
  border-radius: 4px;
  font-size: 13px;
}
.breadcrumbFloor__path a { color: #666; }
.breadcrumbFloor__tags {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, auto));
  gap: 8px;
}
.breadcrumbFloor__tags .tag {
  background: #fff;
  border: 1px solid #ddd;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 12px;
  color: #333;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}
.breadcrumbFloor__tags .tag i {
  font-style: normal;
  color: #999;
  cursor: pointer;
  font-size: 14px;
}
.breadcrumbFloor__tags .tag i:hover { color: var(--theme-red); }
</style>
