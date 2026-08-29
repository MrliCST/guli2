package com.atlearn.guli;

import com.atlearn.guli.domain.RmeMemberImg;

/**
 * 会员远程服务接口
 *
 * @author guli
 */
public interface RemoteMemberService {

    /**
     * 注册会员
     *
     * @param bo 会员注册信息
     * @return 注册成功的会员id
     */
    Long register(RmeMemberImg bo);

    /**
     * 按条件查询会员（username / email 任一非空即作为查询条件）
     *
     * @param img 查询条件
     * @return 会员镜像对象，未找到返回 null
     */
    RmeMemberImg findByCondition(RmeMemberImg img);

}
