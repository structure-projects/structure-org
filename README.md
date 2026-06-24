# structure-org

组织中心服务，提供组织、部门、成员的全生命周期管理能力。

## 技术栈

| 分类 | 技术 | 版本 |
|-----|------|------|
| 语言 | Java | 21 |
| 框架 | Spring Boot | 3.2.x |
| 数据库 | MySQL | 8.0+ |
| ORM | MyBatis Plus | 3.5.x |
| 构建工具 | Maven | 3.9+ |

## 项目结构

```
structure-org/
├── structure-org-api/        # 控制层（对外暴露REST API）
├── structure-org-biz/        # 业务层（核心业务逻辑）
├── structure-org-common/     # 公共层（DTO、VO、枚举、异常等）
└── structure-org-dependencies/ # 依赖管理（统一版本控制）
```

## 模块职责

| 模块 | 职责 | 包含包 |
|-----|------|--------|
| **structure-org-api** | 控制层，处理HTTP请求 | `controller/`, `OrgApplication.java` |
| **structure-org-biz** | 业务逻辑层 | `service/`, `manager/`, `mapper/`, `entity/`, `assembler/` |
| **structure-org-common** | 公共组件 | `dto/`, `vo/`, `query/`, `enums/`, `exception/`, `constant/` |
| **structure-org-dependencies** | Maven依赖管理 | `pom.xml` |

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.9+
- MySQL 8.0+

### 构建项目

```bash
# 进入项目目录
cd structure-org

# 构建项目
mvn clean package -DskipTests
```

### 运行服务

```bash
# 运行API模块
java -jar structure-org-api/target/structure-org-api.jar
```

### 访问地址

- API文档: http://localhost:8080/swagger-ui.html
- 健康检查: http://localhost:8080/actuator/health

## 功能模块

| 模块 | 说明 |
|-----|------|
| 组织管理 | 组织的创建、更新、删除、查询 |
| 部门管理 | 部门的树形结构管理 |
| 成员管理 | 成员的增删改查 |
| 成员邀请 | 成员邀请链接管理 |
| 成员申请 | 成员加入申请审批 |

## 配置说明

主要配置项（application.yaml）：

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/example_db
    username: admin
    password: password
```

## 项目规范

详细的项目规范请参考 [PROJECT_RULES.md](PROJECT_RULES.md)

## License

MIT License