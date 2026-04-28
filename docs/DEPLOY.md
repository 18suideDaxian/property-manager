# 物业管理系统 - 部署文档

## 目录

- [环境要求](#环境要求)
- [Docker 部署（推荐）](#docker-部署推荐)
- [手动部署](#手动部署)
- [生产环境注意事项](#生产环境注意事项)
- [常见问题](#常见问题)

---

## 环境要求

### Docker 部署
- Docker 20.10+
- Docker Compose 2.0+
- 至少 2GB 可用内存

### 手动部署
- JDK 17+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0+
- Redis 7.0+（可选，用于缓存）
- Nginx 1.20+

---

## Docker 部署（推荐）

### 1. 克隆项目

```bash
git clone <仓库地址> property-management-system
cd property-management-system
```

### 2. 配置环境变量

```bash
cd deploy
cp .env.example .env
```

编辑 `.env` 文件，修改以下关键配置：

```properties
# 数据库密码（必须修改）
MYSQL_ROOT_PASSWORD=你的数据库密码
MYSQL_PASSWORD=你的应用数据库密码

# JWT 密钥（必须修改，使用随机长字符串）
JWT_SECRET=你的JWT密钥

# 端口配置
ADMIN_PORT=80        # 管理员端
OWNER_PORT=8081      # 业主端
```

### 3. 构建并启动

```bash
cd deploy
docker-compose up -d
```

首次启动会自动：
- 拉取 MySQL 8.0、Redis 7、Nginx Alpine 镜像
- 构建后端 JAR 包（Maven）
- 构建管理员端前端（Node.js）
- 初始化数据库（执行 schema.sql 和 init-data.sql）
- 启动所有服务

### 4. 导入测试数据（可选）

```bash
# 进入 MySQL 容器
docker exec -it property-mysql mysql -u property -p property_managementone

# 在 MySQL 命令行中执行
source /docker-entrypoint-initdb.d/02-seed-data.sql
```

或者直接将 seed-data.sql 挂载后执行：

```bash
docker exec -i property-mysql mysql -u property -pProperty@2024 property_managementone < ../database/seed-data.sql
```

### 5. 验证部署

- 管理员端：http://localhost（默认端口）
- 业主端 H5：http://localhost:8081
- API 文档：http://localhost/doc.html
- 后端健康检查：http://localhost:8080/api/actuator/health

### 6. 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 超级管理员 | superadmin | admin123 |
| 测试业主 | 13900001111 | 123456 |

### 7. 常用运维命令

```bash
# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f backend          # 后端日志
docker-compose logs -f mysql            # 数据库日志
docker-compose logs -f nginx-admin      # 前端日志

# 重启单个服务
docker-compose restart backend

# 停止所有服务
docker-compose down

# 停止并删除数据卷（⚠️ 会清除数据）
docker-compose down -v

# 重新构建（代码更新后）
docker-compose build --no-cache backend
docker-compose up -d backend
```

---

## 手动部署

### 1. 数据库准备

#### 1.1 创建数据库

```sql
CREATE DATABASE property_managementone DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'property'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON property_managementone.* TO 'property'@'localhost';
FLUSH PRIVILEGES;
```

#### 1.2 导入数据

```bash
mysql -u property -p property_managementone < database/schema.sql
mysql -u property -p property_managementone < database/init-data.sql
mysql -u property -p property_managementone < database/seed-data.sql  # 可选测试数据
```

### 2. 后端部署

#### 2.1 配置数据库连接

编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/property_managementone?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: property
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
```

#### 2.2 编译打包

```bash
cd backend
mvn clean package -DskipTests
```

#### 2.3 启动服务

```bash
java -jar target/property-management-1.0.0.jar --spring.profiles.active=prod
```

后台运行：

```bash
nohup java -jar target/property-management-1.0.0.jar --spring.profiles.active=prod > app.log 2>&1 &
```

### 3. 管理员端前端部署

#### 3.1 安装依赖并构建

```bash
cd frontend/admin
npm install
npm run build
```

构建产物在 `dist/` 目录。

#### 3.2 Nginx 配置

```nginx
server {
    listen 80;
    server_name your-domain.com;

    root /path/to/frontend/admin/dist;
    index index.html;

    # API 反向代理
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # Vue Router history 模式
    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

#### 3.3 重启 Nginx

```bash
sudo nginx -t          # 测试配置
sudo nginx -s reload   # 重载配置
```

### 4. 业主端 H5 部署

#### 4.1 构建 H5

```bash
cd frontend/owner
npm install
npm run build:h5
```

#### 4.2 Nginx 配置

参考 `deploy/nginx-owner.conf`，配置方式与管理员端类似。

---

## 生产环境注意事项

### 安全配置

1. **修改默认密码**：数据库密码、JWT 密钥必须使用强密码
2. **HTTPS 配置**：生产环境务必配置 SSL 证书
3. **防火墙**：只开放 80/443 端口，数据库端口不对外暴露
4. **CORS 配置**：限制允许的域名

### 性能优化

1. **MySQL**：根据服务器配置调整 `innodb-buffer-pool-size`（建议为内存的 50-70%）
2. **JVM**：根据服务器配置调整 `-Xms` 和 `-Xmx` 参数
3. **Nginx**：启用 Gzip 压缩、配置静态资源缓存
4. **Redis**：配置 `maxmemory` 和淘汰策略

### 备份策略

```bash
# 数据库每日备份脚本
#!/bin/bash
DATE=$(date +%Y%m%d)
mysqldump -u property -p property_managementone > /backup/db_$DATE.sql
# 保留最近 30 天
find /backup -name "db_*.sql" -mtime +30 -delete
```

### 监控

- 后端健康检查：`/api/actuator/health`
- 日志文件：`backend/logs/property-management.log`
- Docker 日志：`docker-compose logs -f`

---

## 常见问题

### Q: Docker 构建超时怎么办？

A: Maven 依赖下载慢，可以配置国内镜像：

```xml
<!-- backend/pom.xml 中添加 -->
<repositories>
    <repository>
        <id>aliyun</id>
        <url>https://maven.aliyun.com/repository/public</url>
    </repository>
</repositories>
```

### Q: 数据库连接失败？

A: 检查以下几点：
1. MySQL 服务是否启动：`docker-compose ps mysql`
2. 密码是否正确
3. 网络是否连通：`docker exec property-backend ping mysql`

### Q: 前端页面空白？

A: 检查 Nginx 配置是否正确设置了 `try_files $uri $uri/ /index.html;`

### Q: 文件上传失败？

A: 检查上传目录权限：
```bash
docker exec property-backend ls -la /opt/property-management/upload/
```

### Q: 如何更新部署？

A:
```bash
git pull
cd deploy
docker-compose build --no-cache backend nginx-admin
docker-compose up -d
```
