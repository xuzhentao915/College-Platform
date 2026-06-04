import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

// 用户信息类型定义
interface UserInfo {
  id: string;
  studentId: string;
  name: string;
  college: string;
  major: string;
  class: string;
  avatar: string;
  role: string;
  isExcellent: boolean;
}

// 登录响应类型
export interface LoginResponse {
  token: string;
  userInfo: UserInfo;
}

export const useUserStore = defineStore('user', () => {
  const token = ref('');
  const userInfo = ref<UserInfo | null>(null);
  const loading = ref(false);

  // 计算属性
  const isLoggedIn = computed(() => !!token.value);
  const userName = computed(() => userInfo.value?.name || '');
  const userAvatar = computed(() => userInfo.value?.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png');

  // 设置token
  const setToken = (newToken: string) => {
    token.value = newToken;
  };

  // 设置用户信息
  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info;
  };

  // 登录
  const login = async (studentId: string, _password: string) => {
    loading.value = true;
    try {
      // 模拟登录请求
      return new Promise<LoginResponse>((resolve) => {
        setTimeout(() => {
          const mockResponse: LoginResponse = {
            token: 'enterprise_token_demo_2026',
            userInfo: {
                id: '1',
                studentId: studentId,
                name: '徐振涛',
                college: '软件工程学院',
                major: '软件工程',
                class: '2021级 3班',
                avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
                role: 'student',
                isExcellent: true
            }
          };
          setToken(mockResponse.token);
          setUserInfo(mockResponse.userInfo);
          resolve(mockResponse);
          loading.value = false;
        }, 1000);
      });
    } catch (error) {
      loading.value = false;
      throw error;
    }
  };

  // 登出
  const logout = () => {
    clearSession();
  };

  // 清除会话
  const clearSession = () => {
    token.value = '';
    userInfo.value = null;
  };

  return {
    token,
    userInfo,
    loading,
    isLoggedIn,
    userName,
    userAvatar,
    setToken,
    setUserInfo,
    login,
    logout,
    clearSession
  };
}, {
  persist: {
    key: 'college-user',
    storage: localStorage as Storage,
    paths: ['token', 'userInfo']
  }
});
