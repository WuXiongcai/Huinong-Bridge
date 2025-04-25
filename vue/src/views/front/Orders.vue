<template>
  <div class="main-content">
    <h2 class="page-title">我的订单</h2>
    <div class="orders-container">
      <h2 class="page-title">特价商品订单 <span class="order-count">({{ tableData.length }})</span></h2>

      <el-card class="order-summary">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="summary-item">
              <i class="el-icon-s-order"></i>
              <div>
                <h3>总订单</h3>
                <p>{{ total }}</p>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <i class="el-icon-s-cooperation"></i>
              <div>
                <h3>待收货</h3>
                <p>{{ tableData.filter(item => item.status === '已发货').length }}</p>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <i class="el-icon-circle-check"></i>
              <div>
                <h3>已完成</h3>
                <p>{{ tableData.filter(item => item.status === '已完成').length }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <el-table :data="tableData" stripe class="order-table" :size="screenWidth <= 768 ? 'mini' : 'medium'">
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="img" label="商品图片" width="100" :width="screenWidth <= 768 ? '60' : '100'">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image :style="{ width: screenWidth <= 768 ? '40px' : '60px', height: screenWidth <= 768 ? '40px' : '60px', borderRadius: '5px' }" v-if="scope.row.img"
                        :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="商品名称" show-overflow-tooltip>
          <template v-slot="scope">
            <div class="goods-name">{{ scope.row.goodsName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="订单价格">
          <template v-slot="scope">
            <span class="price">¥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderId" label="订单编号" width="180"></el-table-column>
        <el-table-column prop="userName" label="收货人"></el-table-column>
        <el-table-column prop="time" label="下单时间" width="180"></el-table-column>
        <el-table-column prop="status" label="订单状态" width="100">
          <template v-slot="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" size="mini" v-if="scope.row.status === '已发货'" icon="el-icon-check" @click="changeStatus(scope.row, '已完成')">确认收货</el-button>
            <el-button plain type="danger" size="mini" v-if="scope.row.status === '已完成'" icon="el-icon-delete" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>

      <!-- 新增农产品订单列表 -->
      <h2 class="page-title">农产品订单 <span class="order-count">({{ productOrdersData.filter(order => order.customerName === user.name).length }})</span></h2>

      <el-card class="order-summary">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="summary-item">
              <i class="el-icon-s-order"></i>
              <div>
                <h3>总订单</h3>
                <p>{{ productOrdersData.filter(order => order.customerName === user.name).length }}</p>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <i class="el-icon-s-cooperation"></i>
              <div>
                <h3>待收货</h3>
                <p>{{ productOrdersData.filter(order => order.customerName === user.name && order.status === '已发货').length }}</p>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <i class="el-icon-circle-check"></i>
              <div>
                <h3>已完成</h3>
                <p>{{ productOrdersData.filter(order => order.customerName === user.name && order.status === '已完成').length }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <el-table :data="productOrdersData.filter(order => order.customerName === user.name)" stripe class="order-table" :size="screenWidth <= 768 ? 'mini' : 'medium'">
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="productName" label="农产品名称" show-overflow-tooltip>
          <template v-slot="scope">
            <div class="goods-name">{{ scope.row.productName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="订单价格">
          <template v-slot="scope">
            <span class="price">¥{{ scope.row.totalAmount.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderId" label="订单编号" width="180"></el-table-column>
        <el-table-column prop="customerName" label="收货人"></el-table-column>
        <el-table-column prop="time" label="下单时间" width="180"></el-table-column>
        <el-table-column prop="status" label="订单状态" width="100">
          <template v-slot="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" size="mini" v-if="scope.row.status === '已发货'" icon="el-icon-check" @click="changeProductOrderStatus(scope.row, '已完成')">确认收货</el-button>
            <el-button plain type="danger" size="mini" v-if="scope.row.status === '已完成'" icon="el-icon-delete" @click="delProductOrder(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleProductOrdersCurrentChange"
            :current-page="productOrdersPageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="productOrdersPageSize"
            layout="total, prev, pager, next, jumper"
            :total="productOrdersData.filter(order => order.customerName === user.name).length">
        </el-pagination>
      </div>
    </div>
    <el-backtop :bottom="100">
      <div class="backtop-inner">
        <i class="el-icon-caret-top"></i>
      </div>
    </el-backtop>
  </div>
</template>



<script>
export default {
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      orderId: null,
      status: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      // 新增农产品订单相关数据
      productOrdersData: [],
      productOrdersPageNum: 1,
      productOrdersPageSize: 5,
      productOrdersTotal: 0,
      screenWidth: window.innerWidth,
    }
  },
  created() {
    this.load(1)
    this.loadProductOrders(1)
  },
  mounted() {
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
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
    del(id) {
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/orders/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
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
      this.$request.get('/orders/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.user.id
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    getStatusType(status) {
      switch (status) {
        case '已发货':
          return 'warning'
        case '已完成':
          return 'success'
        default:
          return 'info'
      }
    },
    // 新增农产品订单相关方法
    loadProductOrders(pageNum) {
      if (pageNum) this.productOrdersPageNum = pageNum
      this.$request.get('/productorders/selectPage', {
        params: {
          pageNum: this.productOrdersPageNum,
          pageSize: this.productOrdersPageSize,
          userId: this.user.id,
          customerName: this.user.name
        }
      }).then(res => {
        this.productOrdersData = res.data?.list
        this.productOrdersTotal = res.data?.total
      })
    },
    handleProductOrdersCurrentChange(pageNum) {
      this.loadProductOrders(pageNum)
    },
    changeProductOrderStatus(row, status) {
      let data = JSON.parse(JSON.stringify(row))
      data.status = status
      this.$request.put('/productorders/update', data).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          this.loadProductOrders(1)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    delProductOrder(id) {
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/productorders/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.loadProductOrders(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    handleResize() {
      this.screenWidth = window.innerWidth
    },
  }
}
</script>


<style scoped>
@import "@/assets/css/theme/front_css/orders.css";
</style>

