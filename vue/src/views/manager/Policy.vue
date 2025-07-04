<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入标题查询" style="width: 200px" prefix-icon="el-icon-search" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" icon="el-icon-search" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" icon="el-icon-refresh" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button
          type="primary"
          icon="el-icon-plus"
          @click="handleAdd"
      >
        新增政策
      </el-button>
      <el-button
          type="danger"
          icon="el-icon-delete"
          :disabled="!ids.length"
          @click="delBatch"
      >
        批量删除
      </el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="政策标题" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="政策内容">
          <template v-slot="scope">
            <el-button type="primary" size="mini" @click="viewData(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间"></el-table-column>
        <el-table-column prop="type" label="政策类型" width="120">
          <template v-slot="{ row }">
            <el-tag :type="getPolicyType(row.type)">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-button plain type="danger" size="mini" icon="el-icon-delete" @click=del(scope.row.id)>删除</el-button>
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
            layout="total, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog title="新增政策" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="name" label="政策标题">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="type" label="政策类型">
          <el-select v-model="form.type" placeholder="请选择政策类型">
            <el-option label="产业扶贫" value="产业扶贫"></el-option>
            <el-option label="教育扶贫" value="教育扶贫"></el-option>
            <el-option label="健康扶贫" value="健康扶贫"></el-option>
            <el-option label="生态扶贫" value="生态扶贫"></el-option>
            <el-option label="金融扶贫" value="金融扶贫"></el-option>
            <el-option label="精准扶贫" value="精准扶贫"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="content" label="政策内容">
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="助农政策" :visible.sync="editorVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="this.viewContent" class="w-e-text"></div>
    </el-dialog>

  </div>
</template>

<script>
import E from 'wangeditor'

export default {
  name: "Policy",
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
        ],
        type: [
          {required: true, message: '请选择政策类型', trigger: 'change'},
        ],
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
    handleAdd() {
      this.form = { type: '产业扶贫', status: true, effectiveDate: new Date() }
      this.fromVisible = true
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
            url: this.form.id ? '/policy/update' : '/policy/add',
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
        this.$request.delete('/policy/delete/' + id).then(res => {
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
        this.$request.delete('/policy/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/policy/selectPage', {
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
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load(1)
    },
    getPolicyType(type) {
      const types = {
        '产业扶贫': 'success',
        '教育扶贫': 'warning',
        '健康扶贫': 'danger',
        '生态扶贫': 'info',
        '金融扶贫': 'primary'
      }
      return types[type] || ''
    },

  }
}
</script>

<style scoped>

</style>
