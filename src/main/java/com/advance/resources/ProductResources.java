package com.advance.resources;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.advance.entity.MyResponse;
import com.advance.entity.Product;
import com.advance.entity.ProductCard;
import com.advance.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
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

	private URI getURI() {
		return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/get/<productId>").toUriString());
	}
}
