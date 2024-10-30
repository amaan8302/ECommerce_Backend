package com.website.service;
import java.util.List;
import org.springframework.data.domain.Page;
import com.website.exception.ProductException;
import com.website.model.Product;
import com.website.request.CreateProductRequest;
public interface ProductService {
	public Product createProduct(CreateProductRequest req);
	public String deleteProduct(Long productId) throws ProductException;
	public Product updateProduct(Long productId, Product req) throws ProductException ;
	public Product findProductById(Long id) throws ProductException;
	public List<Product> findProductByCategory(String category);
	public List<Product> getAllProducts();
	public Page<Product>getAllProduct(String Category
			,List<String>colors
			,List<String>sizes
			,Integer minPrice
			,Integer maxPrice
			,Integer minDiscount
			,String sort
			,String stock
			,Integer pageNumber
			,Integer pageSize);
	public List<Product> recentlyAddedProduct();
}








