# 项目规范

## 📅 文档信息

- **版本**: 1.0.0
- **创建日期**: 2026-06-16
- **最后更新**: 2026-06-16

## 📁 项目结构规范

### 整体架构

项目采用标准的微服务分层架构，分为四个模块：

```
structure-org/
├── structure-org-api/        # 控制层（对外暴露REST API）
├── structure-org-biz/        # 业务层（核心业务逻辑）
├── structure-org-common/     # 公共层（DTO、VO、枚举、异常等）
└── structure-org-dependencies/ # 依赖管理（统一版本控制）
```

### 模块职责说明

| 模块 | 职责 | 包含包 |
|-----|------|--------|
| **structure-org-api** | 控制层，处理HTTP请求 | `controller/`, `OrgApplication.java` |
| **structure-org-biz** | 业务逻辑层 | `service/`, `manager/`, `mapper/`, `entity/`, `assembler/`, `config/` |
| **structure-org-common** | 公共组件 | `dto/`, `vo/`, `query/`, `enums/`, `exception/`, `constant/` |
| **structure-org-dependencies** | Maven依赖管理 | `pom.xml` |

### 包结构详解

```
cn.structured.org/
├── controller/     # REST API控制层，处理请求和响应
├── service/        # 业务服务层，定义业务接口和实现
├── manager/        # 数据管理层，封装数据访问逻辑
├── mapper/         # MyBatis数据访问层
├── entity/         # 数据库实体类
├── assembler/      # 对象转换器（Entity ↔ DTO/VO）
├── dto/            # 数据传输对象（请求参数）
├── vo/             # 视图对象（响应数据）
├── query/          # 查询条件对象
├── enums/          # 枚举定义（状态码、错误码等）
├── exception/      # 自定义业务异常
├── constant/       # 常量定义
└── config/         # 配置类
```

---

## 1️⃣ 枚举规范 (enums/)

### 1.1 枚举存放位置

```
cn.structured.org.enums
```

### 1.2 枚举使用场景

- **业务状态码**：如组织状态、部门状态、成员状态等
- **错误码枚举**：定义业务错误的错误码和消息
- **类型枚举**：如组织类型、申请类型等

### 1.3 枚举命名规范

| 类型 | 命名模式 | 示例 |
|-----|---------|------|
| 状态枚举 | `{业务}StateEnum` | `MemberStateEnum` |
| 类型枚举 | `{业务}TypeEnum` | `OrganizationTypeEnum` |
| 错误码枚举 | `{业务}ExceptionEnum` | `OrgExceptionEnum` |

### 1.4 枚举定义示例

```java
package cn.structured.org.enums;

public enum OrgExceptionEnum {

    ORGANIZATION_NOT_FOUND("ORG_001", "组织不存在"),
    ORGANIZATION_ALREADY_EXISTS("ORG_002", "组织已存在"),
    DEPT_NOT_FOUND("ORG_101", "部门不存在"),
    DEPT_PARENT_NOT_FOUND("ORG_102", "父部门不存在"),
    DEPT_HAS_CHILDREN("ORG_103", "存在子部门，无法删除");

    private final String code;
    private final String message;

    OrgExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
```

---

## 2️⃣ 异常规范 (exception/)

### 2.1 异常存放位置

```
cn.structured.org.exception
```

### 2.2 自定义业务异常

所有业务异常必须继承 `cn.structure.common.exception.CommonException`

### 2.3 异常类定义示例

```java
package cn.structured.org.exception;

import cn.structure.common.exception.CommonException;
import cn.structured.org.enums.OrgExceptionEnum;

public class OrgException extends CommonException {

    public OrgException(OrgExceptionEnum exceptionEnum) {
        super(exceptionEnum.getCode(), exceptionEnum.getMessage());
    }

    public OrgException(String code, String message) {
        super(code, message);
    }
}
```

### 2.4 异常抛出示例

