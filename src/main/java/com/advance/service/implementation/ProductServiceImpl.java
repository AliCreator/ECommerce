package com.advance.service.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.advance.entity.Product;
import com.advance.entity.ProductCard;
import com.advance.entity.User;
import com.advance.exception.ApiException;
import com.advance.repository.ProductRepository;
import com.advance.repository.UserRepository;
import com.advance.service.ProductService;
import com.advance.utils.ProductUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepo;
	private final UserRepository userRepo;

	@Override
	public Product addProduct(Product product) {
		if (null != getProductByName(product.getName().trim().toLowerCase())) {
			throw new ApiException("Product with this name already exist. Please change the name!");
		}
		try {
			product.setCreatedAt(LocalDate.now());
			product.setRating(5.0);
			product.setNumReviews(0);
			product.setBoughtNum(0L);
			return productRepo.save(product);

		} catch (Exception e) {
			throw new ApiException("Somethig went wrong during saving the product!");
		}
	}

	@Override
	public Product getProductById(Long productId) {
		try {
			Optional<Product> product = productRepo.findById(productId);
			if (product.isPresent()) {
				return product.get();
			}
			throw new ApiException("Product was not found!");
		} catch (Exception e) {
			throw new ApiException("Somethig went wrong during retrieving the product!");
		}
	}

	@Override
	public List<ProductCard> getProductByCategory(String categoryName) {
		try {
			List<Product> list = productRepo.findProductsByCategory_Name(categoryName);
			ArrayList<ProductCard> allProducts = new ArrayList<>();
			for (Product p : list) {
				allProducts.add(ProductUtils.convertToProductCard(p));
			}
			return allProducts;
		} catch (Exception e) {
			throw new ApiException("Somethig went wrong during retrieving products by category!");
		}
	}

	@Override
	public List<ProductCard> getProductBySeasonalStatus(Boolean isSeasonal) {
		try {
			List<Product> list = productRepo.findProductsByIsSeasonal(isSeasonal);
			ArrayList<ProductCard> allProducts = new ArrayList<>();
			for (Product p : list) {
				allProducts.add(ProductUtils.convertToProductCard(p));
			}
			return allProducts;
		} catch (Exception e) {
			throw new ApiException("Somethig went wrong during retrieving products by seasonal status!");
		}
	}

	@Override
	public List<ProductCard> getProductByIsFeatured(Boolean isFeatured) {
		try {
			Iterable<Product> list = productRepo.findProductsByIsFeatured(isFeatured);
			ArrayList<ProductCard> allProducts = new ArrayList<>();

			for (Product p : list) {
				allProducts.add(ProductUtils.convertToProductCard(p));
			}
			return allProducts;
		} catch (Exception e) {
			throw new ApiException("Somethig went wrong during retrieving products by featured status!");
		}
	}

	@Override
	public Product updateProduct(Product product) {
		try {
			product.setUpdateAt(LocalDate.now());
			return productRepo.save(product);
		} catch (Exception e) {
			throw new ApiException("Somethig went wrong during updating product!");
		}
	}

	@Override
	public void updateProductAfterPurchase(Long productId, Optional<Long> purchaseAmount) {
		try {
			productRepo.incrementPurchasedTimes(productId, purchaseAmount.orElse(1L));
		} catch (Exception e) {
			throw new ApiException("Somethig went wrong during updating product bought times!");
		}

	}

	@Override
	public void updateProductForIsFeatured(Long productId, Boolean isFeatured) {
		try {
			productRepo.updateProductIsFeatured(productId, isFeatured);
		} catch (Exception e) {
			throw new ApiException("Somethig went wrong during updating product featured status!");
		}

	}

	@Override
	public void updateProductForAvailability(Long productId, Boolean isAvailable) {
		try {
			if (isAvailable) {
				productRepo.updateProductAvailability(productId, isAvailable);
				productRepo.updateProductInStock(productId, 10);
			} else {
				productRepo.updateProductAvailability(productId, isAvailable);
				productRepo.updateProductInStock(productId, 0);
			}
		} catch (Exception e) {
			throw new ApiException("Somethig went wrong during updating product availability status!");
		}

	}

	@Override
	public Boolean deleteProduct(Long productId) {
		try {
			productRepo.deleteById(productId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Product getProductByName(String name) {
		try {
			return productRepo.findByName(name);
		} catch (Exception e) {
			throw new ApiException("Somethig went wrong during retrieving product by name!");
		}
	}

	@Override
	public List<ProductCard> getProductsByName(String productName) {
		try {
			List<Product> list = productRepo.findProductsByName(productName);
			ArrayList<ProductCard> allProducts = new ArrayList<>();
			for (Product p : list) {
				allProducts.add(ProductUtils.convertToProductCard(p));
			}
			return allProducts;
		} catch (Exception e) {
			throw new ApiException("Somethig went wrong during retrieving products by name!");
		}
	}

	@Override
	public void wishlistAdd(Long userId, Long productId) {
		try {
			Optional<User> optionalUser = userRepo.findById(productId);
			Optional<Product> optionalProduct = productRepo.findById(productId);
			if (optionalUser.isPresent() && optionalProduct.isPresent()) {
				User user = optionalUser.get();
				Product product = optionalProduct.get();
				List<Product> wishList = user.getWishList();
				wishList.add(product);
				userRepo.save(user);
			}

		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException("User or product not found!");
		}

		catch (Exception e) {
			throw new ApiException("An error occured!");
		}

	}

}
