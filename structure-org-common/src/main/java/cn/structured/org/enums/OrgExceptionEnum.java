package cn.structured.org.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrgExceptionEnum {

    ORGANIZATION_NOT_FOUND("110501", "组织不存在"),
    ORGANIZATION_ALREADY_EXISTS("110502", "组织已存在"),
    ORGANIZATION_CODE_DUPLICATE("110503", "组织编码重复"),
    
    DEPT_NOT_FOUND("110511", "部门不存在"),
    DEPT_PARENT_NOT_FOUND("110512", "父部门不存在"),
    DEPT_HAS_CHILDREN("110513", "存在子部门，无法删除"),
    
    MEMBER_NOT_FOUND("110521", "成员不存在"),
    
    MEMBER_REQUEST_NOT_FOUND("110531", "申请记录不存在"),
    MEMBER_REQUEST_STATUS_ERROR("110532", "申请状态不允许审核"),

    INVITE_LINK_NOT_FOUND("110541", "邀请链接不存在"),
    INVITE_LINK_EXPIRED("110542", "邀请链接已过期"),
    INVITE_LINK_DISABLED("110543", "邀请链接已禁用"),
    
    INVITE_CODE_EMPTY("110544", "邀请码不能为空"),
    INVITE_CODE_ERROR("110545", "邀请码错误"),
    INVITE_STATE_ERROR("110546", "邀请状态不允许此操作"),
    PHONE_MISMATCH("110547", "手机号与邀请接收人不匹配"),
    
    VALIDATION_ERROR("110599", "参数校验失败");

    private final String code;
    private final String message;
}