<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入非遗名称" style="width: 200px" prefix-icon="el-icon-search" v-model="name"></el-input>
      <el-select v-model="level" placeholder="请选择非遗级别" style="width: 150px; margin-left: 10px">
        <el-option label="国家级" value="国家级"></el-option>
        <el-option label="省级" value="省级"></el-option>
        <el-option label="市级" value="市级"></el-option>
        <el-option label="县级" value="县级"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" icon="el-icon-search" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" icon="el-icon-refresh" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增非遗项目</el-button>
      <el-button type="danger" icon="el-icon-delete" :disabled="!ids.length" @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="image" label="展示图片" width="100">
          <template v-slot="scope">
            <el-image 
              style="width: 60px; height: 60px"
              :src="getImageUrl(scope.row.image)"
              :preview-src-list="[getImageUrl(scope.row.image)]">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="非遗名称"></el-table-column>
        <el-table-column prop="level" label="级别">
          <template v-slot="scope">
            <el-tag :type="scope.row.level === '国家级' ? 'danger' : 
                          scope.row.level === '省级' ? 'warning' : 
                          scope.row.level === '市级' ? 'success' : 'info'">
              {{ scope.row.level }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="slogan" label="宣传标语" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="简介" show-overflow-tooltip></el-table-column>
        <el-table-column prop="entryTime" label="入选时间" width="100"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-button plain type="danger" icon="el-icon-delete" @click="del(scope.row.id)" size="mini">删除</el-button>
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
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog 
        :title="form.id ? '编辑非遗项目' : '新增非遗项目'" 
        :visible.sync="dialogVisible" 
        width="65%" 
        :close-on-click-modal="false"
        :destroy-on-close="true">
        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="非遗名称" prop="name">
                        <el-input v-model="form.name" placeholder="请输入非遗名称"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="非遗级别" prop="level">
                        <el-select v-model="form.level" placeholder="请选择级别" style="width: 100%">
                            <el-option label="国家级" value="国家级"></el-option>
                            <el-option label="省级" value="省级"></el-option>
                            <el-option label="市级" value="市级"></el-option>
                            <el-option label="县级" value="县级"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="入选时间" prop="entryTime">
                        <el-date-picker
                            v-model="form.entryTime"
                            type="year"
                            placeholder="选择年份"
                            value-format="yyyy"
                            style="width: 100%">
                        </el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="宣传标语" prop="slogan">
                        <el-input v-model="form.slogan" placeholder="请输入宣传标语"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-form-item label="简介" prop="description">
                <el-input 
                    type="textarea" 
                    v-model="form.description" 
                    :rows="3"
                    placeholder="请输入简介"
                    resize="none">
                </el-input>
            </el-form-item>

            <el-form-item label="展示图片" prop="image">
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
                <div class="upload-tip">建议尺寸: 750x500px, 支持jpg、png格式</div>
            </el-form-item>
        </el-form>
        <div slot="footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "HeritageManager",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 8,
      total: 0,
      name: null,
      level: null,
      dialogVisible: false,
      form: {},
      ids: [],
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      rules: {
        name: [{ required: true, message: '请输入非遗名称', trigger: 'blur' }],
        level: [{ required: true, message: '请选择级别', trigger: 'change' }],
        entryTime: [{ required: true, message: '请选择入选时间', trigger: 'change' }],
        description: [{ required: true, message: '请输入简介', trigger: 'blur' }],
        image: [{ required: true, message: '请上传图片', trigger: 'change' }]
      }
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/heritage/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          level: this.level
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
      this.level = null
      this.load(1)
    },
    handleAdd() {
      this.form = {}
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/heritage/update' : '/heritage/add',
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
      this.$confirm('确认删除该非遗项目?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$request.delete('/heritage/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(row => row.id)
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm(`确认删除选中的 ${this.ids.length} 条数据?`, '提示', {
        type: 'warning'
      }).then(() => {
        this.$request.delete('/heritage/delete/batch', {
          data: this.ids
        }).then(res => {
          if (res.code === '200') {
            this.$message.success('批量删除成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleImageSuccess(res) {
      if (res.code === '200') {
        this.form.image = res.data
        this.$refs.formRef.validateField('image')
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
.search {
  margin-bottom: 20px;
}

.operation {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.avatar-uploader {
  text-align: center;
}

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

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}
</style>
