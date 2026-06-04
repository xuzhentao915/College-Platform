<template>
  <!-- 现代登录背景 -->
  <div class="h-screen w-full flex items-center justify-center bg-gradient-to-br from-blue-600 to-indigo-800 relative selection:bg-white/30 overflow-hidden">
    <!-- 背景几何纹理 -->
    <div class="absolute inset-0 opacity-10 pointer-events-none">
      <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <pattern id="grid" width="60" height="60" patternUnits="userSpaceOnUse">
            <path d="M 60 0 L 0 0 0 60" fill="none" stroke="white" stroke-width="1"/>
          </pattern>
        </defs>
        <rect width="100%" height="100%" fill="url(#grid)" />
      </svg>
    </div>

    <!-- 装饰元素 -->
    <div class="absolute top-1/4 left-1/4 w-64 h-64 bg-white/10 rounded-full blur-3xl"></div>
    <div class="absolute bottom-1/4 right-1/4 w-80 h-80 bg-white/10 rounded-full blur-3xl"></div>

    <!-- 登录容器 -->
    <div class="w-full max-w-[420px] bg-white rounded-xl shadow-2xl p-8 z-10 transform transition-all duration-500 hover:shadow-3xl">
      <div class="mb-8 text-center flex flex-col items-center">
        <div class="w-24 h-24 mb-6 relative group flex items-center justify-center">
          <!-- 磨砂玻璃底层装饰 -->
          <div class="absolute inset-0 bg-blue-500/5 rounded-full blur-2xl group-hover:bg-blue-500/10 transition-colors duration-500"></div>
          
          <img 
            src="@/assets/images/logo/logo.png" 
            alt="Logo" 
            class="relative z-10 w-full h-full object-contain transform group-hover:scale-110 transition-all duration-500 drop-shadow-lg"
          >
        </div>
        <h2 class="text-2xl font-bold text-gray-800 tracking-tight">大学生综合服务平台</h2>
        <p class="text-gray-500 text-sm mt-2 font-medium tracking-widest uppercase opacity-80">University Hub Platform</p>
      </div>

      <!-- 现代登录表单 -->
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" size="large" @submit.prevent>
        <el-form-item prop="studentId">
          <el-input 
            v-model="loginForm.studentId" 
            placeholder="请输入学号/工号" 
            :prefix-icon="User"
            class="rounded-lg border-gray-200 focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all duration-300"
          >
          </el-input>
        </el-form-item>
        
        <el-form-item prop="password" class="mt-4">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入登录密码" 
            show-password 
            :prefix-icon="Lock"
            @keyup.enter="handleLogin"
            class="rounded-lg border-gray-200 focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all duration-300"
          >
          </el-input>
        </el-form-item>
        
        <div class="flex items-center justify-between mt-4 mb-6">
          <el-checkbox v-model="rememberMe" class="text-gray-600">记住密码</el-checkbox>
          <el-link type="primary" :underline="false" class="text-blue-600 hover:text-blue-700 transition-colors">忘记密码？</el-link>
        </div>

        <el-form-item class="mb-0">
          <el-button 
            type="primary" 
            class="w-full h-12 text-base font-semibold tracking-wider rounded-lg"
            :loading="loading" 
            @click="handleLogin"
            :class="{'animate-pulse': loading}"
          >
            立即登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="mt-6 pt-6 border-t border-gray-100 text-center">
        <span class="text-gray-500 text-sm">还没有账号？</span>
        <el-link type="primary" :underline="false" class="ml-2 font-bold text-blue-600 hover:text-blue-700 transition-colors">申请入驻</el-link>
      </div>

      <!-- 第三方登录 -->
      <div class="mt-6 pt-6 border-t border-gray-100">
        <p class="text-center text-gray-500 text-sm mb-4">其他登录方式</p>
        <div class="flex justify-center space-x-6">
          <div class="w-10 h-10 bg-gray-100 rounded-full flex items-center justify-center cursor-pointer hover:bg-gray-200 transition-colors">
            <el-icon class="text-gray-600"><ChatLineRound /></el-icon>
          </div>
          <div class="w-10 h-10 bg-gray-100 rounded-full flex items-center justify-center cursor-pointer hover:bg-gray-200 transition-colors">
            <el-icon class="text-gray-600"><Link /></el-icon>
          </div>
          <div class="w-10 h-10 bg-gray-100 rounded-full flex items-center justify-center cursor-pointer hover:bg-gray-200 transition-colors">
            <el-icon class="text-gray-600"><Share /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部版权信息 -->
    <div class="absolute bottom-6 text-white/60 text-xs tracking-widest uppercase">
      &copy; 2026 UNIVERSITY OPEN PLATFORM &bull; SYSTEM INFRASTRUCTURE
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { User, Lock, ChatLineRound, Link, Share } from '@element-plus/icons-vue';
import { useUserStore } from '../../store/user';

const router = useRouter();
const userStore = useUserStore();

const loginFormRef = ref();
const loading = ref(false);
const rememberMe = ref(true);

const loginForm = reactive({
  studentId: '',
  password: ''
});

const rules = {
  studentId: [{ required: true, message: '请输入学号/工号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入登录密码', trigger: 'blur' }]
};

const handleLogin = () => {
  loginFormRef.value.validate((valid: boolean) => {
    if (valid) {
      loading.value = true;
      setTimeout(() => {
        userStore.setToken('enterprise_token_demo_2026'); 
        ElMessage({
          message: '身份验证成功，欢迎登录',
          type: 'success',
          duration: 1500
        });
        router.push('/home');
        loading.value = false;
      }, 1000);
    }
  });
};
</script>

<style scoped>
:deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #e5e7eb inset !important;
  border-radius: 8px !important;
  transition: all 0.3s ease !important;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #2563eb inset, 0 0 0 3px rgba(37, 99, 235, 0.1) !important;
}

:deep(.el-button--primary) {
  background-color: #2563eb !important;
  border-color: #2563eb !important;
  transition: all 0.3s ease !important;
}

:deep(.el-button--primary:hover) {
  background-color: #1d4ed8 !important;
  border-color: #1d4ed8 !important;
  transform: translateY(-1px) !important;
  box-shadow: 0 4px 6px -1px rgba(37, 99, 235, 0.3) !important;
}

:deep(.el-checkbox__label) {
  transition: all 0.2s ease !important;
}

:deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #2563eb !important;
  border-color: #2563eb !important;
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-card {
  animation: fadeInUp 0.5s ease forwards;
}
</style>