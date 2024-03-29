package com.dollop.app.services;

import java.util.Map;

import com.dollop.app.dtos.CartItemDto;
import com.dollop.app.dtos.PageableResponse;

public interface ICartItemService {
	
	public Map<String, Object> addToCart(CartItemDto cartItemDto);
	public Map<String, Object> removeItemFromCart(String cartItemId);
	public Map<String, Object> updateCartItem(CartItemDto cartItemDto,String cartItemId);
	public CartItemDto getCartItemById(String cartItemId);
	public PageableResponse<CartItemDto> getAllCartItems(String userId,int pageNumber, int pageSize, String sortBy, String sortDirection);
	
}

