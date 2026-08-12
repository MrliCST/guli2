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
import com.atlearn.guli.domain.vo.WmsWareInfoVo;
import com.atlearn.guli.domain.bo.WmsWareInfoBo;
import com.atlearn.guli.service.IWmsWareInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 仓库信息
 * 前端访问路由地址为:/guli/wareInfo
 *
 * @author mayao
 * @date 2026-08-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wareInfo")
public class WmsWareInfoController extends BaseController {

    private final IWmsWareInfoService wmsWareInfoService;

    /**
     * 查询仓库信息列表
     */
    @SaCheckPermission("guli:wareInfo:list")
    @GetMapping("/list")
    public TableDataInfo<WmsWareInfoVo> list(WmsWareInfoBo bo, PageQuery pageQuery) {
        return wmsWareInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出仓库信息列表
     */
    @SaCheckPermission("guli:wareInfo:export")
    @Log(title = "仓库信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WmsWareInfoBo bo, HttpServletResponse response) {
        List<WmsWareInfoVo> list = wmsWareInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "仓库信息", WmsWareInfoVo.class, response);
    }

    /**
     * 获取仓库信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("guli:wareInfo:query")
    @GetMapping("/{id}")
    public R<WmsWareInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(wmsWareInfoService.queryById(id));
    }

    /**
     * 新增仓库信息
     */
    @SaCheckPermission("guli:wareInfo:add")
    @Log(title = "仓库信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WmsWareInfoBo bo) {
        return toAjax(wmsWareInfoService.insertByBo(bo));
    }

    /**
     * 修改仓库信息
     */
    @SaCheckPermission("guli:wareInfo:edit")
    @Log(title = "仓库信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WmsWareInfoBo bo) {
        return toAjax(wmsWareInfoService.updateByBo(bo));
    }

    /**
     * 删除仓库信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("guli:wareInfo:remove")
    @Log(title = "仓库信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(wmsWareInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