```java
if (dept == null) {
    throw new OrgException(OrgExceptionEnum.DEPT_NOT_FOUND);
}

throw new OrgException("VALIDATION_ERROR", "部门名称不能为空");
```

---

## 3️⃣ 转换器规范 (assembler/)

### 3.1 转换器存放位置

```
cn.structured.org.assembler
```

### 3.2 转换器命名规范

转换器命名为 `{业务名}Assembler`，如 `DeptAssembler`、`MemberAssembler`

### 3.3 转换器定义原则

1. **工具类模式**：使用私有构造函数，防止实例化
2. **静态方法**：所有转换方法使用 `static` 关键字
3. **方法命名**：统一使用 `assembler()` 方法名

### 3.4 转换器示例

```java
package cn.structured.org.assembler;

import cn.structured.org.dto.DeptDTO;
import cn.structured.org.entity.Dept;
import cn.structured.org.vo.DeptVO;

public class DeptAssembler {

    private DeptAssembler() {
    }

    public static DeptVO assembler(Dept dept) {
        if (dept == null) {
            return null;
        }
        DeptVO vo = new DeptVO();
        vo.setId(dept.getId());
        vo.setName(dept.getName());
        vo.setParentId(dept.getParentId());
        vo.setTreePath(dept.getTreePath());
        vo.setSort(dept.getSort());
        vo.setEnabled(dept.getEnabled());
        vo.setCreateTime(dept.getCreateTime());
        vo.setUpdateTime(dept.getUpdateTime());
        return vo;
    }

    public static Dept assembler(DeptDTO dto) {
        if (dto == null) {
            return null;
        }
        Dept dept = new Dept();
        dept.setName(dto.getName());
        dept.setParentId(dto.getParentId());
        dept.setTreePath(dto.getTreePath());
        dept.setSort(dto.getSort());
        dept.setEnabled(dto.getEnabled());
        return dept;
    }
}
```

---

## 4️⃣ 常量规范 (constant/)

### 4.1 常量存放位置

```
cn.structured.org.constant
```

### 4.2 常量命名规范

- 类名：`{业务}Constant`，如 `OrgConstant`
- 常量名：全大写，下划线分隔，如 `DEFAULT_PAGE_SIZE`

### 4.3 常量定义示例

```java
package cn.structured.org.constant;

public class OrgConstant {

    private OrgConstant() {
    }

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final String CACHE_KEY_PREFIX = "org:";
    public static final String CACHE_KEY_ORGANIZATION = CACHE_KEY_PREFIX + "organization:";
    public static final String CACHE_KEY_DEPT = CACHE_KEY_PREFIX + "dept:";
    public static final String CACHE_KEY_MEMBER = CACHE_KEY_PREFIX + "member:";

    public static final long CACHE_EXPIRE_SECONDS = 3600;
}
```

---

## 5️⃣ 实体类规范 (entity/)

### 5.1 实体类字段规范

| 字段 | 命名 | 注解 | 说明 |
|-----|------|------|------|
| 主键 | `id` | `@TableId` | 自增主键 |
| 逻辑删除 | `deleted` | `@TableLogic` | Boolean类型 |
| 创建时间 | `createTime` | `@TableField(fill = FieldFill.INSERT)` | LocalDateTime |
| 更新时间 | `updateTime` | `@TableField(fill = FieldFill.INSERT_UPDATE)` | LocalDateTime |
| 创建人 | `createBy` | `@TableField(fill = FieldFill.INSERT)` | Long |
| 更新人 | `updateBy` | `@TableField(fill = FieldFill.INSERT_UPDATE)` | Long |

---

## 6️⃣ DTO规范 (dto/)

### 6.1 DTO命名规范

类名：`{业务}DTO`，如 `DeptDTO`、`MemberDTO`

### 6.2 DTO字段规范

- 使用 `@NotBlank`、`@NotNull`、`@Size` 等注解进行参数校验
- 使用 `@Schema` 注解添加Swagger文档说明

---

## 7️⃣ VO规范 (vo/)

