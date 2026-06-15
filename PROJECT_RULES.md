# 项目规范

## 📅 文档信息

- **版本**: 1.0.0
- **创建日期**: 2026-06-01
- **最后更新**: 2026-06-01

## 📁 目录结构规范

项目采用标准的分层架构，所有枚举、异常、转换器和常量都有明确的存放位置：

```
cn.structured.portal/
├── enums/          # 枚举定义（状态码、业务枚举等）
├── exception/      # 自定义业务异常
├── assembler/      # 类型转换器（Entity ↔ DTO/VO）
├── constant/       # 常量定义
├── controller/     # 控制层
├── service/        # 服务层
├── mapper/         # 数据访问层
├── entity/         # 实体类
├── dto/            # 数据传输对象
├── vo/             # 视图对象
└── common/        # 通用工具类
```

## 1️⃣ 枚举规范 (enums/)

### 1.1 枚举存放位置

```
cn.structured.*.enums
```

### 1.2 枚举使用场景

- **业务状态码**：如订单状态、用户状态、站点状态等
- **错误码枚举**：定义业务错误的错误码和消息
- **类型枚举**：如性别、角色类型、菜单类型等

### 1.3 枚举定义示例

```java
package cn.structured.portal.enums;

/**
 * 站点状态枚举
 *
 * @author System
 */
public enum SiteStatusEnum {

    ACTIVE("active", "激活"),
    INACTIVE("inactive", "未激活"),
    DELETED("deleted", "已删除");

    private final String code;
    private final String description;

    SiteStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static SiteStatusEnum getByCode(String code) {
        for (SiteStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
```

### 1.4 错误码枚举示例

```java
package cn.structured.portal.enums;

/**
 * 业务异常枚举 1105XX 错误码
 *
 * @author System
 */
public enum PortalExceptionEnum {

    SITE_NOT_FOUND("PORTAL_001", "站点不存在"),
    SITE_ALREADY_EXISTS("PORTAL_002", "站点已存在"),
    SITE_NAME_DUPLICATE("PORTAL_003", "站点名称重复"),
    SITE_DOMAIN_DUPLICATE("PORTAL_004", "站点域名重复"),
    SITE_CONFIG_ERROR("PORTAL_005", "站点配置错误"),
    MENU_NOT_FOUND("PORTAL_101", "菜单不存在"),
    MICRO_APP_NOT_FOUND("PORTAL_201", "微应用不存在"),
    SCENE_NOT_FOUND("PORTAL_301", "场景不存在"),
    CONFIG_NOT_FOUND("PORTAL_401", "配置不存在");

    private final String code;
    private final String message;

    PortalExceptionEnum(String code, String message) {
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

## 2️⃣ 异常规范 (exception/)

### 2.1 异常存放位置

```
cn.structured.portal.exception
```

### 2.2 自定义业务异常

所有业务异常必须继承 `cn.structure.common.exception.CommonException`

### 2.3 异常类定义示例

```java
package cn.structured.portal.exception;

import cn.structure.common.exception.CommonException;
import cn.structured.portal.enums.PortalExceptionEnum;

/**
 * Portal业务异常
 *
 * @author System
 */
public class PortalException extends CommonException {

    public PortalException(PortalExceptionEnum exceptionEnum) {
        super(exceptionEnum.getCode(), exceptionEnum.getMessage());
    }

    public PortalException(String code, String message) {
        super(code, message);
    }

