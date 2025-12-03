# 部署指南

## 本地开发环境部署

### 1. 环境准备

#### 1.1 安装JDK 17
```bash
# 检查Java版本
java -version

# 应该输出类似: java version "17.0.x"
```

#### 1.2 安装MySQL 8.0+
```bash
# Ubuntu/Debian
sudo apt-get install mysql-server

# CentOS/RHEL
sudo yum install mysql-server

# 启动MySQL
sudo systemctl start mysql
sudo systemctl enable mysql
```

#### 1.3 安装Redis
```bash
# Ubuntu/Debian
sudo apt-get install redis-server

# CentOS/RHEL
sudo yum install redis

# 启动Redis
sudo systemctl start redis
sudo systemctl enable redis
```

#### 1.4 安装Maven
```bash
# Ubuntu/Debian
sudo apt-get install maven

# CentOS/RHEL
sudo yum install maven

# 验证安装
mvn -version
```

### 2. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 执行SQL脚本
source /path/to/homestead-booking-platform/sql/schema.sql

# 或者直接导入
mysql -u root -p < /path/to/homestead-booking-platform/sql/schema.sql
```

验证数据库创建成功:
```sql
USE homestead_booking;
SHOW TABLES;
```

### 3. 配置文件修改

编辑 `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/homestead_booking?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root          # 修改为你的MySQL用户名
    password: your_password # 修改为你的MySQL密码

  data:
    redis:
      host: localhost
      port: 6379
      password:             # 如果Redis设置了密码,在这里填写

homestead:
  jwt:
    secret: your-custom-secret-key-change-this-in-production
```

### 4. 编译和运行

#### 方式1: 使用Maven运行
```bash
cd /path/to/homestead-booking-platform

# 清理并编译
mvn clean compile

# 运行项目
mvn spring-boot:run
```

#### 方式2: 打包后运行
```bash
# 打包
mvn clean package

# 运行jar包
java -jar target/homestead-booking-platform-1.0.0.jar
```

### 5. 验证部署

访问以下地址验证服务是否正常:

```bash
# 健康检查(如果配置了actuator)
curl http://localhost:8080/api/actuator/health

# 测试发送验证码接口
curl -X POST http://localhost:8080/api/user/sendVerifyCode?phone=13800138000
```

---

## 生产环境部署

### 1. 服务器要求

- **操作系统**: Linux (推荐Ubuntu 20.04 或 CentOS 8)
- **CPU**: 2核及以上
- **内存**: 4GB及以上
- **磁盘**: 50GB及以上
- **带宽**: 5Mbps及以上

### 2. 安装依赖软件

```bash
# 更新系统
sudo apt-get update

# 安装JDK 17
sudo apt-get install openjdk-17-jdk

# 安装MySQL
sudo apt-get install mysql-server

# 安装Redis
sudo apt-get install redis-server

# 安装Nginx(可选,用于反向代理)
sudo apt-get install nginx
```

### 3. MySQL配置优化

编辑 `/etc/mysql/mysql.conf.d/mysqld.cnf`:

```ini
[mysqld]
# 字符集配置
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci

# 性能优化
max_connections = 1000
innodb_buffer_pool_size = 1G
innodb_log_file_size = 256M

# 时区设置
default-time-zone = '+8:00'
```

重启MySQL:
```bash
sudo systemctl restart mysql
```

### 4. Redis配置优化

编辑 `/etc/redis/redis.conf`:

```conf
# 绑定地址(生产环境建议只绑定内网)
bind 127.0.0.1

# 设置密码
requirepass your_redis_password

# 持久化配置
save 900 1
save 300 10
save 60 10000

# 最大内存
maxmemory 512mb
maxmemory-policy allkeys-lru
```

重启Redis:
```bash
sudo systemctl restart redis
```

### 5. 打包部署

```bash
# 在本地打包
mvn clean package -DskipTests

# 上传到服务器
scp target/homestead-booking-platform-1.0.0.jar user@your-server:/opt/homestead/

# 上传配置文件
scp src/main/resources/application.yml.bak.bak user@your-server:/opt/homestead/
```

### 6. 创建systemd服务

创建 `/etc/systemd/system/homestead.service`:

```ini
[Unit]
Description=Homestead Booking Platform
After=syslog.target network.target mysql.service redis.service

[Service]
Type=simple
User=homestead
WorkingDirectory=/opt/homestead
ExecStart=/usr/bin/java -jar \
    -Xms512m -Xmx1024m \
    -Dspring.config.location=/opt/homestead/application.yml \
    /opt/homestead/homestead-booking-platform-1.0.0.jar

Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启动服务:
```bash
# 创建用户
sudo useradd -r -s /bin/false homestead
sudo chown -R homestead:homestead /opt/homestead

# 重载systemd
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start homestead

# 开机自启
sudo systemctl enable homestead

# 查看状态
sudo systemctl status homestead

# 查看日志
sudo journalctl -u homestead -f
```

### 7. Nginx反向代理配置(可选)

