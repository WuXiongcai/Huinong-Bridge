<template>
  <div>
    <div class="front-notice">
      <el-alert
          :title="top"
          type="info"
          show-icon
          :closable="false"
      >
        <i class="el-icon-bell" slot="icon"></i>
      </el-alert>
    </div>

    <header class="front-header">
      <div class="container">
        <div class="front-header-left" @click="navTo('/front/home')">
          <div class="logo-container">
            <img src="../assets/imgs/logo1.png" alt="惠农云桥 Logo" class="logo">
            <h1 class="title">惠农云桥</h1>
          </div>
        </div>

        <nav class="front-header-center">
          <el-menu 
            :default-active="$route.path" 
            mode="horizontal" 
            router 
            class="front-header-nav"
            background-color="#ffffff"
            text-color="#333333"
            active-text-color="#19a14b">
            <el-menu-item index="/front/home">
              <i class="el-icon-s-home"></i>首页
            </el-menu-item>
            <el-menu-item index="/front/stories">
              <i class="el-icon-collection"></i>乡村文化传播
            </el-menu-item>
            <el-menu-item index="/front/products">
              <i class="el-icon-shopping-bag-1"></i>助农农产品购买
            </el-menu-item>
            <el-menu-item index="/front/sight">
              <i class="el-icon-house"></i>龙门景点推荐
            </el-menu-item>
            <el-menu-item index="/front/about">
              <i class="el-icon-info"></i>关于惠农云桥
            </el-menu-item>
          </el-menu>
        </nav>

        <div class="front-header-right">
          <template v-if="!user.username">
            <el-button type="text" @click="$router.push('/login')">
              <i class="el-icon-user"></i> 登录
            </el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button type="primary" plain @click="$router.push('/register')">注册</el-button>
          </template>
          <el-dropdown v-else trigger="click">
            <div class="user-info">
              <el-avatar :size="32" :src="user.avatar"></el-avatar>
              <span class="username">{{ user.name }}</span>
              <i class="el-icon-arrow-down"></i>
            </div>
            <el-dropdown-menu slot="dropdown" class="user-dropdown">
              <el-dropdown-item @click.native="navTo('/front/orders')">
                <i class="el-icon-s-order"></i> 我的订单
              </el-dropdown-item>
              <el-dropdown-item @click.native="navTo('/front/praise')">
                <i class="el-icon-star-on"></i> 历史点赞
              </el-dropdown-item>
              <el-dropdown-item @click.native="navTo('/front/apply')">
                <i class="el-icon-s-claim"></i> 我的申请
              </el-dropdown-item>
              <el-dropdown-item @click.native="navTo('/front/person')">
                <i class="el-icon-user"></i> 个人中心
              </el-dropdown-item>
              <el-dropdown-item divided @click.native="logout">
                <i class="el-icon-switch-button"></i> 退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </header>

    <main class="main-body">
      <router-view ref="child" @update:user="updateUser" />
    </main>
  </div>
</template>

<script>
export default {
  name: "FrontLayout",
  data() {
    return {
      top: '',
      notice: [],
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
    }
  },
  mounted() {
    this.loadNotice()
  },
  methods: {
    loadNotice() {
      this.$request.get('/notice/selectAll').then(res => {
        this.notice = res.data
        let i = 0
        if (this.notice && this.notice.length) {
          this.top = this.notice[0].content
          setInterval(() => {
            this.top = this.notice[i].content
            i++
            if (i === this.notice.length) {
              i = 0
            }
          }, 2500)
        }
      })
    },
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
    },
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
    navTo(url) {
      location.href=url
    },
    navToProducts() {
      this.$router.push('/front/products')
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/front.css";



.container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}





.front-header-nav {
  background-color: transparent;
  border-bottom: none;
}

.front-header-nav .front-header-nav  {
  font-size: 20px;
  color: #000000;
  height: 60px;
  line-height: 60px;
}

.front-header-nav .el-menu-item:hover,
.front-header-nav .el-submenu__title:hover {
  color: #19a14b;
}

.front-header-nav .el-menu-item.is-active {
  color: #19a14b;
  border-bottom: 2px solid #19a14b;
}

.front-header-nav a {
  text-decoration: none;
  color: inherit;
}


.user-name {
  margin: 0 10px;
  font-size: 14px;
  color: #333;
}

.main-body {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

@media (max-width: 768px) {
  .container {
    flex-direction: column;
    align-items: stretch;
  }

  .front-header-left,
  .front-header-center,
  .front-header-right {
    margin-bottom: 10px;
  }

  .front-header-nav {
    justify-content: flex-start;
    overflow-x: auto;
  }
}
</style>