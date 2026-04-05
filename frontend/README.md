# 朋友圈社区 - 前端项目

基于 React 18 + TypeScript + Ant Design 构建的社交平台前端应用。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| React | 19.x | 前端框架 |
| TypeScript | 5.x | 类型安全 |
| Vite | 8.x | 构建工具 |
| Ant Design | 6.x | UI 组件库 |
| Tailwind CSS | 4.x | 原子化 CSS |
| Zustand | 5.x | 状态管理 |
| React Router | 7.x | 路由管理 |
| Axios | 1.x | HTTP 请求 |
| TanStack Query | 5.x | 数据请求缓存 |
| ahooks | 3.x | React Hooks 库 |
| dayjs | 1.x | 日期处理 |

## 目录结构

```
frontend/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API 接口封装
│   │   ├── user.ts        # 用户相关接口
│   │   └── index.ts       # 导出
│   ├── components/        # 公共组件
│   │   ├── ProtectedRoute.tsx  # 路由守卫
│   │   └── index.ts
│   ├── layouts/           # 布局组件
│   │   ├── MainLayout.tsx # 主布局（侧边栏+顶栏）
│   │   └── index.ts
│   ├── pages/             # 页面组件
│   │   ├── Home/          # 首页
│   │   ├── Login/         # 登录/注册
│   │   ├── Users/         # 好友列表
│   │   ├── Moments/       # 动态列表
│   │   ├── Search/        # 搜索
│   │   ├── NotFound/      # 404 页面
│   │   └── index.ts
│   ├── router/            # 路由配置
│   │   └── index.tsx
│   ├── stores/            # 状态管理
│   │   ├── userStore.ts   # 用户状态
│   │   └── index.ts
│   ├── types/             # TypeScript 类型定义
│   │   ├── user.ts        # 用户相关类型
│   │   └── index.ts
│   ├── utils/             # 工具函数
│   │   └── request.ts     # Axios 封装
│   ├── App.tsx            # 根组件
│   ├── main.tsx           # 入口文件
│   └── index.css          # 全局样式
├── .env.development       # 开发环境变量
├── .env.production        # 生产环境变量
├── vite.config.ts         # Vite 配置
├── tsconfig.json          # TypeScript 配置
└── package.json           # 项目依赖
```

## 快速开始

### 环境要求

- Node.js >= 18.x
- pnpm >= 8.x（推荐）

### 安装依赖

```bash
cd frontend
pnpm install
```

### 启动开发服务器

```bash
pnpm dev
```

访问 http://localhost:3000

### 构建生产版本

```bash
pnpm build
```

### 代码检查

```bash
pnpm lint
```

## 功能模块

### 1. 登录/注册

- 用户名密码登录
- 用户注册（支持昵称、邮箱、手机号）
- JWT Token 认证
- 登录状态持久化

### 2. 首页

- 个人数据统计（好友数、获赞数、动态数、关注数）
- 功能引导

### 3. 好友

- 我的关注列表
- 推荐关注
- 关注/取消关注
- 好友搜索
- 私信入口

### 4. 动态（朋友圈）

- 动态列表展示
- 发布动态
- 点赞功能
- 评论功能
- 图片预览

### 5. 搜索

- 全局搜索（用户/动态/图片）
- 分类筛选
- 搜索结果高亮

## API 接口

### 代理配置

开发环境下，API 请求代理到后端服务：

```typescript
// vite.config.ts
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8089',  // 后端服务地址
      changeOrigin: true,
    },
  },
}
```

### 接口规范

```typescript
// 请求
POST /api/user/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}

// 响应
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "xxx",
    "user": { ... }
  }
}
```

### Token 认证

```typescript
// 请求拦截器自动添加 Token
headers: {
  Authorization: 'Bearer xxx'
}
```

## 状态管理

使用 Zustand 进行状态管理，支持持久化存储：

```typescript
// stores/userStore.ts
const useUserStore = create<UserState>()(
  persist(
    (set, get) => ({
      user: null,
      token: null,
      setUser: (user) => set({ user }),
      setToken: (token) => set({ token }),
      logout: () => set({ user: null, token: null }),
    }),
    {
      name: 'user-storage',  // localStorage key
    }
  )
);
```

## 路由配置

```typescript
const router = createBrowserRouter([
  { path: '/login', element: <Login /> },
  {
    path: '/',
    element: (
      <ProtectedRoute>
        <MainLayout />
      </ProtectedRoute>
    ),
    children: [
      { path: 'home', element: <Home /> },
      { path: 'users', element: <Users /> },
      { path: 'moments', element: <Moments /> },
      { path: 'search', element: <Search /> },
    ],
  },
]);
```

## 环境变量

```bash
# .env.development
VITE_API_BASE_URL=/api
VITE_APP_TITLE=朋友圈社区

# .env.production
VITE_API_BASE_URL=/api
VITE_APP_TITLE=朋友圈社区
```

## 代码规范

### 组件命名

- 页面组件：PascalCase（如 `Users.tsx`）
- 公共组件：PascalCase（如 `ProtectedRoute.tsx`）
- 导出文件：`index.ts`

### 类型定义

```typescript
// types/user.ts
export interface User {
  id: number;
  username: string;
  nickname?: string;
  // ...
}

export interface ApiResponse<T = unknown> {
  code: number;
  message: string;
  data: T;
}
```

### API 封装

```typescript
// api/user.ts
export function login(data: LoginRequest) {
  return http.post<ApiResponse<LoginResponse>>('/user/login', data);
}
```

## 浏览器支持

- Chrome >= 90
- Firefox >= 88
- Safari >= 14
- Edge >= 90

## 相关链接

- [React 文档](https://react.dev/)
- [Ant Design 文档](https://ant.design/)
- [Vite 文档](https://vitejs.dev/)
- [Zustand 文档](https://zustand-demo.pmnd.rs/)
- [TanStack Query 文档](https://tanstack.com/query/latest)

## License

MIT
