package com.website.controller;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.website.exception.ProductException;
import com.website.exception.UserException;
import com.website.model.Cart;
import com.website.model.User;
import com.website.request.AddItemRequest;
import com.website.response.ApiResponse;
import com.website.service.CartService;
import com.website.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart Management", description = "find user cart , add item to cart")
public class CartController {
	private CartService cartService;
	private UserService userService;
	public CartController(CartService cartService,UserService userService) {
		this.cartService=cartService;
		this.userService=userService;
	}
	@GetMapping("/")
	@Operation(description = "find cart by user id")
	public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException{
		User user=userService.findUserProfileByJwt(jwt);
		Cart cart=cartService.findUserCart(user.getId());
//		System.out.println("cart - "+cart.getUser().getEmail());	
		return new ResponseEntity<Cart>(cart,HttpStatus.OK);
	}
	@PutMapping("/add")
	@Operation(description = "add item to cart")
	public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest req, 
			@RequestHeader("Authorization") String jwt) throws UserException, ProductException{
		User user=userService.findUserProfileByJwt(jwt);
		cartService.addCartItem(user.getId(), req);
		ApiResponse res= new ApiResponse();
		res.setMessage("Item Added To Cart Successfully");
		res.setStatus(true);
		return new ResponseEntity<>(res,HttpStatus.OK);	
	}
	
}
