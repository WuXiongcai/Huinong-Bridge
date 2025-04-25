<template>
  <div class="main-content">
    <el-card class="user-card">
      <div class="user-header">
        <h2 class="user-title">个人中心</h2>
        <div class="user-actions">
          <el-button type="primary" icon="el-icon-lock" @click="updatePassword">修改密码</el-button>
          <el-button type="success" icon="el-icon-wallet" @click="rechargeInit">充 值</el-button>
        </div>
      </div>

      <el-row :gutter="20">
        <el-col :xs="24" :sm="8" :md="6" :lg="6" :xl="6">
          <div class="avatar-container">
            <el-upload
                class="avatar-uploader"
                :action="$baseUrl + '/files/upload'"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
            >
              <img v-if="user.avatar" :src="user.avatar" class="avatar" alt="用户头像" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <p class="avatar-hint">点击更换头像</p>
          </div>
          <div class="user-summary">
            <h3>{{ user.name || user.username }}</h3>
            <p>{{ user.level }}</p>
            <el-tag type="success">余额: ¥{{ user.account }}</el-tag>
          </div>
        </el-col>

        <el-col :xs="24" :sm="16" :md="18" :lg="18" :xl="18">
          <el-form :model="user" label-width="80px" class="user-form">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="user.username" placeholder="用户名" disabled></el-input>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="user.name" placeholder="姓名"></el-input>
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input v-model="user.phone" placeholder="电话"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="user.email" placeholder="邮箱"></el-input>
            </el-form-item>
            <el-form-item label="身份" prop="level">
              <el-input v-model="user.level" disabled></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-check" @click="update">保存信息</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>

      <el-divider content-position="center">最近活动</el-divider>

      <el-timeline>
        <el-timeline-item
            v-for="(activity, index) in recentActivities"
            :key="index"
            :type="activity.type"
            :color="activity.color"
            :timestamp="activity.timestamp"
        >
          {{ activity.content }}
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <el-dialog title="修改密码" :visible.sync="dialogVisible" width="30%" :close-on-click-modal="false" destroy-on-close custom-class="custom-dialog">
      <el-form :model="user" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="user.password" placeholder="请输入原始密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="user.newPassword" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input show-password v-model="user.confirmPassword" placeholder="请再次输入新密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="账户充值" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close custom-class="custom-dialog">
      <el-form label-width="100px">
        <el-form-item prop="account" label="充值金额">
          <el-input-number v-model="account" :min="1" :max="10000" :step="100"></el-input-number>
        </el-form-item>
        <el-form-item prop="type" label="支付方式">
          <el-radio-group v-model="type">
            <el-radio label="支付宝">
              <i class="el-icon-money" style="color: #1296db;"></i> 支付宝
            </el-radio>
            <el-radio label="微信">
              <i class="el-icon-chat-dot-round" style="color: #67c23a;"></i> 微信
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="recharge">确认充值</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('确认密码错误'))
      } else {
        callback()
      }
    }
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      dialogVisible: false,
      fromVisible: false,
      account: 100,
      type: '支付宝',
      rules: {
        password: [
          { required: true, message: '请输入原始密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      },
      recentActivities: [
        { content: '完成了扶贫项目申请', timestamp: '2024-11-20 18:30', type: 'primary', color: '#0bbd87' },
        { content: '账户充值 ¥100', timestamp: '2024-11-18 09:15', type: 'success', color: '#1296db' },
        { content: '更新了个人信息', timestamp: '2024-11-20 14:20', type: 'info', color: '#e6a23c' },
        { content: '首次登录系统', timestamp: '2024-11-21 10:00', type: 'warning', color: '#f56c6c' },
      ]
    }
  },
  created() {
    this.getPerson()
  },
  methods: {
    getPerson() {
      this.$request.get('/user/selectById/' + this.user.id).then(res => {
        if (res.code === '200') {
          this.user = res.data
          localStorage.setItem("xm-user", JSON.stringify(this.user))
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    rechargeInit() {
      this.account = 100
      this.fromVisible = true
    },
    recharge() {
      this.user.account = parseFloat(this.user.account) + parseFloat(this.account)
      this.$request.put('/user/update', this.user).then(res => {
        if (res.code === '200') {
          this.$message.success('充值成功')
          localStorage.setItem("xm-user", JSON.stringify(this.user))
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    update() {
      this.$request.put('/user/update', this.user).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          localStorage.setItem('xm-user', JSON.stringify(this.user))
          this.$emit('update:user')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(response, file, fileList) {
      this.$set(this.user, 'avatar', response.data)
    },
    updatePassword() {
      this.dialogVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request.put('/updatePassword', this.user).then(res => {
            if (res.code === '200') {
              this.$message.success('修改密码成功')
              this.$router.push('/login')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/theme/front_css/person.css";
</style>