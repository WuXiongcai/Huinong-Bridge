<template>
  <div class="forgot-password-container">
    <div class="forgot-password-card">
      <div class="logo-container">
        <img src="../assets/imgs/logo1.png" alt="惠农云桥 Logo" class="logo">
        <h1 class="title">惠农云桥</h1>
        <h2 class="subtitle">惠州市龙门县乡村旅游与助农平台</h2>
      </div>
      <h3 class="forgot-password-title">忘记密码</h3>
      <p class="forgot-password-message">
        如果您忘记了密码，请联系管理员重置：
      </p>
      <div class="admin-info">
        <p><strong>管理员姓名：</strong> mizhuo</p>
        <p><strong>联系电话：</strong> 15019816191</p>
      </div>
      <el-button type="primary" class="back-button" @click="goBack">返回登录</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ForgotPassword',
  methods: {
    goBack() {
      this.$router.push('/login');
    }
  }
}
</script>

<style scoped>
.forgot-password-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: cover;
  background-position: center;
}

.forgot-password-card {
  width: 400px;
  padding: 30px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.logo-container {
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

.forgot-password-title {
  font-size: 24px;
  color: #28826b;
  margin-bottom: 20px;
}

.forgot-password-message {
  margin-bottom: 20px;
  color: #333;
}

.admin-info {
  background-color: #f0f9f6;
  padding: 15px;
  border-radius: 5px;
  margin-bottom: 20px;
}

.admin-info p {
  margin: 5px 0;
}

.back-button {
  width: 100%;
  background-color: #28826b;
  border-color: #28826b;
}
</style>