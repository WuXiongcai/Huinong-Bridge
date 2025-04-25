<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入农家乐名称" style="width: 200px" prefix-icon="el-icon-search" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" icon="el-icon-search" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" icon="el-icon-refresh" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增榜单</el-button>
      <el-button type="danger" icon="el-icon-delete" :disabled="!ids.length" @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="排名" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="农家乐名称"></el-table-column>
        <el-table-column prop="rating" label="评分" width="120">
          <template v-slot="scope">
            {{ scope.row.rating }} 分
          </template>
        </el-table-column>
        <el-table-column prop="reviewCount" label="评价数" width="120">
          <template v-slot="scope">
            {{ scope.row.reviewCount }}条评价
          </template>
        </el-table-column>
        <el-table-column prop="monthlyVisitors" label="收藏量" width="120">
          <template v-slot="scope">
            {{ scope.row.monthlyVisitors }}人收藏
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="更新时间" width="180"></el-table-column>
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
        :title="form.id ? '编辑榜单' : '新增榜单'" 
        :visible.sync="dialogVisible" 
        width="50%" 
        :close-on-click-modal="false"
        :destroy-on-close="true">
        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
            <el-form-item label="农家乐名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入农家乐名称"></el-input>
            </el-form-item>
            <el-form-item label="评分" prop="rating">
                <el-input-number 
                    v-model="form.rating"
                    :precision="1"
                    :step="0.1"
                    :min="0"
                    :max="5"
                    controls-position="right"
                    placeholder="请输入评分(0-5分)"
                ></el-input-number>
                <span style="margin-left: 10px">分</span>
            </el-form-item>
            <el-form-item label="评价数" prop="reviewCount">
                <el-input-number 
                    v-model="form.reviewCount" 
                    :min="0"
                    controls-position="right"
                ></el-input-number>
                <span style="margin-left: 10px">条</span>
            </el-form-item>
            <el-form-item label="标签" prop="tags">
                <el-select
                    v-model="form.tags"
                    multiple
                    filterable
                    allow-create
                    default-first-option
                    placeholder="请选择或输入标签"
                    style="width: 100%"
                >
                    <el-option
                        v-for="item in tagOptions"
                        :key="item"
                        :label="item"
                        :value="item">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="收藏量" prop="monthlyVisitors">
                <el-input-number 
                    v-model="form.monthlyVisitors" 
                    :min="0"
                    controls-position="right"
                ></el-input-number>
                <span style="margin-left: 10px">人</span>
            </el-form-item>
            <el-form-item label="展示图片">
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
        <div slot="footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "FarmList",
  data() {
    return {
      name: '',
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      ids: [],
      dialogVisible: false,
      form: {},
      tagOptions: ['农家菜', '住宿', '采摘', '垂钓', '休闲', '景观', '亲子', '团建'],
      rules: {
        name: [{ required: true, message: '请输入农家乐名称', trigger: 'blur' }],
        rating: [
          { required: true, message: '请输入评分', trigger: 'blur' },
          { type: 'number', min: 0, max: 5, message: '评分范围为0-5分', trigger: 'blur' }
        ],
        reviewCount: [{ required: true, message: '请输入评价数', trigger: 'blur' }],
        monthlyVisitors: [{ required: true, message: '请输入收藏量', trigger: 'blur' }],
        tags: [{ required: true, message: '请至少选择一个标签', trigger: 'change' }]
      }
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/farmList/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = ''
      this.load(1)
    },
    handleAdd() {
      this.form = {
        rating: 0,
        reviewCount: 0,
        monthlyVisitors: 0,
        tags: []
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      if (typeof this.form.tags === 'string') {
        this.form.tags = this.form.tags.split(',')
      }
      this.dialogVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          const formData = { ...this.form }
          if (Array.isArray(formData.tags)) {
            formData.tags = formData.tags.join(',')
          }
          if (formData.id) {
            this.$request.put('/farmList/update', formData).then(res => {
              if (res.code === '200') {
                this.$message.success('保存成功')
                this.dialogVisible = false
                this.load(1)
              } else {
                this.$message.error(res.msg)
              }
            })
          } else {
            this.$request.post('/farmList/add', formData).then(res => {
              if (res.code === '200') {
                this.$message.success('保存成功')
                this.dialogVisible = false
                this.load(1)
              } else {
                this.$message.error(res.msg)
              }
            })
          }
        }
      })
    },
    del(id) {
      this.$confirm('确认删除这条榜单记录？', '提示', {type: 'warning'}).then(() => {
        this.$request.delete('/farmList/delete/' + id).then(res => {
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
      this.ids = rows.map(v => v.id)
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('确认批量删除这些榜单记录？', '提示', {type: 'warning'}).then(() => {
        this.$request.delete('/farmList/delete/batch', {data: this.ids}).then(res => {
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
      this.form.image = res.data
    },
    beforeImageUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
      }
      return isLt2M
    },
    getImageUrl(path) {
      if (path && !path.startsWith('http')) {
        return this.$baseUrl + '/files/' + path
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

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.avatar-uploader:hover {
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

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
