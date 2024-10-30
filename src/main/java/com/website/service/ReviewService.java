package com.website.service;

import java.util.List;

import com.website.exception.ProductException;
import com.website.model.Review;
import com.website.model.User;
import com.website.request.ReviewRequest;

public interface ReviewService {
	public Review createReview(ReviewRequest req,User user) throws ProductException;
	public List<Review> getAllReview(Long productId);
	
}