    public PortalException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
```

### 2.4 异常抛出示例

```java
// ✅ 正确：使用枚举抛出异常
if (site == null) {
    throw new PortalException(PortalExceptionEnum.SITE_NOT_FOUND);
}

// ✅ 正确：使用枚举和自定义消息
if (site == null) {
    throw new PortalException(
        PortalExceptionEnum.SITE_NOT_FOUND.getCode(),
        "ID为" + id + "的站点不存在"
    );
}

// ❌ 错误：直接抛出IllegalArgumentException
if (site == null) {
    throw new IllegalArgumentException("Site not found");
}
```

### 2.5 Controller层异常处理

```java
@GetMapping("/{id}")
@Operation(summary = "获取站点详情")
public ResResultVO<PortalSiteVO> getSiteById(@PathVariable String id) {
    PortalSiteVO site = portalSiteService.getSiteById(id);
    if (site == null) {
        throw new PortalException(PortalExceptionEnum.SITE_NOT_FOUND);
    }
    return PortalResultUtils.success(site);
}
```

## 3️⃣ 转换器规范 (assembler/)

### 3.1 转换器存放位置

```
cn.structured.portal.assembler
```

### 3.2 转换器使用场景

- **Entity → VO**：数据库实体转换为视图对象
- **Entity → DTO**：数据库实体转换为数据传输对象
- **DTO → Entity**：数据传输对象转换为数据库实体
- **复杂对象转换**：包含多个字段或计算逻辑的转换

### 3.3 转换器定义原则

1. **工具类模式**：转换器使用私有构造函数，防止实例化
2. **静态方法**：所有转换方法使用 `static` 关键字
3. **命名规范**：转换器命名为 `{业务名}Assembler`，如 `SiteAssembler`
4. **单向转换**：建议分开定义不同方向的转换方法，提高可读性

### 3.4 转换器示例

```java
package cn.structured.portal.assembler;

import cn.structured.portal.dto.PortalSiteDTO;
import cn.structured.portal.entity.PortalSite;
import cn.structured.portal.vo.PortalSiteVO;

/**
 * Portal Site 转换器
 *
 * @author System
 */
public class PortalSiteAssembler {

    private PortalSiteAssembler() {
    }

    /**
     * Entity转VO
     *
     * @param site 站点实体
     * @return 站点VO
     */
    public static PortalSiteVO toVO(PortalSite site) {
        if (site == null) {
            return null;
        }
        PortalSiteVO vo = new PortalSiteVO();
        vo.setId(site.getId());
        vo.setName(site.getName());
        vo.setDomain(site.getDomain());
        vo.setStatus(site.getStatus());
        vo.setDescription(site.getDescription());
        vo.setConfig(site.getConfig());
        vo.setCreateTime(site.getCreateTime());
        vo.setUpdateTime(site.getUpdateTime());
        return vo;
    }

    /**
     * Entity列表转VO列表
     *
     * @param sites 站点实体列表
     * @return 站点VO列表
     */
    public static List<PortalSiteVO> toVOList(List<PortalSite> sites) {
        if (sites == null) {
            return Collections.emptyList();
        }
        return sites.stream()
                .map(PortalSiteAssembler::toVO)
                .collect(Collectors.toList());
    }

    /**
     * DTO转Entity
     *
     * @param dto 站点DTO
     * @return 站点实体
     */
    public static PortalSite toEntity(PortalSiteDTO dto) {
        if (dto == null) {
            return null;
        }
        PortalSite site = new PortalSite();
        site.setId(dto.getId());
        site.setName(dto.getName());
        site.setDomain(dto.getDomain());
        site.setStatus(dto.getStatus());
        site.setDescription(dto.getDescription());
        site.setConfig(dto.getConfig());
        return site;
    }

    /**
     * 更新Entity
     *
     * @param dto 站点DTO
     * @param site 站点实体
     */
    public static void updateEntity(PortalSiteDTO dto, PortalSite site) {
        if (dto == null || site == null) {
            return;
        }
        site.setName(dto.getName());
        site.setDomain(dto.getDomain());
        site.setStatus(dto.getStatus());
        site.setDescription(dto.getDescription());
        site.setConfig(dto.getConfig());
    }
}
```

### 3.5 Service层使用转换器

```java
// ✅ 正确：使用Assembler进行转换
@Override
public PortalSiteVO getSiteById(String id) {
    PortalSite site = this.getById(id);
    if (site == null) {
        return null;
    }
    return PortalSiteAssembler.toVO(site);
}

@Override
public List<PortalSiteVO> getSites() {
    List<PortalSite> sites = this.list();
    return PortalSiteAssembler.toVOList(sites);
}

// ❌ 错误：直接使用BeanUtil.copyProperties
@Override
public PortalSiteVO getSiteById(String id) {
    PortalSite site = this.getById(id);
    return BeanUtil.copyProperties(site, PortalSiteVO.class);
}
```

## 4️⃣ 常量规范 (constant/)

### 4.1 常量存放位置

```
cn.structured.portal.constant
```

### 4.2 常量使用场景

- **业务常量**：如默认配置、分页大小、缓存key等
- **正则表达式**：常用的验证正则
- **时间常量**：如超时时间、间隔时间等
- **配置key**：配置文件中的key

### 4.3 常量定义示例

```java
package cn.structured.portal.constant;

/**
 * Portal业务常量
 *
 * @author System
 */
public class PortalConstant {

