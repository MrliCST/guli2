<template>
  <section class="bannerFloor">
    <div class="bannerFloor_inner">
      <!-- 种类树表 -->
      <div class="categoryTreeTable">
        <h4><a href="#">图书、音像、数字商品</a></h4>
        <h4><a href="#">家用电器</a></h4>
        <h4><a href="#">手机、数码、充值</a></h4>
        <h4><a href="#">电脑、办公</a></h4>
        <h4><a href="#">家居、家具、家装、厨具</a></h4>
        <h4><a href="#">服饰内衣</a></h4>
        <h4><a href="#">个护化妆</a></h4>
        <h4><a href="#">运动健康</a></h4>
        <h4><a href="#">汽车用品</a></h4>
        <h4><a href="#">彩票、旅行</a></h4>
        <h4><a href="#">理财、众筹</a></h4>
        <h4><a href="#">母婴、玩具</a></h4>
        <h4><a href="#">箱包</a></h4>
        <h4><a href="#">运动户外</a></h4>
      </div>
      <!-- 滚动屏 -->
      <div class="bannerFloor_carousel" @mouseenter="stopAuto" @mouseleave="startAuto">
        <img
          class="slide"
          v-for="(b, i) in banners"
          :key="b"
          :src="b"
          :class="{ active: isSlideActive(i) }"
          alt="Banner"
        />
        <button class="arrow prev" @click="prev">‹</button>
        <button class="arrow next" @click="next">›</button>
        <div class="dots">
          <span
            v-for="(b, i) in banners"
            :key="'dot' + i"
            :class="{ active: isSlideActive(i) }"
            @click="goTo(i)"
          ></span>
        </div>
      </div>
      <!-- banner边栏 -->
      <div class="bannerFloor_sidebar">
        <!-- 标题 -->
        <div class="newsHeader">
          <span class="title">谷粒快报</span>
          <span class="more">更多 &gt;</span>
        </div>
        <!-- 新闻项 -->
        <div class="newsList">
          <ul>
            <li v-for="n in news" :key="n.text">
              <span class="bold" :style="{ color: n.color }">[{{ n.tag }}]</span>{{ n.text }}
            </li>
          </ul>
        </div>
        <!-- 服务区域 -->
        <div class="bannerSidebar_services">
          <div class="serviceItem" v-for="s in services" :key="s.label">
            <span class="serviceIcon" :style="{ backgroundPosition: s.iconPos }"></span>
            <span class="serviceText">{{ s.label }}</span>
          </div>
        </div>
        <!-- 广告位 -->
        <div class="bannerSidebar_ad">
          <img :src="bannerSidebarAd" alt="广告" />
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const banners = ref([
  '/mall/index/bannerCarousel-1.jpg',
  '/mall/index/bannerCarousel-2.jpg',
  '/mall/index/bannerCarousel-3.jpg'
])
const current = ref(0)
const isSlideActive = (i: number) => i === current.value
const bannerSidebarAd = ref('/mall/index/bannerSidebarAd.png')

let timer: ReturnType<typeof setInterval> | undefined
const next = () => { current.value = (current.value + 1) % banners.value.length }
const prev = () => { current.value = (current.value - 1 + banners.value.length) % banners.value.length }
const goTo = (i: number) => { current.value = i }
const startAuto = () => {
  stopAuto()
  timer = setInterval(next, 3000)
}
const stopAuto = () => {
  if (timer) {
    clearInterval(timer)
    timer = undefined
  }
}
onMounted(startAuto)
onUnmounted(stopAuto)

const news = ref([
  { tag: '特惠', text: '备战开学季 全民半价购数码', color: '#c81623' },
  { tag: '公告', text: '谷粒会员日 爆品低至5折', color: '#c81623' },
  { tag: '特惠', text: '家电以旧换新 最高补贴800', color: '#c81623' },
  { tag: '公告', text: '部分地区物流时效调整通知', color: '#c81623' },
  { tag: '特惠', text: '美妆盛典 买一送一', color: '#c81623' }
])

