package com.atlearn.guli.dubbo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.atlearn.guli.RemoteMemberService;
import com.atlearn.guli.domain.RmeMemberImg;
import com.atlearn.guli.domain.UmsMember;
import com.atlearn.guli.mapper.UmsMemberMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

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

    @Override
    public Long register(RmeMemberImg bo) {
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
    public RmeMemberImg findByCondition(RmeMemberImg img) {
        LambdaQueryWrapper<UmsMember> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(img.getUsername())) {
            wrapper.eq(x -> x.getUsername(), img.getUsername());
        }
        if (StrUtil.isNotBlank(img.getEmail())) {
            wrapper.eq(x -> x.getEmail(), img.getEmail());
        }
        if (wrapper.getCustomSqlSegment() == null || wrapper.getCustomSqlSegment().isBlank()) {
            return null;
        }
        UmsMember member = umsMemberMapper.selectOne(wrapper);
        
        if (member == null) return null;
        return BeanUtil.copyProperties(member, RmeMemberImg.class);
    }
}
