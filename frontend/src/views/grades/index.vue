<template>
  <div class="min-h-screen bg-gray-50 flex flex-col font-sans">
    <!-- 引入头部组件 -->
    <Header />

    <!-- 主体内容 -->
    <main class="max-w-7xl mx-auto w-full px-4 py-6 space-y-6">

      <!-- 页面标题区 -->
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-2xl font-bold text-gray-800">教务成绩</h1>
          <p class="text-gray-500 text-sm mt-1">学分绩点统计与学业分析</p>
        </div>
        <el-button type="primary" class="rounded-lg">
          <el-icon class="mr-2"><Download /></el-icon>导出成绩单
        </el-button>
      </div>

      <!-- 绩点统计卡片 -->
      <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
        <div
          v-for="(stat, index) in stats"
          :key="index"
          class="bg-white rounded-xl border border-gray-100 p-4 transition-all duration-300 hover:shadow-lg cursor-pointer group"
          :class="stat.borderClass"
        >
          <div class="flex items-start justify-between">
            <div>
              <p class="text-gray-500 text-sm mb-1">{{ stat.label }}</p>
              <p class="text-2xl font-bold" :class="stat.valueClass">{{ stat.value }}</p>
            </div>
            <div :class="[stat.iconBg, 'p-2 rounded-lg transition-transform duration-300 group-hover:scale-110']">
              <el-icon :class="stat.iconClass" class="text-xl">
                <component :is="stat.icon" />
              </el-icon>
            </div>
          </div>
          <p class="text-xs mt-2" :class="stat.subClass">{{ stat.sub }}</p>
        </div>
      </div>

      <!-- 标签页切换 -->
      <el-card shadow="never" class="border border-gray-100 rounded-xl">
        <el-tabs v-model="activeTab" class="grade-tabs">
          <el-tab-pane label="成绩单" name="list">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><Document /></el-icon>
                成绩单
              </span>
            </template>

            <!-- 学期和类型选择 -->
            <div class="bg-gray-50 rounded-xl p-4 mb-4">
              <el-form :model="filterForm" layout="inline" size="large" class="filter-form">
                <el-form-item label="学期" class="filter-item">
                  <el-select v-model="filterForm.term" placeholder="选择学期" class="filter-select" @change="changeTerm">
                    <el-option label="2025-2026 学年春季学期" value="2026-spring" />
                    <el-option label="2025-2026 学年秋季学期" value="2025-fall" />
                    <el-option label="2024-2025 学年春季学期" value="2025-spring" />
                    <el-option label="2024-2025 学年秋季学期" value="2024-fall" />
                  </el-select>
                </el-form-item>
                <el-form-item label="课程类型" class="filter-item">
                  <el-select v-model="filterForm.type" placeholder="选择类型" class="filter-select" @change="changeType">
                    <el-option label="全部课程" value="all" />
                    <el-option label="必修课" value="required" />
                    <el-option label="选修课" value="elective" />
                    <el-option label="公选课" value="public" />
                  </el-select>
                </el-form-item>
                <el-form-item class="filter-actions">
                  <el-button type="primary" class="rounded-lg" @click="search">
                    <el-icon class="mr-2"><Search /></el-icon>筛选
                  </el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 加载状态 -->
            <div v-if="loading" class="space-y-4">
              <el-skeleton v-for="i in 5" :key="i" animated class="bg-white rounded-xl p-4" />
            </div>

            <!-- 成绩列表 -->
            <div v-else class="space-y-3">
              <div
                v-for="(grade, index) in grades"
                :key="index"
                class="bg-white rounded-xl border border-gray-100 p-4 transition-all duration-300 hover:shadow-md hover:border-blue-200 cursor-pointer"
                @click="viewDetail(grade)"
              >
                <div class="flex flex-col lg:flex-row lg:items-center gap-4">
                  <!-- 课程信息 -->
                  <div class="flex-grow">
                    <div class="flex items-center gap-3 mb-2">
                      <h3 class="font-bold text-gray-800 text-lg">{{ grade.courseName }}</h3>
                      <el-tag :type="grade.type === 'required' ? 'primary' : grade.type === 'elective' ? 'success' : 'warning'" size="small">
                        {{ grade.type === 'required' ? '必修' : grade.type === 'elective' ? '选修' : '公选' }}
                      </el-tag>
                      <el-tag v-if="grade.score < 60" type="danger" size="small" class="ml-2">
                        <el-icon class="mr-1"><Warning /></el-icon>未通过
                      </el-tag>
                    </div>
                    <div class="flex flex-wrap gap-4 text-sm text-gray-500">
                      <span class="flex items-center gap-1">
                        <el-icon><Document /></el-icon>
                        {{ grade.courseCode }}
                      </span>
                      <span class="flex items-center gap-1">
                        <el-icon><User /></el-icon>
                        {{ grade.teacher }}
                      </span>
                      <span class="flex items-center gap-1">
                        <el-icon><Clock /></el-icon>
                        {{ grade.credit }} 学分
                      </span>
                    </div>
                  </div>

                  <!-- 成绩显示 -->
                  <div class="flex items-center gap-6">
                    <!-- 分数圆环 -->
                    <div class="relative w-16 h-16 flex-shrink-0">
                      <el-progress
                        type="circle"
                        :percentage="grade.score"
                        :color="getScoreColor(grade.score)"
                        :width="60"
                        :stroke-width="6"
                      />
                    </div>

                    <!-- 绩点 -->
                    <div class="text-center">
                      <p class="text-2xl font-bold" :class="getScoreTextClass(grade.score)">{{ grade.gradePoint }}</p>
                      <p class="text-xs text-gray-500">绩点</p>
                    </div>

                    <!-- 状态 -->
                    <div class="text-center min-w-[60px]">
                      <el-tag :type="grade.status === 'passed' ? 'success' : 'danger'" size="small">
                        {{ grade.status === 'passed' ? '已通过' : '未通过' }}
                      </el-tag>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 分页 -->
            <div v-if="!loading" class="mt-4 flex justify-end">
              <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[5, 10, 20, 50]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              />
            </div>
          </el-tab-pane>

          <el-tab-pane label="成绩分析" name="analysis">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><DataAnalysis /></el-icon>
                成绩分析
              </span>
            </template>

            <!-- 分析图表区域 -->
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <!-- 成绩分布 -->
              <div class="bg-white rounded-xl border border-gray-100 p-5 transition-all duration-300 hover:shadow-md">
                <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                  <el-icon class="mr-2 text-blue-600"><PieChart /></el-icon>
                  成绩分布
                </h3>
                <div class="h-72 flex items-center justify-center">
                  <el-empty v-if="loading" description="加载中..." />
                  <div v-else class="w-full h-full flex items-center justify-center bg-gray-50 rounded-xl">
                    <div class="text-center text-gray-400">
                      <el-icon class="text-6xl mb-2"><PieChart /></el-icon>
                      <p class="text-sm">成绩分布图表</p>
                      <p class="text-xs">（集成 ECharts）</p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 绩点趋势 -->
              <div class="bg-white rounded-xl border border-gray-100 p-5 transition-all duration-300 hover:shadow-md">
                <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                  <el-icon class="mr-2 text-green-600"><TrendCharts /></el-icon>
                  绩点趋势
                </h3>
                <div class="h-72 flex items-center justify-center">
                  <el-empty v-if="loading" description="加载中..." />
                  <div v-else class="w-full h-full flex items-center justify-center bg-gray-50 rounded-xl">
                    <div class="text-center text-gray-400">
                      <el-icon class="text-6xl mb-2"><TrendCharts /></el-icon>
                      <p class="text-sm">绩点趋势图表</p>
                      <p class="text-xs">（集成 ECharts）</p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 各科成绩雷达图 -->
              <div class="bg-white rounded-xl border border-gray-100 p-5 transition-all duration-300 hover:shadow-md">
                <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                  <el-icon class="mr-2 text-purple-600"><Grid /></el-icon>
                  各科成绩雷达
                </h3>
                <div class="h-72 flex items-center justify-center">
                  <el-empty v-if="loading" description="加载中..." />
                  <div v-else class="w-full h-full flex items-center justify-center bg-gray-50 rounded-xl">
                    <div class="text-center text-gray-400">
                      <el-icon class="text-6xl mb-2"><Grid /></el-icon>
                      <p class="text-sm">雷达分析图表</p>
                      <p class="text-xs">（集成 ECharts）</p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 学情概览 -->
              <div class="bg-white rounded-xl border border-gray-100 p-5 transition-all duration-300 hover:shadow-md">
                <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                  <el-icon class="mr-2 text-orange-600"><Histogram /></el-icon>
                  学情概览
                </h3>
                <div class="space-y-4">
                  <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                    <span class="text-gray-600">必修课通过率</span>
                    <div class="flex items-center gap-3">
                      <el-progress :percentage="94" :stroke-width="8" class="w-32" />
                      <span class="text-sm font-medium text-green-600">94%</span>
                    </div>
                  </div>
                  <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                    <span class="text-gray-600">选修课通过率</span>
                    <div class="flex items-center gap-3">
                      <el-progress :percentage="100" :stroke-width="8" class="w-32" />
                      <span class="text-sm font-medium text-green-600">100%</span>
                    </div>
                  </div>
                  <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                    <span class="text-gray-600">平均绩点</span>
                    <div class="flex items-center gap-3">
                      <el-progress :percentage="86" :stroke-width="8" class="w-32" color="#3b82f6" />
                      <span class="text-sm font-medium text-blue-600">3.42</span>
                    </div>
                  </div>
                  <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                    <span class="text-gray-600">学分完成度</span>
                    <div class="flex items-center gap-3">
                      <el-progress :percentage="91" :stroke-width="8" class="w-32" color="#10b981" />
                      <span class="text-sm font-medium text-emerald-600">128/140</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="挂科预警" name="warning">
            <template #label>
              <span class="flex items-center gap-2">
                <el-icon><Warning /></el-icon>
                挂科预警
                <el-badge :value="warnings.length" :max="99" type="danger" class="badge" v-if="warnings.length > 0" />
              </span>
            </template>

            <!-- 预警列表 -->
            <div v-if="warnings.length > 0" class="space-y-4">
              <div
                v-for="(warn, index) in warnings"
                :key="index"
                class="bg-red-50 rounded-xl border border-red-200 p-4 transition-all duration-300 hover:shadow-md"
              >
                <div class="flex items-start gap-4">
                  <div class="p-2 bg-red-100 rounded-lg">
                    <el-icon class="text-red-500 text-xl"><Warning /></el-icon>
                  </div>
                  <div class="flex-grow">
                    <h4 class="font-bold text-gray-800">{{ warn.courseName }}</h4>
                    <p class="text-sm text-gray-600 mt-1">
                      当前成绩：<span class="text-red-500 font-bold">{{ warn.score }}分</span>，
                      距离及格线还差 {{ 60 - warn.score }} 分
                    </p>
                    <div class="flex gap-4 mt-3 text-xs text-gray-500">
                      <span>课程代码：{{ warn.courseCode }}</span>
                      <span>学分：{{ warn.credit }}</span>
                      <span>学期：{{ warn.term }}</span>
                    </div>
                    <div class="mt-3">
                      <el-tag type="danger" size="small">需重修</el-tag>
                      <el-button type="primary" size="small" class="ml-3 rounded-lg">查看重修方案</el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 无预警 -->
            <el-empty v-else description="暂无挂科记录，继续保持！" class="my-8">
              <template #image>
                <el-icon class="text-6xl text-green-400"><SuccessFilled /></el-icon>
              </template>
              <el-button type="primary" class="rounded-lg" @click="activeTab = 'list'">查看成绩单</el-button>
            </el-empty>
          </el-tab-pane>
        </el-tabs>
      </el-card>
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
import {
  Download, Document, Search, Clock, User, Warning,
  DataAnalysis, PieChart, TrendCharts, Grid, Histogram,
  SuccessFilled, Trophy, Timer, Reading, Star
} from '@element-plus/icons-vue';

