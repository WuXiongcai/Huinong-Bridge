<template>
  <div>
    <div class="search">
      <el-input 
        placeholder="请输入景点名称" 
        style="width: 200px" 
        prefix-icon="el-icon-search" 
        v-model="name">
      </el-input>
      <el-button 
        type="info" 
        plain 
        style="margin-left: 10px" 
        icon="el-icon-search" 
        @click="load(1)">
        查询
      </el-button>
      <el-button 
        type="warning" 
        plain 
        style="margin-left: 10px" 
        icon="el-icon-refresh" 
        @click="reset">
        重置
      </el-button>
    </div>

    <div class="operation">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增景点</el-button>
      <el-button 
        type="danger" 
        icon="el-icon-delete" 
        :disabled="!ids.length" 
        @click="delBatch">
        批量删除
      </el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="景点名称"></el-table-column>
        <el-table-column prop="feature" label="景点特色" show-overflow-tooltip></el-table-column>
        <el-table-column prop="address" label="地址" show-overflow-tooltip></el-table-column>
        <el-table-column prop="openTime" label="开放时间"></el-table-column>
        <el-table-column label="位置信息" width="180">
          <template v-slot="scope">
            <div>经度: {{scope.row.longitude}}</div>
            <div>纬度: {{scope.row.latitude}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类">
          <template v-slot="scope">
            <el-tag :type="getTagType(scope.row.category)">
              {{scope.row.category}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template v-slot="scope">
            <el-button 
              type="primary" 
              icon="el-icon-edit" 
              size="mini" 
              @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button 
              type="danger" 
              icon="el-icon-delete" 
              size="mini" 
              @click="del(scope.row.id)">
              删除
            </el-button>
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
      :title="form.id ? '编辑景点' : '新增景点'" 
      :visible.sync="dialogVisible" 
      width="70%" 
      :close-on-click-modal="false"
      :destroy-on-close="true">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="景点名称" prop="name">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="category">
              <el-select v-model="form.category" style="width: 100%">
                <el-option label="自然景观" value="自然景观"></el-option>
                <el-option label="人文古迹" value="人文古迹"></el-option>
                <el-option label="休闲娱乐" value="休闲娱乐"></el-option>
                <el-option label="特色景点" value="特色景点"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="景点特色" prop="feature">
              <el-input v-model="form.feature"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开放时间" prop="openTime">
              <el-input v-model="form.openTime"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address"></el-input>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" readonly placeholder="将根据地址自动生成"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" readonly placeholder="将根据地址自动生成"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="简介" prop="description">
          <el-input type="textarea" v-model="form.description" :rows="3"></el-input>
        </el-form-item>

        <el-form-item label="收费情况" prop="content">
          <el-input type="textarea" v-model="form.content" :rows="3" placeholder="请输入景点收费情况"></el-input>
        </el-form-item>

        <el-form-item label="景点图片">
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
  name: 'SightManager',
  data() {
    return {
      name: '',
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      dialogVisible: false,
      form: {},
      ids: [],
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      amapKey: '8820b301cbb7e426018db32287d394a0',
      rules: {
        name: [{ required: true, message: '请输入景点名称', trigger: 'blur' }],
        category: [{ required: true, message: '请选择分类', trigger: 'change' }],
        address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
      }
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/sight/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        this.tableData = res.data?.list || []
        this.total = res.data?.total || 0
      })
    },
    reset() {
      this.name = ''
      this.load(1)
    },
    handleAdd() {
      this.form = {
        longitude: '',
        latitude: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          const promise = this.form.id ? 
            this.$request.put('/sight/update', this.form) :
            this.$request.post('/sight/add', this.form)
          
          promise.then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.load(1)
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    del(id) {
      this.$confirm('确认删除该景点？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$request.delete('/sight/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(row => row.id)
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确认删除选中的景点？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$request.delete('/sight/delete/batch', {
          data: this.ids
        }).then(res => {
          if (res.code === '200') {
            this.$message.success('批量删除成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    getImageUrl(path) {
      if (path && !path.startsWith('http')) {
        return this.$baseUrl + '/files/' + path
      }
      return path
    },
    handleImageSuccess(res) {
      if (res.code === '200') {
        this.form.image = res.data
      }
    },
    beforeImageUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('上传文件只能是图片格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
      }
      return isImage && isLt2M
    },
    getTagType(category) {
      const types = {
        '自然景观': 'success',
        '人文古迹': 'warning',
        '休闲娱乐': 'primary',
        '特色景点': 'info'
      }
      return types[category] || ''
    },
    async getGeocode(address) {
      try {
        const response = await fetch(
          `https://restapi.amap.com/v3/geocode/geo?key=${this.amapKey}&address=${encodeURIComponent(address)}`
        )
        const data = await response.json()
        
        if (data.status === '1' && data.geocodes && data.geocodes.length > 0) {
          const [longitude, latitude] = data.geocodes[0].location.split(',')
          this.form.longitude = longitude
          this.form.latitude = latitude
        } else {
          this.$message.warning('无法获取该地址的经纬度信息')
        }
      } catch (error) {
        console.error('获取经纬度失败:', error)
        this.$message.error('获取经纬度失败')
      }
    },
    async handleAddressChange(address) {
      if (address) {
        await this.getGeocode(address)
      }
    }
  },
  watch: {
    'form.address': {
      handler: 'handleAddressChange',
      deep: true
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
