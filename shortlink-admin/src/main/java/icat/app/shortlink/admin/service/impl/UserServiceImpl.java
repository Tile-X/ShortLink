package icat.app.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icat.app.shortlink.admin.dao.entity.UserDO;
import icat.app.shortlink.admin.dao.mapper.UserMapper;
import icat.app.shortlink.admin.dto.req.UserLoginReqDTO;
import icat.app.shortlink.admin.dto.req.UserRegisterReqDTO;
import icat.app.shortlink.admin.dto.req.UserUpdateReqDTO;
import icat.app.shortlink.admin.dto.resp.UserLoginRespDTO;
import icat.app.shortlink.admin.dto.resp.UserRespDTO;
import icat.app.shortlink.admin.service.GroupService;
import icat.app.shortlink.admin.service.UserService;
import icat.app.shortlink.admin.util.RedisUtils;
import icat.app.shortlink.common.biz.user.UserContext;
import icat.app.shortlink.common.convention.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import static icat.app.shortlink.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static icat.app.shortlink.common.constant.RedisCacheConstant.USER_LOGIN_KEY;
import static icat.app.shortlink.common.convention.errorcode.BaseErrorCode.*;

/**
 * 用户接口实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RedisUtils redisUtils;
    private final RedissonClient redissonClient;
    private final GroupService groupService;
    private final RBloomFilter<String> registerCachePenetrationBloomFilter;

    @Override
    public void register(UserRegisterReqDTO requestParam) {
        if (!isUsernameAvailable(requestParam.getUsername())) {
          throw new ClientException(USER_NAME_EXIST_ERROR);
        }
        requestParam.setPassword(BCrypt.hashpw(requestParam.getPassword()));
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUsername());
        try {
            if (lock.tryLock()) {
                if (baseMapper.insert(BeanUtil.copyProperties(requestParam, UserDO.class))<1) {
                    throw new ClientException(USER_REGISTER_ERROR);
                }
                registerCachePenetrationBloomFilter.add(requestParam.getUsername());
                groupService.createGroup(requestParam.getUsername(), "默认分组");
            } else {
                throw new ClientException(USER_REGISTER_ERROR);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, requestParam.getUsername());
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException(USERNAME_OR_PASSWORD_ERROR);
        }
        if (!BCrypt.checkpw(requestParam.getPassword(), userDO.getPassword())) {
            throw new ClientException(USERNAME_OR_PASSWORD_ERROR);
        }
        String token = UUID.randomUUID().toString(true);
        redisUtils.setHash(USER_LOGIN_KEY + userDO.getUsername(), token, userDO, 2 * 60 * 60);
        return new UserLoginRespDTO()
                .setUsername(userDO.getUsername())
                .setToken(token);
    }

    @Override
    public void logout() {
        if (!isUserLogin()) {
            throw new ClientException("用户未登录");
        }
        redisUtils.remove(USER_LOGIN_KEY + UserContext.getUsername());
    }

    @Override
    public void updateUser(UserUpdateReqDTO requestParam) {
        if (!UserContext.getUsername().equals(requestParam.getUsername())) {
            throw new ClientException(USER_UPDATE_ERROR);
        }
        LambdaUpdateWrapper<UserDO> wrapper = Wrappers.lambdaUpdate(UserDO.class)
                        .eq(UserDO::getUsername, requestParam.getUsername());
        requestParam.setPassword(BCrypt.hashpw(requestParam.getPassword()));
        if (baseMapper.update(BeanUtil.copyProperties(requestParam, UserDO.class), wrapper)<1) {
            throw new ClientException(USER_UPDATE_ERROR);
        }
    }

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO user = baseMapper.selectOne(wrapper);
        if (user == null) {
            throw new ClientException(USER_NOT_EXIST);
        }
        return BeanUtil.toBean(user, UserRespDTO.class);
    }

    @Override
    public Boolean isUsernameAvailable(String username) {
        return !registerCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public Boolean isUserLogin() {
        return redisUtils.getHash(USER_LOGIN_KEY + UserContext.getUsername(), UserContext.getToken()) != null;
    }

}
