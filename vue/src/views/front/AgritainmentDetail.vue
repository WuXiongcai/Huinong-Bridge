<template>
  <div class="main-content">
    <div style="width: 65%; margin: 20px auto">
      <div style="text-align: center">
        <span style="font-size: 22px">{{ agritainmentData.name }}</span>
        <span style="margin-left: 50px; color: #666666">发布时间：{{ agritainmentData.time }}</span>
      </div>
      <div v-html="agritainmentData.content" class="w-e-text w-e-text-container" style="margin-top: 30px"></div>
    </div>

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
    let agritainmentId = this.$route.query.id
    return {
      agritainmentId: agritainmentId,
      agritainmentData: {}
    }
  },
  mounted() {
    this.loadInformation()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadInformation() {
      this.$request.get('/agritainment/selectById/' + this.agritainmentId).then(res => {
        if (res.code === '200') {
          this.agritainmentData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style>
p, ol {
  color: #3a3c40 !important;
  line-height: 30px !important;
}

.backtop-inner {
  background-color: #19a14b;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}
</style>
