// 用户相关类型
export interface User {
  id: number;
  username: string;
  nickname?: string;
  avatar?: string;
  email?: string;
  phone?: string;
  createTime?: string;
  updateTime?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  nickname?: string;
  email?: string;
  phone?: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

// API 响应通用类型
export interface ApiResponse<T = unknown> {
  code: number;
  message: string;
  data: T;
}
