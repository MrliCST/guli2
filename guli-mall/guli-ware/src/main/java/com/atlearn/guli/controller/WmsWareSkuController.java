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
import com.atlearn.guli.domain.vo.WmsWareSkuVo;
import com.atlearn.guli.domain.bo.WmsWareSkuBo;
import com.atlearn.guli.service.IWmsWareSkuService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商品库存
 * 前端访问路由地址为:/guli/wareSku
 *
 * @author mayao
 * @date 2026-08-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wareSku")
public class WmsWareSkuController extends BaseController {

    private final IWmsWareSkuService wmsWareSkuService;

    /**
     * 查询商品库存列表
     */
    @SaCheckPermission("guli:wareSku:list")
    @GetMapping("/list")
    public TableDataInfo<WmsWareSkuVo> list(WmsWareSkuBo bo, PageQuery pageQuery) {
        return wmsWareSkuService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品库存列表
     */
    @SaCheckPermission("guli:wareSku:export")
    @Log(title = "商品库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WmsWareSkuBo bo, HttpServletResponse response) {
        List<WmsWareSkuVo> list = wmsWareSkuService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品库存", WmsWareSkuVo.class, response);
    }

    /**
     * 获取商品库存详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("guli:wareSku:query")
    @GetMapping("/{id}")
    public R<WmsWareSkuVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(wmsWareSkuService.queryById(id));
    }

    /**
     * 新增商品库存
     */
    @SaCheckPermission("guli:wareSku:add")
    @Log(title = "商品库存", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WmsWareSkuBo bo) {
        return toAjax(wmsWareSkuService.insertByBo(bo));
    }

    /**
     * 修改商品库存
     */
    @SaCheckPermission("guli:wareSku:edit")
    @Log(title = "商品库存", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WmsWareSkuBo bo) {
        return toAjax(wmsWareSkuService.updateByBo(bo));
    }

    /**
     * 删除商品库存
     *
     * @param ids 主键串
     */
    @SaCheckPermission("guli:wareSku:remove")
    @Log(title = "商品库存", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(wmsWareSkuService.deleteWithValidByIds(List.of(ids), true));
    }
}
