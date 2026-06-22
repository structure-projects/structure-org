package cn.structured.org.enums;

/**
 * 成员邀请状态枚举
 *
 * @author chuck
 * @since 2024-01-01
 */
public enum MemberInviteStateEnum {

    PENDING(1, "待接收"),
    ACCEPTED(2, "已接收"),
    EXPIRED(3, "已过期"),
    CANCELLED(4, "已取消");

    private final Integer code;
    private final String description;

    MemberInviteStateEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MemberInviteStateEnum getByCode(Integer code) {
        for (MemberInviteStateEnum state : values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }
}