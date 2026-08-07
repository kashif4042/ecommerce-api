package com.kashif.ecommerce_api.cart;

import com.kashif.ecommerce_api.user.User;

public interface CartService {
    Cart getCartByUser(User user);
    Cart addItemToCart(User user, Long productId, Integer quantity);
    Cart updateItemQuantity(User user, Long productId, Integer quantity);
    Cart removeItemFromCart(User user, Long productId);
}