// 标签页
const activeTab = ref('list');

// 加载状态
const loading = ref(false);

// 筛选表单
const filterForm = reactive({
  term: '2026-spring',
  type: 'all'
});

// 分页
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(50);

// 统计卡片
const stats = [
  {
    label: '当前学期绩点',
    value: '3.85',
    sub: '较上学期提升 0.15',
    icon: Trophy,
    iconBg: 'bg-yellow-50',
    iconClass: 'text-yellow-500',
    valueClass: 'text-gray-800',
    subClass: 'text-green-500',
    borderClass: 'border-l-4 border-l-yellow-400'
  },
  {
    label: '累计平均绩点',
    value: '3.72',
    sub: '班级排名前 5%',
    icon: Star,
    iconBg: 'bg-blue-50',
    iconClass: 'text-blue-500',
    valueClass: 'text-gray-800',
    subClass: 'text-blue-500',
    borderClass: 'border-l-4 border-l-blue-400'
  },
  {
    label: '已修学分',
    value: '128',
    sub: '总学分要求：140',
    icon: Reading,
    iconBg: 'bg-green-50',
    iconClass: 'text-green-500',
    valueClass: 'text-gray-800',
    subClass: 'text-gray-500',
    borderClass: 'border-l-4 border-l-green-400'
  },
  {
    label: '本学期课程',
    value: '6',
    sub: '已完成：4 / 6',
    icon: Timer,
    iconBg: 'bg-purple-50',
    iconClass: 'text-purple-500',
    valueClass: 'text-gray-800',
    subClass: 'text-gray-500',
    borderClass: 'border-l-4 border-l-purple-400'
  }
];

