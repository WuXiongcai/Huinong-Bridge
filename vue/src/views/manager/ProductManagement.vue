<template>
  <div class="product-management">
    <el-card class="header-card">
      <div slot="header" class="clearfix">
        <span class="header-title">农产品列表</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="showHelp">使用帮助</el-button>
      </div>
      <el-row :gutter="24">
        <el-col :span="8">
          <div class="stat-card">
            <el-statistic title="总产品数" :value="totalProducts">
              <template slot="suffix">
                <i class="el-icon-goods" style="color: #409EFF; font-size: 20px;"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card">
            <el-statistic title="本月新增农产品" :value="monthlyNewProducts">
              <template slot="suffix">
                <i class="el-icon-s-data" style="color: #67C23A; font-size: 20px;"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card">
            <el-statistic title="帮扶农户" :value="helpedFarmers">
              <template slot="suffix">
                <i class="el-icon-user" style="color: #E6A23C; font-size: 20px;"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="search-card">
      <div class="search-operation-container">
        <div class="search-area">
          <el-input
            placeholder="请输入农产品名称查询"
            style="width: 200px"
            prefix-icon="el-icon-search"
            v-model="name"
            clearable
          ></el-input>
          <el-select
            v-model="category"
            placeholder="选择农产品类别"
            clearable
            style="width: 150px; margin-left: 10px;"
            @change="handleCategoryChange"
          >
            <el-option
              v-for="item in categories"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <el-button type="primary" plain icon="el-icon-search" @click="load(1)">查询</el-button>
          <el-button type="info" plain icon="el-icon-refresh" @click="reset">重置</el-button>
        </div>
        <div class="operation-area">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增农产品</el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            :disabled="!ids.length"
            @click="delBatch"
          >批量删除</el-button>
          <el-button type="info" icon="el-icon-download" @click="exportData">导出数据</el-button>
        </div>
      </div>
    </el-card>

    <el-card class="table-card">
      <el-table
        :data="tableData"
        stripe
        border
        @selection-change="handleSelectionChange"
        :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center" fixed></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" fixed></el-table-column>
        <el-table-column prop="image" label="农产品图片" width="120" align="center">
          <template v-slot="scope">
            <div class="image-container">
              <el-image
                style="width: 50px; height: 50px; border-radius: 4px;"
                v-if="scope.row.image"
                :src="scope.row.image"
                :preview-src-list="[scope.row.image]"
                fit="cover"
              ></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="农产品名称" min-width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="category" label="农产品类别" width="120"></el-table-column>
        <el-table-column prop="price" label="农产品价格" width="120" align="right">
          <template v-slot="scope">
            ¥{{ scope.row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" align="right"></el-table-column>
        <el-table-column prop="origin" label="产地" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="农产品描述" min-width="120">
          <template v-slot="scope">
            <el-button type="text" @click="viewData(scope.row.description)">查看详情</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="del(scope.row.id)">删除</el-button>
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
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>

    <el-dialog title="新增农产品" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item label="农产品图片">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="农产品名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item prop="category" label="农产品类别">
          <el-select v-model="form.category" placeholder="请选择农产品类别">
            <el-option v-for="item in categories" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="price" label="农产品价格">
          <el-input-number v-model="form.price" :precision="2" :step="0.1" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item prop="stock" label="库存">
          <el-input-number v-model="form.stock" :min="0" :step="1"></el-input-number>
        </el-form-item>
        <el-form-item prop="origin" label="产地">
          <el-input v-model="form.origin" placeholder="请输入农产品产地"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="农产品描述">
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="使用帮助" :visible.sync="helpVisible" width="30%">
      <div>
        <h4>快速入门：</h4>
        <p>1. 使用搜索栏快速查找农产品，也可以分类别查询</p>
        <p>2. 点击"新增商品"添加新的农产品</p>
        <p>3. 使用批量删除功能可以同时删除多个农产品</p>
        <p>4. 点击"导出数据"可以下载当前商品列表</p>
      </div>
    </el-dialog>
    <el-dialog title="农产品详情描述" :visible.sync="editorVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="this.viewContent" class="w-e-text"></div>
    </el-dialog>

    <div class="page-component__scroll">
      <el-backtop :bottom="100">
        <div :style="{
          height: '40px',
          width: '40px',
          backgroundColor: '#f2f5f6',
          boxShadow: '0 0 6px rgba(0,0,0, .12)',
          textAlign: 'center',
          lineHeight: '40px',
          color: '#19a14b'
        }">
          up
        </div>
      </el-backtop>
    </div>

  </div>
</template>

<script>
import E from 'wangeditor'
import { saveAs } from 'file-saver'
import * as XLSX from 'xlsx'
export default {
  name: "Products",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      category: null,
      helpVisible: false,
      fromVisible: false,
      editorVisible: false,
      editor: null,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      rules: {
        name: [{required: true, message: '请输入农产品名称', trigger: 'blur'}],
        category: [{required: true, message: '请选择农产品类别', trigger: 'change'}],
        price: [{required: true, message: '请输入农产品价格', trigger: 'blur'}],
        stock: [{required: true, message: '请输入库存数量', trigger: 'blur'}],
        origin: [
          { required: true, message: '请输入农产品产地', trigger: 'blur' },
          { min: 2, max: 50, message: '产地长度在 2 到 50 个字符', trigger: 'blur' }
        ]
      },
      totalProducts: 0,
      monthlyNewProducts: 0,
      helpedFarmers: 0,
      ids: [],
      loading: false,
      categories: [
        {value: '水果', label: '水果'},
        {value: '蔬菜', label: '蔬菜'},
        {value: '粮食', label: '粮食'},
        {value: '畜牧', label: '畜牧'},
        {value: '土特产', label: '土特产'},
        {value: '手工艺品', label: '手工艺品'}
      ],
      viewContent: null
    }
  },
  created() {
    this.load(1)
    this.loadStatistics()
  },
  methods: {
    initWangEditor(description) {
      this.$nextTick(() => {
        this.editor = new E('#editor')
        this.editor.config.placeholder = '请输入农产品详情描述'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload'
        this.editor.create()
        setTimeout(() => {
          this.editor.txt.html(description)
        })
      })
    },
    viewData(description) {
      this.viewContent = description
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
      this.initWangEditor(this.form.description || '')
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.description = this.editor.txt.html();
          this.$request({
            url: this.form.id ? '/products/update' : '/products/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功');
              this.load(1);
              this.fromVisible = false;
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/products/delete/' + id).then(res => {
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
        this.$request.delete('/products/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/products/selectPage', {
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
      this.category = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleAvatarSuccess(response, file, fileList) {
      this.form.image = response.data
    },
    exportData() {
      // Fetch all data for export
      this.$request.get('/products/selectAll', {
        params: {
          name: this.name,
          category: this.category
        }
      }).then(res => {
        const dataToExport = res.data.map(item => ({
          ID: item.id,
          Name: item.name,
          Category: item.category,
          Price: item.price,
          Stock: item.stock,
          Origin: item.origin,
          Description: item.description
        }));

        // Create a new workbook and add the data
        const wb = XLSX.utils.book_new();
        const ws = XLSX.utils.json_to_sheet(dataToExport);
        XLSX.utils.book_append_sheet(wb, ws, "Products");

        // Generate the Excel file
        const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'array' });

        // Save the file
        const blob = new Blob([wbout], { type: 'application/octet-stream' });
        saveAs(blob, 'agricultural_products.xlsx');

        this.$message.success('数据导出成功');
      }).catch(() => {
        this.$message.error('数据导出失败');
      });
    },
    showHelp() {
      this.helpVisible = true
    },
    loadStatistics() {
      this.$request.get('/products/statistics').then(res => {
        if (res.code === '200') {
          this.totalProducts = res.data.totalProducts;
          this.monthlyNewProducts = res.data.monthlyNewProducts;
          this.helpedFarmers = res.data.helpedFarmers; // This will always be 99
        } else {
          this.$message.error('Failed to load statistics');
        }
      }).catch(error => {
        console.error('Error loading statistics:', error);
        this.$message.error('Failed to load statistics');
      });
    },
    handleCategoryChange() {
      this.load(1)
    }
  }
}
</script>

<style scoped>
.product-management {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 60px);
}

.header-card {
  margin-bottom: 20px;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.stat-card {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px 0 rgba(0,0,0,0.15);
}

.search-card {
  margin-bottom: 20px;
}

.search-operation-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

.search-area {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.operation-area {
  display: flex;
  gap: 10px;
}

.table-card {
  margin-bottom: 20px;
}

.image-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .search-operation-container {
    flex-direction: column;
    align-items: stretch;
  }
  
  .operation-area {
    justify-content: flex-start;
  }
  
  .el-table {
    width: 100%;
    overflow-x: auto;
  }
}
</style>