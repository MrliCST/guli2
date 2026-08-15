package com.atlearn.guli.controller;

import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.log.enums.BusinessType;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.vo.PmsCategoryBrandRelationVo;
import com.atlearn.guli.domain.vo.PmsAttrGroupWithAttrsVo;
import com.atlearn.guli.domain.vo.PmsAttrVo;
import com.atlearn.guli.domain.bo.PmsSpuBo;
import com.atlearn.guli.service.IPmsSpuService;

import java.util.List;

/**
 * SPU信息
 * 前端访问路由地址为:/product/maintain/release
 *
 * @author mayao
 * @date 2026-08-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/maintain")
public class PmsSpuController extends BaseController {

    private final IPmsSpuService pmsSpuService;

    /*
     *  ---- SPU信息 CRUD ----
     *  53  /release          POST   新增
     *  62  /treeCategory     GET    分类树
     *  71  /brands           GET    品牌列表(按分类)
     *  80  /attrGroups       GET    属性分组及属性值储(按分类)
     *  89  /saleAttrs        GET    销售属性列表
     * 
     *  99  /up               POST   上架到 ESearch
     */

    /**
     * 新增spu信息
     */
    @SaCheckPermission("guli:release:add")
    @Log(title = "spu信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/release")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PmsSpuBo bo) {
        return toAjax(pmsSpuService.insertByBo(bo));
    }

    /**
     * 获取分类树
     */
    @SaCheckPermission("guli:release:query")
    @GetMapping("/treeCategory")
    public R<List<PmsCategoryVo>> treeCategory() {
        return R.ok(pmsSpuService.listTreeCategory());
    }

    /**
     * 根据分类id查询品牌列表
     */
    @SaCheckPermission("guli:release:query")
    @GetMapping("/brands")
    public R<List<PmsCategoryBrandRelationVo>> listBrands(@RequestParam("catelogId") Long catelogId) {
        return R.ok(pmsSpuService.listBrandsByCategoryId(catelogId));
    }

    /**
     * 根据分类id查询属性分组及其属性列表
     */
    @SaCheckPermission("guli:release:query")
    @GetMapping("/attrGroups")
    public R<List<PmsAttrGroupWithAttrsVo>> listBaseAttrs(@RequestParam("catelogId") Long catelogId) {
        return R.ok(pmsSpuService.listBaseAttrs(catelogId));
    }

    /**
     * 根据分类id查询销售属性列表
     */
    @SaCheckPermission("guli:release:query")
    @GetMapping("/saleAttrs")
    public R<List<PmsAttrVo>> listSaleAttrs(@RequestParam("catelogId") Long catelogId) {
        return R.ok(pmsSpuService.listSaleAttrs(catelogId));
    }


    /**
     * 上架到ESearch
     */
    @SaCheckPermission("guli:release:edit")
    @PostMapping("/up")
    public R<Void> up(@RequestBody Long spuId) {
        return toAjax(pmsSpuService.upToEsearch(spuId));
    }
}
