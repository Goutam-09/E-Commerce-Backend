package com.dollop.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dollop.app.dtos.CartDto;
import com.dollop.app.services.ICartService;

@RestController
@RequestMapping("e-commerce/cart/")
public class CartController {

	@Autowired
	private ICartService cartService;
	
	
	@GetMapping("get/cart/{userId}")
	public ResponseEntity<CartDto> getCartOfUser(
			@PathVariable String userId
			){
		return new ResponseEntity<>(cartService.getCartByUser(userId),HttpStatus.OK);
	}

}
