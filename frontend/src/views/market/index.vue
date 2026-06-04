<template>
  <div class="min-h-screen bg-gray-50 flex flex-col font-sans">
    <!-- 引入头部组件 -->
    <Header />

    <!-- 主体内容 -->
    <main class="max-w-7xl mx-auto w-full px-4 py-6 space-y-6">

      <!-- 页面标题区 -->
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 class="text-2xl font-bold text-gray-800">二手跳蚤集市</h1>
          <p class="text-gray-500 text-sm mt-1">Campus Marketplace • 发现校园好物，开启循环生活</p>
        </div>
        <el-button type="primary" class="!h-11 px-6 rounded-xl font-bold shadow-sm hover:shadow-indigo-200" @click="showPublishDialog = true">
          <el-icon class="mr-2"><Plus /></el-icon>发布闲置
        </el-button>
      </div>

      <!-- 标签页 -->
      <el-card shadow="never" class="border border-gray-100 rounded-xl">
        <el-tabs v-model="activeTab" class="market-tabs">
          <el-tab-pane label="商品列表" name="list">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><Shop /></el-icon>
                商品列表
              </span>
            </template>

            <!-- 统计卡片 -->
            <div class="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
              <div
                v-for="(stat, index) in stats"
                :key="index"
                class="bg-gradient-to-br from-white to-gray-50 rounded-xl border border-gray-100 p-4 transition-all duration-300 hover:shadow-md cursor-pointer"
              >
                <div class="flex items-center gap-3">
                  <div :class="[stat.iconBg, 'p-2.5 rounded-lg']">
                    <el-icon :class="[stat.iconColor, 'text-xl']">
                      <component :is="stat.icon" />
                    </el-icon>
                  </div>
                  <div>
                    <p class="text-2xl font-bold text-gray-800">{{ stat.value }}</p>
                    <p class="text-xs text-gray-500">{{ stat.label }}</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 搜索和筛选 -->
            <div class="bg-gray-50 rounded-xl p-4 mb-4">
              <el-form :model="searchForm" layout="inline" size="large" class="search-form">
                <el-form-item label="关键字" class="search-item">
                  <el-input
                    v-model="searchForm.keyword"
                    placeholder="搜索商品名称..."
                    clearable
                    class="search-input"
                    @keyup.enter="handleSearch"
                  >
                    <template #prefix><el-icon><Search /></el-icon></template>
                  </el-input>
                </el-form-item>
                <el-form-item label="分类" class="search-item">
                  <el-select v-model="searchForm.category" placeholder="全部分类" clearable class="search-input">
                    <el-option label="电子产品" value="electronics" />
                    <el-option label="图书教材" value="books" />
                    <el-option label="生活用品" value="life" />
                    <el-option label="体育用品" value="sports" />
                    <el-option label="美妆服饰" value="beauty" />
                    <el-option label="其他" value="other" />
                  </el-select>
                </el-form-item>
                <el-form-item class="search-actions">
                  <el-button type="primary" class="rounded-xl" @click="handleSearch" :loading="listLoading">
                    <el-icon class="mr-2"><Search /></el-icon>筛选
                  </el-button>
                  <el-button class="rounded-xl" @click="handleReset">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 加载状态 -->
            <div v-if="listLoading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
              <div v-for="i in 8" :key="i" class="bg-white rounded-2xl border border-gray-100 overflow-hidden">
                <el-skeleton animated>
                  <template #template>
                    <el-skeleton-item variant="image" class="w-full !h-48" />
                    <div class="p-4 space-y-3">
                      <el-skeleton-item variant="text" class="w-3/4" />
                      <el-skeleton-item variant="text" class="w-1/2" />
                    </div>
                  </template>
                </el-skeleton>
              </div>
            </div>

            <!-- 商品列表 -->
            <div v-else-if="goodsRecords.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
              <div
                v-for="item in goodsRecords"
                :key="item.id"
                class="group bg-white rounded-2xl border border-gray-100 overflow-hidden transition-all duration-300 hover:shadow-xl hover:-translate-y-1 cursor-pointer"
                @click="handleViewDetail(item)"
              >
                <!-- 商品图片 -->
                <div class="relative aspect-[4/3] overflow-hidden bg-slate-100">
                  <img
                    :src="parseFirstImage(item.images)"
                    :alt="item.title"
                    class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110"
                  >
                  <!-- 分类标签 -->
                  <div class="absolute top-3 left-3">
                    <span class="bg-white/90 backdrop-blur-sm text-slate-900 text-[10px] font-bold px-2.5 py-1 rounded-full shadow-sm border border-slate-100">
                      {{ translateCategory(item.category) }}
                    </span>
                  </div>
                  <!-- 状态标签 -->
                  <div class="absolute top-3 right-3">
                    <el-tag
                      :type="item.status === 'ON_SALE' ? 'success' : 'info'"
                      size="small"
                      class="backdrop-blur-sm"
                    >
                      {{ item.status === 'ON_SALE' ? '热售中' : '已售出' }}
                    </el-tag>
                  </div>
                </div>

                <!-- 商品信息 -->
                <div class="p-4">
                  <h3 class="font-bold text-gray-800 line-clamp-1 group-hover:text-indigo-600 transition-colors mb-1">
                    {{ item.title }}
                  </h3>
                  <p class="text-slate-400 text-xs line-clamp-2 mb-3 leading-relaxed">{{ item.description || '暂无详细描述' }}</p>

                  <div class="flex items-center justify-between pt-3 border-t border-slate-50">
                    <div class="flex items-center gap-2">
                      <el-avatar :size="20" :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${item.sellerName}`" />
                      <span class="text-slate-600 text-[11px] font-bold">{{ item.sellerName }}</span>
                    </div>
                    <span class="text-rose-500 font-black text-lg">¥{{ item.price }}</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- 空状态 -->
            <el-empty v-else description="暂无相关商品" :image-size="200" class="my-8">
              <el-button type="primary" class="rounded-xl" @click="handleReset">查看全部商品</el-button>
            </el-empty>

            <!-- 分页 -->
            <div v-if="!listLoading && goodsRecords.length > 0" class="mt-6 flex justify-center">
              <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[8, 16, 24, 48]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                class="refined-pagination"
              />
            </div>
          </el-tab-pane>

          <el-tab-pane label="我的发布" name="myGoods">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><Sell /></el-icon>
                我的发布
                <el-badge :value="myGoods.length" :max="99" class="badge" v-if="myGoods.length > 0" />
              </span>
            </template>

            <!-- 加载状态 -->
            <div v-if="loadingMyGoods" class="space-y-4">
              <el-skeleton v-for="i in 3" :key="i" animated class="bg-white rounded-xl p-4" />
            </div>

            <!-- 我的发布列表 -->
            <div v-else-if="myGoods.length > 0" class="space-y-4">
              <div
                v-for="item in myGoods"
                :key="item.id"
                class="bg-white rounded-xl border border-gray-100 p-4 transition-all duration-300 hover:shadow-md"
              >
                <div class="flex flex-col sm:flex-row gap-4">
                  <!-- 图片 -->
                  <div class="w-full sm:w-32 h-32 sm:h-24 bg-gray-100 rounded-xl overflow-hidden flex-shrink-0">
                    <img :src="parseFirstImage(item.images)" class="w-full h-full object-cover" />
                  </div>

                  <!-- 信息 -->
                  <div class="flex-grow">
                    <div class="flex justify-between items-start">
                      <div>
                        <h3 class="font-bold text-gray-800">{{ item.title }}</h3>
                        <p class="text-gray-500 text-sm mt-1">{{ item.description }}</p>
                      </div>
                      <el-tag :type="item.status === 'ON_SALE' ? 'success' : 'info'" size="small">
                        {{ getStatusText(item.status) }}
                      </el-tag>
                    </div>
                    <div class="flex justify-between items-end mt-3">
                      <span class="text-rose-500 font-bold text-lg">¥{{ item.price }}</span>
                      <div class="flex gap-2">
                        <el-button size="small" type="primary" class="rounded-lg" @click="handleEdit(item)">编辑</el-button>
                        <el-button size="small" type="success" class="rounded-lg" @click="handleChangeStatus(item, 'SOLD')" v-if="item.status === 'ON_SALE'">已售</el-button>
                        <el-button size="small" type="warning" class="rounded-lg" @click="handleChangeStatus(item, 'ON_SALE')" v-if="item.status === 'SOLD'">重新上架</el-button>
                        <el-button size="small" type="danger" class="rounded-lg" @click="handleDelete(item)">删除</el-button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 空状态 -->
            <el-empty v-else description="您还没有发布任何商品" class="my-8">
              <el-button type="primary" class="rounded-xl" @click="showPublishDialog = true">发布商品</el-button>
            </el-empty>
          </el-tab-pane>

          <el-tab-pane label="我的收藏" name="myFavorite">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><Star /></el-icon>
                我的收藏
                <el-badge :value="myFavorites.length" :max="99" class="badge" v-if="myFavorites.length > 0" />
              </span>
            </template>

            <!-- 加载状态 -->
            <div v-if="loadingFavorites" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
              <div v-for="i in 4" :key="i" class="bg-white rounded-xl p-4">
                <el-skeleton animated />
              </div>
            </div>

            <!-- 收藏列表 -->
            <div v-else-if="myFavorites.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
              <div
                v-for="item in myFavorites"
                :key="item.id"
                class="bg-white rounded-xl border border-gray-100 p-4 transition-all duration-300 hover:shadow-lg cursor-pointer group"
                @click="handleViewDetail(item)"
              >
                <div class="w-full h-36 bg-gray-100 rounded-lg overflow-hidden mb-3">
                  <img :src="parseFirstImage(item.images)" class="w-full h-full object-cover" />
                </div>
                <h3 class="font-bold text-gray-800 line-clamp-1 group-hover:text-indigo-600 transition-colors">{{ item.title }}</h3>
                <div class="flex justify-between items-center mt-2">
                  <span class="text-rose-500 font-bold">¥{{ item.price }}</span>
                  <el-button size="small" type="danger" plain class="rounded-lg" @click.stop="removeFavorite(item)">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 空状态 -->
            <el-empty v-else description="您还没有收藏任何商品" class="my-8">
              <el-button type="primary" class="rounded-xl" @click="activeTab = 'list'">去逛逛</el-button>
            </el-empty>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </main>

    <!-- 发布商品弹窗 -->
    <el-dialog
      v-model="showPublishDialog"
      title="发布闲置物品"
      width="500px"
      class="refined-dialog"
      destroy-on-close
    >
      <el-form :model="publishForm" :rules="publishRules" ref="publishFormRef" label-position="top" class="mt-2">
        <el-form-item label="商品名称" prop="title">
          <el-input v-model="publishForm.title" placeholder="给你的宝贝起个吸睛的名字" class="refined-input" />
        </el-form-item>
        <div class="grid grid-cols-2 gap-4">
          <el-form-item label="价格 (元)" prop="price">
            <el-input-number v-model="publishForm.price" :min="0" :precision="2" :step="10" class="!w-full" />
          </el-form-item>
          <el-form-item label="分类" prop="category">
            <el-select v-model="publishForm.category" placeholder="选择分类" class="!w-full">
              <el-option label="电子产品" value="electronics" />
              <el-option label="图书教材" value="books" />
              <el-option label="生活用品" value="life" />
              <el-option label="体育用品" value="sports" />
              <el-option label="美妆服饰" value="beauty" />
              <el-option label="其他" value="other" />
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="物品图片 (单张链接)" prop="images">
          <el-input v-model="publishForm.images" placeholder="请输入图片 URL 地址" />
        </el-form-item>
        <el-form-item label="详细说明" prop="description">
          <el-input
            v-model="publishForm.description"
            type="textarea"
            rows="4"
            placeholder="描述一下成色、来源等详情..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="flex gap-3 pt-2">
          <el-button class="flex-1 !h-11 rounded-xl font-bold" @click="showPublishDialog = false">取消</el-button>
          <el-button type="primary" class="flex-1 !h-11 rounded-xl font-bold" @click="handlePublish" :loading="submitLoading">确认发布</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="showDetailDialog"
      :title="null"
      width="700px"
      class="refined-dialog no-header-dialog"
      destroy-on-close
    >
      <div v-if="currentItem" class="flex flex-col md:flex-row">
        <!-- 图片 -->
        <div class="md:w-1/2">
          <img :src="parseFirstImage(currentItem.images)" class="w-full h-full min-h-[300px] object-cover rounded-l-2xl md:rounded-r-none rounded-r-2xl" />
        </div>

        <!-- 信息 -->
        <div class="md:w-1/2 p-6 md:p-8 flex flex-col">
          <div class="mb-4">
            <span class="text-indigo-600 text-[10px] font-black uppercase tracking-widest">
              {{ translateCategory(currentItem.category) }}
            </span>
            <h2 class="text-2xl font-black text-gray-900 mt-1 leading-tight">{{ currentItem.title }}</h2>
          </div>

          <div class="flex items-center justify-between mb-6">
            <span class="text-3xl font-black text-rose-500">¥{{ currentItem.price }}</span>
            <el-tag :type="currentItem.status === 'ON_SALE' ? 'success' : 'info'">
              {{ currentItem.status === 'ON_SALE' ? '热售中' : '已售出' }}
            </el-tag>
          </div>

          <div class="flex-1">
            <h4 class="text-gray-400 text-[10px] font-bold uppercase tracking-widest mb-2">物品描述</h4>
            <p class="text-gray-600 text-sm leading-relaxed">{{ currentItem.description || '暂无详细说明' }}</p>
          </div>

          <!-- 卖家信息 -->
          <div class="mt-6 pt-6 border-t border-gray-100 flex items-center justify-between">
            <div class="flex items-center gap-3">
              <el-avatar :size="36" :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${currentItem.sellerName}`" />
              <div>
                <div class="text-gray-900 text-sm font-bold">{{ currentItem.sellerName }}</div>
                <div class="text-gray-400 text-xs">卖家</div>
              </div>
            </div>
            <el-button type="primary" class="!h-10 px-5 rounded-xl font-bold">
              <el-icon class="mr-2"><ChatDotRound /></el-icon>联系卖家
            </el-button>
          </div>
        </div>
      </div>
    </el-dialog>

    <Footer />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import Header from '../../components/Header.vue';
