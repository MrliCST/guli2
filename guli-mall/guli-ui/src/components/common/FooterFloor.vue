<template>
  <section class="footerFloor">
    <div class="footerFloor__inner">
      <div class="footerFloor__links">
        <div class="linkCol" v-for="col in columns" :key="col.title">
          <h4>{{ col.title }}</h4>
          <ul v-if="col.items">
            <li v-for="it in col.items" :key="it">{{ it }}</li>
          </ul>
          <div v-else class="qrcode">
            <img :src="col.qrcode" alt="二维码" />
          </div>
        </div>
      </div>
      <div class="footerFloor__copyright">
        <div class="helpLink">
          <a v-for="l in helpLinks" :key="l" href="#">{{ l }}</a>
        </div>
        <p>地址:北京市昌平区宏福科技园综合楼6层 &nbsp;&nbsp; 京ICP备19006430号</p>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

const columns = ref([
  { title: '购物指南', items: ['购物流程', '会员介绍', '生活旅行', '常见问题'] },
  { title: '配送方式', items: ['上门自提', '211限时达', '配送服务查询', '配送费标准'] },
  { title: '支付方式', items: ['货到付款', '在线支付', '分期付款', '邮局汇款'] },
  { title: '售后服务', items: ['售后政策', '价格保护', '退款说明', '返修退换货'] },
  { title: '特色服务', items: ['夺宝岛', 'DIY装机', '延保服务', '谷粒E卡'] },
  { title: '帮助中心', qrcode: '/img/wx_cz.jpg' }
])

const helpLinks = ref(['关于我们', '联系我们', '商家入驻', '营销中心', '友情链接', '销售联盟'])
</script>

<style scoped>
.footerFloor { background: Gainsboro; padding: 10px 0; }
.footerFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto;
  gap: 16px;
}
.footerFloor__links {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 12px;
}
.footerFloor__links .linkCol {
  background: rgba(255, 255, 255, 0.7);
  padding: 12px;
  border-radius: 4px;
  font-size: 12px;
}
.footerFloor__links .linkCol h4 { font-size: 14px; margin-bottom: 8px; color: #333; }
.footerFloor__links .linkCol ul li { line-height: 1.9; color: #777; }
.footerFloor__links .linkCol .qrcode img { width: 120px; height: 120px; object-fit: contain; }
.footerFloor__copyright {
  background: Silver;
  padding: 16px;
  text-align: center;
  border-radius: 4px;
  font-size: 12px;
  color: #555;
}
.footerFloor__copyright .helpLink a { margin: 0 8px; color: #555; }
.footerFloor__copyright p { margin-top: 8px; }
</style>
