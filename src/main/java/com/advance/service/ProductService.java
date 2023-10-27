package com.advance.service;

import java.util.List;
import java.util.Optional;

import com.advance.entity.Product;
import com.advance.entity.ProductCard;

public interface ProductService {

	Product addProduct(Product product); 
	Product getProductById(Long productId); 
	Product getProductByName(String name);
	List<ProductCard> getProductsByName(String productName); 
	List<ProductCard> getProductByCategory(String categoryName); 
	List<ProductCard> getProductBySeasonalStatus(Boolean isSeasonal); 
	List<ProductCard> getProductByIsFeatured(Boolean isFeatured); 
	Product updateProduct(Product product); 
	void updateProductAfterPurchase(Long productId, Optional<Long> purchaseAmount);
	void updateProductForIsFeatured(Long productId, Boolean isFeatured); 
	void updateProductForAvailability(Long productId, Boolean isAvailable); 
	Boolean deleteProduct(Long productId); 
}
