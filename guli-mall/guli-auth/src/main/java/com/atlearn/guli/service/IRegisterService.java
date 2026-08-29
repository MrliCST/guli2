package com.atlearn.guli.service;

import com.atlearn.guli.form.RegisterBody;

/**
 * 注册服务接口
 *
 * @author guli
 */
public interface IRegisterService {

    /**
     * 注册
     *
     * @param body 注册请求体
     * @return 注册成功的会员id
     */
    Long register(RegisterBody body);

}
