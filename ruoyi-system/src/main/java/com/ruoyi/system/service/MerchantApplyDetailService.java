package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.MerchantApplyDetail;
import com.ruoyi.system.domain.dto.OrderPictureApplyDTO;

import java.util.List;

public interface MerchantApplyDetailService extends IService<MerchantApplyDetail> {
    List<OrderPictureApplyDTO> listPicturesByApplyDetailIds(List<String> idList);
}
