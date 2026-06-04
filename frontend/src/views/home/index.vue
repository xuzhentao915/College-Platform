<template>
  <div class="min-h-screen bg-gray-50 flex flex-col font-sans">
    <!-- 引入头部组件 -->
    <Header />

    <!-- 主体内容 -->
    <main class="max-w-7xl mx-auto w-full px-4 py-6 space-y-6">

      <!-- 欢迎信息卡片 (Workbench Card) -->
      <div class="bg-white p-4 sm:p-6 rounded-xl border border-gray-100 shadow-sm flex flex-col sm:flex-row items-center space-y-4 sm:space-y-0 sm:space-x-6 transition-all duration-300 hover:shadow-md">
        <el-avatar :size="64" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" class="border border-gray-200" />
        <div class="flex-grow text-center sm:text-left">
          <h2 class="text-lg sm:text-xl font-bold text-gray-800" v-if="userStore.isLoggedIn">早安，{{ userStore.userName }}同学！祝你今天学习愉快。</h2>
          <h2 class="text-lg sm:text-xl font-bold text-gray-800" v-else>欢迎来到 College Hub！</h2>
          <p class="text-gray-500 text-sm mt-1" v-if="userStore.isLoggedIn">
            软件工程学院 | 2021级 3班 | <span class="text-blue-600 font-medium">优秀学生</span>
          </p>
          <p class="text-gray-500 text-sm mt-1" v-else>
            请登录以使用更多功能
          </p>
        </div>
        <div class="flex flex-col sm:flex-row items-center space-y-4 sm:space-y-0 sm:space-x-4 text-center sm:text-right w-full sm:w-auto">
          <div v-if="userStore.isLoggedIn" class="bg-gray-50 p-3 rounded-lg transition-all duration-300 hover:bg-gray-100 w-full sm:w-auto">
            <div class="text-gray-500 text-xs mb-1">当前绩点</div>
            <div class="text-2xl font-bold text-gray-800">3.85 / 4.0</div>
          </div>
          <div v-if="userStore.isLoggedIn" class="bg-gray-50 p-3 rounded-lg transition-all duration-300 hover:bg-gray-100 w-full sm:w-auto">
            <div class="text-gray-500 text-xs mb-1">待还图书</div>
            <div class="text-2xl font-bold text-red-500">2 本</div>
          </div>
          <el-button v-else type="primary" @click="handleLogin">立即登录</el-button>
        </div>
      </div>

      <!-- 四路金刚位 (Business Grid) -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <FeatureCard :features="features" />
      </div>

      <!-- 分栏内容 -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

        <!-- 左侧：公告与动态 (占 2 份) -->
        <div class="lg:col-span-2 space-y-6">
          <el-card shadow="never" class="border border-gray-100 rounded-xl">
            <template #header>
              <div class="flex justify-between items-center">
                <span class="font-bold"><el-icon class="mr-1 text-blue-600"><Platform /></el-icon> 校园新闻公告</span>
                <el-link type="primary" :underline="false" class="text-xs text-blue-600 hover:text-blue-700 transition-colors">查看更多</el-link>
              </div>
            </template>
            <div v-for="(news, n) in announcements" :key="n"
                 class="flex items-center py-3 border-b border-gray-50 last:border-0 hover:bg-gray-50 px-2 transition-all duration-300 cursor-pointer rounded-md hover:shadow-sm">
              <span class="w-1.5 h-1.5 rounded-full bg-blue-600 mr-3 flex-shrink-0"></span>
              <span class="flex-grow text-gray-600 text-sm">{{ news.content }}</span>
              <span class="text-gray-400 text-xs flex-shrink-0">{{ news.date }}</span>
            </div>
          </el-card>

          <!-- 闲置集市 (表格或简洁列表形式) -->
          <el-card shadow="never" class="border border-gray-100 rounded-xl">
            <template #header>
              <div class="flex justify-between items-center">
                <span class="font-bold"><el-icon class="mr-1 text-orange-500"><ShoppingCart /></el-icon> 二手跳蚤集市</span>
                <el-link type="primary" :underline="false" class="text-xs text-blue-600 hover:text-blue-700 transition-colors" @click="router.push('/market')">浏览全站</el-link>
              </div>
            </template>
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div v-for="(item, i) in goods" :key="i" class="flex flex-col sm:flex-row p-3 border border-gray-100 rounded-lg hover:border-blue-200 transition-all duration-300 cursor-pointer group hover:shadow-sm">
                <div class="w-full sm:w-24 h-24 bg-gray-100 rounded-lg overflow-hidden mr-0 sm:mr-4 flex-shrink-0 mb-3 sm:mb-0">
                  <img :src="item.image" :alt="item.name" class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110">
                </div>
                <div class="flex-grow flex flex-col justify-between overflow-hidden">
                  <div class="font-bold text-sm text-gray-700 truncate group-hover:text-blue-600 transition-colors">{{ item.name }}</div>
                  <div class="text-xs text-gray-500">来自：{{ item.seller }} | 发布于 {{ item.time }}</div>
                  <div class="flex justify-between items-end">
                    <span class="text-red-500 font-bold">{{ item.price }}</span>
                    <el-button type="primary" size="small" plain class="text-blue-600 border-blue-200 hover:bg-blue-50">联系他</el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </div>

        <!-- 右侧：快捷工具与个人状态 -->
        <div class="space-y-6 order-last lg:order-none">
          <el-card shadow="never" header="快捷入口" class="border border-gray-100 rounded-xl">
            <QuickTools :tools="tools" />
          </el-card>

          <el-card shadow="never" class="border border-gray-100 rounded-xl">
             <template #header>
              <div class="flex justify-between items-center">
                <span class="font-bold">图书借阅状态</span>
              </div>
            </template>
            <div class="space-y-4">
              <div v-for="(book, index) in books" :key="index" class="text-xs p-3 bg-gray-50 rounded-lg transition-all duration-300 hover:bg-gray-100">
                <div class="flex justify-between text-gray-700 font-bold mb-1">
                  <span>{{ book.name }}</span>
                  <span :class="book.daysLeft <= 3 ? 'text-red-500' : 'text-orange-500'">剩余 {{ book.daysLeft }} 天</span>
                </div>
                <el-progress
                  :percentage="book.progress"
                  :status="book.daysLeft <= 3 ? 'warning' : ''"
                  class="my-2"
                />
                <div class="text-gray-500 mt-1 flex justify-between">
                  <span>应还日期：{{ book.dueDate }}</span>
                  <el-link type="primary" :underline="false" class="text-xs text-blue-600 hover:text-blue-700">一键续借</el-link>
                </div>
              </div>
            </div>
          </el-card>

          <div class="bg-gradient-to-r from-blue-600 to-indigo-600 rounded-xl p-5 text-white shadow-lg relative overflow-hidden group cursor-pointer transition-all duration-300 hover:shadow-xl">
            <div class="relative z-10">
              <h4 class="font-bold text-lg mb-1">学术期刊库</h4>
              <p class="text-blue-100 text-xs mb-4">全面接入知网、arXiv 数据源</p>
              <el-button size="small" class="bg-white text-blue-600 border-none font-weight-bold hover:bg-blue-50 transition-colors" @click="router.push('/journal')">开始检索</el-button>
            </div>
            <el-icon class="absolute -right-4 -bottom-4 text-8xl opacity-10 group-hover:scale-110 transition-transform duration-500"><DocumentCopy /></el-icon>
          </div>
        </div>

      </div>
    </main>

    <!-- 引入页脚组件 -->
    <Footer />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../../store/user';
