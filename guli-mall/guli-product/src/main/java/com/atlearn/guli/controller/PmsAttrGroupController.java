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
import com.atlearn.guli.domain.vo.PmsAttrGroupVo;
import com.atlearn.guli.domain.bo.PmsAttrGroupBo;
import com.atlearn.guli.service.IPmsAttrGroupService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 属性分组
 * 前端访问路由地址为:/guli/attrGroup
 *
 * @author mayao
 * @date 2026-08-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/platformAttr/attrGroup")
public class PmsAttrGroupController extends BaseController {

    private final IPmsAttrGroupService pmsAttrGroupService;

    /**
     * 查询属性分组列表
     */
    @SaCheckPermission("guli:attrGroup:list")
    @GetMapping("/list")
    public TableDataInfo<PmsAttrGroupVo> list(PmsAttrGroupBo bo, PageQuery pageQuery) {
        // 没有指定分类id，返回空兜底
        if (bo.getCatelogId() == null) {
            return TableDataInfo.build();
        }
        return pmsAttrGroupService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出属性分组列表
     */
    @SaCheckPermission("guli:attrGroup:export")
    @Log(title = "属性分组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PmsAttrGroupBo bo, HttpServletResponse response) {
        List<PmsAttrGroupVo> list = pmsAttrGroupService.queryList(bo);
        ExcelUtil.exportExcel(list, "属性分组", PmsAttrGroupVo.class, response);
    }

    /**
     * 获取属性分组详细信息
     *
     * @param attrGroupId 主键
     */
    @SaCheckPermission("guli:attrGroup:query")
    @GetMapping("/{attrGroupId}")
    public R<PmsAttrGroupVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("attrGroupId") Long attrGroupId) {
        return R.ok(pmsAttrGroupService.queryById(attrGroupId));
    }

    /**
     * 新增属性分组
     */
    @SaCheckPermission("guli:attrGroup:add")
    @Log(title = "属性分组", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PmsAttrGroupBo bo) {
        return toAjax(pmsAttrGroupService.insertByBo(bo));
    }

    /**
     * 修改属性分组
     */
    @SaCheckPermission("guli:attrGroup:edit")
    @Log(title = "属性分组", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PmsAttrGroupBo bo) {
        return toAjax(pmsAttrGroupService.updateByBo(bo));
    }

    /**
     * 删除属性分组
     *
     * @param attrGroupIds 主键串
     */
    @SaCheckPermission("guli:attrGroup:remove")
    @Log(title = "属性分组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{attrGroupIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("attrGroupIds") Long[] attrGroupIds) {
        return toAjax(pmsAttrGroupService.deleteWithValidByIds(List.of(attrGroupIds), true));
    }
}
