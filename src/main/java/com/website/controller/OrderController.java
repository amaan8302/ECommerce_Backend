package com.website.controller;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.website.exception.OrderException;
import com.website.exception.UserException;
import com.website.model.Address;
import com.website.model.Order;
import com.website.model.User;
import com.website.service.OrderService;
import com.website.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private OrderService orderService;
	private UserService userService;
	public OrderController(OrderService orderService,UserService userService) {
		this.orderService=orderService;
		this.userService=userService;
	}
	@PostMapping("/")
	public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress,
			@RequestHeader("Authorization")String jwt) throws UserException
	{
		User user=userService.findUserProfileByJwt(jwt);
		Order order =orderService.createOrder(user, shippingAddress);
		System.out.println("order: " + order);
		return new ResponseEntity<Order>(order,HttpStatus.CREATED);	
	}
	@GetMapping("/user")
	public ResponseEntity< List<Order>> usersOrderHistory(@RequestHeader("Authorization") 
	String jwt) throws OrderException, UserException
	{	
		User user=userService.findUserProfileByJwt(jwt);
		List<Order> orders=orderService.usersOrderHistory(user.getId());
		return new ResponseEntity<>(orders,HttpStatus.CREATED);
	}
	@GetMapping("/{Id}")
	public ResponseEntity< Order> findOrderById(@PathVariable("Id") Long orderId, @RequestHeader("Authorization") 
	String jwt) throws OrderException, UserException
	{	
		User user = userService.findUserProfileByJwt(jwt);
		Order orders=orderService.findOrderById(orderId);
		return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
	}
}