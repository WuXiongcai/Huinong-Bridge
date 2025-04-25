<template>
  <div class="sight-container">
    <div class="sight-content">
      <!-- 左侧景点列表 -->
      <div class="sight-list">
        <h2>龙门县游玩景点推荐</h2>
        <div v-for="sight in sights" :key="sight.id" class="sight-item">
          <div class="sight-image" v-if="sight.image">
            <img :src="getImageUrl(sight.image)" :alt="sight.name">
          </div>
          <h3>{{ sight.name }}</h3>
          <p class="sight-type">
            <el-tag :type="getTagType(sight.category)">{{ sight.category }}</el-tag>
          </p>
          <p class="sight-feature" v-if="sight.feature">{{ sight.feature }}</p>
          <p class="sight-address">地址：{{ sight.address }}</p>
          <p v-if="sight.openTime" class="sight-time">
            开放时间：{{ sight.openTime }}
          </p>
          <p v-if="sight.content" class="sight-price">
            收费情况：{{ sight.content }}
          </p>
          <div class="sight-actions">
            <el-button 
              type="primary" 
              size="small" 
              @click="showNavigation(sight)"
            >
              导航路线
            </el-button>
            <el-button 
              type="info" 
              size="small" 
              @click="showDetails(sight)"
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

    <!-- 评论区域 -->
    <div class="comment-section">
      <div class="comment-header">
        <h2>游玩评论与经验分享</h2>
        <el-button type="primary" @click="showCommentDialog">发表评论</el-button>
      </div>
      
      <div class="comment-list">
        <div v-for="(comment, index) in displayedComments" :key="comment.id" class="comment-item">
          <div class="comment-main">
            <div class="comment-user">
              <i class="el-icon-user"></i>
              <span>{{ comment.userName }}</span>
              <span class="comment-time">{{ comment.time }}</span>
            </div>
            <div class="comment-content" :class="{ 'is-expanded': comment.expanded }">
              <h3 v-if="comment.title">{{ comment.title }}</h3>
              <p>{{ comment.content }}</p>
            </div>
            <div class="comment-actions">
              <el-button 
                type="text" 
                @click="comment.expanded = !comment.expanded"
                v-if="comment.content.length > 200"
              >
                {{ comment.expanded ? '收起' : '展开' }}
              </el-button>
              <el-button type="text" @click="showReplyDialog(comment)">
                <i class="el-icon-chat-line-round"></i> 回复
              </el-button>
              <el-button type="text" @click="likeComment(comment)">
                <i :class="comment.liked ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
                {{ comment.likes || 0 }}
              </el-button>
            </div>
            
            <!-- 回复列表 -->
            <div class="reply-list" v-if="comment.replies && comment.replies.length">
              <div v-for="(reply, replyIndex) in displayedReplies(comment)" :key="reply.id" class="reply-item">
                <div class="reply-user">
                  <i class="el-icon-user"></i>
                  <span>{{ reply.userName }}</span>
                  <span class="reply-time">{{ reply.time }}</span>
                </div>
                <div class="reply-content">{{ reply.content }}</div>
              </div>
              
              <!-- 回复的展开/收起按钮 -->
              <div class="show-more-replies" v-if="comment.replies.length > 3">
                <el-button 
                  type="text" 
                  @click="toggleReplies(comment)"
                >
                  {{ comment.showAllReplies ? '收起回复' : `查看更多回复 (${comment.replies.length - 3})` }}
                </el-button>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 评论的展开/收起按钮 -->
        <div class="show-more-comments" v-if="comments.length > 3">
          <el-button 
            type="text" 
            @click="toggleComments"
          >
            {{ showAllComments ? '收起评论' : `查看更多评论 (${comments.length - 3})` }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- 导航对话框 -->
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

    <!-- 景点详情对话框 -->
    <el-dialog 
      title="景点详情" 
      :visible.sync="detailsDialogVisible" 
      width="600px"
    >
      <div v-if="currentSight" class="sight-details">
        <div class="detail-image" v-if="currentSight.image">
          <img :src="getImageUrl(currentSight.image)" :alt="currentSight.name">
        </div>
        <h2>{{ currentSight.name }}</h2>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="分类">
            <el-tag :type="getTagType(currentSight.category)">
              {{ currentSight.category }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="特色" v-if="currentSight.feature">
            {{ currentSight.feature }}
          </el-descriptions-item>
          <el-descriptions-item label="地址">
            {{ currentSight.address }}
          </el-descriptions-item>
          <el-descriptions-item label="开放时间" v-if="currentSight.openTime">
            {{ currentSight.openTime }}
          </el-descriptions-item>
          <el-descriptions-item label="收费情况" v-if="currentSight.content">
            {{ currentSight.content }}
          </el-descriptions-item>
          <el-descriptions-item label="景点简介" v-if="currentSight.description">
            {{ currentSight.description }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 版权信息 -->
    <div class="copyright">
      <p>&copy; 2024 惠农云桥—惠州市龙门县乡村旅游与助农平台. 版权所有.</p>
    </div>

    <!-- 回到顶部按钮 -->
    <el-backtop 
      :right="40" 
      :bottom="90"
      :visibility-height="400"
    >
      <div class="backtop-inner">
        <i class="el-icon-caret-top"></i>
        <span>顶部</span>
      </div>
    </el-backtop>

    <!-- 发表评论对话框 -->
    <el-dialog 
      title="发表评论" 
      :visible.sync="commentDialogVisible" 
      width="50%"
    >
      <el-form :model="commentForm" :rules="commentRules" ref="commentForm">
        <el-form-item label="标题" prop="title">
          <el-input v-model="commentForm.title" placeholder="请输入标题（选填）"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input 
            type="textarea" 
            v-model="commentForm.content" 
            :rows="6"
            placeholder="分享您的游玩经验和建议"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="commentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitComment">发表</el-button>
      </span>
    </el-dialog>

    <!-- 回复对话框 -->
    <el-dialog 
      title="回复评论" 
      :visible.sync="replyDialogVisible" 
      width="40%"
    >
      <el-form :model="replyForm" :rules="replyRules" ref="replyForm">
        <el-form-item label="内容" prop="content">
          <el-input 
            type="textarea" 
            v-model="replyForm.content" 
            :rows="4"
            placeholder="请输入回复内容"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReply">回复</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader'

export default {
  name: 'Sight',
  data() {
    return {
      map: null,
      sights: [],
      markers: [],
      navigationDialogVisible: false,
      detailsDialogVisible: false,
      navigationForm: {
        startAddress: '',
        endAddress: ''
      },
      currentSight: null,
      comments: [],
      commentDialogVisible: false,
      replyDialogVisible: false,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      commentForm: {
        userId: null,
        title: '',
        content: '',
        parentId: 0
      },
      replyForm: {
        userId: null,
        content: '',
        parentId: null
      },
      currentComment: null,
      commentRules: {
        content: [
          { required: true, message: '请输入评论内容', trigger: 'blur' },
          { min: 5, message: '内容至少 5 个字符', trigger: 'blur' }
        ]
      },
      replyRules: {
        content: [
          { required: true, message: '请输入回复内容', trigger: 'blur' },
          { min: 2, message: '内容至少 2 个字符', trigger: 'blur' }
        ]
      },
      showAllComments: false, // 控制是否显示所有评论
    }
  },
  computed: {
    displayedComments() {
      return this.showAllComments ? this.comments : this.comments.slice(0, 3);
    }
  },
  created() {
    this.loadSights()
    this.loadComments()
    this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
    if (this.user.id) {
      this.loadComments()
    }
  },
  mounted() {
    this.initMap()
  },
  methods: {
    loadSights() {
      this.$request.get('/sight/selectAll').then(res => {
        if (res.code === '200') {
          this.sights = res.data
          this.updateMapMarkers()
        }
      })
    },
    async initMap() {
      try {
        const AMap = await AMapLoader.load({
          key: 'e9e7c13059174d930b578eca947385ef',
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

        this.updateMapMarkers()
      } catch (e) {
        console.error('地图加载失败：', e)
      }
    },
    updateMapMarkers() {
      if (!this.map || !this.sights.length) return

      // 清除现有标记
      this.markers.forEach(marker => marker.setMap(null))
      this.markers = []

      // 添加新标记
      this.sights.forEach(sight => {
        if (sight.longitude && sight.latitude) {
          const marker = new AMap.Marker({
            position: [sight.longitude, sight.latitude],
            title: sight.name
          })
          marker.setMap(this.map)
          this.markers.push(marker)

          // 添加点击事件
          marker.on('click', () => {
            this.showDetails(sight)
          })
        }
      })
    },
    showNavigation(sight) {
      this.currentSight = sight
      this.navigationForm.endAddress = sight.address
      this.navigationDialogVisible = true
    },
    showDetails(sight) {
      this.currentSight = sight
      this.detailsDialogVisible = true
    },
    startNavigation() {
      if (!this.navigationForm.startAddress) {
        this.$message.error('请输入起点地址')
        return
      }
      
      window.open(`https://uri.amap.com/navigation?from=${this.navigationForm.startAddress}&to=${this.navigationForm.endAddress}&mode=car&policy=1`)
      this.navigationDialogVisible = false
    },
    getImageUrl(path) {
      if (path && !path.startsWith('http')) {
        return this.$baseUrl + '/files/' + path
      }
      return path
    },
    getTagType(category) {
      const types = {
        '自然景观': 'success',
        '人文古迹': 'warning',
        '休闲娱乐': 'primary',
        '特色景点': 'info'
      }
      return types[category] || ''
    },
    loadComments() {
      this.$request.get('/sightcomment/list').then(res => {
        if (res.code === '200') {
          this.comments = res.data.map(comment => ({
            ...comment,
            expanded: false,
            liked: this.checkIfLiked(comment.id),
            replies: comment.replies?.map(reply => ({
              ...reply,
              liked: this.checkIfLiked(reply.id)
            })) || []
          }))
        }
      })
    },
    showCommentDialog() {
      if (!this.user.id) {
        this.$message.warning('请先登录')
        return
      }
      this.commentForm = { 
        userId: this.user.id,
        title: '', 
        content: '',
        parentId: 0
      }
      this.commentDialogVisible = true
    },
    showReplyDialog(comment) {
      if (!this.user.id) {
        this.$message.warning('请先登录')
        return
      }
      this.currentComment = comment
      this.replyForm = { 
        userId: this.user.id,
        content: '', 
        parentId: comment.id 
      }
      this.replyDialogVisible = true
    },
    submitComment() {
      this.$refs.commentForm.validate(valid => {
        if (valid) {
          if (!this.user.id) {
            this.$message.warning('请先登录')
            return
          }
          this.$request.post('/sightcomment/add', this.commentForm).then(res => {
            if (res.code === '200') {
              this.$message.success('发表成功')
              this.commentDialogVisible = false
              this.loadComments()
            } else {
              this.$message.error(res.msg || '发表失败')
            }
          })
        }
      })
    },
    submitReply() {
      this.$refs.replyForm.validate(valid => {
        if (valid) {
          if (!this.user.id) {
            this.$message.warning('请先登录')
            return
          }
          this.$request.post('/sightcomment/add', this.replyForm).then(res => {
            if (res.code === '200') {
              this.$message.success('回复成功')
              this.replyDialogVisible = false
              this.loadComments()
            } else {
              this.$message.error(res.msg || '回复失败')
            }
          })
        }
      })
    },
    likeComment(comment) {
      if (!this.user.id) {
        this.$message.warning('请先登录')
        return
      }
      
      const isLike = !comment.liked
      this.$request.post(`/sightcomment/like/${comment.id}`, null, {
        params: { isLike }
      }).then(res => {
        if (res.code === '200') {
          comment.liked = isLike
          comment.likes = isLike ? (comment.likes || 0) + 1 : (comment.likes || 1) - 1
          
          const likedComments = JSON.parse(localStorage.getItem(`sight-comment-likes-${this.user.id}`) || '[]')
          if (isLike) {
            likedComments.push(comment.id)
          } else {
            const index = likedComments.indexOf(comment.id)
            if (index > -1) {
              likedComments.splice(index, 1)
            }
          }
          localStorage.setItem(`sight-comment-likes-${this.user.id}`, JSON.stringify(likedComments))
        }
      })
    },
    checkIfLiked(commentId) {
      const likedComments = JSON.parse(localStorage.getItem(`sight-comment-likes-${this.user.id}`) || '[]')
      return likedComments.includes(commentId)
    },
    // 切换评论的展开/收起状态
    toggleComments() {
      this.showAllComments = !this.showAllComments;
    },
    
    // 切换特定评论的回复展开/收起状态
    toggleReplies(comment) {
      this.$set(comment, 'showAllReplies', !comment.showAllReplies);
    },
    
    // 获取要显示的回复列表
    displayedReplies(comment) {
      if (!comment.replies) return [];
      return comment.showAllReplies ? comment.replies : comment.replies.slice(0, 3);
    }
  },
  watch: {
    sights: {
      handler() {
        this.updateMapMarkers()
      },
      deep: true
    },
    'user.id'(newVal) {
      if (newVal) {
        this.loadComments()
      }
    }
  }
}
</script>

<style scoped>
.sight-container {
  width: 100%;
  height: 100vh;
  box-sizing: border-box;
}

.sight-content {
  display: flex;
  height: calc(100% - 40px);
}

.sight-list {
  width: 380px;
  padding: 20px;
  background: #fff;
  border-radius: 0;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  height: 100vh;
}

.sight-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  margin-bottom: 10px;
  background: #f9f9f9;
  border-radius: 4px;
  transition: all 0.3s;
}

.sight-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.sight-image {
  width: 100%;
  height: 180px;
  margin-bottom: 10px;
  overflow: hidden;
  border-radius: 4px;
}

.sight-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.sight-item h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.sight-type {
  margin: 10px 0;
}

.sight-feature {
  color: #666;
  font-size: 14px;
  margin: 5px 0;
}

.sight-address {
  color: #666;
  font-size: 14px;
  margin: 5px 0;
}

.sight-time {
  color: #666;
  font-size: 14px;
  margin: 5px 0;
}

.sight-price {
  color: #666;
  font-size: 14px;
  margin: 5px 0;
}

.sight-actions {
  margin-top: 10px;
  display: flex;
  gap: 10px;
}

.map-container {
  flex: 1;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: 100vh;
}

#map-container {
  width: 100%;
  height: 100%;
}

.sight-details {
  padding: 20px;
}

.detail-image {
  width: 100%;
  height: 300px;
  margin-bottom: 20px;
  overflow: hidden;
  border-radius: 4px;
}

.detail-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 适配移动设备 */
@media screen and (max-width: 768px) {
  .sight-content {
    flex-direction: column;
  }

  .sight-list {
    width: 100%;
    height: 50vh;
  }

  .map-container {
    height: 50vh;
  }

  .sight-image {
    height: 150px;
  }

  .comment-section {
    padding: 15px;
    margin: 20px 0;
  }
  
  .comment-content {
    font-size: 14px;
  }
  
  .reply-list {
    padding-left: 15px;
  }

  .comment-header h2 {
    font-size: 18px;
  }
}

/* 适配大屏幕 */
@media screen and (min-width: 1440px) {
  .sight-list {
    width: 420px;
  }

  .sight-image {
    height: 220px;
  }
}

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

.comment-section {
  background: #fff;
  padding: 20px;
  margin: 40px 0;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.comment-section::before {
  content: '';
  display: block;
  height: 1px;
  background: #eee;
  margin: -20px -20px 20px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f0f2f5;
}

.comment-header h2 {
  font-size: 20px;
  color: #303133;
  font-weight: 600;
}

.comment-list {
  margin-top: 20px;
}

.comment-item {
  border-bottom: 1px solid #eee;
  padding: 20px 0;
}

.comment-user {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #666;
  margin-bottom: 10px;
}

.comment-time {
  color: #999;
  font-size: 12px;
}

.comment-content {
  margin: 10px 0;
  line-height: 1.6;
  max-height: 100px;
  overflow: hidden;
  transition: max-height 0.3s;
}

.comment-content.is-expanded {
  max-height: none;
}

.comment-content h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.comment-actions {
  display: flex;
  gap: 15px;
  margin-top: 10px;
}

.reply-list {
  margin-top: 15px;
  padding-left: 20px;
  border-left: 2px solid #eee;
}

.reply-item {
  padding: 10px 0;
}

.reply-user {
  font-size: 13px;
  color: #666;
  margin-bottom: 5px;
}

.reply-time {
  font-size: 12px;
  color: #999;
  margin-left: 10px;
}

.reply-content {
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}

.backtop-inner {
  background-color: #409EFF;
  color: #fff;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s;
}

.backtop-inner:hover {
  background-color: #66b1ff;
  transform: translateY(-2px);
}

.backtop-inner i {
  font-size: 16px;
  margin-bottom: 2px;
}

/* 适配移动设备 */
@media screen and (max-width: 768px) {
  .backtop-inner {
    width: 36px;
    height: 36px;
    font-size: 10px;
  }

  .backtop-inner i {
    font-size: 14px;
  }
}

.show-more-comments,
.show-more-replies {
  text-align: center;
  padding: 10px 0;
  margin-top: 10px;
  border-top: 1px dashed #eee;
}

.show-more-comments .el-button,
.show-more-replies .el-button {
  font-size: 14px;
  color: #409EFF;
}

.show-more-comments .el-button:hover,
.show-more-replies .el-button:hover {
  color: #66b1ff;
}

.show-more-replies {
  margin-left: -20px;
  margin-right: -10px;
  padding: 8px 0;
}

/* 适配移动设备 */
@media screen and (max-width: 768px) {
  .show-more-comments .el-button,
  .show-more-replies .el-button {
    font-size: 13px;
  }
  
  .show-more-replies {
    margin-left: -15px;
    margin-right: -5px;
  }
}
</style>
