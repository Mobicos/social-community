# 部署指南

本文档介绍如何在开发环境和生产环境中部署朋友圈社区项目。

## 目录

- [开发环境部署](#开发环境部署)
- [生产环境部署](#生产环境部署)
- [Docker 部署](#docker-部署)
- [常见问题](#常见问题)

---

## 开发环境部署

### 环境要求

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 8+ / 17+ | eureka-server 和 service-consumer 需要 JDK 17+ |
| Node.js | 18+ | 前端开发 |
| pnpm | 8+ | 包管理器 |
| Docker | 最新版 | 运行中间件 |
| Maven | 3.6+ | Java 构建 |

### 1. 克隆项目

```bash
git clone <repository-url>
cd Eureka+Feign
```

### 2. 启动中间件

```bash
# 启动全部中间件
./start-infra.sh

# 或仅启动 MySQL + Redis
docker-compose -f docker-compose.minimal.yml up -d
```

验证中间件状态：

```bash
docker-compose ps
```

### 3. 配置数据库

```bash
# 连接 MySQL
mysql -h localhost -P 13306 -u root -p
# 密码: root123456

# 创建数据库
CREATE DATABASE mydb_dev;

# 导入初始化脚本（如有）
source init-db/init.sql;
```

### 4. 启动后端服务

在 IDEA 中依次启动：

1. **eureka-server** - 服务注册中心
2. **my-multi-module-project** - 主业务服务
3. **service-consumer** - 服务消费者

### 5. 启动前端

```bash
cd frontend
pnpm install
pnpm dev
```

### 6. 验证部署

| 服务 | 地址 | 预期结果 |
|------|------|----------|
| 前端 | http://localhost:3000 | 显示登录页面 |
| Eureka | http://localhost:8761 | 显示服务列表 |
| API 文档 | http://localhost:8089/doc.html | 显示接口列表 |

---

## 生产环境部署

### 服务器要求

| 配置 | 最低要求 | 推荐配置 |
|------|----------|----------|
| CPU | 2核 | 4核+ |
| 内存 | 4GB | 8GB+ |
| 磁盘 | 50GB | 100GB+ SSD |
| 系统 | CentOS 7+ / Ubuntu 20+ | - |

### 1. 准备环境

```bash
# 安装 JDK 17
sudo yum install java-17-openjdk java-17-openjdk-devel

# 安装 Docker
curl -fsSL https://get.docker.com | sh
sudo systemctl start docker

# 安装 Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### 2. 构建后端服务

```bash
# 构建所有模块
mvn clean package -DskipTests

# 生成的 JAR 文件位置
# eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
# service-consumer/target/service-consumer-0.0.1-SNAPSHOT.jar
# my-multi-module-project/controller-module/target/controller-module-1.0-SNAPSHOT.jar
```

### 3. 构建前端

```bash
cd frontend
pnpm install
pnpm build

# 生成的静态文件在 dist/ 目录
```

### 4. 配置 Nginx

```nginx
# /etc/nginx/conf.d/social.conf

# 前端静态资源
server {
    listen 80;
    server_name your-domain.com;

    root /var/www/frontend/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    # API 代理
    location /api {
        proxy_pass http://127.0.0.1:8089;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### 5. 启动后端服务

```bash
# 创建 systemd 服务
sudo cat > /etc/systemd/system/social.service << EOF
[Unit]
Description=Social Community Backend
After=network.target

[Service]
Type=simple
User=app
WorkingDirectory=/opt/social
ExecStart=/usr/bin/java -jar controller-module.jar
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF

# 启动服务
sudo systemctl daemon-reload
sudo systemctl enable social
sudo systemctl start social
```

### 6. 生产环境变量

```bash
# /opt/social/application-prod.yml

spring:
  datasource:
    url: jdbc:mysql://prod-db:3306/social_prod
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  redis:
    host: ${REDIS_HOST}
    password: ${REDIS_PASSWORD}

eureka:
  client:
    service-url:
      defaultZone: http://eureka-server:8761/eureka/
```

---

## Docker 部署

### 单机部署

```bash
# 构建镜像
docker build -t social-frontend ./frontend
docker build -t social-backend ./my-multi-module-project/controller-module

# 使用 Docker Compose
docker-compose -f docker-compose.prod.yml up -d
```

### docker-compose.prod.yml 示例

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
      MYSQL_DATABASE: social_prod
    volumes:
      - mysql_data:/var/lib/mysql
    ports:
      - "3306:3306"

  redis:
    image: redis:7-alpine
    command: redis-server --requirepass ${REDIS_PASSWORD}
    volumes:
      - redis_data:/data
    ports:
      - "6379:6379"

  backend:
    image: social-backend
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_HOST: mysql
      DB_PASSWORD: ${DB_PASSWORD}
      REDIS_HOST: redis
      REDIS_PASSWORD: ${REDIS_PASSWORD}
    depends_on:
      - mysql
      - redis
    ports:
      - "8089:8089"

  frontend:
    image: social-frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql_data:
  redis_data:
```

---

## 常见问题

### 1. 端口被占用

```bash
# 查看端口占用
lsof -i :8761

# 终止进程
kill -9 <PID>
```

### 2. MySQL 连接失败

```bash
# 检查 MySQL 是否运行
docker-compose ps mysql

# 查看 MySQL 日志
docker-compose logs mysql

# 进入 MySQL 容器
docker-compose exec mysql bash
mysql -u root -p
```

### 3. Eureka 服务未注册

- 检查 eureka-server 是否启动
- 检查 application.yml 中的 eureka.client.service-url 配置
- 检查网络连通性

### 4. 前端代理失败

- 检查 vite.config.ts 中的 proxy 配置
- 确认后端服务已启动
- 检查浏览器控制台错误信息

### 5. 内存不足

```bash
# 调整 JVM 参数
java -Xms512m -Xmx1024m -jar app.jar
```

---

## 监控与日志

### 查看日志

```bash
# Docker 日志
docker-compose logs -f backend

# 应用日志
tail -f /var/log/social/application.log
```

### 健康检查

```bash
# 后端健康检查
curl http://localhost:8089/actuator/health

# Eureka 状态
curl http://localhost:8761/eureka/apps
```

---

## 备份与恢复

### 数据库备份

```bash
# 备份
docker-compose exec mysql mysqldump -u root -p social_prod > backup.sql

# 恢复
docker-compose exec -T mysql mysql -u root -p social_prod < backup.sql
```

### Redis 备份

```bash
# 触发 RDB 快照
docker-compose exec redis redis-cli -a ${REDIS_PASSWORD} BGSAVE

# 复制备份文件
docker cp redis:/data/dump.rdb ./redis_backup.rdb
```
