package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ruoyi.system.domain.ApplyPicture;
import com.ruoyi.system.domain.MerchantApplyDetail;
import com.ruoyi.system.domain.dto.OrderPictureApplyDTO;
import com.ruoyi.system.mapper.MerchantApplyDetailMapper;
import com.ruoyi.system.service.MerchantApplyDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantApplyDetailServiceImpl extends MPJBaseServiceImpl<MerchantApplyDetailMapper,MerchantApplyDetail> implements MerchantApplyDetailService {
    @Override
    public List<OrderPictureApplyDTO> listPicturesByApplyDetailIds(List<String> idList) {
        MPJLambdaWrapper<MerchantApplyDetail> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAs(MerchantApplyDetail::getId, OrderPictureApplyDTO::getApplyDetailId);
        wrapper.selectAll(ApplyPicture.class);
        wrapper.from(from -> from.selectAll().in(MerchantApplyDetail::getId,idList));
        wrapper.leftJoin(ApplyPicture.class, on -> on.eq(ApplyPicture::getApplyDetailId, MerchantApplyDetail::getId));
        return selectJoinList(OrderPictureApplyDTO.class, wrapper);
    }
}
