<template>
  <div class="main-content">
    <el-card class="policy-card">
      <div class="policy-header">
        <h1 class="policy-title">{{ policyData.name }}</h1>
        <div class="policy-meta">
          <el-tag size="small" type="info">发布时间：{{ policyData.time }}</el-tag>
          <el-tag size="small" type="success">点赞： {{ policyData.num }}</el-tag>
          <el-button
              :type="isPraise ? 'primary' : 'default'"
              size="small"
              icon="el-icon-thumb"
              @click="praise"
          >
            {{ isPraise ? '已点赞' : '点赞' }}
          </el-button>
        </div>
      </div>

      <el-divider content-position="left">政策内容</el-divider>

      <div v-html="policyData.content" class="policy-content w-e-text w-e-text-container"></div>

      <el-divider content-position="left">相关文件</el-divider>

      <div class="policy-files">
        <el-button type="primary" icon="el-icon-document" size="small">下载政策文件</el-button>
        <el-button type="info" icon="el-icon-document-copy" size="small">查看相关文件</el-button>
      </div>

      <el-divider content-position="left">评论区</el-divider>

      <div class="comment-section">
        <h3>欢迎发表您宝贵的意见</h3>
        <el-input
            type="textarea"
            :rows="4"
            placeholder="请输入您的评论"
            v-model="content"
            maxlength="500"
            show-word-limit
        ></el-input>
        <div class="comment-submit">
          <el-button type="primary" @click="submit(content, 0)" icon="el-icon-s-comment">提交评论</el-button>
        </div>
      </div>

      <div class="comments-list">
        <el-timeline>
          <el-timeline-item
              v-for="item in commentData"
              :key="item.id"
              :timestamp="item.time"
              placement="top"
          >
            <el-card class="comment-card">
              <div class="comment-user">
                <el-avatar :size="40" :src="item.avatar"></el-avatar>
                <span class="username">{{ item.userName }}</span>
              </div>
              <p class="comment-content">{{ item.content }}</p>

              <div v-if="item.children && item.children.length > 0" class="comment-replies">
                <el-collapse>
                  <el-collapse-item :title="`查看 ${item.children.length} 条回复`">
                    <div v-for="child in item.children" :key="child.id" class="reply-item">
                      <div class="comment-user">
                        <el-avatar :size="30" :src="child.avatar"></el-avatar>
                        <span class="username">{{ child.userName }}</span>
                      </div>
                      <p class="comment-content">{{ child.content }}</p>
                      <span class="reply-time">{{ child.time }}</span>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </div>

              <el-input
                  v-model="item.tmp"
                  placeholder="回复评论"
                  class="reply-input"
              >
                <el-button slot="append" type="primary" @click="submit(item.tmp, item.id)">回复</el-button>
              </el-input>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-card>
    <el-backtop :bottom="100">
      <div class="backtop-inner">
        <i class="el-icon-caret-top"></i>
      </div>
    </el-backtop>
  </div>
</template>

<script>
import E from 'wangeditor'

export default {
  data() {
    let policyId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      policyId: policyId,
      policyData: {},
      isPraise: false,
      content: null,
      commentData: [],
    }
  },
  mounted() {
    this.loadInformation()
    this.checkPraise()
    this.loadComment()
  },
  methods: {
    loadComment() {
      this.$request.get('/comment/selectByPolicyId?id=' + this.policyId).then(res => {
        if (res.code === '200') {
          this.commentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    submit(content, parentId) {
      if (!content) {
        this.$message.warning('请输入内容')
        return
      }
      let data = {
        userId: this.user.id,
        policyId: this.policyId,
        parentId: parentId,
        content: content
      }
      this.$request.post('/comment/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('评论成功')
          this.content = null
          this.loadComment()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    checkPraise() {
      this.$request.get('/praise/selectAll', {
        params: {
          userId: this.user.id,
          policyId: this.policyId
        }
      }).then(res => {
        if (res.code === '200') {
          this.isPraise = res.data.length > 0
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    praise() {
      let data = {
        userId: this.user.id,
        policyId: this.policyId
      }
      this.$request.post('/praise/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          this.loadInformation()
          this.checkPraise()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadInformation() {
      this.$request.get('/policy/selectById/' + this.policyId).then(res => {
        if (res.code === '200') {
          this.policyData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/theme/front_css/policyDetail.css";
</style>