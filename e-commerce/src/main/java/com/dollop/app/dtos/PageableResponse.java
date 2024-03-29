package com.dollop.app.dtos;

import java.util.List;

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
public class PageableResponse<T> {

	private List<T> content;
	private int pageNumber;
	private int pageSize;
	private Long totalElements;
	private int totalPages;
	private Boolean lastPage;
	
}
