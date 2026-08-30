package com.atlearn.guli.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.atlearn.guli.RemoteMemberService;
import com.atlearn.guli.constant.AuthConstant;
import com.atlearn.guli.core.UserInfo;
import com.atlearn.guli.domain.bo.RmeMemberBo;
import com.atlearn.guli.domain.vo.LoginVo;
import com.atlearn.guli.domain.vo.RmeMemberVo;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.form.EmailLoginBody;
import com.atlearn.guli.service.ILoginService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.ValidatorUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.api.model.LoginUser;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 邮箱登录策略
 * <p>
 * 不带 {@code @Primary}，注入 ILoginService 时不会被默认选中。
 * 需要使用时通过 {@code @Qualifier("emailLoginServiceImpl")} 显式指定。
 *
 * @author guli
 */
@RequiredArgsConstructor
@Service
public class EmailLoginServiceImpl implements ILoginService {

    private final RemoteMemberService remoteMemberService;

    @Override
    public LoginVo login(String body) {
        // jsr303 参数校验
        EmailLoginBody emailBody = JsonUtils.parseObject(body, EmailLoginBody.class);
        ValidatorUtils.validate(emailBody);

        // 从reids取出code
        String cachedCode = RedisUtils.getCacheObject(AuthConstant.CODE_KEY_PREFIX + emailBody.getEmail());
        if (cachedCode == null) {
            throw new BusinessException(ErrorCodeEnum.CODE_EXPIRED);
        }

        // 验证传来的code 和 存入redis的code是否一致
        if (!cachedCode.equals(emailBody.getCode())) {
            throw new BusinessException(ErrorCodeEnum.CODE_INVALID);
        }

        // 调用远程服务查询用户信息
        RmeMemberVo member = remoteMemberService.findByCondition(
            RmeMemberBo.builder().email(emailBody.getEmail()).build());
        if (member == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        if (member.getStatus() == null || member.getStatus() != 1) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_DISABLED);
        }

        // 删除redis中的code
        RedisUtils.deleteObject(AuthConstant.CODE_KEY_PREFIX + emailBody.getEmail());
        return buildLoginVo(member);
    }

    // 构造登录返回对象
    private LoginVo buildLoginVo(RmeMemberVo member) {
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
        RedisUtils.setCacheObject(
            AuthConstant.AUTH_TOKEN_KEY_PREFIX + token, 
            userInfo, 
            Duration.ofSeconds(timeout)
        );

        LoginVo vo = new LoginVo();
        vo.setAccessToken(token);
        vo.setExpireIn(timeout);
        vo.setMemberId(member.getId());
        return vo;
    }

}
