package com.dollop.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.app.entities.Cart;
import com.dollop.app.entities.User;

public interface CartRepository extends JpaRepository<Cart, String> {

	public Optional<Cart> findByUser(User user);

}
