# 物业管理系统 - API 接口文档

## 目录

- [概述](#概述)
- [认证方式](#认证方式)
- [通用响应格式](#通用响应格式)
- [管理员端 API](#管理员端-api)
  - [认证模块](#管理员认证模块)
  - [用户管理](#用户管理)
  - [角色管理](#角色管理)
  - [小区管理](#小区管理)
  - [费用管理](#费用管理)
  - [报修管理](#报修管理)
  - [公告管理](#公告管理)
- [业主端 API](#业主端-api)
  - [认证模块](#业主认证模块)
  - [费用查询](#费用查询)
  - [报修服务](#报修服务)
  - [公告查看](#公告查看)
  - [房产管理](#房产管理)
  - [个人中心](#个人中心)
- [错误码说明](#错误码说明)

---

## 概述

- 基础路径：`/api`
- 管理员端路径：`/api/admin/*`
- 业主端路径：`/api/owner/*`
- 数据格式：JSON
- 字符编码：UTF-8

## 认证方式

使用 JWT Token 认证，在请求头中携带：

```
Authorization: Bearer <token>
```

Token 通过登录接口获取，有效期 24 小时。

## 通用响应格式

```json
{
    "code": 200,
    "message": "success",
    "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码，200 表示成功 |
| message | String | 提示信息 |
| data | Object | 返回数据 |

### 分页响应格式

```json
{
    "code": 200,
    "message": "success",
    "data": {
        "records": [],
        "total": 100,
        "size": 10,
        "current": 1,
        "pages": 10
    }
}
```

---

## 管理员端 API

### 管理员认证模块

#### 管理员登录

```
POST /api/admin/auth/login
```

**请求参数：**

```json
{
    "username": "superadmin",
    "password": "admin123"
}
```

**响应：**

```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "tokenType": "Bearer",
        "expiresIn": 86400,
        "userInfo": {
            "id": 1,
            "username": "superadmin",
            "realName": "超级管理员",
            "isSuperAdmin": true
        }
    }
}
```

#### 管理员登出

```
POST /api/admin/auth/logout
```

**请求头：** Authorization: Bearer \<token\>

**响应：**

```json
{
    "code": 200,
    "message": "登出成功"
}
```

---

### 用户管理

#### 获取业主列表

```
GET /api/admin/user/list?page=1&size=10&keyword=张三&status=1
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认 1 |
| size | Integer | 否 | 每页条数，默认 10 |
| keyword | String | 否 | 搜索关键词（姓名/手机号） |
| status | Integer | 否 | 状态：0-禁用，1-启用 |

#### 创建业主

```
POST /api/admin/user/create
```

```json
{
    "phone": "13900001234",
    "password": "123456",
    "realName": "张三",
    "idCard": "110105199001011234",
    "email": "zhangsan@example.com"
}
```

#### 更新业主

```
PUT /api/admin/user/{id}
```

```json
{
    "realName": "张三",
    "phone": "13900001234",
    "email": "zhangsan@example.com",
    "status": 1
}
```

#### 删除业主

```
DELETE /api/admin/user/{id}
```

#### 重置业主密码

```
PUT /api/admin/user/{id}/reset-password
```

```json
{
    "newPassword": "123456"
}
```

---

### 角色管理

#### 获取角色列表

```
GET /api/admin/role/list
```

#### 创建角色

```
POST /api/admin/role/create
```

```json
{
    "roleName": "财务管理员",
    "roleCode": "FINANCE_ADMIN",
    "description": "负责财务管理",
    "permissionIds": [1, 2, 3]
}
```

#### 更新角色

```
PUT /api/admin/role/{id}
```

#### 删除角色

```
DELETE /api/admin/role/{id}
```

#### 获取权限列表

```
GET /api/admin/role/permissions
```

---

### 小区管理

#### 获取小区列表

```
GET /api/admin/community/list?page=1&size=10
```

#### 创建小区

```
POST /api/admin/community/create
```

```json
{
    "name": "阳光花园",
    "address": "北京市朝阳区建国路88号",
    "developer": "阳光地产集团",
    "propertyCompany": "阳光物业服务有限公司",
    "contactPhone": "010-88886666",
    "totalArea": 120000.00,
    "greenRate": 35.5,
    "buildYear": 2018,
    "description": "高品质住宅小区"
}
```

#### 获取楼栋列表

```
GET /api/admin/community/{communityId}/buildings
```

#### 创建楼栋

```
POST /api/admin/community/{communityId}/building/create
```

```json
{
    "buildingName": "1号楼",
    "floors": 18,
    "unitsPerFloor": 2,
    "roomsPerUnit": 4
}
```

#### 获取房间列表

```
GET /api/admin/room/list?page=1&size=10&buildingId=1&unitId=1&status=1
```

#### 创建房间

```
POST /api/admin/room/create
```

```json
{
    "unitId": 1,
    "roomNumber": "101",
    "floor": 1,
    "area": 89.50,
    "roomType": "两室一厅"
}
```

#### 导入房产（批量）

```
POST /api/admin/room/import
Content-Type: multipart/form-data
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| file | File | 是 | Excel 文件 |

#### 分配业主

```
PUT /api/admin/room/{roomId}/assign-owner
```

```json
{
    "ownerId": 1,
    "isMain": true
}
```

---

### 费用管理

#### 获取费用类型列表

```
GET /api/admin/fee/types
```

#### 创建费用类型

```
POST /api/admin/fee/type/create
```

```json
{
    "typeName": "物业费",
    "typeCode": "PROPERTY_FEE",
    "unit": "元/月·平方米",
    "price": 2.50,
    "description": "物业管理服务费"
}
```

#### 生成月度账单

```
POST /api/admin/fee/generate
```

```json
{
    "period": "2024-03",
    "feeTypeId": 1
}
```

#### 获取账单列表

```
GET /api/admin/fee/bills?page=1&size=10&period=2024-03&status=0&communityId=1
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页条数 |
| period | String | 否 | 账期，格式：YYYY-MM |
| status | Integer | 否 | 0-未缴，1-已缴，2-逾期，3-部分缴纳，4-作废 |
| communityId | Long | 否 | 小区 ID |
| buildingId | Long | 否 | 楼栋 ID |
| ownerId | Long | 否 | 业主 ID |
| feeTypeId | Long | 否 | 费用类型 ID |

#### 导出账单

```
GET /api/admin/fee/export?period=2024-03
```

返回 Excel 文件下载。

#### 收费统计

```
GET /api/admin/fee/statistics?startDate=2024-01&endDate=2024-03
```

```json
{
    "code": 200,
    "data": {
        "totalAmount": 150000.00,
        "paidAmount": 120000.00,
        "unpaidAmount": 30000.00,
        "collectionRate": 80.0,
        "monthlyStats": [
            { "period": "2024-01", "amount": 50000, "paid": 45000 },
            { "period": "2024-02", "amount": 50000, "paid": 40000 },
            { "period": "2024-03", "amount": 50000, "paid": 35000 }
        ]
    }
}
```

---

### 报修管理

#### 获取报修类型列表

```
GET /api/admin/repair/types
```

#### 创建报修类型

```
POST /api/admin/repair/type/create
```

```json
{
    "typeName": "水电维修",
    "description": "水管漏水、电路故障等维修"
}
```

#### 获取报修工单列表

```
GET /api/admin/repair/orders?page=1&size=10&status=0&repairTypeId=1
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页条数 |
| status | Integer | 否 | 0-待处理，1-已分配，2-处理中，3-已完成，4-已取消 |
| repairTypeId | Long | 否 | 报修类型 ID |
| priority | Integer | 否 | 1-低，2-中，3-高 |
| startDate | String | 否 | 开始日期 |
| endDate | String | 否 | 结束日期 |

#### 分配工单

```
PUT /api/admin/repair/{id}/assign
```

```json
{
    "assigneeId": 3
}
```

#### 处理工单

```
PUT /api/admin/repair/{id}/process
```

```json
{
    "status": 2,
    "remark": "已安排维修人员上门"
}
```

#### 完成工单

```
PUT /api/admin/repair/{id}/complete
```

```json
{
    "completeRemark": "已更换水龙头密封圈，测试正常无漏水"
}
```

---

### 公告管理

#### 获取公告列表

```
GET /api/admin/announcement/list?page=1&size=10&status=1
```

#### 创建公告

```
POST /api/admin/announcement/create
```

```json
{
    "title": "关于2024年春节放假安排的通知",
    "content": "尊敬的各位业主...",
    "publisher": "阳光花园物业服务中心",
    "communityId": 1,
    "publishTime": "2024-02-01 09:00:00",
    "expireTime": "2024-02-20 23:59:59",
    "status": 1
}
```

#### 更新公告

```
PUT /api/admin/announcement/{id}
```

#### 删除公告

```
DELETE /api/admin/announcement/{id}
```

---

## 业主端 API

### 业主认证模块

#### 手机号注册

```
POST /api/owner/auth/register
```

```json
{
    "phone": "13900001234",
    "password": "123456",
    "code": "1234"
}
```

#### 手机号登录

```
POST /api/owner/auth/login
```

```json
{
    "phone": "13900001111",
    "password": "123456"
}
```

**响应：**

```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "tokenType": "Bearer",
        "expiresIn": 86400,
        "userInfo": {
            "id": 1,
            "phone": "13900001111",
            "realName": "张三"
        }
    }
}
```

#### 发送验证码

```
POST /api/owner/auth/send-code
```

```json
{
    "phone": "13900001234",
    "type": "register"
}
```

---

### 费用查询

#### 获取我的账单列表

```
GET /api/owner/fee/bills?page=1&size=10&status=0&roomId=1
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页条数 |
| status | Integer | 否 | 0-未缴，1-已缴，2-逾期 |
| roomId | Long | 否 | 房间 ID |

#### 获取账单详情

```
GET /api/owner/fee/bill/{id}
```

#### 缴费

```
POST /api/owner/fee/pay
```

```json
{
    "billId": 1,
    "payMethod": "微信支付"
}
```

#### 获取缴费记录

```
GET /api/owner/fee/payments?page=1&size=10
```

#### 获取费用汇总

```
GET /api/owner/fee/summary
```

```json
{
    "code": 200,
    "data": {
        "totalUnpaid": 523.75,
        "totalPaid": 1500.00,
        "unpaidCount": 3,
        "recentBills": [...]
    }
}
```

---

### 报修服务

#### 创建报修工单

```
POST /api/owner/repair/create
```

```json
{
    "roomId": 1,
    "repairTypeId": 1,
    "title": "厨房水龙头漏水",
    "description": "厨房水龙头关不紧，一直在滴水",
    "images": "https://xxx.com/img1.jpg,https://xxx.com/img2.jpg",
    "contactName": "张三",
    "contactPhone": "13900001111",
    "appointmentDate": "2024-01-16",
    "appointmentTime": "09:00-12:00"
}
```

#### 获取我的报修列表

```
GET /api/owner/repair/list?page=1&size=10&status=0
```

#### 获取报修详情

```
GET /api/owner/repair/{id}
```

#### 取消报修

```
PUT /api/owner/repair/{id}/cancel
```

#### 评价工单

```
POST /api/owner/repair/{id}/feedback
```

```json
{
    "rating": 5,
    "feedback": "维修很快，师傅态度很好！"
}
```

---

### 公告查看

#### 获取公告列表

```
GET /api/owner/announcement/list?page=1&size=10
```

#### 获取公告详情

```
GET /api/owner/announcement/{id}
```

---

### 房产管理

#### 获取我的房产列表

```
GET /api/owner/room/list
```

**响应：**

```json
{
    "code": 200,
    "data": [
        {
            "id": 1,
            "communityName": "阳光花园",
            "buildingName": "1号楼",
            "unitName": "1单元",
            "roomNumber": "101",
            "floor": 1,
            "area": 89.50,
            "roomType": "两室一厅",
            "isMain": true
        }
    ]
}
```

#### 获取房产详情

```
GET /api/owner/room/{id}
```

---

### 个人中心

#### 获取个人信息

```
GET /api/owner/profile
```

#### 修改个人信息

```
PUT /api/owner/profile
```

```json
{
    "realName": "张三",
    "email": "zhangsan@example.com",
    "avatar": "https://xxx.com/avatar.jpg"
}
```

#### 修改密码

```
PUT /api/owner/profile/password
```

```json
{
    "oldPassword": "123456",
    "newPassword": "654321"
}
```

#### 获取仪表盘数据

```
GET /api/owner/dashboard
```

```json
{
    "code": 200,
    "data": {
        "roomCount": 2,
        "unpaidBills": 3,
        "unpaidAmount": 523.75,
        "pendingRepairs": 1,
        "recentAnnouncements": [...]
    }
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未认证或 Token 过期 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 1001 | 用户名或密码错误 |
| 1002 | 用户已被禁用 |
| 1003 | 手机号已注册 |
| 1004 | 验证码错误或已过期 |
| 2001 | 账单不存在 |
| 2002 | 账单已缴纳 |
| 2003 | 账单已逾期 |
| 3001 | 报修工单不存在 |
| 3002 | 工单状态不允许此操作 |
| 4001 | 公告不存在 |
| 5001 | 房间不存在 |
| 5002 | 房间已分配业主 |
