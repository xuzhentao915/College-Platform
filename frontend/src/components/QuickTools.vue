<template>
  <div class="grid grid-cols-3 gap-y-4">
    <div
      v-for="tool in tools"
      :key="tool.name"
      class="flex flex-col items-center cursor-pointer group"
      @click="handleToolClick(tool)"
    >
      <div class="w-12 h-12 bg-gray-50 group-hover:bg-blue-50 rounded-lg flex items-center justify-center transition-all duration-200 mb-2 border border-gray-100 group-hover:border-blue-200">
        <el-icon class="text-xl text-gray-600 group-hover:text-blue-500 transition-colors"><component :is="tool.icon" /></el-icon>
      </div>
      <span class="text-xs text-gray-600 group-hover:text-blue-600 transition-colors">{{ tool.name }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

interface ToolItem {
  name: string;
  icon: any;
  route?: string;
}

defineProps<{
  tools: ToolItem[];
}>();

const router = useRouter();

const handleToolClick = (tool: ToolItem) => {
  if (tool.route) {
    router.push(tool.route);
  }
};
</script>