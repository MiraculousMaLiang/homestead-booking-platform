# API接口文档

## 基础信息

- **Base URL**: `http://localhost:8080/api`
- **认证方式**: Sa-Token 
- **请求格式**: `application/json`
- **响应格式**: `application/json`

## 统一响应格式

### 成功响应
```json
{
  "code": 1,
  "msg": null,
  "data": {}
}
```

### 失败响应
```json
{
  "code": 0,
  "msg": "错误信息",
  "data": null
}
```

## 状态码说明

| 状态码 | 说明 |
|-------|------|
| 1 | 成功 |
| 0 | 失败 |

---

## 一、用户管理接口

### 1.1 用户注册

**接口**: `POST /user/register`

**是否需要认证**: 否

**请求参数**:
```json
{
  "username": "testuser",
  "password": "Test123456",
  "phone": "13800138000",
  "verifyCode": "123456",
  "email": "test@example.com",
  "nickname": "测试用户"
}
```

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| username | string | 是 | 用户名,4-20位字母/数字/下划线 |
| password | string | 是 | 密码,6-20位,包含字母和数字 |
| phone | string | 是 | 手机号 |
| verifyCode | string | 是 | 短信验证码 |
| email | string | 否 | 邮箱 |
| nickname | string | 否 | 昵称 |

**响应示例**:
```json
{
  "code": 1,
  "msg": null,
  "data": null
}
```

---

### 1.2 用户登录

**接口**: `POST /user/login`

**是否需要认证**: 否

**请求参数**:
```json
{
  "username": "testuser",
  "password": "Test123456",
  "loginType": 1
}
```

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| username | string | 是 | 用户名或手机号 |
| password | string | 条件必填 | 密码(loginType=1时必填) |
| verifyCode | string | 条件必填 | 验证码(loginType=2时必填) |
| loginType | integer | 否 | 登录类型: 1-密码登录 2-验证码登录,默认1 |

**响应示例**:
```json
{
  "code": 1,
  "msg": null,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoidGVzdHVzZXIifQ...",
    "user": {
      "id": 1,
      "username": "testuser",
      "phone": "13800138000",
      "email": "test@example.com",
      "nickname": "测试用户",
      "avatar": null,
      "gender": 0,
      "birthday": null,
      "userType": 1,
      "status": 1,
      "createTime": "2025-11-18 10:00:00"
    }
  }
}
```

---

### 1.3 发送验证码

**接口**: `POST /user/sendVerifyCode`

**是否需要认证**: 否

**请求参数**:
```
POST /user/sendVerifyCode?phone=13800138000
```

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| phone | string | 是 | 手机号 |

**响应示例**:
```json
{
  "code": 1,
  "msg": null,
  "data": null,
  
}
```

---

### 1.4 获取用户信息

**接口**: `GET /user/info`

