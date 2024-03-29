package com.dollop.app.services.impl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.dollop.app.dtos.CategoryDto;
import com.dollop.app.dtos.PageableResponse;
import com.dollop.app.entities.Category;
import com.dollop.app.exceptions.ResourceNotFoundException;
import com.dollop.app.helper.Helper;
import com.dollop.app.repositories.CategoryRepository;
import com.dollop.app.services.ICategoryService;
import com.dollop.app.util.Constants;

@Service
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		if (categoryRepository.existsBytitle(categoryDto.getTitle()))
			throw new ResourceNotFoundException(Constants.CATEGORY_ALREADY_EXIST + " " + categoryDto.getTitle());

		String categoryId = UUID.randomUUID().toString();
		categoryDto.setId(categoryId);
		Category saveCategory = categoryRepository.save(mapper.map(categoryDto, Category.class));
		return mapper.map(saveCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.CATEGORY_NOT_FOUND + " " + categoryId));

//		if(categoryRepository.existsByTitleAndIdNot(categoryDto.getTitle(),categoryId))
//			throw new ResourceNotFoundException(Constants.CATEGORY_ALREADY_EXIST+" "+categoryDto.getTitle());
//		
		category.setTitle(categoryDto.getTitle());
		category.setDiscription(categoryDto.getDiscription());
		category.setCoverImage(categoryDto.getCoverImage());

		return mapper.map(categoryRepository.save(category), CategoryDto.class);
	}

	@Override
	public void deleteCategory(String categoryId) {

		if (!categoryRepository.existsById(categoryId))
			throw new ResourceNotFoundException(Constants.CATEGORY_NOT_FOUND + " " + categoryId);

		categoryRepository.deleteById(categoryId);
	}

	@Override
	public CategoryDto getSingleCategory(String categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.CATEGORY_NOT_FOUND + " " + categoryId));
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy,
			String sortDirection) {
		Sort sort = (sortDirection.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
				: (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Category> page = categoryRepository.findAll(pageable);
		PageableResponse<CategoryDto> response = Helper.getPageableResponse(page, CategoryDto.class);
		return response;
	}

}
