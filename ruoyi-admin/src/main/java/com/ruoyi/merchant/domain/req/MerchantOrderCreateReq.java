package com.ruoyi.merchant.domain.req;

import com.ruoyi.merchant.domain.dto.OrderProductDTO;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MerchantOrderCreateReq {
    @NotBlank(message = "商品id不能为空")
    private String productId;

    @NotBlank(message = "下单件量不能为空")
    @Min(value = 0, message = "下单件数不能小于0")
    private String num;
    
    /** 0为散户，1为合作客户 */
    @NotNull(message = "客户类型不能为空")
    @Range(min = 0, max = 1, message = "客户类型错误")
    private Integer customerType;
    
    @NotBlank(message = "客户不能为空")
    private String customerName;
    
    @NotBlank(message = "客户id不能为空")
    private String customerId;
    
    @NotBlank(message = "收件人电话不能为空")
    private String receiverPhone;
    
    @NotBlank(message = "收件人名称不能为空")
    private String receiverName;
    
    @NotBlank(message = "收件人地址不能为空")
    private String receiverAddress;
    
    @Valid
    @NotEmpty(message = "下单商品为空")
    private List<OrderProductDTO> orderProductDTOList;
    
    /** 订单备注 */
    private String remark;
}
