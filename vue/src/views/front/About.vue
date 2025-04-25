<template>
  <div class="about-us-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <h1 class="page-title">关于惠农云桥</h1>
        <div class="page-subtitle">助力龙门县乡村振兴 • 共建美好家园</div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-4">
      <el-col :span="24">
        <el-carousel :interval="5000" type="card" height="300px">
          <el-carousel-item v-for="item in carouselItems" :key="item.id">
            <img :src="item.imageUrl" :alt="item.title" class="carousel-image">
            <h3 class="carousel-title">{{ item.title }}</h3>
          </el-carousel-item>
        </el-carousel>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-4">
      <el-col :span="24">
        <el-card class="about-card">
          <div slot="header" class="card-header">
            <i class="el-icon-platform-eleme"></i>
            <span>平台简介</span>
          </div>
          <div class="card-content">
            <p>惠农云桥是一个专注于惠州市龙门县乡村农业发展的综合性平台，致力于通过互联网技术和现代化管理手段，推动龙门县乡村农业和旅游业的可持续发展。我们提供多元化服务，包括：</p>
            <div class="feature-grid">
              <div class="feature-item">
                <i class="el-icon-news"></i>
                <h4>政策资讯</h4>
                <p>发布最新农业政策与补贴信息</p>
              </div>
              <div class="feature-item">
                <i class="el-icon-shopping-cart-full"></i>
                <h4>农产品销售</h4>
                <p>特色农产品线上展示与交易</p>
              </div>
              <div class="feature-item">
                <i class="el-icon-school"></i>
                <h4>旅游景点推荐</h4>
                <p>推荐龙门县特色的旅游景点与农家乐</p>
              </div>
              <div class="feature-item">
                <i class="el-icon-picture"></i>
                <h4>文化传播</h4>
                <p>展示乡村文化与农村故事</p>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-4">
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="about-card">
          <div slot="header" class="card-header">
            <i class="el-icon-aim"></i>
            <span>我们的使命</span>
          </div>
          <div class="card-content">
            <p>惠农云桥致力于通过创新的方式连接城乡，促进农村发展，帮助农民增收，实现共同富裕。我们相信，通过科技、教育和市场的力量，可以为乡村振兴注入新的活力。</p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="about-card">
          <div slot="header" class="card-header">
            <i class="el-icon-view"></i>
            <span>我们的愿景</span>
          </div>
          <div class="card-content">
            <p>我们期望成为中国领先的惠州市龙门县乡村旅游与助农平台，通过连接城乡资源，推动农村经济发展，提高农民生活质量，促进城乡融合，最终实现乡村振兴的宏伟目标。</p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8">
        <el-card class="about-card">
          <div slot="header" class="card-header">
            <i class="el-icon-star-on"></i>
            <span>我们的价值观</span>
          </div>
          <div class="card-content">
            <ul>
              <li><strong>诚信为本：</strong>诚实守信，言行一致</li>
              <li><strong>创新驱动：</strong>拥抱变化，持续创新</li>
              <li><strong>合作共赢：</strong>团结协作，互利共赢</li>
              <li><strong>以人为本：</strong>关爱农民，服务三农</li>
              <li><strong>可持续发展：</strong>平衡经济、社会和环境效益</li>
            </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-4">
      <el-col :span="24">
        <el-card class="about-card">
          <div slot="header" class="card-header">
            <i class="el-icon-notebook-1"></i>
            <span>我们的故事</span>
          </div>
          <div class="card-content">
            <div class="story-timeline">
              <div class="timeline-item">
                <div class="timeline-icon">
                  <i class="el-icon-star-on"></i>
                </div>
                <div class="timeline-content">
                  <h3>项目起源</h3>
                  <p>惠农云桥项目源于我们对惠州市龙门县农业发展现状的深入调研。作为惠州学院的在校大学生，我们发现龙门县拥有丰富的农业资源和独特的乡村风光，但农产品销售渠道不畅、信息化程度低等问题制约着农村发展。</p>
                </div>
              </div>
              <div class="timeline-item">
                <div class="timeline-icon">
                  <i class="el-icon-trophy"></i>
                </div>
                <div class="timeline-content">
                  <h3>创新创业</h3>
                  <p>2023年，我们组建团队参加"挑战杯"大学生创业大赛，将这个想法转化为切实可行的创业项目。通过多轮比赛的打磨，项目方案不断完善，获得了评委们的认可。</p>
                </div>
              </div>
              <div class="timeline-item">
                <div class="timeline-icon">
                  <i class="el-icon-success"></i>
                </div>
                <div class="timeline-content">
                  <h3>项目成果</h3>
                  <p>目前，惠农云桥已完成平台基础建设，成功对接15个村的特色农产品，帮助50多户农民打开线上销售渠道。我们的创新实践也获得了省级创新创业大赛的认可。</p>
                  <div class="achievement-stats">
                    <el-row :gutter="20" class="mt-4">
                      <el-col :span="8">
                        <el-statistic title="合作村庄" :value="15" suffix="个">
                          <template slot="prefix">
                            <i class="el-icon-house" style="color: #19a14b"></i>
                          </template>
                        </el-statistic>
                      </el-col>
                      <el-col :span="8">
                        <el-statistic title="帮扶农户" :value="50" suffix="户">
                          <template slot="prefix">
                            <i class="el-icon-user" style="color: #19a14b"></i>
                          </template>
                        </el-statistic>
                      </el-col>
                      <el-col :span="8">
                        <el-statistic title="平台访问量" :value="12800" suffix="次">
                          <template slot="prefix">
                            <i class="el-icon-view" style="color: #19a14b"></i>
                          </template>
                        </el-statistic>
                      </el-col>
                    </el-row>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-4">
      <el-col :span="24">
        <el-card class="about-card">
          <div slot="header" class="card-header">
            <i class="el-icon-user"></i>
            <span>核心团队</span>
          </div>
          <div class="card-content team-grid-five">
            <div v-for="member in teamMembers" :key="member.name" class="team-member">
              <el-avatar :size="80" :src="member.avatar"></el-avatar>
              <h3>{{ member.name }}</h3>
              <p class="member-title">{{ member.position }}</p>
              <p class="member-description">{{ member.description }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-4">
      <el-col :span="24">
        <el-card class="about-card">
          <div slot="header" class="card-header">
            <i class="el-icon-phone"></i>
            <span>联系我们</span>
          </div>
          <div class="card-content">
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12">
                <p><i class="el-icon-location"></i> 地址：广东省惠州市惠城区演达大道46号惠州学院</p>
                <p><i class="el-icon-phone"></i> 电话：15019816191</p>
                <p><i class="el-icon-message"></i> 1357781541@qq.com</p>
                <el-button type="primary" icon="el-icon-message" @click="showContactForm">给我们留言</el-button>
              </el-col>
              <el-col :xs="24" :sm="12">
                <div class="map-container">
                  <!-- 这里可以嵌入一个实际的地图组件，如高德地图或百度地图 -->
                  <img :src="require('@/assets/imgs/hzu.jpg')" alt="地图占位图" style="width: 100%; height: 200px; object-fit: cover;">
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog title="联系我们" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
      <el-form ref="contactForm" :model="contactForm" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="contactForm.name"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="contactForm.email"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="contactForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="留言" prop="content">
          <el-input type="textarea" v-model="contactForm.content"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitMessage">确 定</el-button>
    </span>
    </el-dialog>

    <el-backtop :bottom="100">
      <div class="backtop-inner">
        <i class="el-icon-caret-top"></i>
      </div>
    </el-backtop>
  </div>
</template>

<script>
export default {
  name: 'About',
  data() {
    return {
      carouselItems: [
        { id: 1, imageUrl: require('@/assets/imgs/美丽乡村.jpg'), title: '龙门县美丽乡村风光' },
        { id: 2, imageUrl: require('@/assets/imgs/增收致富.jpeg'), title: '助农增收计划' },
        { id: 3, imageUrl: require('@/assets/imgs/城乡发展.png'), title: '数字化助农服务' },
      ],
      teamMembers: [
        { 
          name: '巫雄才', 
          position: '项目创始人', 
          avatar: require('@/assets/imgs/wxc.jpg'), 
          description: '惠州学院软件工程专业，负责项目整体规划与技术实现，曾获省级创新创业大赛奖项' 
        },
        { 
          name: '吕振晖', 
          position: '团队负责人', 
          avatar: require('@/assets/imgs/lzh.jpg'), 
          description: '惠州学院工程管理专业，负责整个项目的运营与统筹规划，曾获得无数创新创业类奖项' 
        },
        { 
          name: '邓振武', 
          position: '技术总监', 
          avatar: require('@/assets/imgs/dzw.jpg'), 
          description: '惠州学院数据科学与大数据技术专业，负责产品设计与运营，具有丰富的数据分析经验' 
        },
        { 
          name: '钟丽苹', 
          position: '市场总监兼法律顾问', 
          avatar: require('@/assets/imgs/zlp.jpg'), 
          description: '惠州学院法学专业，负责市场调研与推广，以及平台法律法规合法性维护等' 
        },
        { 
          name: '刘祺', 
          position: '农业顾问', 
          avatar: require('@/assets/imgs/67.jpg'), 
          description: '惠州学院数学专业，专注农业技术研究，负责农户对接与项目申请等' 
        }
      ],
      dialogVisible: false,
      contactForm: {
        name: '',
        email: '',
        content: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入您的姓名', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入您的邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        content: [
          { required: true, message: '请输入您的留言', trigger: 'blur' },
          { min: 10, max: 500, message: '长度在 10 到 500 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    showContactForm() {
      this.dialogVisible = true
    },

    submitMessage() {
      this.$refs.contactForm.validate((valid) => {
        if (valid) {
          this.$request.post('/message/add', this.contactForm).then(res => {
            if (res.code === '200') {
              this.$message.success('感谢您的留言，我们会尽快回复！')
              this.dialogVisible = false
              this.$refs.contactForm.resetFields()
            } else {
              this.$message.error(res.msg)
            }
          })
        } else {
          console.log('表单验证失败')
          return false
        }
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/theme/front_css/about.css";
</style>