**是否需要认证**: 是

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 1,
  "msg": null,
  "data": {
    "id": 1,
    "username": "testuser",
    "phone": "13800138000",
    "email": "test@example.com",
    "nickname": "测试用户",
    "avatar": "https://example.com/avatar.jpg",
    "gender": 1,
    "birthday": "1990-01-01",
    "userType": 1,
    "status": 1,
    "createTime": "2025-11-18 10:00:00"
  },
  
}
```

---

### 1.5 更新用户信息

**接口**: `PUT /user/update`

**是否需要认证**: 是

**请求参数**:
```json
{
  "nickname": "新昵称",
  "avatar": "https://example.com/new-avatar.jpg",
  "gender": 1,
  "birthday": "1990-01-01",
  "email": "newemail@example.com"
}
```

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| nickname | string | 否 | 昵称 |
| avatar | string | 否 | 头像URL |
| gender | integer | 否 | 性别: 0-未知 1-男 2-女 |
| birthday | date | 否 | 生日,格式: yyyy-MM-dd |
| email | string | 否 | 邮箱 |

---

### 1.6 修改密码

**接口**: `PUT /user/changePassword`

**是否需要认证**: 是

**请求参数**:
```
PUT /user/changePassword?oldPassword=Old123456&newPassword=New123456
```

---

## 二、房源管理接口

### 2.1 发布房源

**接口**: `POST /homestead/publish`

**是否需要认证**: 是

**请求参数**:
```json
{
  "title": "温馨两居室",
  "description": "位于市中心,交通便利,周边配套设施齐全",
  "province": "浙江省",
  "city": "杭州市",
  "district": "西湖区",
  "address": "文一路100号",
  "longitude": 120.123456,
  "latitude": 30.123456,
  "roomType": 1,
  "bedroomCount": 2,
  "bedCount": 3,
  "bathroomCount": 1,
  "maxGuests": 4,
  "area": 80.5,
  "pricePerDay": 388.00,
  "pricePerWeek": 2500.00,
  "pricePerMonth": 9000.00,
  "deposit": 500.00,
  "minDays": 1,
  "coverImage": "https://example.com/cover.jpg",
  "checkInTime": "14:00",
  "checkOutTime": "12:00"
}
```

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| title | string | 是 | 房源标题 |
| description | string | 否 | 房源描述 |
| province | string | 是 | 省份 |
| city | string | 是 | 城市 |
| district | string | 否 | 区县 |
| address | string | 是 | 详细地址 |
| longitude | decimal | 否 | 经度 |
| latitude | decimal | 否 | 纬度 |
| roomType | integer | 是 | 房型: 1-整套房 2-独立房间 3-合住房间 |
| bedroomCount | integer | 否 | 卧室数量 |
| bedCount | integer | 否 | 床位数量 |
| bathroomCount | integer | 否 | 卫生间数量 |
| maxGuests | integer | 是 | 最多入住人数 |
| area | decimal | 否 | 面积(平方米) |
| pricePerDay | decimal | 是 | 日租金 |
| pricePerWeek | decimal | 否 | 周租金 |
| pricePerMonth | decimal | 否 | 月租金 |
| deposit | decimal | 否 | 押金 |
| minDays | integer | 否 | 最少入住天数,默认1 |
| coverImage | string | 否 | 封面图URL |
| checkInTime | time | 否 | 入住时间,格式HH:mm |
| checkOutTime | time | 否 | 退房时间,格式HH:mm |

---

### 2.2 查询房源列表

**接口**: `POST /homestead/list`

**是否需要认证**: 否

**请求参数**:
```json
{
  "keyword": "温馨",
  "city": "杭州市",
  "roomType": 1,
  "minPrice": 100,
  "maxPrice": 500,
  "minGuests": 2,
  "minRating": 4.0,
  "sortField": "price",
  "sortOrder": "asc",
  "pageNum": 1,
  "pageSize": 10
}
```

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| keyword | string | 否 | 关键词(搜索标题和描述) |
| city | string | 否 | 城市 |
| roomType | integer | 否 | 房型 |
| minPrice | decimal | 否 | 最低价格 |
| maxPrice | decimal | 否 | 最高价格 |
| minGuests | integer | 否 | 最少入住人数 |
| minRating | decimal | 否 | 最低评分 |
| sortField | string | 否 | 排序字段: price-价格 rating-评分 view-浏览量 |
| sortOrder | string | 否 | 排序方式: asc-升序 desc-降序 |
| pageNum | integer | 否 | 页码,默认1 |
| pageSize | integer | 否 | 每页大小,默认10 |

**响应示例**:
```json
{
  "code": 1,
  "msg": null,
  "data": {
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 10,
    "records": [
      {
        "id": 1,
        "userId": 2,
        "title": "温馨两居室",
        "description": "位于市中心",
        "province": "浙江省",
        "city": "杭州市",
        "district": "西湖区",
        "address": "文一路100号",
        "pricePerDay": 388.00,
        "coverImage": "https://example.com/cover.jpg",
        "viewCount": 120,
        "favoriteCount": 35,
        "ratingScore": 4.8,
        "status": 1,
        "createTime": "2025-11-18 10:00:00"
      }
    ]
  },
  
}
```

---

### 2.3 获取房源详情

**接口**: `GET /homestead/detail/{id}`

**是否需要认证**: 否

**路径参数**:
- `id`: 房源ID

**响应示例**:
```json
{
  "code": 1,
  "msg": null,
  "data": {
    "id": 1,
    "userId": 2,
    "title": "温馨两居室",
    "description": "位于市中心,交通便利",
    "province": "浙江省",
    "city": "杭州市",
    "district": "西湖区",
    "address": "文一路100号",
    "longitude": 120.123456,
    "latitude": 30.123456,
    "roomType": 1,
    "bedroomCount": 2,
    "bedCount": 3,
    "bathroomCount": 1,
    "maxGuests": 4,
    "area": 80.5,
    "pricePerDay": 388.00,
    "pricePerWeek": 2500.00,
    "pricePerMonth": 9000.00,
    "deposit": 500.00,
    "minDays": 1,
    "coverImage": "https://example.com/cover.jpg",
    "checkInTime": "14:00",
    "checkOutTime": "12:00",
    "viewCount": 121,
    "favoriteCount": 35,
    "orderCount": 18,
    "ratingScore": 4.8,
    "status": 1,
    "createTime": "2025-11-18 10:00:00"
  },
  
}
```

---

### 2.4 更新房源

**接口**: `PUT /homestead/update/{id}`

**是否需要认证**: 是

**路径参数**:
- `id`: 房源ID

**请求参数**: 同发布房源接口

---

### 2.5 删除房源

**接口**: `DELETE /homestead/delete/{id}`

**是否需要认证**: 是

**路径参数**:
- `id`: 房源ID

---

### 2.6 上架/下架房源

**接口**: `PUT /homestead/status/{id}`

**是否需要认证**: 是

**路径参数**:
- `id`: 房源ID

**请求参数**:
```
PUT /homestead/status/1?status=1
```

| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| status | integer | 是 | 状态: 0-下架 1-上架 2-维护中 |

---

### 2.7 获取我发布的房源列表

**接口**: `GET /homestead/my`

**是否需要认证**: 是

**请求参数**:
```
GET /homestead/my?pageNum=1&pageSize=10
```

---

## 三、订单管理接口

### 3.1 创建订单

**接口**: `POST /order/create`

**是否需要认证**: 是

**请求参数**:
```json
{
  "homesteadId": 1,
  "checkInDate": "2025-12-01",
  "checkOutDate": "2025-12-03",
  "guestCount": 2,
  "guestName": "张三",
  "guestPhone": "13800138000",
  "specialRequest": "需要婴儿床"
}
```

---

### 3.2 取消订单

**接口**: `PUT /order/cancel/{orderId}`

**是否需要认证**: 是

**请求参数**:
```
PUT /order/cancel/1?reason=行程变更
```

---

## 四、收藏管理接口

### 4.1 收藏房源

**接口**: `POST /favorite/add/{homesteadId}`

**是否需要认证**: 是

**路径参数**:
- `homesteadId`: 房源ID

---

### 4.2 取消收藏

**接口**: `DELETE /favorite/remove/{homesteadId}`

**是否需要认证**: 是

**路径参数**:
- `homesteadId`: 房源ID

---

## 五、评价管理接口

### 5.1 提交评价

**接口**: `POST /review/submit/{orderId}`

**是否需要认证**: 是

**路径参数**:
- `orderId`: 订单ID

---

### 5.2 获取房源评价列表

**接口**: `GET /review/list/{homesteadId}`

**是否需要认证**: 否

**路径参数**:
- `homesteadId`: 房源ID

**请求参数**:
```
GET /review/list/1?pageNum=1&pageSize=10
```

---

## 附录

### 房型枚举
- 1: 整套房
- 2: 独立房间
- 3: 合住房间

### 用户类型枚举
- 1: 普通用户
- 2: 房东
- 3: 管理员

### 订单状态枚举
- 1: 待支付
- 2: 已支付
- 3: 已入住
- 4: 已完成
- 5: 已取消
- 6: 已退款

### 性别枚举
- 0: 未知
- 1: 男
- 2: 女
