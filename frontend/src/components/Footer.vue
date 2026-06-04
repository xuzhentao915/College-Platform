<template>
  <footer class="mt-auto py-8 text-center text-gray-400 text-xs border-t border-gray-200 bg-white">
    <div class="max-w-7xl mx-auto w-full px-4">
      <div class="flex flex-col md:flex-row justify-center items-center space-y-4 md:space-y-0 md:space-x-8 mb-6">
        <a href="#" class="hover:text-blue-500 transition-colors">关于我们</a>
        <a href="#" class="hover:text-blue-500 transition-colors">使用帮助</a>
        <a href="#" class="hover:text-blue-500 transition-colors">隐私政策</a>
        <a href="#" class="hover:text-blue-500 transition-colors">联系我们</a>
      </div>
      <div class="flex justify-center items-center space-x-4 mb-4">
        <a href="#" class="text-gray-400 hover:text-blue-500 transition-colors">
          <el-icon class="text-lg"><ChatLineRound /></el-icon>
        </a>
        <a href="#" class="text-gray-400 hover:text-blue-500 transition-colors">
          <el-icon class="text-lg"><Share /></el-icon>
        </a>
        <a href="#" class="text-gray-400 hover:text-blue-500 transition-colors">
          <el-icon class="text-lg"><Link /></el-icon>
        </a>
      </div>
      <p class="text-sm">
        大学生综合服务开放平台 ©2026 技术支持：校园网络中心
      </p>
      <p class="text-xs mt-2">
        备案号：京ICP备12345678号-1
      </p>
    </div>
  </footer>
</template>

<script setup lang="ts">
import { ChatLineRound, Share, Link } from '@element-plus/icons-vue';
</script>

<style scoped>
footer {
  transition: all 0.3s ease;
}

footer:hover {
  border-top-color: #e0e7ff;
}
</style>