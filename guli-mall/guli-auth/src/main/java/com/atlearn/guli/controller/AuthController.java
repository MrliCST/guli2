package com.atlearn.guli.controller;

import cn.hutool.core.util.RandomUtil;
import com.atlearn.guli.constant.AuthConstant;
import com.atlearn.guli.domain.vo.LoginVo;
import com.atlearn.guli.form.RegisterBody;
import com.atlearn.guli.service.ILoginService;
import com.atlearn.guli.service.IRegisterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mail.utils.MailUtils;
import org.dromara.common.redis.utils.RedisUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 * <p>
 * 教学案例：多 Bean 冲突与 @Primary 解决
 * <p>
 * 容器中有两个 ILoginService 实现：
 * <ul>
 *   <li>{@code PasswordLoginServiceImpl} — 标注 {@code @Primary}，默认注入</li>
 *   <li>{@code EmailLoginServiceImpl} — 需 {@code @Qualifier} 显式指定</li>
 * </ul>
 *
 * @author guli
 */
@Validated
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final ILoginService loginService;
    private final IRegisterService registerService;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/sendCode")
    public R<Void> sendCode(@RequestParam @NotBlank(message = "邮箱不能为空") @Email(message = "邮箱格式不正确") String email) {
        String code = RandomUtil.randomNumbers(AuthConstant.CODE_LENGTH);
        RedisUtils.setCacheObject(AuthConstant.CODE_KEY_PREFIX + email, code, AuthConstant.CODE_TTL);
        String content = String.format(AuthConstant.MAIL_CONTENT_TEMPLATE, code);
        MailUtils.sendText(email, AuthConstant.MAIL_SUBJECT, content);
        return R.ok();
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public R<Long> register(@Valid @RequestBody RegisterBody body) {
        return R.ok(registerService.register(body));
    }

    /**
     * 登录
     * <p>
     * 接收原始 JSON 字符串，委托给 {@link ILoginService}。
     * 当前注入的是 {@code PasswordLoginServiceImpl}（因 {@code @Primary}），
     * 它会将 body 解析为 {@link com.atlearn.guli.form.PasswordLoginBody}。
     */
    @PostMapping("/login")
    public R<LoginVo> login(@RequestBody String body) {
        return R.ok(loginService.login(body));
    }

}
