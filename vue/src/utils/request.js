import axios from 'axios'
import router from "@/router";

// 创建可一个新的axios对象
const request = axios.create({
    baseURL: process.env.VUE_APP_BASEURL, // 后端的接口地址  ip:port
    timeout: 30000 // 30s请求超时
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8'; // 设置请求头格式

    // 获取缓存的用户信息，优先检查管理员信息
    let user = JSON.parse(localStorage.getItem("xm-admin") || '{}')
    if (!user.token) {
        user = JSON.parse(localStorage.getItem("xm-user") || '{}')
    }

    if (user.token) {
        config.headers['token'] = user.token // 设置请求头
    }

    return config
}, error => {
    console.error('request error: ' + error) // for debug
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;

        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if (res.code === '401') {
            // 清除所有本地存储的用户信息
            localStorage.removeItem('xm-user')
            localStorage.removeItem('xm-admin')
            router.push('/login')
        }
        return res;
    },
    error => {
        console.error('response error: ' + error) // for debug
        return Promise.reject(error)
    }
)

export default request