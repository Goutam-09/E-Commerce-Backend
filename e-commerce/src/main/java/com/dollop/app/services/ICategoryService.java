package com.dollop.app.services;
import com.dollop.app.dtos.CategoryDto;
import com.dollop.app.dtos.PageableResponse;

public interface ICategoryService {
	
	public CategoryDto createCategory(CategoryDto categoryDto);
	public CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);
	public void deleteCategory(String categoryId);
	public CategoryDto getSingleCategory(String categoryId);
	public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDirection);

}
