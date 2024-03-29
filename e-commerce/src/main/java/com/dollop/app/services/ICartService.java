package com.dollop.app.services;

import com.dollop.app.dtos.CartDto;

public interface ICartService {
	
	public void createCartForUser(String userId);
	public CartDto getCartByUser(String userId);
}
