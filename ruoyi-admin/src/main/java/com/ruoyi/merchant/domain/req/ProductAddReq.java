package com.ruoyi.merchant.domain.req;

import com.ruoyi.merchant.domain.dto.ProductImgDTO;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductAddReq {
    /** 商品标题 */
    @NotBlank(message = "商品标题不能为空")
    private String title;
    
    /** 商品描述 */
    @NotBlank(message = "商品描述不能为空")
    private String description;
    
    /** 单价 */
    @NotNull(message = "商品单价不能为空")
    @Min(value = 0, message = "商品单价不能小于0")
    private BigDecimal price;
    
    /** 库存数量 */
    @NotNull(message = "商品不能为空")
    @Min(value = 0, message = "库存数量不能小于0")
    private Integer stock;
    
    /** 状态 0=已下架 1=上架 */
    @NotNull(message = "商品上下架状态不能为空")
    @Range(max = 1, min = 0, message = "商品状态错误")
    private Integer status;
    
    /** 商品图片列表 */
    @Valid
    @NotEmpty(message = "商品至少要上传一张图片")
    private List<ProductImgDTO> prdImgList;
}
