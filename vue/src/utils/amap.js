// 高德地图配置工具函数

// 使用Base64编码对安全密钥进行简单加密
// 这种方式可以防止密钥被直接看到，但不是完全安全的加密方式
// 在生产环境中建议使用更安全的方式存储和传输密钥
const encryptedSecurityCode = 'ODZmNmI1ZjcwZjZlZmI4NjRiMjVhYjQ4MTlmNmVhMzI=';

// 解密函数
// 使用浏览器内置的atob函数将Base64编码的字符串解码为原始字符串
// atob: ASCII to Binary，用于解码Base64编码的字符串
// 如果解码失败，将返回空字符串并在控制台输出错误信息
function decryptSecurityCode() {
  try {
    return atob(encryptedSecurityCode);
  } catch (error) {
    console.error('解密高德地图安全密钥失败:', error);
    return '';
  }
}

// 初始化高德地图配置
// 在使用高德地图JavaScript API之前，需要先设置安全密钥
// window._AMapSecurityConfig对象是高德地图要求的全局配置对象
// securityJsCode属性用于设置解密后的安全密钥
export function initAMapConfig() {
  window._AMapSecurityConfig = {
    securityJsCode: decryptSecurityCode(),
  };
}