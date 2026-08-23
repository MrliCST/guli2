<template>
  <section class="productDetailFloor">
    <div class="productDetailFloor_inner">
      <!-- 左边栏 wrapepr -->
      <div class="asideCol">
        <!-- 左边栏 tab -->
        <div class="asideCol_tabs">
          <div class="asideTab"
            :class="{ active: isAsideTabActive(idx) }"
            v-for="(tab, idx) in asideTabs"
            :key="tab"
            @click="asideTab = idx"
          >{{ tab }}</div>
        </div>
        <!-- 左边栏内容 -->
        <div class="asideCol_content">
          <!-- 相关分类列表 -->
          <ul class="catList">
            <li v-for="cat in categories" :key="cat">{{ cat }}</li>
          </ul>
          <!-- 相关商品 -->
          <div class="relatedGoods">
            <BaseGoodsCard
              v-for="(g, i) in relatedGoods"
              :key="i"
              :img="g.img"
              :price="g.price"
              :title="g.title"
              :show-operate="true"
            />
          </div>
        </div>
      </div>
      <!-- 右边详情主体 -->
      <div class="detailCol">
        <!-- 选择搭配 -->
        <div class="fittingSection">
          <!-- 标题 wrapper -->
          <h4 class="sectionTitle">选择搭配</h4>
          <!-- 搭配 wrapper -->
          <div class="fittingBody">
            <!-- 主货物 -->
            <div class="masterGoods">
              <img src="/img/_/l-m01.png" alt="" />
              <em>￥5299</em>
              <i>+</i>
            </div>
            <!-- 搭配物 wrapper -->
            <div class="suitsList">
              <div
                class="suitItem"
                :class="{ checked: suit.checked }"
                v-for="(suit, i) in suits"
                :key="i"
                @click="suit.checked = !suit.checked"
              >
                <img :src="suit.img" alt="" />
                <i>{{ suit.name }}</i>
                <label class="checkboxPretty">
                  <input type="checkbox" v-model="suit.checked" />
                  <span>￥{{ suit.price }}</span>
                </label>
              </div>
            </div>
            <!-- 搭配结果并加入购物车 -->
            <div class="fittingResult">
              <div class="resultNum">已选购{{ checkedCount }}件商品</div>
              <div class="resultLabel"><strong>套餐价</strong></div>
              <div class="resultPrice">￥{{ totalPrice }}</div>
              <button class="cartBtn">加入购物车</button>
            </div>
          </div>
        </div>
        <!-- 详情商品信息 -->
        <div class="detailTabs">
          <!-- 细节导航栏 -->
          <div class="detailTabs_nav">
            <div
              class="detailTab"
              :class="{ active: isDetailTabActive(idx) }"
              v-for="(tab, idx) in detailTabs"
              :key="tab"
              @click="detailTab = idx"
            >{{ tab }}</div>
          </div>
          <!-- 细节内容 -->
          <div class="detailTabs_content">
            <!-- 商品介绍 -->
            <div v-show="showDetailTab0" class="tabPane">
              <table class="specTable">
                <tbody>
                  <template v-for="group in specGroups" :key="group.groupName">
                    <tr v-for="(item, idx) in group.data" :key="group.groupName + '-' + idx">
                      <td v-if="isFirstSpec(idx)" class="specGroup" :rowspan="group.data.length">{{ group.groupName }}</td>
                      <td class="specAttr">{{ item.attrName }}</td>
                      <td class="specValue">{{ item.attrValue }}</td>
                    </tr>
                  </template>
                </tbody>
              </table>
              <div class="introImages">
                <img src="/img/_/intro01.png" alt="" />
                <img src="/img/_/intro02.png" alt="" />
                <img src="/img/_/intro03.png" alt="" />
              </div>
            </div>
            <!-- 基本属性 -->
            <div v-show="showDetailTab1" class="tabPane">
              <p>基本属性</p>
            </div>
            <!-- 售后保障 -->
            <div v-show="showDetailTab2" class="tabPane">
              <p>售后保障</p>
            </div>
            <!-- 商品评价 -->
            <div v-show="showDetailTab3" class="tabPane">
              <!-- 评价标题 -->
              <div class="commentSummary">
                <div class="commentTitle">商品评价</div>
                <div class="commentPercent">
                  好评度 <span class="percent">96%</span>
                </div>
              </div>
              <!-- 评价类型 -->
              <div class="commentTypes">
                <div
                  class="commentType"
                  :class="{ active: isCommentTypeActive(idx) }"
                  v-for="(type, idx) in commentTypes"
                  :key="idx"
                  @click="commentType = idx"
                >{{ type }}</div>
              </div>
              <!-- 评价列表 -->
              <div class="commentList">
                <div class="commentItem" v-for="(c, i) in comments" :key="i">
                  <div class="commentUser">
                    <img :src="c.avatar" alt="" />
                    <div class="userName">{{ c.user }}</div>
                    <div class="userLevel">品享值{{ c.level }}</div>
                  </div>
                  <div class="commentBody">
                    <div class="stars">★★★★☆</div>
                    <p>{{ c.text }}</p>
                    <div class="commentMeta">
                      <ul class="metaMini">
                        <li v-for="m in c.meta" :key="m">{{ m }}</li>
                      </ul>
                      <div class="commentOps">
                        <span>♥ {{ c.likes }}</span>
                        <span>💬 {{ c.replies }}</span>
                      </div>
                    </div>
                    <div class="reply" v-if="c.replyText">
                      <p><span class="replyName">{{ c.replyName }}</span> 回复：</p>
                      <div>{{ c.replyText }}</div>
                      <p class="replyTime">{{ c.replyTime }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <!-- 手机社区 -->
            <div v-show="showDetailTab4" class="tabPane">
              <p>手机社区</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import BaseGoodsCard from '@/components/common/BaseGoodsCard.vue'

const asideTabs = ['相关分类', '推荐品牌']
const asideTab = ref(0)
const isAsideTabActive = (idx: number) => asideTab.value === idx

const categories = ['手机', '手机壳', '内存卡', 'Iphone配件', '贴膜', '手机耳机', '移动电源', '平板电脑']

const relatedGoods = ref([
  { img: '/mall/item/relate-1.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699)' },
  { img: '/mall/item/relate-2.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699)' },
  { img: '/mall/item/relate-3.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699)' }
])

const detailTabs = ['商品介绍', '规格与包装', '售后保障', '商品评价', '手机社区']
const detailTab = ref(0)
const isDetailTabActive = (idx: number) => detailTab.value === idx
const showDetailTab0 = computed(() => detailTab.value === 0)
const showDetailTab1 = computed(() => detailTab.value === 1)
const showDetailTab2 = computed(() => detailTab.value === 2)
const showDetailTab3 = computed(() => detailTab.value === 3)
const showDetailTab4 = computed(() => detailTab.value === 4)
const isFirstSpec = (idx: number) => idx === 0

const specGroups = [
  {
    groupName: '主体',
    data: [
      { attrName: '品牌', attrValue: 'Apple' },
      { attrName: '商品名称', attrValue: 'APPLEiPhone 6s Plus' },
      { attrName: '商品编号', attrValue: '1861098' },
      { attrName: '商品毛重', attrValue: '0.51kg' },
      { attrName: '商品产地', attrValue: '中国大陆' }
    ]
  },
  {
    groupName: '硬件配置',
    data: [
      { attrName: '分辨率', attrValue: '1920*1080(FHD)' },
      { attrName: '后置摄像头', attrValue: '1200万像素' },
      { attrName: '前置摄像头', attrValue: '500万像素' },
      { attrName: '核数', attrValue: '其他' },
      { attrName: '频率', attrValue: '以官网信息为准' },
      { attrName: '系统', attrValue: '苹果（IOS）' },
      { attrName: '像素', attrValue: '1000-1600万' },
      { attrName: '机身内存', attrValue: '64GB' },
      { attrName: '热点', attrValue: '指纹识别，Apple Pay，金属机身，拍照神器' }
    ]
  }
]

const commentTypes = ['全部评价(123456)', '晒图(500)', '追评(500)', '好评(500)', '中评(500)', '差评(500)']
const commentType = ref(0)
const isCommentTypeActive = (idx: number) => commentType.value === idx

const suits = reactive([
  { img: '/mall/item/suit-1.png', name: 'Feless费勒斯VR', price: 39, checked: false },
  { img: '/mall/item/suit-2.png', name: 'Feless费勒斯VR', price: 50, checked: false },
  { img: '/mall/item/suit-3.png', name: 'Feless费勒斯VR', price: 59, checked: false },
  { img: '/mall/item/suit-4.png', name: 'Feless费勒斯VR', price: 99, checked: false }
])

const checkedCount = computed(() => suits.filter(s => s.checked).length)
const totalPrice = computed(() => 5299 + suits.filter(s => s.checked).reduce((sum, s) => sum + s.price, 0))

const comments = ref([
  {
    avatar: '/img/_/photo.jpg',
    user: '用户****1',
    level: '258698',
    text: '手机还不错，可以的可以的',
    meta: ['玫瑰金', '标配版', '2017-11-02 13:23'],
    likes: 325,
    replies: 256,
    replyName: '商家名称官方旗舰店',
    replyText: '亲爱的用户，非常抱歉亲爱的用户，非常抱歉亲爱的用户，非常抱歉亲爱的用户，非常抱歉。',
    replyTime: '2017-11-13'
  },
  {
    avatar: '/img/_/photo.jpg',
    user: '用户****2',
    level: '158698',
    text: '手机还不错，可以的可以的',
    meta: ['玫瑰金', '标配版', '2017-11-02 13:23'],
    likes: 325,
    replies: 256
  },
  {
    avatar: '/img/_/photo.jpg',
    user: '用户****3',
    level: '358698',
    text: '手机还不错，可以的可以的',
    meta: ['玫瑰金', '标配版', '2017-11-02 13:23'],
    likes: 325,
    replies: 256
  }
])
</script>

<style scoped lang="scss">
.productDetailFloor {
  background: Seashell;
  padding: 10px 0;

  /** 产品详情 wrapper */
  .productDetailFloor_inner {
    display: grid;
    grid-template-columns: 210px 1fr;
    gap: 12px;
    align-items: start;
  }

  /* ===== 左侧栏 wrapper ===== */
  .asideCol {
    display: grid;
    grid-template-rows: auto 1fr;

    /** 左侧栏 tabs */
    .asideCol_tabs {
      display: grid;
      grid-template-columns: 1fr 1fr;
      border-radius: 6px 6px 0 0;
      overflow: hidden;

      /** 左侧栏 tab */
      .asideTab {
        background: #f5f5f5;
        padding: 10px 0;
        text-align: center;
        font-size: 13px;
        color: #666;
        cursor: pointer;
      }

      /** 激活样式 */
      .active {
        background: #c81623;
        color: #fff;
      }
    }

    /** 左边栏内容 */
    .asideCol_content {
      background: #fff;
      border-radius: 0 0 6px 6px;
      padding: 10px;

      /** 分类列表 */
      .catList {
        list-style: none;
        padding: 0;
        margin: 0 0 10px;

        li {
          padding: 6px 8px;
          font-size: 12px;
          color: #555;
          border-bottom: 1px dashed #eee;
          cursor: pointer;

          &:hover {
            color: #c81623;
          }
        }
      }

      /** 相关商品 */
      .relatedGoods {
        display: grid;
        gap: 8px;
      }
    }
  }

  /* ===== 右详情 ===== */
  .detailCol {
    display: grid;
    grid-template-rows: auto auto;
    gap: 12px;

    /* 选择搭配 wrapper */
    .fittingSection {
      background: #fff;
      border-radius: 6px;
      padding: 12px;

      /** 标题 */
      .sectionTitle {
        margin: 0 0 10px;
        font-size: 14px;
        color: #333;
        border-left: 3px solid #c81623;
        padding-left: 8px;
      }

      /** 搭配 wrapper */
      .fittingBody {
        display: grid;
        grid-template-columns: 120px 1fr 140px;
        gap: 12px;
        align-items: center;

        /** 主货物 */
        .masterGoods {
          text-align: center;
          position: relative;

          img {
            width: 80px;
            height: 80px;
            object-fit: contain;
          }

          /** ￥符号样式 */
          em {
            display: block;
            font-style: normal;
            color: #c81623;
            font-size: 14px;
            margin-top: 4px;
          }

          /** +符号样式 */
          i {
            position: absolute;
            right: -8px;
            top: 35px;
            font-style: normal;
            font-size: 18px;
            color: #ccc;
            font-weight: bold;
          }
        }

        /** 搭配列表 wrapper */
        .suitsList {
          display: flex;
          gap: 10px;

          /** 搭配列表 */
          .suitItem {
            text-align: center;
            cursor: pointer;
            padding: 6px;
            border: 2px solid transparent;
            border-radius: 6px;

            img {
              width: 60px;
              height: 60px;
              object-fit: contain;
            }

            /** 搭配物字体 */
            i {
              display: block;
              font-style: normal;
              font-size: 11px;
              color: #555;
              margin: 4px 0;
            }

            /** 多选框样式 */
            .checkboxPretty {
              display: flex;
              align-items: center;
              justify-content: center;
              gap: 4px;
              font-size: 12px;
              color: #c81623;

              input {
                margin: 0;
              }
            }
          }

          /** suitsList标记类上标记该属性生效 */
          .checked {
            border-color: #c81623;
            background: rgba(200, 22, 35, 0.05);
          }
        }

        /** 决定并加入购物车 */
        .fittingResult {
          text-align: center;
          border-left: 1px solid #eee;
          padding-left: 12px;

          .resultNum {
            font-size: 12px;
            color: #999;
            margin-bottom: 6px;
          }

          .resultLabel {
            font-size: 13px;
            color: #333;
          }

          .resultPrice {
            font-size: 20px;
            color: #c81623;
            font-weight: bold;
            margin: 4px 0 8px;
          }

          .cartBtn {
            background: #c81623;
            color: #fff;
            border: none;
            padding: 6px 16px;
            border-radius: 4px;
            font-size: 12px;
            cursor: pointer;
          }
        }
      }
    }

    /* 详情内容Tabs wrapper */
    .detailTabs {
      background: #fff;
      border-radius: 6px;
      overflow: hidden;

      /** 导航条 wrapper */
      .detailTabs_nav {
        display: flex;
        border-bottom: 1px solid #eee;

        /** 导航项 */
        .detailTab {
          padding: 12px 20px;
          font-size: 13px;
          color: #666;
          cursor: pointer;
          border-right: 1px solid #eee;
        }

        /** 激活 */
        .active {
          color: #c81623;
          border-bottom: 2px solid #c81623;
          font-weight: bold;
        }
      }

      /** 详情Tabs内容 （仪表盘们的 wrapper） */
      .detailTabs_content {
        padding: 16px;

        /** tab仪表盘 */
        .tabPane {
          font-size: 13px;
          color: #333;

          // =========== 商品详情 =================
          /* 商品详情介绍 */
          .specTable {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 16px;
            font-size: 12px;
            color: #555;

            td {
              border: 1px solid #eee;
              padding: 6px 8px;
            }

            .specGroup {
              width: 120px;
              text-align: center;
              background: #f5f5f5;
              font-weight: bold;
              color: #333;
            }

            .specAttr {
              width: 160px;
              background: #f9f9f9;
              color: #666;
            }

            .specValue {
              color: #555;
            }
          }

          /** 商品介绍大图 */
          .introImages {
            img {
              width: 100%;
              margin-bottom: 8px;
              border-radius: 4px;
            }
          }

          // =========== 商品评价 =================
          /* 评价标题（概括） */
          .commentSummary {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;

            .commentTitle {
              font-size: 16px;
              font-weight: bold;
            }

            .commentPercent {
              font-size: 14px;
              color: #666;

              .percent {
                color: #c81623;
                font-size: 24px;
                font-weight: bold;
              }
            }
          }

          /**评价种类 */
          .commentTypes {
            display: flex;
            gap: 8px;
            margin-bottom: 16px;
            flex-wrap: wrap;

            .commentType {
              padding: 4px 12px;
              border: 1px solid #ddd;
              border-radius: 4px;
              font-size: 12px;
              color: #555;
              cursor: pointer;

              &.active {
                border-color: #c81623;
                color: #c81623;
              }
            }
          }

          /** 评价列表 */
          .commentList {
            display: grid;
            gap: 12px;

            .commentItem {
              display: grid;
              grid-template-columns: 140px 1fr;
              gap: 12px;
              padding: 12px 0;
              border-bottom: 1px solid #eee;

              .commentUser {
                text-align: center;

                img {
                  width: 40px;
                  height: 40px;
                  border-radius: 50%;
                }

                .userName {
                  font-size: 12px;
                  color: #333;
                  margin-top: 4px;
                }

                .userLevel {
                  font-size: 11px;
                  color: #999;
                }
              }

              .commentBody {
                font-size: 13px;

                .stars {
                  color: #ff9800;
                  font-size: 14px;
                  margin-bottom: 4px;
                }

                > p {
                  margin: 4px 0;
                  color: #333;
                }

                .commentMeta {
                  display: flex;
                  justify-content: space-between;
                  align-items: center;
                  margin-top: 6px;

                  .metaMini {
                    list-style: none;
                    padding: 0;
                    margin: 0;
                    display: flex;
                    gap: 8px;

                    li {
                      font-size: 11px;
                      color: #999;
                    }
                  }

                  .commentOps {
                    display: flex;
                    gap: 12px;

                    span {
                      font-size: 12px;
                      color: #999;
                    }
                  }
                }

                .reply {
                  background: #f9f9f9;
                  padding: 8px 12px;
                  border-radius: 4px;
                  margin-top: 8px;

                  .replyName {
                    color: #c81623;
                    font-weight: bold;
                  }

                  .replyTime {
                    font-size: 11px;
                    color: #999;
                    margin-top: 4px;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
</style>
