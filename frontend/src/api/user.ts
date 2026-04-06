import { http } from '@/utils/request';
import type {
  User,
  LoginRequest,
  RegisterRequest,
  ApiResponse,
  DtsLoginResponse,
  UserMoment,
  MomentRequest,
} from '@/types';

// ============ 用户相关接口 ============

/**
 * 用户注册
 * POST /api/users
 */
export function register(data: RegisterRequest) {
  return http.post<ApiResponse<string>>('/users', data);
}

/**
 * 用户登录
 * POST /api/users/login
 */
export function login(data: LoginRequest) {
  return http.post<ApiResponse<string>>('/users/login', data);
}

/**
 * 双 Token 登录（推荐）
 * POST /api/users/login-dts
 */
export function loginDts(data: LoginRequest) {
  return http.post<ApiResponse<DtsLoginResponse>>('/users/login-dts', data);
}

/**
 * 获取当前用户信息
 * GET /api/users
 */
export function getCurrentUser() {
  return http.get<ApiResponse<User>>('/users');
}

/**
 * 退出登录
 * DELETE /api/users/logout
 */
export function logout() {
  return http.delete<ApiResponse<string>>('/users/logout');
}

/**
 * 获取 RSA 公钥
 * GET /api/users/rsa-public-key
 */
export function getRsaPublicKey() {
  return http.get<ApiResponse<string>>('/users/rsa-public-key');
}

/**
 * 刷新访问令牌
 * POST /api/users/refresh-access-token
 */
export function refreshAccessToken(refreshToken: string) {
  return http.post<ApiResponse<string>>('/users/refresh-access-token', null, {
    headers: { refreshToken },
  });
}

// ============ 用户动态接口 ============

/**
 * 发布动态
 * POST /api/userMoment
 */
export function addMoment(data: MomentRequest) {
  return http.post<ApiResponse<string>>('/userMoment', data);
}

/**
 * 获取动态列表
 * GET /api/userMoment/upMoments
 */
export function getMoments() {
  return http.get<ApiResponse<UserMoment[]>>('/userMoment/upMoments');
}

/**
 * 点赞动态
 * POST /api/userMoment/{id}/like
 */
export function likeMoment(id: number) {
  return http.post<ApiResponse<string>>(`/userMoment/${id}/like`, null);
}

/**
 * 评论动态
 * POST /api/userMoment/{id}/comment
 */
export function commentMoment(id: number, content: string) {
  return http.post<ApiResponse<string>>(`/userMoment/${id}/comment`, { content });
}

// ============ 用户关注接口 ============

/**
 * 关注用户
 * POST /api/userFollowing
 */
export function followUser(data: { followingId: number }) {
  return http.post<ApiResponse<string>>('/userFollowing', data);
}

/**
 * 取消关注
 * DELETE /api/userFollowing/{id}
 */
export function unfollowUser(followingId: number) {
  return http.delete<ApiResponse<string>>(`/userFollowing/${followingId}`);
}

/**
 * 获取关注列表
 * GET /api/userFollowing/following
 */
export function getFollowingList() {
  return http.get<ApiResponse<UserFollowing[]>>('/userFollowing/following');
}

/**
 * 获取粉丝列表
 * GET /api/userFollowing/followers
 */
export function getFollowersList() {
  return http.get<ApiResponse<UserFollowing[]>>('/userFollowing/followers');
}

/**
 * 获取推荐用户
 * GET /api/userFollowing/recommend
 */
export function getRecommendUsers() {
  return http.get<ApiResponse<User[]>>('/userFollowing/recommend');
}

// ============ 搜索接口 ============

/**
 * 搜索书籍（按作者）
 * GET /api/search/by-author
 */
export function searchBooksByAuthor(author: string) {
  return http.get<ApiResponse<unknown[]>>('/search/by-author', { params: { author } });
}

/**
 * 搜索视频
 * GET /api/search/find-videos
 */
export function searchVideos(params: {
  keyword: string;
  pubtime_begin_s: number;
  pubtime_end_s: number;
  order: string;
  duration: number;
  limit: number;
  offset: number;
}) {
  return http.get<ApiResponse<unknown[]>>('/search/find-videos', { params });
}

// ============ 文件上传接口 ============

/**
 * 上传图片
 * POST /api/file/upload
 */
export function uploadFile(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return http.post<ApiResponse<{ url: string; fileId: string }>>('/file/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}
