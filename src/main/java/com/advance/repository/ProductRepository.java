package com.advance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.advance.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, ListCrudRepository<Product, Long>{

    Product findByName(String name);

    @Query(value = "SELECT * FROM products WHERE name LIKE :name", nativeQuery= true)
    List<Product> findProductsByName(@Param("name") String name);

    List<Product> findProductsByCategory_Name(String category);
    
    @Query(value = "SELECT * FROM products WHERE is_featured = :isFeatured", nativeQuery = true)
    List<Product> findProductsByIsFeatured(@Param("isFeatured") Boolean isFeatured);
    List<Product> findProductsByIsSeasonal(Boolean isSeasonal);

    @Modifying
    @Query(value = "UPDATE products p SET p.bought_num = p.bought_num + :increment WHERE p.id = :productId", nativeQuery = true)
    void incrementPurchasedTimes(@Param("productId") Long productId, @Param("increment") Long increment);

    @Modifying
    @Query(value = "UPDATE products p SET p.is_featured = :isFeatured WHERE p.id = :productId", nativeQuery= true)
    void updateProductIsFeatured(@Param("productId") Long productId, @Param("isFeatured") Boolean isFeatured);

    @Modifying
    @Query(value = "UPDATE products p SET p.availability = :availability WHERE p.id = :productId", nativeQuery= true)
    void updateProductAvailability(@Param("productId") Long productId, @Param("availability") Boolean availability);
    
    @Modifying
    @Query(value = "UPDATE products p SET p.in_stock = :inStock WHERE p.id = :userId", nativeQuery = true)
    void updateProductInStock(@Param("userId") Long userId, @Param("inStock") Integer inStock); 
}
