package com.advance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.advance.entity.User;
import com.advance.enumeration.RoleType;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, ListCrudRepository<User, Long> {

	Optional<User> findByEmail(String email);

	@Query(value = "UPDATE Users SET is_using_mfa = :status WHERE id = :userId ", nativeQuery = true)
	User toggleUsingMfa(@Param("userId") Long userId, @Param("status") Boolean status);
	
	@Query(value="SELECT * FROM Users WHERE id = (SELECT v.user_id FROM Verification v WHERE v.url = :url)", nativeQuery = true)
	User getUserByPasswordKey(@Param("url") String url);
	
	@Modifying
	@Query(value="UPDATE Users SET password = :password WHERE id = (SELECT v.user_id FROM Verification v WHERE v.url = :url)", nativeQuery = true)
	void updateUserPasswordWithKey(@Param("password") String password, @Param("url") String url);
	
	@Modifying
	@Query(value="UPDATE Users SET firstName = :firstName, lastName = :lastName, phone = :phone WHERE id = :userId", nativeQuery = true)
	void udpateUserFields(@Param("userId") Long userId, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("phone") String phone);

	@Modifying
	@Query(value="UPDATE Users SET password = :password WHERE id = :userId", nativeQuery = true)
	void updateUserPassword(@Param("userId") Long userId, String password);
	
	@Modifying
	@Query(value = "UPDATE Users SET role = :role WHERE id = :userId", nativeQuery= true)
	void updateUserRole(@Param("userId") Long userId, @Param("role") RoleType role);
}
