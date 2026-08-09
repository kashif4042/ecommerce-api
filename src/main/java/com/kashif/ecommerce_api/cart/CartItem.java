package com.kashif.ecommerce_api.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kashif.ecommerce_api.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "cart_id")
    @ManyToOne
    @JsonIgnore
    private Cart cart;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    private Integer quantity;
}
