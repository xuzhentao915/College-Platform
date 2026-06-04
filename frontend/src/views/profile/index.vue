<template>
  <div class="min-h-screen bg-gray-50 flex flex-col">
    <!-- 引入头部组件 -->
    <Header />

    <!-- 主体内容 -->
    <main class="max-w-7xl mx-auto w-full px-4 py-6 space-y-6">
      <h1 class="text-2xl font-bold text-gray-800">个人中心</h1>
      
      <!-- 个人信息卡片 -->
      <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-6 transition-all duration-300 hover:shadow-md">
        <div class="flex items-center space-x-6">
          <el-avatar :size="100" :src="userStore.userAvatar" class="border-4 border-gray-100" />
          <div class="flex-grow">
            <h2 class="text-xl font-bold text-gray-800">{{ userStore.userInfo?.name }}</h2>
            <p class="text-gray-500 text-sm mt-1">{{ userStore.userInfo?.studentId }}</p>
            <div class="flex flex-wrap gap-2 mt-3">
              <span class="px-3 py-1 bg-blue-50 text-blue-600 rounded-full text-xs font-medium">{{ userStore.userInfo?.college }}</span>
              <span class="px-3 py-1 bg-green-50 text-green-600 rounded-full text-xs font-medium">{{ userStore.userInfo?.major }}</span>
              <span class="px-3 py-1 bg-purple-50 text-purple-600 rounded-full text-xs font-medium">{{ userStore.userInfo?.class }}</span>
              <span v-if="userStore.userInfo?.isExcellent" class="px-3 py-1 bg-yellow-50 text-yellow-600 rounded-full text-xs font-medium">优秀学生</span>
            </div>
          </div>
          <el-button type="primary" plain class="rounded-lg">
            <el-icon class="mr-2"><Edit /></el-icon>编辑资料
          </el-button>
        </div>
      </div>

      <!-- 个人信息表单 -->
      <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-6 transition-all duration-300 hover:shadow-md">
        <h3 class="text-lg font-semibold text-gray-800 mb-4">基本信息</h3>
        <el-form :model="userForm" label-width="120px" size="medium">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="姓名">
                <el-input v-model="userForm.name" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="学号">
                <el-input v-model="userForm.studentId" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="学院">
                <el-input v-model="userForm.college" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="专业">
                <el-input v-model="userForm.major" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="班级">
                <el-input v-model="userForm.class" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="角色">
                <el-input v-model="userForm.role" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="联系电话">
                <el-input v-model="userForm.phone" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="电子邮箱">
                <el-input v-model="userForm.email" />
              </el-form-item>
            </el-col>
          </el-row>
          <div class="flex justify-end mt-4">
            <el-button class="mr-2" @click="resetForm">重置</el-button>
            <el-button type="primary" @click="saveForm">保存</el-button>
          </div>
        </el-form>
      </div>

      <!-- 账户安全 -->
      <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-6 transition-all duration-300 hover:shadow-md">
        <h3 class="text-lg font-semibold text-gray-800 mb-4">账户安全</h3>
        <div class="space-y-4">
          <div class="flex items-center justify-between p-4 border-b border-gray-100 last:border-0">
            <div>
              <div class="font-medium text-gray-800">修改密码</div>
              <div class="text-sm text-gray-500 mt-1">定期修改密码可以提高账户安全性</div>
            </div>
            <el-button type="primary" plain class="rounded-lg">修改</el-button>
          </div>
          <div class="flex items-center justify-between p-4 border-b border-gray-100 last:border-0">
            <div>
              <div class="font-medium text-gray-800">绑定手机</div>
              <div class="text-sm text-gray-500 mt-1">绑定手机可以接收重要通知</div>
            </div>
            <el-button type="primary" plain class="rounded-lg">绑定</el-button>
          </div>
          <div class="flex items-center justify-between p-4 border-b border-gray-100 last:border-0">
            <div>
              <div class="font-medium text-gray-800">绑定邮箱</div>
              <div class="text-sm text-gray-500 mt-1">绑定邮箱可以找回密码</div>
            </div>
            <el-button type="primary" plain class="rounded-lg">绑定</el-button>
          </div>
        </div>
      </div>
    </main>
    
    <!-- 引入页脚组件 -->
    <Footer />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import Header from '../../components/Header.vue';
import Footer from '../../components/Footer.vue';
import { useUserStore } from '../../store/user';
import { ElMessage } from 'element-plus';
import { Edit } from '@element-plus/icons-vue';

const userStore = useUserStore();

const userForm = ref({
  name: userStore.userInfo?.name || '',
  studentId: userStore.userInfo?.studentId || '',
  college: userStore.userInfo?.college || '',
  major: userStore.userInfo?.major || '',
  class: userStore.userInfo?.class || '',
  role: userStore.userInfo?.role === 'student' ? '学生' : '教师',
  phone: '',
  email: ''
});

const resetForm = () => {
  userForm.value = {
    name: userStore.userInfo?.name || '',
    studentId: userStore.userInfo?.studentId || '',
    college: userStore.userInfo?.college || '',
    major: userStore.userInfo?.major || '',
    class: userStore.userInfo?.class || '',
    role: userStore.userInfo?.role === 'student' ? '学生' : '教师',
    phone: '',
    email: ''
  };
};

const saveForm = () => {
  ElMessage({
    message: '信息保存成功',
    type: 'success',
    duration: 1500
  });
};

onMounted(() => {
  // 页面加载动画
  document.body.classList.add('fade-in');
});
</script>

<style scoped>
:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-button) {
  transition: all 0.3s ease;
}

:deep(.el-button:hover) {
  transform: translateY(-1px);
}
</style>