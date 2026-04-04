#!/bin/bash

# 本地开发启动脚本

set -e

echo "🚀 Starting development environment..."

# 检查 Docker 是否运行
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# 启动基础设施服务
echo "📦 Starting infrastructure services..."
docker-compose up -d mysql redis rocketmq-namesrv rocketmq-broker elasticsearch fastdfs-tracker fastdfs-storage

# 等待服务就绪
echo "⏳ Waiting for services to be ready..."
sleep 10

# 检查 MySQL
echo "Checking MySQL..."
until docker exec eureka-mysql mysqladmin ping -h localhost -uroot -p${DB_PASSWORD:-root123456} --silent 2>/dev/null; do
    echo "Waiting for MySQL..."
    sleep 2
done
echo "✅ MySQL is ready"

# 检查 Redis
echo "Checking Redis..."
until docker exec eureka-redis redis-cli -a ${REDIS_PASSWORD:-redis123456} ping 2>/dev/null | grep -q PONG; do
    echo "Waiting for Redis..."
    sleep 2
done
echo "✅ Redis is ready"

# 检查 Elasticsearch
echo "Checking Elasticsearch..."
until curl -s http://localhost:9200/_cluster/health > /dev/null 2>&1; do
    echo "Waiting for Elasticsearch..."
    sleep 3
done
echo "✅ Elasticsearch is ready"

echo ""
echo "🎉 All services are ready!"
echo ""
echo "Services:"
echo "  - MySQL:         localhost:3306 (user: root, password: ${DB_PASSWORD:-root123456})"
echo "  - Redis:         localhost:6379 (password: ${REDIS_PASSWORD:-redis123456})"
echo "  - RocketMQ:      localhost:9876"
echo "  - Elasticsearch: localhost:9200"
echo "  - FastDFS:       localhost:22122 (tracker), localhost:8888 (storage)"
echo ""
echo "To start the application:"
echo "  1. cd eureka-server && mvn spring-boot:run"
echo "  2. cd my-multi-module-project/controller-module && mvn spring-boot:run"
echo "  3. cd service-consumer && mvn spring-boot:run (optional)"
