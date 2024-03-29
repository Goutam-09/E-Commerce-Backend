package com.dollop.app.dtos;

import java.sql.Timestamp;
import java.util.List;

import com.dollop.app.entities.CartItem;
import com.dollop.app.entities.Category;
import com.dollop.app.entities.OrderItem;
import com.dollop.app.validate.ImageNameValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class ProductDto {

	private String id;
	@NotBlank(message = "This Field Is Required")
	@Size(min = 3, max = 20, message = "This Field Contain Atleast 3 Character And Max At 20")
	private String title;
	@NotBlank
	private Double price;
	private Timestamp createdAt;
	private Double discountedPrice;
	private Integer quantity;
	@NotBlank
	private Boolean live;
	private Boolean stock;
	
	@ImageNameValid
	private String imageName;

	private Category category;

	private List<OrderItem> orderItem;

	private List<CartItem> cartItem;

}
