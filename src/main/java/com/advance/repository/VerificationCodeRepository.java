package com.advance.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.advance.entity.VerificationCode;

@Repository
public interface VerificationCodeRepository extends CrudRepository<VerificationCode, Long>{

	@Query(value= "DELETE FROM VerificationCodes WHERE user_id= :userId", nativeQuery = true)
	void deleteVerificationCodeByUserId(@Param("userId") Long userId); 
}