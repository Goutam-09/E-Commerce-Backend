package com.dollop.app.dtos;
import com.dollop.app.entities.Cart;
import com.dollop.app.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

	private String cartItemId;
	private Integer quantity;
	private Double totalPrice;
	private Cart cart;
	private Product product;
}
