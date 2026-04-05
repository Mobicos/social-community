# 朋友圈社区 - 后端服务

基于 Spring Cloud 微服务架构的社交平台后端服务。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.0 / 3.4.4 | 基础框架 |
| Spring Cloud | 2021.0.3 / 2024.0.1 | 微服务框架 |
| Netflix Eureka | - | 服务注册与发现 |
| OpenFeign | - | 声明式服务调用 |
| MyBatis | 2.2.0 | ORM 框架 |
| MySQL | 8.0 | 关系型数据库 |
| Redis | - | 缓存中间件 |
| RocketMQ | 5.1.0 | 消息队列 |
| Elasticsearch | 8.12.0 | 全文搜索引擎 |
| FastDFS | - | 分布式文件存储 |
| JWT | 3.18.2 | 身份认证 |
| Knife4j | 3.0.3 / 4.5.0 | API 文档 |

## 项目结构

```
├── eureka-server/              # 服务注册中心 (端口: 8761)
│
├── service-consumer/           # 服务消费者 (端口: 8082)
│   ├── src/main/java/org/example/
│   │   ├── ServiceConsumerApplication.java
│   │   ├── ConsumerController.java
│   │   └── UserServiceClient.java
│   └── src/main/resources/application.yml
│
├── my-multi-module-project/    # 主业务服务 (端口: 8089)
│   ├── controller-module/      # 控制层
│   │   └── src/main/java/org/example/
│   │       ├── Application.java
│   │       ├── config/
│   │       ├── controller/
│   │       └── aspect/
│   ├── service-module/         # 服务层
│   │   └── src/main/java/org/example/service/
│   ├── dao-module/             # 数据访问层
│   │   └── src/main/java/org/example/
│   │       ├── dao/
│   │       └── model/
│   └── pom.xml
```

## 服务说明

| 服务 | 端口 | 说明 |
|------|------|------|
| eureka-server | 8761 | 服务注册中心 |
| my-multi-module-project | 8089 | 主业务服务 |
| service-consumer | 8082 | 服务消费者 |

## 快速开始

### 环境要求

- JDK 17+（eureka-server、service-consumer）
- JDK 8+（my-multi-module-project）
- Maven 3.6+

### 启动步骤

1. 启动基础设施（MySQL、Redis 等）
   ```bash
   ./start-infra.sh
   ```

2. 在 IDEA 中依次启动服务
   - `eureka-server`
   - `my-multi-module-project`
   - `service-consumer`

3. 验证服务
   - Eureka Dashboard: http://localhost:8761
   - API 文档: http://localhost:8089/doc.html

## 模块说明

### controller-module（控制层）

| 控制器 | 功能 |
|--------|------|
| `UserController` | 用户管理 |
| `UserAuthController` | 用户认证（登录/注册） |
| `UserMomentsController` | 用户动态（朋友圈） |
| `UserFollowingController` | 用户关注 |
| `SearchController` | 搜索功能 |
| `FastDfsTestController` | 文件上传 |

### service-module（服务层）

| 服务 | 功能 |
|------|------|
| `UserService` | 用户服务 |
| `UserAuthService` | 认证服务 |
| `UserTokenService` | Token 管理 |
| `UserMomentsService` | 动态服务 |
| `ElasticsearchSearchService` | 搜索服务 |

### dao-module（数据访问层）

| DAO | 功能 |
|-----|------|
| `UserDao` | 用户数据访问 |
| `UserMomentsDao` | 动态数据访问 |
| `UserFollowingDao` | 关注数据访问 |
| `FileInfoDao` | 文件信息访问 |

## 中间件集成

### Redis 缓存

```java
@Autowired
private StringRedisTemplate redisTemplate;

// 存储数据
redisTemplate.opsForValue().set("key", "value");

// 获取数据
String value = redisTemplate.opsForValue().get("key");
```

### RocketMQ 消息队列

```java
@Autowired
private RocketMQTemplate rocketMQTemplate;

// 发送消息
rocketMQTemplate.convertAndSend("topic", message);

// 消费消息
@RocketMQMessageListener(topic = "topic", consumerGroup = "group")
public class Consumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        // 处理消息
    }
}
```

### Elasticsearch 搜索

```java
@Autowired
private ElasticsearchClient elasticsearchClient;

// 搜索文档
SearchResponse<Book> response = elasticsearchClient.search(s -> s
    .index("books")
    .query(q -> q.match(m -> m.field("title").query("关键词"))),
    Book.class
);
```

### FastDFS 文件存储

```java
@Autowired
private FastFileStorageClient fastFileStorageClient;

// 上传文件
StorePath storePath = fastFileStorageClient.uploadFile(
    inputStream, fileSize, fileExtension, null
);

// 访问URL
String url = "http://localhost:18888/" + storePath.getFullPath();
```

## 配置说明

### 环境变量

```bash
# 数据库
DB_HOST=localhost
DB_PORT=13306
DB_NAME=mydb_dev
DB_USERNAME=root
DB_PASSWORD=root123456

# Redis
REDIS_HOST=localhost
REDIS_PORT=16379
REDIS_PASSWORD=redis123456

# RocketMQ
ROCKETMQ_NAMESERVER=localhost:19876

# Eureka
EUREKA_URL=http://localhost:8761/eureka/

# FastDFS
FASTDFS_TRACKER=localhost:22122

# Elasticsearch
ES_URL=localhost:19200
```

## 相关链接

- [Spring Boot 文档](https://spring.io/projects/spring-boot)
- [Spring Cloud 文档](https://spring.io/projects/spring-cloud)
- [RocketMQ 文档](https://rocketmq.apache.org/)
- [Elasticsearch 文档](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)
