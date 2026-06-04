import { createRouter, createWebHistory } from 'vue-router';
import type { RouteRecordRaw } from 'vue-router';
import { useUserStore } from '../store/user';

// 配置强类型路由
const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/index.vue'),
    meta: { 
      title: '授权空间 | College Hub',
      requiresAuth: false
    }
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/home/index.vue'),
    meta: { 
      title: '主轴看板 | College Hub',
      requiresAuth: false
    }
  },
  // 新增路由
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/profile/index.vue'),
    meta: { 
      title: '个人中心 | College Hub',
      requiresAuth: true
    }
  },
  {
    path: '/books',
    name: 'Books',
    component: () => import('../views/books/index.vue'),
    meta: { 
      title: '图书管理 | College Hub',
      requiresAuth: false
    }
  },
  {
    path: '/grades',
    name: 'Grades',
    component: () => import('../views/grades/index.vue'),
    meta: { 
      title: '教务成绩 | College Hub',
      requiresAuth: false
    }
  },
  {
    path: '/market',
    name: 'Market',
    component: () => import('../views/market/index.vue'),
    meta: { 
      title: '二手集市 | College Hub',
      requiresAuth: false
    }
  },
  {
    path: '/journal',
    name: 'Journal',
    component: () => import('../views/journal/index.vue'),
    meta: {
      title: '学术期刊 | College Hub',
      requiresAuth: false
    }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('../views/settings/index.vue'),
    meta: {
      title: '系统设置 | College Hub',
      requiresAuth: true
    }
  },
  // 404路由
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/404/index.vue'),
    meta: { 
      title: '页面不存在 | College Hub',
      requiresAuth: false
    }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(_to, _from, savedPosition) {
    if (savedPosition) return savedPosition;
    return { top: 0, behavior: 'smooth' };
  }
});

// 全局前置守卫
router.beforeEach((to, _from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title as string;
  }

  // 路由守卫
  const userStore = useUserStore();
  const requiresAuth = to.meta.requiresAuth !== false; // 默认需要认证

  if (requiresAuth && !userStore.isLoggedIn) {
    // 未登录，重定向到登录页
    next('/login');
  } else if (to.path === '/login' && userStore.isLoggedIn) {
    // 已登录，重定向到首页
    next('/home');
  } else {
    next();
  }
});

// 全局后置守卫
router.afterEach((to, from) => {
  // 可以在这里添加页面统计等逻辑
  console.log(`路由从 ${from.path} 跳转到 ${to.path}`);
});

export default router;
