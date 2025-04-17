import axios from 'axios';

// 创建axios实例
const instance = axios.create({
  baseURL: 'http://localhost:8080/api', // 确保基础URL与后端匹配
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
instance.interceptors.request.use(
  config => {
    console.log('发送请求:', config);
    
    // 从localStorage获取token
    const token = localStorage.getItem('accessToken');
    
    // 如果有token则添加到请求头
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    
    return config;
  },
  error => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
instance.interceptors.response.use(
  response => {
    console.log('API响应原始数据:', response);
    
    // 直接返回响应数据
    return response.data;
  },
  error => {
    console.error('API响应错误:', error);
    
    if (error.response) {
      // 服务器返回了错误状态码
      console.error('错误状态码:', error.response.status);
      console.error('错误数据:', error.response.data);
      
      switch (error.response.status) {
        case 401: // 未授权
          // 清除token并跳转到登录页
          localStorage.removeItem('accessToken');
          window.location.href = '/login';
          break;
          
        case 403: // 禁止访问
          console.error('没有权限访问此资源');
          break;
          
        case 404: // 资源不存在
          console.error('请求的资源不存在');
          break;
          
        case 500: // 服务器错误
          console.error('服务器错误');
          break;
          
        default:
          console.error(`请求错误: ${error.response.status}`);
      }
      
      // 返回错误响应中的数据
      return Promise.reject(error);
    } else if (error.request) {
      // 请求已发送但没有收到响应
      console.error('未收到服务器响应，请检查网络连接');
      return Promise.reject({ msg: '网络连接错误，请检查您的网络' });
    } else {
      // 请求配置错误
      console.error('请求配置错误', error.message);
      return Promise.reject({ msg: '请求配置错误' });
    }
  }
);

export default instance;