package cn.structured.org.exception;

import cn.structure.common.exception.CommonException;
import cn.structured.org.enums.OrgExceptionEnum;

/**
 * 组织异常
 *
 * @author chuck
 * @since 2024-01-01
 */
public class OrgException extends CommonException {

    /**
     * 构造函数
     *
     * @param exceptionEnum 异常枚举
     */
    public OrgException(OrgExceptionEnum exceptionEnum) {
        super(exceptionEnum.getCode(), exceptionEnum.getMessage());
    }

    /**
     * 构造函数
     *
     * @param code    异常码
     * @param message 异常信息
     */
    public OrgException(String code, String message) {
        super(code, message);
    }

}
