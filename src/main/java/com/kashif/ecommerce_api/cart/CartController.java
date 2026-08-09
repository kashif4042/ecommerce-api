package com.kashif.ecommerce_api.cart;

import com.kashif.ecommerce_api.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(cartService.getCartByUser(userDetails.getUser()));
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @RequestParam Long productId,
                                              @RequestParam Integer quantity){
        return ResponseEntity.ok(cartService.addItemToCart(userDetails.getUser(),productId,quantity));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<Cart> updateItemQuantity(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable Long productId,
                                                   @RequestParam Integer quantity){
        return ResponseEntity.ok(cartService.updateItemQuantity(userDetails.getUser(),productId,quantity));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Cart> removeItemFromCart(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable Long productId){
        return ResponseEntity.ok(cartService.removeItemFromCart(userDetails.getUser(),productId));
    }
}