import Header from '../../components/Header.vue';
import Footer from '../../components/Footer.vue';
import FeatureCard from '../../components/FeatureCard.vue';
import QuickTools from '../../components/QuickTools.vue';
import {
  ShoppingCart, Reading, DataLine, DocumentCopy,
  Setting, Star, Wallet, MessageBox, Tickets, User, Platform
} from '@element-plus/icons-vue';

// 状态管理
const userStore = useUserStore();
const router = useRouter();

// 处理登录
const handleLogin = () => {
  router.push('/login');
};

// 核心业务配置 (彩色扁平化)
const features = [
  { title: '二手集市', desc: '闲置交易平台', icon: ShoppingCart, bgClass: 'bg-blue-50', textColor: 'text-blue-600', route: '/market' },
  { title: '图书管理', desc: '书籍预约借阅', icon: Reading, bgClass: 'bg-green-50', textColor: 'text-green-600', route: '/books' },
  { title: '教务成绩', desc: '学分绩点统计', icon: DataLine, bgClass: 'bg-purple-50', textColor: 'text-purple-600', route: '/grades' },
  { title: '学术期刊', desc: '论文文献共享', icon: DocumentCopy, bgClass: 'bg-orange-50', textColor: 'text-orange-600', route: '/journal' }
];

const announcements = [
  { date: '2026-03-24', content: '关于调整 2026 年春季图书馆开馆时间的通知' },
  { date: '2026-03-22', content: '校园保卫处：关于开展"防诈骗"专题讲座的预告' },
  { date: '2026-03-20', content: '教务处：本学期公共选修课第二轮补选工作安排' },
  { date: '2026-03-18', content: '关于开展 2026 年大学生创新创业大赛申报的通知' }
];

