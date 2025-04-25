<template>
  <div class="container">
    <div class="register-section">
      <div class="register-card">
        <div class="logo-container">
          <img src="../assets/imgs/logo1.png" alt="惠农云桥 Logo" class="logo">
          <h1 class="title">惠农云桥</h1>
          <h2 class="subtitle">惠州市龙门县乡村旅游与助农平台</h2>
        </div>
        <el-form :model="form" :rules="rules" ref="formRef" class="register-form">
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPass">
            <el-input prefix-icon="el-icon-lock" placeholder="请确认密码" show-password v-model="form.confirmPass"></el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input prefix-icon="el-icon-message" placeholder="请输入邮箱" v-model="form.email"></el-input>
          </el-form-item>
          <el-form-item prop="phone">
            <el-input prefix-icon="el-icon-mobile-phone" placeholder="请输入手机号" v-model="form.phone"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="register-button" @click="register">注 册</el-button>
          </el-form-item>
          <div class="form-footer">
            <el-checkbox v-model="agreeTerms">我已阅读并同意<a href="#" @click.prevent="showTerms">服务条款</a></el-checkbox>
          </div>
        </el-form>
        <div class="login-link">
          已有账号？<a href="/login">立即登录</a>
        </div>
      </div>
    </div>

    <el-dialog title="服务条款" :visible.sync="termsVisible" width="50%">
      <div class="terms-content">
        <h3>惠农云桥 - 惠州市龙门县乡村旅游与助农平台服务条款</h3>
        <p>欢迎使用惠农云桥惠州市龙门县乡村旅游与助农平台。本平台致力于连接农户、志愿者和企业，促进农村发展和扶贫工作。使用本平台即表示您同意以下条款：</p>
        <ol>
          <li>用户信息：您提供的所有信息必须真实、准确。</li>
          <li>账户安全：请妥善保管您的账户信息，对账户活动负责。</li>
          <li>合法使用：禁止使用平台进行任何非法活动。</li>
          <li>知识产权：平台内容的知识产权归平台所有。</li>
          <li>隐私保护：我们将按照隐私政策保护您的个人信息。</li>
        </ol>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="termsVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    const validatePassword = (rule, confirmPass, callback) => {
      if (confirmPass === '') {
        callback(new Error('请确认密码'))
      } else if (confirmPass !== this.form.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    const validateEmail = (rule, value, callback) => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (value === '') {
        callback(new Error('请输入邮箱'))
      } else if (!emailRegex.test(value)) {
        callback(new Error('请输入有效的邮箱地址'))
      } else {
        callback()
      }
    }
    const validatePhone = (rule, value, callback) => {
      const phoneRegex = /^1[3-9]\d{9}$/
      if (value === '') {
        callback(new Error('请输入手机号'))
      } else if (!phoneRegex.test(value)) {
        callback(new Error('请输入有效的手机号'))
      } else {
        callback()
      }
    }
    return {
      form: { role: 'USER', userType: ''},
      agreeTerms: false,
      termsVisible: false,
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能小于 6 个字符', trigger: 'blur' }
        ],
        confirmPass: [
          { validator: validatePassword, trigger: 'blur' }
        ],
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ],
        phone: [
          { validator: validatePhone, trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    register() {
      if (!this.agreeTerms) {
        this.$message.warning('请阅读并同意服务条款')
        return
      }
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/register', this.form).then(res => {
            if (res.code === '200') {
              this.$router.push('/login')  // 跳转登录页面
              this.$message.success('注册成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    showTerms() {
      this.termsVisible = true
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  display: flex;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: cover;
  background-position: center;
  color: #333;
}

.register-section {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.register-card {
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

.register-form {
  margin-top: 20px;
}

.register-button {
  width: 100%;
  background-color: #28826b;
  border-color: #28826b;
}

.form-footer {
  margin-top: 15px;
  text-align: center;
}

.login-link {
  text-align: center;
  margin-top: 20px;
}


.info-item i {
  font-size: 36px;
  margin-bottom: 10px;
}

.terms-content {
  max-height: 300px;
  overflow-y: auto;
}

@media (min-width: 768px) {
  .container {
    flex-direction: row;
  }

  .register-section {
    width: 50%;
  }

  .info-section {
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
</style>