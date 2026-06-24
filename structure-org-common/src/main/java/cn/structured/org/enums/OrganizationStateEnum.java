package cn.structured.org.enums;

public enum OrganizationStateEnum {

    NORMAL(1, "正常"),
    DISABLED(2, "停用"),
    FROZEN(3, "冻结");

    private final Integer code;
    private final String description;

    OrganizationStateEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrganizationStateEnum getByCode(Integer code) {
        for (OrganizationStateEnum state : values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }
}
