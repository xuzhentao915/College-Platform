<template>
  <header class="bg-white border-b border-[#e5e7eb] sticky top-0 z-50 h-[60px] flex items-center shadow-sm transition-all duration-300" :class="{ 'shadow-md': isScrolled }">
    <div class="max-w-7xl mx-auto w-full px-4 flex justify-between items-center">
      <!-- Logo -->
      <div class="flex items-center space-x-3 cursor-pointer hover:opacity-90 transition-opacity group" @click="router.push('/home')">
        <div class="relative w-10 h-10 flex items-center justify-center">
          <!-- 装饰性光晕 -->
          <div class="absolute inset-0 bg-blue-500/10 rounded-full blur-md group-hover:bg-blue-500/20 transition-all duration-300"></div>
          <img 
            src="@/assets/images/logo/logo.png" 
            alt="Logo" 
            class="relative z-10 w-full h-full object-contain transform transition-transform duration-300 group-hover:scale-110"
          >
        </div>
        <span class="font-bold text-xl text-slate-800 hidden sm:block tracking-tight">高校综合服务平台</span>
        <span class="font-bold text-lg text-slate-800 sm:hidden">智慧校园</span>
      </div>

      <!-- 桌面端业务导航 (New) -->
      <nav class="hidden lg:flex items-center space-x-8 ml-10 flex-grow">
        <a v-for="nav in navMenus" :key="nav.path" 
           @click="router.push(nav.path)"
           class="text-sm font-medium transition-colors cursor-pointer hover:text-blue-600"
           :class="router.currentRoute.value.path === nav.path ? 'text-blue-600' : 'text-gray-500'">
          {{ nav.name }}
        </a>
      </nav>

      <!-- 功能组件 -->
      <div class="flex items-center space-x-4">
        <!-- 搜索框 - 桌面版 -->
        <div class="hidden md:block w-64 sm:w-80">
          <el-input 
            v-model="searchQuery" 
            placeholder="搜索系统资源..." 
            :prefix-icon="Search" 
            size="default" 
            clearable 
            class="rounded-full border-gray-200 focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
          />
        </div>
        
        <template v-if="isLoggedIn">
          <!-- 通知 -->
          <el-dropdown trigger="click" @click="handleNotificationClick">
            <div class="relative cursor-pointer p-1 rounded-full hover:bg-gray-100 transition-colors">
              <el-icon class="text-gray-500 text-xl hover:text-blue-500 transition-colors"><Bell /></el-icon>
              <span v-if="notificationCount > 0" class="absolute -top-1 -right-1 w-5 h-5 bg-red-500 text-white text-xs rounded-full flex items-center justify-center">
                {{ notificationCount }}
              </span>
            </div>
            <template #dropdown>
              <el-dropdown-menu class="w-80">
                <el-dropdown-item v-for="(item, index) in notifications" :key="index" class="p-3">
                  <div class="flex items-start space-x-3">
                    <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0">
                      <el-icon class="text-blue-500">
                        <component :is="item.icon" />
                      </el-icon>
                    </div>
                    <div class="flex-grow">
                      <div class="text-sm font-medium text-gray-800">{{ item.title }}</div>
                      <div class="text-xs text-gray-500 mt-1">{{ item.content }}</div>
                      <div class="text-xs text-gray-400 mt-1">{{ item.time }}</div>
                    </div>
                  </div>
                </el-dropdown-item>
                <el-dropdown-item divided class="text-center text-blue-500 hover:text-blue-600">
                  查看全部通知
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          
          <!-- 用户菜单 -->
          <el-dropdown trigger="click">
            <div class="flex items-center space-x-2 cursor-pointer outline-none hover:opacity-90 transition-opacity p-1 rounded-full hover:bg-gray-100">
              <el-avatar :size="30" :src="userAvatar" class="border border-gray-200" />
              <span class="text-sm font-medium text-gray-700 hidden sm:block">{{ userName }}</span>
              <el-icon class="text-xs text-gray-400 hidden sm:block"><CaretBottom /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item :icon="User" class="hover:bg-gray-50" @click="router.push('/profile')">个人中心</el-dropdown-item>
                <el-dropdown-item :icon="Tickets" class="hover:bg-gray-50">消息提醒</el-dropdown-item>
                <el-dropdown-item :icon="Setting" class="hover:bg-gray-50">系统设置</el-dropdown-item>
                <el-dropdown-item divided :icon="SwitchButton" @click="handleLogout" class="text-red-500 hover:bg-red-50">退出系统</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        
        <!-- 未登录状态 -->
        <div v-else class="flex items-center space-x-2">
          <el-button type="primary" size="default" round @click="router.push('/login')" class="px-6">
            登录
          </el-button>
        </div>
        
        <!-- 移动端菜单按钮 -->
        <div class="md:hidden">
          <el-button type="text" @click="showMobileMenu = !showMobileMenu">
            <el-icon class="text-gray-500 text-xl"><Menu /></el-icon>
          </el-button>
        </div>
      </div>
    </div>
    
    <!-- 移动端菜单 -->
    <div v-if="showMobileMenu" class="fixed inset-0 bg-black/50 z-40 md:hidden" @click="showMobileMenu = false"></div>
    <div v-if="showMobileMenu" class="fixed top-[60px] right-0 w-64 bg-white shadow-lg z-50 md:hidden transition-all duration-300 transform">
      <div class="p-4 border-b border-gray-100" v-if="isLoggedIn">
        <div class="flex items-center space-x-3 mb-2">
           <el-avatar :size="40" :src="userAvatar" />
           <div>
             <div class="font-bold text-gray-800">{{ userName }}</div>
             <div class="text-xs text-gray-500">欢迎回来</div>
           </div>
        </div>
        <el-input 
          v-model="searchQuery" 
          placeholder="搜索系统资源..." 
          :prefix-icon="Search" 
          size="default" 
          clearable 
        />
      </div>
      <div class="p-4">
        <div class="space-y-4">
          <template v-if="isLoggedIn">
            <div class="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-50 cursor-pointer" @click="router.push('/profile'); showMobileMenu = false">
              <el-icon class="text-gray-600"><User /></el-icon>
              <span class="text-gray-700">个人中心</span>
            </div>
            <div class="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-50 cursor-pointer">
              <el-icon class="text-gray-600"><Tickets /></el-icon>
              <span class="text-gray-700">消息提醒</span>
            </div>
            <div class="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-50 cursor-pointer">
              <el-icon class="text-gray-600"><Setting /></el-icon>
              <span class="text-gray-700">系统设置</span>
            </div>
            <div class="flex items-center space-x-3 p-2 rounded-lg hover:bg-red-50 cursor-pointer text-red-500" @click="handleLogout">
              <el-icon class="text-red-500"><SwitchButton /></el-icon>
              <span class="text-red-500">退出系统</span>
            </div>
          </template>
          <template v-else>
            <div class="flex items-center space-x-3 p-2 rounded-lg hover:bg-blue-50 cursor-pointer text-blue-600" @click="router.push('/login'); showMobileMenu = false">
              <el-icon><User /></el-icon>
              <span class="font-medium">立即登录</span>
            </div>
          </template>

          <!-- 业务快捷入口 (Mobile Only) -->
          <div class="pt-4 mt-4 border-t border-gray-100">
            <p class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mb-3 px-2">业务模块</p>
            <div v-for="nav in navMenus" :key="nav.path" 
                 class="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-50 cursor-pointer" 
                 @click="router.push(nav.path); showMobileMenu = false">
              <el-icon class="text-gray-600"><component :is="nav.icon" /></el-icon>
              <span class="text-gray-700">{{ nav.name }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../store/user';
import { ElMessage } from 'element-plus';
import { 
  Search, Bell, CaretBottom, User, Tickets, 
  SwitchButton, Setting, Message, DataAnalysis, Menu,
  Platform, ShoppingCart, Reading, DataLine, DocumentCopy
} from '@element-plus/icons-vue';

const router = useRouter();
const userStore = useUserStore();

const searchQuery = ref('');
const isScrolled = ref(false);
const notificationCount = ref(3);
const showMobileMenu = ref(false);

const isLoggedIn = computed(() => userStore.isLoggedIn);
const userName = computed(() => userStore.userName);
const userAvatar = computed(() => userStore.userAvatar);

const navMenus = [
  { name: '首页', path: '/home', icon: Platform },
  { name: '二手集市', path: '/market', icon: ShoppingCart },
  { name: '图书管理', path: '/books', icon: Reading },
  { name: '教务成绩', path: '/grades', icon: DataLine },
  { name: '学术期刊', path: '/journal', icon: DocumentCopy }
];

const notifications = ref([
  {
    icon: Message,
    title: '新消息',
    content: '您的图书借阅申请已通过',
    time: '10分钟前'
  },
  {
    icon: DataAnalysis,
    title: '成绩更新',
    content: '高等数学成绩已发布',
    time: '1小时前'
  },
  {
    icon: Bell,
    title: '系统通知',
    content: '图书馆开放时间调整通知',
    time: '昨天'
  }
]);

const handleScroll = () => {
  isScrolled.value = window.scrollY > 10;
};

const handleNotificationClick = () => {
  // 模拟已读通知
  notificationCount.value = 0;
};

const handleLogout = () => {
  userStore.clearSession();
  showMobileMenu.value = false;
  ElMessage({
    message: '您已安全退出系统',
    type: 'info',
    duration: 1000
  });
  router.push('/login');
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<style scoped>
:deep(.el-dropdown-menu) {
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid #f0f0f0;
  overflow: hidden;
}

:deep(.el-dropdown-item) {
  transition: all 0.2s ease;
}

:deep(.el-dropdown-item:hover) {
  background-color: #f9fafb !important;
}

/* 移动端菜单动画 */
@keyframes slideInRight {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}

.fixed.right-0 {
  animation: slideInRight 0.3s ease forwards;
}
</style>