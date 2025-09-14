package com.MyProject.AgritradeHub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MyProject.AgritradeHub.Model.Users1;
import com.MyProject.AgritradeHub.Model.Users1.UserRole;

public interface UserRepository extends JpaRepository<Users1, Long> {

	boolean getByEmail(String email);

	Users1 findByEmail(String email);

	boolean existsByEmail(String email);

	List<Users1> findAllByRole(UserRole merchant);

	List<Users1> findAllByRoleAndStatus(UserRole merchant, String string);

	

}
