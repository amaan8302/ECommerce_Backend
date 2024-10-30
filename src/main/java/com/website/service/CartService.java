package com.website.service;

import com.website.exception.ProductException;
import com.website.model.Cart;
import com.website.model.User;
import com.website.request.AddItemRequest;

public interface CartService {
	public Cart createCart(User user);
	public String addCartItem(Long userId, AddItemRequest req) throws ProductException;
	public Cart findUserCart(Long id);
}
