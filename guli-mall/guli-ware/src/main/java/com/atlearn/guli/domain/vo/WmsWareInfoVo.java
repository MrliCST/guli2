package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.WmsWareInfo;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 仓库信息视图对象 wms_ware_info
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WmsWareInfo.class)
public class WmsWareInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 仓库名
     */
    @ExcelProperty(value = "仓库名")
    private String name;

    /**
     * 仓库地址
     */
    @ExcelProperty(value = "仓库地址")
    private String address;

    /**
     * 区域编码
     */
    @ExcelProperty(value = "区域编码")
    private String areacode;


}
