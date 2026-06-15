package cn.structured.org.enums;

public enum MemberRequestTypeEnum {

    APPLY(1, "申请加入"),
    INVITE(2, "邀请加入");

    private final Integer code;
    private final String description;

    MemberRequestTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MemberRequestTypeEnum getByCode(Integer code) {
        for (MemberRequestTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
