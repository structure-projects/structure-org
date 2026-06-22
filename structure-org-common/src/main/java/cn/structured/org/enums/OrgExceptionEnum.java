package cn.structured.org.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrgExceptionEnum {

    ORGANIZATION_NOT_FOUND("ORG_001", "组织不存在"),
    ORGANIZATION_ALREADY_EXISTS("ORG_002", "组织已存在"),
    ORGANIZATION_CODE_DUPLICATE("ORG_003", "组织编码重复"),
    
    DEPT_NOT_FOUND("ORG_101", "部门不存在"),
    DEPT_PARENT_NOT_FOUND("ORG_102", "父部门不存在"),
    DEPT_HAS_CHILDREN("ORG_103", "存在子部门，无法删除"),
    
    MEMBER_NOT_FOUND("ORG_201", "成员不存在"),
    
    MEMBER_REQUEST_NOT_FOUND("ORG_301", "申请记录不存在"),
    MEMBER_REQUEST_STATUS_ERROR("ORG_302", "申请状态不允许审核");

    private final String code;
    private final String message;
}
