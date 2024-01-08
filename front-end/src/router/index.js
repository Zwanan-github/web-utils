import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
    },
    {
        path: '/paste/:name',
        name: 'PasteDetail',
        component: () => import('@/views/Paste/Detail.vue')
    },
    {
        path: '/paste',
        name: 'Paste',
        component: () => import('@/views/paste/Home.vue'),
    },
    {
        path: '/code',
        name: 'Code',
        component: () => import('@/views/code/Code.vue')
    }
]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

export default router
