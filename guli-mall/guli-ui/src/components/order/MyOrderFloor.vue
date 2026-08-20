<template>
  <div class="myOrderFloor">
    <!-- 标题 -->
    <div class="myOrderFloor__title">
      <strong>我的订单</strong>
    </div>

    <!-- 表头 -->
    <div class="orderHeader">
      <div class="col col-goods">商品</div>
      <div class="col col-detail">订单详情</div>
      <div class="col col-receiver">收货人</div>
      <div class="col col-amount">金额</div>
      <div class="col col-status">状态</div>
      <div class="col col-op">操作</div>
    </div>

    <!-- 订单列表 -->
    <div class="orderList">
      <div class="orderBlock" v-for="(order, i) in orders" :key="i">
        <!-- 订单头 -->
        <div class="orderBlock__header">
          <span class="orderTime">{{ order.date }} 订单编号：{{ order.num }}</span>
          <span class="deleteBtn" @click="deleteOrder(i)">删除</span>
        </div>
        <!-- 订单体 -->
        <div class="orderBlock__body">
          <div class="col-goods">
            <div class="goodsRow" v-for="(g, gi) in order.goods" :key="gi">
              <img :src="g.img" alt="" />
              <a href="#" class="goodsName">{{ g.name }}</a>
              <span class="goodsQty">x{{ g.qty }}</span>
              <ul class="goodsAfter"><li>申请售后</li></ul>
            </div>
          </div>
          <div class="col-receiver">{{ order.receiver }}</div>
          <div class="col-amount">
            <div>总金额¥{{ order.total }}</div>
            <div>{{ order.payType }}</div>
          </div>
          <div class="col-status"><span class="statusTag">{{ order.status }}</span></div>
          <div class="col-op"><a href="#" class="opLink">评价|晒单</a></div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <button class="pageBtn disabled">«上一页</button>
      <button class="pageBtn active">1</button>
      <button class="pageBtn">2</button>
      <button class="pageBtn">下一页»</button>
      <span class="pageInfo">共2页</span>
    </div>

    <!-- 猜你喜欢 -->
    <div class="likeSection">
      <div class="likeSection__title"><strong>猜你喜欢</strong></div>
      <div class="likeSection__list">
        <BaseGoodsCard
          v-for="(g, i) in likeGoods"
          :key="i"
          :img="g.img"
          :price="g.price"
          :title="g.title"
          :show-operate="true"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import BaseGoodsCard from '../common/BaseGoodsCard.vue'

const orders = ref([
  {
    date: '2017-02-11 11:59', num: '7867473872181848', receiver: '小丽', total: '138.00', payType: '在线支付', status: '已完成',
    goods: [
      { img: '/img/goods.png', name: '包邮 正品玛姬儿压缩面膜无纺布纸膜100粒 送泡瓶面膜刷喷瓶 新款', qty: 1 },
      { img: '/img/goods.png', name: '包邮 正品玛姬儿压缩面膜无纺布纸膜100粒 送泡瓶面膜刷喷瓶 新款', qty: 1 }
    ]
  },
  {
    date: '2017-03-11 12:59', num: '7867473872555', receiver: '小丽', total: '138.00', payType: '在线支付', status: '已完成',
    goods: [
      { img: '/img/goods.png', name: '包邮 正品玛姬儿压缩面膜无纺布纸膜100粒 送泡瓶面膜刷喷瓶 新款', qty: 1 },
      { img: '/img/goods.png', name: '包邮 正品玛姬儿压缩面膜无纺布纸膜100粒 送泡瓶面膜刷喷瓶 新款', qty: 1 }
    ]
  }
])

const likeGoods = ref([
  { img: '/img/_/itemlike01.png', price: '3699.00', title: 'DELL戴尔Ins 15MR-7528SS 15英寸 银色 笔记本' },
  { img: '/img/_/itemlike02.png', price: '4299.00', title: 'DELL戴尔Ins 15MR-7528SS 15英寸 银色 笔记本' },
  { img: '/img/_/itemlike03.png', price: '5299.00', title: 'DELL戴尔Ins 15MR-7528SS 15英寸 银色 笔记本' },
  { img: '/img/_/itemlike04.png', price: '4399.00', title: 'DELL戴尔Ins 15MR-7528SS 15英寸 银色 笔记本' }
])

function deleteOrder(idx) {
  orders.value.splice(idx, 1)
}
</script>

<style scoped>
.myOrderFloor { display: grid; gap: 0; }
.myOrderFloor__title { padding: 12px 0; font-size: 16px; color: #333; }

/* 表头 */
.orderHeader {
  display: grid;
  grid-template-columns: 29% 31% 8% 13% 1fr 1fr;
  background: #f5f5f5;
  padding: 10px 0;
  font-size: 13px;
  color: #666;
  text-align: center;
  border-radius: 4px;
}
.col-goods { text-align: left; padding-left: 16px; }

/* 订单块 */
.orderList { display: grid; gap: 12px; margin-top: 12px; }
.orderBlock {
  background: #fff;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #f0f0f0;
}
.orderBlock__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fafafa;
  padding: 10px 16px;
  font-size: 12px;
  color: #666;
}
.orderTime { color: #999; }
.deleteBtn { cursor: pointer; color: #999; }
.deleteBtn:hover { color: var(--theme-red); }

.orderBlock__body {
  display: grid;
  grid-template-columns: 29% 31% 8% 13% 1fr 1fr;
  align-items: center;
}
.goodsRow {
  display: grid;
  grid-template-columns: 60px 1fr auto;
  gap: 8px;
  align-items: center;
  padding: 10px 16px;
  border-bottom: 1px solid #f5f5f5;
}
.goodsRow:last-child { border-bottom: none; }
.goodsRow img { width: 50px; height: 50px; object-fit: cover; border-radius: 4px; }
.goodsName { font-size: 12px; color: #333; text-decoration: none; line-height: 1.4; }
.goodsName:hover { color: var(--theme-red); }
.goodsQty { font-size: 12px; color: #999; }
.goodsAfter { list-style: none; margin: 0; padding: 0; grid-column: 2; }
.goodsAfter li { font-size: 11px; color: #999; cursor: pointer; }

.col-receiver, .col-amount, .col-status, .col-op {
  text-align: center;
  font-size: 12px;
  color: #666;
  padding: 8px;
}
.col-amount div { margin: 2px 0; }
.statusTag { color: #4CAF50; font-weight: bold; }
.opLink { font-size: 12px; color: #666; text-decoration: none; }
.opLink:hover { color: var(--theme-red); }

/* 分页 */
.pagination {
  display: flex;
  gap: 4px;
  justify-content: center;
  align-items: center;
  padding: 20px 0;
}
.pageBtn {
  padding: 6px 12px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  font-size: 12px;
  color: #666;
  cursor: pointer;
}
.pageBtn.active { background: var(--theme-red); color: #fff; border-color: var(--theme-red); }
.pageBtn.disabled { color: #ccc; cursor: not-allowed; }
.pageInfo { font-size: 12px; color: #999; margin-left: 8px; }

/* 猜你喜欢 */
.likeSection { margin-top: 20px; }
.likeSection__title { padding: 12px 0; font-size: 16px; color: #333; }
.likeSection__list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}
</style>
