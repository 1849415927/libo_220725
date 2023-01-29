package com.libo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.libo.common.BaseDataPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author junji
 * @since 2023-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("luo_jiaoyue")
@ApiModel(value="LuoJiaoyue对象", description="")
public class LuoJiaoyue extends BaseDataPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "礼金")
    private Long money;

    @ApiModelProperty(value = "住址")
    private String address;

    @ApiModelProperty(value = "礼品")
    private String present;

    @ApiModelProperty(value = "类别（内收，外收，同学）")
    private String type;

    @ApiModelProperty(value = "人员（任浩乾 · 罗娇月）")
    private String personnel;

}
