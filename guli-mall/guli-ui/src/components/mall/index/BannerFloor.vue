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
        <h4><a href="#">箱包</a></h4>
      </div>
      <!-- 滚动屏 -->
      <div class="bannerFloor_carousel">
        <img src="/img/banner1.jpg" alt="Banner" />
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
          <div class="serviceItem" v-for="s in services" :key="s">{{ s }}</div>
        </div>
        <!-- 广告位 -->
        <div class="bannerSidebar_ad">
          <img src="/img/ad1.png" alt="广告" />
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const news = ref([
  { tag: '特惠', text: '备战开学季 全民半价购数码', color: '#c81623' },
  { tag: '公告', text: '谷粒会员日 爆品低至5折', color: '#c81623' },
  { tag: '特惠', text: '家电以旧换新 最高补贴800', color: '#c81623' },
  { tag: '公告', text: '部分地区物流时效调整通知', color: '#c81623' },
  { tag: '特惠', text: '美妆盛典 买一送一', color: '#c81623' }
])

const services = ref([
  '话费', '机票', '电影票', '游戏',
  '彩票', '加油', '酒店', '火车票',
  '众筹', '理财', '礼品卡', '白条'
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
      h4 {
        font-size: 14px;
        font-weight: normal;
        margin: 4px 0 4px 12px;

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
      height: 438px;

      img {
        width: 100%;
        height: 438px;
        object-fit: cover;
      }
    }

    /** banner边栏 */
    .bannerFloor_sidebar {
      display: grid;
      grid-template-rows: auto auto auto 1fr;

      /** 标题 */
      .newsHeader {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px;
        border: 1px solid #ccc;

        .title {
          font-size: 14px;
          font-weight: bold;
        }

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
