# 朋友圈社区

一个基于 Spring Cloud 微服务架构的社交平台，支持用户动态发布、好友关注、全文搜索等功能。

## 项目概览

```
┌─────────────────────────────────────────────────────────────────┐
│                         前端 (React)                             │
│                     http://localhost:3000                        │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Eureka Server :8761                         │
│                       服务注册与发现                              │
└─────────────────────────────────────────────────────────────────┘
          │                                       │
          ▼                                       ▼
┌───────────────────────┐           ┌───────────────────────┐
│  my-multi-module      │           │  service-consumer     │
│  主业务服务 :8089      │◄─────────►│  服务消费者 :8082      │
│  (用户/动态/搜索)      │   Feign   │  (Feign 调用示例)      │
└───────────────────────┘           └───────────────────────┘
          │
          ▼
┌─────────────────────────────────────────────────────────────────┐
│                        中间件层                                   │
│  ┌─────────┐ ┌─────────┐ ┌──────────┐ ┌──────────┐ ┌─────────┐  │
│  │ MySQL   │ │ Redis   │ │ RocketMQ │ │   ES     │ │ FastDFS │  │
│  │ :13306  │ │ :16379  │ │ :19876   │ │ :19200   │ │ :22122  │  │
│  └─────────┘ └─────────┘ └──────────┘ └──────────┘ └─────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## 技术栈

### 后端

| 技术 | 说明 |
|------|------|
| Spring Boot 2.7/3.4 | 基础框架 |
| Spring Cloud 2021/2024 | 微服务框架 |
| Netflix Eureka | 服务注册与发现 |
| OpenFeign | 声明式服务调用 |
| MySQL + MyBatis | 数据存储 |
| Redis | 缓存 |
| RocketMQ | 消息队列 |
| Elasticsearch | 全文搜索 |
| FastDFS | 文件存储 |
| JWT | 身份认证 |

### 前端

| 技术 | 说明 |
|------|------|
| React 19 | 前端框架 |
| TypeScript | 类型安全 |
| Vite 8 | 构建工具 |
| Ant Design 6 | UI 组件库 |
| Tailwind CSS 4 | 样式方案 |
| Zustand | 状态管理 |
| TanStack Query | 数据请求 |

## 快速开始

### 环境要求

- JDK 8+ / 17+
- Node.js 18+
- Docker & Docker Compose
- pnpm（推荐）

### 一键启动

```bash
# 1. 启动基础设施
./start-infra.sh

# 2. 启动后端服务（在 IDEA 中运行）
#    - eureka-server
#    - my-multi-module-project
#    - service-consumer

# 3. 启动前端
cd frontend && pnpm install && pnpm dev
```

### 访问地址

| 服务 | 地址 |
|------|------|
| 前端应用 | http://localhost:3000 |
| Eureka 控制台 | http://localhost:8761 |
| 后端 API 文档 | http://localhost:8089/doc.html |
| 消费者 API 文档 | http://localhost:8082/doc.html |

## 项目结构

```
Eureka+Feign/
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/               # API 接口
│   │   ├── components/        # 公共组件
│   │   ├── pages/             # 页面
│   │   ├── stores/            # 状态管理
│   │   └── router/            # 路由配置
│   └── README.md
│
├── backend/                    # 后端项目
│   └── README.md
│
├── eureka-server/             # 服务注册中心
├── service-consumer/          # 服务消费者
├── my-multi-module-project/   # 主业务服务
│   ├── controller-module/
│   ├── service-module/
│   └── dao-module/
│
├── docs/                       # 文档
│   ├── api.md                 # API 规范
│   └── deployment.md          # 部署指南
│
├── docker-compose.yml         # Docker 编排
├── start-infra.sh             # 启动脚本
└── stop-infra.sh              # 停止脚本
```

## 功能模块

| 模块 | 功能 |
|------|------|
| 用户认证 | 注册、登录、JWT Token |
| 用户动态 | 发布动态、点赞、评论 |
| 好友关系 | 关注、取消关注、好友列表 |
| 文件上传 | 图片/视频上传、FastDFS 存储 |
| 全文搜索 | 用户/动态/图片搜索 |

## 文档

- [后端服务文档](backend/README.md)
- [前端项目文档](frontend/README.md)
- [API 接口规范](docs/api.md)
- [部署指南](docs/deployment.md)

## License

MIT
