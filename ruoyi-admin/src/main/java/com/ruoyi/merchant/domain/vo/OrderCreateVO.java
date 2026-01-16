package com.ruoyi.merchant.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.merchant.domain.dto.ProductListDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderCreateVO {
    // 订单id
    private String orderId;
    
    // 订单总金额
    private BigDecimal totalPrice;
    
    // 订单创建时间
    @JsonFormat()
    private Date orderCreatedTime;
    
    // 订单商品列表
    private List<ProductListDTO> prdDTOLIst;
    
    // 收件人姓名
    private String receiverName;
    
    // 收件人电话
    private String receiverPhone;
    
    // 收件人地址
    private String receiverAddress;
    
    // 商品缩略图
    private String showImage;
    
    // 下单时间
    private Date orderCreateTime;
}
