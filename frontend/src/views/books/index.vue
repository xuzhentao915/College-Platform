<template>
  <div class="min-h-screen bg-gray-50 flex flex-col font-sans">
    <!-- 引入头部组件 -->
    <Header />

    <!-- 主体内容 -->
    <main class="max-w-7xl mx-auto w-full px-4 py-6 space-y-6">

      <!-- 页面标题区 -->
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-2xl font-bold text-gray-800">图书管理</h1>
          <p class="text-gray-500 text-sm mt-1">图书馆藏检索与个人借阅管理</p>
        </div>
        <el-button type="primary" class="rounded-lg">
          <el-icon class="mr-2"><Reading /></el-icon>预约借阅
        </el-button>
      </div>

      <!-- 标签页切换 -->
      <el-card shadow="never" class="border border-gray-100 rounded-xl">
        <el-tabs v-model="activeTab" class="book-tabs">
          <el-tab-pane label="馆藏检索" name="search">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><Search /></el-icon>
                馆藏检索
              </span>
            </template>

            <!-- 搜索和筛选 -->
            <div class="bg-gray-50 rounded-xl p-4 mb-4">
              <el-form :model="searchForm" layout="inline" size="large" class="search-form">
                <el-form-item label="图书名称" class="search-item">
                  <el-input
                    v-model="searchForm.name"
                    placeholder="书名/作者/出版社"
                    clearable
                    class="search-input"
                  >
                    <template #prefix><el-icon><Document /></el-icon></template>
                  </el-input>
                </el-form-item>
                <el-form-item label="ISBN" class="search-item">
                  <el-input
                    v-model="searchForm.isbn"
                    placeholder="国际标准书号"
                    clearable
                    class="search-input"
                  >
                    <template #prefix><el-icon><Collection /></el-icon></template>
                  </el-input>
                </el-form-item>
                <el-form-item label="分类" class="search-item">
                  <el-select v-model="searchForm.category" placeholder="选择分类" clearable class="search-input">
                    <el-option label="计算机科学" value="computer" />
                    <el-option label="文学艺术" value="literature" />
                    <el-option label="历史哲学" value="history" />
                    <el-option label="自然科学" value="science" />
                    <el-option label="社会科学" value="social" />
                  </el-select>
                </el-form-item>
                <el-form-item class="search-actions">
                  <el-button type="primary" class="rounded-lg" @click="search">
                    <el-icon class="mr-2"><Search /></el-icon>搜索
                  </el-button>
                  <el-button class="rounded-lg" @click="reset">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 加载状态 - 骨架屏 -->
            <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
              <div v-for="i in 8" :key="i" class="bg-white rounded-xl border border-gray-100 p-4">
                <el-skeleton animated>
                  <template #template>
                    <el-skeleton-item variant="image" class="w-full h-40 rounded-lg mb-3" />
                    <el-skeleton-item variant="text" class="w-3/4 mb-2" />
                    <el-skeleton-item variant="text" class="w-1/2" />
                  </template>
                </el-skeleton>
              </div>
            </div>

            <!-- 图书列表 - 卡片网格 -->
            <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
              <div
                v-for="book in books"
                :key="book.id"
                class="bg-white rounded-xl border border-gray-100 p-4 transition-all duration-300 hover:shadow-lg hover:border-blue-200 cursor-pointer group"
                @click="viewDetail(book)"
              >
                <!-- 图书封面 -->
                <div class="w-full h-40 bg-gray-100 rounded-lg overflow-hidden mb-3 relative">
                  <img
                    :src="book.cover || defaultCover"
                    :alt="book.name"
                    class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
                  >
                  <!-- 状态标签 -->
                  <div class="absolute top-2 right-2">
                    <el-tag
                      :type="book.status === 'available' ? 'success' : 'warning'"
                      size="small"
                      class="status-tag"
                    >
                      {{ book.status === 'available' ? '可借' : '已借出' }}
                    </el-tag>
                  </div>
                  <!-- 分类标签 -->
                  <div class="absolute top-2 left-2">
                    <el-tag type="info" size="small" class="category-tag">
                      {{ book.category }}
                    </el-tag>
                  </div>
                </div>

                <!-- 图书信息 -->
                <div class="space-y-2">
                  <h3 class="font-bold text-gray-800 truncate group-hover:text-blue-600 transition-colors" :title="book.name">
                    {{ book.name }}
                  </h3>
                  <p class="text-gray-500 text-sm truncate">作者：{{ book.author }}</p>
                  <p class="text-gray-400 text-xs truncate">ISBN：{{ book.isbn }}</p>

                  <!-- 操作按钮 -->
                  <div class="flex gap-2 pt-2">
                    <el-button
                      size="small"
                      type="primary"
                      :disabled="book.status !== 'available'"
                      class="flex-1 rounded-lg"
                      @click.stop="borrowBook(book)"
                    >
                      {{ book.status === 'available' ? '借阅' : '已借出' }}
                    </el-button>
                    <el-button size="small" class="rounded-lg" @click.stop="viewDetail(book)">
                      详情
                    </el-button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 空状态 -->
            <el-empty v-if="!loading && books.length === 0" description="暂无相关图书" class="my-8" />

            <!-- 分页 -->
            <div v-if="!loading && books.length > 0" class="mt-4 flex justify-end">
              <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[8, 16, 24, 32]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              />
            </div>
          </el-tab-pane>

          <el-tab-pane label="我的借阅" name="myBorrow">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><Tickets /></el-icon>
                我的借阅
                <el-badge :value="myBooks.length" :max="99" class="badge" />
              </span>
            </template>

            <!-- 加载状态 -->
            <div v-if="loadingMyBooks" class="space-y-4">
              <el-skeleton v-for="i in 3" :key="i" animated class="bg-white rounded-xl p-4" />
            </div>

            <!-- 借阅卡片列表 -->
            <div v-else-if="myBooks.length > 0" class="space-y-4">
              <div
                v-for="item in myBooks"
                :key="item.id"
                class="bg-white rounded-xl border border-gray-100 p-4 transition-all duration-300 hover:shadow-md"
                :class="{ 'border-red-200': item.daysLeft <= 3 }"
              >
                <div class="flex flex-col sm:flex-row gap-4">
                  <!-- 图书封面 -->
                  <div class="w-full sm:w-24 h-32 sm:h-24 bg-gray-100 rounded-lg overflow-hidden flex-shrink-0">
                    <img
                      :src="item.cover || defaultCover"
                      :alt="item.name"
                      class="w-full h-full object-cover"
                    >
                  </div>

                  <!-- 借阅信息 -->
                  <div class="flex-grow space-y-2">
                    <div class="flex justify-between items-start">
                      <div>
                        <h3 class="font-bold text-gray-800">{{ item.name }}</h3>
                        <p class="text-gray-500 text-sm">作者：{{ item.author }}</p>
                      </div>
                      <el-tag
                        :type="item.daysLeft <= 3 ? 'danger' : item.daysLeft <= 7 ? 'warning' : 'success'"
                        size="small"
                        class="ml-2"
                      >
                        <el-icon class="mr-1"><Clock /></el-icon>
                        {{ item.daysLeft <= 0 ? '已超期' : `剩余 ${item.daysLeft} 天` }}
                      </el-tag>
                    </div>

                    <!-- 借阅进度 -->
                    <div class="mt-2">
                      <div class="flex justify-between text-xs text-gray-500 mb-1">
                        <span>借阅进度</span>
                        <span>{{ item.borrowDate }} ~ {{ item.dueDate }}</span>
                      </div>
                      <el-progress
                        :percentage="item.progress"
                        :status="item.daysLeft <= 3 ? 'warning' : ''"
                        :stroke-width="8"
                        class="mb-2"
                      />
                    </div>

                    <!-- 操作按钮 -->
                    <div class="flex gap-2 pt-2">
                      <el-button type="primary" size="small" class="rounded-lg" @click="returnBook(item)">
                        归还
                      </el-button>
                      <el-button
                        size="small"
                        :disabled="item.renewed"
                        class="rounded-lg"
                        @click="renewBook(item)"
                      >
                        {{ item.renewed ? '已续借' : '续借' }}
                      </el-button>
                      <el-button size="small" type="info" plain class="rounded-lg" @click="viewDetail(item)">
                        详情
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 空状态 -->
            <el-empty v-else description="暂无借阅记录" class="my-8">
              <el-button type="primary" class="rounded-lg" @click="activeTab = 'search'">
                去借阅图书
              </el-button>
            </el-empty>
          </el-tab-pane>

          <el-tab-pane label="借阅历史" name="history">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><Clock /></el-icon>
                借阅历史
              </span>
            </template>

            <!-- 历史记录表格 -->
            <el-table :data="borrowHistory" stripe class="history-table">
              <el-table-column prop="name" label="图书名称" min-width="200" />
              <el-table-column prop="author" label="作者" width="150" />
              <el-table-column prop="borrowDate" label="借阅日期" width="120" />
              <el-table-column prop="returnDate" label="归还日期" width="120" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.status === 'returned' ? 'info' : 'success'" size="small">
                    {{ scope.row.status === 'returned' ? '已归还' : '超期未还' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>

            <div class="mt-4 flex justify-end">
              <el-pagination
                v-model:current-page="historyPage"
                :page-size="10"
                layout="total, prev, pager, next"
                :total="historyTotal"
              />
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>

      <!-- 图书详情弹窗 -->
      <el-dialog
        v-model="detailDialogVisible"
        title="图书详情"
        width="600px"
        class="book-detail-dialog"
      >
        <div v-if="selectedBook" class="flex flex-col sm:flex-row gap-6">
          <!-- 封面大图 -->
          <div class="w-full sm:w-48 h-64 sm:h-72 bg-gray-100 rounded-xl overflow-hidden flex-shrink-0 mx-auto sm:mx-0">
            <img
              :src="selectedBook.cover || defaultCover"
              :alt="selectedBook.name"
              class="w-full h-full object-cover"
            >
          </div>

          <!-- 详细信息 -->
          <div class="flex-grow space-y-3">
            <h2 class="text-xl font-bold text-gray-800">{{ selectedBook.name }}</h2>
            <p class="text-gray-600">作者：{{ selectedBook.author }}</p>
            <p class="text-gray-600">ISBN：{{ selectedBook.isbn }}</p>
            <p class="text-gray-600">出版社：{{ selectedBook.publisher || '清华大学出版社' }}</p>
            <p class="text-gray-600">分类：{{ selectedBook.category }}</p>
            <p class="text-gray-600">馆藏数量：{{ selectedBook.totalCopies || 5 }} 册</p>
            <p class="text-gray-600">可借数量：{{ selectedBook.availableCopies || 3 }} 册</p>
            <p class="text-gray-600">所在书架：{{ selectedBook.location || 'A-3-15' }}</p>

            <div class="pt-4">
              <el-tag
                :type="selectedBook.status === 'available' ? 'success' : 'warning'"
                size="large"
                class="mb-4"
              >
                {{ selectedBook.status === 'available' ? '可借' : '已借出' }}
              </el-tag>
              <div class="flex gap-2">
                <el-button
                  type="primary"
                  :disabled="selectedBook.status !== 'available'"
                  class="rounded-lg"
                  @click="borrowBook(selectedBook)"
                >
                  借阅此书
                </el-button>
                <el-button class="rounded-lg">加入心愿单</el-button>
              </div>
            </div>
          </div>
        </div>
      </el-dialog>
    </main>

    <!-- 引入页脚组件 -->
    <Footer />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import Header from '../../components/Header.vue';
import Footer from '../../components/Footer.vue';
import { ElMessage } from 'element-plus';
import { Reading, Search, Document, Collection, Tickets, Clock } from '@element-plus/icons-vue';

// 默认封面
const defaultCover = 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80';

// 标签页
const activeTab = ref('search');

// 搜索表单
const searchForm = reactive({
  name: '',
  isbn: '',
  category: ''
});

// 分页
const currentPage = ref(1);
const pageSize = ref(8);
const total = ref(50);

// 历史记录分页
const historyPage = ref(1);
const historyTotal = ref(20);

// 加载状态
const loading = ref(false);
const loadingMyBooks = ref(false);

// 详情弹窗
const detailDialogVisible = ref(false);
const selectedBook = ref<any>(null);

// 图书列表
const books = ref([
  {
    id: '1',
    name: 'JavaScript 高级程序设计',
    author: 'Nicholas C. Zakas',
    category: '计算机科学',
    isbn: '9787115275790',
    status: 'available',
    cover: 'https://images.unsplash.com/photo-1627398242454-45a1465c2479?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
    totalCopies: 5,
    availableCopies: 3,
    location: 'A-3-15'
  },
  {
    id: '2',
    name: 'Spring Cloud 微服务架构',
    author: '周立',
    category: '计算机科学',
    isbn: '9787115498847',
    status: 'borrowed',
    cover: 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
    totalCopies: 3,
    availableCopies: 0,
    location: 'A-5-22'
  },
  {
    id: '3',
    name: '深入理解计算机系统',
    author: 'Randal E. Bryant',
    category: '计算机科学',
    isbn: '9787111544937',
    status: 'available',
    cover: 'https://images.unsplash.com/photo-1518770660439-4636190af475?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
    totalCopies: 4,
    availableCopies: 2,
    location: 'B-1-08'
  },
  {
    id: '4',
    name: '人类简史：从动物到上帝',
    author: '尤瓦尔·赫拉利',
    category: '历史哲学',
    isbn: '9787508647357',
    status: 'available',
    cover: 'https://images.unsplash.com/photo-1471970471555-19d4b113e9ed?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
    totalCopies: 6,
    availableCopies: 4,
    location: 'C-2-11'
  },
  {
    id: '5',
    name: '百年孤独',
    author: '加西亚·马尔克斯',
    category: '文学艺术',
    isbn: '9787544718455',
    status: 'borrowed',
    cover: 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
    totalCopies: 3,
    availableCopies: 1,
    location: 'D-4-03'
  },
  {
    id: '6',
    name: '算法导论',
    author: 'Thomas H. Cormen',
    category: '计算机科学',
    isbn: '9787111407010',
    status: 'available',
    cover: 'https://images.unsplash.com/photo-1509228468518-180dd4864904?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
    totalCopies: 5,
    availableCopies: 2,
    location: 'A-2-19'
  },
  {
    id: '7',
    name: '红楼梦',
    author: '曹雪芹',
    category: '文学艺术',
    isbn: '9787020002207',
    status: 'available',
    cover: 'https://images.unsplash.com/photo-1512820790803-83ca734da794?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
    totalCopies: 8,
    availableCopies: 5,
    location: 'D-1-01'
  },
  {
    id: '8',
    name: '时间简史',
    author: '斯蒂芬·霍金',
    category: '自然科学',
    isbn: '9787535728358',
    status: 'borrowed',
    cover: 'https://images.unsplash.com/photo-1462331940025-496dfbfc7564?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
    totalCopies: 4,
    availableCopies: 1,
    location: 'E-3-12'
  }
]);

// 我的借阅
const myBooks = ref([
  {
    id: '101',
    name: 'JavaScript 高级程序设计',
    author: 'Nicholas C. Zakas',
    borrowDate: '2026-03-15',
    dueDate: '2026-04-15',
    daysLeft: 26,
    progress: 15,
    renewed: false,
    cover: 'https://images.unsplash.com/photo-1627398242454-45a1465c2479?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80'
  },
  {
    id: '102',
    name: 'Spring Cloud 微服务架构',
    author: '周立',
    borrowDate: '2026-03-01',
    dueDate: '2026-03-31',
    daysLeft: 12,
    progress: 75,
    renewed: true,
    cover: 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80'
  },
  {
    id: '103',
    name: '时间简史',
    author: '斯蒂芬·霍金',
    borrowDate: '2026-02-20',
    dueDate: '2026-03-20',
    daysLeft: -30,
    progress: 100,
    renewed: false,
    cover: 'https://images.unsplash.com/photo-1462331940025-496dfbfc7564?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80'
  }
]);

// 借阅历史
const borrowHistory = ref([
  { name: '高等数学', author: '同济大学', borrowDate: '2025-09-01', returnDate: '2025-12-15', status: 'returned' },
  { name: '大学英语', author: '李静', borrowDate: '2025-09-01', returnDate: '2025-12-20', status: 'returned' },
  { name: '数据结构', author: '严蔚敏', borrowDate: '2025-09-15', returnDate: '2026-01-10', status: 'returned' },
  { name: '操作系统原理', author: '汤小丹', borrowDate: '2025-10-01', returnDate: '2026-01-15', status: 'returned' },
  { name: '计算机网络', author: '谢希仁', borrowDate: '2025-10-15', returnDate: '2026-02-01', status: 'returned' }
]);

// 搜索
const search = () => {
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
    ElMessage({ message: '搜索完成', type: 'success', duration: 1500 });
  }, 800);
};

