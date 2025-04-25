<template>
  <div class="comment-container">
    <el-card class="header-card" :body-style="{ padding: '20px' }">
      <div slot="header" class="clearfix">
        <span class="header-title">政策评论管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="showHelp">
          <i class="el-icon-question"></i> 使用帮助
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="stat-card">
            <el-statistic title="总评论数" :value="totalComments">
              <template slot="suffix">
                <i class="el-icon-chat-line-square stat-icon blue"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card">
            <el-statistic title="本月新增" :value="monthlyNewComments">
              <template slot="suffix">
                <i class="el-icon-data-line stat-icon green"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card">
            <el-statistic title="待审核" :value="pendingReviews">
              <template slot="suffix">
                <i class="el-icon-warning-outline stat-icon orange"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="operation-card">
      <div class="search-operation-container">
        <div class="search-box">
          <el-input
            placeholder="请输入评论内容"
            style="width: 300px"
            prefix-icon="el-icon-search"
            v-model="content"
            clearable
          ></el-input>
          <el-button type="primary" plain icon="el-icon-search" @click="load(1)">查询</el-button>
          <el-button type="info" plain icon="el-icon-refresh" @click="reset">重置</el-button>
        </div>
        <div class="operation-box">
          <el-button type="danger" plain icon="el-icon-delete" @click="delBatch">批量删除</el-button>
        </div>
      </div>
    </el-card>

    <el-card class="table-card">
      <el-table
        :data="tableData"
        stripe
        border
        @selection-change="handleSelectionChange"
        style="width: 100%"
        :header-cell-style="{ background: '#f5f7fa' }"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="评论人" min-width="120"></el-table-column>
        <el-table-column prop="content" label="评论内容" min-width="250" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="评论时间" min-width="180"></el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" icon="el-icon-view" size="mini" @click="viewDetails(scope.row)">查看</el-button>
            <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          background
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>

    <el-dialog title="评论详情" :visible.sync="dialogVisible" width="50%">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="评论人">{{ currentComment.userName }}</el-descriptions-item>
        <el-descriptions-item label="评论内容">{{ currentComment.content }}</el-descriptions-item>
        <el-descriptions-item label="评论时间">{{ currentComment.time }}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
    <el-dialog title="使用帮助" :visible.sync="helpVisible" width="30%">
      <div>
        <h4>快速入门：</h4>
        <p>1. 使用搜索栏快速查找评论</p>
        <p>2. 批量操作可以同时处理多个评论</p>
        <p>3. 查看评论详情可以进行更多操作</p>
        <p>4. 及时审核和回复评论以提高用户满意度</p>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Comment",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      content: '',
      commentType: '',
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      loading: false,
      selectedComments: [],
      dialogVisible: false,
      currentComment: {},
      helpVisible: false,
      totalComments: 0,
      monthlyNewComments: 0,
      pendingReviews: 0,
    }
  },
  created() {
    this.load(1)
    this.loadStatistics()
  },
  methods: {
    commentTypeTag(type) {
      const tags = {
        policy: '',
        feedback: 'success',
        suggestion: 'info'
      }
      return tags[type] || ''
    },
    statusLabel(status) {
      const statuses = {
        pending: '待审核',
        approved: '已通过',
        rejected: '已拒绝'
      }
      return statuses[status] || '未知'
    },
    statusTag(status) {
      const tags = {
        pending: 'warning',
        approved: 'success',
        rejected: 'danger'
      }
      return tags[status] || ''
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/comment/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/comment/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/comment/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          content: this.content,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.content = null
      this.load(1)
    },
    viewDetails(row) {
      this.currentComment = row
      this.dialogVisible = true
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    showHelp() {
      this.helpVisible = true
    },
    loadStatistics() {
      this.$request.get('/comment/statistics').then(res => {
        if (res.code === '200') {
          this.totalComments = res.data.totalComments;
          this.monthlyNewComments = res.data.monthlyNewComments;
          this.pendingReviews = res.data.pendingReviews;
        } else {
          this.$message.error('Failed to load statistics');
        }
      }).catch(error => {
        console.error('Error loading statistics:', error);
        this.$message.error('Failed to load statistics');
      });
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.load(1)
    },
  }
}
</script>

<style scoped>
.comment-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);
}

.header-card {
  margin-bottom: 20px;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.stat-card {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px 0 rgba(0, 0, 0, 0.15);
}

.stat-icon {
  font-size: 20px;
  margin-left: 8px;
}

.stat-icon.blue { color: #409EFF; }
.stat-icon.green { color: #67C23A; }
.stat-icon.orange { color: #E6A23C; }

.operation-card {
  margin-bottom: 20px;
}

.search-operation-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

.search-box {
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  padding: 10px 0;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .search-operation-container {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-box {
    width: 100%;
  }
  
  .operation-box {
    width: 100%;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