创建 `/etc/nginx/sites-available/homestead`:

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 日志配置
    access_log /var/log/nginx/homestead-access.log;
    error_log /var/log/nginx/homestead-error.log;

    # 反向代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # 超时设置
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # 文件上传大小限制
    client_max_body_size 50M;
}
```

启用配置:
```bash
sudo ln -s /etc/nginx/sites-available/homestead /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### 8. 配置HTTPS(可选)

使用Let's Encrypt免费SSL证书:

```bash
# 安装certbot
sudo apt-get install certbot python3-certbot-nginx

# 获取证书
sudo certbot --nginx -d your-domain.com

# 自动续期
sudo certbot renew --dry-run
```

### 9. 防火墙配置

```bash
# 允许HTTP和HTTPS
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp

# 如果直接访问Spring Boot端口
sudo ufw allow 8080/tcp

# 启用防火墙
sudo ufw enable
```

---

## Docker部署(推荐)

### 1. 创建Dockerfile

在项目根目录创建 `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/homestead-booking-platform-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Xms512m", "-Xmx1024m", "app.jar"]
```

### 2. 创建docker-compose.yml

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: homestead-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: homestead_booking
      MYSQL_USER: homestead
      MYSQL_PASSWORD: homestead_password
      TZ: Asia/Shanghai
    ports:
      - "3306:3306"
    volumes:
      - ./sql/schema.sql:/docker-entrypoint-initdb.d/schema.sql
      - mysql-data:/var/lib/mysql
    networks:
      - homestead-network

  redis:
    image: redis:7-alpine
    container_name: homestead-redis
    command: redis-server --requirepass redis_password
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    networks:
      - homestead-network

  app:
    build: .
    container_name: homestead-app
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/homestead_booking?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
      SPRING_DATASOURCE_USERNAME: homestead
      SPRING_DATASOURCE_PASSWORD: homestead_password
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PASSWORD: redis_password
    depends_on:
      - mysql
      - redis
    networks:
      - homestead-network

volumes:
  mysql-data:
  redis-data:

networks:
  homestead-network:
    driver: bridge
```

### 3. 构建和运行

```bash
# 打包项目
mvn clean package -DskipTests

# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f app

# 停止服务
docker-compose down

# 停止并删除数据
docker-compose down -v
```

---

## 监控和日志

### 1. 日志配置

修改 `application.yml`:

```yaml
logging:
  level:
    com.homestead.booking: info
  file:
    name: /var/log/homestead/app.log
    max-size: 100MB
    max-history: 30
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{50} - %msg%n"
```

### 2. 集成Spring Boot Actuator

添加依赖到 `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

配置:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
```

访问监控端点:
- 健康检查: `http://your-domain/api/actuator/health`
- 应用信息: `http://your-domain/api/actuator/info`
- 性能指标: `http://your-domain/api/actuator/metrics`

---

## 常见问题

### 1. 端口被占用

```bash
# 查找占用8080端口的进程
lsof -i:8080

# 或者
netstat -tunlp | grep 8080

# 杀死进程
kill -9 <PID>
```

### 2. 内存溢出

增加JVM内存:
```bash
java -jar -Xms1g -Xmx2g homestead-booking-platform-1.0.0.jar
```

### 3. 数据库连接失败

检查:
- MySQL服务是否启动: `systemctl status mysql`
- 端口是否开放: `telnet localhost 3306`
- 用户名密码是否正确
- 数据库是否存在: `SHOW DATABASES;`

### 4. Redis连接失败

检查:
- Redis服务是否启动: `systemctl status redis`
- 端口是否开放: `telnet localhost 6379`
- 密码是否正确

---

## 性能优化建议

1. **数据库优化**
   - 为常用查询字段添加索引
   - 使用连接池(HikariCP)
   - 定期清理过期数据

2. **缓存优化**
   - 使用Redis缓存热点数据
   - 设置合理的缓存过期时间
   - 使用布隆过滤器防止缓存穿透

3. **应用优化**
   - 使用异步处理耗时操作
   - 启用GZIP压缩
   - 使用CDN加速静态资源

4. **服务器优化**
   - 使用负载均衡(Nginx)
   - 配置合理的JVM参数
   - 监控系统资源使用情况

---

## 备份策略

### 数据库备份

```bash
# 创建备份脚本 /opt/scripts/backup-mysql.sh
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/backup/mysql"
DB_NAME="homestead_booking"

mkdir -p $BACKUP_DIR
mysqldump -u root -p$MYSQL_PASSWORD $DB_NAME | gzip > $BACKUP_DIR/${DB_NAME}_${DATE}.sql.gz

# 删除7天前的备份
find $BACKUP_DIR -name "*.sql.gz" -mtime +7 -delete
```

添加到crontab:
```bash
# 每天凌晨2点备份
0 2 * * * /opt/scripts/backup-mysql.sh
```

---

## 安全建议

1. 修改默认密码
2. 使用HTTPS
3. 限制IP访问(如管理后台)
4. 定期更新依赖包
5. 配置防火墙
6. 启用SQL注入防护
7. 限制文件上传类型和大小
8. 使用强密码策略

---

## 联系支持

如遇到部署问题,请参考:
- 项目文档: README.md
- API文档: API.md
- 提交Issue: GitHub Issues
