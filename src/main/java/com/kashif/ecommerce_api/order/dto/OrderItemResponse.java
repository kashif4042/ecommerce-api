package com.kashif.ecommerce_api.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double priceAtPurchase;  // snapshot price, not current product price
    private Double subtotal;          // priceAtPurchase * quantity
}

