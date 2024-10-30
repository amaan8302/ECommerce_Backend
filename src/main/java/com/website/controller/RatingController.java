package com.website.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.website.exception.ProductException;
import com.website.exception.UserException;
import com.website.model.Rating;
import com.website.model.User;
import com.website.request.RatingRequest;
import com.website.service.RatingService;
import com.website.service.UserService;
@RestController
@RequestMapping("/api/ratings")
public class RatingController {
	private UserService userService;
	private RatingService ratingService;
	public RatingController(UserService userService,RatingService ratingService) {
		this.ratingService=ratingService;
		this.userService=userService;
		// TODO Auto-generated constructor stub
	}
	@PostMapping("/create")
	public ResponseEntity<Rating> createRating(@RequestBody RatingRequest req,
			@RequestHeader("Authorization") String jwt) throws UserException, ProductException{
		User user=userService.findUserProfileByJwt(jwt);
		Rating rating=ratingService.createRating(req, user);
		return new ResponseEntity<Rating>(rating,HttpStatus.CREATED);
	}
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Rating>> getProductsRating(@PathVariable Long productId,
			@RequestHeader("Authorization") String jwt)throws UserException, ProductException{
		User user = userService.findUserProfileByJwt(jwt);
		List<Rating> ratings=ratingService.getProductsRatings(productId);
		return new ResponseEntity<>(ratings,HttpStatus.CREATED);
	}
}
