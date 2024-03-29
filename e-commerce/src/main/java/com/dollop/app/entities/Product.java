package com.dollop.app.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "products")
public class Product {

	@Id
	@Column(name = "product_id")
	private String id;
	@Column(name = "product_title",unique =true)
	private String title;
	@Column(length = 1000)
	private String description;
	private Double price;
	private Double discountedPrice;
	private Date createdAt;
	private Integer quantity;
	private Boolean live;
	private Boolean stock;
	@Column(name = "product_image_name")
	private String imageName;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "product")
	private List<OrderItem> orderItem;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "product")
	private List<CartItem> cartItem;
	
}
