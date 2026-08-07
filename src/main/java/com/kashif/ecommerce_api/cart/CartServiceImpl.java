package com.kashif.ecommerce_api.cart;

import com.kashif.ecommerce_api.product.Product;
import com.kashif.ecommerce_api.product.ProductRepository;
import com.kashif.ecommerce_api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public Cart getCartByUser(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    public Cart addItemToCart(User user, Long productId, Integer quantity) {
        Cart cart = getCartByUser(user);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return cartRepository.save(cart);
            }
        }

        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        cart.getItems().add(newItem);

        return cartRepository.save(cart);
    }

    @Override
    public Cart updateItemQuantity(User user, Long productId, Integer quantity) {
        Cart cart = getCartByUser(user);

        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                return cartRepository.save(cart);
            }
        }

        throw new RuntimeException("Item not found in cart");
    }

    @Override
    public Cart removeItemFromCart(User user, Long productId) {
        Cart cart = getCartByUser(user);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }
}