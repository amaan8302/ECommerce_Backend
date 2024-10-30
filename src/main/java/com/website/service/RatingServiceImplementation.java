package com.website.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

//import com.amaansaloni.model.Product;
import com.website.exception.ProductException;
import com.website.model.Product;
import com.website.model.Rating;
import com.website.model.User;
import com.website.repository.RatingRepository;
import com.website.request.RatingRequest;
@Service
public class RatingServiceImplementation implements RatingService{

	
	private RatingRepository ratingRepository;
	private ProductService productService;
	public RatingServiceImplementation(RatingRepository ratingRepository
			,ProductService productService) 
	{
		this.ratingRepository = ratingRepository;
		this.productService = productService;
	}
	@Override
	public Rating createRating(RatingRequest req, User user) throws ProductException {
		Product product = productService.findProductById(req.getProductId());
		Rating rating = new Rating();
		rating.setProduct(product);
		rating.setUser(user);
		rating.setRating(req.getRating());
		rating.setCreatedAt(LocalDateTime.now());
		return ratingRepository.save(rating);
	}

	@Override
	public List<Rating> getProductsRatings(Long productId) {
		return ratingRepository.getAllProductsRating(productId);
	}

}
