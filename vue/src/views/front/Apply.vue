<template>
  <div class="main-content">
    <div class="application-container">
      <h2 class="page-title">我的申请</h2>
      <el-tabs v-model="activeName" class="custom-tabs">
        <el-tab-pane label="贫困户申请" name="first">
          <div class="tab-content">
            <div class="action-bar">
              <el-button type="primary" icon="el-icon-plus" @click="handlePoorAdd">提交申请</el-button>
            </div>
            <el-table :data="poorData" stripe class="custom-table">
              <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
              <el-table-column prop="userName" label="申请用户"></el-table-column>
              <el-table-column prop="content" label="申请说明" show-overflow-tooltip></el-table-column>
              <el-table-column prop="time" label="申请时间"></el-table-column>
              <el-table-column prop="file" label="申请资料">
                <template v-slot="scope">
                  <el-button type="warning" size="mini" icon="el-icon-download" @click="downPoor(scope.row.file)">下载资料</el-button>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="审核状态">
                <template v-slot="scope">
                  <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="descr" label="审核说明" show-overflow-tooltip></el-table-column>
              <el-table-column label="操作" width="180" align="center">
                <template v-slot="scope">
                  <el-button plain type="primary" size="mini" icon="el-icon-edit" v-if="scope.row.status !== '审核通过'" @click="handlePoorEdit(scope.row)">编辑</el-button>
                  <el-button plain type="danger" size="mini" icon="el-icon-delete" v-if="scope.row.status !== '审核通过'" @click="delPoor(scope.row.id)">撤销申请</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination">
              <el-pagination
                  background
                  @current-change="handlePoorChange"
                  :current-page="poorPageNum"
                  :page-sizes="[5, 10, 20]"
                  :page-size="poorPageSize"
                  layout="total, prev, pager, next, jumper"
                  :total="poorTotal">
              </el-pagination>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="扶贫项目申请" name="second">
          <div class="tab-content">
            <div class="action-bar">
              <el-button type="primary" icon="el-icon-plus" @click="handleProjectAdd">提交申请</el-button>
            </div>
            <el-table :data="projectData" stripe class="custom-table">
              <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
              <el-table-column prop="userName" label="申请用户"></el-table-column>
              <el-table-column prop="content" label="申请说明" show-overflow-tooltip></el-table-column>
              <el-table-column prop="time" label="申请时间"></el-table-column>
              <el-table-column prop="file" label="申请资料">
                <template v-slot="scope">
                  <el-button type="warning" size="mini" icon="el-icon-download" @click="downProject(scope.row.file)">下载资料</el-button>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="审核状态">
                <template v-slot="scope">
                  <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="descr" label="审核说明" show-overflow-tooltip></el-table-column>
              <el-table-column label="操作" width="180" align="center">
                <template v-slot="scope">
                  <el-button plain type="primary" size="mini" icon="el-icon-edit" v-if="scope.row.status !== '审核通过'" @click="handleProjectEdit(scope.row)">编辑</el-button>
                  <el-button plain type="danger" size="mini" icon="el-icon-delete" v-if="scope.row.status !== '审核通过'" @click="delProject(scope.row.id)">撤销申请</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination">
              <el-pagination
                  background
                  @current-change="handleProjectChange"
                  :current-page="projectPageNum"
                  :page-sizes="[5, 10, 20]"
                  :page-size="projectPageSize"
                  layout="total, prev, pager, next, jumper"
                  :total="projectTotal">
              </el-pagination>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog title="扶贫项目申请信息" :visible.sync="projectVisible" width="40%" :close-on-click-modal="false" destroy-on-close custom-class="custom-dialog">
      <el-form label-width="100px" :model="projectForm" class="custom-form">
        <el-form-item prop="file" label="申请资料">
          <el-upload
              class="custom-upload"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="text"
              :on-success="handleProjectSuccess"
          >
            <el-button type="primary" icon="el-icon-upload2">上传资料</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="content" label="申请说明">
          <el-input type="textarea" :rows="5" v-model="projectForm.content" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="projectVisible = false">取 消</el-button>
        <el-button type="primary" @click="projectSave">提 交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="贫困户申请信息" :visible.sync="poorVisible" width="40%" :close-on-click-modal="false" destroy-on-close custom-class="custom-dialog">
      <el-form label-width="100px" :model="poorForm" class="custom-form">
        <el-form-item prop="file" label="申请资料">
          <el-upload
              class="custom-upload"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="text"
              :on-success="handlePoorSuccess"
          >
            <el-button type="primary" icon="el-icon-upload2">上传资料</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="content" label="申请说明">
          <el-input type="textarea" :rows="5" v-model="poorForm.content" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="poorVisible = false">取 消</el-button>
        <el-button type="primary" @click="poorSave">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      activeName: 'first',

      poorData: [],
      poorPageNum: 1,
      poorPageSize: 5,
      poorTotal: 0,
      poorVisible: false,
      poorForm: {},

      projectData: [],
      projectPageNum: 1,
      projectPageSize: 5,
      projectTotal: 0,
      projectVisible: false,
      projectForm: {},
    }
  },
  created() {
    this.loadPoor(1)
    this.loadProject(1)
  },
  methods: {
    handlePoorAdd() {
      this.poorForm = {
        userId: this.user.id,
        status: '待审核'
      }
      this.poorVisible = true
    },
    handleProjectAdd() {
      this.projectForm = {
        userId: this.user.id,
        status: '待审核'
      }
      this.projectVisible = true
    },
    handlePoorEdit(row) {
      this.poorForm = JSON.parse(JSON.stringify(row))
      this.poorVisible = true
    },
    handleProjectEdit(row) {
      this.projectForm = JSON.parse(JSON.stringify(row))
      this.projectVisible = true
    },
    poorSave() {
      if (this.poorForm.id) {
        this.poorForm.status = '待审核'
        this.poorForm.descr = ''
      }
      this.$request({
        url: this.poorForm.id ? '/poorapply/update' : '/poorapply/add',
        method: this.poorForm.id ? 'PUT' : 'POST',
        data: this.poorForm
      }).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.loadPoor(1)
          this.poorVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    projectSave() {
      if (this.projectForm.id) {
        this.projectForm.status = '待审核'
        this.projectForm.descr = ''
      }
      this.$request({
        url: this.projectForm.id ? '/projectapply/update' : '/projectapply/add',
        method: this.projectForm.id ? 'PUT' : 'POST',
        data: this.projectForm
      }).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.loadProject(1)
          this.projectVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    delPoor(id) {
      this.$confirm('您确定要撤销申请吗？', '提示', {type: "warning"}).then(response => {
        this.$request.delete('/poorapply/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功')
            this.loadPoor(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    delProject(id) {
      this.$confirm('您确定要撤销申请吗？', '提示', {type: "warning"}).then(response => {
        this.$request.delete('/projectapply/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功')
            this.loadProject(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    loadPoor(pageNum) {
      if (pageNum) this.poorPageNum = pageNum
      this.$request.get('/poorapply/selectPage', {
        params: {
          pageNum: this.poorPageNum,
          pageSize: this.poorPageSize,
          userId: this.user.id
        }
      }).then(res => {
        this.poorData = res.data?.list
        this.poorTotal = res.data?.total
      })
    },
    loadProject(pageNum) {
      if (pageNum) this.projectPageNum = pageNum
      this.$request.get('/projectapply/selectPage', {
        params: {
          pageNum: this.projectPageNum,
          pageSize: this.projectPageSize,
          userId: this.user.id
        }
      }).then(res => {
        this.projectData = res.data?.list
        this.projectTotal = res.data?.total
      })
    },
    handlePoorChange(pageNum) {
      this.loadPoor(pageNum)
    },
    handleProjectChange(pageNum) {
      this.loadProject(pageNum)
    },
    handlePoorSuccess(response) {
      this.poorForm.file = response.data
    },
    handleProjectSuccess(response) {
      this.projectForm.file = response.data
    },
    downPoor(url) {
      window.open(url, '_blank')
    },
    downProject(url) {
      window.open(url, '_blank')
    },
    getStatusType(status) {
      switch(status) {
        case '审核通过':
          return 'success'
        case '待审核':
          return 'warning'
        case '审核不通过':
          return 'danger'
        default:
          return 'info'
      }
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/theme/front_css/apply.css";
</style>
