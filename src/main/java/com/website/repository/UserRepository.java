package com.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.website.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	public User findByEmail(String email);
	
}
