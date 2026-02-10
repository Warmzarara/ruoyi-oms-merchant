package com.ruoyi.merchant.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.merchant.domain.Customer;
import com.ruoyi.merchant.domain.Order;
import com.ruoyi.merchant.domain.OrderProduct;
import com.ruoyi.merchant.domain.Product;
import com.ruoyi.merchant.domain.dto.OrderProductDTO;
import com.ruoyi.merchant.domain.req.MerchantOrderCreateReq;
import com.ruoyi.merchant.enums.OrderStatusEnum;
import com.ruoyi.merchant.manager.OrderManager;
import com.ruoyi.merchant.manager.OrderProductManager;
import com.ruoyi.merchant.manager.ProductManager;
import com.ruoyi.merchant.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Resource
    private OrderManager orderManager;
    
    @Resource
    private OrderProductManager orderProductManager;
    
    @Resource
    private ProductManager productManager;
    
    @Override
    @Transactional
    public void createOrder(MerchantOrderCreateReq merchantOrderCreateReq, Map<String, Product> dbProductIdMap, Customer customer, Date orderCreateTime, String orderId, BigDecimal totalPrice) {
        // 检查库存是否充足
        checkProductStock(merchantOrderCreateReq, dbProductIdMap);

        // 批量扣减商品库存
        batchDecreaseStock(merchantOrderCreateReq);

        // 插入订单主表
        saveOrder(merchantOrderCreateReq, customer, totalPrice, orderCreateTime, orderId);

        // 插入订单详情表
        saveOrderProduct(merchantOrderCreateReq, dbProductIdMap, orderId, orderCreateTime);
    }

    private static void checkProductStock(MerchantOrderCreateReq merchantOrderCreateReq, Map<String, Product> dbProductIdMap) {
        List<OrderProductDTO> insufficientProductList = merchantOrderCreateReq.getOrderProductDTOList()
                .stream().filter(dto ->
                        NumberUtil.compare(dto.getNum(), dbProductIdMap.get(dto.getProductId()).getStock()) > 0)
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(insufficientProductList)) {
            throw new ServiceException("商品库存不足", insufficientProductList);
        }
    }

    private void batchDecreaseStock(MerchantOrderCreateReq merchantOrderCreateReq) {
        List<OrderProductDTO> orderProductDTOList = merchantOrderCreateReq.getOrderProductDTOList();
        for (OrderProductDTO orderProductDTO : orderProductDTOList) {
            if (!productManager.decreaseProductStock(orderProductDTO.getProductId(), orderProductDTO.getNum())) {
                throw new ServiceException("下单失败，下单期间库存有变动，请重试");
            }
        }
    }

    private void saveOrder(MerchantOrderCreateReq merchantOrderCreateReq, Customer customer, BigDecimal totalPrice, Date orderCreateTime, String orderId) {
        // 组装订单数据
        Order order = assembleOrder(merchantOrderCreateReq, customer, totalPrice, orderCreateTime, orderId);

        // 更新订单
        if (!orderManager.save(order)) {
            throw new ServiceException("下单失败，订单数据插入失败");
        }
    }

    private void saveOrderProduct(MerchantOrderCreateReq merchantOrderCreateReq, Map<String, Product> dbProductIdMap, String orderId, Date orderCreateTime) {
        // 组装订单列表
        List<OrderProduct> orderProductList = merchantOrderCreateReq.getOrderProductDTOList().stream()
                .map(orderProductDTO ->
                        assembleOrderProduct(orderProductDTO, dbProductIdMap, orderId, orderCreateTime))
                .collect(Collectors.toList());
        // 插入订单详情信息
        if (!orderProductManager.saveBatch(orderProductList)) {
            throw new ServiceException("下单失败，商品详情插入失败");
        }
    }

    private static OrderProduct assembleOrderProduct(OrderProductDTO orderProductDTO, Map<String, Product> dbProductIdMap, String orderId, Date orderCreateTime) {
        Product dbProduct = dbProductIdMap.get(orderProductDTO.getProductId());
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId(IdUtil.fastSimpleUUID());
        orderProduct.setProductId(orderProductDTO.getProductId());
        orderProduct.setProductNameSnapshot(dbProduct.getTitle());
        orderProduct.setOrderId(orderId);
        orderProduct.setProductQuantity(orderProductDTO.getNum());
        orderProduct.setProductPriceSnapshot(dbProduct.getPrice());
        orderProduct.setCreatedUser(SecurityUtils.getUsername());
        orderProduct.setCreatedTime(orderCreateTime);
        return orderProduct;
    }

    private Order assembleOrder(MerchantOrderCreateReq merchantOrderCreateReq, Customer customer, BigDecimal totalPrice, Date orderCreateTime, String orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setCustomerId(customer.getId());
        order.setStatus(OrderStatusEnum.ORDER_CREATED.getCode());
        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(getTotalNum(merchantOrderCreateReq));
        order.setRemark(merchantOrderCreateReq.getRemark());
        order.setCreatedUser(SecurityUtils.getUsername());
        order.setCreatedTime(orderCreateTime);
        order.setReceiverAddress(merchantOrderCreateReq.getReceiverAddress());
        order.setReceiverName(merchantOrderCreateReq.getReceiverName());
        order.setReceiverPhone(merchantOrderCreateReq.getReceiverPhone());
        return order;
    }

    private static int getTotalNum(MerchantOrderCreateReq merchantOrderCreateReq) {
        return merchantOrderCreateReq.getOrderProductDTOList().stream().mapToInt(OrderProductDTO::getNum).sum();
    }
}
