<template>
  <section class="productInfoFloor">
    <div class="productInfoFloor__inner">
      <!-- 面包屑 -->
      <div class="productInfoFloor__crumb">
        <a href="#">Apple苹果</a>
        <span>></span>
        <span class="active">iphone 6S系类</span>
      </div>

      <!-- 主体:左放大镜 + 右商品信息 -->
      <div class="productInfoFloor__main">
        <!-- 左:图片预览 -->
        <div class="previewWrap">
          <div class="previewWrap__main">
            <img :src="mainImg" alt="" />
          </div>
          <div class="previewWrap__thumbs">
            <button class="thumbBtn prev" @click="scrollThumbs(-3)">&lt;</button>
            <div class="thumbList">
              <div
                class="thumbItem"
                :class="{ active: idx === activeThumb }"
                v-for="(thumb, idx) in thumbs"
                :key="idx"
                @click="selectThumb(idx)"
              >
                <img :src="thumb.small" alt="" />
              </div>
            </div>
            <button class="thumbBtn next" @click="scrollThumbs(3)">&gt;</button>
          </div>
        </div>

        <!-- 右:商品信息 -->
        <div class="itemInfo">
          <h4 class="itemInfo__name">{{ goods.name }}</h4>
          <div class="itemInfo__news">
            <span>推荐选择下方[移动优惠购],手机套餐齐搞定,不用换号,每月还有花费返</span>
          </div>

          <!-- 价格区 -->
          <div class="itemInfo__summary">
            <div class="summaryRow">
              <div class="summaryRow__title">价　　格</div>
              <div class="summaryRow__price">
                <i>¥</i><em>{{ goods.price }}</em>
                <span class="notice">降价通知</span>
              </div>
              <div class="summaryRow__remark">
                <i>累计评价</i><em>{{ goods.comment }}</em>
              </div>
            </div>
            <div class="summaryRow">
              <div class="summaryRow__title">促　　销</div>
              <div class="summaryRow__promo">
                <i class="redTag">加价购</i>
                <em>满999.00另加20.00元，或满1999.00另加30.00元，即可换购热销商品</em>
              </div>
            </div>
          </div>

          <!-- 支持区 -->
          <div class="itemInfo__support">
            <div class="summaryRow">
              <div class="summaryRow__title">支　　持</div>
              <div class="summaryRow__value">以旧换新，闲置手机回收 4G套餐超值抢 礼品购</div>
            </div>
            <div class="summaryRow">
              <div class="summaryRow__title">配 送 至</div>
              <div class="summaryRow__value">北京市 昌平区</div>
            </div>
          </div>

          <!-- 规格选择 -->
          <div class="itemInfo__choose">
            <div class="chooseRow" v-for="spec in specs" :key="spec.label">
              <div class="chooseRow__label">{{ spec.label }}</div>
              <div class="chooseRow__options">
                <a
                  href="#"
                  v-for="(opt, i) in spec.options"
                  :key="i"
                  :class="{ selected: i === spec.activeIdx }"
                  @click.prevent="spec.activeIdx = i"
                >{{ opt }}</a>
              </div>
            </div>
          </div>

          <!-- 数量 + 加入购物车 -->
          <div class="itemInfo__action">
            <div class="qtyBox">
              <button class="qtyBtn" @click="qty > 1 && qty--">-</button>
              <input class="qtyInput" v-model="qty" type="text" />
              <button class="qtyBtn" @click="qty++">+</button>
            </div>
            <button class="cartBtn">加入购物车</button>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

const goods = reactive({
  name: 'Apple iPhone 6s (A1699) 64G 玫瑰金色 移动联通电信4G手机',
  price: '5299.00',
  comment: '123456'
})

const mainImg = ref('/img/_/b1.png')
const activeThumb = ref(0)
const thumbs = ref([
  { small: '/img/_/s1.png', big: '/img/_/b1.png' },
  { small: '/img/_/s2.png', big: '/img/_/b2.png' },
  { small: '/img/_/s3.png', big: '/img/_/b3.png' },
  { small: '/img/_/s1.png', big: '/img/_/b1.png' },
  { small: '/img/_/s2.png', big: '/img/_/b2.png' },
  { small: '/img/_/s3.png', big: '/img/_/b3.png' },
  { small: '/img/_/s1.png', big: '/img/_/b1.png' },
  { small: '/img/_/s2.png', big: '/img/_/b2.png' },
  { small: '/img/_/s3.png', big: '/img/_/b3.png' }
])

function selectThumb(idx) {
  activeThumb.value = idx
  mainImg.value = thumbs.value[idx].big
}

function scrollThumbs() {
  // 静态:缩略图滚动预留
}

const qty = ref(1)

const specs = reactive([
  { label: '选择颜色', options: ['金色', '银色', '深空灰', '玫瑰金'], activeIdx: 0 },
  { label: '内存容量', options: ['16G', '64G', '128G'], activeIdx: 1 },
  { label: '选择版本', options: ['公开版', '移动版'], activeIdx: 0 },
  { label: '购买方式', options: ['官方标配', '移动优惠版', '电信优惠版'], activeIdx: 0 }
])
</script>

