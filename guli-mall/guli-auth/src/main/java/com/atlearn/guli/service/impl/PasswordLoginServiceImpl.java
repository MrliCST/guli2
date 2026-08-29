package com.atlearn.guli.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.atlearn.guli.RemoteMemberService;
import com.atlearn.guli.constant.AuthConstant;
import com.atlearn.guli.core.UserInfo;
import com.atlearn.guli.domain.RmeMemberImg;
import com.atlearn.guli.domain.vo.LoginVo;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.form.PasswordLoginBody;
import com.atlearn.guli.service.ILoginService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.ValidatorUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.api.model.LoginUser;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 密码登录策略
 * <p>
 * {@code @Primary}：当容器中有多个 ILoginService 实现时，Spring 默认注入此 Bean，
 * 解决多 Bean 冲突。
 *
 * @author guli
 */
@RequiredArgsConstructor
@Service
@Primary
public class PasswordLoginServiceImpl implements ILoginService {

    private final RemoteMemberService remoteMemberService;

    @Override
    public LoginVo login(String body) {
        // jsr303 校验
        PasswordLoginBody pwdBody = JsonUtils.parseObject(body, PasswordLoginBody.class);
        ValidatorUtils.validate(pwdBody);

        // 调用远程服务查询用户信息
        RmeMemberImg member = remoteMemberService.findByCondition(
            RmeMemberImg.builder().username(pwdBody.getUsername()).build());
        if (member == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        if (member.getStatus() == null || member.getStatus() != 1) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_DISABLED);
        }

        // 校验密码
        if (!BCrypt.checkpw(pwdBody.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCodeEnum.PASSWORD_ERROR);
        }
        return buildLoginVo(member);
    }

    // 构造登录返回对象
    private LoginVo buildLoginVo(RmeMemberImg member) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(member.getId());
        loginUser.setUsername(member.getUsername());
        loginUser.setUserType(AuthConstant.USER_TYPE_MEMBER);
        LoginHelper.login(loginUser, null);  // 签发登录token

        String token = StpUtil.getTokenValue();
        long timeout = StpUtil.getTokenTimeout();

        // 存入redis中，供其他服务通过拦截器获取用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(member.getId());
        userInfo.setUserKey(token);
        userInfo.setIsTempUser(false);
        RedisUtils.setCacheObject(AuthConstant.AUTH_TOKEN_KEY_PREFIX + token, userInfo, Duration.ofSeconds(timeout));

        LoginVo vo = new LoginVo();
        vo.setAccessToken(token);
        vo.setExpireIn(timeout);
        vo.setMemberId(member.getId());
        return vo;
    }

}
