<template>
  <section class="footerFloor">
    <div class="footerFloor_inner">
      <!-- 相关链接 -->
      <div class="footerFloor_links">
        <div class="linkCol" v-for="col in columns" :key="col.title">
          <!-- 标题 -->
          <h4>{{ col.title }}</h4>
          <!-- 无序列表 -->
          <ul>
            <li v-for="it in col.items" :key="it">{{ it }}</li>
          </ul>
        </div>
      </div>
      <!-- 版权声明 -->
      <div class="footerFloor_copyright">
        <div class="helpLink">
          <a v-for="l in helpLinks" :key="l" href="javascript:void(0)">{{ l }}</a>
        </div>
        <p>地址:北京市昌平区宏福科技园综合楼6层 &nbsp;&nbsp; 京ICP备19006430号</p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const columns = ref([
  { title: '购物指南', items: ['购物流程', '会员介绍', '生活旅行', '常见问题'] },
  { title: '配送方式', items: ['上门自提', '211限时达', '配送服务查询', '配送费标准'] },
  { title: '支付方式', items: ['货到付款', '在线支付', '分期付款', '邮局汇款'] },
  { title: '售后服务', items: ['售后政策', '价格保护', '退款说明', '返修退换货'] },
  { title: '特色服务', items: ['夺宝岛', 'DIY装机', '延保服务', '谷粒E卡'] }
])

const helpLinks = ref(['关于我们', '联系我们', '商家入驻', '营销中心', '友情链接', '销售联盟'])
</script>

<style scoped lang="scss">
.footerFloor {
  background: Gainsboro;

  .footerFloor_inner {
    display: grid;
    grid-template-rows: auto auto;
    gap: 16px;
  }

  /** 相关链接 */
  .footerFloor_links {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 12px;

    .linkCol {
      padding: 12px;
      border-radius: 4px;
      font-size: 12px;

      h4 {
        font-size: 14px;
        margin-bottom: 8px;
        color: #333;
        margin-left: 20px;
      }

      ul {
        list-style: none;
        padding: 0;
        margin: 0;
        margin-left: 20px;

        li {
          line-height: 1.9;
          color: #777;
        }
      }
    }
  }

  /* 版权声明 */
  .footerFloor_copyright {
    background: Silver;
    padding: 16px;
    text-align: center;
    font-size: 12px;
    color: #555;

    .helpLink {
      a {
        margin: 0 8px;
        color: #555;
        text-decoration: none;
      }
    }

    p {
      margin-top: 8px;
    }
  }
}
</style>
