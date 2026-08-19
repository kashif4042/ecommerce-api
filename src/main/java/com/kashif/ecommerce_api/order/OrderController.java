package com.kashif.ecommerce_api.order;


import com.kashif.ecommerce_api.order.dto.OrderResponse;
import com.kashif.ecommerce_api.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(orderService.checkout(userDetails.getUser()));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails ){
        return ResponseEntity.ok(orderService.getOrdersByUser(userDetails.getUser()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrdersById(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(orderService.getOrderById(orderId, userDetails.getUser()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId,@RequestParam OrderStatus status){
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId,status));
    }


}
