package com.dollop.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.app.entities.User;

public interface UserRepository extends JpaRepository<User, String> {
	public List<User> findByNameContaining(String keyword);
		
}
