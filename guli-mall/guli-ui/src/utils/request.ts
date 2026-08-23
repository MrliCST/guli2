import axios from 'axios'
import type { AxiosResponse, InternalAxiosRequestConfig, AxiosError } from 'axios'

const SUCCESS_CODE = 200

// 创建 axios 实例
const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 50000,
  transitional: {
    clarifyTimeoutError: true
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8'
    // FormData 数据去掉 Content-Type，让浏览器自动设置 boundary
    if (config.data instanceof FormData) {
      delete config.headers['Content-Type']
    }
    return config
  },
  (error: AxiosError) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 二进制数据直接返回
    if (response.request.responseType === 'blob' || response.request.responseType === 'arraybuffer') {
      return response.data
    }
    const { code, msg } = response.data
    // 响应体未携带业务状态码，直接返回
    if (code === undefined) {
      return response.data
    }
    if (code === SUCCESS_CODE) {
      return response.data.data
    }
    console.error(msg || '请求失败')
    return Promise.reject(new Error(msg || 'error'))
  },
  (error: AxiosError) => {
    let { message } = error
    if (message === 'Network Error') {
      message = '后端接口连接异常'
    } else if (message.includes('timeout')) {
      message = '系统接口请求超时'
    } else if (message.includes('Request failed with status code')) {
      message = '系统接口' + message.substr(message.length - 3) + '异常'
    }
    console.error(message)
    return Promise.reject(error)
  }
)

export default service
