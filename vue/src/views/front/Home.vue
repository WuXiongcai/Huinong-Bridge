<template>
  <div class="main-content">
    <div>
      <el-carousel :interval="4000" type="card" height="300px">
        <el-carousel-item v-for="(item, index) in carouselData" :key="index">
          <div class="carousel-item">
            <img :src="item" alt="" style="width: 100%; height: 300px; border-radius: 5px">
            <div class="carousel-content">
              <h3>{{ getCarouselTitle(index) }}</h3>
              <p>{{ getCarouselDescription(index) }}</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
    <div class="home-content">
      <div style="display: flex">
        <div style="width: 50%; height: 330px; margin-right: 25px; margin-top: 10px">
          <el-carousel height="330px">
            <el-carousel-item v-for="item in left" :key="item">
              <img :src="item" alt="" style="width: 100%; height: 330px">
            </el-carousel-item>
          </el-carousel>
        </div>
        <div style="width: 50%; height: 330px; margin-left: 17px">
          <el-tabs v-model="activeName">
            <el-tab-pane label="助农资讯" name="first">
              <div style="display: flex; line-height: 48px; height: 48px" v-for="item in informationData">
                <div style="flex: 1; font-size: 17px; width: 0; cursor: pointer;" class="overflowShow" @click="navTo('/front/informationDetail?id=' + item.id)">{{ item.name }}</div>
                <div style="width: 150px; text-align: right; color: #a9a9b8; font-size: 15px">{{ item.time }}</div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="助农政策" name="second">
              <div style="display: flex; line-height: 48px; height: 48px" v-for="item in policyData">
                <div style="flex: 1; font-size: 17px; width: 0; cursor: pointer;" class="overflowShow" @click="navTo('/front/policyDetail?id=' + item.id)">{{ item.name }}</div>
                <div style="width: 150px; text-align: right; color: #a9a9b8; font-size: 15px">{{ item.time }}</div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>

      <div style="text-align: center; color: #19a14b; font-size: 25px; font-weight: 550; margin: 40px 0">精选农家乐</div>
      <hr style="border: 0; height: 1px; background: linear-gradient(to right, #19a14b, #f0f9eb, #19a14b); margin: 40px 0;">

      <div class="agritainment-container">
        <!-- 左侧农家乐列表 -->
        <div class="agritainment-list">
          <h2>龙门县精选农家乐推荐</h2>
          <div v-for="item in agritainmentData" :key="item.id" class="agritainment-item">
            <div class="agritainment-image" v-if="item.image">
              <img :src="getImageUrl(item.image)" :alt="item.name">
            </div>
            <h3>{{ item.name }}</h3>
            <p class="agritainment-type">
              <el-tag 
                v-for="tag in item.tags.split(',')" 
                :key="tag"
                :type="getTagType(tag)"
                style="margin-right: 5px">
                {{ tag }}
              </el-tag>
            </p>
            <p class="agritainment-feature" v-if="item.feature">{{ item.feature }}</p>
            <p class="agritainment-address">地址：{{ item.address }}</p>
            <p class="agritainment-phone">联系电话：{{ item.phone }}</p>
            <div class="agritainment-actions">
              <el-button 
                type="primary" 
                size="small" 
                @click="showNavigation(item)"
              >
                导航路线
              </el-button>
              <el-button 
                type="info" 
                size="small" 
                @click="showDetails(item)"
              >
                查看详情
              </el-button>
            </div>
          </div>
        </div>

        <!-- 右侧地图容器 -->
        <div class="map-container">
          <div id="map-container"></div>
        </div>
      </div>

      <!-- 添加农家乐排行榜部分 -->
      <div class="farm-ranking">
        <h2>龙门县农家乐人气排行榜</h2>
        <div class="ranking-list">
          <div v-for="(item, index) in farmRankingList" 
               :key="item.id" 
               class="ranking-item"
               @click="showDetails(item)">
            <div class="ranking-number" :class="'rank-' + (index + 1)">{{ index + 1 }}</div>
            <div class="ranking-image">
              <img :src="getImageUrl(item.image)" :alt="item.name">
            </div>
            <div class="ranking-info">
              <h3>{{ item.name }}</h3>
              <div class="ranking-tags">
                <el-tag 
                  v-for="tag in item.tags.split(',')" 
                  :key="tag"
                  :type="getTagType(tag)"
                  size="small"
                  style="margin-right: 5px; margin-bottom: 5px">
                  {{ tag }}
                </el-tag>
              </div>
              <div class="ranking-stats">
                <span class="rating">
                  <i class="el-icon-star-on"></i>
                  {{ item.rating }}分
                </span>
                <span class="reviews">
                  <i class="el-icon-chat-dot-round"></i>
                  {{ item.reviewCount }}条评价
                </span>
                <span class="visitors">
                  <i class="el-icon-star-off"></i>
                  {{ item.monthlyVisitors }}人收藏
                </span>
              </div>
            </div>
          </div>
        </div>
        <div class="ranking-description">
          <p><i class="el-icon-info-circle"></i> 排名说明：</p>
          <ul>
            <li>榜单根据高德地图农家乐的评分、评价数量及收藏数综合排名</li>
            <li>数据每月更新，实时反映游客好评度和欢迎程度</li>
            <li>点击农家乐卡片可查看详细信息</li>
          </ul>
        </div>
      </div>

      <div style="text-align: center; color: #19a14b; font-size: 25px; font-weight: 550; margin: 40px 0">特价助农商品推荐</div>
      <hr style="border: 0; height: 1px; background: linear-gradient(to right, #19a14b, #f0f9eb, #19a14b); margin: 40px 0;">
      <el-card class="purchase-notice" shadow="hover">
        <h3 style="color: #19a14b; margin-bottom: 15px;">特价助农商品购买须知：</h3>
        <ol style="padding-left: 20px; line-height: 1.8;">
          <li>所有特价助农商品均为当季新鲜农产品，由贫困地区农户直接供应。</li>
          <li>特价商品数量有限，先到先得，售完即止。</li>
          <li>特价商品不支持退换，请谨慎购买。</li>
          <li>每个用户每天限购3件特价商品，以帮助更多用户受益。</li>
          <li>特价商品配送时间可能略长于普通商品，请耐心等待。</li>
          <li>如收到商品有质量问题，请在24小时内联系客服处理。</li>
          <li>购买特价助农商品即视为支持扶贫事业，感谢您的爱心和支持！</li>
        </ol>
        <p style="margin-top: 15px; color: #e4393c;">温馨提示：您的每一次购买都在直接帮助贫困地区的农民，让我们携手共建美好乡村！</p>
      </el-card>
      <section class="products-section">
        <el-row :gutter="20">
          <el-col :xs="12" :sm="8" :md="6" :lg="4" :xl="4" v-for="item in goodsData" :key="item.id">
            <el-card :body-style="{ padding: '0px' }" class="product-card">
              <img :src="item.img" :alt="item.name" class="product-image">
              <div class="product-info">
                <h3 class="product-name">{{ item.name }}</h3>
                <div class="product-details">
                  <span class="product-price">￥{{ item.price }}</span>
                  <span class="product-stock">库存：{{ item.num }}</span>
                </div>
                <el-button type="warning" size="small" icon="el-icon-shopping-cart-2" @click="buy(item)" :disabled="item.num === 0">购 买</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </section>


      <div style="text-align: center; color: #19a14b; font-size: 25px; font-weight: 550; margin: 40px 0">帮扶项目</div>
      <hr style="border: 0; height: 1px; background: linear-gradient(to right, #19a14b, #f0f9eb, #19a14b); margin: 40px 0;">
      <section class="aid-projects-section">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6" v-for="(project, index) in aidProjects" :key="index">
            <el-card class="aid-project-card" shadow="hover" :body-style="{ padding: '0px' }">
              <el-image :src="project.img" :alt="project.name" fit="cover" class="aid-project-image">
                <template #error>
                  <div class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </template>
              </el-image>
              <div class="aid-project-overlay">
                <p>{{ project.description }}</p>
                <el-progress :percentage="project.progress" :format="formatProgress"></el-progress>
                <el-button type="primary" round @click="navTo(project.link)">参与帮扶</el-button>
              </div>
            </el-card>
            <div style="text-align: center; font-size: 16px; font-weight: 530; margin-top: 10px">{{ project.name }}</div>
          </el-col>
        </el-row>
      </section>

      <div class="friendly-links-section">
        <div class="friendly-links-container">
          <div class="friendly-links-header">
            <div class="friendly-links-tab">友情链接</div>
          </div>
          <div class="friendly-links-content">
            <el-collapse v-model="activeCollapse" accordion>
              <el-collapse-item 
                v-for="(group, groupIndex) in friendlyLinks" 
                :key="groupIndex"
                :title="group.name"
                :name="groupIndex"
              >
                <div class="link-grid">
                  <a 
                    v-for="(link, linkIndex) in group.links" 
                    :key="linkIndex"
                    :href="link.url"
                    target="_blank"
                    class="link-item"
                  >
                    {{ link.name }}
                  </a>
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>
        </div>
      </div>
      <el-backtop 
        :bottom="90"
        :right="40"
      >
        <div class="backtop-inner">
          <i class="el-icon-caret-top"></i>
        </div>
      </el-backtop>
    </div>
    <!-- 版权信息 -->
    <div class="copyright">
      <p>&copy; 2024 惠农云桥—惠州市龙门县乡村旅游与助农平台. 版权所有.</p>
    </div>

    <!-- 添加导航对话框 -->
    <el-dialog 
      title="路线导航" 
      :visible.sync="navigationDialogVisible" 
      width="500px"
    >
      <el-form :model="navigationForm" label-width="100px">
        <el-form-item label="起点地址">
          <el-input v-model="navigationForm.startAddress"></el-input>
        </el-form-item>
        <el-form-item label="终点地址">
          <el-input v-model="navigationForm.endAddress" disabled></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="navigationDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="startNavigation">开始导航</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加详情对话框 -->
    <el-dialog 
      title="农家乐详情" 
      :visible.sync="detailsDialogVisible" 
      width="600px"
    >
      <div v-if="currentAgritainment" class="agritainment-details">
        <div class="detail-image" v-if="currentAgritainment.image">
          <img :src="getImageUrl(currentAgritainment.image)" :alt="currentAgritainment.name">
        </div>
        <h2>{{ currentAgritainment.name }}</h2>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="标签">
            <el-tag 
              v-for="tag in currentAgritainment.tags.split(',')" 
              :key="tag"
              :type="getTagType(tag)"
              style="margin-right: 5px">
              {{ tag }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="特色" v-if="currentAgritainment.feature">
            {{ currentAgritainment.feature }}
          </el-descriptions-item>
          <el-descriptions-item label="地址">
            {{ currentAgritainment.address }}
          </el-descriptions-item>
          <el-descriptions-item label="联系电话">
            {{ currentAgritainment.phone }}
          </el-descriptions-item>
          <el-descriptions-item label="营业时间" v-if="currentAgritainment.businessHours">
            {{ currentAgritainment.businessHours }}
          </el-descriptions-item>
          <el-descriptions-item label="价格区间" v-if="currentAgritainment.priceRange">
            {{ currentAgritainment.priceRange }}
          </el-descriptions-item>
          <el-descriptions-item label="详细描述" v-if="currentAgritainment.description">
            {{ currentAgritainment.description }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader'

export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      carouselData: [
        require('@/assets/imgs/lun-1.jpg'),
        require('@/assets/imgs/lun-2.jpg'),
        require('@/assets/imgs/lun-3.jpg'),
        require('@/assets/imgs/lun-4.jpg'),
      ],
      left: [
        require('@/assets/imgs/huidonglun_8.jpg'),
        require('@/assets/imgs/lun-5.jpg'),
        require('@/assets/imgs/huidonglun_7.jpg'),
        require('@/assets/imgs/lun-7.png'),
      ],
      activeName: 'first',
      informationData: [],
      policyData: [],
      agritainmentData: [],
      goodsData: [],
      aidProjects: [
        {
          name: "订单式农业计划",
          description: "与农户签订溢价包销协议，保障农产品销售",
          img: require('@/assets/imgs/订单农业.jpg'),
          progress: 75,
          link: "https://ccy.zgshbfw.com/?p=2#/"
        },
        {
          name: "特色产业全产业链帮扶",
          description: "推广地方特色农产品，提高农民收入",
          img: require('@/assets/imgs/全产业链.jpg'),
          progress: 60,
          link: "https://ccy.zgshbfw.com/?p=2#/"
        },
        {
          name: "乡村农产品供应链计划",
          description: "创新农产品地市场建设模式和运营机制",
          img: require('@/assets/imgs/冷链计划.jpg'),
          progress: 75,
          link: "https://ccy.zgshbfw.com/?p=2#/"
        },
        {
          name: "产业扶贫计划",
          description: "重点发展草食畜牧、中药材等产业，带动贫困地区人口增收",
          img: require('@/assets/imgs/产业扶贫.jpg'),
          progress: 40,
          link: "https://ccy.zgshbfw.com/?p=2#/"
        }
      ],
      friendlyLinks: [
        {
          name: '国家机关',
          links: [
            {name: '国家发改委', url: 'https://www.ndrc.gov.cn/'},
            {name: '国家乡村振兴局', url: 'http://www.nrra.gov.cn/'},
            {name: '农业农村部', url: 'http://www.moa.gov.cn'},
            {name: '文化和旅游部', url: 'http://www.mct.gov.cn/'},
            {name: '国家卫生健康委员会', url: 'http://www.nhc.gov.cn/'},
            {name: '国家林业和草原局', url: 'http://www.forestry.gov.cn/'},
            {name: '中国政府网', url: 'http://www.gov.cn/'},
            {name: '中国农业科学院', url: 'http://www.caas.cn/'},
            {name: '中国农业科学研究院', url: 'http://www.caas.cn/'},
            {name: '中国农业科学院', url: 'http://www.caas.cn/'},
            {name: '人力资源和社会保障部', url: 'http://www.mohrss.gov.cn/'},
          ]
        },
        {
          name: '省级乡村振兴局',
          links: [
            {name: '广东省农业农村厅', url: 'https://dara.gd.gov.cn/'},
            {name: '广西壮族自治区乡村振兴局', url: 'http://nynct.gxzf.gov.cn/'},
            {name: '湖南省乡村振兴局', url: 'https://agri.hunan.gov.cn/'},
            {name: '湖北省乡村振兴局', url: 'http://www.hbxczx.gov.cn/'},
            {name: '河南省乡村振兴局', url: 'http://www.hnxczx.gov.cn/'},
            {name: '河北省乡村振兴局', url: 'http://www.hbxczx.gov.cn/'},
            {name: '山东省乡村振兴局', url: 'http://www.sdxczx.gov.cn/'},
            {name: '山西省乡村振兴局', url: 'http://www.sxxcxzx.gov.cn/'},
            {name: '陕西省乡村振兴局', url: 'http://www.sxxcxzx.gov.cn/'},
            {name: '四川省乡村振兴局', url: 'http://www.scxczx.gov.cn/'},
            {name: '贵州省乡村振兴局', url: 'http://www.gzxczx.gov.cn/'},
            {name: '云南省乡村振兴局', url: 'http://www.ynxczx.gov.cn/'},
            {name: '甘肃省乡村振兴局', url: 'http://www.gansu.gov.cn/'},
            {name: '青海省乡村振兴局', url: 'http://www.qhxczx.gov.cn/'},
            {name: '江西省乡村振兴局', url: 'http://www.jiangxi.gov.cn/'},
            {name: '安徽省乡村振兴局', url: 'https://xczxj.ah.gov.cn/'},
            {name: '浙江省农业农村厅', url: 'http://nynct.zj.gov.cn/col/col1229142060/index.html'},
            {name: '江苏省乡村振兴局', url: 'http://xczxj.nynct.jiangsu.gov.cn/'},
          ]
        },
        {
          name: '合作伙伴',
          links: [
            {name: '惠州学院', url: 'http://www.hzu.edu.cn/'},
            {name: '惠州本地宝', url: 'https://huizhou.bendibao.com/'},
            {name: '广东省人民政府', url: 'http://www.gd.gov.cn/'},
            {name: '中国乡村发展基金会', url: 'http://www.cfpa.org.cn/'},
            {name: '中国乡村发展协会', url: 'https://www.zgxcfzxh.org.cn/'},
            {name: '龙门县人民政府', url: 'https://www.longmen.gov.cn/'},
            {name: '腾讯公益', url: 'https://gongyi.qq.com/'},
            {name: '阿里巴巴公益', url: 'https://www.alibabagroup.com/cn/global/home'},
          ]
        },
        {
          name: '相关社团',
          links: [
            {name: '中华慈善总会', url: 'http://www.chinacharityfederation.org'},
            {name: '中国扶贫基金会', url: 'https://static.cfpa.org.cn/'},
            {name: '广东省慈善总会', url: 'https://www.gdcf.org.cn/'},
            {name: 'i志愿', url: 'https://www.gdzyz.cn/'},
            {name: '中国青少年基金会', url: 'http://www.cydf.org.cn'},
            {name: '惠州文明网', url: 'http://hz.wenming.cn/'},
          ]
        }
      ],
      map: null,
      navigationDialogVisible: false,
      navigationForm: {
        startAddress: '',
        endAddress: '',
      },
      detailsDialogVisible: false,
      currentAgritainment: null,
      activeCollapse: '',
      farmRankingList: [],
    }
  },
  mounted() {
    this.loadInformation()
    this.loadPolicy()
    this.loadAgritainment().then(() => {
      this.$nextTick(() => {
        this.initMap()
      })
    })
    this.loadGoods()
    this.loadFarmRanking()
  },
  methods: {
    buy(goods) {
      if (this.user.role === 'ADMIN') {
        this.$message.warning('您是管理员，不支持该操作')
        return
      }
      let data = {
        goodsId: goods.id,
        userId: this.user.id,
        price: goods.price,
        status: '待发货'
      }
      this.$request.post('/orders/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('购买成功')
          this.loadGoods()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadGoods() {
      this.$request.get('/goods/selectAll').then(res => {
        if (res.code === '200') {
          this.goodsData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadAgritainment() {
      return this.$request.get('/agritainment/selectAll').then(res => {
        if (res.code === '200') {
          this.agritainmentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadInformation() {
      this.$request.get('/information/top6').then(res => {
        if (res.code === '200') {
          this.informationData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadPolicy() {
      this.$request.get('/policy/top6').then(res => {
        if (res.code === '200') {
          this.policyData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    navTo(url) {
      location.href = url
    },
    handleLinkClick(url) {
      window.open(url, '_blank');
    },
    formatProgress(percentage) {
      return percentage.toFixed(0) + '%';
    },
    getImageUrl(path) {
      if (path && !path.startsWith('http')) {
        return this.$baseUrl + '/files/' + path
      }
      return path
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
    showNavigation(item) {
      this.currentAgritainment = item
      this.navigationForm.endAddress = item.address
      this.navigationDialogVisible = true
    },
    showDetails(item) {
      this.currentAgritainment = item
      this.detailsDialogVisible = true
    },
    async initMap() {
      try {
        const AMap = await AMapLoader.load({
          key: 'e9e7c13059174d930b578eca947385ef',  // 替换为您的高德地图API密钥
          version: '2.0',
          plugins: ['AMap.ToolBar', 'AMap.Scale', 'AMap.HawkEye', 'AMap.MapType', 'AMap.Marker']
        })

        this.map = new AMap.Map('map-container', {
          zoom: 12,
          center: [114.254829, 23.746466] // 龙门县中心坐标
        })

        // 添加地图控件
        this.map.addControl(new AMap.ToolBar())
        this.map.addControl(new AMap.Scale())
        this.map.addControl(new AMap.HawkEye())
        this.map.addControl(new AMap.MapType())

        // 为每个农家乐添加标记
        this.agritainmentData.forEach(item => {
          if (item.longitude && item.latitude) {
            const marker = new AMap.Marker({
              position: [item.longitude, item.latitude],
              title: item.name,
              icon: new AMap.Icon({
                size: new AMap.Size(25, 34),
                image: '//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png',
                imageSize: new AMap.Size(25, 34),
              }),
              offset: new AMap.Pixel(-13, -34)
            })
            
            // 添加点击事件
            marker.on('click', () => {
              this.showDetails(item)
            })
            
            // 添加信息窗体
            const infoWindow = new AMap.InfoWindow({
              content: `
                <div style="padding: 10px;">
                  <h4 style="margin: 0 0 5px 0;">${item.name}</h4>
                  <p style="margin: 0 0 5px 0;font-size: 12px;color: #666;">${item.feature || ''}</p>
                  <p style="margin: 0 0 5px 0;font-size: 12px;">地址：${item.address}</p>
                  <p style="margin: 0;font-size: 12px;">电话：${item.phone}</p>
                </div>
              `,
              offset: new AMap.Pixel(0, -30),
              anchor: 'bottom-center',
              closeWhenClickMap: true
            })
            
            // 鼠标移入显示信息窗体
            marker.on('mouseover', () => {
              infoWindow.open(this.map, marker.getPosition())
            })
            
            // 鼠标移出关闭信息窗体
            marker.on('mouseout', () => {
              infoWindow.close()
            })
            
            this.map.add(marker)
          }
        })

      } catch (e) {
        console.error('地图加载失败：', e)
        this.$message.error('地图加载失败，请刷新页面重试')
      }
    },
    startNavigation() {
      if (!this.navigationForm.startAddress) {
        this.$message.error('请输入起点地址')
        return
      }
      
      window.open(`https://uri.amap.com/navigation?from=${this.navigationForm.startAddress}&to=${this.navigationForm.endAddress}&mode=car&policy=1`)
      this.navigationDialogVisible = false
    },
    loadFarmRanking() {
      this.$request.get('/farmList/selectTop20').then(res => {
        if (res.code === '200') {
          this.farmRankingList = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    getCarouselTitle(index) {
      const titles = [
        '惠州龙门',
        '龙门县龙城街道黄溪村',
        '龙门麻榨镇',
        '龙门蓝田瑶族乡',
        // 根据实际轮播图数量添加对应标题
      ]
      return titles[index] || ''
    },
    getCarouselDescription(index) {
      const descriptions = [
        '发展“大山经济”助力乡村振兴',
        '龙门天堂山的80后“山民”返乡创业做民宿旅游 复活龙门古村',
        '龙门“宝藏”农产品走出大山',
        '龙门蓝田瑶族乡入选首批广东省旅游风情小镇',
        // 根据实际轮播图数量添加对应描述
      ]
      return descriptions[index] || ''
    },
  }
}
</script>

<style scoped>
@import "@/assets/css/theme/front_css/home.css";

/* 版权信息样式 */
.copyright {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: rgba(255, 255, 255, 0.9);
  text-align: center;
  padding: 10px 0;
  font-size: 14px;
  color: #666;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

/* 适配移动设备时调整版权信息样式 */
@media screen and (max-width: 768px) {
  .copyright {
    font-size: 12px;
    padding: 8px 0;
  }
}

.home-content {
  width: 100%;
  margin: 20px 0;
}

/* 排行榜说明样式 */
.ranking-description {
  margin-top: 15px;
  padding: 10px 15px;
  background: #f8f9fa;
  border-radius: 4px;
  font-size: 13px;
  color: #666;
}

.ranking-description p {
  display: flex;
  align-items: center;
  color: #19a14b;
  font-weight: bold;
  margin-bottom: 5px;
}

.ranking-description i {
  margin-right: 5px;
  font-size: 16px;
}

.ranking-description ul {
  margin: 0;
  padding-left: 21px;
  line-height: 1.6;
}

.ranking-description li {
  margin-bottom: 3px;
}

/* 调整移动端样式 */
@media screen and (max-width: 768px) {
  .ranking-description {
    font-size: 12px;
    padding: 8px 12px;
  }
  
  .ranking-description ul {
    padding-left: 18px;
  }
}
</style>

