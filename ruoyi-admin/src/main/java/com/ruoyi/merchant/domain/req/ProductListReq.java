package com.ruoyi.merchant.domain.req;

import com.ruoyi.common.model.BasePage;
import lombok.Data;

@Data
public class ProductListReq extends BasePage {
    private String title;
    private Integer status;
}
