<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-gray-50">
    <div class="text-center">
      <div class="text-9xl font-bold text-gray-200 mb-6">404</div>
      <h1 class="text-3xl font-bold text-gray-800 mb-4">页面不存在</h1>
      <p class="text-gray-500 mb-8">抱歉，您访问的页面不存在或已被移除</p>
      <el-button type="primary" class="rounded-lg" @click="goHome">
        <el-icon class="mr-2"><ArrowLeft /></el-icon>返回首页
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { ArrowLeft } from '@element-plus/icons-vue';

const router = useRouter();

const goHome = () => {
  router.push('/home');
};
</script>

<style scoped>
/* 404页面样式 */
</style>