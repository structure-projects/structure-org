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
