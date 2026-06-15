package cn.structured.org.enums;

public enum MemberStateEnum {

    NORMAL(1, "正常"),
    RESIGNED(2, "离职"),
    DISABLED(3, "禁用");

    private final Integer code;
    private final String description;

    MemberStateEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MemberStateEnum getByCode(Integer code) {
        for (MemberStateEnum state : values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }
}
