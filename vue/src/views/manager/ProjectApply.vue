<template>
  <div>
    <div class="search">
      <el-card class="header-card">
        <div slot="header" class="clearfix">
          <span class="header-title">扶贫项目申请管理</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="showHelp">使用帮助</el-button>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-statistic title="总申请数" :value="totalApplications">
              <template slot="suffix">
                <i class="el-icon-s-cooperation" style="color: #409EFF;"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="8">
            <el-statistic title="待审核" :value="pendingReviews">
              <template slot="suffix">
                <i class="el-icon-warning-outline" style="color: #E6A23C;"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="8">
            <el-statistic title="本月通过" :value="monthlyApproved">
              <template slot="suffix">
                <i class="el-icon-circle-check" style="color: #67C23A;"></i>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
      </el-card>

      <el-select v-model="status" placeholder="请选择状态" style="width: 200px">
        <el-option label="待审核" value="待审核"></el-option>
        <el-option label="审核通过" value="审核通过"></el-option>
        <el-option label="审核不通过" value="审核不通过"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" icon="el-icon-search" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" icon="el-icon-refresh" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="申请用户"></el-table-column>
        <el-table-column prop="content" label="申请说明" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="申请时间"></el-table-column>
        <el-table-column prop="file" label="申请资料">
          <template v-slot="scope">
            <el-button type="warning" size="mini" @click="downPoor(scope.row.file)">下载资料</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="审核状态"></el-table-column>
        <el-table-column prop="descr" label="审核说明"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" size="mini" :disabled="scope.row.status !== '待审核'" icon="el-icon-check" @click="check(scope.row)">审核</el-button>
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
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>
    <el-dialog title="贫困户审核" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form">
        <el-form-item prop="status" label="审核状态">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="待审核" value="待审核"></el-option>
            <el-option label="审核通过" value="审核通过"></el-option>
            <el-option label="审核不通过" value="审核不通过"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="descr" label="审核说明">
          <el-input type="textarea" :rows="5" v-model="form.descr" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="使用帮助" :visible.sync="helpVisible" width="30%">
      <div>
        <h4>快速入门：</h4>
        <p>1. 使用状态筛选和日期范围快速查找项目申请</p>
        <p>2. 点击"审核"按钮处理待审核的项目申请</p>
        <p>3. 查看申请详情可以了解更多项目信息</p>
        <p>4. 及时处理待审核申请以提高工作效率</p>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ProjectApply",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      status: null,
      dateRange: [],
      form: {},
      fromVisible: false,
      detailVisible: false,
      currentDetail: {},
      helpVisible: false,
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      loading: false,
      totalApplications: 0,
      pendingReviews: 0,
      monthlyApproved: 0,
    }
  },
  created() {
    this.load(1)
    this.loadStatistics()
  },
  methods: {
    check(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.fromVisible = true
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/projectapply/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          status: this.status,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    save() {
      this.$request.put('/projectapply/update', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          this.fromVisible = false
          this.load(1)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    reset() {
      this.status = null
      this.dateRange = []
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    downPoor(url) {
      location.href = url
    },
    viewDetails(row) {
      this.currentDetail = row
      this.detailVisible = true
    },
    showHelp() {
      this.helpVisible = true
    },
    loadStatistics() {
      this.$request.get('/projectapply/statistics').then(res => {
        if (res.code === '200') {
          this.totalApplications = res.data.totalApplications
          this.pendingReviews = res.data.pendingReviews
          this.monthlyApproved = res.data.monthlyApproved
        }
      }).catch(() => {
        this.$message.error('加载统计数据失败')
      })
    }
  }
}
</script>

<style scoped>

.header-card {
  margin-bottom: 20px;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
}

.pagination {
  text-align: right;
}

</style>

