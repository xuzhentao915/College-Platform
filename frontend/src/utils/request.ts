import axios, { InternalAxiosRequestConfig, AxiosResponse } from 'axios';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/store/user';

// 创建 axios 实例
const service = axios.create({
  baseURL: '/api',
  timeout: 10000,
  withCredentials: true
});

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const userStore = useUserStore();
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data;

    if (res.code !== 200) {
      ElMessage({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      });

      if (res.code === 401) {
        const userStore = useUserStore();
        userStore.clearSession();
        location.href = '/login';
      }
      return Promise.reject(new Error(res.message || 'Error'));
    } else {
      return res.data; // 返回解包后的数据
    }
  },
  (error) => {
    ElMessage({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    });
    return Promise.reject(error);
  }
);

// 定义通用的请求函数
const request = <T = any>(config: any): Promise<T> => {
  return service.request<T, T>(config); // 强制 TypeScript 认为返回的是 T 而非 AxiosResponse<T>
};

export default request;