<style scoped>
.productInfoFloor { background: LavenderBlush; padding: 10px 0; }
.productInfoFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto auto;
  gap: 10px;
}

/* 面包屑 */
.productInfoFloor__crumb {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #999;
  padding: 6px 0;
}
.productInfoFloor__crumb a { color: #666; }
.productInfoFloor__crumb .active { color: #333; }

/* 主体:左右分栏 */
.productInfoFloor__main {
  display: grid;
  grid-template-columns: 400px 1fr;
  gap: 20px;
}

/* 左:图片预览 */
.previewWrap {
  display: grid;
  grid-template-rows: 400px auto;
  gap: 10px;
}
.previewWrap__main {
  background: #fff;
  border-radius: 6px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}
.previewWrap__main img { width: 100%; height: 100%; object-fit: contain; }
.previewWrap__thumbs {
  display: grid;
  grid-template-columns: 24px 1fr 24px;
  gap: 8px;
  align-items: center;
}
.thumbBtn {
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  height: 60px;
  font-size: 14px;
  color: #999;
}
.thumbBtn:hover { border-color: var(--theme-red); color: var(--theme-red); }
.thumbList {
  display: flex;
  gap: 6px;
  overflow-x: auto;
  overflow-y: hidden;
}
.thumbItem {
  flex: 0 0 60px;
  height: 60px;
  border: 2px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
}
.thumbItem.active { border-color: var(--theme-red); }
.thumbItem img { width: 100%; height: 100%; object-fit: cover; }

/* 右:商品信息 */
.itemInfo {
  background: #fff;
  border-radius: 6px;
  padding: 16px;
  display: grid;
  grid-template-rows: auto auto auto auto auto auto;
  gap: 12px;
}
.itemInfo__name { font-size: 16px; color: #333; margin: 0; line-height: 1.4; }
.itemInfo__news {
  background: #fff8e1;
  padding: 6px 10px;
  border-radius: 4px;
  font-size: 12px;
  color: #e65100;
}

/* summary 行 */
.itemInfo__summary, .itemInfo__support {
  display: grid;
  gap: 4px;
}
.summaryRow {
  display: grid;
  grid-template-columns: 80px 1fr auto;
  gap: 8px;
  align-items: center;
  font-size: 13px;
}
.summaryRow__title { color: #999; text-align: right; }
.summaryRow__price { color: var(--theme-red); font-size: 24px; font-weight: bold; }
.summaryRow__price i { font-style: normal; font-size: 14px; }
.summaryRow__price em { font-style: normal; }
.summaryRow__price .notice { font-size: 12px; color: #999; margin-left: 12px; cursor: pointer; }
.summaryRow__remark { font-size: 13px; color: #999; }
.summaryRow__remark em { color: #333; font-style: normal; font-weight: bold; margin-left: 4px; }
.summaryRow__promo { grid-column: 2 / 4; }
.summaryRow__promo .redTag {
  background: var(--theme-red);
  color: #fff;
  padding: 1px 6px;
  border-radius: 3px;
  font-style: normal;
  font-size: 12px;
  margin-right: 6px;
}
.summaryRow__promo em { font-style: normal; color: #666; font-size: 12px; }
.summaryRow__value { color: #666; font-size: 12px; }

/* 规格选择 */
.itemInfo__choose {
  display: grid;
  gap: 10px;
  border-top: 1px solid #eee;
  padding-top: 12px;
}
.chooseRow {
  display: grid;
  grid-template-columns: 80px 1fr;
  gap: 8px;
  align-items: center;
}
.chooseRow__label { color: #999; text-align: right; font-size: 13px; }
.chooseRow__options { display: flex; flex-wrap: wrap; gap: 8px; }
.chooseRow__options a {
  padding: 6px 14px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 12px;
  color: #333;
  text-decoration: none;
  cursor: pointer;
}
.chooseRow__options a:hover { border-color: var(--theme-red); }
.chooseRow__options a.selected {
  border-color: var(--theme-red);
  background: rgba(200, 22, 35, 0.05);
  color: var(--theme-red);
}

/* 数量 + 购物车 */
.itemInfo__action {
  display: flex;
  align-items: center;
  gap: 16px;
  border-top: 1px solid #eee;
  padding-top: 12px;
}
.qtyBox {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}
.qtyBtn {
  width: 30px;
  height: 30px;
  background: #f5f5f5;
  border: none;
  cursor: pointer;
  font-size: 16px;
  color: #666;
}
.qtyBtn:hover { background: #eee; }
.qtyInput {
  width: 40px;
  height: 30px;
  text-align: center;
  border: none;
  border-left: 1px solid #ddd;
  border-right: 1px solid #ddd;
  font-size: 14px;
}
.cartBtn {
  background: var(--theme-red);
  color: #fff;
  border: none;
  padding: 8px 30px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}
.cartBtn:hover { opacity: 0.9; }
</style>
