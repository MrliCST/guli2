package com.atlearn.guli.dubbo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.atlearn.guli.RemoteMemberService;
import com.atlearn.guli.domain.UmsMember;
import com.atlearn.guli.domain.UmsMemberReceiveAddress;
import com.atlearn.guli.domain.bo.RmeMemberBo;
import com.atlearn.guli.domain.vo.RmeMemberReceiveAddressVO;
import com.atlearn.guli.domain.vo.RmeMemberVo;
import com.atlearn.guli.mapper.UmsMemberMapper;
import com.atlearn.guli.mapper.UmsMemberReceiveAddressMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员远程服务实现
 *
 * @author guli
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteMemberServiceImpl implements RemoteMemberService {

    private final UmsMemberMapper umsMemberMapper;
    private final UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public Long insertMember(RmeMemberBo bo) {
        UmsMember member = new UmsMember();
        member.setUsername(bo.getUsername());
        member.setPassword(bo.getPassword());
        member.setEmail(bo.getEmail());
        member.setStatus(1);
        member.setSourceType(1);
        member.setGrowth(0);
        member.setIntegration(0);
        umsMemberMapper.insert(member);
        return member.getId();
    }

    @Override
    public RmeMemberVo findByCondition(RmeMemberBo bo) {
        LambdaQueryWrapper<UmsMember> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(bo.getUsername())) {
            wrapper.eq(x -> x.getUsername(), bo.getUsername());
        }
        if (StrUtil.isNotBlank(bo.getEmail())) {
            wrapper.eq(x -> x.getEmail(), bo.getEmail());
        }
        if (wrapper.getCustomSqlSegment() == null || wrapper.getCustomSqlSegment().isBlank()) {
            return null;
        }
        UmsMember member = umsMemberMapper.selectOne(wrapper);

        if (member == null) return null;
        return BeanUtil.copyProperties(member, RmeMemberVo.class);
    }

    @Override
    public List<RmeMemberReceiveAddressVO> getReceiveAddressList(Long memberId) {
        List<UmsMemberReceiveAddress> list = umsMemberReceiveAddressMapper.selectList(
            new LambdaQueryWrapper<UmsMemberReceiveAddress>().eq(x -> x.getMemberId(), memberId)
        );
        return BeanUtil.copyToList(list, RmeMemberReceiveAddressVO.class);
    }
}
