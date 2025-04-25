<template>
  <div class="goods-container">
    <el-card class="header-card">
      <div slot="header" class="clearfix">
        <span class="header-title">特价扶贫助农商品管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="showHelp">使用帮助</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-statistic title="总商品数" :value="totalGoods">
            <template slot="suffix">
              <i class="el-icon-goods" style="color: #409EFF;"></i>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="8">
          <el-statistic title="本月新增商品" :value="monthlyNewGoods">
            <template slot="suffix">
              <i class="el-icon-s-data" style="color: #67C23A;"></i>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="8">
          <el-statistic title="帮扶农户" :value="helpedFarmers">
            <template slot="suffix">
              <i class="el-icon-user" style="color: #E6A23C;"></i>
            </template>
          </el-statistic>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="content-card">
      <div class="toolbar">
        <div class="search-area">
          <el-input
            placeholder="请输入商品名称查询"
            style="width: 220px"
            prefix-icon="el-icon-search"
            v-model="name"
            clearable
          ></el-input>
          <el-select
            v-model="category"
            placeholder="选择商品类别"
            clearable
            style="width: 160px; margin-left: 16px;"
            @change="handleCategoryChange"
          >
            <el-option
              v-for="item in categories"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <el-button type="primary" plain style="margin-left: 16px" icon="el-icon-search" @click="load(1)">查询</el-button>
          <el-button type="info" plain style="margin-left: 10px" icon="el-icon-refresh" @click="reset">重置</el-button>
        </div>

        <div class="operation-area">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增商品</el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            :disabled="!ids.length"
            @click="delBatch"
          >批量删除</el-button>
          <el-button type="info" icon="el-icon-download" @click="exportData">导出数据</el-button>
        </div>
      </div>

      <el-table
        :data="tableData"
        stripe
        border
        highlight-current-row
        @selection-change="handleSelectionChange"
        class="goods-table"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="img" label="商品图片">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px" v-if="scope.row.img"
                        :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="category" label="商品类别"></el-table-column>
        <el-table-column prop="price" label="商品价格"></el-table-column>
        <el-table-column prop="num" label="库存" sortable></el-table-column>
        <el-table-column prop="sales" label="销量" sortable></el-table-column>
        <el-table-column prop="farmer" label="供应农户"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-button plain type="danger" icon="el-icon-delete" size="mini" @click=del(scope.row.id)>删除</el-button>
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

    <el-dialog title="新增商品" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item label="商品图片">
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
        <el-form-item prop="name" label="商品名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item prop="category" label="商品类别">
          <el-select v-model="form.category" placeholder="请选择商品类别">
            <el-option v-for="item in categories" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="price" label="商品价格">
          <el-input-number v-model="form.price" :precision="2" :step="0.1" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item prop="num" label="商品库存">
          <el-input-number v-model="form.num" :min="0" :step="1"></el-input-number>
        </el-form-item>
        <el-form-item prop="farmer" label="供应农户">
          <el-input v-model="form.farmer" autocomplete="off"></el-input>
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
        <p>1. 使用搜索栏快速查找商品</p>
        <p>2. 点击"新增商品"添加新的扶贫商品</p>
        <p>3. 使用批量删除功能可以同时删除多个商品</p>
        <p>4. 点击"导出数据"可以下载当前商品列表</p>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { saveAs } from 'file-saver'
import * as XLSX from 'xlsx'
export default {
  name: "Goods",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 6,  // 每页显示的个数
      total: 0,
      name: null,
      category: null,
      helpVisible: false,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        category: [{ required: true, message: '请选择商品类别', trigger: 'change' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
        num: [{ required: true, message: '请输入库存数量', trigger: 'blur' }],
        farmer: [{ required: true, message: '请输入供应农户', trigger: 'blur' }]
      },
      totalGoods: 0,
      monthlyNewGoods: 0,
      helpedFarmers: 0,
      ids: [],
      loading: false,
      categories: [
        { value: '水果', label: '水果' },
        { value: '蔬菜', label: '蔬菜' },
        { value: '粮食', label: '粮食' },
        { value: '畜牧', label: '畜牧' },
        { value: '水产', label: '水产' },
        { value: '手工艺品', label: '手工艺品' }
      ],
    }
  },
  created() {
    this.load(1)
    this.loadStatistics()
  },
  methods: {
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/goods/update' : '/goods/add',
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
        this.$request.delete('/goods/delete/' + id).then(res => {
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
        this.$request.delete('/goods/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/goods/selectPage', {
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
      this.form.img = response.data
    },
    exportData() {
      // Fetch all data for export
      this.$request.get('/goods/selectAll', {
        params: {
          name: this.name,
          category: this.category
        }
      }).then(res => {
        const dataToExport = res.data.map(item => ({
          ID: item.id,
          "商品名称": item.name,
          "种类": item.category,
          "价格": item.price,
          "库存": item.num,
          "销量": item.sales,
          "供应农户": item.farmer
        }));

        // Create a new workbook and add the data
        const wb = XLSX.utils.book_new();
        const ws = XLSX.utils.json_to_sheet(dataToExport);
        XLSX.utils.book_append_sheet(wb, ws, "Goods");

        // Generate the Excel file
        const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'array' });

        // Save the file
        const blob = new Blob([wbout], { type: 'application/octet-stream' });
        saveAs(blob, 'goods_data.xlsx');

        this.$message.success('数据导出成功');
      }).catch(() => {
        this.$message.error('数据导出失败');
      });
    },
    showHelp() {
      this.helpVisible = true
    },
    loadStatistics() {
      this.$request.get('/goods/statistics').then(res => {
        if (res.code === '200') {
          this.totalGoods = res.data.totalGoods;
          this.monthlyNewGoods = res.data.monthlyNewGoods;
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
.goods-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 60px);
}

.header-card {
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.content-card {
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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
  flex-wrap: wrap;
}

.goods-table {
  margin: 20px 0;
  border-radius: 4px;
}

.goods-table ::v-deep .el-table__header-wrapper th {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: bold;
  height: 50px;
}

.goods-table ::v-deep .el-table__row {
  height: 55px;
}

.pagination {
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
  transition: all 0.3s;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .toolbar {
    flex-direction: column;
    gap: 15px;
  }
  
  .search-area,
  .operation-area {
    width: 100%;
    justify-content: flex-start;
  }
  
  .el-input,
  .el-select {
    width: 100% !important;
    margin-left: 0 !important;
  }
}
</style>
