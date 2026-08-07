package com.atlearn.guli.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import com.atlearn.guli.domain.vo.PmsBrandVo;
import com.atlearn.guli.domain.vo.PmsCategoryBrandRelationVo;
import com.atlearn.guli.domain.bo.PmsBrandBo;
import com.atlearn.guli.domain.bo.PmsCategoryBrandRelationBo;
import com.atlearn.guli.service.IPmsBrandService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 品牌
 * 前端访问路由地址为:/guli/brand
 *
 * @author mayao
 * @date 2026-07-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/brand")
public class PmsBrandController extends BaseController {

    private final IPmsBrandService pmsBrandService;

    /*
      50-100 基础brand的CURD
      119-159 brand和category关联的CURD
    */

    /**
     * 查询品牌列表
     */
    @SaCheckPermission("guli:brand:list")
    @GetMapping("/list")
    public TableDataInfo<PmsBrandVo> list(PmsBrandBo bo, PageQuery pageQuery) {
        return pmsBrandService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出品牌列表
     */
    @SaCheckPermission("guli:brand:export")
    @Log(title = "品牌", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PmsBrandBo bo, HttpServletResponse response) {
        List<PmsBrandVo> list = pmsBrandService.queryList(bo);
        ExcelUtil.exportExcel(list, "品牌", PmsBrandVo.class, response);
    }

    /**
     * 获取品牌详细信息
     *
     * @param brandId 主键
     */
    @SaCheckPermission("guli:brand:query")
    @GetMapping("/{brandId}")
    public R<PmsBrandVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("brandId") Long brandId) {
        return R.ok(pmsBrandService.queryById(brandId));
    }

    /**
     * 新增品牌
     */
    @SaCheckPermission("guli:brand:add")
    @Log(title = "品牌", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PmsBrandBo bo) {
        return toAjax(pmsBrandService.insertByBo(bo));
    }

    /**
     * 修改品牌
     */
    @SaCheckPermission("guli:brand:edit")
    @Log(title = "品牌", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PmsBrandBo bo) {
        return toAjax(pmsBrandService.updateByBo(bo));
    }

    /**
     * 删除品牌
     *
     * @param brandIds 主键串
     */
    @SaCheckPermission("guli:brand:remove")
    @Log(title = "品牌", businessType = BusinessType.DELETE)
    @DeleteMapping("/{brandIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("brandIds") Long[] brandIds) {
        return toAjax(pmsBrandService.deleteWithValidByIds(List.of(brandIds), true));
    }

    // =========   品牌分类关联业务  =========

    /**
     * 查询品牌分类关联列表
     */
    @SaCheckPermission("guli:categoryBrandRelation:list")
    @GetMapping("/cbrList")
    public TableDataInfo<PmsCategoryBrandRelationVo> listCbr(PmsCategoryBrandRelationBo bo, PageQuery pageQuery) {
        return pmsBrandService.queryCbrPageList(bo, pageQuery);
    }

    /**
     * 获取品牌分类关联详细信息
     *
     * @param cbrId 主键
     */
    @SaCheckPermission("guli:categoryBrandRelation:query")
    @GetMapping("/cbr/{cbrId}")
    public R<PmsCategoryBrandRelationVo> getCbr(@NotNull(message = "主键不能为空")
                                                  @PathVariable("cbrId") Long cbrId) {
        return R.ok(pmsBrandService.queryCbrById(cbrId));
    }

    /**
     * 新增品牌分类关联
     */
    @SaCheckPermission("guli:categoryBrandRelation:add")
    @Log(title = "品牌分类关联", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/cbr")
    public R<Void> addCbr(@Validated(AddGroup.class) @RequestBody PmsCategoryBrandRelationBo bo) {
        return toAjax(pmsBrandService.insertCbrByBo(bo));
    }

    /**
     * 修改品牌分类关联
     */
    @SaCheckPermission("guli:categoryBrandRelation:edit")
    @Log(title = "品牌分类关联", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/cbr")
    public R<Void> editCbr(@Validated(EditGroup.class) @RequestBody PmsCategoryBrandRelationBo bo) {
        return toAjax(pmsBrandService.updateCbrByBo(bo));
    }

    /**
     * 删除品牌分类关联
     *
     * @param cbrIds 主键串
     */
    @SaCheckPermission("guli:categoryBrandRelation:remove")
    @Log(title = "品牌分类关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/cbr/{cbrIds}")
    public R<Void> removeCbr(@NotEmpty(message = "主键不能为空")
                          @PathVariable("cbrIds") Long[] cbrIds) {
        return toAjax(pmsBrandService.deleteCbrWithValidByIds(List.of(cbrIds), true));
    }
}
