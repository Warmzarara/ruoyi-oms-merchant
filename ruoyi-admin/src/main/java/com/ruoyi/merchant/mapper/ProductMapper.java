package com.ruoyi.merchant.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.ruoyi.merchant.domain.Product;
import com.ruoyi.merchant.domain.dto.OrderProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProductMapper extends MPJBaseMapper<Product> {
    @Update("<script>" +
            "<foreach collection='list' item='item' separator=';'>" +
            "UPDATE T_PRODUCT " +
            "SET STOCK = STOCK - #{item.num} " +
            "WHERE id = #{item.productId} " +
            "AND stock &gt;= #{item.num}" + // 注意这里要用转义字符 &gt;= 代表 >=
            "</foreach>" +
            "</script>")
    int batchDecreaseProductStock(@Param("list") List<OrderProductDTO> list);
}
