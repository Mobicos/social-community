# API 接口规范

本文档定义前后端 API 接口规范，确保前后端开发一致性。

## 通用规范

### 基础路径

```
开发环境: http://localhost:8089/api
生产环境: /api
```

### 请求格式

```http
Content-Type: application/json
token: <access_token>
refreshToken: <refresh_token>
```

### 响应格式

```json
{
  "code": "0",
  "msg": "成功",
  "data": { ... }
}
```

### 状态码

| 状态码 | 说明 |
|--------|------|
| 0 | 成功 |
| 1 | 失败 |
| 500 | 服务器错误 |
| 555 | Token 过期 |

---

## 用户认证

### 获取 RSA 公钥

```http
GET /api/users/rsa-public-key
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQ..."
}
```

### 用户注册

```http
POST /api/users
Content-Type: application/json

{
  "phone": "13800138000",
  "email": "test@example.com",
  "password": "<RSA加密后的密码>"
}
```

**响应**

```json
{
  "code": "0",
  "msg": "用户注册成功",
  "data": null
}
```

### 用户登录（单 Token）

```http
POST /api/users/login
Content-Type: application/json

{
  "phone": "13800138000",
  "password": "<RSA加密后的密码>"
}
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": "eyJraWQiOiIxIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ..."
}
```

### 用户登录（双 Token）

```http
POST /api/users/login-dts
Content-Type: application/json

{
  "phone": "13800138000",
  "password": "<RSA加密后的密码>"
}
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": {
    "accessToken": "eyJraWQiOiIxIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ...",
    "refreshToken": "eyJraWQiOiIxIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ..."
  }
}
```

### 获取当前用户

```http
GET /api/users
token: <access_token>
refreshToken: <refresh_token>
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": {
    "id": 1,
    "phone": "13800138000",
    "email": "test@example.com",
    "status": 1,
    "createTime": "2024-01-15T12:00:00",
    "updateTime": "2024-01-15T12:00:00",
    "lastLoginTime": "2024-01-15T12:00:00",
    "userInfo": {
      "id": 1,
      "userId": 1,
      "nick": "码上阅读",
      "avatar": null,
      "sign": null,
      "gender": 0,
      "birth": "2024-01-15T00:00:00",
      "createTime": "2024-01-15T12:00:00",
      "updateTime": "2024-01-15T12:00:00"
    }
  }
}
```

### 退出登录

```http
DELETE /api/users/logout
token: <access_token>
refreshToken: <refresh_token>
```

### 刷新访问令牌

```http
POST /api/users/refresh-access-token
refreshToken: <refresh_token>
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": "<新的access_token>"
}
```

---

## 用户动态

### 发布动态

```http
POST /api/userMoment
token: <access_token>
refreshToken: <refresh_token>
Content-Type: application/json

{
  "type": 2,
  "contentId": 123456
}
```

**字段说明：**
- `type`: 动态类型（0-视频，1-直播，2-专栏动态）
- `contentId`: 内容详情ID

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": null
}
```

### 获取动态列表

```http
GET /api/userMoment/upMoments
token: <access_token>
refreshToken: <refresh_token>
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "type": 2,
      "contentId": 123456,
      "createTime": "2024-01-15T12:00:00",
      "updateTime": "2024-01-15T12:00:00"
    }
  ]
}
```

### 点赞动态

```http
POST /api/userMoment/{id}/like
token: <access_token>
refreshToken: <refresh_token>
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": "点赞成功"
}
```

### 评论动态

```http
POST /api/userMoment/{id}/comment
token: <access_token>
refreshToken: <refresh_token>
Content-Type: application/json

{
  "content": "真不错！"
}
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": "评论成功"
}
```

---

## 用户关注

### 关注用户

```http
POST /api/userFollowing
token: <access_token>
refreshToken: <refresh_token>
Content-Type: application/json

