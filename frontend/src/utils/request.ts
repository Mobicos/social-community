import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios';
import { message } from 'antd';

// 创建 axios 实例
const request: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 获取存储的 token
function getStoredTokens(): { token: string | null; refreshToken: string | null } {
  let token = localStorage.getItem('token');
  let refreshToken = localStorage.getItem('refreshToken');

  // 如果直接获取不到，尝试从 zustand storage 获取
  if (!token) {
    const userStorage = localStorage.getItem('user-storage');
    if (userStorage) {
      try {
        const parsed = JSON.parse(userStorage);
        token = parsed?.state?.token || null;
        refreshToken = parsed?.state?.refreshToken || null;
      } catch {
        // ignore parse error
      }
    }
  }

  return { token, refreshToken };
}

// 保存 token
function saveTokens(token: string, refreshToken: string) {
  localStorage.setItem('token', token);
  localStorage.setItem('refreshToken', refreshToken);

  // 同步更新 zustand storage
  const userStorage = localStorage.getItem('user-storage');
  if (userStorage) {
    try {
      const parsed = JSON.parse(userStorage);
      parsed.state = parsed.state || {};
      parsed.state.token = token;
      parsed.state.refreshToken = refreshToken;
      localStorage.setItem('user-storage', JSON.stringify(parsed));
    } catch {
      // ignore
    }
  }
}

// Token 刷新状态
let isRefreshing = false;
let refreshSubscribers: ((token: string) => void)[] = [];

// 通知所有订阅者
function onTokenRefreshed(token: string) {
  refreshSubscribers.forEach((callback) => callback(token));
  refreshSubscribers = [];
}

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    const { token, refreshToken } = getStoredTokens();

    if (token) {
      config.headers.token = token;
    }
    if (refreshToken) {
      config.headers.refreshToken = refreshToken;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response;

    // 如果返回的是文件流，直接返回
    if (response.config.responseType === 'blob') {
      return response;
    }

    // 业务逻辑错误处理 - 后端使用 code: "0" 表示成功
    if (data.code && data.code !== '0') {
      // Token 过期，尝试刷新
      if (data.msg === 'Token过期！' || data.code === '555') {
        const { refreshToken } = getStoredTokens();
        if (refreshToken && !isRefreshing) {
          isRefreshing = true;
          return refreshTokenRequest(refreshToken)
            .then((newToken) => {
              // 重试原请求
              response.config.headers.token = newToken;
              return request(response.config);
            })
            .catch(() => {
              // 刷新失败，跳转登录
              clearTokensAndRedirect();
              return Promise.reject(new Error('Token 刷新失败'));
            })
            .finally(() => {
              isRefreshing = false;
            });
        }
      }

      message.error(data.msg || '请求失败');
      return Promise.reject(new Error(data.msg || '请求失败'));
    }

    return data;
  },
  (error) => {
    // HTTP 错误处理
    if (error.response) {
      const { status } = error.response;
      switch (status) {
        case 401:
          message.error('登录已过期，请重新登录');
          clearTokensAndRedirect();
          break;
        case 403:
          message.error('没有权限访问');
          break;
        case 404:
          message.error('请求的资源不存在');
          break;
        case 500:
          message.error('服务器错误');
          break;
        default:
          message.error(error.message || '网络错误');
      }
    } else {
      message.error('网络连接失败');
    }
    return Promise.reject(error);
  }
);

// 刷新 Token 请求
async function refreshTokenRequest(refreshToken: string): Promise<string> {
  const response = await axios.post('/api/users/refresh-access-token', null, {
    headers: { refreshToken },
  });

  if (response.data.code === '0' && response.data.data) {
    const newToken = response.data.data;
    const { refreshToken: newRefreshToken } = getStoredTokens();
    saveTokens(newToken, newRefreshToken || refreshToken);
    onTokenRefreshed(newToken);
    return newToken;
  }

  throw new Error('Token 刷新失败');
}

// 清除 Token 并跳转登录
function clearTokensAndRedirect() {
  localStorage.removeItem('token');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('user-storage');
  window.location.href = '/login';
}

// 封装请求方法
export const http = {
  get<T = unknown>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return request.get(url, config);
  },

  post<T = unknown>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return request.post(url, data, config);
  },

  put<T = unknown>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return request.put(url, data, config);
  },

  delete<T = unknown>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return request.delete(url, config);
  },

  patch<T = unknown>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return request.patch(url, data, config);
  },
};

export default request;
