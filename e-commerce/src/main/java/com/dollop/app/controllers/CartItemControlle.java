package com.dollop.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dollop.app.dtos.CartItemDto;
import com.dollop.app.dtos.PageableResponse;
import com.dollop.app.services.ICartItemService;

@RestController
@RequestMapping("e-commerce/cartitem/")
public class CartItemControlle {

	@Autowired
	private ICartItemService cartItemService;

	@PostMapping("create/cart/item")
	public ResponseEntity<?> addToCart(@RequestBody CartItemDto cartItemDto) {
		return new ResponseEntity<>(cartItemService.addToCart(cartItemDto), HttpStatus.CREATED);
	}

	@DeleteMapping("remove/item/{cartItemId}")
	public ResponseEntity<?> removeItemFromCart(@PathVariable String cartItemId) {
		return new ResponseEntity<>(cartItemService.removeItemFromCart(cartItemId), HttpStatus.OK);
	}

	@PutMapping("update/cart/item/{cartItemId}")
	public ResponseEntity<?> updateCartItem(@PathVariable String cartItemId, @RequestBody CartItemDto cartItemDto) {
		return new ResponseEntity<>(cartItemService.updateCartItem(cartItemDto, cartItemId), HttpStatus.OK);
	}

	@GetMapping("get/cartItem/{cartItemId}")
	public ResponseEntity<?> getCartItemById(@PathVariable String cartItemId) {
		return new ResponseEntity<>(cartItemService.getCartItemById(cartItemId), HttpStatus.OK);
	}

	@GetMapping("get/all/cartItems/{userId}")
	public ResponseEntity<PageableResponse<CartItemDto>> getAllCartItems(@PathVariable String userId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection) {
		return new ResponseEntity<>(
				cartItemService.getAllCartItems(userId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
	}
}
