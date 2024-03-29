package com.dollop.app.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entities.Category;
import com.dollop.app.entities.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

	public Boolean existsBytitle(String title);

	public Boolean existsByTitleAndIdNot(String title, String productId);

	 @Query("SELECT p FROM Product p WHERE p.live = true")
	 public Page<Product> findAllLive(Pageable pageable);


	public Page<Product> findByLive(Pageable pageable, Boolean True);

	public Page<Product> findByTitleContaining(Pageable pageable,String search);

	public Page<Product> findByCategory(Pageable pageable,Category category);


}