// 成绩列表
const grades = ref([
  {
    courseName: '高等数学（下）',
    courseCode: 'MATH102',
    credit: 4,
    type: 'required',
    score: 92,
    gradePoint: 4.0,
    teacher: '李明教授',
    status: 'passed'
  },
  {
    courseName: '大学英语（四级）',
    courseCode: 'ENGL104',
    credit: 3,
    type: 'required',
    score: 88,
    gradePoint: 3.5,
    teacher: '王芳副教授',
    status: 'passed'
  },
  {
    courseName: '数据结构与算法',
    courseCode: 'CS201',
    credit: 4,
    type: 'required',
    score: 95,
    gradePoint: 4.0,
    teacher: '张伟教授',
    status: 'passed'
  },
  {
    courseName: '操作系统原理',
    courseCode: 'CS202',
    credit: 4,
    type: 'required',
    score: 85,
    gradePoint: 3.5,
    teacher: '刘强副教授',
    status: 'passed'
  },
  {
    courseName: '人工智能导论',
    courseCode: 'CS301',
    credit: 3,
    type: 'elective',
    score: 90,
    gradePoint: 4.0,
    teacher: '陈华教授',
    status: 'passed'
  },
  {
    courseName: '计算机网络',
    courseCode: 'CS203',
    credit: 3,
    type: 'required',
    score: 45,
    gradePoint: 0,
    teacher: '赵军副教授',
    status: 'failed'
  }
]);

