# 项目依赖配置说明

## 📋 依赖管理概览

本项目采用 Maven 多模块架构，依赖管理集中在 `structure-org-dependencies` 模块中。

---

## 1️⃣ 版本常量定义

| 依赖名称 | 版本 | 说明 |
|---------|------|------|
| `revision` | `1.0.0-SNAPSHOT` | 项目版本 |
| `spring-boot.version` | `4.0.6` | Spring Boot 版本 |
| `mybatis-plus.version` | `3.5.16` | MyBatis Plus 版本 |
| `springdoc.version` | `3.0.3` | SpringDoc OpenAPI 版本 |
| `structure.version` | `1.4.1-SNAPSHOT` | 内部公共组件版本 |
| `spring-cloud.version` | `2025.1.0` | Spring Cloud 版本 |
| `spring-alibaba.version` | `2025.1.0.0` | Spring Cloud Alibaba 版本 |
| `spring-cloud-parent.version` | `5.0.0` | Spring Cloud Parent 版本 |

---

## 2️⃣ 模块依赖关系

```
structure-org-api
    └── structure-org-biz
            └── structure-org-common
                    └── structure-common
```

---

## 3️⃣ 依赖分类表

### 3.1 内部模块依赖

| 模块 | GroupId | ArtifactId | 版本 | 说明 |
|-----|---------|------------|------|------|
| structure-org-common | cn.structured | structure-org-common | ${revision} | 公共模块 |
| structure-org-biz | cn.structured | structure-org-biz | ${revision} | 业务模块 |

### 3.2 内部公共组件

| 组件 | GroupId | ArtifactId | 版本 | 说明 |
|-----|---------|------------|------|------|
| structure-common | cn.structured | structure-common | ${structure.version} | 基础公共组件 |
| structure-mybatis-plus-starter | cn.structured | structure-mybatis-plus-starter | ${structure.version} | MyBatis Plus 封装 |
| structure-restful-web-starter | cn.structured | structure-restful-web-starter | ${structure.version} | RESTful Web 封装 |
| structure-security-oauth-resource-starter | cn.structured | structure-security-oauth-resource-starter | 1.1.2-SNAPSHOT | OAuth2 资源服务 |

### 3.3 Spring Boot 官方依赖

| 依赖 | GroupId | ArtifactId | 说明 |
|-----|---------|------------|------|
| spring-boot-starter-web | org.springframework.boot | spring-boot-starter-web | Web 支持 |
| spring-boot-starter-actuator | org.springframework.boot | spring-boot-starter-actuator | 监控支持 |

### 3.4 Spring Cloud 依赖

| 依赖 | GroupId | ArtifactId | 说明 |
|-----|---------|------------|------|
| spring-cloud-starter-openfeign | org.springframework.cloud | spring-cloud-starter-openfeign | Feign 客户端 |
| spring-cloud-starter-alibaba-nacos-discovery | com.alibaba.cloud | spring-cloud-starter-alibaba-nacos-discovery | Nacos 服务发现 |
| spring-cloud-starter-alibaba-nacos-config | com.alibaba.cloud | spring-cloud-starter-alibaba-nacos-config | Nacos 配置管理 |

### 3.5 数据库相关

| 依赖 | GroupId | ArtifactId | 版本 | 说明 |
|-----|---------|------------|------|------|
| mybatis-plus-spring-boot4-starter | com.baomidou | mybatis-plus-spring-boot4-starter | ${mybatis-plus.version} | MyBatis Plus 启动器 |
| mysql-connector-j | com.mysql | mysql-connector-j | - | MySQL 驱动 |
| flyway-core | org.flywaydb | flyway-core | - | 数据库迁移 |
| flyway-mysql | org.flywaydb | flyway-mysql | - | MySQL 迁移支持 |

### 3.6 工具与文档

| 依赖 | GroupId | ArtifactId | 版本 | 说明 |
|-----|---------|------------|------|------|
| lombok | org.projectlombok | lombok | - | 简化代码 |
| springdoc-openapi-starter-webmvc-ui | org.springdoc | springdoc-openapi-starter-webmvc-ui | ${springdoc.version} | API 文档 |
| jakarta.validation-api | jakarta.validation | jakarta.validation-api | - | 参数校验 |

---

## 4️⃣ 模块依赖清单

### 4.1 structure-org-api 模块

| 序号 | 依赖名称 | 来源模块 |
|-----|---------|---------|
| 1 | structure-org-biz | 内部模块 |
| 2 | spring-boot-starter-web | Spring Boot |
| 3 | lombok | 工具 |
| 4 | structure-restful-web-starter | 内部组件 |
| 5 | springdoc-openapi-starter-webmvc-ui | API 文档 |
| 6 | spring-cloud-starter-openfeign | Spring Cloud |
| 7 | spring-boot-starter-actuator | Spring Boot |
| 8 | spring-cloud-starter-alibaba-nacos-discovery | Nacos |
| 9 | spring-cloud-starter-alibaba-nacos-config | Nacos |
| 10 | flyway-core | 数据库迁移 |
| 11 | flyway-mysql | 数据库迁移 |
| 12 | mysql-connector-j | 数据库驱动 |
| 13 | structure-security-oauth-resource-starter | 安全组件 |

