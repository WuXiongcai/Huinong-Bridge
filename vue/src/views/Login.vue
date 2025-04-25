<template>
  <div class="container">
    <!-- 添加管理员登录入口 -->
    <div class="admin-login">
      <el-button type="text" @click="showAdminLogin">
        <i class="el-icon-user-solid"></i> 管理员登录
      </el-button>
    </div>

    <div class="login-section">
      <div class="login-card">
        <div class="logo-container">
          <img src="../assets/imgs/logo1.png" alt="惠农云桥 Logo" class="logo">
          <h1 class="title">惠农云桥</h1>
          <h2 class="subtitle">惠州市龙门县乡村旅游与助农平台</h2>
        </div>
        <el-form :model="form" :rules="rules" ref="formRef" class="login-form">
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="login-button" @click="login">登 录</el-button>
          </el-form-item>
          <div class="form-footer">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
            <router-link to="/forgot-password" class="forgot-password">忘记密码？</router-link>
          </div>
        </el-form>
        <div class="register-link">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </div>
    </div>

    <!-- 管理员登录对话框 -->
    <el-dialog
      title="管理员登录"
      :visible.sync="adminDialogVisible"
      width="400px"
      :close-on-click-modal="false"
      center>
      <el-form :model="adminForm" :rules="rules" ref="adminFormRef">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" placeholder="请输入管理员账号" v-model="adminForm.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password v-model="adminForm.password"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="adminDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="adminLogin">登 录</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: {
        role: 'USER' // 默认为用户登录
      },
      adminForm: {
        role: 'ADMIN'
      },
      adminDialogVisible: false,
      rememberMe: false,
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      }
    }
  },
  methods: {
    showAdminLogin() {
      this.adminDialogVisible = true
    },
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))
              location.href = '/front/home'
              this.$message.success('登录成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    adminLogin() {
      this.$refs['adminFormRef'].validate((valid) => {
        if (valid) {
          this.$request.post('/login', this.adminForm).then(res => {
            if (res.code === '200') {
              if (res.data.role === 'ADMIN') {
                localStorage.setItem("xm-admin", JSON.stringify(res.data))
                this.$router.push('/home')
                this.$message.success('管理员登录成功')
              } else {
                this.$message.error('非管理员账号，请使用管理员账号登录')
              }
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
.container {
  min-height: 100vh;
  display: flex;
  background-image: url("@/assets/imgs/bg_xiangcun1.jpg");
  background-size: cover;
  background-position: center;
  color: #333;
}

.login-section {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 30px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.logo-container {
  text-align: center;
  margin-bottom: 20px;
}

.logo {
  width: 80px;
  height: 80px;
  margin-bottom: 10px;
}

.title {
  font-size: 28px;
  color: #28826b;
  margin-bottom: 5px;
}

.subtitle {
  font-size: 18px;
  color: #666;
  margin-bottom: 20px;
}

.login-form {
  margin-top: 20px;
}

.login-button {
  width: 100%;
  background-color: #28826b;
  border-color: #28826b;
}

.form-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}

.forgot-password {
  color: #28826b;
  text-decoration: none;
}

.register-link {
  text-align: center;
  margin-top: 20px;
}


.feature-item i {
  font-size: 36px;
  margin-bottom: 10px;
}

@media (min-width: 768px) {
  .container {
    flex-direction: row;
  }

  .login-section {
    width: 50%;
  }

}

a {
  color: #28826b;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

/* 添加管理员登录按钮样式 */
.admin-login {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 100;
}

.admin-login .el-button {
  color: #fff;
  font-size: 14px;
  padding: 8px 15px;
  background-color: rgba(40, 130, 107, 0.8);
  border-radius: 20px;
}

.admin-login .el-button:hover {
  background-color: rgba(40, 130, 107, 1);
}

/* 适配移动端 */
@media (max-width: 768px) {
  .admin-login {
    top: 10px;
    right: 10px;
  }
  
  .admin-login .el-button {
    font-size: 12px;
    padding: 6px 12px;
  }
}

/* 修改对话框样式 */
.el-dialog {
  border-radius: 8px;
}

.el-dialog__header {
  text-align: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.el-dialog__body {
  padding: 30px 20px;
}

.el-dialog__footer {
  text-align: center;
  padding: 20px;
  border-top: 1px solid #eee;
}
</style>