package com.advance.resources;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.advance.entity.MyResponse;
import com.advance.entity.Product;
import com.advance.entity.ProductCard;
import com.advance.entity.User;
import com.advance.service.ProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Transactional
public class ProductResources {

	private final ProductService productService;

	@PostMapping("/add")
	public ResponseEntity<MyResponse> addProduct(@RequestBody Product product) {
		Product newProduct = productService.addProduct(product);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(HttpStatus.CREATED.value()).httpStatus(HttpStatus.CREATED).message("Product added")
				.data(Map.of("product", newProduct)).build();

		return ResponseEntity.created(getURI()).body(myResponse);
	}

	@GetMapping("/find/id/{id}")
	public ResponseEntity<MyResponse> getProductById(@PathVariable("id") Long id) {
		Product product = productService.getProductById(id);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).data(Map.of("product", product))
				.message("Product retrieved").build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/name/{name}")
	public ResponseEntity<MyResponse> getProductByName(@PathVariable("name") String name) {
		Product product = productService.getProductByName(name);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).data(Map.of("product", product))
				.message("Product retrieved").build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/all/featured")
	public ResponseEntity<MyResponse> getAllProductsByFeatured(@RequestParam("status") Boolean status) {
		List<ProductCard> allProducts = productService.getProductByIsFeatured(status);

		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).data(Map.of("products", allProducts))
				.message("Products retrieved").build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/all/seasonal")
	public ResponseEntity<MyResponse> getAllProductsBySeasonal(@RequestParam("status") Boolean Status) {
		List<ProductCard> allProducts = productService.getProductBySeasonalStatus(Status);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).data(Map.of("products", allProducts))
				.message("Products retrieved").build();
		return ResponseEntity.ok().body(myResponse);
	}

	@GetMapping("/find/all/category")
	public ResponseEntity<MyResponse> getAllProductsByCategory(@RequestParam("category") String category) {
		List<ProductCard> allProducts = productService.getProductByCategory(category);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).data(Map.of("products", allProducts))
				.message("Products retrieved").build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update")
	public ResponseEntity<MyResponse> updateProduct(@RequestBody Product product) {
		Product updateProduct = productService.updateProduct(product);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).data(Map.of("product", updateProduct))
				.message("Product updated").build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/purchas/{id}")
	public ResponseEntity<MyResponse> updateProductAfterPurchase(@PathVariable("id") Long id,
			@RequestParam("purchaseAmount") Optional<Long> purchaseAmount) {
		productService.updateProductAfterPurchase(id, purchaseAmount);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).message("Product updated").build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/featured/{id}")
	public ResponseEntity<MyResponse> updateProductIsFeatured(@PathVariable("id") Long id,
			@RequestParam("featured") Optional<Boolean> featured) {
		productService.updateProductForIsFeatured(id, featured.orElse(true));
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).message("Product updated").build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/update/availablity/{id}")
	public ResponseEntity<MyResponse> updateProductAvailability(@PathVariable("id") Long id,
			@RequestParam("avilability") Optional<Boolean> isAvailable) {
		productService.updateProductForAvailability(id, isAvailable.orElse(true));
		productService.updateProductForIsFeatured(id, isAvailable.orElse(true));
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).message("Product updated").build();
		return ResponseEntity.ok().body(myResponse);
	}

	@PutMapping("/wishlist/{productId}")
	public ResponseEntity<MyResponse> wishlistAdd(@AuthenticationPrincipal User user,
			@PathVariable("productId") Long productId) {
		productService.wishlistAdd(productId, productId);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(HttpStatus.OK.value()).httpStatus(HttpStatus.OK).message("Product added to wishlist!").build();
		return ResponseEntity.ok().body(myResponse);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<MyResponse> deleteProduct(@PathVariable("id") Long id) {
		Boolean status = productService.deleteProduct(id);
		MyResponse myResponse = MyResponse.builder().timestamp(LocalDateTime.now().toString())
				.status(status ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
				.httpStatus(status ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
				.message(status ? "Product deleted" : "An error occured").build();
		return ResponseEntity.ok().body(myResponse);
	}

	private URI getURI() {
		return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/get/<productId>").toUriString());
	}
}