import Footer from '../../components/Footer.vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Search, Shop, Sell, Star, Delete, ChatDotRound, Goods, ShoppingCart, View } from '@element-plus/icons-vue';
import { listMarketGoods, publishGoods, deleteGoods, changeGoodsStatus, type GoodsVO } from '../../api/market';
import { useUserStore } from '../../store/user';

const userStore = useUserStore();

// 标签页
const activeTab = ref('list');

// 加载状态
const listLoading = ref(false);
const loadingMyGoods = ref(false);
const loadingFavorites = ref(false);
const submitLoading = ref(false);

// 统计
const stats = [
  { label: '在售商品', value: '128', icon: Shop, iconBg: 'bg-blue-50', iconColor: 'text-blue-500' },
  { label: '今日新增', value: '24', icon: Plus, iconBg: 'bg-green-50', iconColor: 'text-green-500' },
  { label: '成交量', value: '856', icon: ShoppingCart, iconBg: 'bg-orange-50', iconColor: 'text-orange-500' },
  { label: '我的收藏', value: '12', icon: Star, iconBg: 'bg-purple-50', iconColor: 'text-purple-500' }
];

// 列表相关
const goodsRecords = ref<GoodsVO[]>([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(12);

// 我的发布
const myGoods = ref<GoodsVO[]>([]);

// 我的收藏
const myFavorites = ref<GoodsVO[]>([]);

// 搜索表单
const searchForm = reactive({
  keyword: '',
  category: ''
});

// 发布相关
const showPublishDialog = ref(false);
const publishFormRef = ref();
const publishForm = reactive({
  title: '',
  price: 0,
  category: '',
  description: '',
  images: ''
});

const publishRules = {
  title: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }]
};