{
  "followingId": 123
}
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": null
}
```

### 取消关注

```http
DELETE /api/userFollowing/{followingId}
token: <access_token>
refreshToken: <refresh_token>
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": "取消关注成功"
}
```

### 获取关注列表

```http
GET /api/userFollowing/following
token: <access_token>
refreshToken: <refresh_token>
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "followingId": 2,
      "groupId": 3,
      "createTime": "2024-01-15T12:00:00",
      "updateTime": "2024-01-15T12:00:00",
      "userInfo": {
        "id": 2,
        "userId": 2,
        "nick": "用户昵称",
        "avatar": null,
        "sign": null,
        "gender": 0,
        "followed": true
      }
    }
  ]
}
```

### 获取粉丝列表

```http
GET /api/userFollowing/followers
token: <access_token>
refreshToken: <refresh_token>
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": [
    {
      "id": 1,
      "userId": 2,
      "followingId": 1,
      "groupId": 3,
      "createTime": "2024-01-15T12:00:00",
      "updateTime": "2024-01-15T12:00:00",
      "userInfo": {
        "id": 2,
        "userId": 2,
        "nick": "用户昵称",
        "avatar": null,
        "sign": null,
        "gender": 0,
        "followed": false
      }
    }
  ]
}
```

### 获取推荐用户

```http
GET /api/userFollowing/recommend
token: <access_token>
refreshToken: <refresh_token>
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": []
}
```

---

## 搜索功能

### 搜索书籍（按作者）

```http
GET /api/search/by-author?author=作者名
token: <access_token>
refreshToken: <refresh_token>
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": []
}
```

### 搜索视频

```http
GET /api/search/find-videos?keyword=关键词&pubtime_begin_s=0&pubtime_end_s=1735689600&order=desc&duration=0&limit=10&offset=0
token: <access_token>
refreshToken: <refresh_token>
```

**响应**

```json
{
  "code": "0",
  "msg": "成功",
  "data": []
}
```

---

## 文件上传

### 上传文件

```http
POST /api/file/upload
token: <access_token>
refreshToken: <refresh_token>
Content-Type: multipart/form-data

file: <binary>
```

**响应**

```text
M00/00/00/wKgAb2fhXnSEUIPxAAAAALr17vc529.pdf
```

---

## TypeScript 类型定义

### 前端类型

```typescript
// types/user.ts

/** API 响应通用类型 */
export interface ApiResponse<T = unknown> {
  code: string;  // "0" 表示成功
  msg: string;
  data: T;
}

/** 用户基本信息 */
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

/** 用户实体 */
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

/** 登录请求 */
export interface LoginRequest {
  phone?: string;
  email?: string;
  password: string;
}

/** 注册请求 */
export interface RegisterRequest {
  phone?: string;
  email?: string;
  password: string;
}

/** 双 Token 登录响应 */
export interface DtsLoginResponse {
  accessToken: string;
  refreshToken: string;
}

/** 用户动态 */
export interface UserMoment {
  id: number;
  userId: number;
  type: number; // 0-视频，1-直播，2-专栏动态
  contentId: number;
  createTime: string;
  updateTime: string;
}

/** 用户关注 */
export interface UserFollowing {
  id: number;
  userId: number;
  followingId: number;
  groupId: number;
  createTime: string;
  updateTime?: string;
  userInfo?: UserInfo;
}
```

---

## 前端 API 封装示例

```typescript
// api/user.ts
import { http } from '@/utils/request';
import type { ApiResponse, User, LoginRequest, DtsLoginResponse } from '@/types';

// 双 Token 登录
export function loginDts(data: LoginRequest) {
  return http.post<ApiResponse<DtsLoginResponse>>('/users/login-dts', data);
}

// 获取当前用户信息
export function getCurrentUser() {
  return http.get<ApiResponse<User>>('/users');
}

// 关注用户
export function followUser(data: { followingId: number }) {
  return http.post<ApiResponse<string>>('/userFollowing', data);
}

// 取消关注
export function unfollowUser(followingId: number) {
  return http.delete<ApiResponse<string>>(`/userFollowing/${followingId}`);
}
```

---

## 认证机制说明

### 双 Token 认证流程

1. **登录**：调用 `/api/users/login-dts` 获取 `accessToken` 和 `refreshToken`
2. **请求接口**：在请求头中携带 `token` 和 `refreshToken`
3. **Token 过期**：当 `accessToken` 过期时，使用 `refreshToken` 调用 `/api/users/refresh-access-token` 获取新的 `accessToken`
4. **退出登录**：调用 `/api/users/logout` 清除服务端的 `refreshToken`

### 前端请求拦截器示例

```typescript
// utils/request.ts
request.interceptors.request.use((config) => {
  let token = localStorage.getItem('token');
  let refreshToken = localStorage.getItem('refreshToken');

  // 如果直接获取不到，尝试从 zustand storage 获取
  if (!token) {
    const userStorage = localStorage.getItem('user-storage');
    if (userStorage) {
      const parsed = JSON.parse(userStorage);
      token = parsed?.state?.token || null;
      refreshToken = parsed?.state?.refreshToken || null;
    }
  }

  if (token) {
    config.headers.token = token;
  }
  if (refreshToken) {
    config.headers.refreshToken = refreshToken;
  }
  return config;
});
```
