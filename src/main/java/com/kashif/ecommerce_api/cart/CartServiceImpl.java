package com.kashif.ecommerce_api.cart;

import com.kashif.ecommerce_api.cart.dto.CartItemResponse;
import com.kashif.ecommerce_api.cart.dto.CartResponse;
import com.kashif.ecommerce_api.exception.ResourceNotFoundException;
import com.kashif.ecommerce_api.product.Product;
import com.kashif.ecommerce_api.product.ProductRepository;
import com.kashif.ecommerce_api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public CartResponse getCartByUser(User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse addItemToCart(User user, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return mapToCartResponse(cartRepository.save(cart));
            }
        }

        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        cart.getItems().add(newItem);

        return mapToCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse updateItemQuantity(User user, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                return mapToCartResponse(cartRepository.save(cart));
            }
        }

        throw new ResourceNotFoundException("Item not found in cart");
    }

    @Override
    public CartResponse removeItemFromCart(User user, Long productId) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

        return mapToCartResponse(cartRepository.save(cart));
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getPrice() * item.getQuantity()
                ))
                .collect(Collectors.toList());

        Double totalAmount = itemResponses.stream()
                .mapToDouble(CartItemResponse::getSubtotal)
                .sum();

        return new CartResponse(cart.getId(), itemResponses, totalAmount);
    }
}