// 详情相关
const showDetailDialog = ref(false);
const currentItem = ref<GoodsVO | null>(null);

// 获取列表
const fetchGoods = async () => {
  listLoading.value = true;
  try {
    const data = await listMarketGoods({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchForm.keyword,
      category: searchForm.category
    });
    goodsRecords.value = data.records;
    total.value = data.total;
  } catch (error) {
    console.error('获取商品列表失败:', error);
  } finally {
    listLoading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  currentPage.value = 1;
  fetchGoods();
};

// 重置
const handleReset = () => {
  searchForm.keyword = '';
  searchForm.category = '';
  activeTab.value = 'list';
  handleSearch();
};

// 分页
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  fetchGoods();
};

const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  fetchGoods();
};

// 发布商品
const handlePublish = () => {
  publishFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      submitLoading.value = true;
      try {
        await publishGoods({
          ...publishForm,
          images: publishForm.images ? JSON.stringify([publishForm.images]) : '[]'
        });
        ElMessage.success('发布成功');
        showPublishDialog.value = false;
        Object.assign(publishForm, { title: '', price: 0, category: '', description: '', images: '' });
        fetchGoods();
        activeTab.value = 'myGoods';
      } catch (error) {
        console.error('发布失败:', error);
      } finally {
        submitLoading.value = false;
      }
    }
  });
};

