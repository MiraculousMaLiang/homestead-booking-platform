# 民宿管理与推荐平台后端系统

## 项目简介

基于SpringBoot + MySQL + MyBatis Plus开发的民宿管理与推荐平台后端系统,为鸿蒙前端应用提供RESTful API接口。

## 技术栈

- **后端框架**: Spring Boot 3.2.0
- **持久层框架**: MyBatis Plus 3.5.5
- **数据库**: MySQL 8.0+
- **缓存**: Redis
- **安全认证**: JWT
- **工具类**: Hutool
- **云服务**: 阿里云OSS(文件存储)、阿里云短信(验证码)

## 项目结构

```
homestead-booking-platform/
├── sql/                           # SQL脚本
│   └── schema.sql                 # 数据库建表脚本
├── src/main/
│   ├── java/com/homestead/booking/
│   │   ├── common/                # 公共类
│   │   │   ├── Result.java        # 统一响应结果
│   │   │   ├── ResultCode.java    # 响应状态码
│   │   │   └── PageResult.java    # 分页结果
│   │   ├── config/                # 配置类
│   │   │   ├── MyBatisPlusConfig.java
│   │   │   └── WebMvcConfig.java
│   │   ├── controller/            # 控制器层
│   │   │   ├── UserController.java        # 用户管理
│   │   │   ├── HomesteadController.java   # 房源管理
│   │   │   ├── OrderController.java       # 订单管理
│   │   │   ├── ReviewController.java      # 评价管理
│   │   │   └── FavoriteController.java    # 收藏管理
│   │   ├── dto/                   # 数据传输对象
│   │   ├── vo/                    # 视图对象
│   │   ├── entity/                # 实体类
│   │   │   ├── User.java          # 用户实体
│   │   │   ├── Homestead.java     # 房源实体
│   │   │   ├── Order.java         # 订单实体
│   │   │   ├── Review.java        # 评价实体
│   │   │   ├── Favorite.java      # 收藏实体
│   │   │   └── Notification.java  # 通知实体
│   │   ├── mapper/                # 数据访问层
│   │   ├── service/               # 业务逻辑层
│   │   │   └── impl/              # 业务实现类
│   │   ├── interceptor/           # 拦截器
│   │   │   └── JwtInterceptor.java
│   │   ├── exception/             # 异常处理
│   │   │   ├── BusinessException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   └── utils/                 # 工具类
│   │       ├── JwtUtil.java       # JWT工具
│   │       └── RedisUtil.java     # Redis工具
│   └── resources/
│       └── application.yml        # 应用配置
└── pom.xml                        # Maven配置
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 5.0+

### 2. 数据库初始化

```bash
# 创建数据库并导入SQL脚本
mysql -u root -p < sql/schema.sql
```

### 3. 配置文件修改

编辑 `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/homestead_booking
    username: your_username
    password: your_password

  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password

homestead:
  jwt:
    secret: your-jwt-secret-key
```

### 4. 启动项目

```bash
# 使用Maven启动
mvn spring-boot:run

# 或者先编译再运行
mvn clean package
java -jar target/homestead-booking-platform-1.0.0.jar
```

项目启动成功后,访问: http://localhost:8080/api

## 核心功能模块

### 1. 用户管理模块

- ✅ 用户注册(手机号+验证码)
- ✅ 用户登录(密码登录/验证码登录)
- ✅ 获取用户信息
- ✅ 更新用户信息
- ✅ 修改密码
- ✅ 发送短信验证码

### 2. 房源管理模块

- ✅ 发布房源
- ✅ 更新房源信息
- ✅ 删除房源(逻辑删除)
- ✅ 上架/下架房源
- ✅ 获取房源详情
- ✅ 查询房源列表(支持多条件筛选和排序)
- ✅ 获取我发布的房源列表

### 3. 预订管理模块

- ⏳ 创建订单
- ⏳ 取消订单
- ⏳ 订单支付
- ⏳ 订单查询
- ⏳ 订单状态管理

### 4. 评价反馈模块

- ⏳ 提交评价
- ⏳ 查询评价列表
- ⏳ 房东回复评价
- ⏳ 评价点赞

### 5. 收藏功能

- ⏳ 收藏房源
- ⏳ 取消收藏
- ⏳ 我的收藏列表

✅ 已完成  ⏳ 待完成

## API接口文档

### 用户相关接口

#### 1. 用户注册
```
POST /api/user/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "Test123456",
  "phone": "13800138000",
  "verifyCode": "123456",
  "email": "test@example.com",
  "nickname": "测试用户"
}
```

#### 2. 用户登录
```
POST /api/user/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "Test123456",
  "loginType": 1
}

响应:
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "username": "testuser",
      "nickname": "测试用户",
      ...
    }
  },
  "timestamp": 1700000000000
}
```

#### 3. 发送验证码
```
POST /api/user/sendVerifyCode?phone=13800138000
```

#### 4. 获取用户信息
```
GET /api/user/info
Authorization: Bearer {token}
```

#### 5. 更新用户信息
```
PUT /api/user/update
Authorization: Bearer {token}
Content-Type: application/json

