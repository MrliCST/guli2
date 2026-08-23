<template>
  <aside class="orderSidebar">
    <dl v-for="group in menuGroups" :key="group.title">
      <dt><i>·</i> {{ group.title }}</dt>
      <dd
        v-for="item in group.items"
        :key="item"
        :class="{ active: item === activeItem }"
        @click="activeItem = item"
      >{{ item }}</dd>
    </dl>
  </aside>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const activeItem = ref('我的订单')

const menuGroups = [
  { title: '订单中心', items: ['我的订单', '团购订单', '本地生活订单', '我的预售', '评价晒单', '取消订单记录'] },
  { title: '关注中心', items: ['关注的商品', '关注的店铺', '关注的专辑', '关注的品牌', '关注的活动', '浏览历史'] },
  { title: '特色业务', items: ['我的营业厅', '京东通信', '定期送', '京东代下单', '我的回收单', '节能补贴', '医药服务', '流量加油站', '黄金托管'] },
  { title: '客户服务', items: ['返修退换货', '价格保护', '意见建议', '购买咨询', '交易纠纷', '我的发票'] },
  { title: '设置', items: ['个人信息', '收货地址'] }
]
</script>

<style scoped>
.orderSidebar {
  background: #fff;
  border-radius: 6px;
  padding: 12px;
}
dl { margin: 0 0 16px; }
dt {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  padding: 8px 0 6px;
  border-bottom: 1px solid #f0f0f0;
}
dt i { color: #c81623; font-style: normal; margin-right: 4px; }
dd {
  font-size: 13px;
  color: #666;
  padding: 6px 12px;
  cursor: pointer;
  border-radius: 4px;
  margin: 0;
}
dd:hover { background: #f5f5f5; color: #333; }
dd.active { color: #c81623; font-weight: bold; }
</style>