// 挂科预警
const warnings = ref([
  {
    courseName: '计算机网络',
    courseCode: 'CS203',
    credit: 3,
    score: 45,
    term: '2025-2026 春季'
  }
]);

// 获取分数颜色
const getScoreColor = (score: number) => {
  if (score >= 90) return '#10b981';
  if (score >= 80) return '#3b82f6';
  if (score >= 70) return '#f59e0b';
  if (score >= 60) return '#ef4444';
  return '#dc2626';
};

// 获取分数文字颜色
const getScoreTextClass = (score: number) => {
  if (score >= 90) return 'text-emerald-500';
  if (score >= 80) return 'text-blue-500';
  if (score >= 70) return 'text-amber-500';
  if (score >= 60) return 'text-orange-500';
  return 'text-red-500';
};

// 筛选
const search = () => {
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
    ElMessage({ message: '筛选完成', type: 'success', duration: 1500 });
  }, 600);
};

// 切换学期
const changeTerm = () => {
  loading.value = true;
  setTimeout(() => { loading.value = false; }, 600);
};

// 切换类型
const changeType = () => {
  loading.value = true;
  setTimeout(() => { loading.value = false; }, 600);
};

// 查看详情
const viewDetail = (grade: any) => {
  ElMessage({ message: `查看《${grade.courseName}》详情`, type: 'info', duration: 1500 });
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
:deep(.grade-tabs .el-tabs__item) {
  font-size: 15px;
  padding: 0 24px;
  height: 50px;
  line-height: 50px;
}

:deep(.grade-tabs .el-tabs__item.is-active) {
  color: #3b82f6;
  font-weight: 600;
}

:deep(.grade-tabs .el-tabs__active-bar) {
  height: 3px;
  border-radius: 3px;
}

:deep(.grade-tabs .el-tabs__nav-wrap::after) {
  height: 1px;
}

/* 筛选表单 */
.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: flex-end;
}

.filter-item {
  margin-right: 0;
}

.filter-select {
  width: 200px;
}

.filter-actions {
  margin-left: auto;
}

/* 徽章 */
.badge {
  margin-left: 4px;
}

/* 悬停效果 */
:deep(.el-button) {
  transition: all 0.3s ease;
}

:deep(.el-button:hover) {
  transform: translateY(-1px);
}

:deep(.el-progress-circle__track) {
  stroke: #f3f4f6;
}

/* 响应式 */
@media (max-width: 640px) {
  .filter-form {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-select {
    width: 100%;
  }

  .filter-actions {
    margin-left: 0;
    width: 100%;
  }

  .filter-actions .el-button {
    width: 100%;
  }
}
</style>
