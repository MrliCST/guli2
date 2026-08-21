package com.atlearn.guli.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.dromara.common.core.domain.R;

import com.atlearn.guli.domain.vo.PmsSkuItemVo;
import com.atlearn.guli.dto.ESearchListVo;
import com.atlearn.guli.dto.ESearchParam;
import com.atlearn.guli.service.IPmsDisplayService;

/**
 * 商品展示
 * 前端访问路由地址为:/product/display
 *
 * @author mayao
 * @date 2026-08-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/display")
public class PmsDisplayController {

    private final IPmsDisplayService pmsDisplayService;

    /**
     * 商品检索
     */
    @PostMapping("/esearch")
    public R<ESearchListVo> esearch(@RequestBody ESearchParam param) {
        return R.ok(pmsDisplayService.esearch(param));
    }

    /**
     * sku详情
     */
    @GetMapping("/item/{spuId}")
    public R<PmsSkuItemVo> item(@PathVariable Long spuId) {
        return R.ok(pmsDisplayService.item(spuId));
    }
}
