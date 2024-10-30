package com.website.controller;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
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
import com.website.model.Review;
import com.website.model.User;
import com.website.request.ReviewRequest;
import com.website.service.ReviewService;
import com.website.service.UserService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
	private ReviewService reviewService;
	private UserService userService;	
	public ReviewController(ReviewService reviewService,UserService userService) {
		this.reviewService=reviewService;
		this.userService=userService;
	}
	@PostMapping("/create")
	public ResponseEntity<Review> createReviewHandler(@RequestBody ReviewRequest req,
			@RequestHeader("Authorization") String jwt) throws UserException, ProductException{
		User user=userService.findUserProfileByJwt(jwt);
//		System.out.println("product id "+req.getProductId()+" - "+req.getReview());
		Review review=reviewService.createReview(req, user);
//		System.out.println("product review "+req.getReview());
		return new ResponseEntity<>(review,HttpStatus.CREATED);
	}
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Review>> getProductsReviewHandler(@PathVariable Long productId)
			throws UserException, ProductException{
		List<Review>reviews=reviewService.getAllReview(productId);
		return new ResponseEntity<>(reviews,HttpStatus.ACCEPTED);
	}
}