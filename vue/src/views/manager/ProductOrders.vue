<template>
  <div class="product-orders">
    <el-card class="header-card">
      <div slot="header" class="clearfix">
        <span class="header-title">农产品订单管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="showHelp">使用帮助</el-button>
      </div>
      <el-row :gutter="20" class="statistics-row">
        <el-col :xs="24" :sm="8">
          <el-card shadow="hover" class="statistics-card">
            <div class="statistics-content">
              <div class="statistics-icon">
                <i class="el-icon-s-order"></i>
              </div>
              <div class="statistics-info">
                <div class="statistics-title">总订单数</div>
                <div class="statistics-value">{{ totalOrders }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-card shadow="hover" class="statistics-card">
            <div class="statistics-content">
              <div class="statistics-icon warning">
                <i class="el-icon-goods"></i>
              </div>
              <div class="statistics-info">
                <div class="statistics-title">待发货</div>
                <div class="statistics-value">{{ pendingShipments }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="8">
          <el-card shadow="hover" class="statistics-card">
            <div class="statistics-content">
              <div class="statistics-icon success">
                <i class="el-icon-money"></i>
              </div>
              <div class="statistics-info">
                <div class="statistics-title">本月销售额</div>
                <div class="statistics-value">¥{{ monthlySales.toFixed(2) }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item>
          <el-input v-model="searchForm.orderId" placeholder="请输入订单号" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="searchForm.customerName" placeholder="请输入客户姓名" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="searchForm.status" placeholder="请选择订单状态" clearable>
            <el-option label="全部" value=""></el-option>
            <el-option label="待发货" value="待发货"></el-option>
            <el-option label="已发货" value="已发货"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="load(1)">查询</el-button>
          <el-button type="warning" icon="el-icon-refresh" @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table
          :data="tableData"
          stripe
          border
          style="width: 100%"
          :header-cell-style="{ background: '#f5f7fa' }"
          v-loading="loading">
        <el-table-column prop="id" label="序号" width="80" align="center" fixed></el-table-column>
        <el-table-column prop="orderId" label="订单号" min-width="180" show-overflow-tooltip fixed></el-table-column>
        <el-table-column prop="productName" label="商品名称" min-width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="customerName" label="客户姓名" min-width="120"></el-table-column>
        <el-table-column prop="time" label="下单时间" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="quantity" label="数量" width="80" align="center"></el-table-column>
        <el-table-column prop="totalAmount" label="总金额" min-width="120">
          <template slot-scope="scope">
            <span class="amount">¥{{ scope.row.totalAmount.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="medium">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" plain @click="viewOrderDetails(scope.row)">查看详情</el-button>
            <el-button 
              size="mini" 
              type="success" 
              plain
              @click="updateOrderStatus(scope.row)" 
              v-if="scope.row.status === '待发货'">发货</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </el-card>

    <el-dialog title="订单详情" :visible.sync="orderDetailsVisible" width="50%">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="订单ID">{{ currentOrder.id }}</el-descriptions-item>
        <el-descriptions-item label="订单号">{{ currentOrder.orderId }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ currentOrder.productName }}</el-descriptions-item>
        <el-descriptions-item label="客户姓名">{{ currentOrder.customerName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentOrder.phone }}</el-descriptions-item>
        <el-descriptions-item label="配送地址">{{ currentOrder.address }}</el-descriptions-item>
        <el-descriptions-item label="数量">{{ currentOrder.quantity }}</el-descriptions-item>
        <el-descriptions-item label="总金额">¥{{ currentOrder.totalAmount?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentOrder.status)">{{ currentOrder.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="快递公司" v-if="currentOrder.status !== '待发货'">
          {{ currentOrder.transportCompany || '未指定' }}
        </el-descriptions-item>
        <el-descriptions-item label="下单日期">{{ currentOrder.time }}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="orderDetailsVisible = false">关闭</el-button>
        <el-button type="primary" @click="updateOrderStatus(currentOrder)" v-if="currentOrder.status === '待发货'">发货</el-button>
      </span>
    </el-dialog>

    <el-dialog title="使用帮助" :visible.sync="helpVisible" width="30%">
      <div>
        <h4>快速入门：</h4>
        <p>1. 使用搜索栏快速查找订单</p>
        <p>2. 点击"查看详情"按钮查看订单完整信息</p>
        <p>3. 点击"发货"按钮处理待发货的订单</p>
        <p>4. 及时处理订单以提高农户收益和用户满意度</p>
      </div>
    </el-dialog>
    <el-dialog title="选择快递公司" :visible.sync="shippingDialogVisible" width="30%">
      <el-select v-model="selectedShippingCompany" placeholder="请选择快递公司">
        <el-option
            v-for="company in shippingCompanies"
            :key="company"
            :label="company"
            :value="company">
        </el-option>
      </el-select>
      <span slot="footer" class="dialog-footer">
        <el-button @click="shippingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmShipping">确认发货</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ProductOrders",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      searchForm: {
        orderId: null,
        customerName: null,
        status: null
      },
      orderDetailsVisible: false,
      currentOrder: {},
      helpVisible: false,
      totalOrders: 0,
      pendingShipments: 0,
      monthlySales: 0,
      shippingCompanies: [
        '极兔快递', '中通快递', '韵达快递', '圆通速递',
        '邮政快递', '申通快递', '百世快递', '顺丰快递'
      ],
      selectedShippingCompany: '',
      shippingDialogVisible: false,
      loading: false,
    }
  },
  created() {
    this.load(1)
    this.loadStatistics()
  },
  methods: {
    load(pageNum) {
      this.loading = true
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/productorders/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          orderId: this.searchForm.orderId,
          customerName: this.searchForm.customerName,
          status: this.searchForm.status,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.list
          this.total = res.data.total
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => {
        this.loading = false
      })
    },
    reset() {
      this.searchForm = {
        orderId: null,
        customerName: null,
        status: null
      }
      this.load(1)
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.load(1)
    },
    handleCurrentChange(val) {
      this.load(val)
    },
    viewOrderDetails(order) {
      this.currentOrder = {...order}
      this.orderDetailsVisible = true
    },
    updateOrderStatus(order) {
      this.currentOrder = order;
      this.selectedShippingCompany = '';
      this.shippingDialogVisible = true;
    },
    confirmShipping() {
      if (!this.selectedShippingCompany) {
        this.$message.warning('请选择快递公司');
        return;
      }

      this.$confirm('确定将订单状态更新为已发货吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const updatedOrder = {
          ...this.currentOrder,
          status: '已发货',
          transportCompany: this.selectedShippingCompany
        };
        this.$request.put('/productorders/update', updatedOrder).then(res => {
          if (res.code === '200') {
            this.$message.success(`订单 ${this.currentOrder.orderId} 已更新为已发货状态`);
            this.load(this.pageNum);
            this.loadStatistics();
            if (this.orderDetailsVisible) {
              this.currentOrder.status = '已发货';
              this.currentOrder.transportCompany = this.selectedShippingCompany;
            }
            this.shippingDialogVisible = false;
          } else {
            this.$message.error(res.msg);
          }
        });
      }).catch(() => {
        this.$message.info('已取消操作');
      });
    },
    getStatusType(status) {
      switch (status) {
        case '待发货':
          return 'warning'
        case '已发货':
          return 'primary'
        case '已完成':
          return 'success'
        default:
          return 'info'
      }
    },
    showHelp() {
      this.helpVisible = true
    },
    loadStatistics() {
      this.$request.get('/productorders/statistics').then(res => {
        if (res.code === '200') {
          this.totalOrders = res.data.totalOrders
          this.pendingShipments = res.data.pendingShipments
          this.monthlySales = res.data.monthlySales
        } else {
          this.$message.error('加载统计数据失败')
        }
      })
    }
  }
}
</script>

<style scoped>
.product-orders {
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
}

.search {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.statistics-row {
  margin-bottom: 20px;
}

.statistics-card {
  height: 108px;
  transition: all 0.3s;
}

.statistics-card:hover {
  transform: translateY(-2px);
}

.statistics-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.statistics-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  background-color: #409EFF;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.statistics-icon i {
  font-size: 24px;
  color: #fff;
}

.statistics-icon.warning {
  background-color: #E6A23C;
}

.statistics-icon.success {
  background-color: #67C23A;
}

.statistics-info {
  flex: 1;
}

.statistics-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.statistics-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.search-form .el-form-item {
  margin-bottom: 0;
  margin-right: 10px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.amount {
  color: #f56c6c;
  font-weight: bold;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .statistics-row .el-col {
    margin-bottom: 20px;
  }
  
  .search-form {
    flex-direction: column;
  }
  
  .search-form .el-form-item {
    margin-right: 0;
    width: 100%;
  }
}
</style>

