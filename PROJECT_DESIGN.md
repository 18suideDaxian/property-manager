# 物业管理系统 - 详细设计文档

## 项目概述
一个完整的物业管理系统，包含业主端和管理员端，采用SpringBoot + Vue + MySQL技术栈。

## 系统架构

### 1. 技术栈
- **后端**: SpringBoot 3.x + MyBatis Plus + Spring Security + JWT
- **前端**: Vue 3 + Element Plus + Vue Router + Pinia + Axios
- **数据库**: MySQL 8.0
- **部署**: Docker + Nginx

### 2. 系统模块划分

#### 2.1 业主端系统
- 用户认证模块（手机号注册登录）
- 缴费管理模块
- 报修管理模块  
- 公告查看模块
- 家业管理模块
- 个人中心模块

#### 2.2 管理员端系统
- 管理员认证模块
- 用户管理模块
- 楼盘管理模块
- 缴费管理模块
- 报修工单模块
- 公告管理模块
- 系统设置模块

## 数据库设计

### 核心表结构

#### 1. 用户相关表
- `user` - 业主用户表
- `admin_user` - 管理员用户表
- `role` - 角色表
- `permission` - 权限表
- `user_role` - 用户角色关联表
- `role_permission` - 角色权限关联表

#### 2. 房产相关表
- `community` - 小区表
- `building` - 楼栋表
- `unit` - 单元表
- `room` - 房间表（房产信息）
- `owner_room` - 业主房产关联表

#### 3. 业务相关表
- `fee_type` - 费用类型表
- `fee_bill` - 费用账单表
- `payment_record` - 缴费记录表
- `repair_type` - 报修类型表
- `repair_order` - 报修工单表
- `announcement` - 公告表
- `operation_log` - 操作日志表

## API接口设计

### 业主端API
- `POST /api/owner/auth/register` - 业主注册
- `POST /api/owner/auth/login` - 业主登录
- `GET /api/owner/fee/bills` - 获取我的账单
- `POST /api/owner/fee/pay` - 缴费
- `POST /api/owner/repair/create` - 创建报修
- `GET /api/owner/repair/list` - 我的报修列表
- `GET /api/owner/announcement/list` - 公告列表
- `GET /api/owner/room/list` - 我的房产列表
- `PUT /api/owner/profile` - 修改个人信息

### 管理员端API
- `POST /api/admin/auth/login` - 管理员登录
- `GET /api/admin/user/list` - 用户列表
- `POST /api/admin/user/create` - 创建用户
- `GET /api/admin/community/list` - 小区列表
- `POST /api/admin/room/import` - 导入房产
- `GET /api/admin/fee/bills` - 账单管理
- `POST /api/admin/fee/setting` - 费用设置
- `GET /api/admin/repair/list` - 报修工单
- `POST /api/admin/repair/assign` - 分配工单
- `POST /api/admin/announcement/create` - 发布公告
- `GET /api/admin/log/list` - 操作日志

## 前端页面设计

### 业主端页面
1. 登录/注册页
2. 首页（仪表板）
3. 缴费页面
4. 报修页面
5. 公告页面
6. 家业页面
7. 个人中心

### 管理员端页面
1. 登录页
2. 仪表板
3. 用户管理
4. 楼盘管理
5. 缴费管理
6. 报修工单
7. 公告管理
8. 系统设置

## 开发计划

### 第一阶段（1-2天）
1. 项目初始化
2. 数据库设计
3. 基础框架搭建
4. 用户认证模块

### 第二阶段（2-3天）
1. 业主端核心功能
2. 管理员端基础功能
3. 前后端联调

### 第三阶段（1-2天）
1. 高级功能开发
2. 权限系统完善
3. 系统测试

### 第四阶段（1天）
1. 部署配置
2. 文档编写
3. GitHub上传

## 部署方案

### 开发环境
- MySQL: 本地或Docker
- 后端: SpringBoot内嵌Tomcat
- 前端: Vue开发服务器

### 生产环境
- Docker Compose部署
- Nginx反向代理
- MySQL主从配置（可选）
- Redis缓存（可选）

## GitHub仓库结构
```
property-management-system/
├── backend/           # SpringBoot后端
├── frontend/          # Vue前端
├── database/          # 数据库脚本
├── deploy/            # 部署配置
├── docs/              # 文档
└── README.md          # 项目说明
```