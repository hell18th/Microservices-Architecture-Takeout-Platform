## 微服务架构外卖平台

**技术架构：** Spring Cloud Alibaba + Spring Boot 3.5.9 + MyBatis + MySQL + Redis + Nacos + Gateway + OpenFeign

**项目描述：**
这是一个基于微服务架构的综合性外卖订餐平台，采用前后端分离设计，包含用户端和管理端两大模块。系统通过 Spring Cloud Gateway 作为统一网关入口，实现了 14 个独立微服务模块的协同工作，支持菜品管理、套餐管理、订单处理、购物车、地址管理、店铺管理、分类管理、员工管理、数据报表等核心业务功能。

**核心职责与技术亮点：**

1. **微服务架构设计与实现**
   - 基于 Spring Cloud Alibaba 构建微服务架构，使用 Nacos 作为服务注册与配置中心
   - 通过 Spring Cloud Gateway 实现统一网关路由，配置 10+ 条动态路由规则
   - 采用 OpenFeign 实现服务间远程调用，定义 6 个 Feign 客户端接口 (Address/Dish/Order/SetMeal/ShoppingCar/UserClient)

2. **公共模块封装**
   - 设计 sky-common 公共模块，封装通用工具类、异常处理、拦截器、注解等
   - 实现 JWT 令牌认证、AOP 日志切面、Redis 缓存、OSS 对象存储等通用功能
   - 创建 sky-api 模块统一管理 Feign 客户端和 DTO 数据传输对象

3. **数据库与持久层设计**
   - 使用 MyBatis 作为 ORM 框架，配置 PageHelper 分页插件
   - 设计多表关联查询，优化 SQL 性能
   - 实现 MySQL 主从数据库连接配置

4. **核心业务功能开发**
   - **订单服务**: 集成 WebSocket 实现订单状态实时推送，开发定时任务处理超时订单
   - **菜品/套餐服务**: 实现多级分类管理、菜品上架/下架、套餐组合等业务逻辑
   - **购物车服务**: 基于 Redis 实现高性能购物车数据存储
   - **报表服务**: 使用 POI 实现数据导出功能，提供营业额、用户量等统计分析

5. **安全与权限控制**
   - 实现双路径认证机制 (adminPath/userPath)，区分管理员和普通用户权限
   - 配置 JWT 拦截器，排除登录等公开接口
   - 基于 ThreadLocal 实现用户上下文传递

6. **配置管理与环境隔离**
   - 使用多环境配置 (application-dev.yml)，实现开发/生产环境隔离
   - 统一配置数据库、Redis、Nacos 等中间件连接信息

**技术栈:**
- 后端：Java 21、Spring Boot 3.5.9、Spring Cloud 2025.0.0、Spring Cloud Alibaba 2025.0.0.0
- 数据库：MySQL、MyBatis 3.0.5、PageHelper 1.4.7
- 中间件：Redis、Nacos、Gateway WebFlux
- 工具：Lombok、JWT 0.9.1、Aliyun OSS 3.17.4、POI 5.4.1
- 通信：OpenFeign、WebSocket、HTTP Client

**项目成果：**
- 成功实现 14 个微服务模块的解耦与协作
- 通过网关统一入口，提升系统安全性和可维护性
- 采用 Redis 缓存和数据库优化，显著提升系统响应速度
- 代码结构清晰，遵循阿里巴巴 Java 开发规范
