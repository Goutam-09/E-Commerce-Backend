package com.dollop.app.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.app.entities.Cart;
import com.dollop.app.entities.CartItem;
import com.dollop.app.entities.Product;

public interface CartItemRepository extends JpaRepository<CartItem, String> {

	public Optional<CartItem> findByProduct(Product product);

	public Page<CartItem> findByCart(Pageable pageable,Cart cart);

	
}
