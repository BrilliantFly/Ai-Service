# Know-Boot 双模式运行配置指南

## 概述

know-boot 支持 **单机模式 (standalone)** 和 **微服务模式 (microservice)** 两种运行方式。本文档说明如何配置、构建和运行两种模式，以及注意事项。

---

## 1. 架构对比

| 维度 | 单机模式 | 微服务模式 |
|------|---------|-----------|
| 服务注册发现 | 无（直连） | Nacos |
| 配置中心 | 本地 YAML | Nacos Config |
| 服务间调用 | 无（单体） | OpenFeign |
| 网关 | 无（Nginx 直转） | Spring Cloud Gateway |
| 限流熔断 | 无 | Sentinel |
| 分布式事务 | 无 | Seata |
| 负载均衡 | 无 | Spring Cloud Loadbalancer |

---

## 2. 快速启动

### 2.1 单机模式（默认）

```bash
# Maven 构建（默认 standalone profile 激活）
cd know-boot
mvn clean install

# 运行 system 模块
cd know-boot-system
mvn spring-boot:run -P standalone
```

访问：http://localhost:8085

### 2.2 微服务模式

```bash
# 先构建基础模块 + cloud starter
cd know-boot
mvn clean install -P SpringCloud

# 构建 system 模块（激活 microservice profile）
cd know-boot-system
mvn clean package -P microservice

# 启动（需要先启动 Nacos）
java -jar target/know-boot-system.jar --spring.profiles.active=microservice
```

---

## 3. Maven Profile 详解

### 3.1 system/pom.xml 中的 profiles

```xml
<!-- 单机模式（默认激活） -->
<profile>
    <id>standalone</id>
    <activation>
        <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
        <profile.name>standalone</profile.name>
    </properties>
    <!-- 不引入 cloud-know-boot-starter -->
</profile>

<!-- 微服务模式 -->
<profile>
    <id>microservice</id>
    <properties>
        <profile.name>microservice</profile.name>
    </properties>
    <dependencies>
        <dependency>
            <groupId>com.know</groupId>
            <artifactId>cloud-know-boot-starter</artifactId>
        </dependency>
    </dependencies>
</profile>
```

**关键区别**：`microservice` profile 会引入 `cloud-know-boot-starter`，该 starter 包含 Nacos、Sentinel、Seata、Feign、LoadBalancer 等全套 Spring Cloud 依赖。

### 3.2 打包主类配置

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <mainClass>com.know.knowboot.KnowBootSystemApplication</mainClass>
    </configuration>
</plugin>
```

> **注意**：POM 中 `mainClass` **始终指向** `KnowBootSystemApplication`。微服务模式下需手动使用 `MicroserviceApplication` 启动，或用构建脚本覆盖。

---

## 4. Java 启动类对比

### 4.1 KnowBootSystemApplication（单机模式）

```java
@SpringBootApplication(exclude = {
    RedisRepositoriesAutoConfiguration.class,
    SecurityAutoConfiguration.class,
    OAuth2ResourceServerAutoConfiguration.class
})
@ComponentScan(basePackages = "com.know", excludeFilters = {
    @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "com\\.know\\.knowboot\\.feign\\..*")  // 排除 Feign 客户端
})
public class KnowBootSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowBootSystemApplication.class, args);
    }
}
```

### 4.2 MicroserviceApplication（微服务模式）

```java
@SpringBootApplication(exclude = {
    RedisRepositoriesAutoConfiguration.class,
    SecurityAutoConfiguration.class
})
@EnableDiscoveryClient       // 启用 Nacos 服务发现
@Import(MicroserviceConfiguration.class)
public class MicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
    }
}
```

**区别总结**：

| 方面 | KnowBootSystemApplication | MicroserviceApplication |
|------|--------------------------|------------------------|
| 扫描范围 | `com.know` | `com.know` |
| Feign 组件 | ❌ 排除 | ✅ 保留 |
| `@EnableDiscoveryClient` | ❌ | ✅ |
| OAuth2 排除 | ✅ | ❌ |

### 4.3 Spring Cloud 条件配置

```java
// CloudNacosConfig.java — 仅在 know.mode=microservice 时加载
@Configuration
@ConditionalOnProperty(name = "know.mode", havingValue = "microservice")
public class CloudNacosConfig {
    private Discovery discovery;
    private Config config;
}
```

---

## 5. YAML 配置详解

### 5.1 单机模式 (application-standalone.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/know_boot?useSSL=false
    username: root
    password: 123456
  redis:
    host: localhost
    port: 6379

know:
  mode: standalone                  # 显式声明单机模式
  redisson:
    enabled: false                  # 默认禁用 Redisson
  ignore:
    urls:
      - /sys/user/**                # 无需 token 校验的路径
      - /schedule/**
      - /habit/**

seata:
  enabled: false                    # 分布式事务禁用

feign:
  circuitbreaker:
    enabled: false
  loadbalancer:
    enabled: false

server:
  port: 8085
  servlet:
    context-path: /know-boot
```

