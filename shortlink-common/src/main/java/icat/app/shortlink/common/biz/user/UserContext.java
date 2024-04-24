package icat.app.shortlink.common.biz.user;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Optional;

/**
 * 用户上下文
 */
public final class UserContext {

    private static final ThreadLocal<UserInfo> USER_THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 设置用户至上下文
     * @param userInfo 用户详情信息
     */
    public static void setUser(UserInfo userInfo) {
        USER_THREAD_LOCAL.set(userInfo);
    }

    /**
     * 获取上下文中用户 ID
     *
     * @return 用户 ID
     */
    public static Long getUserId() {
        UserInfo userInfo = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfo).map(UserInfo::getUserId).orElse(null);
    }

    /**
     * 获取上下文中用户名称
     *
     * @return 用户名称
     */
    public static String getUsername() {
        UserInfo userInfo = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfo).map(UserInfo::getUsername).orElse(null);
    }

    /**
     * 获取上下文中用户真实姓名
     *
     * @return 用户真实姓名
     */
    public static String getRealName() {
        UserInfo userInfo = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfo).map(UserInfo::getRealName).orElse(null);
    }

    /**
     * 获取上下文中用户 Token
     *
     * @return 用户 Token
     */
    public static String getToken() {
        UserInfo userInfo = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfo).map(UserInfo::getToken).orElse(null);
    }

    /**
     * 清理用户上下文
     */
    public static void removeUser() {
        USER_THREAD_LOCAL.remove();
    }
}