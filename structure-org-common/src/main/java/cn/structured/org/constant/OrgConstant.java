package cn.structured.org.constant;

public class OrgConstant {

    private OrgConstant() {
    }

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final String CACHE_KEY_PREFIX = "org:";
    public static final String CACHE_KEY_ORGANIZATION = CACHE_KEY_PREFIX + "organization:";
    public static final String CACHE_KEY_DEPT = CACHE_KEY_PREFIX + "dept:";
    public static final String CACHE_KEY_MEMBER = CACHE_KEY_PREFIX + "member:";

    public static final long CACHE_EXPIRE_SECONDS = 3600;

    public static final String INVITE_LINK_PATH = "/#/invite/";
}
