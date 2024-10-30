package com.website.service;

import java.util.Optional;

import org.springframework.stereotype.Service;


import com.website.exception.CartItemException;
import com.website.exception.UserException;
import com.website.model.Cart;
import com.website.model.CartItem;
import com.website.model.Product;
import com.website.model.User;
import com.website.repository.CartItemRepository;
import com.website.repository.CartRepository;

@Service
public class CartItemServiceImplementation implements CartItemService {
	private CartItemRepository cartItemRepository;
	private UserService userService;
	private CartRepository cartRepository;
	public CartItemServiceImplementation(CartItemRepository cartItemRepository,
			UserService userService,
			CartRepository cartRepository) 
	{
		this.cartItemRepository=cartItemRepository;
		this.cartRepository=cartRepository;
		this.userService=userService;
	}
	@Override
	public CartItem createCartItem(CartItem cartItem) {
		// TODO Auto-generated method stub
		cartItem.setQuantity(1);
		cartItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
		cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()*cartItem.getQuantity());
		CartItem createdCartItem = cartItemRepository.save(cartItem);
		return createdCartItem;
	}

	@Override
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
		CartItem item = findCartItemById(id);
		User user = userService.findUserById(item.getUserId());
		if(user.getId().equals(userId))
		{
			item.setQuantity(cartItem.getQuantity());
			item.setPrice(item.getQuantity()*item.getProduct().getPrice());
			item.setDiscountedPrice(item.getProduct().getDiscountedPrice()*item.getQuantity());
		}
		return cartItemRepository.save(item);
	}
	@Override
	public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
		// TODO Auto-generated method stub
		CartItem cartItem = cartItemRepository.isCartItemExist(cart, product, size, userId);
		return cartItem;
	}

	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
		// TODO Auto-generated method stub
		CartItem cartItem = findCartItemById(cartItemId);
		User user = userService.findUserById(cartItem.getUserId());
		User reqUser = userService.findUserById(userId);
		if(user.getId().equals(reqUser.getId()))
		{
			cartItemRepository.deleteById(cartItemId);
		}
		else
		{
			throw new UserException("You can't remove another user item");
		}
	}

	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		Optional<CartItem>opt = cartItemRepository.findById(cartItemId);
		if(opt.isPresent())
		{
			return opt.get();
		}
		throw new CartItemException("Cart item not found with id : "+cartItemId);
	}

}