// 查看详情
const handleViewDetail = (item: GoodsVO) => {
  currentItem.value = item;
  showDetailDialog.value = true;
};

// 编辑
const handleEdit = (item: GoodsVO) => {
  ElMessage.info('编辑功能开发中');
};

// 修改状态
const handleChangeStatus = async (item: GoodsVO, status: string) => {
  try {
    await changeGoodsStatus(item.id, status);
    ElMessage.success('操作成功');
    fetchMyGoods();
  } catch (error) {
    console.error('操作失败:', error);
  }
};

// 删除
const handleDelete = (item: GoodsVO) => {
  ElMessageBox.confirm('确定要删除该商品吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteGoods(item.id);
      ElMessage.success('删除成功');
      fetchMyGoods();
    } catch (error) {
      console.error('删除失败:', error);
    }
  });
};

// 收藏/取消收藏
const removeFavorite = (item: GoodsVO) => {
  myFavorites.value = myFavorites.value.filter(f => f.id !== item.id);
  ElMessage.info('已取消收藏');
};

// 获取我的发布
const fetchMyGoods = async () => {
  loadingMyGoods.value = true;
  try {
    // 模拟数据，实际应调用API
    myGoods.value = goodsRecords.value.slice(0, 2);
  } finally {
    loadingMyGoods.value = false;
  }
};

