package com.kashif.ecommerce_api.order;

import com.kashif.ecommerce_api.order.dto.OrderResponse;
import com.kashif.ecommerce_api.user.User;

import java.util.List;

public interface OrderService {
    OrderResponse checkout(User user);
    List<OrderResponse> getOrdersByUser(User user);
    OrderResponse getOrderById(Long orderId, User user);
}