### 5.2 微服务模式 (application-microservice.yml)

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 39.98.108.121:10006
      config:
        server-addr: 39.98.108.121:10006
        namespace: ab16776b-dae9-474e-8943-7bbbc627b637
        group: DEFAULT_GROUP
        file-extension: yaml
  datasource:
    url: jdbc:mysql://101.37.83.88:3306/flyp_dev?useSSL=false
    username: root
    password: Wanglei!@#123
  redis:
    host: 101.37.83.88
    port: 6379

know:
  mode: microservice                # 微服务模式

seata:
  enabled: true                     # 启用分布式事务

feign:
  circuitbreaker:
    enabled: true
  loadbalancer:
    enabled: true
```

---

## 6. Nginx 路由配置

### 6.1 单机模式

请求流向：`Uni-App → Nginx → know-boot-system (单体)`

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location /api/ {
        rewrite ^/api/(.*)$ /know-boot/$1 break;
        proxy_pass http://192.168.1.100:8085;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # camera 服务（独立端口）
    location /camera/ {
        proxy_pass http://192.168.1.100:8088;
        proxy_set_header Host $host;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }

    # 前端静态资源
    location / {
        root /var/www/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
}
```

### 6.2 微服务模式

请求流向：`Uni-App → Nginx → Spring Cloud Gateway → 各微服务`

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location /api/ {
        rewrite ^/api/(.*)$ /$1 break;
        proxy_pass http://192.168.1.100:9999;  # 转发到 Gateway
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location / {
        root /var/www/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
}
```

---

## 6.1 Camera 服务 Nginx 配置

摄像头服务独立运行在 **8088** 端口，通过 Nginx 代理访问：

```nginx
location /camera/ {
    rewrite ^/camera/(.*)$ /know-boot/$1 break;
    proxy_pass http://127.0.0.1:8088;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_read_timeout 120s;
}
```

URL 转换示例：
```
请求: /camera/device/list
Nginx: rewrite → /know-boot/camera/device/list
后端:   context-path=/know-boot + @RequestMapping("/camera/device") → /device/list ✅
```

### 6.2 Vite 开发代理

```typescript
// build/vite/proxy.ts
ret['/camera'] = {
    target: 'http://localhost:8088',
    changeOrigin: true,
    rewrite: path => path.replace(/^\/camera/, '/know-boot/camera')
}
```

### 6.3 Camera 双模式切换

| 配置项 | 单机模式 | 微服务模式 |
|--------|---------|-----------|
| 端口 | 8088 | 8088 |
| Spring Profile | `standalone` | `microservice` |
| Nacos | ❌ | ✅ |
| Seata | ❌ | ✅ |
| Feign | ❌ | ✅ |
| 启动命令 | `mvn spring-boot:run -Dspring-boot.run.profiles=standalone` | `mvn spring-boot:run -Dspring-boot.run.profiles=microservice` |

### 6.4 Camera 认证机制

摄像头模块使用 Token 认证，与主系统共享 Redis 会话：

1. 请求携带 `token` 头（或 `Authorization: Bearer xxx`）
2. `TokenAuthInterceptor` 从 Redis 中查找 Sa-Token 会话
3. 提取 `loginId` 作为当前用户 ID
4. 若 Token 无效或 Redis 不可用，默认使用用户 ID `1L`

认证文件位置：
- `know-boot-camera/.../auth/TokenAuthInterceptor.java` — Token 验证拦截器
- `know-boot-camera/.../auth/CameraAuthContext.java` — 用户 ID 提取工具
- `know-boot-camera/.../config/WebMvcConfig.java` — 拦截器注册

### 6.5 Camera 端口变更

```
旧: server.port = 8085  (与 know-boot-system 冲突)
新: server.port = 8088  (不冲突)
```

> **注意**: 启动 camera 前确保 know-boot-system 的 Redis 正在运行，否则 Token 验证会降级为默认用户。

## 7. 前端（Uni-App）注意事项

### 7.1 请求前缀配置

Uni-App 使用 `/api` 前缀，通过 `request.js` 中的 `urlPrefix` 自动添加：

```javascript
// request.js
const urlPrefix = import.meta.env.VITE_API_BASE || '/api'

// 请求示例
// GET /api/plan/list → Nginx → /know-boot/plan/list (单机)
// GET /api/plan/list → Nginx → gateway /plan/list (微服务)
```

**已验证**：`plan` 和 `knowledge` 模块的 API 路径在两种模式下均兼容。

### 7.2 Camera 服务直连问题

```javascript
// camera/api/request.js — ⚠️ 硬编码了 localhost
const BASE_URL = 'http://localhost:8085/know-boot'
```

**问题**：部署到非本机环境会失效。
**修复方案**：通过环境变量配置，统一走 Nginx 代理。

### 7.3 微信小程序白名单

| 模式 | 需要加入白名单的地址 |
|------|-------------------|
| 单机 | Nginx 公网 IP / 域名 |
| 微服务 | Nginx 公网 IP / 域名 |
| 本地开发 | `localhost:8085`（单机）或 `localhost:9999`（微服务 gateway） |

---

## 8. 切换模式步骤清单

### 单机 → 微服务

- [ ] 1. 启动 Nacos（配置中心 + 服务发现）
- [ ] 2. 启动 Redis、MySQL 等基础服务
- [ ] 3. `system/pom.xml` 使用 `-P microservice` 打包
- [ ] 4. 使用 `MicroserviceApplication` 作为启动类
- [ ] 5. YAML 中设置 `know.mode: microservice`
- [ ] 6. Nacos 配置中心中推送对应环境的配置文件
- [ ] 7. 如有其他微服务模块，启动对应服务
- [ ] 8. 确保 Feign 接口路径正确
- [ ] 9. 更新 Nginx 指向 Gateway 端口
- [ ] 10. 更新小程序白名单

### 微服务 → 单机

- [ ] 1. `system/pom.xml` 使用 `-P standalone` 打包（或去掉 profile 用默认）
- [ ] 2. 使用 `KnowBootSystemApplication` 作为启动类
- [ ] 3. YAML 中设置 `know.mode: standalone`
- [ ] 4. 注释或移除 `@EnableDiscoveryClient`
- [ ] 5. 禁用 Seata（`seata.enabled: false`）
- [ ] 6. 禁用 Feign（`feign.circuitbreaker.enabled: false`）
- [ ] 7. 更新 Nginx 直连 system 服务端口
- [ ] 8. 更新小程序白名单

---

## 9. 常见问题

### Q1: 微服务模式下 `know.mode` 不起作用？

确保 `@ConditionalOnProperty` 配置类上的注解正确，且 YAML 中 `know.mode`
的值与 `havingValue` 完全匹配（区分大小写）。

### Q2: 单机模式下报 Feign 相关错误？

原因：`cloud-know-boot-starter` 被引入但 Nacos 未启动。
解决：确认使用 `-P standalone` 构建，或检查 `KnowBootSystemApplication`
的 `@ComponentScan` 已正确排除 `feign` 包。

### Q3: 微服务模式下 Nacos 注册失败？

检查项：
- Nacos 服务是否正常运行
- `spring.cloud.nacos.discovery.server-addr` 是否正确
- 网络是否能连通 Nacos 服务端
- 是否有防火墙拦截

### Q4: 前端接口 404？

- 单机模式：确认 Nginx 路径转换 `/api/* → /know-boot/*`
- 微服务模式：确认 Nginx 指向 Gateway，Gateway 路由规则正确
- 两种模式下都确认 `request.js` 中的 `urlPrefix` 是否为 `/api`
