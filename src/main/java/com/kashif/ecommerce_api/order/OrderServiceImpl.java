package com.kashif.ecommerce_api.order;

import com.kashif.ecommerce_api.cart.Cart;
import com.kashif.ecommerce_api.cart.CartItem;
import com.kashif.ecommerce_api.cart.CartRepository;
import com.kashif.ecommerce_api.exception.ResourceNotFoundException;
import com.kashif.ecommerce_api.order.dto.OrderItemResponse;
import com.kashif.ecommerce_api.order.dto.OrderResponse;
import com.kashif.ecommerce_api.product.Product;
import com.kashif.ecommerce_api.product.ProductRepository;
import com.kashif.ecommerce_api.user.User;
import com.kashif.ecommerce_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderResponse checkout(User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty, cannot checkout");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);

        Double totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getPriceAtPurchase() * item.getQuantity())
                .sum();
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return mapToOrderResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrdersByUser(User user) {
        List<Order> orders = orderRepository.findByUserId(user.getId());
        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("This order does not belong to you");
        }

        return mapToOrderResponse(order);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPriceAtPurchase(),
                        item.getPriceAtPurchase() * item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                itemResponses,
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }
    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponse(updatedOrder);


    }
}