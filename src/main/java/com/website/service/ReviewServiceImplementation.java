package com.website.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;


import com.website.exception.ProductException;
import com.website.model.Product;
import com.website.model.Review;
import com.website.model.User;
import com.website.repository.ProductRepository;
import com.website.repository.ReviewRepository;
import com.website.request.ReviewRequest;

@Service
public class ReviewServiceImplementation implements ReviewService{

	private ReviewRepository reviewRepository;
	private ProductService productService;
//	private ProductRepository productRepository;
	public ReviewServiceImplementation(ReviewRepository reviewRepository,
			ProductService productService,
			ProductRepository productRepository) {
		// TODO Auto-generated constructor stub
		this.reviewRepository = reviewRepository;
//		this.productRepository = productRepository;
		this.productService=productService;
	}
	@Override
	public Review createReview(ReviewRequest req, User user) throws ProductException {
		Product product = productService.findProductById(req.getProductId());
		Review review = new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReview(req.getReview());
		review.setCreatedAt(LocalDateTime.now());
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getAllReview(Long productId) {
		return reviewRepository.getAllProductsReview(productId);
	}

}
