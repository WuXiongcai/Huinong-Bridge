<template>
  <div class="main-content">
    <el-card class="info-card">
      <div class="info-header">
        <h1 class="info-title">{{ informationData.name }}</h1>
        <div class="info-meta">
          <el-tag size="small" type="info">
            <i class="el-icon-date"></i>
            发布时间：{{ informationData.time }}
          </el-tag>
          <el-tag size="small" type="success">
            <i class="el-icon-view"></i>
            阅读量：{{ informationData.views || 0 }}
          </el-tag>
        </div>
      </div>

      <el-divider content-position="left">
        <i class="el-icon-news"></i> 资讯内容
      </el-divider>

      <div v-html="informationData.content" class="info-content w-e-text w-e-text-container"></div>

      <el-divider content-position="left">
        <i class="el-icon-connection"></i> 相关资讯
      </el-divider>

      <div class="related-info">
        <el-row :gutter="20">
          <el-col :span="8" v-for="(item, index) in relatedInfo" :key="index">
            <el-card shadow="hover" class="related-card" @click.native="navigateToInfo(item.id)">
              <img :src="item.image" class="related-image" alt="相关资讯图片">
              <div class="related-title">{{ item.title }}</div>
              <div class="related-date">{{ item.date }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <el-divider content-position="left">
        <i class="el-icon-share"></i> 分享资讯
      </el-divider>

      <div class="share-section">
        <el-button type="primary" icon="el-icon-share" circle></el-button>
        <el-button type="success" icon="el-icon-chat-dot-square" circle></el-button>
        <el-button type="warning" icon="el-icon-star-off" circle></el-button>
      </div>

      <el-divider content-position="left">
        <i class="el-icon-chat-line-square"></i> 读者评论
      </el-divider>

      <div class="comment-section">
        <el-input
            type="textarea"
            :rows="3"
            placeholder="请输入您的评论"
            v-model="commentContent"
        >
        </el-input>
        <el-button type="primary" @click="submitComment" class="comment-button">
          提交评论
        </el-button>
      </div>

      <div class="comments-list" v-if="comments.length > 0">
        <el-card v-for="(comment, index) in comments" :key="index" class="comment-card">
          <div class="comment-user">
            <el-avatar :size="40" :src="comment.avatar"></el-avatar>
            <span class="username">{{ comment.username }}</span>
          </div>
          <p class="comment-content">{{ comment.content }}</p>
          <span class="comment-date">{{ comment.date }}</span>
        </el-card>
      </div>
      <el-empty v-else description="暂无评论"></el-empty>
    </el-card>
  </div>
</template>

<script>
import E from 'wangeditor'

export default {
  data() {
    let informationId = this.$route.query.id
    return {
      informationId: informationId,
      informationData: {},
      relatedInfo: [
        { id: 1, title: '乡村振兴新政策', image: require('@/assets/imgs/乡村扶贫政策.jpeg'), date: '2021-05-15' },
        { id: 2, title: '农产品销售渠道拓展', image: require('@/assets/imgs/农产品销售渠道.jpg'), date: '2022-08-14' },
        { id: 3, title: '精准扶贫成果展示', image: require('@/assets/imgs/精准扶贫成果.jpg'), date: '2024-11-13' },
      ],
      commentContent: '',
      comments: [
        { username: '张三', avatar: '/placeholder.svg?height=40&width=40', content: '这个政策真的很有帮助！', date: '2023-05-15 14:30' },
        { username: '李四', avatar: '/placeholder.svg?height=40&width=40', content: '希望能有更多这样的资讯。', date: '2023-05-15 15:45' },
      ]
    }
  },
  mounted() {
    this.loadInformation()
  },
  methods: {
    loadInformation() {
      this.$request.get('/information/selectById/' + this.informationId).then(res => {
        if (res.code === '200') {
          this.informationData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    navigateToInfo(id) {
      // 使用 vue-router 进行页面跳转，保持在应用内部
      this.$router.push({
        path: '/information-detail',
        query: {
          id: id
        }
      })
    },
    submitComment() {
      if (this.commentContent.trim() === '') {
        this.$message.warning('请输入评论内容')
        return
      }
      // 这里应该调用后端 API 提交评论

      this.$message.success('评论提交成功')
      this.comments.unshift({
        username: '当前用户',
        avatar: '/placeholder.svg?height=40&width=40',
        content: this.commentContent,
        date: new Date().toLocaleString()
      })
      this.commentContent = ''
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/theme/front_css/information.css";
</style>