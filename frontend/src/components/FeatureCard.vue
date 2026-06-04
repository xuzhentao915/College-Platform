<template>
  <div 
    v-for="(item, index) in features" 
    :key="index" 
    class="enterprise-card bg-white p-5 rounded-lg border border-gray-100 shadow-sm hover:shadow-md hover:border-blue-200 cursor-pointer flex items-center transition-all duration-300 group"
    @click="handleFeatureClick(item)"
  >
    <div :class="`w-12 h-12 rounded-lg ${item.bgClass} flex items-center justify-center mr-4 group-hover:scale-110 transition-transform`">
      <el-icon :class="`text-2xl ${item.textColor}`"><component :is="item.icon" /></el-icon>
    </div>
    <div>
      <div class="font-bold text-gray-800 group-hover:text-blue-600 transition-colors">{{ item.title }}</div>
      <div class="text-xs text-gray-400 mt-0.5 group-hover:text-gray-600 transition-colors">{{ item.desc }}</div>
    </div>
    <el-icon class="ml-auto text-gray-300 group-hover:text-blue-400 transition-colors"><ArrowRight /></el-icon>
  </div>
</template>

<script setup lang="ts">
import { ArrowRight } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';

interface FeatureItem {
  title: string;
  desc: string;
  icon: any;
  bgClass: string;
  textColor: string;
  route?: string;
}

const props = defineProps<{
  features: FeatureItem[];
}>();

const router = useRouter();

const handleFeatureClick = (item: FeatureItem) => {
  // 如果有路由，则跳转到对应页面
  if (item.route) {
    router.push(item.route);
  }
};
</script>

<style scoped>
.enterprise-card {
  border-radius: 8px;
}

.enterprise-card:hover {
  transform: translateY(-2px);
}
</style>