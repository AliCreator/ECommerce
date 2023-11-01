package com.advance.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.advance.entity.UserEvent;

public interface UserEventRepository extends PagingAndSortingRepository<UserEvent, Long>, ListCrudRepository<UserEvent, Long>{

	@Query(value = "SELECT * FROM user_event WHERE user_id = :userId", nativeQuery = true)
	Collection<UserEvent> findByUser(@Param("userId")Long userId);
	
}
