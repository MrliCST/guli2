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
import com.atlearn.guli.domain.vo.WmsPurchaseVo;
import com.atlearn.guli.domain.bo.WmsPurchaseBo;
import com.atlearn.guli.service.IWmsPurchaseService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 采购信息
 * 前端访问路由地址为:/guli/purchase
 *
 * @author mayao
 * @date 2026-08-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/purchase")
public class WmsPurchaseController extends BaseController {

    private final IWmsPurchaseService wmsPurchaseService;

    /**
     * 查询采购信息列表
     */
    @SaCheckPermission("guli:purchase:list")
    @GetMapping("/list")
    public TableDataInfo<WmsPurchaseVo> list(WmsPurchaseBo bo, PageQuery pageQuery) {
        return wmsPurchaseService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出采购信息列表
     */
    @SaCheckPermission("guli:purchase:export")
    @Log(title = "采购信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WmsPurchaseBo bo, HttpServletResponse response) {
        List<WmsPurchaseVo> list = wmsPurchaseService.queryList(bo);
        ExcelUtil.exportExcel(list, "采购信息", WmsPurchaseVo.class, response);
    }

    /**
     * 获取采购信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("guli:purchase:query")
    @GetMapping("/{id}")
    public R<WmsPurchaseVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(wmsPurchaseService.queryById(id));
    }

    /**
     * 新增采购信息
     */
    @SaCheckPermission("guli:purchase:add")
    @Log(title = "采购信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WmsPurchaseBo bo) {
        return toAjax(wmsPurchaseService.insertByBo(bo));
    }

    /**
     * 修改采购信息
     */
    @SaCheckPermission("guli:purchase:edit")
    @Log(title = "采购信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WmsPurchaseBo bo) {
        return toAjax(wmsPurchaseService.updateByBo(bo));
    }

    /**
     * 删除采购信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("guli:purchase:remove")
    @Log(title = "采购信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(wmsPurchaseService.deleteWithValidByIds(List.of(ids), true));
    }
}