// 获取我的收藏
const fetchMyFavorites = async () => {
  loadingFavorites.value = true;
  try {
    // 模拟数据
    myFavorites.value = goodsRecords.value.slice(0, 3);
  } finally {
    loadingFavorites.value = false;
  }
};

// 辅助函数
const parseFirstImage = (imagesJson: string) => {
  try {
    const images = JSON.parse(imagesJson);
    return images && images.length > 0 ? images[0] : 'https://images.unsplash.com/photo-1596524430615-b46475ddff6e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80';
  } catch (e) {
    return 'https://images.unsplash.com/photo-1596524430615-b46475ddff6e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80';
  }
};

const translateCategory = (cat: string) => {
  const map: Record<string, string> = {
    'electronics': '电子产品',
    'books': '图书教材',
    'life': '生活用品',
    'sports': '体育用品',
    'beauty': '美妆服饰',
    'other': '其他杂项'
  };
  return map[cat] || cat;
};

const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    'ON_SALE': '热售中',
    'SOLD': '已售出',
    'OFF_SHELF': '已下架'
  };
  return map[status] || status;
};

// 监听标签页切换
const handleTabChange = (tab: string) => {
  if (tab === 'myGoods') {
    fetchMyGoods();
  } else if (tab === 'myFavorite') {
    fetchMyFavorites();
  }
};

onMounted(() => {
  fetchGoods();
});
</script>

<style scoped>
/* 标签页样式 */
:deep(.market-tabs .el-tabs__item) {
  font-size: 15px;
  padding: 0 24px;
  height: 50px;
  line-height: 50px;
}

:deep(.market-tabs .el-tabs__item.is-active) {
  color: #3b82f6;
  font-weight: 600;
}

:deep(.market-tabs .el-tabs__active-bar) {
  height: 3px;
  border-radius: 3px;
}

:deep(.market-tabs .el-tabs__nav-wrap::after) {
  height: 1px;
}

/* 搜索表单 */
.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: flex-end;
}

.search-item {
  margin-right: 0;
}

.search-input {
  width: 180px;
}

.search-actions {
  margin-left: auto;
}

/* 徽章 */
.badge {
  margin-left: 4px;
}

/* 弹窗样式 */
.refined-dialog {
  border-radius: 20px !important;
  overflow: hidden !important;
}

.no-header-dialog :deep(.el-dialog__header) {
  display: none !important;
}

.no-header-dialog :deep(.el-dialog__body) {
  padding: 0 !important;
}

/* 输入框样式 */
:deep(.refined-input .el-input__wrapper) {
  border-radius: 12px !important;
}

/* 分页样式 */
:deep(.refined-pagination .el-pager li) {
  @apply !rounded-lg !mx-1 !font-bold !border-none !bg-white !shadow-sm !transition-all;
}

:deep(.refined-pagination .el-pager li.is-active) {
  @apply !bg-indigo-600 !text-white !shadow-indigo-100;
}

/* 悬停效果 */
:deep(.el-button) {
  transition: all 0.3s ease;
}

:deep(.el-button:hover) {
  transform: translateY(-1px);
}

/* 响应式 */
@media (max-width: 640px) {
  .search-form {
    flex-direction: column;
    align-items: stretch;
  }

  .search-input {
    width: 100%;
  }

  .search-actions {
    margin-left: 0;
    width: 100%;
  }

  .search-actions .el-button {
    width: 100%;
    margin-bottom: 8px;
  }
}
</style>
