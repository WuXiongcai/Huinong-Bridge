<template>
  <div class="stories-container">
    <!-- 顶部Banner -->
    <div class="banner-section">
      <div class="banner-content">
        <h1>惠农云桥 - 连接城乡的桥梁</h1>
        <p>让农村的故事被听见，让乡村的文化被传承</p>
      </div>
    </div>

    <!-- 添加快捷导航 -->
    <div class="quick-nav">
      <div class="nav-container">
        <div class="nav-item" 
             v-for="(item, index) in navItems" 
             :key="index"
             @click="scrollToSection(item.target)"
             :class="{ active: currentSection === item.target }">
          <i :class="item.icon"></i>
          <span>{{ item.name }}</span>
        </div>
      </div>
    </div>

    <!-- 助农故事分享板块 -->
    <div class="section stories-section" id="stories">
      <div class="section-header">
        <h2 class="section-title">
          <i class="el-icon-collection"></i>
          助农故事分享
        </h2>
        <p class="section-subtitle">分享乡村振兴的感人故事，传递农村发展的希望之光</p>
      </div>


      <!-- 优化故事卡片布局 -->
      <el-row :gutter="24">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="story in stories" :key="story.id">
          <div class="story-card-wrapper">
            <el-card class="story-card" :body-style="{ padding: '0px' }">
              <div class="story-image-wrapper">
                <img :src="getImageUrl(story.image)" class="story-image" :alt="story.title" />
                <div class="story-category">
                  <el-tag size="small">{{ story.category || '乡村故事' }}</el-tag>
                </div>
              </div>
              <div class="story-content">
                <h3 class="story-title">{{ story.name }}</h3>
                <p class="story-excerpt">{{ story.excerpt }}</p>
                <div class="story-footer">
                  <span class="story-date">
                    <i class="el-icon-date"></i>
                    {{ formatDate(story.time) }}
                  </span>
                  <el-button type="primary" size="small" @click="viewStory(story)">
                    阅读更多
                    <i class="el-icon-right"></i>
                  </el-button>
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="prev, pager, next, total"
            :total="total"
        >
        </el-pagination>
      </div>

      <!-- 故事详情对话框 -->
      <el-dialog
          :title="currentStory.name"
          :visible.sync="dialogVisible"
          width="70%"
          :before-close="handleClose"
      >
        <div v-if="currentStory.image" class="dialog-image-container">
          <img :src="getImageUrl(currentStory.image)" :alt="currentStory.name" class="dialog-image" />
        </div>
        <div v-html="currentStory.content" class="story-full-content"></div>
        <div class="story-metadata">
          <p><strong>发布时间：</strong>{{ formatDate(currentStory.time) }}</p>
          <p><strong>地区：</strong>{{ currentStory.region || '未指定' }}</p>
          <p><strong>扶贫方式：</strong>{{ currentStory.category || '未分类' }}</p>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="shareStory">分享故事</el-button>
        </span>
      </el-dialog>
    </div>

    <!-- 扶贫助农微视频板块 -->
    <div class="section video-section" id="videos">
      <div class="section-header">
        <h2 class="section-title">
          <i class="el-icon-video-camera"></i>
          扶贫助农微视频
        </h2>
        <p class="section-subtitle">用镜头记录乡村振兴的点点滴滴</p>
      </div>

      <el-row :gutter="24">
        <el-col :xs="24" :sm="12" :md="6" :lg="6" v-for="video in displayedVideos" :key="video.id">
          <div class="video-card-wrapper">
            <el-card class="video-card" :body-style="{ padding: '0px' }">
              <div class="video-thumbnail" @click="playVideo(video)">
                <img :src="getImageUrl(video.coverImage)" :alt="video.title" class="video-image" />
                <div class="play-overlay">
                  <i class="el-icon-video-play"></i>
                </div>
              </div>
              <div class="video-content">
                <h3 class="video-title">{{ video.title }}</h3>
                <p class="video-description">{{ video.description }}</p>
                <div class="video-footer">
                  <span class="video-date">
                    <i class="el-icon-time"></i>
                    {{ formatDate(video.uploadTime) }}
                  </span>
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
      <div class="view-more-container">
        <el-button v-if="!showAllVideos" @click="loadMoreVideos">查看更多</el-button>
        <el-button v-if="showAllVideos" @click="collapseVideos">收起</el-button>
      </div>
    </div>

    <!-- 龙门文化展示板块 -->
    <div class="section culture-section" id="culture">
      <div class="section-header">
        <h2 class="section-title">
          <i class="el-icon-collection-tag"></i>
          龙门文化展示
        </h2>
        <p class="section-subtitle">传承千年文化，展现龙门魅力</p>
      </div>

      <!-- 新增：文化亮点展示轮播 -->
      <div class="culture-highlights">
        <el-carousel :interval="4000" type="card" height="300px">
          <el-carousel-item v-for="item in culturalHighlights" :key="item.id">
            <div class="highlight-card">
              <img :src="getImageUrl(item.image)" :alt="item.title">
              <div class="highlight-content">
                <h3>{{ item.title }}</h3>
                <p>{{ item.description }}</p>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>

      <!-- 文化分类标签 -->
      <div class="culture-tabs">
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <el-tab-pane label="传统手工艺" name="handicraft">
            <div class="culture-grid">
              <el-card v-for="item in handicraftList" :key="item.id" class="culture-card" shadow="hover">
                <div class="culture-image" @click="showCultureDetail('handicraft', item)">
                  <img :src="getImageUrl(item.image)" :alt="item.name">
                  <div class="culture-overlay">
                    <span>查看详情</span>
                  </div>
                </div>
                <div class="culture-info">
                  <h3>{{ item.name }}</h3>
                  <el-tag size="small" :type="getTagType(item.category)">{{ item.category }}</el-tag>
                  <p class="culture-desc">{{ item.description }}</p>
                </div>
              </el-card>
            </div>
            <div class="view-more-container">
              <el-button v-if="!showAllHandicrafts" @click="toggleHandicrafts">
                查看更多
              </el-button>
              <el-button v-else @click="toggleHandicrafts">
                收起
              </el-button>
            </div>
          </el-tab-pane>

          <el-tab-pane label="民俗文化" name="folk">
            <div class="culture-grid">
              <el-card v-for="item in folkList" :key="item.id" class="culture-card" shadow="hover">
                <div class="culture-image" @click="showCultureDetail('folk', item)">
                  <img :src="getImageUrl(item.image)" :alt="item.title">
                  <div class="culture-overlay">
                    <span>查看详情</span>
                  </div>
                </div>
                <div class="culture-info">
                  <h3>{{ item.title }}</h3>
                  <el-tag size="small" :type="getFolkTagType(item.category)">{{ item.category }}</el-tag>
                  <p class="culture-desc">{{ item.description }}</p>
                </div>
              </el-card>
            </div>
            <div class="view-more-container">
              <el-button v-if="!showAllFolks" @click="toggleFolks">
                查看更多
              </el-button>
              <el-button v-else @click="toggleFolks">
                收起
              </el-button>
            </div>
          </el-tab-pane>

          <el-tab-pane label="特色美食" name="food">
            <div class="culture-grid">
              <el-card v-for="item in foodList" :key="item.id" class="culture-card" shadow="hover">
                <div class="culture-image" @click="showCultureDetail('food', item)">
                  <img :src="getImageUrl(item.image)" :alt="item.name">
                  <div class="culture-overlay">
                    <span>查看详情</span>
                  </div>
                </div>
                <div class="culture-info">
                  <h3>{{ item.name }}</h3>
                  <el-tag size="small" :type="getFoodTagType(item.category)">{{ item.category }}</el-tag>
                  <p class="culture-desc">{{ item.description }}</p>
                  <div class="food-price">
                    <span>¥{{ item.price }}</span>
                  </div>
                </div>
              </el-card>
            </div>
            <div class="view-more-container">
              <el-button v-if="!showAllFoods" @click="toggleFoods">
                查看更多
              </el-button>
              <el-button v-else @click="toggleFoods">
                收起
              </el-button>
            </div>
          </el-tab-pane>

          <!-- 新增：文化景点标签页 -->
          <el-tab-pane label="文化景点" name="attractions">
            <div class="culture-grid">
              <el-card v-for="spot in culturalSpots" :key="spot.id" class="culture-card" shadow="hover">
                <div class="culture-image" @click="showSpotDetail(spot)">
                  <img :src="getImageUrl(spot.image)" :alt="spot.name">
                  <div class="culture-overlay">
                    <span>查看详情</span>
                  </div>
                </div>
                <div class="culture-info">
                  <h3>{{ spot.name }}</h3>
                  <el-tag size="small" type="success">{{ spot.type }}</el-tag>
                  <p class="culture-desc">{{ spot.description }}</p>
                  <div class="spot-meta">
                    <span><i class="el-icon-location"></i>{{ spot.location }}</span>
                    <span><i class="el-icon-star-on"></i>{{ spot.rating }}分</span>
                  </div>
                </div>
              </el-card>
            </div>
          </el-tab-pane>

          <!-- 新增：非遗文化标签页 -->
          <el-tab-pane label="非遗文化" name="heritage">
            <div class="heritage-section">
              <!-- 添加顶部统计数据 -->
              <div class="heritage-stats">
                <div class="stat-item">
                  <div class="stat-number">{{ heritageStats.national }}</div>
                  <div class="stat-label">国家级非遗</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">{{ heritageStats.provincial }}</div>
                  <div class="stat-label">省级非遗</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">{{ heritageStats.city }}</div>
                  <div class="stat-label">市级非遗</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">{{ heritageStats.county }}</div>
                  <div class="stat-label">县级非遗</div>
                </div>
              </div>


              <!-- 修改时间轴展示部分 -->
              <div class="heritage-timeline">
                <div class="timeline-header">
                  <h3>非遗文化传承历程</h3>
                  <p>见证龙门非物质文化遗产的保护与发展</p>
                </div>
                <el-timeline>
                  <el-timeline-item
                    v-for="year in timelineYears"
                    :key="year"
                    :timestamp="year + '年'"
                    placement="top"
                    :type="getTimelineItemType(year)"
                    :size="getTimelineItemSize(year)"
                    :icon="getTimelineIcon(year)">
                    <el-card class="timeline-card">
                      <div class="timeline-items">
                        <div v-for="item in getHeritageByYear(year)"
                             :key="item.id"
                             class="timeline-item"
                             @click="showHeritageDetail(item)">
                          <div class="item-header">
                            <span class="item-name">{{ item.name }}</span>
                            <el-tag :type="getHeritageTagType(item.level)" size="mini">
                              {{ item.level }}
                            </el-tag>
                          </div>
                          <div class="item-slogan">{{ item.slogan }}</div>
                        </div>
                      </div>
                    </el-card>
                  </el-timeline-item>
                </el-timeline>
              </div>

              <!-- 添加分类筛选 -->
              <div class="heritage-filter">
                <el-radio-group v-model="heritageLevel" @change="filterHeritage">
                  <el-radio-button label="">全部</el-radio-button>
                  <el-radio-button label="国家级">国家级</el-radio-button>
                  <el-radio-button label="省级">省级</el-radio-button>
                  <el-radio-button label="市级">市级</el-radio-button>
                  <el-radio-button label="县级">县级</el-radio-button>
                </el-radio-group>
              </div>

              <!-- 优化展示布局 -->
              <div class="heritage-grid">
                <div v-for="item in filteredHeritageList" 
                     :key="item.id" 
                     class="heritage-card"
                     @click="showHeritageDetail(item)">
                  <div class="heritage-card-inner">
                    <div class="heritage-image">
                      <img :src="getImageUrl(item.image)" :alt="item.name">
                      <div class="heritage-year">{{ item.entryTime }}年入选</div>
                    </div>
                    <div class="heritage-info">
                      <h3>{{ item.name }}</h3>
                      <el-tag :type="getHeritageTagType(item.level)" size="small">
                        {{ item.level }}
                      </el-tag>
                      <p class="heritage-slogan">{{ item.slogan }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 添加查看更多按钮 -->
              <div class="view-more-container" v-if="heritageList.length > heritagePageSize">
                <el-button 
                  v-if="!showAllHeritage" 
                  @click="loadMoreHeritage"
                  type="primary"
                  plain
                  class="view-more-btn">
                  查看更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-button 
                  v-else 
                  @click="collapseHeritage"
                  type="info"
                  plain
                  class="view-more-btn">
                  收起<i class="el-icon-arrow-up el-icon--right"></i>
                </el-button>
              </div>

              <!-- 添加详情弹窗 -->
              <el-dialog
                :title="currentHeritage.name"
                :visible.sync="heritageDetailVisible"
                width="70%"
                class="heritage-dialog">
                <div class="heritage-detail">
                  <div class="detail-header">
                    <el-tag :type="getHeritageTagType(currentHeritage.level)">
                      {{ currentHeritage.level }}
                    </el-tag>
                    <span class="entry-time">{{ currentHeritage.entryTime }}年入选</span>
                  </div>
                  <div class="detail-image">
                    <img :src="getImageUrl(currentHeritage.image)" :alt="currentHeritage.name">
                  </div>
                  <div class="detail-slogan">{{ currentHeritage.slogan }}</div>
                  <div class="detail-content">{{ currentHeritage.description }}</div>
                </div>
              </el-dialog>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

      <!-- 文化详情对话框 -->
      <el-dialog
        :title="cultureDetail.title || cultureDetail.name"
        :visible.sync="cultureDialogVisible"
        width="70%"
        class="culture-dialog"
      >
        <div class="culture-detail">
          <div class="detail-header">
            <el-tag :type="getCurrentTagType">{{ cultureDetail.category }}</el-tag>
            <span class="detail-time">发布时间：{{ formatDate(cultureDetail.createTime) }}</span>
          </div>
          <div class="detail-image" v-if="cultureDetail.image">
            <img :src="getImageUrl(cultureDetail.image)" :alt="cultureDetail.title || cultureDetail.name">
          </div>
          <div class="detail-content" v-html="cultureDetail.content"></div>
          <div v-if="activeTab === 'food'" class="detail-price">
            <span class="price-label">参考价格：</span>
            <span class="price-value">¥{{ cultureDetail.price }}</span>
          </div>
        </div>
      </el-dialog>
    </div>

    <!-- 新增：文化活动日历 -->
    <div class="culture-events">
      <h3 class="sub-section-title">文化活动日历</h3>
      <el-calendar v-model="currentDate">
        <template slot="dateCell" slot-scope="{date, data}">
          <div class="calendar-cell">
            {{ data.day.split('-').slice(2).join('') }}
            <div v-if="hasEvent(date)" class="event-dot"></div>
          </div>
        </template>
      </el-calendar>
    </div>

    <el-backtop 
      :bottom="90"
      :right="40"
    >
      <div class="backtop-inner">
        <i class="el-icon-caret-top"></i>
      </div>
    </el-backtop>

    <!-- 视频播放对话框 -->
    <el-dialog
        :title="currentVideo.title"
        :visible.sync="videoDialogVisible"
        width="70%"
        :before-close="handleVideoDialogClose"
    >
      <video :src="getVideoUrl(currentVideo.videoUrl)" controls class="full-width-video" ref="videoPlayer"></video>
      <div class="video-description">{{ currentVideo.description }}</div>
    </el-dialog>

    <!-- 版权信息 -->
    <div class="copyright">
      <p>&copy; 2024 惠农云桥—惠州市龙门县乡村旅游与助农平台. 版权所有.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Stories',
  data() {
    return {
      stories: [],
      currentPage: 1,
      pageSize: 12,
      total: 0,
      dialogVisible: false,
      currentStory: {},
      searchKeyword: '',
      selectedCategory: '',
      videos: [],
      displayedVideos: [],
      showAllVideos: false,
      videoDialogVisible: false,
      currentVideo: {},
      videoPageNum: 1,
      videoPageSize: 10,
      videoTotal: 0,
      activeTab: 'handicraft',
      cultureDetail: {},
      cultureDialogVisible: false,
      handicraftList: [],
      folkList: [],
      foodList: [],
      culturePageSize: 6,
      showAllHandicrafts: false,
      showAllFolks: false,
      showAllFoods: false,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      culturalHighlights: [
        {
          id: 1,
          title: '南昆山国家森林公园',
          description: '生态宝地，自然风光旖旎，远离喧嚣的天然氧吧，被誉为"北回归线上的绿洲"',
          image: require('@/assets/imgs/longmen1.png')
        },
        {
          id: 2,
          title: '龙门南昆山云顶旅游度假区',
          description: '是集自然风光、温泉养生、休闲度假于一体的综合性旅游胜地',
          image: require('@/assets/imgs/longmen2.jpg')
        },
        {
          id: 3,
          title: '龙门功武村',
          description: '是具有六百多年历史的古村落，以保存完好的清代建筑群著称',
          image: require('@/assets/imgs/longmen3.png')
        }
      ],
      culturalSpots: [
        {
          id: 1,
          name: '龙门石窟',
          type: '历史遗迹',
          description: '始建于北魏孝文帝时期的石窟艺术',
          location: '龙门镇',
          rating: 4.9,
          image: require('@/assets/imgs/lun-5.jpg')
        },
        {
          id: 2,
          name: '南昆山国家森林公园',
          type: '自然景观',
          description: '北回归线上的绿洲，生态旅游胜地',
          location: '龙门县南部',
          rating: 4.8,
          image: require('@/assets/imgs/huidonglun_8.jpg')
        },
        {
          id: 3,
          name: '龙门温泉度假区',
          type: '休闲度假',
          description: '集温泉、住宿、餐饮于一体的综合度假区',
          location: '龙门县城东',
          rating: 4.7,
          image: require('@/assets/imgs/lun-7.png')
        }
      ],
      heritageList: [],
      filteredHeritageList: [],
      heritageLevel: '',
      heritageStats: {
        national: 0,
        provincial: 0,
        city: 0,
        county: 0
      },
      showAllHeritage: false,
      heritagePageSize: 6,
      heritageDetailVisible: false,
      currentHeritage: {},
      culturalEvents: [
        // 文化活动数据
      ],
      timelineYears: [],
      navItems: [
        { name: '助农故事分享', icon: 'el-icon-collection', target: 'stories' },
        { name: '扶贫助农微视频', icon: 'el-icon-video-camera', target: 'videos' },
        { name: '龙门文化展示', icon: 'el-icon-collection-tag', target: 'culture' }
      ],
      currentSection: 'stories'
    }
  },
  computed: {
    // Removed computed properties related to Q&A and feedback
  },
  created() {
    this.fetchStories()
    this.fetchVideos()
    this.fetchHandicrafts()
    this.fetchHeritageList()
  },
  methods: {
    fetchStories() {
      this.$request.get('/stories/selectPage', {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          name: this.searchKeyword,
          category: this.selectedCategory
        }
      }).then(res => {
        if (res.code === '200') {
          this.stories = res.data.list
          this.total = res.data.total
        } else {
          this.$message.error('获取故事列表失败')
        }
      }).catch(err => {
        console.error('获取故事列表出错:', err)
        this.$message.error('获取故事列表出错')
      })
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.fetchStories()
    },
    viewStory(story) {
      this.currentStory = story
      this.dialogVisible = true
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done()
          })
          .catch(_ => {})
    },
    formatDate(dateString) {
      if (!dateString) return '未知日期'
      const date = new Date(dateString)
      return `${date.getFullYear()}年${String(date.getMonth() + 1).padStart(2, '0')}月${String(date.getDate()).padStart(2, '0')}日`
    },
    handleSearch() {
      this.currentPage = 1
      this.fetchStories()
    },
    handleCategoryChange() {
      this.currentPage = 1
      this.fetchStories()
    },
    shareStory() {
      this.$message.success('分享功能待实现')
    },
    fetchVideos() {
      this.$request.get('/videos/selectPage', {
        params: {
          pageNum: this.videoPageNum,
          pageSize: this.videoPageSize,
          title: '',
        }
      }).then(res => {
        if (res.code === '200') {
          this.videos = res.data.list
          this.videoTotal = res.data.total
          this.displayedVideos = this.videos.slice(0, 4)
        } else {
          this.$message.error('获取视频列表失败')
        }
      }).catch(err => {
        console.error('获取视频列表出错:', err)
        this.$message.error('获取视频列表出错')
      })
    },
    loadMoreVideos() {
      this.showAllVideos = true;
      this.displayedVideos = this.videos;
    },
    collapseVideos() {
      this.showAllVideos = false;
      this.displayedVideos = this.videos.slice(0, 4);
    },
    playVideo(video) {
      this.currentVideo = video
      this.videoDialogVisible = true
    },
    handleVideoDialogClose(done) {
      this.$confirm('确认关闭视频？')
          .then(_ => {
            if (this.$refs.videoPlayer) {
              this.$refs.videoPlayer.pause();
              this.$refs.videoPlayer.currentTime = 0;
            }
            done();
          })
          .catch(_ => {});
    },
    getImageUrl(path) {
      if (!path) return '';
      if (typeof path === 'string' && (path.startsWith('http') || path.startsWith('data:'))) {
        return path;
      }
      if (typeof path === 'object' && path.hasOwnProperty('default')) {
        return path.default;
      }
      return path;
    },
    getVideoUrl(path) {
      if (path && !path.startsWith('http')) {
        return `${this.$baseUrl}/files/${path}`
      }
      return path
    },
    showCultureDetail(type, item) {
      this.cultureDetail = item
      this.cultureDialogVisible = true
    },
    getTagType(category) {
      const types = {
        '编织工艺': 'success',
        '造纸工艺': 'warning',
        '木雕工艺': 'danger',
        '医疗技艺': 'info',
        '制作工艺': 'primary'
      }
      return types[category] || ''
    },
    getFolkTagType(category) {
      return category === '节日习俗' ? 'success' : 
             category === '民间艺术' ? 'warning' : 'info'
    },
    getFoodTagType(category) {
      const types = {
        '特色小吃': 'success',
        '农家菜品': 'warning',
        '地方特产': 'danger',
        '传统美食': 'info'
      }
      return types[category] || ''
    },
    getCurrentTagType() {
      switch(this.activeTab) {
        case 'handicraft':
          return this.getTagType(this.cultureDetail.category)
        case 'folk':
          return this.getFolkTagType(this.cultureDetail.category)
        case 'food':
          return this.getFoodTagType(this.cultureDetail.category)
        default:
          return ''
      }
    },
    handleTabClick(tab) {
      switch(tab.name) {
        case 'handicraft':
          this.fetchHandicrafts()
          break
        case 'folk':
          this.fetchFolks()
          break
        case 'food':
          this.fetchFoods()
          break
      }
    },
    fetchHandicrafts() {
      this.$request.get('/handicraft/selectPage', {
        params: { 
          pageNum: 1, 
          pageSize: this.showAllHandicrafts ? 999 : this.culturePageSize 
        }
      }).then(res => {
        if (res.code === '200') {
          this.handicraftList = res.data.list
        }
      })
    },
    fetchFolks() {
      this.$request.get('/folk/selectPage', {
        params: { 
          pageNum: 1, 
          pageSize: this.showAllFolks ? 999 : this.culturePageSize 
        }
      }).then(res => {
        if (res.code === '200') {
          this.folkList = res.data.list
        }
      })
    },
    fetchFoods() {
      this.$request.get('/food/selectPage', {
        params: { 
          pageNum: 1, 
          pageSize: this.showAllFoods ? 999 : this.culturePageSize 
        }
      }).then(res => {
        if (res.code === '200') {
          this.foodList = res.data.list
        }
      })
    },
    toggleHandicrafts() {
      this.showAllHandicrafts = !this.showAllHandicrafts
      this.fetchHandicrafts()
    },
    toggleFolks() {
      this.showAllFolks = !this.showAllFolks
      this.fetchFolks()
    },
    toggleFoods() {
      this.showAllFoods = !this.showAllFoods
      this.fetchFoods()
    },
    hasEvent(date) {
      // 检查该日期是否有文化活动
      return this.culturalEvents.some(event => 
        event.date.toDateString() === date.toDateString()
      )
    },
    showSpotDetail(spot) {
      // 显示景点详情
    },
    fetchHeritageList() {
      this.$request.get('/heritage/selectPage', {
        params: {
          pageNum: 1,
          pageSize: 999
        }
      }).then(res => {
        if (res.code === '200') {
          this.heritageList = res.data.list
          this.updateFilteredList()
          this.calculateHeritageStats()
          this.getTimelineYears()
        }
      })
    },
    calculateHeritageStats() {
      this.heritageStats = {
        national: this.heritageList.filter(item => item.level === '国家级').length,
        provincial: this.heritageList.filter(item => item.level === '省级').length,
        city: this.heritageList.filter(item => item.level === '市级').length,
        county: this.heritageList.filter(item => item.level === '县级').length
      }
    },
    filterHeritage(level) {
      this.heritageLevel = level
      this.updateFilteredList()
    },
    loadMoreHeritage() {
      this.showAllHeritage = true
      this.updateFilteredList()
    },
    collapseHeritage() {
      this.showAllHeritage = false
      this.updateFilteredList()
    },
    showHeritageDetail(item) {
      this.currentHeritage = item
      this.heritageDetailVisible = true
    },
    getHeritageTagType(level) {
      return level === '国家级' ? 'danger' : 
             level === '省级' ? 'warning' : 
             level === '市级' ? 'success' : 'info'
    },
    updateFilteredList() {
      if (this.heritageLevel) {
        const filtered = this.heritageList.filter(item => item.level === this.heritageLevel)
        this.filteredHeritageList = this.showAllHeritage ? filtered : filtered.slice(0, this.heritagePageSize)
      } else {
        this.filteredHeritageList = this.showAllHeritage ? this.heritageList : this.heritageList.slice(0, this.heritagePageSize)
      }
    },
    getTimelineYears() {
      const years = [...new Set(this.heritageList.map(item => item.entryTime))]
      this.timelineYears = years.sort((a, b) => b - a)
    },
    getHeritageByYear(year) {
      return this.heritageList.filter(item => item.entryTime === year)
    },
    getTimelineItemType(year) {
      const currentYear = new Date().getFullYear().toString()
      if (year === currentYear) return 'primary'
      return 'success'
    },
    getTimelineItemSize(year) {
      const currentYear = new Date().getFullYear().toString()
      if (year === currentYear) return 'large'
      return 'normal'
    },
    getTimelineIcon(year) {
      const currentYear = new Date().getFullYear().toString()
      if (year === currentYear) return 'el-icon-star-on'
      return ''
    },
    scrollToSection(target) {
      const element = document.getElementById(target)
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'start' })
        this.currentSection = target
      }
    },
    handleScroll() {
      const sections = ['stories', 'videos', 'culture']
      for (let section of sections) {
        const element = document.getElementById(section)
        if (element) {
          const rect = element.getBoundingClientRect()
          if (rect.top <= 100 && rect.bottom >= 100) {
            this.currentSection = section
            break
          }
        }
      }
    }
  },
  watch: {
    videoDialogVisible(newVal) {
      if (!newVal && this.$refs.videoPlayer) {
        this.$refs.videoPlayer.pause();
        this.$refs.videoPlayer.currentTime = 0;
      }
    }
  },
  mounted() {
    // 添加滚动监听以更新当前section
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll)
  }
}
</script>

<style scoped>
@import "@/assets/css/theme/front_css/story1.css";
@import "@/assets/css/theme/front_css/story2.css";


</style>