// 重置
const reset = () => {
  searchForm.name = '';
  searchForm.isbn = '';
  searchForm.category = '';
  search();
};

// 借阅
const borrowBook = (book: any) => {
  ElMessage({ message: `借阅《${book.name}》成功`, type: 'success', duration: 2000 });
  detailDialogVisible.value = false;
};

// 详情
const viewDetail = (book: any) => {
  selectedBook.value = book;
  detailDialogVisible.value = true;
};

// 归还
const returnBook = (item: any) => {
  ElMessage({ message: `归还《${item.name}》成功`, type: 'success', duration: 2000 });
};

// 续借
const renewBook = (item: any) => {
  item.renewed = true;
  item.daysLeft += 15;
  ElMessage({ message: `续借《${item.name}》成功，期限延长15天`, type: 'success', duration: 2000 });
};

// 分页
const handleSizeChange = (size: number) => {
  pageSize.value = size;
  loading.value = true;
  setTimeout(() => { loading.value = false; }, 500);
};

const handleCurrentChange = (current: number) => {
  currentPage.value = current;
  loading.value = true;
  setTimeout(() => { loading.value = false; }, 500);
};

// 初始化
onMounted(() => {
  document.body.classList.add('fade-in');
});
</script>

<style scoped>
/* 标签页样式 */
:deep(.book-tabs .el-tabs__item) {
  font-size: 15px;
  padding: 0 24px;
  height: 50px;
  line-height: 50px;
}

:deep(.book-tabs .el-tabs__item.is-active) {
  color: #3b82f6;
  font-weight: 600;
}

:deep(.book-tabs .el-tabs__active-bar) {
  height: 3px;
  border-radius: 3px;
}

:deep(.book-tabs .el-tabs__nav-wrap::after) {
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
  width: 200px;
}

.search-actions {
  margin-left: auto;
}

/* 图书卡片 */
.status-tag,
.category-tag {
  backdrop-filter: blur(8px);
}

/* 徽章 */
.badge {
  margin-left: 4px;
}

/* 历史表格 */
.history-table :deep(.el-table th) {
  font-weight: 600;
  background-color: #f9fafb;
}

/* 弹窗样式 */
.book-detail-dialog :deep(.el-dialog__body) {
  padding: 24px;
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
