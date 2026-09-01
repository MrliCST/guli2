package com.atlearn.guli.dubbo;

import com.atlearn.guli.RemoteOrderService;
import com.atlearn.guli.domain.OmsOrder;
import com.atlearn.guli.mapper.OmsOrderMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * 订单远程服务实现
 *
 * @author guli
 */
@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteOrderServiceImpl implements RemoteOrderService {

    private final OmsOrderMapper orderMapper;

    @Override
    @SuppressWarnings("null")//null抑制警告
    public Integer getOrderStatusBySn(String orderSn) {
        OmsOrder order = orderMapper.selectOne(
            new LambdaQueryWrapper<OmsOrder>()
                .select(OmsOrder::getStatus)
                .eq(OmsOrder::getOrderSn, orderSn)
        );
        return order == null ? null : order.getStatus();
    }
}
