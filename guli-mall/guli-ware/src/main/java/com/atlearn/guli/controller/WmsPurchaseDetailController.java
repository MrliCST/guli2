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
import com.atlearn.guli.domain.vo.WmsPurchaseDetailVo;
import com.atlearn.guli.domain.bo.WmsPurchaseDetailBo;
import com.atlearn.guli.service.IWmsPurchaseDetailService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 采购单详情
 * 前端访问路由地址为:/guli/purchaseDetail
 *
 * @author mayao
 * @date 2026-08-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/purchaseDetail")
public class WmsPurchaseDetailController extends BaseController {

    private final IWmsPurchaseDetailService wmsPurchaseDetailService;

    /**
     * 查询采购单详情列表
     */
    @SaCheckPermission("guli:purchaseDetail:list")
    @GetMapping("/list")
    public TableDataInfo<WmsPurchaseDetailVo> list(WmsPurchaseDetailBo bo, PageQuery pageQuery) {
        return wmsPurchaseDetailService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出采购单详情列表
     */
    @SaCheckPermission("guli:purchaseDetail:export")
    @Log(title = "采购单详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WmsPurchaseDetailBo bo, HttpServletResponse response) {
        List<WmsPurchaseDetailVo> list = wmsPurchaseDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "采购单详情", WmsPurchaseDetailVo.class, response);
    }

    /**
     * 获取采购单详情详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("guli:purchaseDetail:query")
    @GetMapping("/{id}")
    public R<WmsPurchaseDetailVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(wmsPurchaseDetailService.queryById(id));
    }

    /**
     * 新增采购单详情
     */
    @SaCheckPermission("guli:purchaseDetail:add")
    @Log(title = "采购单详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WmsPurchaseDetailBo bo) {
        return toAjax(wmsPurchaseDetailService.insertByBo(bo));
    }

    /**
     * 修改采购单详情
     */
    @SaCheckPermission("guli:purchaseDetail:edit")
    @Log(title = "采购单详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WmsPurchaseDetailBo bo) {
        return toAjax(wmsPurchaseDetailService.updateByBo(bo));
    }

    /**
     * 删除采购单详情
     *
     * @param ids 主键串
     */
    @SaCheckPermission("guli:purchaseDetail:remove")
    @Log(title = "采购单详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(wmsPurchaseDetailService.deleteWithValidByIds(List.of(ids), true));
    }
}
