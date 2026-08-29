package com.atlearn.guli.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.atlearn.guli.RemoteMemberService;
import com.atlearn.guli.constant.AuthConstant;
import com.atlearn.guli.domain.RmeMemberImg;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.form.RegisterBody;
import com.atlearn.guli.service.IRegisterService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.redis.utils.RedisUtils;
import org.springframework.stereotype.Service;

/**
 * 注册服务实现
 *
 * @author guli
 */
@RequiredArgsConstructor
@Service
public class RegisterServiceImpl implements IRegisterService {

    private final RemoteMemberService remoteMemberService;

    @Override
    public Long register(RegisterBody body) {
        // 验证码校验，防止恶意注册
        String cachedCode = RedisUtils.getCacheObject(AuthConstant.CODE_KEY_PREFIX + body.getEmail());
        if (cachedCode == null) {
            throw new BusinessException(ErrorCodeEnum.CODE_EXPIRED);
        }
        if (!cachedCode.equals(body.getCode())) {
            throw new BusinessException(ErrorCodeEnum.CODE_INVALID);
        }

        // 用户名不能重复
        RmeMemberImg byUsername = remoteMemberService.findByCondition(
            RmeMemberImg.builder().username(body.getUsername()).build());
        if (byUsername != null) {
            throw new BusinessException(ErrorCodeEnum.USERNAME_EXISTS);
        }
        // 邮箱不能重复
        RmeMemberImg byEmail = remoteMemberService.findByCondition(
            RmeMemberImg.builder().email(body.getEmail()).build());
        if (byEmail != null) {
            throw new BusinessException(ErrorCodeEnum.EMAIL_EXISTS);
        }

        // 注册用户
        RmeMemberImg bo = RmeMemberImg.builder()
            .username(body.getUsername())
            .password(BCrypt.hashpw(body.getPassword()))
            .email(body.getEmail())
            .build();
        Long memberId = remoteMemberService.register(bo);

        // 删除验证码
        RedisUtils.deleteObject(AuthConstant.CODE_KEY_PREFIX + body.getEmail());
        return memberId;
    }

}
