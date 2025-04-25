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
        新增故事
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
        <el-table-column prop="name" label="故事标题" show-overflow-tooltip></el-table-column>
        <el-table-column prop="excerpt" label="故事摘要" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="故事内容">
          <template v-slot="scope">
            <el-button type="primary" size="mini" @click="viewData(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间"></el-table-column>

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

    <el-dialog title="新增故事" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="name" label="故事标题">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="excerpt" label="故事摘要">
          <el-input v-model="form.excerpt" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="故事内容">
          <div id="editor"></div>
        </el-form-item>
        <el-form-item label="故事背景图">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              :show-file-list="false"
              :on-success="handleImageSuccess"
              :before-upload="beforeImageUpload">
            <img v-if="form.image" :src="getImageUrl(form.image)" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="扶贫故事" :visible.sync="editorVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="this.viewContent" class="w-e-text"></div>
    </el-dialog>

  </div>
</template>

<script>
import E from 'wangeditor'

export default {
  name: "Stories",
  data() {
    return {
      editor: null,
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      excerpt: null,
      fromVisible: false,
      editorVisible: false,
      form: {
        image: ''
      },
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入标题', trigger: 'blur'},
        ],
        excerpt: [
          {required: true, message: '请输入摘要', trigger: 'blur'},
        ]
      },
      ids: [],
      viewContent: null,
      baseUrl: 'http://localhost:9090'
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
        this.editor.config.uploadImgServer = 'http://localhost:9090/files/upload'
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
    handleImageSuccess(res, file) {
      if (res.code === '200') {
        this.form.image = res.data; // Store the image path returned from the server
      } else {
        this.$message.error('图片上传失败');
      }
    },
    beforeImageUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG && !isPNG) {
        this.$message.error('上传背景图片只能是 JPG 或 PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传背景图片大小不能超过 2MB!');
      }
      return (isJPG || isPNG) && isLt2M;
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.content = this.editor.txt.html()
          this.$request({
            url: this.form.id ? '/stories/update' : '/stories/add',
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
        this.$request.delete('/stories/delete/' + id).then(res => {
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
        this.$request.delete('/stories/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/stories/selectPage', {
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
    getImageUrl(path) {
      if (path && !path.startsWith('http')) {
        return `${this.baseUrl}/files/${path}`;
      }
      return path;
    },
  }
}
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
