<template>
  <div class="video-manager-container">
    <div class="search-wrapper">
      <div class="search-left">
        <el-input 
          placeholder="请输入视频标题查询" 
          v-model="title"
          prefix-icon="el-icon-search"
          clearable>
        </el-input>
        <el-button type="info" plain icon="el-icon-search" @click="load(1)">查询</el-button>
        <el-button type="warning" plain icon="el-icon-refresh" @click="reset">重置</el-button>
      </div>
      <div class="operation-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增视频</el-button>
        <el-button
          type="danger"
          icon="el-icon-delete"
          :disabled="!ids.length"
          @click="delBatch">
          批量删除
        </el-button>
      </div>
    </div>

    <div class="table-wrapper">
      <el-table 
        :data="tableData" 
        stripe 
        @selection-change="handleSelectionChange"
        height="calc(100vh - 220px)"
        border>
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="title" label="视频标题" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="视频描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="videoUrl" label="视频链接">
          <template v-slot="scope">
            <el-button type="primary" size="mini" @click="playVideo(scope.row.videoUrl)">播放视频</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="uploadTime" label="上传时间"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-button plain type="danger" size="mini" icon="el-icon-delete" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
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

    <el-dialog title="新增视频" :visible.sync="formVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="title" label="视频标题">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="视频描述">
          <el-input type="textarea" v-model="form.description" :rows="3" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="videoUrl" label="视频文件">
          <el-upload
              class="upload-demo"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              :on-success="handleVideoSuccess"
              :before-upload="beforeVideoUpload"
              :limit="1"
              accept="video/*"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传mp4/avi文件，且不超过500MB</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="视频封面">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              :show-file-list="false"
              :on-success="handleImageSuccess"
              :before-upload="beforeImageUpload">
            <img v-if="form.coverImage" :src="getImageUrl(form.coverImage)" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="播放视频" :visible.sync="videoVisible" width="70%" :close-on-click-modal="false">
      <video :src="currentVideoUrl" controls style="width: 100%"></video>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "VideoManager",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      title: null,
      formVisible: false,
      videoVisible: false,
      form: {
        title: '',
        description: '',
        videoUrl: '',
        coverImage: ''
      },
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      rules: {
        title: [
          {required: true, message: '请输入视频标题', trigger: 'blur'},
        ],
        description: [
          {required: true, message: '请输入视频描述', trigger: 'blur'},
        ],
        videoUrl: [
          {required: true, message: '请上传视频文件', trigger: 'change'},
        ]
      },
      ids: [],
      currentVideoUrl: null,
      baseUrl: 'http://localhost:9090'
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    playVideo(url) {
      this.currentVideoUrl = url
      this.videoVisible = true
    },
    handleAdd() {
      this.form = {}
      this.formVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.formVisible = true
    },
    handleImageSuccess(res, file) {
      if (res.code === '200') {
        this.form.coverImage = res.data;
      } else {
        this.$message.error('图片上传失败');
      }
    },
    beforeImageUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG && !isPNG) {
        this.$message.error('上传封面图片只能是 JPG 或 PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传封面图片大小不能超过 2MB!');
      }
      return (isJPG || isPNG) && isLt2M;
    },
    handleVideoSuccess(res, file) {
      if (res.code === '200') {
        this.form.videoUrl = res.data;
        this.$message.success('视频上传成功');
      } else {
        this.$message.error('视频上传失败');
      }
    },
    beforeVideoUpload(file) {
      const isVideo = file.type === 'video/mp4' || file.type === 'video/avi';
      const isLt500M = file.size / 1024 / 1024 < 500;

      if (!isVideo) {
        this.$message.error('上传视频只能是 MP4 或 AVI 格式!');
      }
      if (!isLt500M) {
        this.$message.error('上传视频大小不能超过 500MB!');
      }
      return isVideo && isLt500M;
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/videos/update' : '/videos/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功')
              this.load(1)
              this.formVisible = false
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    del(id) {
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/videos/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
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
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/videos/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/videos/selectPage', {
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
.video-manager-container {
  padding: 20px;
  height: 100%;
  box-sizing: border-box;
  background-color: #f5f7fa;
}

.search-wrapper {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-left .el-input {
  width: 300px;
}

.operation-right {
  display: flex;
  gap: 10px;
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
.upload-demo {
  width: 360px;
}

@media screen and (max-width: 768px) {
  .search-wrapper {
    flex-direction: column;
    gap: 15px;
  }
  
  .search-left {
    width: 100%;
  }
  
  .operation-right {
    width: 100%;
    justify-content: flex-start;
  }
  
  .search-left .el-input {
    width: 100%;
  }
}
</style>