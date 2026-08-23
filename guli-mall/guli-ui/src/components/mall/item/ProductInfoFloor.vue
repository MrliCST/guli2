<template>
  <section class="productInfoFloor">
    <div class="productInfoFloor_inner">
      <!-- 面包屑 -->
      <div class="productInfoFloor_crumb">
        <a href="#">Apple苹果</a>
        <span>></span>
        <span class="active">iphone 6S系类</span>
      </div>

      <!-- 商品主体 -->
      <div class="productInfoFloor_main">
        <!-- 左侧区域：图片预览 -->
        <div class="previewWrap">
          <!-- 图片预览大图 -->
          <div class="previewWrap_main">
            <img :src="mainImg" alt="" />
          </div>
          <!-- 下栏图集 -->
          <div class="previewWrap_thumbs">
            <button class="thumbBtn prev" @click="scrollThumbs(-3)">&lt;</button>
            <div class="thumbList">
              <div
                class="thumbItem"
                :class="{ active: isActive(idx) }"
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

        <!-- 右侧区域: 商品信息 -->
        <div class="itemInfo">
          <!-- 主标题 -->
          <h4 class="itemInfo_name">{{ goods.name }}</h4>
          <!-- 建议信息 -->
          <div class="itemInfo_news">
            <span>推荐选择下方[移动优惠购],手机套餐齐搞定,不用换号,每月还有花费返</span>
          </div>
          <!-- 价格行 -->
          <div class="summaryRow">
            <span class="summaryRow_title">价 格</span>
            <div class="summaryRow_price">
              <b>¥</b><em>{{ goods.price }}</em>
              <span class="notice">降价通知</span>
            </div>
            <span class="summaryRow_remark"><i>累计评价</i><em>{{ goods.comment }}</em></span>
          </div>
          <!-- 促销行 -->
          <div class="summaryRow">
            <span class="summaryRow_title">促 销</span>
            <div class="summaryRow_value">
              <i class="redTag">加价购</i>
              <span>满999.00另加20.00元，或满1999.00另加30.00元，即可换购热销商品</span>
            </div>
          </div>
          <!-- 支持行 -->
          <div class="summaryRow">
            <span class="summaryRow_title">支 持</span>
            <div class="summaryRow_value">以旧换新，闲置手机回收 4G套餐超值抢 礼品购</div>
          </div>
          <!-- 配送到行 -->
          <div class="summaryRow">
            <span class="summaryRow_title">配 送 至</span>
            <div class="summaryRow_value">北京市 昌平区</div>
          </div>
          <!-- 销售属性选择 -->
          <div class="itemInfo_saleAttr">
            <!-- 销售属性 -->
            <div class="chooseRow" v-for="spec in specs" :key="spec.label">
              <!-- 属性名 -->
              <div class="chooseRow_label">{{ spec.label }}</div>
              <!-- 属性值 -->
              <div class="chooseRow_options">
                <a href="#"
                  v-for="(opt, i) in spec.options"
                  :key="i" :class="{ selected: isSelected(spec, i) }"
                  @click.prevent="spec.activeIdx = i"
                >{{ opt }}</a>
              </div>
            </div>
          </div>
          <!-- 数量 + 加入购物车 -->
          <div class="itemInfo_action">
            <div class="qtyBox">
              <button class="qtyBtn" @click="decreaseQty">-</button>
              <input class="qtyInput" v-model="qty" type="text" />
              <button class="qtyBtn" @click="increaseQty">+</button>
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

const mainImg = ref('/mall/item/productInfo_show-b1.png')
const activeThumb = ref(0)

const isActive = (idx: number) => activeThumb.value === idx
const thumbs = ref([
  { small: '/mall/item/productInfo_show-s1.png', big: '/mall/item/productInfo_show-b1.png' },
  { small: '/mall/item/productInfo_show-s2.png', big: '/mall/item/productInfo_show-b2.png' },
  { small: '/mall/item/productInfo_show-s3.png', big: '/mall/item/productInfo_show-b3.png' },
  { small: '/mall/item/productInfo_show-s1.png', big: '/mall/item/productInfo_show-b1.png' },
  { small: '/mall/item/productInfo_show-s2.png', big: '/mall/item/productInfo_show-b2.png' },
  { small: '/mall/item/productInfo_show-s3.png', big: '/mall/item/productInfo_show-b3.png' },
  { small: '/mall/item/productInfo_show-s1.png', big: '/mall/item/productInfo_show-b1.png' },
  { small: '/mall/item/productInfo_show-s2.png', big: '/mall/item/productInfo_show-b2.png' },
  { small: '/mall/item/productInfo_show-s3.png', big: '/mall/item/productInfo_show-b3.png' }
])

function selectThumb(idx: number) {
  activeThumb.value = idx
  mainImg.value = thumbs.value[idx]!.big
}

function scrollThumbs(step: number) {
  // 静态:缩略图滚动预留
  void step
}

const qty = ref(1)

function decreaseQty() {
  if (qty.value > 1) qty.value--
}

function increaseQty() {
  qty.value++
}

const specs = reactive([
  { label: '选择颜色', options: ['金色', '银色', '深空灰', '玫瑰金'], activeIdx: 0 },
  { label: '内存容量', options: ['16G', '64G', '128G'], activeIdx: 1 },
  { label: '选择版本', options: ['公开版', '移动版'], activeIdx: 0 },
  { label: '购买方式', options: ['官方标配', '移动优惠版', '电信优惠版'], activeIdx: 0 }
])

const isSelected = (spec: { activeIdx: number }, i: number) => spec.activeIdx === i
</script>

