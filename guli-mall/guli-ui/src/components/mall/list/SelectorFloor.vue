<template>
  <section class="selectorFloor">
    <div class="selectorFloor__inner">
      <div
        class="selectorRow"
        v-for="row in filterRows"
        :key="row.key"
      >
        <div class="cell selectorRow__key">{{ row.key }}</div>
        <div class="selectorRow__value">
          <!-- 品牌行: logo 图片 + 文字 -->
          <template v-if="row.type === 'logo'">
            <ul class="logoList">
              <li v-for="(logo, i) in row.logos" :key="'logo' + i">
                <img :src="logo" alt="" />
              </li>
              <li v-for="text in row.texts" :key="text">{{ text }}</li>
            </ul>
          </template>
          <!-- 普通行: 文字选项 -->
          <template v-else>
            <ul class="optionList">
              <li v-for="opt in row.options" :key="opt"><a href="#">{{ opt }}</a></li>
            </ul>
          </template>
        </div>
        <div class="selectorRow__ext" v-if="row.ext">
          <a href="#" v-if="row.ext.multi" class="extBtn">多选</a>
          <a href="#" v-if="row.ext.more" class="extLink">更多</a>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const filterRows = ref([
  {
    key: '品牌',
    type: 'logo',
    logos: [
      '/img/_/phone06.png', '/img/_/phone07.png', '/img/_/phone08.png',
      '/img/_/phone09.png', '/img/_/phone10.png', '/img/_/phone11.png',
      '/img/_/phone12.png', '/img/_/phone14.png', '/img/_/phone01.png'
    ],
    texts: ['索尼（SONY）', 'TCL', '长虹（CHANGHONG）', '飞利浦（PHILIPS）', '风行电视'],
    ext: { multi: true, more: true }
  },
  {
    key: '网络制式',
    type: 'text',
    options: ['GSM（移动/联通2G）', '电信2G', '电信3G', '移动3G', '联通3G', '联通4G'],
    ext: {}
  },
  {
    key: '显示屏尺寸',
    type: 'text',
    options: ['4.0-4.9英寸', '5.0-5.9英寸', '6.0英寸以上'],
    ext: {}
  },
  {
    key: '摄像头像素',
    type: 'text',
    options: ['1200万以上', '800-1199万', '1200-1599万', '1600万以上', '无摄像头'],
    ext: {}
  },
  {
    key: '价格',
    type: 'text',
    options: ['0-500元', '500-1000元', '1000-1500元', '1500-2000元', '2000-3000元', '3000元以上'],
    ext: {}
  },
  {
    key: '更多筛选项',
    type: 'text',
    options: ['特点', '系统', '手机内存', '单卡双卡', '其他'],
    ext: {}
  }
])
</script>

<style scoped>
.selectorFloor { background: MistyRose; padding: 10px 0; }
.selectorFloor__inner {
  width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-rows: repeat(6, auto);
  gap: 1px;
  background: #eee;
  border-radius: 6px;
  overflow: hidden;
}
.selectorRow {
  display: grid;
  grid-template-columns: 120px 1fr 100px;
  gap: 0;
  background: #fff;
  align-items: stretch;
}
.selectorRow__key {
  background: PaleTurquoise;
  padding: 10px 14px;
  font-size: 13px;
  color: #333;
  font-weight: bold;
  display: flex;
  align-items: center;
}
.selectorRow__value {
  padding: 10px 14px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}
.logoList {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  list-style: none;
}
.logoList li {
  width: 100px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #eee;
  border-radius: 4px;
  font-size: 12px;
  color: #555;
}
.logoList li img { max-width: 80px; max-height: 30px; object-fit: contain; }
.optionList {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  list-style: none;
}
.optionList li a {
  font-size: 12px;
  color: #555;
}
.optionList li a:hover { color: #c81623; }
.selectorRow__ext {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px;
}
.selectorRow__ext .extBtn {
  background: #fff;
  border: 1px solid #ddd;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  color: #666;
}
.selectorRow__ext .extLink {
  font-size: 12px;
  color: #c81623;
}
</style>
