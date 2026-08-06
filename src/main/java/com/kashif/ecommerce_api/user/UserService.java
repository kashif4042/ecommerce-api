package com.kashif.ecommerce_api.user;

public interface UserService {
    User registerUser(User user);
    User login(String email, String password);
}
