<template>
  <div class="min-h-screen bg-gray-50 flex flex-col font-sans">
    <Header />

    <main class="max-w-4xl mx-auto w-full px-5 py-6 space-y-5">
      <!-- 页面标题 -->
      <div class="flex items-center gap-3">
        <el-button text @click="router.back()">
          <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <h1 class="text-xl font-semibold text-gray-800">系统设置</h1>
      </div>

      <!-- 设置内容 -->
      <div class="bg-white rounded-lg border border-gray-200 divide-y divide-gray-100">
        <!-- 头像与基本信息 -->
        <div class="p-5">
          <div class="flex items-center gap-5">
            <el-avatar :size="72" :src="userInfo.avatar" />
            <div class="flex-grow">
              <div class="font-medium text-gray-800 mb-1">{{ userInfo.name }}</div>
              <div class="text-gray-500 text-sm mb-2">{{ userInfo.email }}</div>
              <el-button size="small" @click="showAvatarDialog = true">更换头像</el-button>
            </div>
          </div>
        </div>

        <!-- 个人信息 -->
        <div class="p-5">
          <div class="flex justify-between items-center mb-4">
            <h3 class="font-medium text-gray-800">个人信息</h3>
            <el-button size="small" type="primary" @click="showProfileDialog = true">编辑</el-button>
          </div>
          <div class="grid grid-cols-2 gap-4 text-sm">
            <div>
              <span class="text-gray-400">姓名</span>
              <div class="text-gray-800 mt-0.5">{{ userInfo.name }}</div>
            </div>
            <div>
              <span class="text-gray-400">学号</span>
              <div class="text-gray-800 mt-0.5">{{ userInfo.studentId }}</div>
            </div>
            <div>
              <span class="text-gray-400">学院</span>
              <div class="text-gray-800 mt-0.5">{{ userInfo.department }}</div>
            </div>
            <div>
              <span class="text-gray-400">班级</span>
              <div class="text-gray-800 mt-0.5">{{ userInfo.className }}</div>
            </div>
          </div>
        </div>

        <!-- 账号安全 -->
        <div class="p-5">
          <h3 class="font-medium text-gray-800 mb-4">账号安全</h3>
          <div class="space-y-3">
            <div class="flex justify-between items-center py-2 border-b border-gray-50">
              <div>
                <div class="text-sm text-gray-800">登录密码</div>
                <div class="text-xs text-gray-400">定期更换密码可保护账号安全</div>
              </div>
              <el-button size="small" @click="showPasswordDialog = true">修改</el-button>
            </div>
            <div class="flex justify-between items-center py-2 border-b border-gray-50">
              <div>
                <div class="text-sm text-gray-800">绑定邮箱</div>
                <div class="text-xs text-gray-400">{{ userInfo.email }}</div>
              </div>
              <el-button size="small" type="primary" plain>更换</el-button>
            </div>
            <div class="flex justify-between items-center py-2">
              <div>
                <div class="text-sm text-gray-800">绑定手机</div>
                <div class="text-xs text-gray-400">{{ userInfo.phone || '未绑定' }}</div>
              </div>
              <el-button size="small" type="primary" plain>绑定</el-button>
            </div>
          </div>
        </div>

        <!-- 通知设置 -->
        <div class="p-5">
          <h3 class="font-medium text-gray-800 mb-4">通知设置</h3>
          <div class="space-y-3">
            <div class="flex justify-between items-center">
              <div>
                <div class="text-sm text-gray-800">系统通知</div>
                <div class="text-xs text-gray-400">接收系统公告和重要通知</div>
              </div>
              <el-switch v-model="notifySettings.system" />
            </div>
            <div class="flex justify-between items-center">
              <div>
                <div class="text-sm text-gray-800">借阅提醒</div>
                <div class="text-xs text-gray-400">图书即将到期时发送提醒</div>
              </div>
              <el-switch v-model="notifySettings.borrow" />
            </div>
            <div class="flex justify-between items-center">
              <div>
                <div class="text-sm text-gray-800">成绩通知</div>
                <div class="text-xs text-gray-400">新成绩发布时通知</div>
              </div>
              <el-switch v-model="notifySettings.grade" />
            </div>
            <div class="flex justify-between items-center">
              <div>
                <div class="text-sm text-gray-800">消息推送</div>
                <div class="text-xs text-gray-400">站内信和私信通知</div>
              </div>
              <el-switch v-model="notifySettings.message" />
            </div>
          </div>
        </div>

        <!-- 关于 -->
        <div class="p-5">
          <h3 class="font-medium text-gray-800 mb-4">关于</h3>
          <div class="space-y-3 text-sm">
            <div class="flex justify-between items-center">
              <span class="text-gray-400">当前版本</span>
              <span class="text-gray-800">v1.0.0</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-400">检查更新</span>
              <el-button size="small" link type="primary">已是最新版本</el-button>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-400">用户协议</span>
              <el-button size="small" link type="primary">查看</el-button>
            </div>
            <div class="flex justify-between items-center">
              <span class="text-gray-400">隐私政策</span>
              <el-button size="small" link type="primary">查看</el-button>
            </div>
          </div>
        </div>

        <!-- 退出登录 -->
        <div class="p-5">
          <el-button type="danger" plain class="w-full" @click="handleLogout">退出登录</el-button>
        </div>
      </div>
    </main>

    <Footer />

    <!-- 修改密码弹窗 -->
    <el-dialog v-model="showPasswordDialog" title="修改密码" width="400px" destroy-on-close>
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-position="top">
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password placeholder="请输入当前密码" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="handleChangePassword">确认修改</el-button>
      </template>
    </el-dialog>

    <!-- 编辑资料弹窗 -->
    <el-dialog v-model="showProfileDialog" title="编辑资料" width="450px" destroy-on-close>
      <el-form :model="profileForm" label-position="top">
        <el-form-item label="姓名">
          <el-input v-model="profileForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="个性签名">
          <el-input v-model="profileForm.signature" type="textarea" :rows="3" placeholder="介绍一下自己" />
        </el-form-item>
        <el-form-item label="学院">
          <el-select v-model="profileForm.department" placeholder="请选择学院" class="w-full">
            <el-option label="软件工程学院" value="软件工程学院" />
            <el-option label="计算机科学与技术学院" value="计算机科学与技术学院" />
            <el-option label="信息工程学院" value="信息工程学院" />
            <el-option label="理学院" value="理学院" />
            <el-option label="文学院" value="文学院" />
          </el-select>
        </el-form-item>
        <el-form-item label="班级">
          <el-input v-model="profileForm.className" placeholder="请输入班级" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showProfileDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSaveProfile">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store/user'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 用户信息
