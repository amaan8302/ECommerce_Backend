package com.website.service;

import org.springframework.stereotype.Service;


import com.website.exception.ProductException;
import com.website.model.Cart;
import com.website.model.CartItem;
import com.website.model.Product;
import com.website.model.User;
import com.website.repository.CartRepository;
import com.website.request.AddItemRequest;

@Service
public class CartServiceImplementation implements CartService{

	private CartRepository cartRepository;
	private CartItemService cartItemService;
	private ProductService productService;
	
	public CartServiceImplementation(CartRepository cartRepository
			,CartItemService cartItemService,
			ProductService productService) {
		// TODO Auto-generated constructor stub
		this.cartItemService = cartItemService;
		this.cartRepository = cartRepository;
		this.productService = productService;
	}
	
	
	@Override
	public Cart createCart(User user) {
		Cart cart = new Cart();
		cart.setUser(user);
		return cartRepository.save(cart);
	}

	@Override
	public String addCartItem(Long userId, AddItemRequest req) throws ProductException {
		Cart cart = cartRepository.findByUserId(userId);
		Product product = productService.findProductById(req.getProductId());
		CartItem isPresent = cartItemService.isCartItemExist(cart, product, req.getSize(), userId);
		if(isPresent==null)
		{
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(req.getQuantity());
			cartItem.setUserId(userId);
			int price = req.getQuantity()*product.getDiscountedPrice();
			cartItem.setPrice(price);
			cartItem.setSize(req.getSize());
			
			CartItem createdCartItem = cartItemService.createCartItem(cartItem);
			cart.getCartItem().add(createdCartItem);
			
		}
		return "Added to cart";
	}

	@Override
	public Cart findUserCart(Long userId) {
		Cart cart = cartRepository.findByUserId(userId);
		int totalPrice = 0;
		int totalDiscountedPrice = 0;
		int totalItem = 0;
		
		
		for(CartItem cartItem : cart.getCartItem()) {
			totalPrice = totalPrice+ cartItem.getPrice();
			totalDiscountedPrice = totalDiscountedPrice+cartItem.getDiscountedPrice();
			totalItem = totalItem+cartItem.getQuantity();	
		}
		cart.setTotalDiscountedPrice(totalDiscountedPrice);
		cart.setTotalItem(totalItem);
		cart.setTotalPrice(totalPrice);
		cart.setDiscount(totalPrice-totalDiscountedPrice);
		
		return cartRepository.save(cart);
	}
	
}
