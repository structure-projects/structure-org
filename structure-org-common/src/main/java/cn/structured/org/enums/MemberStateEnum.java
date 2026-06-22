package cn.structured.org.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 成员状态枚举
 *
 * @author chuck
 * @since 2024-01-01
 */
@Getter
@AllArgsConstructor
public enum MemberStateEnum {

    NORMAL(1, "正常"),
    RESIGNED(2, "离职"),
    DISABLED(3, "禁用");

    private final Integer code;
    private final String description;

    public static MemberStateEnum getByCode(Integer code) {
        for (MemberStateEnum state : values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }
}