const tools = [
  { name: '系统设置', icon: Setting, route: '/settings' },
  { name: '我的收藏', icon: Star },
  { name: '电子钱包', icon: Wallet },
  { name: '站内信', icon: MessageBox },
  { name: '日程安排', icon: Tickets },
  { name: '联系辅导员', icon: User }
];

const goods = [
  {
    name: 'Apple Watch 系列 8 (自用回血)',
    price: '￥1680.00',
    seller: '李林同学',
    time: '2小时前',
    image: 'https://images.unsplash.com/photo-1546868871-7041f2a55e12?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80'
  },
  {
    name: '全新未拆封 AirPods Pro 2',
    price: '￥1299.00',
    seller: '王小明同学',
    time: '5小时前',
    image: 'https://images.unsplash.com/photo-1606220588913-b3aacb4d2f46?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80'
  }
];

const books = [
  {
    name: '《JavaScript 高级程序设计》',
    daysLeft: 3,
    dueDate: '2026-03-27',
    progress: 85
  },
  {
    name: '《Spring Cloud 微服务》',
    daysLeft: 7,
    dueDate: '2026-03-31',
    progress: 70
  }
];

// 页面加载动画
onMounted(() => {
  document.body.classList.add('fade-in');
});
</script>

<style scoped>
:deep(.el-card__header) {
  padding: 12px 20px;
  background-color: #f9fafb;
  border-bottom: 1px solid #e5e7eb;
  border-radius: 8px 8px 0 0;
}

:deep(.el-progress-bar__outer) {
  background-color: #f3f4f6;
}

:deep(.el-progress-bar__inner) {
  border-radius: 3px;
}

/* 卡片悬浮效果 - 克制 */
:deep(.el-card) {
  transition: box-shadow 0.2s ease, transform 0.2s ease;
}

:deep(.el-card:hover) {
  transform: translateY(-1px);
}

/* 按钮悬浮效果 - 简洁 */
:deep(.el-button--primary) {
  transition: background-color 0.15s ease, transform 0.15s ease;
}

:deep(.el-button--primary:hover) {
  transform: translateY(-1px);
}

/* 图片悬浮放大 - 流畅 */
:deep(.el-card img) {
  transition: transform 0.4s ease;
}

/* 入口卡片悬浮 */
.feature-card {
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.feature-card:hover {
  border-color: #bfdbfe;
}
</style>
