const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        port: 8080,
    },
    chainWebpack: config => {
        config.plugin('html')
            .tap(args => {
                args[0].title = "惠农云桥";
                return args;
            })
    }
})