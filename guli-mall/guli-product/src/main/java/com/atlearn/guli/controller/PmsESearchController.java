package com.atlearn.guli.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.core.domain.R;
import com.atlearn.guli.dto.ESearchParam;
import com.atlearn.guli.dto.SkuEsModel;
import com.atlearn.guli.service.IPmsESearchService;

import java.util.List;

/**
 * ES商品检索
 *
 * @author mayao
 * @date 2026-08-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/esearch")
public class PmsESearchController {

    private final IPmsESearchService pmsESearchService;

    /**
     * 商品检索
     */
    @PostMapping
    public R<List<SkuEsModel>> esearch(@RequestBody ESearchParam param) {
        return R.ok(pmsESearchService.esearch(param));
    }
}
