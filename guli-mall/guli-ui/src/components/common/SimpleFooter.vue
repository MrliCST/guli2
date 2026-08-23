<template>
  <footer class="simpleFooterFloor">
    <div class="simpleFooterFloor_inner">
      <ul class="links">
        <li v-for="link in links" :key="link">{{ link }}</li>
      </ul>
      <p class="address">地址：北京市昌平区宏福科技园综合楼6层</p>
      <p class="beian">京ICP备19006430号</p>
    </div>
  </footer>
</template>

<script setup lang="ts">
const links = ['关于我们', '联系我们', '联系客服', '商家入驻', '营销中心', '手机谷粒', '销售联盟', '谷粒社区']
</script>

<style scoped lang="scss">
.simpleFooterFloor { background: #f5f5f5; padding: 20px 0; }
.simpleFooterFloor_inner {
  width: 1200px;
  margin: 0 auto;
  text-align: center;
}
.links {
  display: flex;
  justify-content: center;
  gap: 20px;
  list-style: none;
  padding: 0;
  margin: 0 0 10px;
}
.links li { font-size: 12px; color: #999; cursor: pointer; }
.links li:hover { color: #c81623; }
.address { font-size: 12px; color: #999; margin: 4px 0; }
.beian { font-size: 12px; color: #999; margin: 4px 0; }
</style>
