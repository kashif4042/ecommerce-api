package com.kashif.ecommerce_api.cart;

import com.kashif.ecommerce_api.cart.dto.CartResponse;
import com.kashif.ecommerce_api.user.User;

public interface CartService {
    CartResponse getCartByUser(User user);
    CartResponse addItemToCart(User user, Long productId, Integer quantity);
    CartResponse updateItemQuantity(User user, Long productId, Integer quantity);
    CartResponse removeItemFromCart(User user, Long productId);
}
