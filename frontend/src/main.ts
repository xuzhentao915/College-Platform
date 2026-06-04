import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import pinia from './store';

// 引入全局样式体系
import './assets/styles/index.scss';

// 引入 Element Plus 及全局样式
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';

// 引入所有图标组件
import * as ElementPlusIconsVue from '@element-plus/icons-vue';

const app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(router)
   .use(pinia)
   .use(ElementPlus)
   .mount('#app');
