package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.GoodsType;

import java.util.List;

public interface GoodsTypeService extends IService<GoodsType> {
    List<GoodsType> listAllNoDeleted();
}
