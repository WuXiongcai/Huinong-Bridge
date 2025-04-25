<template>
  <div class="product-list">
    <div class="title">助农农产品购买</div>
    <hr class="gradient-line">

    <el-card class="platform-announcement" shadow="hover">
      <div slot="header" class="clearfix">
        <span class="announcement-title">惠农云桥平台公告</span>
        <el-button style="float: right; padding: 3px 0" type="text">查看更多</el-button>
      </div>
      <div class="announcement-content">
        <p>欢迎来到惠农云桥惠州市龙门县乡村旅游与助农平台！我们承诺：</p>
        <ul>
          <li>所有农产品均由官方包邮直接送到您家门口</li>
          <li>品质保证，不满意无条件退换</li>
          <li>您的每一次购买都在直接帮助贫困地区的农民</li>
          <li>所有农产品都是低收入农户提供的产品，此助农平台为"爱心直购"通道</li>
          <li>定期举办线上直播，与农户面对面交流</li>
        </ul>
        <p class="highlight">购买我们的产品，不仅能享受优质农产品，更能为扶贫事业贡献一份力量！</p>
      </div>
    </el-card>

    <div class="search-section">
      <el-input
          v-model="searchName"
          placeholder="请输入农产品名称查询"
          prefix-icon="el-icon-search"
      ></el-input>
      <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
      <el-button type="warning" icon="el-icon-refresh" @click="resetSearch">重置</el-button>
      <el-badge :value="cartItems.length" class="cart-badge" v-show="cartItems.length">
        <el-button type="primary" icon="el-icon-shopping-cart-2" circle @click="cartDrawerVisible = true"></el-button>
      </el-badge>
    </div>

    <el-tabs v-model="activeCategory" @tab-click="handleCategoryChange" class="category-tabs">
      <el-tab-pane label="全部" name="all"></el-tab-pane>
      <el-tab-pane label="水果" name="水果"></el-tab-pane>
      <el-tab-pane label="蔬菜" name="蔬菜"></el-tab-pane>
      <el-tab-pane label="粮食" name="粮食"></el-tab-pane>
      <el-tab-pane label="畜牧" name="畜牧"></el-tab-pane>
      <el-tab-pane label="土特产" name="土特产"></el-tab-pane>
      <el-tab-pane label="手工艺品" name="手工艺品"></el-tab-pane>
    </el-tabs>
    
    <section class="products-section">
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="6" :lg="4.8" :xl="4.8" v-for="product in filteredProducts" :key="product.id">
          <el-card :body-style="{ padding: '0px' }" class="product-card" shadow="hover">
            <img :src="product.image" :alt="product.name" class="product-image">
            <div class="product-info">
              <h3 class="product-name">{{ product.name }}</h3>
              <div class="product-details">
                <div class="price-stock">
                  <span class="product-price">{{ product.price.toFixed(2) }}</span>
                  <span class="product-stock">(库存: {{ product.stock }})</span>
                </div>
                <span class="product-origin">{{ product.origin }}</span>
              </div>
              <div class="product-buttons">
                <el-button type="warning" size="small" @click="showProductDetails(product)" :disabled="product.stock === 0">查看详情</el-button>
                <el-button type="primary" size="small" @click="addToCart(product)" :disabled="product.stock === 0">
                  <i class="el-icon-shopping-cart-2"></i>
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </section>

    <el-dialog 
        title="产品详情" 
        :visible.sync="productDetailsVisible" 
        width="70%" 
        :before-close="handleDetailDialogClose"
        custom-class="product-detail-dialog">
        <div v-if="selectedProduct" class="product-details-dialog">
            <div class="product-image-container">
                <img :src="selectedProduct.image" :alt="selectedProduct.name" class="product-detail-image">
            </div>
            <div class="product-info-container">
                <h2 class="product-detail-name">{{ selectedProduct.name }}</h2>
                <div class="product-origin-detail">产地：{{ selectedProduct.origin }}</div>
                <div class="product-description" v-html="selectedProduct.description"></div>
                <p class="product-detail-price">{{ selectedProduct.price.toFixed(2) }}</p>
                <el-form :model="orderForm" label-width="100px" class="order-form" :rules="orderRules" ref="orderForm">
                    <el-form-item label="购买数量" prop="quantity">
                        <el-input-number 
                            v-model="orderForm.quantity" 
                            :min="1" 
                            :max="selectedProduct.stock"
                            :step="1"
                            controls-position="right">
                        </el-input-number>
                        <span class="stock-info"> 库存: {{ selectedProduct.stock }}</span>
                    </el-form-item>
                    <el-form-item label="收货人" prop="customerName">
                        <el-input v-model="orderForm.customerName" placeholder="请输入收货人姓名"></el-input>
                    </el-form-item>
                    <el-form-item label="联系电话" prop="phone">
                        <el-input v-model="orderForm.phone" placeholder="请输入联系电话"></el-input>
                    </el-form-item>
                    <el-form-item label="收货地址" prop="address">
                        <el-input 
                            type="textarea" 
                            v-model="orderForm.address" 
                            :rows="3"
                            placeholder="请输入详细收货地址">
                        </el-input>
                    </el-form-item>
                    <el-button type="primary" class="submit-order-btn" @click="submitOrder">
                        立即购买
                    </el-button>
                </el-form>
            </div>
        </div>
    </el-dialog>

    <el-backtop :bottom="100">
      <div class="backtop-inner">
        <i class="el-icon-caret-top"></i>
      </div>
    </el-backtop>

    <!-- 添加版权信息 -->
    <div class="copyright">
      <p>&copy; 2024 惠农云桥—惠州市龙门县乡村旅游与助农平台. 版权所有.</p>
    </div>

    <el-drawer
      title="购物车"
      :visible.sync="cartDrawerVisible"
      direction="rtl"
      size="35%">
      <div class="cart-content">
        <div v-if="cartItems.length === 0" class="empty-cart">
          <i class="el-icon-shopping-cart-2"></i>
          <p>购物车是空的</p>
          <el-button type="primary" plain @click="cartDrawerVisible = false">去购物</el-button>
        </div>
        <template v-else>
          <div class="cart-items">
            <el-card v-for="item in cartItems" :key="item.id" class="cart-item" shadow="hover">
              <div class="cart-item-content">
                <img :src="item.image" :alt="item.name" class="cart-item-image">
                <div class="cart-item-info">
                  <h4 class="cart-item-name">{{ item.name }}</h4>
                  <p class="cart-item-price">￥{{ item.price.toFixed(2) }}</p>
                  <div class="cart-item-quantity">
                    <el-input-number 
                      v-model="item.quantity" 
                      :min="1" 
                      :max="item.stock"
                      size="mini"
                      @change="updateCartItem(item)">
                    </el-input-number>
                    <span class="cart-item-subtotal">小计：￥{{ (item.price * item.quantity).toFixed(2) }}</span>
                    <el-button type="danger" size="mini" icon="el-icon-delete" circle @click="removeFromCart(item)"></el-button>
                  </div>
                </div>
              </div>
            </el-card>
          </div>
          <div class="cart-footer">
            <div class="cart-total">
              <span>总计: </span>
              <span class="cart-total-price">￥{{ cartTotal.toFixed(2) }}</span>
            </div>
            <el-button type="primary" size="medium" @click="showCheckoutDialog">结算</el-button>
          </div>
        </template>
      </div>
    </el-drawer>

    <el-dialog
      title="确认订单信息"
      :visible.sync="checkoutDialogVisible"
      width="50%"
      :before-close="handleCheckoutDialogClose">
      <el-form :model="checkoutForm" :rules="checkoutRules" ref="checkoutForm" label-width="100px">
        <el-form-item label="收货人" prop="customerName">
          <el-input v-model="checkoutForm.customerName" placeholder="请输入收货人姓名"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="checkoutForm.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="收货地址" prop="address">
          <el-input type="textarea" v-model="checkoutForm.address" placeholder="请输入详细收货地址"></el-input>
        </el-form-item>
        <el-form-item label="订单备注">
          <el-input type="textarea" v-model="checkoutForm.remark" placeholder="选填：请输入订单备注"></el-input>
        </el-form-item>
      </el-form>
      <div class="checkout-summary">
        <div class="checkout-items">
          <div v-for="item in cartItems" :key="item.id" class="checkout-item">
            <span>{{ item.name }} x {{ item.quantity }}</span>
            <span>￥{{ (item.price * item.quantity).toFixed(2) }}</span>
          </div>
        </div>
        <div class="checkout-total">
          <span>订单总额：</span>
          <span class="checkout-total-price">￥{{ cartTotal.toFixed(2) }}</span>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCheckoutDialogClose">取 消</el-button>
        <el-button type="primary" @click="submitCheckout">提交订单</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  data() {
    return {
      products: [],
      searchName: null,
      activeCategory: 'all',
      selectedProduct: null,
      productDetailsVisible: false,
      orderForm: {
        quantity: 1,
        customerName: '',
        phone: '',
        address: ''
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      cartItems: JSON.parse(localStorage.getItem('cartItems') || '[]'),
      cartDrawerVisible: false,
      checkoutDialogVisible: false,
      checkoutForm: {
        customerName: '',
        phone: '',
        address: '',
        remark: ''
      },
      checkoutRules: {
        customerName: [
          { required: true, message: '请输入收货人姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入收货地址', trigger: 'blur' },
          { min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    filteredProducts() {
      return this.products.filter(product => {
        const categoryMatch = this.activeCategory === 'all' || product.category === this.activeCategory;
        const nameMatch = !this.searchName || product.name.toLowerCase().includes(this.searchName.toLowerCase());
        return categoryMatch && nameMatch;
      });
    },
    cartTotal() {
      return this.cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
    }
  },
  created() {
    this.loadProducts()
  },
  methods: {
    loadProducts() {
      this.$request.get('/products/selectPage', {
        params: {
          pageNum: 1,
          pageSize: 1000
        }
      }).then(res => {
        if (res.code === '200') {
          this.products = res.data.list
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleCategoryChange(tab) {
      this.activeCategory = tab.name;
    },
    handleSearch() {
      // The filtering is done in the computed property
    },
    resetSearch() {
      this.searchName = null;
      this.activeCategory = 'all';
    },
    showProductDetails(product) {
      this.selectedProduct = product
      this.productDetailsVisible = true
    },
    submitOrder() {
  if (!this.validateOrderForm()) {
    return;
  }
  const order = {
    productId: this.selectedProduct.id,
    productName: this.selectedProduct.name,
    customerName: this.orderForm.customerName,
    phone: this.orderForm.phone,
    address: this.orderForm.address,
    quantity: this.orderForm.quantity,
    totalAmount: this.selectedProduct.price * this.orderForm.quantity,
    status: '待发货',
    time: new Date().toISOString(),
    orderId: this.generateOrderId()
  };

  // Check if user has sufficient balance
  if (this.user.account < order.totalAmount) {
    this.$message.error('余额不足，请先充值');
    return;
  }

  // First update the product stock
  const updatedProduct = {
    ...this.selectedProduct,
    stock: this.selectedProduct.stock - this.orderForm.quantity
  };

  // Update product stock in backend
  this.$request.put('/products/update', updatedProduct).then(stockRes => {
    if (stockRes.code === '200') {
      // After successful stock update, proceed with order creation
      this.$request.post('/productorders/add', order).then(orderRes => {
        if (orderRes.code === '200') {
          // Deduct the order amount from user's account
          this.user.account -= order.totalAmount;

          // Update user information in localStorage and backend
          localStorage.setItem('xm-user', JSON.stringify(this.user));
          this.updateUserAccount();

          // Update the local product data
          this.selectedProduct.stock -= this.orderForm.quantity;
          
          // Update the products list
          const index = this.products.findIndex(p => p.id === this.selectedProduct.id);
          if (index !== -1) {
            this.products[index].stock = updatedProduct.stock;
          }

          this.$message.success('订单已提交成功！余额已扣除');
          this.productDetailsVisible = false;
          this.resetOrderForm();
        } else {
          this.$message.error(orderRes.msg);
        }
      });
    } else {
      this.$message.error('更新库存失败：' + stockRes.msg);
    }
  }).catch(error => {
    console.error('Error updating stock:', error);
    this.$message.error('更新库存失败，请稍后重试');
  });
},
    validateOrderForm() {
      if (!this.orderForm.customerName.trim()) {
        this.$message.error('请填写收货人姓名')
        return false
      }
      if (!/^1[3-9]\d{9}$/.test(this.orderForm.phone)) {
        this.$message.error('请填写正确的联系电话')
        return false
      }
      if (!this.orderForm.address.trim()) {
        this.$message.error('请填写收货地址')
        return false
      }
      return true
    },
    resetOrderForm() {
      this.orderForm = {
        quantity: 1,
        customerName: '',
        phone: '',
        address: ''
      }
    },
    generateOrderId() {
      // Generate a unique order ID (you may want to use a more robust method)
      return Date.now() + Math.floor(Math.random() * 1000);
    },
    updateUserAccount() {
      this.$request.put('/user/update', this.user).then(res => {
        if (res.code !== '200') {
          this.$message.error('更新用户余额失败，请联系客服');
        }
      })
    },
    addToCart(product) {
      const existingItem = this.cartItems.find(item => item.id === product.id);
      if (existingItem) {
        if (existingItem.quantity < product.stock) {
          existingItem.quantity++;
          this.$message.success('已增加商品数量');
        } else {
          this.$message.warning('已达到库存上限');
        }
      } else {
        this.cartItems.push({
          ...product,
          quantity: 1
        });
        this.$message.success('已添加到购物车');
      }
      this.saveCartToStorage();
    },
    removeFromCart(item) {
      const index = this.cartItems.findIndex(cartItem => cartItem.id === item.id);
      if (index > -1) {
        this.cartItems.splice(index, 1);
        this.$message.success('已从购物车移除');
        this.saveCartToStorage();
      }
    },
    updateCartItem(item) {
      if (item.quantity > item.stock) {
        item.quantity = item.stock;
        this.$message.warning('已达到库存上限');
      }
      this.saveCartToStorage();
    },
    showCheckoutDialog() {
      if (this.user.name) {
        this.checkoutForm.customerName = this.user.name;
        this.checkoutForm.phone = this.user.phone;
        this.checkoutForm.address = this.user.address;
      }
      this.checkoutDialogVisible = true;
    },
    handleCheckoutDialogClose() {
      this.$refs.checkoutForm?.resetFields();
      this.checkoutDialogVisible = false;
    },
    submitCheckout() {
      this.$refs.checkoutForm.validate((valid) => {
        if (valid) {
          if (this.user.account < this.cartTotal) {
            this.$message.error('余额不足，请先充值');
            return;
          }

          const orderPromises = this.cartItems.map(item => {
            const order = {
              productId: item.id,
              productName: item.name,
              customerName: this.checkoutForm.customerName,
              phone: this.checkoutForm.phone,
              address: this.checkoutForm.address,
              remark: this.checkoutForm.remark,
              quantity: item.quantity,
              totalAmount: item.price * item.quantity,
              status: '待发货',
              time: new Date().toISOString(),
              orderId: this.generateOrderId()
            };

            const updatedProduct = {
              ...item,
              stock: item.stock - item.quantity
            };

            return this.$request.put('/products/update', updatedProduct)
              .then(stockRes => {
                if (stockRes.code === '200') {
                  return this.$request.post('/productorders/add', order);
                }
                throw new Error(stockRes.msg);
              });
          });

          Promise.all(orderPromises)
            .then(() => {
              // 更新用户余额
              this.user.account -= this.cartTotal;
              localStorage.setItem('xm-user', JSON.stringify(this.user));
              this.updateUserAccount();
              
              // 清空购物车
              this.cartItems = [];
              this.saveCartToStorage();
              
              // 关闭对话框
              this.cartDrawerVisible = false;
              this.checkoutDialogVisible = false;
              
              // 重置表单
              this.$refs.checkoutForm.resetFields();
              
              this.$message.success('订单已提交成功！');
            })
            .catch(error => {
              console.error('结算失败:', error);
              this.$message.error('结算失败，请稍后重试');
            });
        }
      });
    },
    handleDetailDialogClose() {
      this.$refs.orderForm?.resetFields();
      this.productDetailsVisible = false;
    },
    saveCartToStorage() {
      localStorage.setItem('cartItems', JSON.stringify(this.cartItems));
    }
  },
  watch: {
    cartItems: {
      handler(newValue) {
        this.saveCartToStorage();
      },
      deep: true
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/theme/front_css/products.css";
</style>