### 4.2 structure-org-biz 模块

| 序号 | 依赖名称 | 来源模块 |
|-----|---------|---------|
| 1 | structure-org-common | 内部模块 |
| 2 | mybatis-plus-spring-boot4-starter | MyBatis Plus |
| 3 | structure-mybatis-plus-starter | 内部组件 |
| 4 | structure-common | 内部组件 |
| 5 | lombok | 工具 |

### 4.3 structure-org-common 模块

| 序号 | 依赖名称 | 来源模块 |
|-----|---------|---------|
| 1 | structure-common | 内部组件 |
| 2 | lombok | 工具 |
| 3 | jakarta.validation-api | 参数校验 |

---

## 5️⃣ Maven 插件配置

### 5.1 编译插件

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <compilerArgs>
            <arg>-parameters</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

### 5.2 Spring Boot 插件

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

---

## 6️⃣ 依赖版本管理文件

### 6.1 structure-org-dependencies/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>cn.structured</groupId>
        <artifactId>structure-dependencies</artifactId>
        <version>1.4.0</version>
    </parent>

    <name>structure-org-dependencies</name>
    <artifactId>structure-org-dependencies</artifactId>
    <version>${revision}</version>
    <packaging>pom</packaging>
    <description>组织架构项目父工程</description>

    <properties>
        <revision>1.0.0-SNAPSHOT</revision>
        <spring-boot.version>4.0.6</spring-boot.version>
        <mybatis-plus.version>3.5.16</mybatis-plus.version>
        <springdoc.version>3.0.3</springdoc.version>
        <structure.version>1.4.1-SNAPSHOT</structure.version>
        <spring-cloud.version>2025.1.0</spring-cloud.version>
        <spring-alibaba.version>2025.1.0.0</spring-alibaba.version>
        <spring-cloud-parent.version>5.0.0</spring-cloud-parent.version>
    </properties>

    <modules>
        <module>../structure-org-common</module>
        <module>../structure-org-api</module>
        <module>../structure-org-biz</module>
    </modules>

    <dependencyManagement>
        <dependencies>
            <!-- 内部模块依赖 -->
            <dependency>
                <groupId>cn.structured</groupId>
                <artifactId>structure-org-common</artifactId>
                <version>${revision}</version>
            </dependency>
            <dependency>
                <groupId>cn.structured</groupId>
                <artifactId>structure-org-biz</artifactId>
                <version>${revision}</version>
            </dependency>

            <!-- 内部公共组件 -->
            <dependency>
                <groupId>cn.structured</groupId>
                <artifactId>structure-common</artifactId>
                <version>${structure.version}</version>
            </dependency>
            <dependency>
                <groupId>cn.structured</groupId>
                <artifactId>structure-mybatis-plus-starter</artifactId>
                <version>${structure.version}</version>
            </dependency>
            <dependency>
                <groupId>cn.structured</groupId>
                <artifactId>structure-restful-web-starter</artifactId>
                <version>${structure.version}</version>
            </dependency>
            <dependency>
                <groupId>cn.structured</groupId>
                <artifactId>structure-security-oauth-resource-starter</artifactId>
                <version>1.1.2-SNAPSHOT</version>
            </dependency>

            <!-- Spring Cloud -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies-parent</artifactId>
                <version>${spring-cloud-parent.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring-alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <!-- MyBatis Plus -->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-spring-boot4-starter</artifactId>
                <version>${mybatis-plus.version}</version>
            </dependency>

            <!-- SpringDoc OpenAPI -->
            <dependency>
                <groupId>org.springdoc</groupId>
                <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
                <version>${springdoc.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

---

## 7️⃣ 依赖管理最佳实践

### ✅ 版本集中管理
- 所有版本号定义在 `structure-org-dependencies/pom.xml` 的 `<properties>` 中
- 子模块通过 `${revision}` 和 `${xxx.version}` 引用

### ✅ 依赖范围控制
- 使用 `dependencyManagement` 声明版本，子模块按需引入
- 避免重复声明版本号

### ✅ 内部模块依赖顺序
- common → biz → api
- 上层模块只依赖下层模块

### ❌ 禁止事项
- 禁止在子模块中硬编码版本号
- 禁止循环依赖
- 禁止引入未在 `dependencyManagement` 中声明的依赖

---

## 8️⃣ 依赖更新流程

1. **更新版本常量**：修改 `structure-org-dependencies/pom.xml` 中的版本号
2. **验证依赖**：运行 `mvn dependency:tree` 检查依赖树
3. **编译测试**：运行 `mvn clean compile` 验证编译
4. **提交变更**：提交 pom.xml 文件变更