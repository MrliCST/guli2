package com.atlearn.guli.service;

import com.atlearn.guli.domain.vo.LoginVo;

/**
 * 登录服务接口
 *
 * @author guli
 */
public interface ILoginService {

    /**
     * 登录
     *
     * @param body JSON 请求体
     * @return 登录响应
     */
    LoginVo login(String body);

}
