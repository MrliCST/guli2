<template>
  <section class="seckillQueueFloor">
    <div class="seckillQueueFloor__inner">
      <!-- 状态切换器 -->
      <div class="stateSwitcher">
        <button
          v-for="(s, i) in states"
          :key="i"
          :class="{ active: current === i }"
          @click="current = i"
        >{{ s.label }}</button>
      </div>

      <!-- 排队中 -->
      <div class="queueCard" v-if="current === 0">
        <div class="queueCard__icon spinner"></div>
        <div class="queueCard__text">排队中...</div>
        <div class="queueCard__sub">系统正在处理您的抢购请求，请稍候</div>
      </div>

      <!-- 抢购失败 -->
      <div class="queueCard" v-else-if="current === 1">
        <div class="queueCard__icon fail">✕</div>
        <div class="queueCard__text">抢购失败</div>
        <div class="queueCard__sub">商品已抢完，下次早点来哦</div>
        <a href="/seckill" class="queueCard__btn">返回秒杀列表</a>
      </div>

      <!-- 抢购成功 - 去下单 -->
      <div class="queueCard" v-else-if="current === 2">
        <div class="queueCard__icon success">✓</div>
        <div class="queueCard__text">抢购成功</div>
        <a href="/seckill-item" class="queueCard__btn">去下单</a>
      </div>

      <!-- 抢购成功 - 我的订单 -->
      <div class="queueCard" v-else-if="current === 3">
        <div class="queueCard__icon success">✓</div>
        <div class="queueCard__text">抢购成功</div>
        <a href="/" class="queueCard__btn">我的订单</a>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const current = ref(0)

const states = [
  { label: '排队中' },
  { label: '抢购失败' },
  { label: '抢购成功·去下单' },
  { label: '抢购成功·我的订单' }
]
</script>

<style scoped>
.seckillQueueFloor { background: #fff5f5; padding: 40px 0; }
.seckillQueueFloor__inner {
  width: var(--page-width);
  margin: 0 auto;
  display: grid;
  grid-template-rows: auto 1fr;
  gap: 30px;
  min-height: 400px;
}

/* 状态切换器 */
.stateSwitcher {
  display: flex;
  gap: 8px;
  justify-content: center;
}
.stateSwitcher button {
  padding: 8px 16px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
}
.stateSwitcher button.active {
  background: var(--theme-red);
  color: #fff;
  border-color: var(--theme-red);
}

/* 状态卡片 */
.queueCard {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  background: #fff;
  border-radius: 8px;
  padding: 60px 20px;
}
.queueCard__icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: bold;
}
.spinner {
  border: 4px solid #ffe0e0;
  border-top-color: var(--theme-red);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }
.fail { background: #fff0f0; color: var(--theme-red); }
.success { background: #f0fff0; color: #4CAF50; }
.queueCard__text { font-size: 22px; font-weight: bold; color: #333; }
.queueCard__sub { font-size: 14px; color: #999; }
.queueCard__btn {
  background: var(--theme-red);
  color: #fff;
  padding: 10px 30px;
  border-radius: 4px;
  font-size: 14px;
  text-decoration: none;
}
.queueCard__btn:hover { opacity: 0.9; }
</style>
