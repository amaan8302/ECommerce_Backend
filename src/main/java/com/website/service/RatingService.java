package com.website.service;

import java.util.List;


//import org.springframework.stereotype.Service;

import com.website.exception.ProductException;
import com.website.model.Rating;
import com.website.model.User;
import com.website.request.RatingRequest;


//@Service
public interface RatingService {

	public Rating createRating(RatingRequest req, User user) throws ProductException;
	public List<Rating> getProductsRatings(Long productId);
}
