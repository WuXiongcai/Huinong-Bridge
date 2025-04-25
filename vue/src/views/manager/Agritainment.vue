<template>
  <div>
    <div class="search">
      <el-input 
        placeholder="请输入农家乐名称" 
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
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增农家乐</el-button>
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
        <el-table-column prop="name" label="农家乐名称"></el-table-column>
        <el-table-column prop="feature" label="特色介绍" show-overflow-tooltip></el-table-column>
        <el-table-column prop="address" label="地址" show-overflow-tooltip></el-table-column>
        <el-table-column prop="phone" label="联系电话"></el-table-column>
        <el-table-column prop="businessHours" label="营业时间"></el-table-column>
        <el-table-column label="位置信息" width="180">
          <template v-slot="scope">
            <div>经度: {{scope.row.longitude}}</div>
            <div>纬度: {{scope.row.latitude}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="tags" label="标签">
          <template v-slot="scope">
            <el-tag 
              v-for="tag in scope.row.tags.split(',')" 
              :key="tag"
              :type="getTagType(tag)"
              style="margin-right: 5px">
              {{tag}}
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
      :title="form.id ? '编辑农家乐' : '新增农家乐'" 
      :visible.sync="dialogVisible" 
      width="70%" 
      :close-on-click-modal="false"
      :destroy-on-close="true">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="农家乐名称" prop="name">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="特色介绍" prop="feature">
              <el-input v-model="form.feature"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="营业时间" prop="businessHours">
              <el-input v-model="form.businessHours"></el-input>
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

        <el-form-item label="价格区间" prop="priceRange">
          <el-input v-model="form.priceRange" placeholder="例如：100-300元/人"></el-input>
        </el-form-item>

        <el-form-item label="标签" prop="tags">
          <el-select v-model="form.tags" multiple style="width: 100%">
            <el-option label="农家饭" value="农家饭"></el-option>
            <el-option label="住宿" value="住宿"></el-option>
            <el-option label="采摘" value="采摘"></el-option>
            <el-option label="垂钓" value="垂钓"></el-option>
            <el-option label="休闲娱乐" value="休闲娱乐"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="详细描述" prop="description">
          <el-input type="textarea" v-model="form.description" :rows="3"></el-input>
        </el-form-item>

        <el-form-item label="农家乐图片">
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
  name: 'Agritainment',
  data() {
    return {
      name: '',
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      ids: [],
      dialogVisible: false,
      form: {},
      rules: {
        name: [
          { required: true, message: '请输入农家乐名称', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' }
        ]
      },
      amapKey: '8820b301cbb7e426018db32287d394a0' // 替换为您的高德地图API密钥
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    load(pageNum) {
      this.pageNum = pageNum
      this.$request.get('/agritainment/selectPage', {
        params: {
          pageNum,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        this.tableData = res.data.list
        this.total = res.data.total
      })
    },
    reset() {
      this.name = ''
      this.load(1)
    },
    handleAdd() {
      this.form = {}
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      if (this.form.tags) {
        this.form.tags = this.form.tags.split(',')
      }
      this.dialogVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          if (this.form.tags) {
            this.form.tags = this.form.tags.join(',')
          }
          const request = this.form.id ? 
            this.$request.put('/agritainment/update', this.form) :
            this.$request.post('/agritainment/add', this.form)
          
          request.then(res => {
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
      this.$confirm('确认删除该农家乐？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$request.delete('/agritainment/delete/' + id).then(res => {
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
      this.$confirm('确认删除选中的农家乐？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$request.delete('/agritainment/delete/batch', {
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
    getTagType(tag) {
      const types = {
        '农家饭': 'success',
        '住宿': 'warning',
        '采摘': 'primary',
        '垂钓': 'info',
        '休闲娱乐': 'danger'
      }
      return types[tag] || ''
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
