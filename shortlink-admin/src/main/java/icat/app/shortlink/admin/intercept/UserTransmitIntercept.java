package icat.app.shortlink.admin.intercept;

import icat.app.shortlink.admin.dao.entity.UserDO;
import icat.app.shortlink.admin.util.RedisUtils;
import icat.app.shortlink.common.biz.user.UserContext;
import icat.app.shortlink.common.biz.user.UserInfo;
import icat.app.shortlink.common.constant.RedisCacheConstant;
import icat.app.shortlink.common.convention.errorcode.BaseErrorCode;
import icat.app.shortlink.common.convention.exception.ClientException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;

@Component
@RequiredArgsConstructor
public class UserTransmitIntercept implements HandlerInterceptor {

    private static final String TOKEN_KEY = "Token";
    private static final String USERNAME_KEY = "Username";

    private final RedisUtils redisUtils;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response,@Nonnull Object handler) {
        String token = request.getHeader(TOKEN_KEY);
        String username = request.getHeader(USERNAME_KEY);
        if (token == null || username == null) {
            throw new ClientException(BaseErrorCode.USER_NOT_LOGIN_ERROR);
        }
        UserDO user = redisUtils.getHash(RedisCacheConstant.USER_LOGIN_KEY+username, token, UserDO.class);
        if (user == null) {
            throw new ClientException(BaseErrorCode.USER_NOT_LOGIN_ERROR);
        }
        UserContext.setUser(new UserInfo()
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setRealName(user.getRealName())
                .setToken(token));
        return true;
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) {
        UserContext.removeUser();
    }
}