<style scoped lang="scss">
.productInfoFloor {
  .productInfoFloor_inner {
    display: grid;
    grid-template-rows: auto auto;
    gap: 10px;

    /* 面包屑 */
    .productInfoFloor_crumb {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 12px;
      color: #999;
      padding: 6px 0;

      a {
        color: #666;
      }

      .active {
        color: #333;
      }
    }

    /* 主体:左右分栏 */
    .productInfoFloor_main {
      display: grid;
      grid-template-columns: 400px 1fr;
      gap: 20px;

      /* 左:图片预览 */
      .previewWrap {
        display: grid;
        grid-template-rows: 400px auto;
        gap: 10px;

        /** 大图 */
        .previewWrap_main {
          background: #fff;
          border-radius: 6px;
          overflow: hidden;
          display: flex;
          align-items: center;
          justify-content: center;

          img {
            width: 100%;
            height: 100%;
            object-fit: contain;
          }
        }

        /** 图栏包裹者 */
        .previewWrap_thumbs {
          display: grid;
          grid-template-columns: 24px 1fr 24px;
          gap: 8px;
          align-items: center;

          /** 图栏左右切换按钮 */
          .thumbBtn {
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 4px;
            cursor: pointer;
            height: 60px;
            font-size: 14px;
            color: #999;

            &:hover {
              border-color: #c81623;
              color: #c81623;
            }
          }

          /** 图栏列表 */
          .thumbList {
            display: flex;
            gap: 6px;
            overflow-x: auto;
            overflow-y: hidden;

            .thumbItem {
              flex: 0 0 60px;
              height: 60px;
              border: 2px solid transparent;
              border-radius: 4px;
              overflow: hidden;
              cursor: pointer;

              &.active {
                border-color: #c81623;
              }

              img {
                width: 100%;
                height: 100%;
                object-fit: cover;
              }
            }
          }
        }
      }

      /* 右:商品信息 */
      .itemInfo {
        background: #fff;
        border-radius: 6px;
        padding: 16px;
        display: grid;
        grid-template-rows: repeat(8, auto);
        gap: 12px;

        /** 商品名字 */
        .itemInfo_name {
          font-size: 16px;
          color: #333;
          margin: 0;
          line-height: 1.4;
        }

        /** 商品建议信息 */
        .itemInfo_news {
          background: #fff8e1;
          padding: 6px 10px;
          border-radius: 4px;
          font-size: 12px;
          color: #e65100;
        }

        /** 商品简介行 */
        .summaryRow {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 13px;

          /** 简介名 */
          .summaryRow_title {
            flex: none;  /** 不参与flex布局 */
            width: 80px;
            color: #999;
            text-align: center;
          }

          /** 简介价格 */
          .summaryRow_price {
            color: #c81623;
            font-size: 24px;
            font-weight: bold;

            /** 降价通知 */
            .notice {
              font-size: 12px;
              color: #999;
              margin-left: 12px;
              cursor: pointer;
            }
          }

          /** 简介评价 */
          .summaryRow_remark {
            margin-left: auto;
            font-size: 13px;
            color: #999;

            em {
              color: #333;
              font-weight: bold;
              margin-left: 4px;
            }
          }

          /** 简介值 */
          .summaryRow_value {
            color: #666;
            font-size: 12px;

            .redTag {
              background: #c81623;
              color: #fff;
              padding: 1px 6px;
              border-radius: 3px;
              font-style: normal;
              font-size: 12px;
              margin-right: 6px;
            }
          }
        }

        /**  销售属性 */
        .itemInfo_saleAttr {
          display: grid;
          gap: 10px;
          border-top: 1px solid #eee;
          padding-top: 12px;

          /** 销售属性行 */
          .chooseRow {
            display: grid;
            grid-template-columns: 80px 1fr;
            gap: 8px;
            align-items: center;

            /** 销售属性名 */
            .chooseRow_label {
              color: #999;
              text-align: center;  /** div中文字居中 */
              font-size: 13px;
            }

            /** 销售属性值 */
            .chooseRow_options {
              display: flex;
              flex-wrap: wrap;  /** 溢出换行 */
              gap: 8px;

              a {
                padding: 6px 14px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 12px;
                color: #333;
                text-decoration: none;
                cursor: pointer;

                &:hover {
                  border-color: #c81623;
                }

                &.selected {
                  border-color: #c81623;
                  background: rgba(200, 22, 35, 0.05);
                  color: #c81623;
                }
              }
            }
          }
        }

        /* 数量 + 购物车 */
        .itemInfo_action {
          display: flex;
          align-items: center;
          gap: 16px;
          border-top: 1px solid #eee;
          padding-top: 12px;

          .qtyBox {
            display: flex;
            align-items: center;
            border: 1px solid #ddd;
            border-radius: 4px;
            overflow: hidden;

            .qtyBtn {
              width: 30px;
              height: 30px;
              background: #f5f5f5;
              border: none;
              cursor: pointer;
              font-size: 16px;
              color: #666;

              &:hover {
                background: #eee;
              }
            }

            .qtyInput {
              width: 40px;
              height: 30px;
              text-align: center;
              border: none;
              border-left: 1px solid #ddd;
              border-right: 1px solid #ddd;
              font-size: 14px;
            }
          }

          .cartBtn {
            background: #c81623;
            color: #fff;
            border: none;
            padding: 8px 30px;
            border-radius: 4px;
            font-size: 14px;
            cursor: pointer;

            &:hover {
              opacity: 0.9;
            }
          }
        }
      }
    }
  }
}
</style>