// 服务项：label 文字 + iconPos 精灵图坐标（serviceIcons.png 为 4 列 × 3 行，每格 30×30）
const services = ref([
  { label: '话费', iconPos: '0px 0px' },
  { label: '机票', iconPos: '-30px 0px' },
  { label: '电影票', iconPos: '-60px 0px' },
  { label: '游戏', iconPos: '-90px 0px' },
  { label: '彩票', iconPos: '0px -30px' },
  { label: '加油', iconPos: '-30px -30px' },
  { label: '酒店', iconPos: '-60px -30px' },
  { label: '火车票', iconPos: '-90px -30px' },
  { label: '众筹', iconPos: '0px -60px' },
  { label: '理财', iconPos: '-30px -60px' },
  { label: '礼品卡', iconPos: '-60px -60px' },
  { label: '白条', iconPos: '-90px -60px' }
])
</script>

<style scoped lang="scss">
/** banner层 */
.bannerFloor {
  /**各层负责与上层的空隙，不制造向下的空隙 */
  margin-top: 10px;

  .bannerFloor_inner {
    display: grid;
    background-color: #FAFAF9;
    grid-template-columns: 210px 1fr 280px;
    gap: 5px;


    .categoryTreeTable {
      padding: 10px 0;

      h4 {
        font-size: 14px;
        font-weight: normal;
        margin: 8px 0 8px 12px;

        a {
          display: inline-block;
          padding: 2px 10px;
          color: inherit;
          text-decoration: none;

          &:hover {
            background: #ddd;
          }
        }
      }
    }

    /** 滚动屏 */
    .bannerFloor_carousel {
      position: relative;
      height: 539px;
      overflow: hidden;

      .slide {
        position: absolute;
        inset: 0;
        width: 100%;
        height: 100%;
        object-fit: cover;
        opacity: 0;
        transition: opacity 0.6s ease;

        &.active {
          opacity: 1;
        }
      }

      .arrow {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        width: 30px;
        height: 60px;
        border: none;
        background: rgba(0, 0, 0, 0.3);
        color: #fff;
        font-size: 24px;
        cursor: pointer;
        opacity: 0;
        transition: opacity 0.3s;

        &.prev {
          left: 0;
          border-radius: 0 4px 4px 0;
        }

        &.next {
          right: 0;
          border-radius: 4px 0 0 4px;
        }
      }

      &:hover .arrow {
        opacity: 1;
      }

      .dots {
        position: absolute;
        bottom: 12px;
        left: 50%;
        transform: translateX(-50%);
        display: flex;
        gap: 6px;

        span {
          width: 10px;
          height: 10px;
          border-radius: 50%;
          background: rgba(255, 255, 255, 0.5);
          cursor: pointer;

          &.active {
            background: #c81623;
          }
        }
      }
    }

    /** banner边栏 */
    .bannerFloor_sidebar {
      display: grid;
      grid-template-rows: auto auto auto 1fr;

      /** 标题层 */
      .newsHeader {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px;
        border: 1px solid #ccc;

        /** 标题 */
        .title {
          font-size: 14px;
          font-weight: bold;
        }

        /** 更多 */
        .more {
          color: #999;
          font-weight: normal;
          font-size: 12px;
        }
      }

      /** 新闻项 */
      .newsList {
        border: 1px solid #ccc;
        border-top: none;

        ul {
          list-style: none;
          padding-left: 12px;

          li {
            font-size: 12px;
            line-height: 1.9;

            .bold {
              font-weight: bold;
              margin-right: 4px;
            }
          }
        }
      }

      /** 服务区域 */
      .bannerSidebar_services {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        border-left: 1px solid #ccc;

        .serviceItem {
          border-right: 1px solid #ccc;
          border-bottom: 1px solid #ccc;
          background: rgba(255, 255, 255, 0.8);
          text-align: center;
          padding: 12px 0;
          font-size: 12px;
          color: #555;
          // 图标在上、文字在下
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 4px;

          .serviceIcon {
            width: 30px;
            height: 30px;
            background-image: url('@/assets/mall/index/bannerServiceIcons.png');
            background-repeat: no-repeat;
          }

          .serviceText {
            font-size: 12px;
            color: #555;
          }
        }
      }

      /** 广告位 */
      .bannerSidebar_ad {
        border: 1px solid #ccc;

        img {
          width: 100%;
          height: 110px;
          object-fit: cover;
        }
      }
    }
  }
}

</style>
