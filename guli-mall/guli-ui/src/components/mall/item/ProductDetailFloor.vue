<template>
  <section class="productDetailFloor">
    <div class="productDetailFloor__inner">
      <!-- 左:相关分类侧栏 -->
      <div class="asideCol">
        <div class="asideCol__tabs">
          <div
            class="asideTab"
            :class="{ active: asideTab === idx }"
            v-for="(tab, idx) in asideTabs"
            :key="tab"
            @click="asideTab = idx"
          >{{ tab }}</div>
        </div>
        <div class="asideCol__content">
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
          <div class="detailTabs__nav">
            <div
              class="detailTab"
              :class="{ active: detailTab === idx }"
              v-for="(tab, idx) in detailTabs"
              :key="tab"
              @click="detailTab = idx"
            >{{ tab }}</div>
          </div>
          <div class="detailTabs__content">
            <!-- 商品介绍 -->
            <div v-show="detailTab === 0" class="tabPane">
              <ul class="specList">
                <li v-for="spec in specList" :key="spec">{{ spec }}</li>
              </ul>
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

const specList = [
  '分辨率：1920*1080(FHD)',
  '后置摄像头：1200万像素',
  '前置摄像头：500万像素',
  '核 数：其他',
  '频 率：以官网信息为准',
  '品牌： Apple',
  '商品名称：APPLEiPhone 6s Plus',
  '商品编号：1861098',
  '商品毛重：0.51kg',
  '商品产地：中国大陆',
  '热点：指纹识别，Apple Pay，金属机身，拍照神器',
  '系统：苹果（IOS）',
  '像素：1000-1600万',
  '机身内存：64GB'
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

<style scoped>
.productDetailFloor { background: Seashell; padding: 10px 0; }
.productDetailFloor__inner {
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
}
.asideCol__tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  border-radius: 6px 6px 0 0;
  overflow: hidden;
}
.asideTab {
  background: #f5f5f5;
  padding: 10px 0;
  text-align: center;
  font-size: 13px;
  color: #666;
  cursor: pointer;
}
.asideTab.active { background: #c81623; color: #fff; }
.asideCol__content {
  background: #fff;
  border-radius: 0 0 6px 6px;
  padding: 10px;
}
.catList {
  list-style: none;
  padding: 0;
  margin: 0 0 10px;
}
.catList li {
  padding: 6px 8px;
  font-size: 12px;
  color: #555;
  border-bottom: 1px dashed #eee;
  cursor: pointer;
}
.catList li:hover { color: #c81623; }
.relatedGoods {
  display: grid;
  gap: 8px;
}

/* ===== 右详情 ===== */
.detailCol {
  display: grid;
  grid-template-rows: auto auto;
  gap: 12px;
}

/* 选择搭配 */
.fittingSection {
  background: #fff;
  border-radius: 6px;
  padding: 12px;
}
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
}
.masterGoods {
  text-align: center;
  position: relative;
}
.masterGoods img { width: 80px; height: 80px; object-fit: contain; }
.masterGoods em { display: block; font-style: normal; color: #c81623; font-size: 14px; margin-top: 4px; }
.masterGoods i {
  position: absolute;
  right: -8px;
  top: 35px;
  font-style: normal;
  font-size: 18px;
  color: #ccc;
  font-weight: bold;
}
.suitsList {
  display: flex;
  gap: 10px;
}
.suitItem {
  text-align: center;
  cursor: pointer;
  padding: 6px;
  border: 2px solid transparent;
  border-radius: 6px;
}
.suitItem.checked { border-color: #c81623; background: rgba(200,22,35,0.05); }
.suitItem img { width: 60px; height: 60px; object-fit: contain; }
.suitItem > i { display: block; font-style: normal; font-size: 11px; color: #555; margin: 4px 0; }
.checkboxPretty {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  font-size: 12px;
  color: #c81623;
}
.checkboxPretty input { margin: 0; }
.fittingResult {
  text-align: center;
  border-left: 1px solid #eee;
  padding-left: 12px;
}
.resultNum { font-size: 12px; color: #999; margin-bottom: 6px; }
.resultLabel { font-size: 13px; color: #333; }
.resultPrice { font-size: 20px; color: #c81623; font-weight: bold; margin: 4px 0 8px; }
.cartBtn {
  background: #c81623;
  color: #fff;
  border: none;
  padding: 6px 16px;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
}

/* 详情 Tab */
.detailTabs {
  background: #fff;
  border-radius: 6px;
  overflow: hidden;
}
.detailTabs__nav {
  display: flex;
  border-bottom: 1px solid #eee;
}
.detailTab {
  padding: 12px 20px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
  border-right: 1px solid #eee;
}
.detailTab.active {
  color: #c81623;
  border-bottom: 2px solid #c81623;
  font-weight: bold;
}
.detailTabs__content { padding: 16px; }
.tabPane { font-size: 13px; color: #333; }

/* 商品介绍 */
.specList {
  list-style: none;
  padding: 0;
  margin: 0 0 16px;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 6px;
}
.specList li {
  padding: 4px 8px;
  background: #f9f9f9;
  border-radius: 4px;
  font-size: 12px;
  color: #555;
}
.introImages img { width: 100%; margin-bottom: 8px; border-radius: 4px; }

/* 商品评价 */
.commentSummary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.commentTitle { font-size: 16px; font-weight: bold; }
.commentPercent { font-size: 14px; color: #666; }
.commentPercent .percent { color: #c81623; font-size: 24px; font-weight: bold; }
.commentTypes {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}
.commentType {
  padding: 4px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 12px;
  color: #555;
  cursor: pointer;
}
.commentType.active { border-color: #c81623; color: #c81623; }
.commentList {
  display: grid;
  gap: 12px;
}
.commentItem {
  display: grid;
  grid-template-columns: 140px 1fr;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}
.commentUser { text-align: center; }
.commentUser img { width: 40px; height: 40px; border-radius: 50%; }
.userName { font-size: 12px; color: #333; margin-top: 4px; }
.userLevel { font-size: 11px; color: #999; }
.commentBody { font-size: 13px; }
.stars { color: #ff9800; font-size: 14px; margin-bottom: 4px; }
.commentBody > p { margin: 4px 0; color: #333; }
.commentMeta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 6px;
}
.metaMini { list-style: none; padding: 0; margin: 0; display: flex; gap: 8px; }
.metaMini li { font-size: 11px; color: #999; }
.commentOps { display: flex; gap: 12px; }
.commentOps span { font-size: 12px; color: #999; }
.reply {
  background: #f9f9f9;
  padding: 8px 12px;
  border-radius: 4px;
  margin-top: 8px;
}
.replyName { color: #c81623; font-weight: bold; }
.replyTime { font-size: 11px; color: #999; margin-top: 4px; }
</style>
