import { http } from '@/utils/request';
import type { LoginRequest, RegisterRequest, LoginResponse, User, ApiResponse } from '@/types';

// 用户登录
export function login(data: LoginRequest) {
  return http.post<ApiResponse<LoginResponse>>('/user/login', data);
}

// 用户注册
export function register(data: RegisterRequest) {
  return http.post<ApiResponse<User>>('/user/register', data);
}

// 获取用户信息
export function getUserInfo() {
  return http.get<ApiResponse<User>>('/user/info');
}

// 更新用户信息
export function updateUserInfo(data: Partial<User>) {
  return http.put<ApiResponse<User>>('/user/update', data);
}

// 用户登出
export function logout() {
  return http.post<ApiResponse<void>>('/user/logout');
}
