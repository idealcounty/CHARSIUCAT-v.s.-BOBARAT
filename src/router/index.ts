import { createRouter, createWebHashHistory } from "vue-router"

const router = createRouter({
    history: createWebHashHistory(),
    routes: [{
        path: '/',
        component: () => import('../views/store/Store.vue'),
    }, {
        path: '/product/:product_id',
        name: 'detail',
        component: () => import('../views/store/Detail.vue'),
    }, {
        path: '/404',
        name: '404',
        component: () => import('../views/NotFound.vue'),
        meta: { title: '404' }
    }, {
        path: '/register',
        component: () => import('../views/user/Register.vue'),
        meta: { title: '用户注册' }
    }, {
        path: '/login',
        component: () => import('../views/user/Login.vue'),
        meta: { title: '用户登录' }
    }, {
        path: '/admin',
        component: () => import('../views/admin/Administrator.vue'),
        meta: { title: '管理商品' },
    }, {
        path: '/profile/:user_id',
        name: 'profile',
        component: () => import('../views/user/Profile.vue'),
    }, {
        path: '/createProduct',
        component: () => import('../views/admin/CreateProduct.vue'),
        meta: { title: '创建商品' }
    }, {
        path: '/updateProduct/:product_id',
        component: () => import('../views/admin/UpdateProduct.vue'),
        meta: { title: '更新商品信息' }
    }, {
        path: '/createAdvertisement',
        component: () => import('../views/admin/CreateAdvertisement.vue'),
        meta: { title: '创建商品' }
    }, {
        path: '/updateAdvertisement/:advertisement_id',
        name: 'updateAdvertisement',
        component: () => import('../views/admin/UpdateAdvertisement.vue'),
        meta: { title: '更新广告信息' }
    }, {
        path: '/wishlist',
        component: () => import('../views/user/WishList.vue'),
        meta: { title: '创建商品' }
    }, {
        path: '/cart',
        component: () => import('../views/cart/Cart.vue'),
        meta: { title: '购物车' }
    }, {
        path: '/games',
        component: () => import('../views/user/Game.vue'),
        meta: { title: '游戏' }
    }, {
        path: '/:catchAll(.*)',
        redirect: '/404'
    },]
})

router.beforeEach((to, _, next) => {
    const token: string | null = sessionStorage.getItem('token');
    const role: string | null = sessionStorage.getItem('role')

    if (to.meta.title) {
        document.title = to.meta.title
    }

    if (token) {
        if (to.meta.permission) {
            if (to.meta.permission.includes(role!)) {
                next()
            } else {
                next('/404')
            }
        } else {
            next()
        }
    } else {
        if (to.path === '/login') {
            next();
        } else if (to.path === '/register') {
            next()
        } else {
            next('/login')
        }
    }
})

export { router }
