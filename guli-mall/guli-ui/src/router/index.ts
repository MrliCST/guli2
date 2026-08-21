import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/index',
      name: 'index',
      component: () => import('@/views/mall/IndexView.vue'),
    },
    {
      path: '/item',
      name: 'item',
      component: () => import('@/views/mall/ItemView.vue'),
    },
    {
      path: '/list',
      name: 'list',
      component: () => import('@/views/mall/ListView.vue'),
    },
  ],
})

export default router
