# 物业管理系统

一个基于 SpringBoot + Vue + MySQL 的完整物业管理系统，包含业主端和管理员端。

## 📋 项目功能

### 业主端功能
- ✅ 手机号注册/登录
- ✅ 缴费管理（水费、电费、物业费、停车费）
- ✅ 在线报修（水电维修、门窗维修、家电故障、管道疏通、其他）
- ✅ 公告查看
- ✅ 家业管理（查看名下房产）
- ✅ 个人中心（修改信息、查看记录）

### 管理员端功能
- ✅ 超级管理员登录（系统内置）
- ✅ 普通管理员注册/登录
- ✅ 用户管理（增删改查业主信息）
- ✅ 楼盘管理（小区管理、房产资源导入导出）
- ✅ 缴费管理（查看账单、费用设置、收款操作）
- ✅ 报修工单（查看、处理、分配工单）
- ✅ 公告管理（发布、编辑、删除公告）
- ✅ 系统设置（账号管理、角色权限、操作日志）

## 🛠️ 技术栈

### 后端
- **框架**: SpringBoot 3.1.5
- **安全**: Spring Security + JWT
- **ORM**: MyBatis Plus
- **数据库**: MySQL 8.0
- **缓存**: Redis (可选)
- **文档**: Knife4j (Swagger)

### 前端
- **框架**: Vue 3 + TypeScript
- **UI**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **构建工具**: Vite 5
- **HTTP**: Axios

## 📁 项目结构

```
property-management-system/
├── backend/                 # SpringBoot 后端
│   ├── src/main/java/
│   │   └── com/property/management/
│   │       ├── config/      # 配置类
│   │       ├── controller/  # 控制器
│   │       ├── service/     # 服务层
│   │       ├── entity/      # 实体类
│   │       ├── dto/         # 数据传输对象
│   │       ├── mapper/      # MyBatis Mapper
│   │       ├── security/    # 安全相关
│   │       └── utils/       # 工具类
│   ├── src/main/resources/
│   │   ├── application.yml  # 配置文件
│   │   └── mapper/          # MyBatis XML
│   └── pom.xml              # Maven 配置
├── frontend/                # Vue 前端
│   ├── src/
│   │   ├── api/             # API 接口
│   │   ├── assets/          # 静态资源
│   │   ├── components/      # 组件
│   │   ├── layout/          # 布局组件
│   │   ├── router/          # 路由配置
│   │   ├── store/           # 状态管理
│   │   ├── utils/           # 工具函数
│   │   └── views/           # 页面视图
│   ├── package.json
│   └── vite.config.ts
├── database/                # 数据库脚本
│   └── schema.sql           # 数据库初始化脚本
├── deploy/                  # 部署配置
└── docs/                    # 文档
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

# 执行初始化脚本
source database/schema.sql
```

### 2. 后端启动

```bash
cd backend

# 修改配置文件 application.yml 中的数据库连接信息
# spring.datasource.url
# spring.datasource.username
# spring.datasource.password

# 编译并运行
mvn clean install
mvn spring-boot:run

# 后端服务运行在 http://localhost:8081
```

### 3. 前端启动

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 前端服务运行在 http://localhost:8080
```

## 🔐 默认账号

### 超级管理员
- 用户名：`superadmin`
- 密码：`admin123`

### 业主账号
- 通过注册页面自行注册

## 📊 API 文档

启动后端后访问：`http://localhost:8081/api/doc.html`

## 🔧 配置说明

### 后端配置 (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/property_management
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

## 📦 部署

### Docker 部署（待完善）

```bash
# 构建后端镜像
docker build -t property-management-backend ./backend

# 构建前端镜像
docker build -t property-management-frontend ./frontend

# 使用 docker-compose 启动
docker-compose up -d
```

## 📝 开发计划

- [ ] 完善所有业务功能
- [ ] 添加单元测试
- [ ] 添加集成测试
- [ ] 完善 Docker 部署配置
- [ ] 添加 CI/CD 流程
- [ ] 性能优化
- [ ] 移动端适配

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

MIT License

## 👥 联系方式

如有问题或建议，请提交 Issue 或联系开发团队。

---

**注意**: 本项目仅供学习参考，生产环境使用请根据实际情况进行调整和优化。