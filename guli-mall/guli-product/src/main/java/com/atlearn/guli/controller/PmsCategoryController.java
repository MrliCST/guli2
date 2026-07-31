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
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.bo.PmsCategoryBo;
import com.atlearn.guli.service.IPmsCategoryService;

/**
 * 商品三级分类
 * 前端访问路由地址为:/guli/category
 *
 * @author mayao
 * @date 2026-07-25
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class PmsCategoryController extends BaseController {

    private final IPmsCategoryService pmsCategoryService;

    /**
     * 查询商品三级分类树列表
     */
    @SaCheckPermission("guli:category:list")
    @GetMapping("/list/tree")
    public R<List<PmsCategoryVo>> list(PmsCategoryBo bo) {
        return R.ok(pmsCategoryService.queryTreeList(bo));
    }

    /**
     * 导出商品三级分类列表
     */
    @SaCheckPermission("guli:category:export")
    @Log(title = "商品三级分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PmsCategoryBo bo, HttpServletResponse response) {
        List<PmsCategoryVo> list = pmsCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品三级分类", PmsCategoryVo.class, response);
    }

    /**
     * 获取商品三级分类详细信息
     *
     * @param catId 主键
     */
    @SaCheckPermission("guli:category:query")
    @GetMapping("/{catId}")
    public R<PmsCategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("catId") Long catId) {
        return R.ok(pmsCategoryService.queryById(catId));
    }

    /**
     * 新增商品三级分类
     */
    @SaCheckPermission("guli:category:add")
    @Log(title = "商品三级分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PmsCategoryBo bo) {
        return toAjax(pmsCategoryService.insertByBo(bo));
    }

    /**
     * 修改商品三级分类
     */
    @SaCheckPermission("guli:category:edit")
    @Log(title = "商品三级分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PmsCategoryBo bo) {
        return toAjax(pmsCategoryService.updateByBo(bo));
    }

    /**
     * 批量修改商品三级分类
     */
    @SaCheckPermission("guli:category:edit")
    @Log(title = "商品三级分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/batch")
    public R<Void> editBatch(@Validated(EditGroup.class) @RequestBody List<PmsCategoryBo> boList) {
        return toAjax(pmsCategoryService.updateBatchByBo(boList));
    }

    /**
     * 删除商品三级分类
     *
     * @param catIds 主键串
     */
    @SaCheckPermission("guli:category:remove")
    @Log(title = "商品三级分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{catIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("catIds") Long[] catIds) {
        return toAjax(pmsCategoryService.deleteWithValidByIds(List.of(catIds), true));
    }
}
