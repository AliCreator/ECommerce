package com.advance.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.advance.entity.Verification;

@Repository
public interface VerificationRepository extends CrudRepository<Verification, Long>{

	@Query(value= "SELECT * FROM Verifications WHERE url = :url", nativeQuery = true)
	Verification getVerificationByUrl(@Param("url") String url);
}