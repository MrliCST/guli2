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
import com.atlearn.guli.domain.vo.PmsAttrVo;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.bo.PmsAttrBo;
import com.atlearn.guli.service.IPmsAttrService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商品属性
 * 前端访问路由地址为:/product/keyValStore
 *
 * @author mayao
 * @date 2026-08-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/attribute/keyValStore")
public class PmsAttrController extends BaseController {

    private final IPmsAttrService pmsAttrService;

    /*
     *  ---- 商品属性 CRUD ----
     *  56  /list            GET    分页列表
     *  66  /export          POST   导出
     *  78  /{attrId}        GET    详情
     *  90  /                POST   新增
     *  101 /                PUT    修改
     *  113 /{attrIds}       DELETE 删除
     *  123 /treeCategory    GET    分类树
     */

    /**
     * 查询商品属性列表
     */
    @SaCheckPermission("guli:keyValStore:list")
    @GetMapping("/list")
    public TableDataInfo<PmsAttrVo> list(PmsAttrBo bo, PageQuery pageQuery) {
        return pmsAttrService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品属性列表
     */
    @SaCheckPermission("guli:keyValStore:export")
    @Log(title = "商品属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PmsAttrBo bo, HttpServletResponse response) {
        List<PmsAttrVo> list = pmsAttrService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品属性", PmsAttrVo.class, response);
    }

    /**
     * 获取商品属性详细信息
     *
     * @param attrId 主键
     */
    @SaCheckPermission("guli:keyValStore:query")
    @GetMapping("/{attrId}")
    public R<PmsAttrVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("attrId") Long attrId) {
        return R.ok(pmsAttrService.queryById(attrId));
    }

    /**
     * 新增商品属性
     */
    @SaCheckPermission("guli:keyValStore:add")
    @Log(title = "商品属性", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PmsAttrBo bo) {
        return toAjax(pmsAttrService.insertByBo(bo));
    }

    /**
     * 修改商品属性
     */
    @SaCheckPermission("guli:keyValStore:edit")
    @Log(title = "商品属性", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PmsAttrBo bo) {
        return toAjax(pmsAttrService.updateByBo(bo));
    }

    /**
     * 删除商品属性
     *
     * @param attrIds 主键串
     */
    @SaCheckPermission("guli:keyValStore:remove")
    @Log(title = "商品属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{attrIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("attrIds") Long[] attrIds) {
        return toAjax(pmsAttrService.deleteWithValidByIds(List.of(attrIds), true));
    }

    /**
     * 获取分类树数据
     */
    @SaCheckPermission("guli:keyValStore:query")
    @GetMapping("/treeCategory")
    public R<List<PmsCategoryVo>> treeCategory() {
        return R.ok(pmsAttrService.listTreeCategory());
    }
}
