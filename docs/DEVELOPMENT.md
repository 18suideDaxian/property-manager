# 物业管理系统 - 开发指南

## 目录

- [环境搭建](#环境搭建)
- [项目结构](#项目结构)
- [启动方式](#启动方式)
- [开发规范](#开发规范)
- [数据库说明](#数据库说明)
- [构建部署](#构建部署)

---

## 环境搭建

### 必需环境

| 工具 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 17+ | 后端运行环境 |
| Maven | 3.8+ | 后端依赖管理和构建 |
| Node.js | 18+ | 前端运行环境 |
| npm | 9+ | 前端包管理器 |
| MySQL | 8.0+ | 数据库 |
| Redis | 7.0+ | 缓存（可选） |
| Git | 2.0+ | 版本控制 |

### 推荐 IDE

- 后端：IntelliJ IDEA
- 前端：Visual Studio Code

### VS Code 插件推荐

- Vue - Official（Vue 3 支持）
- ESLint
- Prettier
- Auto Close Tag
- Auto Rename Tag

### 安装步骤

#### 1. 克隆项目

```bash
git clone <仓库地址> property-management-system
cd property-management-system
```

#### 2. 创建数据库

```bash
mysql -u root -p
```

```sql
CREATE DATABASE property_managementone DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. 导入数据库结构和初始化数据

```bash
mysql -u root -p property_managementone < database/schema.sql
mysql -u root -p property_managementone < database/init-data.sql
mysql -u root -p property_managementone < database/seed-data.sql  # 可选测试数据
```

#### 4. 安装后端依赖

```bash
cd backend
mvn clean install -DskipTests
```

#### 5. 安装管理员端前端依赖

```bash
cd frontend/admin
npm install
```

#### 6. 安装业主端前端依赖

```bash
cd frontend/owner
npm install
```

---

## 项目结构

```
property-management-system/
├── backend/                          # 后端（Spring Boot）
│   ├── pom.xml                       # Maven 配置
│   └── src/
│       └── main/
│           ├── java/com/property/management/
│           │   ├── PropertyManagementApplication.java  # 启动类
│           │   ├── config/           # 配置类
│           │   │   ├── JwtConfig.java
│           │   │   └── SecurityConfig.java
│           │   ├── controller/       # 控制器
│           │   │   ├── Admin*Controller.java    # 管理员端接口
│           │   │   └── Owner*Controller.java    # 业主端接口
│           │   ├── dto/              # 数据传输对象
│           │   │   ├── LoginDTO.java
│           │   │   ├── RegisterDTO.java
│           │   │   └── ResponseResult.java
│           │   ├── entity/           # 实体类
│           │   │   ├── AdminUser.java
│           │   │   ├── OwnerUser.java
│           │   │   ├── Room.java
│           │   │   ├── FeeBill.java
│           │   │   ├── RepairOrder.java
│           │   │   └── ...
│           │   ├── exception/        # 异常处理
│           │   │   └── GlobalExceptionHandler.java
│           │   ├── mapper/           # MyBatis Plus Mapper
│           │   ├── security/         # Spring Security
│           │   │   ├── JwtAuthenticationFilter.java
│           │   │   └── JwtAuthorizationFilter.java
│           │   ├── service/          # 业务逻辑层
│           │   │   ├── *.java        # 接口
│           │   │   └── impl/         # 实现
│           │   └── utils/            # 工具类
│           │       └── JwtUtil.java
│           └── resources/
│               └── application.yml   # 应用配置
│
├── database/                         # 数据库脚本
│   ├── schema.sql                    # 表结构
│   ├── init-data.sql                 # 初始化数据
│   └── seed-data.sql                 # 测试数据
│
├── deploy/                           # 部署配置
│   ├── docker-compose.yml            # Docker Compose
│   ├── Dockerfile.backend            # 后端 Dockerfile
│   ├── Dockerfile.admin              # 管理员端 Dockerfile
│   ├── nginx-admin.conf              # 管理员端 Nginx
│   ├── nginx-owner.conf              # 业主端 Nginx
│   └── .env.example                  # 环境变量模板
│
├── docs/                             # 项目文档
│   ├── API.md                        # API 文档
│   ├── DEPLOY.md                     # 部署文档
│   └── DEVELOPMENT.md                # 开发指南
│
├── frontend/
│   ├── admin/                        # 管理员端（Vue 3 + Element Plus）
│   │   ├── package.json
│   │   ├── vite.config.js
│   │   ├── .env.development
│   │   ├── .env.production
│   │   └── src/
│   │       ├── main.js               # 入口文件
│   │       ├── App.vue               # 根组件
│   │       ├── api/                  # API 接口封装
│   │       │   ├── auth.js
│   │       │   ├── user.js
│   │       │   ├── community.js
│   │       │   ├── fee.js
│   │       │   ├── repair.js
│   │       │   ├── announcement.js
│   │       │   ├── role.js
│   │       │   └── room.js
│   │       ├── components/           # 公共组件
│   │       │   └── Pagination.vue
│   │       ├── layout/               # 布局组件
│   │       │   ├── AdminLayout.vue
│   │       │   ├── Header.vue
│   │       │   └── Sidebar.vue
│   │       ├── router/               # 路由配置
│   │       │   └── index.js
│   │       ├── store/                # 状态管理（Pinia）
│   │       │   ├── index.js
│   │       │   └── user.js
│   │       ├── utils/                # 工具函数
│   │       │   ├── auth.js
│   │       │   └── request.js
│   │       └── views/                # 页面组件
│   │           ├── Login.vue
│   │           ├── Dashboard.vue
│   │           ├── UserManage.vue
│   │           ├── CommunityManage.vue
│   │           ├── RoomManage.vue
│   │           ├── FeeManage.vue
│   │           ├── RepairManage.vue
│   │           ├── AnnouncementManage.vue
│   │           ├── RoleManage.vue
│   │           └── OperationLog.vue
│   │
│   └── owner/                        # 业主端（uni-app）
│       ├── package.json
│       ├── manifest.json             # uni-app 配置
│       ├── pages.json                # 页面路由配置
│       ├── App.vue                   # 根组件
│       ├── main.js                   # 入口文件
│       ├── api/                      # API 接口封装
│       │   ├── auth.js
│       │   ├── fee.js
│       │   ├── repair.js
│       │   ├── announcement.js
│       │   ├── room.js
│       │   └── profile.js
│       ├── components/               # 公共组件
│       │   ├── empty-state/
│       │   └── phone-login/
│       ├── pages/                    # 页面
│       │   ├── index/                # 首页
│       │   ├── login/                # 登录
│       │   ├── fee/                  # 缴费
│       │   ├── my-bills/             # 我的账单
│       │   ├── repair/               # 报修
│       │   ├── my-repairs/           # 我的报修
│       │   ├── repair-list/          # 报修列表
│       │   ├── announcement/         # 公告列表
│       │   ├── announcement-detail/  # 公告详情
│       │   ├── room/                 # 房产
│       │   ├── my-rooms/             # 我的房产
│       │   ├── profile/              # 个人中心
│       │   └── edit-name/            # 修改姓名
│       ├── static/                   # 静态资源
│       └── utils/                    # 工具函数
│           ├── auth.js
│           ├── config.js
│           └── request.js
│
├── PROJECT_DESIGN.md                 # 项目设计文档
├── README.md                         # 项目说明
└── .gitignore
```

---

## 启动方式

### 后端启动

```bash
cd backend

# 方式一：Maven 启动
mvn spring-boot:run

# 方式二：IDEA 启动
# 运行 PropertyManagementApplication.java

# 方式三：JAR 包启动
mvn package -DskipTests
java -jar target/property-management-1.0.0.jar
```

后端默认端口：8080

### 管理员端前端启动

```bash
cd frontend/admin
npm run dev
```

管理员端默认端口：3100，访问 http://localhost:3100

Vite 已配置代理，`/api` 请求会自动转发到后端 http://localhost:8080

### 业主端前端启动

```bash
cd frontend/owner

# H5 模式
npm run dev:h5

# 微信小程序模式
npm run dev:mp-weixin
```

H5 模式默认端口：5173，访问 http://localhost:5173

### 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 超级管理员 | superadmin | admin123 |
| 财务管理员 | finance01 | admin123 |
| 客服管理员 | service01 | admin123 |
| 测试业主 | 13900001111 | 123456 |

---

## 开发规范

### 后端规范

1. **分层架构**：Controller → Service → Mapper，不允许跨层调用
2. **命名规范**：
   - 类名：大驼峰（PascalCase）
   - 方法名、变量名：小驼峰（camelCase）
   - 常量：全大写下划线分隔（UPPER_SNAKE_CASE）
   - 数据库表名、字段名：下划线分隔（snake_case）
3. **返回格式**：统一使用 `ResponseResult` 包装
4. **异常处理**：业务异常使用自定义异常类，由 `GlobalExceptionHandler` 统一处理
5. **日志规范**：使用 SLF4J，关键操作必须记录日志
6. **注释规范**：公开 API 必须有 JavaDoc 注释

### 前端规范

1. **组件命名**：大驼峰（PascalCase），如 `UserManage.vue`
2. **文件命名**：
   - 组件：大驼峰
   - 页面：kebab-case，如 `my-bills`
   - API 文件：camelCase，如 `user.js`
3. **状态管理**：使用 Pinia，按模块拆分 store
4. **API 调用**：统一通过 `api/` 目录下的封装函数
5. **样式规范**：
   - 管理员端：使用 Element Plus 组件 + scoped CSS
   - 业主端：使用 SCSS，遵循 uni-app 规范

### Git 规范

1. **分支管理**：
   - `main`：主分支，稳定版本
   - `develop`：开发分支
   - `feature/*`：功能分支
   - `hotfix/*`：紧急修复分支
2. **提交信息**：
   - `feat: 新增xxx功能`
   - `fix: 修复xxx问题`
   - `docs: 更新文档`
   - `style: 代码格式调整`
   - `refactor: 重构xxx`
   - `test: 添加测试`
   - `chore: 构建/工具变动`

---

## 数据库说明

### 核心表

| 表名 | 说明 |
|------|------|
| admin_user | 管理员用户表 |
| owner_user | 业主用户表 |
| role | 角色表 |
| permission | 权限表 |
| user_role | 用户-角色关联 |
| role_permission | 角色-权限关联 |
| community | 小区表 |
| building | 楼栋表 |
| unit | 单元表 |
| room | 房间表 |
| owner_room | 业主-房间关联 |
| fee_type | 费用类型表 |
| fee_bill | 费用账单表 |
| payment_record | 缴费记录表 |
| repair_type | 报修类型表 |
| repair_order | 报修工单表 |
| announcement | 公告表 |
| operation_log | 操作日志表 |
| file | 文件表 |

### 账单状态

| 状态码 | 说明 |
|--------|------|
| 0 | 未缴 |
| 1 | 已缴 |
| 2 | 逾期 |
| 3 | 部分缴纳 |
| 4 | 作废 |

### 报修工单状态

| 状态码 | 说明 |
|--------|------|
| 0 | 待处理 |
| 1 | 已分配 |
| 2 | 处理中 |
| 3 | 已完成 |
| 4 | 已取消 |

---

## 构建部署

### 后端构建

```bash
cd backend
mvn clean package -DskipTests
# 产物：target/property-management-1.0.0.jar
```

### 管理员端构建

```bash
cd frontend/admin
npm run build
# 产物：dist/ 目录
```

### 业主端 H5 构建

```bash
cd frontend/owner
npm run build:h5
# 产物：dist/build/h5/ 目录
```

### 业主端微信小程序构建

```bash
cd frontend/owner
npm run build:mp-weixin
# 产物：dist/build/mp-weixin/ 目录
# 使用微信开发者工具导入该目录
```

### Docker 构建

```bash
cd deploy
docker-compose build
docker-compose up -d
```

详细部署文档请参考 [DEPLOY.md](./DEPLOY.md)。
