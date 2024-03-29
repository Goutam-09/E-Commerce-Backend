package com.dollop.app.dtos;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.dollop.app.entities.CartItem;
import com.dollop.app.entities.User;

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
public class CartDto {
	
	private String cartId;
	@CreationTimestamp
	private Timestamp createdAt;
	private User user;
	private List<CartItem> items;
}
