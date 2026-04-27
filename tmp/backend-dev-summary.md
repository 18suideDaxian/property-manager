# 后端核心业务模块开发完成汇总

## 完成时间
2026-04-27

## 新增文件清单（共 49 个新文件）

### 1. 实体类（entity/）— 16 个文件

| 文件名 | 表名 | 说明 |
|--------|------|------|
| Community.java | community | 小区信息 |
| Building.java | building | 楼栋信息 |
| Unit.java | unit | 单元信息 |
| Room.java | room | 房产信息 |
| OwnerRoom.java | owner_room | 业主房产关联 |
| FeeType.java | fee_type | 费用类型 |
| FeeBill.java | fee_bill | 费用账单 |
| PaymentRecord.java | payment_record | 缴费记录 |
| RepairType.java | repair_type | 报修类型 |
| RepairOrder.java | repair_order | 报修工单 |
| Announcement.java | announcement | 公告 |
| Role.java | role | 角色 |
| Permission.java | permission | 权限 |
| UserRole.java | user_role | 用户角色关联 |
| RolePermission.java | role_permission | 角色权限关联 |
| OperationLog.java | operation_log | 操作日志 |

### 2. Mapper 接口（mapper/）— 16 个文件

所有实体对应的 Mapper 接口，继承 `BaseMapper<T>`，使用 `@Repository` 注解：
- CommunityMapper, BuildingMapper, UnitMapper, RoomMapper, OwnerRoomMapper
- FeeTypeMapper, FeeBillMapper, PaymentRecordMapper
- RepairTypeMapper, RepairOrderMapper
- AnnouncementMapper
- RoleMapper, PermissionMapper, UserRoleMapper, RolePermissionMapper
- OperationLogMapper

### 3. Service 接口 + 实现（service/ + service/impl/）— 6 个接口 + 6 个实现 = 12 个文件

| Service 接口 | 实现类 | 核心功能 |
|-------------|--------|---------|
| FeeService | FeeServiceImpl | 业主查账单/缴费/查记录；管理员生成账单/代缴/统计 |
| RepairService | RepairServiceImpl | 业主报修/查工单/评价/取消；管理员受理/处理/完成/关闭/统计 |
| AnnouncementService | AnnouncementServiceImpl | 业主查公告；管理员创建/发布/撤回/删除 |
| RoomService | RoomServiceImpl | 业主查房产；管理员增删改查/绑定解绑业主 |
| CommunityService | CommunityServiceImpl | 小区/楼栋/单元的 CRUD |
| UserService | UserServiceImpl | 业主个人信息/改密；管理员管理业主和管理员账号 |

### 4. Controller（controller/）— 11 个文件

#### 业主端（5 个）
| Controller | 路径前缀 | 功能 |
|-----------|---------|------|
| OwnerFeeController | /owner/fee | 查账单、缴费、查缴费记录 |
| OwnerRepairController | /owner/repair | 报修、查工单、评价、取消 |
| OwnerAnnouncementController | /owner/announcement | 查公告列表、查看详情 |
| OwnerRoomController | /owner/room | 查我的房产列表、房产详情 |
| OwnerProfileController | /owner/profile | 查看/修改个人信息、改密码 |

#### 管理端（6 个）
| Controller | 路径前缀 | 功能 |
|-----------|---------|------|
| AdminUserController | /admin/user | 业主/管理员的增删改查、重置密码、统计 |
| AdminCommunityController | /admin/community | 小区/楼栋/单元的 CRUD |
| AdminFeeController | /admin/fee | 账单管理、生成/作废/代缴、统计 |
| AdminRepairController | /admin/repair | 工单管理（受理/处理/完成/关闭）、统计 |
| AdminAnnouncementController | /admin/announcement | 公告管理（创建/发布/撤回/删除） |
| AdminRoleController | /admin/role | 角色/权限 CRUD、用户角色分配、角色权限分配 |

## API 路由汇总

### 业主端 API
```
POST   /owner/auth/register          — 注册
POST   /owner/auth/login             — 登录
GET    /owner/profile                 — 查看个人信息
PUT    /owner/profile                 — 修改个人信息
PUT    /owner/profile/password        — 修改密码
GET    /owner/room/list               — 我的房产列表
GET    /owner/room/{roomId}           — 房产详情
GET    /owner/fee/bills               — 我的账单列表
GET    /owner/fee/bills/{billId}      — 账单详情
POST   /owner/fee/bills/{billId}/pay  — 缴费
GET    /owner/fee/payments            — 缴费记录
POST   /owner/repair/orders           — 提交报修
GET    /owner/repair/orders           — 我的工单列表
GET    /owner/repair/orders/{orderId} — 工单详情
POST   /owner/repair/orders/{orderId}/rate   — 评价工单
POST   /owner/repair/orders/{orderId}/cancel — 取消工单
GET    /owner/announcement/list       — 公告列表
GET    /owner/announcement/{id}       — 公告详情
```

