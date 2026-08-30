package com.atlearn.guli;

import com.atlearn.guli.domain.bo.RmeMemberBo;
import com.atlearn.guli.domain.vo.RmeMemberReceiveAddressVO;
import com.atlearn.guli.domain.vo.RmeMemberVo;

import java.util.List;

/**
 * 会员远程服务接口
 *
 * @author guli
 */
public interface RemoteMemberService {

    /**
     * 新增会员
     *
     * @param bo 会员信息
     * @return 新增成功的会员id
     */
    Long insertMember(RmeMemberBo bo);

    /**
     * 按条件查询会员（username / email 任一非空即作为查询条件）
     *
     * @param bo 查询条件
     * @return 会员 VO，未找到返回 null
     */
    RmeMemberVo findByCondition(RmeMemberBo bo);

    /**
     * 根据 memberId 获取收货地址列表
     *
     * @param memberId 会员id
     * @return 收货地址列表
     */
    List<RmeMemberReceiveAddressVO> getReceiveAddressList(Long memberId);

}