### 7.1 VO命名规范

类名：`{业务}VO`，如 `DeptVO`、`MemberVO`

### 7.2 VO字段规范

- 使用 `@Schema` 注解添加Swagger文档说明
- 树形结构包含 `children` 字段

---

## 8️⃣ Query规范 (query/)

### 8.1 Query命名规范

类名：`{业务}Query`，如 `DeptQuery`、`MemberQuery`

---

## 9️⃣ Service层规范

### 9.1 Service接口规范

| 操作 | 方法名 | 返回类型 |
|-----|-------|---------|
| 创建 | `create(DTO)` | `Long` |
| 更新 | `update(id, DTO)` | `void` |
| 删除 | `delete(id)` | `void` |
| 查询单个 | `findById(id)` | `VO` |
| 分页查询 | `page(query, reqPage)` | `ResPage<VO>` |

### 9.2 Service实现规范

- 实现类名：`{业务}ServiceImpl`
- 使用 `@Service`、`@Slf4j`、`@AllArgsConstructor` 注解

---

## 🔟 Controller层规范

### 10.1 Controller方法规范

| 操作 | HTTP方法 | 路径 | 返回类型 |
|-----|---------|------|---------|
| 创建 | POST | `/api/{业务}` | `ResResultVO<Long>` |
| 更新 | PUT | `/api/{业务}/{id}` | `ResResultVO<Void>` |
| 删除 | DELETE | `/api/{业务}/{id}` | `ResResultVO<Void>` |
| 查询单个 | GET | `/api/{业务}/{id}` | `ResResultVO<VO>` |
| 分页查询 | GET | `/api/{业务}/page` | `ResResultVO<ResPage<VO>>` |

---

## 1️⃣1️⃣ Manager层规范

### 11.1 Manager层职责

- 封装数据访问逻辑
- 继承 MyBatis Plus 的 `IService`
- 在 Manager 层进行基础的数据校验

---

## 1️⃣2️⃣ 响应规范

使用 `cn.structure.common.utils.ResultUtilSimpleImpl` 封装响应：

```java
ResultUtilSimpleImpl.success(data);
ResultUtilSimpleImpl.fail(code, message);
```

---

## 1️⃣3️⃣ 命名规范汇总

| 类型 | 命名模式 | 示例 |
|-----|---------|------|
| 枚举类 | `{业务}Enum` | `OrgExceptionEnum` |
| 异常类 | `{业务}Exception` | `OrgException` |
| 转换器 | `{业务}Assembler` | `DeptAssembler` |
| 常量类 | `{业务}Constant` | `OrgConstant` |
| 实体类 | `{业务}` | `Dept` |
| DTO类 | `{业务}DTO` | `DeptDTO` |
| VO类 | `{业务}VO` | `DeptVO` |
| Query类 | `{业务}Query` | `DeptQuery` |
| Service接口 | `I{业务}Service` | `IDeptService` |
| Service实现 | `{业务}ServiceImpl` | `DeptServiceImpl` |
| Manager接口 | `I{业务}Manager` | `IDeptManager` |
| Manager实现 | `{业务}ManagerImpl` | `DeptManagerImpl` |
| Controller | `{业务}Controller` | `DeptController` |
| Mapper | `{业务}Mapper` | `DeptMapper` |

---

## 1️⃣4️⃣ 代码检查清单

- [ ] 枚举是否存放在 `enums/` 包下？
- [ ] 是否使用 `OrgException` 抛出业务异常？
- [ ] 是否使用 `Assembler` 进行对象转换？
- [ ] 业务常量是否定义为常量类？
- [ ] 是否使用 `ResultUtilSimpleImpl` 封装响应？

---

## 📚 相关文档

- [README](README.md)

## 🔄 文档更新日志

### v1.0.0 (2026-06-16)

- 初始版本
- 定义枚举、异常、转换器、常量规范
- 定义实体类、DTO/VO/Query规范
- 定义Service/Controller/Manager层规范