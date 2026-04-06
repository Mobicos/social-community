# 更新日志

本项目的所有重要变更都将记录在此文件中。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

## [Unreleased]

### 新增
- 前端 React 项目初始化
  - Vue 3 → React 19 技术栈迁移
  - 集成 Ant Design 6、Tailwind CSS 4
  - 集成 Zustand 状态管理
  - 集成 TanStack Query 数据请求
  - 集成 React Router 7 路由
- 后端 API 文档集成
  - my-multi-module-project 集成 Knife4j 3.0.3
  - service-consumer 集成 Knife4j 4.5.0
- 项目文档完善
  - 根目录 README.md 项目总览
  - backend/README.md 后端文档
  - frontend/README.md 前端文档
  - docs/api.md API 接口规范
  - docs/deployment.md 部署指南
  - CHANGELOG.md 版本变更记录
- 后端新增 API 端点
  - `GET /api/userFollowing/following` 获取关注列表
  - `GET /api/userFollowing/followers` 获取粉丝列表
  - `DELETE /api/userFollowing/{followingId}` 取消关注
  - `GET /api/userFollowing/recommend` 获取推荐用户
  - `POST /api/userMoment/{id}/like` 点赞动态
  - `POST /api/userMoment/{id}/comment` 评论动态

### 变更
- 项目名称更改为「朋友圈社区」
- 前端页面调整为社交平台风格
  - 首页：个人数据统计
  - 好友：关注/推荐列表
  - 动态：朋友圈功能
  - 搜索：用户/动态/图片搜索
- 后端服务目录重组至 `backend/` 目录
- 前后端认证机制对接
  - Token 请求头从 `Authorization: Bearer` 改为 `token`
  - 实现双 Token（accessToken + refreshToken）认证
  - 登录接口改用 `/api/users/login-dts`

### 修复
- 修复 Spring Boot 2.7 与 Knife4j 兼容性问题（添加 `spring.mvc.pathmatch.matching-strategy=ant_path_matcher`）
- 修复前端 Token 读取问题（从 zustand storage 读取）
- 修复 Elasticsearch 搜索索引不存在时的优雅降级处理
- 修复 RocketMQ Topic 不存在时的优雅降级处理
- 修复文件上传接口路径（`/file/upload` → `/api/file/upload`）
- 修复 Ant Design 6.x Statistic 组件 `valueStyle` 废弃警告
- 移除动态发布的权限限制（P5 角色黑名单）

---

## [1.0.0] - 2024-01-05

### 新增
- Spring Cloud 微服务架构搭建
  - eureka-server 服务注册中心
  - my-multi-module-project 主业务服务
  - service-consumer 服务消费者
- 基础设施 Docker 编排
  - MySQL 8.0
  - Redis 7
  - RocketMQ 5.1.0
  - Elasticsearch 8.12.0
  - FastDFS
- 用户模块
  - 用户注册/登录
  - JWT Token 认证
- 社交功能
  - 用户动态发布
  - 用户关注
  - 点赞/评论
- 搜索功能
  - Elasticsearch 全文搜索
- 文件存储
  - FastDFS 文件上传

---

[Unreleased]: https://github.com/xxx/social-community/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/xxx/social-community/releases/tag/v1.0.0