### 管理端 API
```
POST   /admin/auth/login              — 管理员登录

# 用户管理
GET    /admin/user/owners             — 业主列表
GET    /admin/user/owners/{id}        — 业主详情
PUT    /admin/user/owners/{id}/status — 启用/禁用业主
GET    /admin/user/admins             — 管理员列表
POST   /admin/user/admins             — 添加管理员
PUT    /admin/user/admins/{id}        — 修改管理员
PUT    /admin/user/admins/{id}/status — 启用/禁用管理员
POST   /admin/user/reset-password     — 重置密码
GET    /admin/user/statistics         — 用户统计

# 小区管理
GET    /admin/community/list          — 小区列表
GET    /admin/community/{id}          — 小区详情
POST   /admin/community               — 创建小区
PUT    /admin/community/{id}          — 修改小区
DELETE /admin/community/{id}          — 删除小区
GET    /admin/community/{id}/buildings — 楼栋列表
POST   /admin/community/buildings     — 添加楼栋
PUT    /admin/community/buildings/{id} — 修改楼栋
DELETE /admin/community/buildings/{id} — 删除楼栋
GET    /admin/community/buildings/{id}/units — 单元列表
POST   /admin/community/units         — 添加单元
PUT    /admin/community/units/{id}    — 修改单元
DELETE /admin/community/units/{id}    — 删除单元

# 缴费管理
GET    /admin/fee/bills               — 账单列表
POST   /admin/fee/bills               — 生成账单
POST   /admin/fee/bills/batch         — 批量生成
POST   /admin/fee/bills/{id}/cancel   — 作废账单
POST   /admin/fee/bills/{id}/admin-pay — 管理员代缴
GET    /admin/fee/statistics          — 缴费统计

# 报修管理
GET    /admin/repair/orders           — 工单列表
POST   /admin/repair/orders/{id}/accept   — 受理
POST   /admin/repair/orders/{id}/process  — 处理
POST   /admin/repair/orders/{id}/complete — 完成
POST   /admin/repair/orders/{id}/close    — 关闭
GET    /admin/repair/statistics       — 报修统计

# 公告管理
GET    /admin/announcement/list       — 公告列表
POST   /admin/announcement            — 创建公告
PUT    /admin/announcement/{id}       — 修改公告
POST   /admin/announcement/{id}/publish — 发布
POST   /admin/announcement/{id}/revoke  — 撤回
DELETE /admin/announcement/{id}       — 删除

# 角色权限
GET    /admin/role/list               — 角色列表
POST   /admin/role                    — 创建角色
PUT    /admin/role/{id}               — 修改角色
DELETE /admin/role/{id}               — 删除角色
GET    /admin/role/{id}               — 角色详情
GET    /admin/role/permissions         — 权限列表
POST   /admin/role/permissions         — 创建权限
PUT    /admin/role/permissions/{id}    — 修改权限
DELETE /admin/role/permissions/{id}    — 删除权限
POST   /admin/role/assign             — 分配角色
DELETE /admin/role/assign             — 移除角色
GET    /admin/role/user/{id}/roles    — 用户的角色
POST   /admin/role/{id}/permissions   — 角色分配权限
GET    /admin/role/{id}/permissions   — 角色的权限
```

## 技术栈
- Spring Boot + MyBatis-Plus + Spring Security + JWT
- 统一响应格式 ResponseResult<T>
- 分页使用 MyBatis-Plus Page
- 使用 LambdaQueryWrapper 构建查询条件
- 事务使用 @Transactional 注解

## 待办事项
- [ ] 数据库建表 SQL（根据实体类生成）
- [ ] MyBatis-Plus 分页插件配置
- [ ] MyBatis-Plus 自动填充配置（createTime/updateTime）
- [ ] 费用类型管理 CRUD（FeeType 相关 Controller/Service）
- [ ] 报修类型管理 CRUD（RepairType 相关 Controller/Service）
- [ ] 操作日志 AOP 切面
- [ ] 前端页面开发
