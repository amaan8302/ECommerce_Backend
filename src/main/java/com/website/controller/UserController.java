package com.website.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.website.exception.ProductException;
import com.website.exception.UserException;
import com.website.model.User;
import com.website.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private UserService userService;
	public UserController(UserService userService) {
		this.userService = userService;
	}
	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization")String jwt)
			 throws UserException{
		User user = userService.findUserProfileByJwt(jwt);
		return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
	}
}