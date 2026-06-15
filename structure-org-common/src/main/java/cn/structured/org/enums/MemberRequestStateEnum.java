package cn.structured.org.enums;

public enum MemberRequestStateEnum {

    PENDING(1, "待审核"),
    APPROVED(2, "已通过"),
    REJECTED(3, "已拒绝");

    private final Integer code;
    private final String description;

    MemberRequestStateEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MemberRequestStateEnum getByCode(Integer code) {
        for (MemberRequestStateEnum state : values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }
}
