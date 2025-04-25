<template>
  <div class="comment-container">
    <el-card class="header-card" :body-style="{ padding: '20px' }">
      <div slot="header" class="clearfix">
        <span class="header-title">景点评论管理</span>
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
            <el-statistic title="总点赞数" :value="totalLikes">
              <template slot="suffix">
                <i class="el-icon-star-on stat-icon orange"></i>
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
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="userName" label="评论人" width="120"></el-table-column>
        <el-table-column prop="title" label="标题" width="150"></el-table-column>
        <el-table-column prop="content" label="评论内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="likes" label="点赞数" width="100" align="center"></el-table-column>
        <el-table-column prop="time" label="评论时间" width="180"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <div class="operation-buttons">
              <el-button 
                type="primary" 
                icon="el-icon-view" 
                size="mini" 
                @click="viewDetails(scope.row)"
              >
                查看
              </el-button>
              <el-button 
                type="danger" 
                icon="el-icon-delete" 
                size="mini" 
                @click="del(scope.row.id)"
              >
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          background
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>

    <!-- 评论详情对话框 -->
    <el-dialog title="评论详情" :visible.sync="dialogVisible" width="50%">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="评论人">{{ currentComment.userName }}</el-descriptions-item>
        <el-descriptions-item label="标题" v-if="currentComment.title">
          {{ currentComment.title }}
        </el-descriptions-item>
        <el-descriptions-item label="评论内容">{{ currentComment.content }}</el-descriptions-item>
        <el-descriptions-item label="点赞数">{{ currentComment.likes }}</el-descriptions-item>
        <el-descriptions-item label="评论时间">{{ currentComment.time }}</el-descriptions-item>
      </el-descriptions>
      
      <div v-if="currentComment.replies && currentComment.replies.length" class="reply-list">
        <h4>回复列表：</h4>
        <div v-for="reply in currentComment.replies" :key="reply.id" class="reply-item">
          <p>
            <span class="reply-user">{{ reply.userName }}</span>
            <span class="reply-time">{{ reply.time }}</span>
          </p>
          <p class="reply-content">{{ reply.content }}</p>
        </div>
      </div>
      
      <span slot="footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'SightComment',
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      content: '',
      dialogVisible: false,
      currentComment: {},
      ids: [],
      totalComments: 0,
      monthlyNewComments: 0,
      totalLikes: 0
    }
  },
  created() {
    this.load(1)
    this.loadStatistics()
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/sightcomment/page', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          content: this.content
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.list
          this.total = res.data.total
        }
      })
    },
    loadStatistics() {
      this.$request.get('/sightcomment/statistics').then(res => {
        if (res.code === '200') {
          this.totalComments = res.data.totalComments
          this.monthlyNewComments = res.data.monthlyNewComments
          this.totalLikes = res.data.totalLikes
        }
      })
    },
    reset() {
      this.content = ''
      this.load(1)
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(row => row.id)
    },
    del(id) {
      this.$confirm('确认删除该评论？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$request.delete('/sightcomment/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load(1)
          }
        })
      })
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确认删除选中的评论？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$request.delete('/sightcomment/delete/batch', {
          data: this.ids
        }).then(res => {
          if (res.code === '200') {
            this.$message.success('批量删除成功')
            this.load(1)
          }
        })
      })
    },
    viewDetails(row) {
      this.currentComment = row
      this.dialogVisible = true
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    }
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
}

.search-box {
  display: flex;
  gap: 10px;
  align-items: center;
}

.table-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.reply-list {
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.reply-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.reply-item:last-child {
  border-bottom: none;
}

.reply-user {
  font-weight: bold;
  color: #409EFF;
}

.reply-time {
  margin-left: 10px;
  color: #999;
  font-size: 12px;
}

.reply-content {
  margin-top: 5px;
  color: #666;
}

.operation-buttons {
  display: flex;
  justify-content: center;
  gap: 8px;
}

@media screen and (max-width: 768px) {
  .search-operation-container {
    flex-direction: column;
    gap: 10px;
  }
  
  .search-box {
    width: 100%;
  }
  
  .operation-box {
    width: 100%;
    display: flex;
    justify-content: flex-end;
  }

  .operation-buttons {
    flex-direction: column;
    gap: 5px;
  }
}
</style> 