<template>
  <div class="notice-container">
    <div class="search-wrapper">
      <el-input
        placeholder="请输入标题查询"
        style="width: 300px"
        prefix-icon="el-icon-search"
        v-model="title"
        clearable
      ></el-input>
      <div class="search-buttons">
        <el-button type="primary" icon="el-icon-search" @click="load(1)">查询</el-button>
        <el-button icon="el-icon-refresh" @click="reset">重置</el-button>
      </div>
    </div>

    <div class="overview-wrapper">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card shadow="hover" class="overview-card">
            <div slot="header" class="card-header">
              <i class="el-icon-document"></i>
              <span>公告总数</span>
            </div>
            <div class="statistic">{{ total }} 条</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="overview-card">
            <div slot="header" class="card-header">
              <i class="el-icon-tickets"></i>
              <span>未读公告</span>
            </div>
            <div class="statistic">{{ total-2 }} 条</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="overview-card">
            <div slot="header" class="card-header">
              <i class="el-icon-user"></i>
              <span>创建人</span>
            </div>
            <div class="statistic">{{ user.name || '未知' }}</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="operation-wrapper">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增公告</el-button>
      <el-button
        type="danger"
        icon="el-icon-delete"
        :disabled="!ids.length"
        @click="delBatch"
      >批量删除</el-button>
    </div>

    <div class="table-wrapper">
      <el-table
        :data="tableData"
        stripe
        border
        @selection-change="handleSelectionChange"
        height="calc(100vh - 420px)"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55" align="center" fixed></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable fixed></el-table-column>
        <el-table-column prop="title" label="标题" min-width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="内容" min-width="250" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="创建时间" width="160" align="center"></el-table-column>
        <el-table-column prop="user" label="创建人" width="120" align="center"></el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          background
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :current-page="pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog title="新增公告" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="title" label="标题">
          <el-input v-model="form.title" placeholder="请输入公告标题"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="内容">
          <el-input
              type="textarea"
              :rows="6"
              v-model="form.content"
              placeholder="请输入公告内容"
          ></el-input>
        </el-form-item>
        <el-form-item label="重要程度">
          <el-rate v-model="form.importance" :max="3" show-score></el-rate>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Notice",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      title: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入内容', trigger: 'blur' },
          { min: 10, max: 500, message: '长度在 10 到 500 个字符', trigger: 'blur' }
        ]
      },
      ids: [],
      loading: false,
      fileList: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/notice/update' : '/notice/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/notice/delete/' + id).then(res => {
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
        this.$request.delete('/notice/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/notice/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.title = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.load(1)
    }
  }
}
</script>

<style scoped>
.notice-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.search-wrapper {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
}

.search-buttons {
  margin-left: 10px;
}

.overview-wrapper {
  margin: 20px 0;
}

.overview-card {
  transition: all 0.3s;
}

.overview-card:hover {
  transform: translateY(-5px);
}

.card-header {
  display: flex;
  align-items: center;
}

.card-header i {
  margin-right: 10px;
  font-size: 18px;
}

.statistic {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  text-align: center;
  margin: 20px 0;
}

.operation-wrapper {
  margin: 20px 0;
  background: #fff;
  padding: 15px 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.table-wrapper {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
