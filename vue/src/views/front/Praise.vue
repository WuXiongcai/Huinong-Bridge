<template>
  <div class="main-content">
    <div class="praise-container">
      <h2 class="page-title">我的历史点赞 <span class="praise-count">({{ total }})</span></h2>

      <el-card class="praise-summary">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="summary-item">
              <i class="el-icon-star-on"></i>
              <div>
                <h3>总点赞数</h3>
                <p>{{ total }}</p>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <i class="el-icon-date"></i>
              <div>
                <h3>本月点赞</h3>
                <p>{{ getMonthlyPraises() }}</p>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <i class="el-icon-trophy"></i>
              <div>
                <h3>最常点赞政策</h3>
                <p>{{ getMostPraisedPolicy() }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <el-table :data="tableData" stripe class="praise-table">
        <el-table-column prop="id" label="序号" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="点赞用户" width="150">
          <template v-slot="scope">
            <el-avatar :size="30" :src="scope.row.userAvatar">{{ scope.row.userName.charAt(0) }}</el-avatar>
            <span style="margin-left: 10px">{{ scope.row.userName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="policyName" label="点赞政策" show-overflow-tooltip>
          <template v-slot="scope">
            <el-link type="primary" :href="'/front/policyDetail?id=' + scope.row.policyId">{{scope.row.policyName}}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="点赞时间" width="200">
          <template v-slot="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 5px">{{ scope.row.time }}</span>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog title="政策详情" :visible.sync="dialogVisible" width="50%">
      <div v-if="selectedPolicy">
        <h3>{{ selectedPolicy.policyName }}</h3>
        <p><strong>发布时间：</strong> {{ selectedPolicy.publishTime }}</p>
        <p><strong>点赞次数：</strong> {{ selectedPolicy.praiseCount }}</p>
        <div class="policy-content" v-html="selectedPolicy.content"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      dialogVisible: false,
      selectedPolicy: null,
    }
  },
  mounted() {
    this.load(1)
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/praise/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.user.id,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list
          this.total = res.data?.total
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    getMonthlyPraises() {
      const currentMonth = new Date().getMonth()
      return this.tableData.filter(item => new Date(item.time).getMonth() === currentMonth).length
    },
    getMostPraisedPolicy() {
      const policyCounts = this.tableData.reduce((acc, curr) => {
        acc[curr.policyName] = (acc[curr.policyName] || 0) + 1
        return acc
      }, {})
      return Object.keys(policyCounts).reduce((a, b) => policyCounts[a] > policyCounts[b] ? a : b, '')
    },
  }
}
</script>

<style scoped>
@import "@/assets/css/theme/front_css/praise.css";
</style>