// API 响应通用类型 - 后端使用 code: "0" 表示成功
export interface ApiResponse<T = unknown> {
  code: string;  // "0" 表示成功
  msg: string;   // 响应消息
  data: T;       // 响应数据
}

// 用户基本信息
export interface UserInfo {
  id: number;
  userId: number;
  nick: string;
  avatar: string;
  sign: string;
  gender: number;
  birth: string;
  createTime: string;
  updateTime: string;
  followed?: boolean;
}

// 用户实体
export interface User {
  id: number;
  phone: string;
  email: string;
  password?: string;
  status: number;
  createTime: string;
  updateTime: string;
  lastLoginTime: string;
  userInfo: UserInfo;
}

// 登录请求
export interface LoginRequest {
  phone?: string;
  email?: string;
  password: string;
}

// 注册请求
export interface RegisterRequest {
  phone?: string;
  email?: string;
  password: string;
}

// 双 Token 登录响应
export interface DtsLoginResponse {
  accessToken: string;
  refreshToken: string;
}

// 用户动态
export interface UserMoment {
  id: number;
  userId: number;
  type: number; // 0-视频，1-直播，2-专栏动态
  contentId: number;
  createTime: string;
  updateTime: string;
}

// 动态发布请求
export interface MomentRequest {
  content: string;
  images?: string[];
  type?: number;
  contentId?: number;
}

// 用户关注
export interface UserFollowing {
  id: number;
  userId: number;
  followingId: number;
  groupId: number;
  createTime: string;
  updateTime?: string;
  userInfo?: UserInfo;
}

// 搜索相关类型
export interface SearchParams {
  keyword: string;
  type?: 'user' | 'moment' | 'video';
  page?: number;
  size?: number;
}

// 文件上传响应
export interface UploadResponse {
  url: string;
  fileId: string;
}
