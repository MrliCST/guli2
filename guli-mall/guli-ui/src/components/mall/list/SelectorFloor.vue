<template>
  <section class="selectorFloor">
    <div class="selectorFloor__inner">
      <div
        class="selectorRow"
        v-for="row in filterRows"
        :key="row.key"
      >
        <div class="cell selectorRow__key">
          <span>{{ row.key }}</span>
        </div>
        <div class="selectorRow__value">
          <!-- 品牌行: logo 图片 + 文字 -->
          <template v-if="row.type === 'logo'">
            <ul class="logoList">
              <li v-for="(src, name) in row.brands" :key="name">
                <img :src="src" :alt="name" />
              </li>
              <li v-for="text in row.texts" :key="text">
                {{ text }}
              </li>
            </ul>
          </template>
          <!-- 普通行: 文字选项 -->
          <template v-else>
            <ul class="optionList">
              <li v-for="opt in row.options" :key="opt"><a href="#">{{ opt }}</a></li>
            </ul>
          </template>
        </div>
        <!-- <div class="selectorRow__ext" v-if="row.ext">
          <a href="#" v-if="row.ext.multi" class="extBtn">多选</a>
          <a href="#" v-if="row.ext.more" class="extLink">更多</a>
        </div> -->
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
    // 品牌 logo 表：key 为品牌名，value 为 public 下的路径（16 个手机品牌）
    brands: {
      huawei: '/mall/brand/brand-huawei.png',
      xiaomi: '/mall/brand/brand-xiaomi.png',
      oppo: '/mall/brand/brand-oppo.png',
      vivo: '/mall/brand/brand-vivo.png',
      samsung: '/mall/brand/brand-samsung.png',
      sony: '/mall/brand/brand-sony.png',
      meizu: '/mall/brand/brand-meizu.png',
      tcl: '/mall/brand/brand-tcl.png',
      hisense: '/mall/brand/brand-hisense.png',
      leshi: '/mall/brand/brand-leshi.png',
      philips: '/mall/brand/brand-philips.png',

    },
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

<style scoped lang="scss">
.selectorFloor {
  padding: 10px 0;
}

.selectorFloor__inner {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-rows: repeat(6, auto);
  gap: 1px;
  background: #ddd;
  border-radius: 6px;
  overflow: hidden;
}

.selectorRow {
  display: grid;
  grid-template-columns: 120px 1fr 100px;
  gap: 0;
  background: #fff;
  align-items: stretch;

  /* 行标题:参照 list.html 的 .type-wrap .key,灰底、右对齐 */
  &__key {
    background: #f1f1f1;
    font-size: 16px;
    color: #333;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  &__value {
    padding: 10px 0 0 15px;
    display: flex;
    align-items: flex-start;
    flex-wrap: wrap;
  }
  // 行扩展:参照 list.html 的 .type-wrap .ext,右对齐、内边距、flex 居中
  &__ext {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 10px;

    .extBtn {
      background: #fff;
      border: 1px solid #e1e1e1;
      padding: 0 10px;
      border-radius: 2px;
      line-height: 18px;
      font-size: 14px;
      color: #333;
    }

    .extLink:hover {
      color: red;
      font-weight: bold;
    }
  }
}

/* 品牌格:参照 .logo-list li,105x52 圆角灰边框小格、红色斜体字
   图片用 object-fit: contain + max-width/max-height 上限,
   完整显示不拉伸;容器 flex 居中 + 内边距,logo 不贴边不裁切 */
.logoList {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  list-style: none;

  li {
    width: 105px;
    height: 52px;
    display: flex;
    align-items: center;
    justify-content: center;
    // 内边距 + 边框 + 圆角 + 白底
    padding: 6px;
    box-sizing: border-box;
    border: 1px solid #e4e4e4;
    border-radius: 4px;
    background: #fff;

    font-size: 14px;
    font-weight: 700;
    font-style: italic;
    color: #e1251b;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    // 鼠标悬停:红色边框 + 阴影 + 上移 1px
    cursor: pointer;
    transition: border-color 0.2s ease, box-shadow 0.2s ease, transform 0.2s ease;

    &:hover {
      border-color: red;
      box-shadow: 0 2px 8px rgba(255, 0, 0, 0.18);
      transform: translateY(-1px);
    }

    img {
      display: block;
      max-width: 100%;
      max-height: 100%;
      object-fit: contain;
    }
  }
}

/* 普通筛选项:悬停字体变红,保留白底不反白 */
.optionList {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  list-style: none;

  li a {
    font-size: 14px;
    color: #555;
    line-height: 26px;
    padding: 2px;
    text-decoration: none;
    transition: color 0.2s ease;

    &:hover {
      color: red;
      font-weight: bold;
    }
  }
}
</style>
