package cn.structured.org.enums;

public enum OrganizationTypeEnum {

    ENTERPRISE(1, "企业组织"),
    TEAM(2, "团队组织"),
    GOVERNMENT(3, "政府组织"),
    EDUCATION(4, "教育组织"),
    OTHER(5, "其他组织");

    private final Integer code;
    private final String description;

    OrganizationTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrganizationTypeEnum getByCode(Integer code) {
        for (OrganizationTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
