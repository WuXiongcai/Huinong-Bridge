import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const routes = [{
        path: '/',
        name: 'Manager',
        component: () =>
            import ('../views/Manager.vue'),
        redirect: '/login', // 重定向到主页
        children: [{
                path: '403',
                name: 'NoAuth',
                meta: { name: '无权限' },
                component: () =>
                    import ('../views/manager/403')
            },
            {
                path: 'home',
                name: 'Home',
                meta: { name: '系统首页' },
                component: () =>
                    import ('../views/manager/Home')
            },
            {
                path: 'admin',
                name: 'Admin',
                meta: { name: '管理员信息' },
                component: () =>
                    import ('../views/manager/Admin')
            },
            {
                path: 'user',
                name: 'User',
                meta: { name: '用户信息' },
                component: () =>
                    import ('../views/manager/User')
            },
            {
                path: 'adminPerson',
                name: 'AdminPerson',
                meta: { name: '个人信息' },
                component: () =>
                    import ('../views/manager/AdminPerson')
            },
            {
                path: 'password',
                name: 'Password',
                meta: { name: '修改密码' },
                component: () =>
                    import ('../views/manager/Password')
            },
            {
                path: 'notice',
                name: 'Notice',
                meta: { name: '公告信息' },
                component: () =>
                    import ('../views/manager/Notice')
            },
            {
                path: 'information',
                name: 'Information',
                meta: { name: '助农资讯' },
                component: () =>
                    import ('../views/manager/Information')
            },
            {
                path: 'policy',
                name: 'Policy',
                meta: { name: '助农政策' },
                component: () =>
                    import ('../views/manager/Policy')
            },
            {
                path: 'agritainment',
                name: 'Agritainment',
                meta: { name: '农家乐信息' },
                component: () =>
                    import ('../views/manager/Agritainment')
            },
            {
                path: 'goods',
                name: 'Goods',
                meta: { name: '扶贫商品' },
                component: () =>
                    import ('../views/manager/Goods')
            },
            {
                path: 'orders',
                name: 'Orders',
                meta: { name: '订单信息' },
                component: () =>
                    import ('../views/manager/Orders')
            },
            {
                path: 'poorApply',
                name: 'PoorApply',
                meta: { name: '贫困户申请' },
                component: () =>
                    import ('../views/manager/PoorApply')
            },
            {
                path: 'products',
                name: 'Products',
                meta: { name: '农产品列表' },
                component: () =>
                    import ('../views/manager/ProductManagement')
            },
            {
                path: 'productOrders',
                name: 'ProductOrders',
                meta: { name: '农产品订单管理' },
                component: () =>
                    import ('../views/manager/ProductOrders')
            },
            {
                path: 'sight',
                name: 'SightManager',
                meta: { name: '龙门景点推荐管理' },
                component: () =>
                    import ('../views/manager/SightManager')
            },
            {
                path: 'sightcomment',
                name: 'SightComment',
                meta: { name: '景点评论管理' },
                component: () =>
                    import ('../views/manager/SightComment')
            },
            {
                path: 'projectApply',
                name: 'ProjectApply',
                meta: { name: '扶贫项目申请' },
                component: () =>
                    import ('../views/manager/ProjectApply')
            },
            {
                path: 'stories',
                name: 'StoriesManager',
                meta: { name: '乡村文化传播' },
                component: () =>
                    import ('../views/manager/StoriesManager.vue')
            },
            {
                path: 'comment',
                name: 'Comment',
                meta: { name: '政策评论' },
                component: () =>
                    import ('../views/manager/Comment')
            },
            {
                path: 'message',
                name: 'Message',
                meta: { name: '留言信息管理' },
                component: () =>
                    import ('../views/manager/Message')
            },
            {
                path: 'videos',
                name: 'VideosManager',
                meta: { name: '扶贫助农视频管理' },
                component: () =>
                    import ('../views/manager/VideoManager')
            },
            {
                path: 'folk',
                name: 'FolkManager',
                meta: { name: '民俗文化管理' },
                component: () =>
                    import ('../views/manager/FolkManager')
            },
            {
                path: 'heritage',
                name: 'HeritageManager',
                meta: { name: '非遗文化名录管理' },
                component: () =>
                    import ('../views/manager/HeritageManager')
            },
            {
                path: 'food',
                name: 'FoodManager',
                meta: { name: '特色美食管理' },
                component: () =>
                    import ('../views/manager/FoodManager')
            },
            {
                path: 'handicraft',
                name: 'HandicraftManager',
                meta: { name: '传统手工艺管理' },
                component: () =>
                    import ('../views/manager/HandicraftManager')
            },
            {
                path: 'funnyStories',
                name: 'FunnyStoriesManager',
                meta: { name: '农村趣事分享' },
                component: () =>
                    import ('../views/manager/FunnyStoriesManager')
            },
            {
                path: 'farmlist',
                name: 'FarmList',
                meta: { name: '农家乐榜单' },
                component: () =>
                    import ('../views/manager/FarmList')
            },
        ]
    },
    {
        path: '/front',
        name: 'Front',
        component: () =>
            import ('../views/Front.vue'),
        children: [{
                path: 'home',
                name: 'Home',
                meta: { name: '系统首页' },
                component: () =>
                    import ('../views/front/Home')
            },
            {
                path: 'about',
                name: 'About',
                meta: { name: '关于我们' },
                component: () =>
                    import ('../views/front/About')
            },
            {
                path: 'person',
                name: 'Person',
                meta: { name: '个人信息' },
                component: () =>
                    import ('../views/front/Person')
            },
            {
                path: 'stories',
                name: 'Stories',
                meta: { name: '乡村文化传播' },
                component: () =>
                    import ('../views/front/Stories')
            },
            {
                path: 'informationDetail',
                name: 'InformationDetail',
                meta: { name: '资讯详情' },
                component: () =>
                    import ('../views/front/InformationDetail')
            },
            {
                path: 'policyDetail',
                name: 'PolicyDetail',
                meta: { name: '政策详情' },
                component: () =>
                    import ('../views/front/PolicyDetail')
            },
            {
                path: 'agritainmentDetail',
                name: 'AgritainmentDetail',
                meta: { name: '农家乐详情' },
                component: () =>
                    import ('../views/front/AgritainmentDetail')
            },
            {
                path: 'orders',
                name: 'Orders',
                meta: { name: '订单信息' },
                component: () =>
                    import ('../views/front/Orders')
            },
            {
                path: 'products',
                name: 'Products',
                meta: { name: '农产品购买' },
                component: () =>
                    import ('../views/front/Products')
            },
            {
                path: 'sight',
                name: 'Sight',
                meta: { name: '龙门景点推荐' },
                component: () =>
                    import ('../views/front/Sight')
            },
            {
                path: 'apply',
                name: 'Apply',
                meta: { name: '我的申请' },
                component: () =>
                    import ('../views/front/Apply')
            },
            {
                path: 'praise',
                name: 'Praise',
                meta: { name: '历史点赞' },
                component: () =>
                    import ('../views/front/Praise')
            },
        ]
    },
    {
        path: '/login',
        name: 'Login',
        meta: { name: '登录' },
        component: () =>
            import ('../views/Login.vue')
    },
    {
        path: '/register',
        name: 'Register',
        meta: { name: '注册' },
        component: () =>
            import ('../views/Register.vue')
    },
    {
        path: '/forgot-password',
        name: 'ForgotPassword',
        meta: { name: '忘记密码' },
        component: () =>
            import ('../views/ForgotPassword.vue')
    },
    {
        path: '*',
        name: 'NotFound',
        meta: { name: '无法访问' },
        component: () =>
            import ('../views/404.vue')
    },
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

// 注：不需要前台的项目，可以注释掉该路由守卫
// 路由守卫
router.beforeEach((to, from, next) => {
    let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
    let admin = JSON.parse(localStorage.getItem("xm-admin") || '{}');
    if (to.path === '/') {
        if (admin.id) {
            next('/home')
        } else if (user.id) {
            next('/front/home')
        } else {
            next('/login')
        }
    } else {
        next()
    }
})

export default router