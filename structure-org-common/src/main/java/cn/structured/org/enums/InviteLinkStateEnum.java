package cn.structured.org.enums;

/**
 * 邀请链接状态枚举
 *
 * @author chuck
 * @since 2024-01-01
 */
public enum InviteLinkStateEnum {

    VALID(1, "有效"),
    EXPIRED(2, "已过期"),
    DISABLED(3, "已禁用");

    private final Integer code;
    private final String description;

    InviteLinkStateEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static InviteLinkStateEnum getByCode(Integer code) {
        for (InviteLinkStateEnum state : values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }
}