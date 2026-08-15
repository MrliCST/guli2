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
import com.atlearn.guli.domain.vo.PmsAttrVo;
import com.atlearn.guli.domain.vo.PmsAttrAttrgroupRelationVo;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.bo.PmsAttrGroupBo;
import com.atlearn.guli.domain.bo.PmsAttrBo;
import com.atlearn.guli.domain.bo.PmsAttrAttrgroupRelationBo;
import com.atlearn.guli.service.IPmsAttrGroupService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 属性分组
 * 前端访问路由地址为:/product/attrGroup
 *
 * @author mayao
 * @date 2026-08-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/attribute/attrGroup")
public class PmsAttrGroupController extends BaseController {

    private final IPmsAttrGroupService pmsAttrGroupService;

    /*
     *  ---- 属性分组基础 CRUD ----
     *  67  /list                GET    分页列表
     *  81  /export              POST   导出
     *  93  /{attrGroupId}       GET    详情
     *  105 /                    POST   新增
     *  116 /                    PUT    修改
     *  128 /{attrGroupIds}      DELETE 删除
     *  138 /treeCategory        GET    分类树
     *
     *  ---- 属性分组-属性值储关联 CRUD ----
     *  149 /availableAttrs      GET    可关联属性列表
     *  158 /relations           GET    关联列表
     *  168 /relation            POST   新增关联
     *  178 /relation            PUT    修改关联
     *  188 /relation/{ids}      DELETE 删除关联
     */

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

    /**
     * 获取分类树数据
     */
    @SaCheckPermission("guli:attrGroup:query")
    @GetMapping("/treeCategory")
    public R<List<PmsCategoryVo>> treeCategory() {
        return R.ok(pmsAttrGroupService.listTreeCategory());
    }

    // =========   属性分组-属性值储关联业务  =========

    /**
     * 查询可关联的属性列表（过滤已关联的）
     */
    @SaCheckPermission("guli:attrGroup:query")
    @GetMapping("/availableAttrs")
    public R<List<PmsAttrVo>> listAvailableAttrs(PmsAttrBo bo) {
        return R.ok(pmsAttrGroupService.listAvailableAttrs(bo.getCatelogId()));
    }

    /**
     * 查询关联列表（附带属性名和分组名）
     */
    @SaCheckPermission("guli:attrGroup:query")
    @GetMapping("/relations")
    public TableDataInfo<PmsAttrAttrgroupRelationVo> listRelations(PmsAttrAttrgroupRelationBo bo, PageQuery pageQuery) {
        return pmsAttrGroupService.listRelations(bo.getAttrGroupId(), pageQuery);
    }

    /**
     * 新增关联
     */
    @SaCheckPermission("guli:attrGroup:add")
    @Log(title = "属性分组关联", businessType = BusinessType.INSERT)
    @PostMapping("/relation")
    public R<Void> addRelation(@Validated(AddGroup.class) @RequestBody PmsAttrAttrgroupRelationBo bo) {
        return toAjax(pmsAttrGroupService.insertRelation(bo));
    }

    /**
     * 修改关联（仅排序）
     */
    @SaCheckPermission("guli:attrGroup:edit")
    @Log(title = "属性分组关联", businessType = BusinessType.UPDATE)
    @PutMapping("/relation")
    public R<Void> editRelation(@Validated(EditGroup.class) @RequestBody PmsAttrAttrgroupRelationBo bo) {
        return toAjax(pmsAttrGroupService.updateRelation(bo));
    }

    /**
     * 删除关联
     */
    @SaCheckPermission("guli:attrGroup:remove")
    @Log(title = "属性分组关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/relation/{ids}")
    public R<Void> removeRelation(@NotEmpty(message = "主键不能为空")
                                   @PathVariable("ids") Long[] ids) {
        return toAjax(pmsAttrGroupService.deleteRelationWithValidByIds(List.of(ids), true));
    }
}
