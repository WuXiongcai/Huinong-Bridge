<template>
  <div class="home-dashboard">
    <el-row :gutter="24">
      <el-col :span="24">
        <el-card class="welcome-card">
          <div class="welcome-header">
            <i class="el-icon-s-home"></i>
            <span>欢迎使用惠农云桥——扶贫助农后台管理系统</span>
          </div>
          <div class="welcome-content">
            您好，<strong>{{ user?.name }}</strong>！祝您学习进步，工作顺利哦！
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="(stat, index) in statistics" :key="index">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-icon">
              <i :class="stat.icon"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-label">{{ stat.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24" class="content-row">
      <el-col :xs="24" :sm="24" :md="12">
        <el-card class="notice-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-bell"></i> 最新公告</span>
            <el-button type="text">查看全部</el-button>
          </div>
          <el-timeline>
            <el-timeline-item
              v-for="item in notices"
              :key="item.id"
              :timestamp="item.time"
              :type="item.type"
            >
              <el-card class="notice-item">
                <h4>{{ item.title }}</h4>
                <p>{{ item.content }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="12">
        <el-card class="quick-access-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-menu"></i> 快速访问</span>
          </div>
          <el-row :gutter="16">
            <el-col :span="8" v-for="(item, index) in quickAccessItems" :key="index">
              <div class="quick-access-item" @click="navigateTo(item.route)">
                <i :class="item.icon"></i>
                <span>{{ item.label }}</span>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="recent-activities-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-date"></i> 最近活动</span>
          </div>
          <el-table
            :data="recentActivities"
            style="width: 100%"
            :header-cell-style="{ background: '#f5f7fa' }"
          >
            <el-table-column prop="date" label="日期" width="120"></el-table-column>
            <el-table-column prop="name" label="活动名称"></el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag
                  :type="getStatusType(scope.row.status)"
                  size="small"
                >
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      notices: [],
      statistics: [
        { label: '已帮扶户数', value: 188, icon: 'el-icon-s-custom' },
        { label: '扶贫项目数', value: 44, icon: 'el-icon-s-cooperation' },
        { label: '农产品销售额', value: '￥52013.14', icon: 'el-icon-s-goods' },
        { label: '帮扶村庄', value: 28, icon: 'el-icon-s-flag' }
      ],
      quickAccessItems: [
        { label: '助农政策', icon: 'el-icon-document', route: '/policy' },
        { label: '农家乐', icon: 'el-icon-house', route: '/agritainment' },
        { label: '扶贫商品', icon: 'el-icon-goods', route: '/goods' },
        { label: '贫困户申请', icon: 'el-icon-s-claim', route: '/poorApply' },
        { label: '项目申请', icon: 'el-icon-s-order', route: '/projectApply' },
        { label: '订单管理', icon: 'el-icon-s-order', route: '/orders' }
      ],
      recentActivities: [
        { date: '2024-05-7', name: '春季农产品展销会', status: '已完成' },
        { date: '2024-08-20', name: '助农政策宣讲会', status: '进行中' },
        { date: '2024-12-20', name: '农业技术培训', status: '未开始' },
        { date: '2024-12-21', name: '贫困户走访', status: '未开始' }
      ]
    }
  },
  created() {
    this.fetchNotices()
  },
  methods: {
    fetchNotices() {
      this.$request.get('/notice/selectAll').then(res => {
        this.notices = res.data || []
      })
    },
    navigateTo(route) {
      this.$router.push(route)
    },
    getStatusType(status) {
      const statusMap = {
        '已完成': 'success',
        '进行中': 'warning',
        '未开始': 'info'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/theme/manager_css/home.css";
</style>