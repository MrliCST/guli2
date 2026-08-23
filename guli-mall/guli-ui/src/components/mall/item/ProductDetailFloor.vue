<template>
  <section class="productDetailFloor">
    <div class="productDetailFloor_inner">
      <!-- 左:相关分类侧栏 -->
      <div class="asideCol">
        <div class="asideCol_tabs">
          <div
            class="asideTab"
            :class="{ active: asideTab === idx }"
            v-for="(tab, idx) in asideTabs"
            :key="tab"
            @click="asideTab = idx"
          >{{ tab }}</div>
        </div>
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

      <!-- 右:详情主体 -->
      <div class="detailCol">
        <!-- 选择搭配 -->
        <div class="fittingSection">
          <h4 class="sectionTitle">选择搭配</h4>
          <div class="fittingBody">
            <div class="masterGoods">
              <img src="/img/_/l-m01.png" alt="" />
              <em>￥5299</em>
              <i>+</i>
            </div>
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
            <div class="fittingResult">
              <div class="resultNum">已选购{{ checkedCount }}件商品</div>
              <div class="resultLabel"><strong>套餐价</strong></div>
              <div class="resultPrice">￥{{ totalPrice }}</div>
              <button class="cartBtn">加入购物车</button>
            </div>
          </div>
        </div>

        <!-- 详情 Tab -->
        <div class="detailTabs">
          <div class="detailTabs_nav">
            <div
              class="detailTab"
              :class="{ active: detailTab === idx }"
              v-for="(tab, idx) in detailTabs"
              :key="tab"
              @click="detailTab = idx"
            >{{ tab }}</div>
          </div>
          <div class="detailTabs_content">
            <!-- 商品介绍 -->
            <div v-show="detailTab === 0" class="tabPane">
              <table class="specTable">
                <tbody>
                  <template v-for="group in specGroups" :key="group.groupName">
                    <tr v-for="(item, idx) in group.data" :key="group.groupName + '-' + idx">
                      <td v-if="idx === 0" class="specGroup" :rowspan="group.data.length">{{ group.groupName }}</td>
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
            <!-- 规格与包装 -->
            <div v-show="detailTab === 1" class="tabPane">
              <p>规格与包装</p>
            </div>
            <!-- 售后保障 -->
            <div v-show="detailTab === 2" class="tabPane">
              <p>售后保障</p>
            </div>
            <!-- 商品评价 -->
            <div v-show="detailTab === 3" class="tabPane">
              <div class="commentSummary">
                <div class="commentTitle">商品评价</div>
                <div class="commentPercent">
                  好评度 <span class="percent">96%</span>
                </div>
              </div>
              <div class="commentTypes">
                <div
                  class="commentType"
                  :class="{ active: commentType === idx }"
                  v-for="(type, idx) in commentTypes"
                  :key="idx"
                  @click="commentType = idx"
                >{{ type }}</div>
              </div>
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
            <div v-show="detailTab === 4" class="tabPane">
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

const categories = ['手机', '手机壳', '内存卡', 'Iphone配件', '贴膜', '手机耳机', '移动电源', '平板电脑']

const relatedGoods = ref([
  { img: '/img/_/part01.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699)' },
  { img: '/img/_/part02.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699)' },
  { img: '/img/_/part03.png', price: '6088.00', title: 'Apple苹果iPhone 6s (A1699)' }
])

const detailTabs = ['商品介绍', '规格与包装', '售后保障', '商品评价', '手机社区']
const detailTab = ref(0)

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

const suits = reactive([
  { img: '/img/_/dp01.png', name: 'Feless费勒斯VR', price: 39, checked: false },
  { img: '/img/_/dp02.png', name: 'Feless费勒斯VR', price: 50, checked: false },
  { img: '/img/_/dp03.png', name: 'Feless费勒斯VR', price: 59, checked: false },
  { img: '/img/_/dp04.png', name: 'Feless费勒斯VR', price: 99, checked: false }
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

  .productDetailFloor_inner {
    width: 1200px;
    margin: 0 auto;
    display: grid;
    grid-template-columns: 210px 1fr;
    gap: 12px;
  }

  /* ===== 左侧栏 ===== */
  .asideCol {
    display: grid;
    grid-template-rows: auto 1fr;

    .asideCol_tabs {
      display: grid;
      grid-template-columns: 1fr 1fr;
      border-radius: 6px 6px 0 0;
      overflow: hidden;

      .asideTab {
        background: #f5f5f5;
        padding: 10px 0;
        text-align: center;
        font-size: 13px;
        color: #666;
        cursor: pointer;

        &.active {
          background: #c81623;
          color: #fff;
        }
      }
    }

    .asideCol_content {
      background: #fff;
      border-radius: 0 0 6px 6px;
      padding: 10px;

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

    /* 选择搭配 */
    .fittingSection {
      background: #fff;
      border-radius: 6px;
      padding: 12px;

      .sectionTitle {
        margin: 0 0 10px;
        font-size: 14px;
        color: #333;
        border-left: 3px solid #c81623;
        padding-left: 8px;
      }

      .fittingBody {
        display: grid;
        grid-template-columns: 120px 1fr 140px;
        gap: 12px;
        align-items: center;

        .masterGoods {
          text-align: center;
          position: relative;

          img {
            width: 80px;
            height: 80px;
            object-fit: contain;
          }

          em {
            display: block;
            font-style: normal;
            color: #c81623;
            font-size: 14px;
            margin-top: 4px;
          }

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

        .suitsList {
          display: flex;
          gap: 10px;

          .suitItem {
            text-align: center;
            cursor: pointer;
            padding: 6px;
            border: 2px solid transparent;
            border-radius: 6px;

            &.checked {
              border-color: #c81623;
              background: rgba(200, 22, 35, 0.05);
            }

            img {
              width: 60px;
              height: 60px;
              object-fit: contain;
            }

            > i {
              display: block;
              font-style: normal;
              font-size: 11px;
              color: #555;
              margin: 4px 0;
            }

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
        }

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

    /* 详情 Tab */
    .detailTabs {
      background: #fff;
      border-radius: 6px;
      overflow: hidden;

      .detailTabs_nav {
        display: flex;
        border-bottom: 1px solid #eee;

        .detailTab {
          padding: 12px 20px;
          font-size: 13px;
          color: #666;
          cursor: pointer;
          border-right: 1px solid #eee;

          &.active {
            color: #c81623;
            border-bottom: 2px solid #c81623;
            font-weight: bold;
          }
        }
      }

      .detailTabs_content {
        padding: 16px;

        .tabPane {
          font-size: 13px;
          color: #333;

          /* 商品介绍 */
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

          .introImages {
            img {
              width: 100%;
              margin-bottom: 8px;
              border-radius: 4px;
            }
          }

          /* 商品评价 */
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
