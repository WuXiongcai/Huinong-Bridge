<template>
  <div>
    <el-card class="header-card">
      <div slot="header" class="clearfix">
        <span class="header-title">留言管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="showHelp">使用帮助</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-statistic title="总留言数" :value="totalMessages">
            <template slot="suffix">
              <i class="el-icon-chat-line-square" style="color: #409EFF;"></i>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="8">
          <el-statistic title="本月新增" :value="monthlyNewMessages">
            <template slot="suffix">
              <i class="el-icon-data-line" style="color: #67C23A;"></i>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="8">
          <el-statistic title="待处理" :value="pendingMessages">
            <template slot="suffix">
              <i class="el-icon-warning-outline" style="color: #E6A23C;"></i>
            </template>
          </el-statistic>
        </el-col>
      </el-row>
    </el-card>

    <div class="search">
      <el-input placeholder="请输入留言内容" style="width: 200px" prefix-icon="el-icon-search" v-model="content"></el-input>
      <el-button type="info" plain style="margin-left: 10px" icon="el-icon-search" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" icon="el-icon-refresh" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="danger" plain icon="el-icon-delete" @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="留言人" show-overflow-tooltip></el-table-column>
        <el-table-column prop="email" label="邮箱" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="留言内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="留言时间"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template v-slot="scope">
            <el-tag :type="statusTag(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="220" align="center">
          <template v-slot="scope">
            <el-button type="primary" icon="el-icon-view" size="mini" @click="viewDetails(scope.row)">查看</el-button>
            <el-button type="success" icon="el-icon-check" size="mini" @click="updateStatus(scope.row, 'Processed')" v-if="scope.row.status === 'Pending'">标记已处理</el-button>
            <el-button plain type="danger" icon="el-icon-delete" size="mini" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20, 50]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog title="留言详情" :visible.sync="dialogVisible" width="50%">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="留言人">{{ currentMessage.name }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentMessage.email }}</el-descriptions-item>
        <el-descriptions-item label="留言内容">{{ currentMessage.content }}</el-descriptions-item>
        <el-descriptions-item label="留言时间">{{ currentMessage.time }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusTag(currentMessage.status)">{{ statusLabel(currentMessage.status) }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="updateStatus(currentMessage, 'Processed')" v-if="currentMessage.status === 'Pending'">标记为已处理</el-button>
      </span>
    </el-dialog>

    <el-dialog title="使用帮助" :visible.sync="helpVisible" width="30%">
      <div>
        <h4>快速入门：</h4>
        <p>1. 使用搜索栏快速查找留言</p>
        <p>2. 批量操作可以同时处理多个留言</p>
        <p>3. 查看留言详情可以进行更多操作</p>
        <p>4. 及时处理和回复留言以提高用户满意度</p>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Message",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      content: null,
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      loading: false,
      selectedMessages: [],
      dialogVisible: false,
      currentMessage: {},
      helpVisible: false,
      totalMessages: 0,
      monthlyNewMessages: 0,
      pendingMessages: 0,
    }
  },
  created() {
    this.load(1)
    this.loadStatistics()
  },
  methods: {
    statusTag(status) {
      const tags = {
        Pending: 'warning',
        Processed: 'success',
        Rejected: 'danger'
      }
      return tags[status] || ''
    },
    statusLabel(status) {
      const statuses = {
        Pending: '待处理',
        Processed: '已处理',
        Rejected: '已拒绝'
      }
      return statuses[status] || '未知'
    },
    del(id) {
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/message/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    handleSelectionChange(rows) {
      this.selectedMessages = rows.map(v => v.id)
    },
    delBatch() {
      if (!this.selectedMessages.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/message/delete/batch', {data: this.selectedMessages}).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/message/selectPage', {
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
      this.content = ''
      this.load(1)
    },
    viewDetails(row) {
      this.currentMessage = row
      this.dialogVisible = true
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    showHelp() {
      this.helpVisible = true
    },
    loadStatistics() {
      this.$request.get('/message/statistics').then(res => {
        if (res.code === '200') {
          this.totalMessages = res.data.totalMessages;
          this.monthlyNewMessages = res.data.monthlyNewMessages;
          this.pendingMessages = res.data.pendingMessages;
        } else {
          this.$message.error('Failed to load statistics');
        }
      }).catch(error => {
        console.error('Error loading statistics:', error);
        this.$message.error('Failed to load statistics');
      });
    },
    updateStatus(message, newStatus) {
      this.$request.put('/message/update', { ...message, status: newStatus }).then(res => {
        if (res.code === '200') {
          this.$message.success('状态更新成功')
          this.load(this.pageNum)
          this.dialogVisible = false
        } else {
          this.$message.error(res.msg)
        }
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

.search, .operation {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>