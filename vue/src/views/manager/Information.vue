<template>
  <div class="information-container">
    <div class="search-wrapper">
      <el-input 
        placeholder="请输入标题查询" 
        v-model="name"
        prefix-icon="el-icon-search"
        clearable
        class="search-input">
      </el-input>
      <div class="search-btns">
        <el-button type="primary" icon="el-icon-search" @click="load(1)">查询</el-button>
        <el-button type="info" icon="el-icon-refresh" @click="reset">重置</el-button>
      </div>
    </div>

    <div class="operation-wrapper">
      <el-button
        type="primary"
        icon="el-icon-plus"
        @click="handleAdd">
        新增资讯
      </el-button>
      <el-button
        type="danger"
        icon="el-icon-delete"
        :disabled="!ids.length"
        @click="delBatch">
        批量删除
      </el-button>
    </div>

    <div class="table-wrapper">
      <el-table 
        :data="tableData" 
        stripe
        border
        highlight-current-row
        @selection-change="handleSelectionChange"
        class="custom-table">
        <el-table-column type="selection" width="55" align="center" fixed></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable fixed></el-table-column>
        <el-table-column prop="name" label="资讯标题" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="资讯内容" align="center" width="120">
          <template v-slot="scope">
            <el-button type="text" @click="viewData(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间" width="180" align="center"></el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template v-slot="scope">
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" class="delete-btn" icon="el-icon-delete" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog title="新增资讯" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="name" label="资讯标题">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="资讯内容">
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="助农资讯" :visible.sync="editorVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="this.viewContent" class="w-e-text"></div>
    </el-dialog>

  </div>
</template>

<script>
import E from 'wangeditor'

export default {
  name: "Information",
  data() {
    return {
      editor: null,
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      fromVisible: false,
      editorVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入标题', trigger: 'blur'},
        ]
      },
      ids: [],
      viewContent: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    initWangEditor(content) {
      this.$nextTick(() => {
        this.editor = new E('#editor')
        this.editor.config.placeholder = '请输入内容'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload'
        this.editor.create()
        setTimeout(() => {
          this.editor.txt.html(content)
        })
      })
    },
    viewData(content) {
      this.viewContent = content
      this.editorVisible = true
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
      this.initWangEditor('')
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
      this.initWangEditor(this.form.content || '')
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.content = this.editor.txt.html()
          this.$request({
            url: this.form.id ? '/information/update' : '/information/add',
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
        this.$request.delete('/information/delete/' + id).then(res => {
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
        this.$request.delete('/information/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/information/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.load(1)
    }
  }
}
</script>

<style scoped>
.information-container {
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.search-wrapper {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.search-input {
  width: 300px;
  margin-right: 15px;
}

.search-btns {
  display: flex;
  gap: 10px;
}

.operation-wrapper {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.table-wrapper {
  margin-top: 20px;
}

.custom-table {
  width: 100%;
  margin-bottom: 20px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.delete-btn {
  color: #F56C6C;
}

.delete-btn:hover {
  color: #f78989;
}

/* 自定义表格样式 */
.el-table {
  border-radius: 4px;
  overflow: hidden;
}

.el-table th {
  background-color: #f5f7fa !important;
  color: #606266;
  font-weight: bold;
}

.el-table td {
  padding: 12px 0;
}

/* 按钮过渡动画 */
.el-button {
  transition: all 0.3s;
}
</style>
