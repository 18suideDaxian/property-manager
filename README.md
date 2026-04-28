# 物业管理系统

一个基于 SpringBoot 3 + Vue 3 + MySQL 的完整物业管理系统，包含业主端和管理员端。

## 📋 功能模块

### 业主端（微信小程序）
- ✅ 手机号注册/登录
- ✅ 缴费管理（水费、电费、物业费、停车费）
- ✅ 在线报修（水电/门窗/家电/管道/其他，支持拍照+预约）
- ✅ 公告查看
- ✅ 家业管理（查看名下房产）
- ✅ 个人中心（修改信息、查看记录）

### 管理员端（Web 后台）
- ✅ 超级管理员登录（系统内置）
- ✅ 普通管理员注册/权限分配
- ✅ 用户管理（增删改查业主信息）
- ✅ 楼盘管理（小区/楼栋/房间管理，Excel 导入导出）
- ✅ 缴费管理（账单查看、费用设置、收款操作）
- ✅ 报修工单（查看/处理/分配，工单流转）
- ✅ 公告管理（发布/编辑/删除）
- ✅ 系统设置（账号管理、角色权限、操作日志）

## 🛠️ 技术栈

### 后端
- **框架**: SpringBoot 3.1.5
- **安全**: Spring Security + JWT
- **ORM**: MyBatis Plus + LambdaQueryWrapper
- **数据库**: MySQL 8.0
- **缓存**: Redis 7
- **文档**: Knife4j (Swagger)

### 前端（管理员端）
- **框架**: Vue 3 + TypeScript
- **UI**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **构建工具**: Vite 5
- **HTTP**: Axios

### 业主端
- **框架**: uni-app + uView UI

## 📁 项目结构

```
property-manager/
├── backend/                    # SpringBoot 后端
│   ├── src/main/java/
│   │   └── com/property/management/
│   │       ├── config/         # 配置类
│   │       ├── controller/     # 11 个控制器
│   │       ├── service/        # 6 个服务层
│   │       ├── entity/         # 16 个实体类
│   │       ├── dto/            # 数据传输对象
│   │       ├── mapper/         # MyBatis Mapper
│   │       ├── security/       # JWT + Spring Security
│   │       └── utils/          # 工具类
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/             # MyBatis XML
│   └── pom.xml                 # Maven 配置
├── frontend/                   # Vue 3 管理员端
│   ├── src/
│   │   ├── api/                # API 接口
│   │   ├── assets/             # 静态资源
│   │   ├── components/         # 组件
│   │   ├── layout/             # 布局组件
│   │   ├── router/             # 路由配置
│   │   ├── store/              # 状态管理
│   │   ├── utils/              # 工具函数
│   │   └── views/              # 页面视图
│   ├── package.json
│   └── vite.config.ts
├── owner-app/                  # uni-app 业主端（微信小程序）
├── database/                   # 数据库脚本
│   ├── schema.sql              # 建表语句（16 张表）
│   ├── init-data.sql           # 初始化数据（角色/权限/费用类型等）
│   └── seed-data.sql           # 测试数据（开发环境用）
├── deploy/                     # Docker 部署配置
│   ├── docker-compose.yml      # 一键部署
│   ├── .env.example            # 环境变量模板
│   ├── Dockerfile.backend      # 后端镜像构建
│   ├── Dockerfile.admin        # 管理员端前端镜像
│   ├── nginx-*.conf            # Nginx 反向代理配置
│   └── mysql/                  # MySQL 自定义配置
└── docs/                       # 文档
```

## 🚀 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### 1. 数据库初始化

```bash
# 登录 MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE property_managementone DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE property_managementone;

# 按顺序执行（不要乱序）
source database/schema.sql       # 建表
source database/init-data.sql    # 初始化基础数据（必做）
# source database/seed-data.sql  # 测试数据（仅开发环境）
```

### 2. 后端启动

```bash
cd backend

# 修改 application.yml 中的数据库连接信息
# spring.datasource.url / username / password

# 编译并运行
mvn clean install
mvn spring-boot:run

# 后端运行在 http://localhost:8081
# API 文档 http://localhost:8081/api/doc.html
```

### 3. 前端启动

```bash
cd frontend

npm install
npm run dev

# 管理员端运行在 http://localhost:8080
```

## 🐳 Docker 一键部署

```bash
cd deploy

# 复制环境变量配置
cp .env.example .env
# 编辑 .env 修改密码等配置

# 启动所有服务
docker compose up -d

# 查看启动状态
docker compose ps
docker compose logs -f

# 停止
docker compose down
```

各服务说明：
| 服务 | 端口 | 说明 |
|------|------|------|
| MySQL | 3306 | 数据持久化 |
| Redis | 6379 | 缓存 |
| 后端 | 8080 | Spring Boot API |
| Nginx (管理员端) | 80 | Web 后台入口 |
| Nginx (业主端 H5) | 8081 | 业主端页面 |

> 首次部署会自动执行 `schema.sql` + `init-data.sql`，无需手动导入。

## 🔐 默认账号

### 超级管理员
- 用户名：`superadmin`
- 密码：`admin123`

### 普通管理员（测试数据）
- 财务：`finance01` / `admin123`
- 客服：`service01` / `admin123`
- 楼盘：`property01` / `admin123`

### 业主（测试数据）
- 张三：`13900001111` / `123456`
- 李四：`13900002222` / `123456`
- 密码见 `database/seed-data.sql`

## 📊 API 文档

启动后端后访问：`http://localhost:8081/api/doc.html`

## 🔧 配置说明

### 后端配置 (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/property_managementone
    username: root
    password: your_password
  
  redis:
    host: localhost
    port: 6379

jwt:
  secret: your-secret-key
  expiration: 86400000
```

### 前端配置 (vite.config.ts)

```typescript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8081',
      changeOrigin: true
    }
  }
}
```

## 📄 License

MIT
