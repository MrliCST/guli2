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

<script setup lang="ts">
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
.breadcrumbFloor { padding:0 0 10px 0; }
.breadcrumbFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 12px;
  align-items: center;
  min-height: 40px;
}
/* 面包屑:参照 list.html 的 .bread > .sui-breadcrumb,纯文本、无卡片背景 */
.breadcrumbFloor__path {
  padding: 3px 15px;
  font-size: 13px;
}
.breadcrumbFloor__path a { color: #666; }
.breadcrumbFloor__path a:hover { color: var(--theme-red); }
.breadcrumbFloor__tags {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, auto));
  gap: 8px;
}
/* 筛选标签:参照 list.html 的 .sui-tag,灰色小标签 */
.breadcrumbFloor__tags .tag {
  height: 20px;
  padding: 0 7px;
  background: #f7f7f7;
  border: 1px solid #dedede;
  font-size: 12px;
  color: #000;
  white-space: nowrap;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.2s ease-out;
}
.breadcrumbFloor__tags .tag:hover {
  color: #28a3ef;
}
.breadcrumbFloor__tags .tag i {
  font-style: normal;
  margin-left: 10px;
  color: #999;
  cursor: pointer;
  font-size: 14px;
}
</style>