const userInfo = reactive({
  name: '李明',
  studentId: '2021001',
  department: '软件工程学院',
  className: '2021级 3班',
  email: 'liming@college.edu',
  phone: '138****8888',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  signature: '热爱编程，享受代码的乐趣'
})

// 通知设置
const notifySettings = reactive({
  system: true,
  borrow: true,
  grade: true,
  message: true
})

// 弹窗状态
const showPasswordDialog = ref(false)
const showProfileDialog = ref(false)
const showAvatarDialog = ref(false)

// 修改密码表单
const passwordFormRef = ref()
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 编辑资料表单
const profileForm = reactive({
  name: '',
  signature: '',
  department: '',
  className: ''
})

// 修改密码
const handleChangePassword = async () => {
  try {
    await passwordFormRef.value.validate()
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    ElMessage.success('密码修改成功')
    showPasswordDialog.value = false
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (e) {
    // 验证失败
  }
}

// 保存资料
const handleSaveProfile = async () => {
  try {
    await new Promise(resolve => setTimeout(resolve, 500))
    Object.assign(userInfo, profileForm)
    ElMessage.success('资料保存成功')
    showProfileDialog.value = false
  } catch (e) {
    ElMessage.error('保存失败')
  }
}

// 退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    userStore.clearSession()
    router.push('/login')
    ElMessage.success('已退出登录')
  } catch (e) {
    // 取消
  }
}

// 初始化
onMounted(() => {
  document.body.classList.add('fade-in')
  profileForm.name = userInfo.name
  profileForm.signature = userInfo.signature
  profileForm.department = userInfo.department
  profileForm.className = userInfo.className
})
</script>

<style scoped>
:deep(.el-switch) {
  --el-switch-off-color: #d1d5db;
}
</style>