{
  "nickname": "新昵称",
  "avatar": "https://example.com/avatar.jpg",
  "gender": 1,
  "birthday": "1990-01-01"
}
```

### 房源相关接口

#### 1. 发布房源
```
POST /api/homestead/publish
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "温馨两居室",
  "description": "位于市中心,交通便利",
  "province": "浙江省",
  "city": "杭州市",
  "district": "西湖区",
  "address": "文一路100号",
  "roomType": 1,
  "maxGuests": 4,
  "pricePerDay": 388.00,
  "coverImage": "https://example.com/cover.jpg"
}
```

#### 2. 查询房源列表
```
POST /api/homestead/list
Content-Type: application/json

{
  "city": "杭州市",
  "minPrice": 100,
  "maxPrice": 500,
  "roomType": 1,
  "sortField": "price",
  "sortOrder": "asc",
  "pageNum": 1,
  "pageSize": 10
}

响应:
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 10,
    "records": [...]
  }
}
```

#### 3. 获取房源详情
```
GET /api/homestead/detail/{id}
```

#### 4. 更新房源
```
PUT /api/homestead/update/{id}
Authorization: Bearer {token}
```

#### 5. 删除房源
```
DELETE /api/homestead/delete/{id}
Authorization: Bearer {token}
```

#### 6. 上架/下架房源
```
PUT /api/homestead/status/{id}?status=1
Authorization: Bearer {token}
```

### 订单相关接口

#### 1. 创建订单
```
POST /api/order/create
Authorization: Bearer {token}
Content-Type: application/json

{
  "homesteadId": 1,
  "checkInDate": "2025-12-01",
  "checkOutDate": "2025-12-03",
  "guestCount": 2,
  "guestName": "张三",
  "guestPhone": "13800138000"
}
```

## 数据库设计

详见 `sql/schema.sql` 文件,主要包括以下表:

- `user` - 用户表
- `homestead` - 房源表
- `homestead_image` - 房源图片表
- `homestead_facility` - 房源设施表
- `order` - 订单表
- `payment` - 支付记录表
- `review` - 评价表
- `favorite` - 收藏表
- `browse_history` - 浏览记录表
- `user_preference` - 用户偏好表
- `notification` - 消息通知表
- `chat_message` - 聊天消息表
- `coupon` - 优惠券表
- `user_coupon` - 用户优惠券表

## 安全认证

项目使用JWT(JSON Web Token)进行用户认证:

1. 用户登录成功后,服务器生成JWT Token返回给客户端
2. 客户端在后续请求中携带Token: `Authorization: Bearer {token}`
3. 服务器通过JwtInterceptor拦截器验证Token有效性
4. Token有效期为7天

**注意**: 以下接口无需Token验证:
- `/user/register` - 用户注册
- `/user/login` - 用户登录
- `/user/sendVerifyCode` - 发送验证码
- `/homestead/list` - 房源列表
- `/homestead/detail/**` - 房源详情

## 配置说明

### JWT配置
```yaml
homestead:
  jwt:
    secret: homestead-booking-platform-secret-key-2025  # JWT密钥
    expiration: 604800000  # 过期时间(7天)
    header: Authorization  # 请求头名称
    prefix: Bearer  # Token前缀
```

### 阿里云OSS配置
```yaml
homestead:
  oss:
    endpoint: oss-cn-hangzhou.aliyuncs.com
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    bucket-name: homestead-booking
```

### 阿里云短信配置
```yaml
homestead:
  sms:
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    sign-name: 民宿预订平台
    template-code: SMS_123456789
```

## 开发说明

### 代码规范

- 使用Lombok简化实体类代码
- 统一使用Result类封装响应结果
- 使用GlobalExceptionHandler统一处理异常
- 使用@Valid注解进行参数校验
- 遵循RESTful API设计规范

### 响应格式

所有接口统一响应格式:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1700000000000
}
```

### 分页格式

分页查询统一返回格式:
```json
{
  "total": 100,
  "pageNum": 1,
  "pageSize": 10,
  "pages": 10,
  "records": []
}
```

## 常见问题

### 1. 验证码在哪里查看?

开发环境下,验证码会输出到控制台:
```
===========================
验证码: 123456
===========================
```

生产环境需配置阿里云短信服务。

### 2. 如何测试接口?

推荐使用Postman或ApiPost工具:
1. 先调用注册/登录接口获取Token
2. 在后续请求的Header中添加: `Authorization: Bearer {token}`

### 3. MySQL连接失败?

检查以下配置:
- MySQL服务是否启动
- 数据库名称是否正确(homestead_booking)
- 用户名密码是否正确
- 时区设置: `serverTimezone=Asia/Shanghai`

### 4. Redis连接失败?

检查:
- Redis服务是否启动: `redis-server`
- 端口是否正确(默认6379)
- 密码配置是否正确

## 后续开发计划

- [ ] 完善订单支付功能(接入微信/支付宝支付)
- [ ] 实现智能推荐算法
- [ ] 添加消息推送功能
- [ ] 实现实时聊天功能(WebSocket)
- [ ] 添加数据统计报表
- [ ] 完善单元测试
- [ ] 集成Swagger API文档
- [ ] Docker容器化部署

## 许可证

MIT License

## 联系方式

- 项目作者: homestead
- 创建日期: 2025-11-18
- 版本: v1.0.0

---

**注意**: 本项目为学习示例项目,生产环境使用需要进一步完善和测试。
