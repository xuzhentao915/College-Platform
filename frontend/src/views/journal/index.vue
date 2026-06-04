<template>
  <div class="min-h-screen bg-gray-50 flex flex-col font-sans">
    <!-- 引入头部组件 -->
    <Header />

    <!-- 主体内容 -->
    <main class="max-w-7xl mx-auto w-full px-4 py-6 space-y-6">

      <!-- 页面标题区 -->
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-2xl font-bold text-gray-800">学术期刊库</h1>
          <p class="text-gray-500 text-sm mt-1">学术文献检索与个人文献管理</p>
        </div>
        <el-button type="primary" class="rounded-lg">
          <el-icon class="mr-2"><Upload /></el-icon>上传论文
        </el-button>
      </div>

      <!-- 标签页切换 -->
      <el-card shadow="never" class="border border-gray-100 rounded-xl">
        <el-tabs v-model="activeTab" class="journal-tabs">
          <el-tab-pane label="文献检索" name="search">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><Search /></el-icon>
                文献检索
              </span>
            </template>

            <!-- 搜索和筛选 -->
            <div class="bg-gray-50 rounded-xl p-4 mb-4">
              <el-form :model="searchForm" layout="inline" size="large" class="search-form">
                <el-form-item label="论文标题" class="search-item">
                  <el-input
                    v-model="searchForm.title"
                    placeholder="输入论文标题..."
                    clearable
                    class="search-input"
                  >
                    <template #prefix><el-icon><Document /></el-icon></template>
                  </el-input>
                </el-form-item>
                <el-form-item label="作者" class="search-item">
                  <el-input
                    v-model="searchForm.author"
                    placeholder="输入作者姓名..."
                    clearable
                    class="search-input"
                  >
                    <template #prefix><el-icon><User /></el-icon></template>
                  </el-input>
                </el-form-item>
                <el-form-item label="关键词" class="search-item">
                  <el-input
                    v-model="searchForm.keywords"
                    placeholder="输入关键词..."
                    clearable
                    class="search-input"
                  >
                    <template #prefix><el-icon><Collection /></el-icon></template>
                  </el-input>
                </el-form-item>
                <el-form-item label="来源" class="search-item">
                  <el-select v-model="searchForm.source" placeholder="选择来源" clearable class="search-input">
                    <el-option label="知网 CNKI" value="cnki" />
                    <el-option label="万方" value="wanfang" />
                    <el-option label="维普" value="cqvip" />
                    <el-option label="arXiv" value="arxiv" />
                    <el-option label="IEEE" value="ieee" />
                    <el-option label="PubMed" value="pubmed" />
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

            <!-- 加载状态 -->
            <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div v-for="i in 6" :key="i" class="bg-white rounded-xl border border-gray-100 p-4">
                <el-skeleton animated>
                  <template #template>
                    <el-skeleton-item variant="text" class="w-full mb-2" />
                    <el-skeleton-item variant="text" class="w-3/4 mb-4" />
                    <el-skeleton-item variant="text" class="w-1/2 mb-4" />
                    <el-skeleton-item variant="text" class="w-2/3" />
                  </template>
                </el-skeleton>
              </div>
            </div>

            <!-- 文献列表 -->
            <div v-else class="space-y-4">
              <div
                v-for="(paper, index) in journals"
                :key="index"
                class="bg-white rounded-xl border border-gray-100 p-5 transition-all duration-300 hover:shadow-lg hover:border-blue-200 cursor-pointer group"
                @click="viewDetail(paper)"
              >
                <div class="flex flex-col lg:flex-row lg:items-start gap-4">
                  <!-- 来源标签 -->
                  <div class="flex-shrink-0">
                    <div
                      class="w-12 h-12 rounded-xl flex items-center justify-center text-white font-bold text-sm"
                      :class="getSourceTagClass(paper.source)"
                    >
                      {{ getSourceAbbr(paper.source) }}
                    </div>
                  </div>

                  <!-- 文献信息 -->
                  <div class="flex-grow">
                    <div class="flex items-start gap-3 mb-2">
                      <h3 class="font-bold text-gray-800 text-lg leading-snug group-hover:text-blue-600 transition-colors flex-grow">
                        {{ paper.title }}
                      </h3>
                      <el-tag :type="paper.featured ? 'danger' : 'info'" size="small" v-if="paper.featured">
                        <el-icon class="mr-1"><Star /></el-icon>高引
                      </el-tag>
                    </div>
                    <p class="text-gray-500 text-sm mb-3 flex items-center gap-2">
                      <el-icon><User /></el-icon>
                      {{ paper.authors }}
                    </p>
                    <div class="flex flex-wrap gap-3 text-xs text-gray-400">
                      <span class="flex items-center gap-1 px-2 py-1 bg-gray-50 rounded">
                        <el-icon><Timer /></el-icon>
                        {{ paper.year }}
                      </span>
                      <span class="flex items-center gap-1 px-2 py-1 bg-gray-50 rounded">
                        <el-icon><Collection /></el-icon>
                        {{ paper.volume }}
                      </span>
                      <span class="flex items-center gap-1 px-2 py-1 bg-gray-50 rounded">
                        <el-icon><Link /></el-icon>
                        DOI: {{ paper.doi }}
                      </span>
                      <span v-if="paper.citations" class="flex items-center gap-1 px-2 py-1 bg-orange-50 text-orange-600 rounded">
                        <el-icon><ChatDotRound /></el-icon>
                        {{ paper.citations }} 引用
                      </span>
                    </div>
                  </div>

                  <!-- 操作按钮 -->
                  <div class="flex flex-wrap lg:flex-nowrap gap-2 lg:items-center">
                    <el-button size="small" type="primary" class="rounded-lg" @click.stop="downloadPaper(paper)">
                      <el-icon class="mr-1"><Download /></el-icon>下载
                    </el-button>
                    <el-button size="small" type="success" class="rounded-lg" @click.stop="collectPaper(paper)">
                      <el-icon class="mr-1"><Star /></el-icon>收藏
                    </el-button>
                    <el-button size="small" class="rounded-lg" @click.stop="viewDetail(paper)">
                      详情
                    </el-button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 空状态 -->
            <el-empty v-if="!loading && journals.length === 0" description="暂无相关文献" class="my-8" />

            <!-- 分页 -->
            <div v-if="!loading && journals.length > 0" class="mt-4 flex justify-end">
              <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[6, 12, 24, 48]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              />
            </div>
          </el-tab-pane>

          <el-tab-pane label="我的收藏" name="favorite">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><Star /></el-icon>
                我的收藏
                <el-badge :value="favorites.length" :max="99" class="badge" v-if="favorites.length > 0" />
              </span>
            </template>

            <!-- 加载状态 -->
            <div v-if="loadingFavorites" class="space-y-4">
              <el-skeleton v-for="i in 3" :key="i" animated class="bg-white rounded-xl p-4" />
            </div>

            <!-- 收藏列表 -->
            <div v-else-if="favorites.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div
                v-for="(item, index) in favorites"
                :key="index"
                class="bg-white rounded-xl border border-gray-100 p-4 transition-all duration-300 hover:shadow-lg hover:border-green-200 group"
              >
                <div class="flex items-start gap-3 mb-3">
                  <div
                    class="w-10 h-10 rounded-lg flex items-center justify-center text-white font-bold text-xs flex-shrink-0"
                    :class="getSourceTagClass(item.source)"
                  >
                    {{ getSourceAbbr(item.source) }}
                  </div>
                  <div class="flex-grow min-w-0">
                    <h4 class="font-bold text-gray-800 truncate group-hover:text-blue-600 transition-colors" :title="item.title">
                      {{ item.title }}
                    </h4>
                    <p class="text-gray-500 text-xs mt-1 truncate">{{ item.authors }}</p>
                  </div>
                </div>
                <div class="flex justify-between items-center pt-3 border-t border-gray-50">
                  <span class="text-xs text-gray-400">{{ item.source }} | {{ item.year }}</span>
                  <div class="flex gap-1">
                    <el-button size="small" type="primary" plain class="rounded-lg" @click="viewDetail(item)">查看</el-button>
                    <el-button size="small" type="danger" plain class="rounded-lg" @click="removeFavorite(item)">
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 空状态 -->
            <el-empty v-else description="暂无收藏文献" class="my-8">
              <el-button type="primary" class="rounded-lg" @click="activeTab = 'search'">
                去搜索文献
              </el-button>
            </el-empty>
          </el-tab-pane>

          <el-tab-pane label="学术交流" name="discussion">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><ChatDotRound /></el-icon>
                学术交流
              </span>
            </template>

            <!-- 交流区 -->
            <div class="space-y-4">
              <!-- 讨论分类 -->
              <div class="flex gap-3 mb-4">
                <el-tag
                  v-for="tag in discussionTags"
                  :key="tag.value"
                  :type="activeTag === tag.value ? 'primary' : 'info'"
                  class="cursor-pointer transition-all"
                  @click="activeTag = tag.value"
                >
                  {{ tag.label }}
                </el-tag>
              </div>

              <!-- 讨论列表 -->
              <div v-for="(discuss, index) in discussions" :key="index" class="bg-white rounded-xl border border-gray-100 p-5 transition-all duration-300 hover:shadow-md">
                <div class="flex gap-4">
                  <!-- 头像 -->
                  <el-avatar :size="44" :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${discuss.author}`" class="flex-shrink-0" />

                  <!-- 内容 -->
                  <div class="flex-grow">
                    <div class="flex items-center gap-2 mb-2">
                      <span class="font-bold text-gray-800">{{ discuss.author }}</span>
                      <el-tag size="small" type="info">{{ discuss.role }}</el-tag>
                      <span class="text-gray-400 text-xs">{{ discuss.time }}</span>
                    </div>
                    <h4 class="font-semibold text-gray-800 mb-2">{{ discuss.title }}</h4>
                    <p class="text-gray-600 text-sm leading-relaxed mb-3">{{ discuss.content }}</p>
                    <div class="flex items-center gap-4 text-xs text-gray-400">
                      <span class="flex items-center gap-1">
                        <el-icon><View /></el-icon>
                        {{ discuss.views }} 阅读
                      </span>
                      <span class="flex items-center gap-1">
                        <el-icon><ChatLineRound /></el-icon>
                        {{ discuss.replies }} 回复
                      </span>
                      <span class="flex items-center gap-1 cursor-pointer hover:text-blue-500">
                        <el-icon><Star /></el-icon>
                        {{ discuss.likes }} 点赞
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 分页 -->
            <div class="mt-4 flex justify-end">
              <el-pagination
                v-model:current-page="discussionPage"
                :page-size="5"
                layout="total, prev, pager, next"
                :total="discussionTotal"
              />
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>

      <!-- 热门论文区域 -->
      <div>
        <h2 class="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
          <el-icon class="text-orange-500"><Top /></el-icon>
          热门论文
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div
            v-for="(paper, index) in hotPapers"
            :key="index"
            class="bg-white rounded-xl border border-gray-100 p-4 transition-all duration-300 hover:shadow-lg hover:border-orange-200 cursor-pointer group"
            @click="viewDetail(paper)"
          >
            <div class="flex items-start gap-3 mb-3">
              <div
                class="w-10 h-10 rounded-lg flex items-center justify-center text-white font-bold text-xs flex-shrink-0"
                :class="getSourceTagClass(paper.source)"
              >
                {{ getSourceAbbr(paper.source) }}
              </div>
              <div class="flex-grow min-w-0">
                <h3 class="font-bold text-gray-800 text-sm line-clamp-2 group-hover:text-blue-600 transition-colors leading-snug">
                  {{ paper.title }}
                </h3>
                <p class="text-gray-400 text-xs mt-1">{{ paper.authors }}</p>
              </div>
            </div>
            <div class="flex justify-between items-center pt-3 border-t border-gray-50">
              <div class="flex items-center gap-2">
                <el-icon class="text-orange-400"><Top /></el-icon>
                <span class="text-xs text-gray-500">{{ paper.hotIndex }} 热度</span>
              </div>
              <el-button size="small" type="primary" plain class="rounded-lg">查看</el-button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="文献详情"
      width="650px"
      class="journal-detail-dialog"
    >
      <div v-if="selectedPaper" class="space-y-4">
        <!-- 来源标识 -->
        <div class="flex items-center gap-3">
          <div
            class="w-12 h-12 rounded-xl flex items-center justify-center text-white font-bold"
            :class="getSourceTagClass(selectedPaper.source)"
          >
            {{ getSourceAbbr(selectedPaper.source) }}
          </div>
          <div>
            <el-tag size="small" type="info">{{ selectedPaper.source }}</el-tag>
            <span class="text-gray-400 text-xs ml-2">{{ selectedPaper.year }}</span>
          </div>
        </div>

        <!-- 标题 -->
        <h2 class="text-xl font-bold text-gray-800 leading-snug">{{ selectedPaper.title }}</h2>

        <!-- 作者 -->
        <div class="flex items-center gap-2 text-gray-600">
          <el-icon><User /></el-icon>
          <span>{{ selectedPaper.authors }}</span>
        </div>

        <!-- 元数据 -->
        <div class="grid grid-cols-2 gap-3">
          <div class="p-3 bg-gray-50 rounded-lg">
            <p class="text-xs text-gray-400 mb-1">卷期</p>
            <p class="text-sm font-medium text-gray-800">{{ selectedPaper.volume }}</p>
          </div>
          <div class="p-3 bg-gray-50 rounded-lg">
            <p class="text-xs text-gray-400 mb-1">DOI</p>
            <p class="text-sm font-medium text-gray-800 truncate" :title="selectedPaper.doi">{{ selectedPaper.doi }}</p>
          </div>
          <div class="p-3 bg-gray-50 rounded-lg">
            <p class="text-xs text-gray-400 mb-1">发表年份</p>
            <p class="text-sm font-medium text-gray-800">{{ selectedPaper.year }}</p>
          </div>
          <div class="p-3 bg-gray-50 rounded-lg">
            <p class="text-xs text-gray-400 mb-1">引用数</p>
            <p class="text-sm font-medium text-orange-600">{{ selectedPaper.citations || 0 }}</p>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="flex gap-3 pt-4 border-t border-gray-100">
          <el-button type="primary" class="rounded-lg flex-1" @click="handleDialogDownload">
            <el-icon class="mr-2"><Download /></el-icon>下载文献
          </el-button>
          <el-button type="success" class="rounded-lg flex-1" @click="handleDialogCollect">
            <el-icon class="mr-2"><Star /></el-icon>添加收藏
          </el-button>
          <el-button class="rounded-lg flex-1" @click="handleCopyLink">
            <el-icon class="mr-2"><Link /></el-icon>复制链接
          </el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 引入页脚组件 -->
    <Footer />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import Header from '../../components/Header.vue';
import Footer from '../../components/Footer.vue';
import { ElMessage } from 'element-plus';
import {
  Upload, Search, Document, User, Collection, Star, Timer, Link,
  Download, Top, ChatDotRound, ChatLineRound, View, Delete
} from '@element-plus/icons-vue';

interface Paper {
  id: string;
  title: string;
  authors: string;
  source: string;
  year: string;
  volume: string;
  doi: string;
  citations?: number;
  featured?: boolean;
  hotIndex?: number;
}

// 标签页
const activeTab = ref('search');

// 加载状态
const loading = ref(false);
const loadingFavorites = ref(false);

// 搜索表单
const searchForm = reactive({
  title: '',
  author: '',
  keywords: '',
  source: ''
});

// 分页
const currentPage = ref(1);
const pageSize = ref(6);
const total = ref(50);

// 详情弹窗
const detailDialogVisible = ref(false);
const selectedPaper = ref<Paper | null>(null);

// 讨论区分页
const discussionPage = ref(1);
const discussionTotal = ref(15);
const activeTag = ref('all');

// 文献列表
const journals = ref<Paper[]>([
  {
    id: '1',
    title: '基于深度学习的图像识别技术研究进展与展望',
    authors: '张三, 李四, 王五, 赵六',
    source: '知网',
    year: '2025',
    volume: '12(3): 45-58',
    doi: '10.1234/j.xx.2025.03.012',
    citations: 128,
    featured: true
  },
  {
    id: '2',
    title: '微服务架构在企业级应用中的实践与性能优化',
    authors: '钱七, 孙八, 周九',
    source: '万方',
    year: '2025',
    volume: '8(2): 112-125',
    doi: '10.5678/j.yy.2025.02.034'
  },
  {
    id: '3',
    title: '区块链技术在供应链管理中的创新应用研究',
    authors: '吴十, 郑一, 冯二',
    source: 'IEEE',
    year: '2024',
    volume: 'Vol.15, No.4',
    doi: '10.1109/J.BLOCK.2024.9876543',
    citations: 86,
    featured: true
  },
  {
    id: '4',
    title: '人工智能在医疗影像诊断中的应用与伦理思考',
    authors: '王二, 陈三, 林四, 郑五',
    source: 'PubMed',
    year: '2024',
    volume: '45(2): 78-92',
    doi: '10.1016/j.med.2024.02.001'
  },
  {
    id: '5',
    title: '大数据分析在教育信息化中的应用研究',
    authors: '刘六, 杨七, 黄八',
    source: '维普',
    year: '2023',
    volume: '10(1): 23-35',
    doi: '10.7890/j.edu.2023.01.005'
  },
  {
    id: '6',
    title: 'Transformer模型在自然语言处理中的突破与应用',
    authors: '周十, 吴一, 郑二',
    source: 'arXiv',
    year: '2024',
    volume: '预印本',
    doi: '10.48550/arXiv.2024.01.0234',
    citations: 256,
    featured: true
  }
]);

// 我的收藏
const favorites = ref<Paper[]>([
  {
    id: '3',
    title: '区块链技术在供应链管理中的创新应用研究',
    authors: '吴十, 郑一, 冯二',
    source: 'IEEE',
    year: '2024',
    volume: 'Vol.15, No.4',
    doi: '10.1109/J.BLOCK.2024.9876543',
    citations: 86
  },
  {
    id: '6',
    title: 'Transformer模型在自然语言处理中的突破与应用',
    authors: '周十, 吴一, 郑二',
    source: 'arXiv',
    year: '2024',
    volume: '预印本',
    doi: '10.48550/arXiv.2024.01.0234',
    citations: 256
  }
]);

// 热门论文
const hotPapers = ref([
  {
    title: '深度学习在自然语言处理中的最新进展',
    authors: '张三, 李四, 王五',
    source: '知网',
    year: '2025',
    hotIndex: 9856
  },
  {
    title: '云计算技术在企业数字化转型中的作用',
    authors: '赵六, 钱七',
    source: '万方',
    year: '2025',
    hotIndex: 8423
  },
  {
    title: '物联网技术在智慧城市建设中的应用研究',
    authors: '孙八, 周九',
    source: '维普',
    year: '2024',
    hotIndex: 7654
  },
  {
    title: '机器学习在金融风控中的创新实践',
    authors: '吴十, 郑一',
    source: 'IEEE',
    year: '2024',
    hotIndex: 6543
  },
  {
    title: '边缘计算在自动驾驶中的应用探索',
    authors: '冯二, 陈三',
    source: 'arXiv',
    year: '2024',
    hotIndex: 5987
  },
  {
    title: '量子计算技术最新进展与前景展望',
    authors: '林四, 杨五',
    source: 'Nature',
    year: '2024',
    hotIndex: 5432
  }
]);

// 讨论标签
const discussionTags = [
  { label: '全部', value: 'all' },
  { label: '论文互助', value: 'help' },
  { label: '学术前沿', value: 'frontier' },
  { label: '科研方法', value: 'method' },
  { label: '就业交流', value: 'career' }
];

// 学术交流讨论
const discussions = ref([
  {
    author: '李明同学',
    role: '计算机研二',
    title: '求助：如何快速检索高质量英文文献？',
    content: '最近在写开题报告，需要大量检索英文文献。请问大家有什么好的检索策略或工具推荐吗？',
    time: '2小时前',
    views: 234,
    replies: 18,
    likes: 45
  },
  {
    author: '王华同学',
    role: '经管大三',
    title: '分享一个免费的文献管理工具',
    content: '推荐一款免费开源的文献管理工具 Zotero，可以自动抓取论文信息，还支持多设备同步。',
    time: '5小时前',
    views: 567,
    replies: 32,
    likes: 128
  },
  {
    author: '张伟教授',
    role: '特邀嘉宾',
    title: '【学术前沿】大语言模型在科研中的应用',
    content: '近期大语言模型（如GPT-4）在学术研究中的应用越来越广泛，今天来和大家探讨一下如何在科研中合理使用AI工具。',
    time: '1天前',
    views: 1234,
    replies: 89,
    likes: 356
  }
]);

// 来源颜色映射
const sourceClassMap: Record<string, string> = {
  '知网': 'bg-red-500',
  '万方': 'bg-blue-500',
  '维普': 'bg-green-500',
  'arXiv': 'bg-orange-500',
  'IEEE': 'bg-purple-500',
  'PubMed': 'bg-teal-500',
  'Nature': 'bg-gray-700'
};

const getSourceTagClass = (source: string) => {
  return sourceClassMap[source] || 'bg-gray-500';
};

const getSourceAbbr = (source: string) => {
  const abbrMap: Record<string, string> = {
    '知网': 'CNKI',
    '万方': 'WF',
    '维普': 'VIP',
    'arXiv': 'arXiv',
    'IEEE': 'IEEE',
    'PubMed': 'PubMed',
    'Nature': 'Nature'
  };
  return abbrMap[source] || source.slice(0, 4);
};

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
  searchForm.title = '';
  searchForm.author = '';
  searchForm.keywords = '';
  searchForm.source = '';
  search();
};

// 下载
const downloadPaper = (paper: Paper) => {
  try {
    console.log('下载论文:', paper);
    if (!paper || !paper.title) {
      ElMessage.error('论文信息不完整');
      return;
    }
    ElMessage({ message: `下载论文《${paper.title}》`, type: 'info', duration: 2000 });
  } catch (e) {
    console.error('下载失败:', e);
    ElMessage.error('下载失败，请重试');
  }
};

// 收藏
const collectPaper = (paper: Paper) => {
  if (!favorites.value.find(f => f.id === paper.id)) {
    favorites.value.push(paper);
    ElMessage({ message: '收藏成功', type: 'success', duration: 1500 });
  } else {
    ElMessage({ message: '已在收藏夹中', type: 'warning', duration: 1500 });
  }
};

// 移除收藏
const removeFavorite = (item: Paper) => {
  favorites.value = favorites.value.filter(f => f.id !== item.id);
  ElMessage({ message: '已移除收藏', type: 'info', duration: 1500 });
};

// 详情
const viewDetail = (paper: Paper) => {
  selectedPaper.value = paper;
  detailDialogVisible.value = true;
};

// 弹窗内下载
const handleDialogDownload = () => {
  if (selectedPaper.value) {
    downloadPaper(selectedPaper.value);
  }
};

// 弹窗内收藏
const handleDialogCollect = () => {
  if (selectedPaper.value) {
    collectPaper(selectedPaper.value);
  }
};

// 弹窗内复制链接
const handleCopyLink = () => {
  if (selectedPaper.value?.doi) {
    navigator.clipboard.writeText(`https://doi.org/${selectedPaper.value.doi}`).then(() => {
      ElMessage.success('链接已复制到剪贴板');
    });
  }
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
:deep(.journal-tabs .el-tabs__item) {
  font-size: 15px;
  padding: 0 24px;
  height: 50px;
  line-height: 50px;
}

:deep(.journal-tabs .el-tabs__item.is-active) {
  color: #3b82f6;
  font-weight: 600;
}

:deep(.journal-tabs .el-tabs__active-bar) {
  height: 3px;
  border-radius: 3px;
}

:deep(.journal-tabs .el-tabs__nav-wrap::after) {
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
.journal-detail-dialog :deep(.el-dialog__body) {
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
