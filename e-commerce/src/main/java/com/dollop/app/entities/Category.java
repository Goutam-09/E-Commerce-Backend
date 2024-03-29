package com.dollop.app.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@Table(name = "categories")
public class Category {

	public Category(String categoryId) {
		this.categoryId = categoryId;
	}

	@Id
	@Column(name = "category_id")
	private String categoryId;
	@Column(name = "category_title",length = 60,nullable = false)
	private String title;
	@Column(name = "category_discription",length = 500)
	private String discription;
	@Column(name = "Category_cover_Image")
	private String coverImage;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "category")
	private List<Product> productsList;
}
