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
Authorization: Bearer <token>  # 需要认证的接口
```

### 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或 Token 过期 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |

---

## 用户认证

### 用户注册

```http
POST /api/user/register
Content-Type: application/json

{
  "username": "test",
  "password": "123456",
  "nickname": "测试用户",
  "email": "test@example.com",
  "phone": "13800138000"
}
```

**响应**

```json
{
  "code": 200,
  "message": "注册成功",
  "data": null
}
```

### 用户登录

```http
POST /api/user/login
Content-Type: application/json

{
  "username": "test",
  "password": "123456"
}
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "test",
      "nickname": "测试用户",
      "avatar": "https://..."
    }
  }
}
```

### 获取当前用户

```http
GET /api/user/current
Authorization: Bearer <token>
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "test",
    "nickname": "测试用户",
    "avatar": "https://...",
    "signature": "这个人很懒，什么都没写",
    "followers": 128,
    "following": 256
  }
}
```

---

## 用户动态

### 发布动态

```http
POST /api/userMoment
Authorization: Bearer <token>
Content-Type: application/json

{
  "content": "今天天气真好！",
  "images": [
    "http://localhost:18888/group1/M00/00/01/xxx.jpg",
    "http://localhost:18888/group1/M00/00/01/yyy.jpg"
  ]
}
```

**响应**

```json
{
  "code": 200,
  "message": "发布成功",
  "data": null
}
```

### 获取动态列表

```http
GET /api/userMoment/upMoments
Authorization: Bearer <token>
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "nickname": "张三",
      "avatar": "https://...",
      "content": "今天天气真好！",
      "images": ["https://..."],
      "likes": 128,
      "comments": 32,
      "createTime": "2024-01-15 12:00:00",
      "isLiked": false
    }
  ]
}
```

### 点赞动态

```http
POST /api/userMoment/{id}/like
Authorization: Bearer <token>
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

### 评论动态

```http
POST /api/userMoment/{id}/comment
Authorization: Bearer <token>
Content-Type: application/json

{
  "content": "真不错！"
}
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 用户关注

### 关注用户

```http
POST /api/userFollowing
Authorization: Bearer <token>
Content-Type: application/json

{
  "followingId": 123
}
```

**响应**

```json
{
  "code": 200,
  "message": "关注成功",
  "data": null
}
```

### 取消关注

```http
DELETE /api/userFollowing/{followingId}
Authorization: Bearer <token>
```

**响应**

```json
{
  "code": 200,
  "message": "取消关注成功",
  "data": null
}
```

### 获取关注列表

```http
GET /api/userFollowing/following
Authorization: Bearer <token>
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 123,
      "username": "lisi",
      "nickname": "李四",
      "avatar": "https://...",
      "signature": "代码改变世界"
    }
  ]
}
```

### 获取粉丝列表

```http
GET /api/userFollowing/followers
Authorization: Bearer <token>
```

---

## 搜索功能

### 搜索用户

```http
GET /api/search/users?keyword=张三
Authorization: Bearer <token>
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "username": "zhangsan",
      "nickname": "张三",
      "avatar": "https://...",
      "signature": "热爱生活"
    }
  ]
}
```

### 搜索动态

```http
GET /api/search/moments?keyword=天气&page=1&size=10
Authorization: Bearer <token>
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "page": 1,
    "size": 10,
    "list": [
      {
        "id": 1,
        "content": "今天天气真好！",
        "userId": 1,
        "nickname": "张三",
        "createTime": "2024-01-15 12:00:00"
      }
    ]
  }
}
```

---

## 文件上传

### 上传图片

```http
POST /api/file/upload
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <binary>
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "url": "http://localhost:18888/group1/M00/00/01/xxx.jpg",
    "fileId": "group1/M00/00/01/xxx.jpg"
  }
}
```

---

## TypeScript 类型定义

### 前端类型

```typescript
// types/api.ts

/** 通用响应 */
export interface ApiResponse<T = unknown> {
  code: number;
  message: string;
  data: T;
}

/** 用户信息 */
export interface User {
  id: number;
  username: string;
  nickname: string;
  avatar?: string;
  signature?: string;
  followers?: number;
  following?: number;
}

/** 登录响应 */
export interface LoginResponse {
  token: string;
  user: User;
}

/** 动态 */
export interface Moment {
  id: number;
  userId: number;
  nickname: string;
  avatar?: string;
  content: string;
  images: string[];
  likes: number;
  comments: number;
  createTime: string;
  isLiked: boolean;
}

/** 分页响应 */
export interface PageResponse<T> {
  total: number;
  page: number;
  size: number;
  list: T[];
}
```

---

## 前端 API 封装示例

```typescript
// api/user.ts
import { http } from '@/utils/request';
import type { ApiResponse, LoginResponse, User } from '@/types';

export function login(data: { username: string; password: string }) {
  return http.post<ApiResponse<LoginResponse>>('/user/login', data);
}

export function getCurrentUser() {
  return http.get<ApiResponse<User>>('/user/current');
}
```

---

## 错误处理

### 前端统一处理

```typescript
// utils/request.ts
axios.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const { code, message } = error.response?.data || {};

    if (code === 401) {
      // Token 过期，跳转登录
      localStorage.removeItem('token');
      window.location.href = '/login';
    }

    return Promise.reject(error);
  }
);
```

### 后端统一异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public JsonResponse<?> handleException(Exception e) {
        return JsonResponse.error(500, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public JsonResponse<?> handleUnauthorized(UnauthorizedException e) {
        return JsonResponse.error(401, "未登录或 Token 过期");
    }
}
```
