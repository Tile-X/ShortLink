package icat.app.shortlink.common.constant;

public class RedisCacheConstant {

    /**
     * 用户注册锁
     */
    public static final String LOCK_USER_REGISTER_KEY = "shortlink:lock_user-register:";

    /**
     * 登录前缀Key
     */
    public static final String USER_LOGIN_KEY = "shortlink:login_";

    /**
     * 短链接跳转前缀Key
     */
    public static final String GOTO_SHORT_LINK_KEY = "shortlink:link_goto_%s";

    /**
     * 短链接跳转数据锁
     */
    public static final String LOCK_GOTO_SHORT_LINK_KEY = "shortlink:lock_link_goto_%s";

    /**
     * 短链接空数据前缀Key
     */
    public static final String GOTO_SHORT_IS_NULL_KEY = "shortlink:link_goto_is_null_%s";

    /**
     * 访问IP数据前缀Key
     */
    public static final String UNIQUE_IP_KEY = "shortlink:unique_ip";

public static final String LOCK_GID_UPDATE_KEY = "shortlink:lock_gid_update_%s";

}
