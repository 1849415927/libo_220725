package com.libo.excelexport;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/*
@ExcelProperty：核心注解，value属性可用来设置表头名称，converter属性可以用来设置类型转换器；
@ColumnWidth：用于设置表格列的宽度；
@DateTimeFormat：用于设置日期转换格式。
* */

@Data
@ColumnWidth(25)
@HeadRowHeight(30)
@ContentRowHeight(20)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LiboExcelExport implements Serializable {
    /**
     * 唯一id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名",index = 0)
    @NotNull(message = "姓名为空")
    private String name;

    /**
     * 礼金
     */
    @ExcelProperty(value = "礼金",index = 1)
    @NotNull(message = "礼金为空")
    private Long money;

    /**
     * 住址
     */
    @ExcelProperty(value= "住址",index = 2)
    @NotNull(message = "住址为空")
    private String address;

    /**
     * 礼品
     */
    @ExcelProperty(value = "礼品",index = 3)
    private String present;

    /**
     * 类别（内收，外收，同学）
     */
    @ExcelProperty(value = "类别（内收，外收，同学）",index = 4)
    @NotNull(message = "类别为空")
    private String type;

}