    private PortalConstant() {
    }

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final String DEFAULT_SITE_STATUS = "active";

    public static final String CACHE_KEY_PREFIX = "portal:";
    public static final String CACHE_KEY_SITE = CACHE_KEY_PREFIX + "site:";
    public static final String CACHE_KEY_CONFIG = CACHE_KEY_PREFIX + "config:";

    public static final long CACHE_EXPIRE_SECONDS = 3600;

    public static final String REGEX_DOMAIN = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$";
    public static final String REGEX_URL = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
}
```

## 5️⃣ 响应规范

### 5.1 统一响应类

使用 `cn.structure.common.entity.ResResultVO` 作为统一响应类型

### 5.2 响应工具类

使用 `cn.structured.portal.common.PortalResultUtils` 封装常用响应方法

### 5.3 响应使用示例

```java
// ✅ 正确：使用PortalResultUtils
return PortalResultUtils.success(data);
return PortalResultUtils.success("操作成功", data);
return PortalResultUtils.fail(404, "资源不存在");

// ✅ 正确：也可以直接使用ResResultVO
return ResResultVO.success(data);

// ❌ 错误：返回null或空集合
return null;
```

### 5.4 全局异常处理

建议在项目中配置全局异常处理器，统一处理业务异常：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PortalException.class)
    public ResResultVO<Void> handlePortalException(PortalException e) {
        return PortalResultUtils.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResResultVO<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return PortalResultUtils.fail("VALIDATION_ERROR", message);
    }
}
```

## 6️⃣ 代码示例

### 6.1 Service层完整示例

```java
package cn.structured.portal.service.impl;

import cn.structured.portal.assembler.PortalSiteAssembler;
import cn.structured.portal.constant.PortalConstant;
import cn.structured.portal.entity.PortalSite;
import cn.structured.portal.dto.PortalSiteDTO;
import cn.structured.portal.enums.PortalExceptionEnum;
import cn.structured.portal.enums.SiteStatusEnum;
import cn.structured.portal.exception.PortalException;
import cn.structured.portal.mapper.PortalSiteMapper;
import cn.structured.portal.service.PortalSiteService;
import cn.structured.portal.vo.PortalSiteVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Portal Site Service Implementation
 *
 * @author System
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PortalSiteServiceImpl extends ServiceImpl<PortalSiteMapper, PortalSite>
        implements PortalSiteService {

    @Override
    public List<PortalSiteVO> getSites() {
        List<PortalSite> sites = this.list();
        return PortalSiteAssembler.toVOList(sites);
    }

    @Override
    public PortalSiteVO getSiteById(String id) {
        PortalSite site = this.getById(id);
        if (site == null) {
            throw new PortalException(PortalExceptionEnum.SITE_NOT_FOUND);
        }
        return PortalSiteAssembler.toVO(site);
    }

    @Override
    public PortalSiteVO createSite(PortalSiteDTO dto) {
        PortalSite existingSite = this.lambdaQuery()
                .eq(PortalSite::getDomain, dto.getDomain())
                .one();
        if (existingSite != null) {
            throw new PortalException(PortalExceptionEnum.SITE_DOMAIN_DUPLICATE);
        }

        PortalSite site = PortalSiteAssembler.toEntity(dto);
        if (site.getStatus() == null) {
            site.setStatus(PortalConstant.DEFAULT_SITE_STATUS);
        }

        this.save(site);
        log.info("Created site: {}", site.getId());
        return PortalSiteAssembler.toVO(site);
    }

    @Override
    public PortalSiteVO updateSite(PortalSiteDTO dto) {
        if (dto.getId() == null) {
            throw new PortalException("VALIDATION_ERROR", "站点ID不能为空");
        }

        PortalSite existingSite = this.getById(dto.getId());
        if (existingSite == null) {
            throw new PortalException(PortalExceptionEnum.SITE_NOT_FOUND);
        }

        PortalSiteAssembler.updateEntity(dto, existingSite);
        this.updateById(existingSite);
        log.info("Updated site: {}", existingSite.getId());
        return PortalSiteAssembler.toVO(existingSite);
    }

    @Override
    public PortalSiteVO deleteSite(String id) {
        PortalSite site = this.getById(id);
        if (site == null) {
            throw new PortalException(PortalExceptionEnum.SITE_NOT_FOUND);
        }

        this.removeById(id);
        log.info("Deleted site: {}", id);
        return PortalSiteAssembler.toVO(site);
    }
}
```

### 6.2 Controller层完整示例

```java
package cn.structured.portal.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structured.portal.common.PortalResultUtils;
import cn.structured.portal.dto.PortalSiteDTO;
import cn.structured.portal.service.PortalSiteService;
import cn.structured.portal.vo.PortalSiteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Portal Site Controller
 *
 * @author System
 */
@RestController
@RequestMapping("/api/portal/sites")
@RequiredArgsConstructor
@Tag(name = "Portal Site API", description = "门户站点管理接口")
public class PortalSiteController {

    private final PortalSiteService portalSiteService;

    @GetMapping
    @Operation(summary = "获取站点列表", description = "获取所有站点")
    public ResResultVO<List<PortalSiteVO>> getSites() {
        List<PortalSiteVO> sites = portalSiteService.getSites();
        return PortalResultUtils.success(sites);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取站点详情", description = "根据ID获取站点详情")
    public ResResultVO<PortalSiteVO> getSiteById(
            @Parameter(description = "站点ID")
            @PathVariable String id) {
        PortalSiteVO site = portalSiteService.getSiteById(id);
        return PortalResultUtils.success(site);
    }

    @PostMapping
    @Operation(summary = "创建站点", description = "创建新的站点")
    public ResResultVO<PortalSiteVO> createSite(
            @Valid @RequestBody PortalSiteDTO dto) {
        PortalSiteVO site = portalSiteService.createSite(dto);
        return PortalResultUtils.success("站点创建成功", site);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新站点", description = "更新指定ID的站点")
    public ResResultVO<PortalSiteVO> updateSite(
            @Parameter(description = "站点ID")
            @PathVariable String id,
            @Valid @RequestBody PortalSiteDTO dto) {
        dto.setId(id);
        PortalSiteVO site = portalSiteService.updateSite(dto);
        return PortalResultUtils.success("站点更新成功", site);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除站点", description = "删除指定ID的站点")
    public ResResultVO<PortalSiteVO> deleteSite(
            @Parameter(description = "站点ID")
            @PathVariable String id) {
        PortalSiteVO site = portalSiteService.deleteSite(id);
        return PortalResultUtils.success("站点删除成功", site);
    }
}
```

## 7️⃣ 代码检查清单

在提交代码前，请检查以下项目：

### ✅ 枚举检查

- [ ] 状态码是否定义为枚举？
- [ ] 错误码是否定义为枚举？
- [ ] 枚举是否存放在 `enums/` 包下？

### ✅ 异常检查

- [ ] 是否使用 `PortalException` 抛出业务异常？
- [ ] 是否继承 `CommonException`？
- [ ] Controller层是否统一使用异常而非返回错误码？

### ✅ 转换器检查

- [ ] 是否使用 `Assembler` 进行对象转换？
- [ ] 是否避免在Service层使用 `BeanUtil.copyProperties`？
- [ ] 转换器是否存放在 `assembler/` 包下？

### ✅ 常量检查

- [ ] 业务常量是否定义为常量类？
- [ ] 常量是否存放在 `constant/` 包下？
- [ ] 是否避免在代码中直接使用魔法值？

### ✅ 响应检查

- [ ] 是否使用 `PortalResultUtils`？
- [ ] Controller是否统一使用异常处理而非手动返回错误？

## 8️⃣ 常见问题

### Q1: 什么时候使用枚举？什么时候使用常量？

- **枚举**：适用于一组相关的固定值，特别是需要遍历或比较的场景
- **常量**：适用于单个固定值，如配置key、缓存时间等

### Q2: 为什么不能用 BeanUtil.copyProperties？

- BeanUtil 适合简单的属性拷贝
- Assembler 适合复杂转换，可以包含业务逻辑
- Assembler 更易维护和测试
- Assembler 可以明确表达转换意图

### Q3: Controller层要不要处理异常？

- ✅ 推荐在Controller层抛出异常，由全局异常处理器统一处理
- ❌ 不推荐在Controller层手动返回错误响应

## 📚 相关文档

- [Final Changes](FINAL_CHANGES.md)
- [Modifications](MODIFICATIONS.md)
- [README](README.md)

## 🔄 文档更新日志

### v1.0.0 (2026-06-01)

- 初始版本
- 定义枚举规范
- 定义异常规范
- 定义转换器规范
- 定义常量规范
- 定义响应规范
- 提供完整的代码示例
