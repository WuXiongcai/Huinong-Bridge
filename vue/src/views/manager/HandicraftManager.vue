<template>
  <div>
    <!-- 搜索区域 -->
    <div class="search">
      <el-input placeholder="请输入手工艺名称" style="width: 200px" prefix-icon="el-icon-search" v-model="name"></el-input>
      <el-select v-model="category" placeholder="手工艺分类" style="width: 150px; margin-left: 10px">
        <el-option label="全部" value=""></el-option>
        <el-option label="编织工艺" value="编织工艺"></el-option>
        <el-option label="造纸工艺" value="造纸工艺"></el-option>
        <el-option label="木雕工艺" value="木雕工艺"></el-option>
        <el-option label="医疗技艺" value="医疗技艺"></el-option>
        <el-option label="制作工艺" value="制作工艺"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" icon="el-icon-search" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" icon="el-icon-refresh" @click="reset">重置</el-button>
    </div>

    <!-- 操作按钮区域 -->
    <div class="operation">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增手工艺</el-button>
      <el-button type="danger" icon="el-icon-delete" :disabled="!ids.length" @click="delBatch">批量删除</el-button>
    </div>

    <!-- 表格区域 -->
    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="手工艺名称"></el-table-column>
        <el-table-column prop="category" label="分类">
          <template v-slot="scope">
            <el-tag :type="getTagType(scope.row.category)">{{ scope.row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="简介" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="详细内容">
          <template v-slot="scope">
            <el-button type="primary" size="mini" @click="viewData(scope.row)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-button plain type="danger" icon="el-icon-delete" @click="del(scope.row.id)" size="mini">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
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
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog 
        :title="form.id ? '编辑手工艺信息' : '新增手工艺'" 
        :visible.sync="dialogVisible" 
        width="65%" 
        :close-on-click-modal="false"
        :destroy-on-close="true"
        custom-class="handicraft-dialog">
        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" class="handicraft-form">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="手工艺名称" prop="name">
                        <el-input v-model="form.name" placeholder="请输入手工艺名称"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="分类" prop="category">
                        <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
                            <el-option label="编织工艺" value="编织工艺">
                                <i class="el-icon-scissors"></i>
                                <span style="margin-left: 8px">编织工艺</span>
                            </el-option>
                            <el-option label="造纸工艺" value="造纸工艺">
                                <i class="el-icon-document"></i>
                                <span style="margin-left: 8px">造纸工艺</span>
                            </el-option>
                            <el-option label="木雕工艺" value="木雕工艺">
                                <i class="el-icon-brush"></i>
                                <span style="margin-left: 8px">木雕工艺</span>
                            </el-option>
                            <el-option label="医疗技艺" value="医疗技艺">
                                <i class="el-icon-first-aid-kit"></i>
                                <span style="margin-left: 8px">医疗技艺</span>
                            </el-option>
                            <el-option label="制作工艺" value="制作工艺">
                                <i class="el-icon-s-cooperation"></i>
                                <span style="margin-left: 8px">制作工艺</span>
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-form-item label="简介" prop="description">
                <el-input 
                    type="textarea" 
                    v-model="form.description" 
                    :rows="3"
                    placeholder="请输入手工艺简介"
                    resize="none">
                </el-input>
            </el-form-item>

            <el-row :gutter="20">
                <el-col :span="16">
                    <el-form-item label="详细内容" prop="content" class="content-editor">
                        <div id="editor"></div>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="展示图片" class="upload-container">
                        <el-upload
                            class="avatar-uploader"
                            :action="$baseUrl + '/files/upload'"
                            :headers="{ token: user.token }"
                            :show-file-list="false"
                            :on-success="handleImageSuccess"
                            :before-upload="beforeImageUpload">
                            <img v-if="form.image" :src="getImageUrl(form.image)" class="avatar">
                            <div v-else class="upload-placeholder">
                                <i class="el-icon-plus avatar-uploader-icon"></i>
                                <div class="upload-text">点击上传图片</div>
                            </div>
                        </el-upload>
                        <div class="upload-tip">建议尺寸: 750x500px, 支持jpg、png格式</div>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog 
        :title="viewTitle" 
        :visible.sync="viewDialogVisible" 
        width="60%"
        custom-class="view-dialog">
        <div class="view-content">
            <div class="view-header">
                <div class="view-info">
                    <el-tag :type="getTagType(viewCategory)" class="view-category">
                        {{ viewCategory }}
                    </el-tag>
                </div>
                <div class="view-time">发布时间：{{ viewTime }}</div>
            </div>
            <div class="view-description">{{ viewDescription }}</div>
            <div class="view-main">
                <div v-if="viewImage" class="view-image">
                    <img :src="getImageUrl(viewImage)" :alt="viewTitle">
                </div>
                <div class="view-text w-e-text" v-html="viewContent"></div>
            </div>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import E from 'wangeditor'

export default {
  name: "HandicraftManager",
  data() {
    return {
      editor: null,
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: '',
      category: '',
      dialogVisible: false,
      viewDialogVisible: false,
      form: {},
      viewContent: '',
      viewTitle: '',
      viewCategory: '',
      viewDifficulty: 0,
      viewDescription: '',
      viewImage: '',
      viewTime: '',
      ids: [],
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      rules: {
        name: [{ required: true, message: '请输入手工艺名称', trigger: 'blur' }],
        category: [{ required: true, message: '请选择分类', trigger: 'change' }],
        description: [{ required: true, message: '请输入简介', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    getTagType(category) {
      const types = {
        '编织工艺': 'success',
        '造纸工艺': 'warning',
        '木雕工艺': 'danger',
        '医疗技艺': 'info',
        '制作工艺': 'primary'
      }
      return types[category] || ''
    },
    initWangEditor(content) {
      this.$nextTick(() => {
        this.editor = new E('#editor')
        this.editor.config.placeholder = '请输入手工艺详细内容'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgServer = this.$baseUrl + '/files/upload'
        this.editor.config.uploadImgHeaders = { token: this.user.token }
        this.editor.create()
        setTimeout(() => {
          this.editor.txt.html(content)
        })
      })
    },
    handleAdd() {
      this.form = {}
      this.dialogVisible = true
      this.initWangEditor('')
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.initWangEditor(this.form.content || '')
    },
    viewData(row) {
      this.viewTitle = row.name
      this.viewCategory = row.category
      this.viewDifficulty = row.difficulty
      this.viewDescription = row.description
      this.viewContent = row.content
      this.viewImage = row.image
      this.viewTime = row.createTime
      this.viewDialogVisible = true
    },
    handleImageSuccess(res) {
      if (res.code === '200') {
        this.form.image = res.data
      } else {
        this.$message.error('图片上传失败')
      }
    },
    beforeImageUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isPNG = file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG && !isPNG) {
        this.$message.error('上传图片只能是 JPG 或 PNG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
      }
      return (isJPG || isPNG) && isLt2M
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.content = this.editor.txt.html()
          this.$request({
            url: this.form.id ? '/handicraft/update' : '/handicraft/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功')
              this.load(1)
              this.dialogVisible = false
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    del(id) {
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/handicraft/delete/' + id).then(res => {
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
      this.ids = rows.map(v => v.id)
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/handicraft/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/handicraft/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          category: this.category
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
      this.category = ''
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    getImageUrl(path) {
      if (path && !path.startsWith('http')) {
        return `${this.$baseUrl}/files/${path}`
      }
      return path
    }
  }
}
</script>

<style scoped>
/* 弹窗样式 */
.handicraft-dialog {
  border-radius: 8px;
}

.handicraft-dialog /deep/ .el-dialog__header {
  padding: 20px 25px;
  border-bottom: 1px solid #eee;
}

.handicraft-dialog /deep/ .el-dialog__body {
  padding: 25px;
}

.handicraft-form {
  padding: 0 15px;
}

.content-editor {
  margin-bottom: 20px;
}

.content-editor /deep/ #editor {
  min-height: 300px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

/* 上传区域样式 */
.upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-uploader {
  width: 100%;
  text-align: center;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100%;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.upload-placeholder {
  padding: 30px 0;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.upload-text {
  color: #606266;
  font-size: 14px;
  margin-top: 10px;
}

.avatar {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

/* 查看详情样式 */
.view-dialog {
  border-radius: 8px;
}

.view-dialog /deep/ .el-dialog__header {
  padding: 20px 25px;
  border-bottom: 1px solid #eee;
}

.view-dialog /deep/ .el-dialog__body {
  padding: 0;
}

.view-content {
  padding: 25px;
}

.view-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.view-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.view-difficulty {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
}

.view-difficulty .el-rate {
  display: inline-block;
  margin-top: 2px;
}

.view-time {
  color: #909399;
  font-size: 14px;
}

.view-description {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 4px;
  margin-bottom: 20px;
}

.view-main {
  .view-image {
    text-align: center;
    margin-bottom: 20px;
    
    img {
      max-width: 100%;
      border-radius: 4px;
      box-shadow: 0 2px 12px rgba(0,0,0,0.1);
    }
  }
  
  .view-text {
    line-height: 1.8;
    color: #333;
    
    img {
      max-width: 100%;
      height: auto;
    }
  }
}
</style>
