<template>
  <div class="orders-container">
    <el-card class="header-card" :body-style="{ padding: '20px' }">
      <div slot="header" class="card-header">
        <span class="header-title">扶贫助农订单管理</span>
        <el-button type="text" @click="showHelp">
          <i class="el-icon-question"></i>
          使用帮助
        </el-button>
      </div>
      <el-row :gutter="20" class="statistics-row">
        <el-col :xs="24" :sm="8">
          <div class="statistic-card">
            <el-statistic title="总订单数" :value="totalOrders">
              <template slot="suffix">
                <i class="el-icon-s-order statistic-icon"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :xs="24" :sm="8">
          <div class="statistic-card">
            <el-statistic title="待发货" :value="pendingShipments">
              <template slot="suffix">
                <i class="el-icon-goods statistic-icon warning"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :xs="24" :sm="8">
          <div class="statistic-card">
            <el-statistic title="本月销售额" :value="monthlySales" :precision="2">
              <template slot="prefix">¥</template>
              <template slot="suffix">
                <i class="el-icon-money statistic-icon success"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="search-card">
      <div class="search-container">
        <div class="search-items">
          <el-input
            placeholder="请输入订单编号"
            v-model="orderId"
            clearable
            prefix-icon="el-icon-document"
            class="search-item"
          ></el-input>
          <el-select
            v-model="status"
            placeholder="请选择订单状态"
            clearable
            class="search-item"
          >
            <el-option label="待发货" value="待发货"></el-option>
            <el-option label="已发货" value="已发货"></el-option>
          </el-select>
        </div>
        <div class="search-buttons">
          <el-button type="primary" icon="el-icon-search" @click="load(1)">查询</el-button>
          <el-button icon="el-icon-refresh" @click="reset">重置</el-button>
        </div>
      </div>
    </el-card>

    <el-card class="table-card">
      <el-table
        :data="tableData"
        stripe
        border
        v-loading="loading"
        :header-cell-style="{ background: '#f5f7fa' }"
        style="width: 100%"
      >
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="img" label="商品图片" width="100">
          <template v-slot="scope">
            <div class="image-container">
              <el-image
                :src="scope.row.img"
                :preview-src-list="[scope.row.img]"
                fit="cover"
                class="product-image"
              ></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="商品名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="price" label="订单价格"></el-table-column>
        <el-table-column prop="orderId" label="订单编号"></el-table-column>
        <el-table-column prop="userName" label="下单用户"></el-table-column>
        <el-table-column prop="time" label="下单时间"></el-table-column>
        <el-table-column prop="status" label="订单状态" width="100"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" icon="el-icon-check" size="mini" v-if="scope.row.status === '待发货'" @click="changeStatus(scope.row, '已发货')">发货</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          background
        >
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Orders",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      orderId: null,
      status: null,
      dateRange: [],
      detailVisible: false,
      currentOrder: {},
      helpVisible: false,
      user: JSON.parse(localStorage.getItem('xm-admin') || '{}'),
      loading: false,
      totalOrders: 0,
      pendingShipments: 0,
      monthlySales: 0,
    }
  },
  created() {
    this.load(1)
    this.loadStatistics()
  },
  methods: {
    changeStatus(row, status) {
      let data = JSON.parse(JSON.stringify(row))
      data.status = status
      this.$request.put('/orders/update', data).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          this.load(1)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/orders/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          orderId: this.orderId,
          status: this.status,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.orderId = null
      this.status = null
      this.dateRange = []
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    showHelp() {
      this.helpVisible = true
    },
    loadStatistics() {
      this.$request.get('/orders/statistics').then(res => {
        if (res.code === '200') {
          this.totalOrders = res.data.totalOrders
          this.pendingShipments = res.data.pendingShipments
          this.monthlySales = res.data.monthlySales
        }
      }).catch(() => {
        this.$message.error('加载统计数据失败')
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.load(1)
    },
  }
}
</script>

<style scoped>
.orders-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 60px);
}

.header-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.statistics-row {
  margin-top: 10px;
}

.statistic-card {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  transition: all 0.3s;
}

.statistic-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.statistic-icon {
  font-size: 20px;
  margin-left: 5px;
}

.statistic-icon.warning {
  color: #E6A23C;
}

.statistic-icon.success {
  color: #67C23A;
}

.search-card {
  margin-bottom: 20px;
}

.search-container {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 10px;
}

.search-items {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.search-item {
  width: 200px;
}

.table-card {
  margin-bottom: 20px;
}

.image-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.product-image {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  object-fit: cover;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .search-container {
    flex-direction: column;
  }
  
  .search-items {
    width: 100%;
  }
  
  .search-item {
    width: 100%;
  }
  
  .search-buttons {
    width: 100%;
    display: flex;
    justify-content: flex-end;
    margin-top: 10px;
  }
}
</style>
