package com.atlearn.guli;

import com.atlearn.guli.domain.RemoteSkuFullReductionBo;
import com.atlearn.guli.domain.RemoteSkuLadderBo;
import com.atlearn.guli.domain.RemoteSpuBoundsBo;

public interface RemoteCouponService {
    /**
     * 新增sku满几减免信息
     * @param bo 远程新增 BO
     * @return 是否新增成功
     */
    Boolean insertSkuFullReductionByBo(RemoteSkuFullReductionBo bo);

    /**
     * 新增sku满几打折信息
     * @param bo
     * @return
     */
    Boolean insertSkuLadderByBo(RemoteSkuLadderBo bo);

    /**
     * 新增spu积分与成长信息
     * @param bo
     * @return
     */
    Boolean insertSpuBoundsByBo(RemoteSpuBoundsBo bo);
}
