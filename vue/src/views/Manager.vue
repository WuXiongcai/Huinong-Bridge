<template>
  <div class="manager-container">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="../assets/imgs/logo1.png" alt="Logo" class="logo" />
        <div class="title">后台管理系统</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <div>{{ user.name ||  '管理员' }}</div>
            <i class="el-icon-caret-bottom"></i>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="goToPerson">
              <i class="el-icon-user"></i> 个人信息
            </el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/password')">
              <i class="el-icon-lock"></i> 修改密码
            </el-dropdown-item>
            <el-dropdown-item @click.native="logout">
              <i class="el-icon-switch-button"></i> 退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!--  主体  -->
    <div class="manager-main">
      <!--  侧边栏  -->
      <div class="manager-main-left">
        <el-menu router style="border: none" :default-active="$route.path">
          <el-menu-item index="/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>
          <el-submenu index="info">
            <template slot="title">
              <i class="el-icon-document"></i><span>信息管理</span>
            </template>
            <el-menu-item index="/notice"><i class="el-icon-bell"></i>公告信息</el-menu-item>
            <el-menu-item index="/information"><i class="el-icon-news"></i>助农资讯</el-menu-item>
            <el-menu-item index="/policy"><i class="el-icon-document-checked"></i>助农政策</el-menu-item>
            <el-menu-item index="/agritainment"><i class="el-icon-house"></i>农家乐信息</el-menu-item>
            <el-menu-item index="/farmlist"><i class="el-icon-s-order"></i>农家乐榜单</el-menu-item>
            <el-menu-item index="/goods"><i class="el-icon-goods"></i>特价助农商品</el-menu-item>
            <el-menu-item index="/orders"><i class="el-icon-s-order"></i>特价商品订单信息</el-menu-item>
            <el-menu-item index="/comment"><i class="el-icon-chat-line-square"></i>政策评论</el-menu-item>
          </el-submenu>
          <el-submenu index="apply">
            <template slot="title">
              <i class="el-icon-s-check"></i>
              <span>申请审核</span>
            </template>
            <el-menu-item index="/poorApply"><i class="el-icon-user"></i>贫困户申请</el-menu-item>
            <el-menu-item index="/projectApply"><i class="el-icon-s-cooperation"></i>扶贫项目申请</el-menu-item>
          </el-submenu>
          <el-submenu index="item">
            <template slot="title">
              <i class="el-icon-document"></i><span>乡村文化传播</span>
            </template>
            <el-menu-item index="/stories"><i class="el-icon-collection"></i>助农故事典例</el-menu-item>
            <el-menu-item index="/folk"><i class="el-icon-news"></i>民俗文化管理</el-menu-item>
            <el-menu-item index="/food"><i class="el-icon-document-checked"></i>特色美食管理</el-menu-item>
            <el-menu-item index="/handicraft"><i class="el-icon-house"></i>传统手工艺管理</el-menu-item>
            <el-menu-item index="/heritage"><i class="el-icon-s-order"></i>非遗文化名录管理</el-menu-item>
            <el-menu-item index="/funnyStories"><i class="el-icon-goods"></i>农村趣事分享</el-menu-item>
          </el-submenu>

          <el-submenu index="sight">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>龙门景点管理</span>
            </template>
            <el-menu-item index="/sight">
              <i class="el-icon-s-promotion"></i>景点信息管理
            </el-menu-item>
            <el-menu-item index="/sightcomment">
              <i class="el-icon-chat-line-round"></i>景点评论管理
            </el-menu-item>
          </el-submenu>

          <el-menu-item index="/videos">
            <i class="el-icon-video-camera"></i>
            <span slot="title">扶贫助农视频管理</span>
          </el-menu-item>

          <el-menu-item index="/message">
            <i class="el-icon-chat-line-square"></i>
            <span slot="title">留言信息管理</span>
          </el-menu-item>
          <el-submenu index="products">
            <template slot="title">
              <i class="el-icon-shopping-cart-full"></i>
              <span>农产品管理</span>
            </template>
            <el-menu-item index="/products"><i class="el-icon-goods"></i>农产品列表</el-menu-item>
            <el-menu-item index="/productOrders"><i class="el-icon-s-order"></i>农产品订单管理</el-menu-item>
          </el-submenu>
          <el-submenu index="user">
            <template slot="title">
              <i class="el-icon-user-solid"></i>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin"><i class="el-icon-s-custom"></i>管理员信息</el-menu-item>
            <el-menu-item index="/user"><i class="el-icon-user"></i>用户信息</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
    }
  },
  created() {
    if (!this.user.id || this.user.role !== 'ADMIN') {
      this.$router.push('/login')
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-admin') || '{}')
    },
    logout() {
      localStorage.removeItem('xm-admin')
      this.$router.push('/login')
    },
    goToPerson() {
      if (this.user.role === 'ADMIN') {
        this.$router.push('/adminPerson')
      }
    },
  }
}
</script>

<style scoped>
@import "@/assets/css/manager.css";
</style>