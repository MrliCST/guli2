import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/index' },
    { path: '/index', name: 'index', component: () => import('@/views/mall/IndexView.vue') },
    { path: '/item', name: 'item', component: () => import('@/views/mall/ItemView.vue') },
    { path: '/list', name: 'list', component: () => import('@/views/mall/ListView.vue') },
    { path: '/cart', name: 'cart', component: () => import('@/views/cart/CartView.vue') },
    { path: '/add-cart', name: 'addCart', component: () => import('@/views/cart/AddCartView.vue') },
    { path: '/order', name: 'order', component: () => import('@/views/order/OrderView.vue') },
    { path: '/trade', name: 'trade', component: () => import('@/views/order/TradeView.vue') },
    { path: '/pay', name: 'pay', component: () => import('@/views/pay/PayView.vue') },
    { path: '/pay-success', name: 'paySuccess', component: () => import('@/views/pay/PaySuccessView.vue') },
    { path: '/wx-pay', name: 'wxPay', component: () => import('@/views/pay/WxPayView.vue') },
    { path: '/seckill', name: 'seckill', component: () => import('@/views/seckill/SeckillListView.vue') },
    { path: '/seckill-item', name: 'seckillItem', component: () => import('@/views/seckill/SeckillItemView.vue') },
    { path: '/seckill-queue', name: 'seckillQueue', component: () => import('@/views/seckill/SeckillQueueView.vue') },
    { path: '/login', name: 'login', component: () => import('@/views/user/LoginView.vue') },
    { path: '/register', name: 'register', component: () => import('@/views/user/RegisterView.vue') },
  ],
})

export default router
