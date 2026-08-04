package com.kashif.ecommerce_api.product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    List<Product> getProductsByCategory(Long categoryId);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}
