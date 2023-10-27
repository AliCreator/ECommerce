package com.advance.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.advance.entity.Review;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Long>, ListCrudRepository<Review, Long>{

}
