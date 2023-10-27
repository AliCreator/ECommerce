package com.advance.repository;

import java.util.Optional;

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

	@Query(value = "UPDATE User SET is_using_mfa = :status WHERE id = :userId ", nativeQuery = true)
	User toggleUsingMfa(@Param("userId") Long userId, @Param("status") Boolean status);
	
	@Query(value="SELECT u FROM User u JOIN Verification v ON u.id = v.user_id WHERE v.url = :url", nativeQuery = true)
	User getUserByPasswordKey(@Param("url") String url);
	
	@Query(value="UPDATE User SET password = :password WHERE id = (SELECT user_id FROM Verifications WHERE url = :url)", nativeQuery = true)
	User updateUserPasswordWithKey(@Param("password") String password, @Param("url") String url);
	
	@Query(value="UPDATE User SET firstName = :firstName, lastName = :lastName, phone = :phone WHERE id = :userId", nativeQuery = true)
	User udpateUserFields(@Param("userId") Long userId, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("phone") String phone);

	@Query(value="UPDATE User SET password = :password WHERE id = :userId", nativeQuery = true)
	void updateUserPassword(@Param("userId") Long userId, String password);
	
	@Query(value = "UPDATE User SET role = :role WHERE id = :userId", nativeQuery= true)
	User updateUserRole(@Param("userId") Long userId, @Param("role") RoleType role);
}
