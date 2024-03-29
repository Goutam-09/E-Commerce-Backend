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

import com.dollop.app.dtos.ApiResponseMessage;
import com.dollop.app.dtos.CategoryDto;
import com.dollop.app.dtos.PageableResponse;
import com.dollop.app.services.ICategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("e-commerce/category/")
public class CategoryController {

	@Autowired
	private ICategoryService categoryService;

	@PostMapping("create")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
	}

	@PutMapping("update/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId,
			@Valid @RequestBody CategoryDto categoryDto) {
		return new ResponseEntity<>(categoryService.updateCategory(categoryDto, categoryId), HttpStatus.OK);
	}

	@DeleteMapping("delete/{categoryId}")
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {

		categoryService.deleteCategory(categoryId);
		ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is Deleted").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<>(responseMessage, HttpStatus.OK);
	}

	@GetMapping("single/{categoryId}")
	public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String categoryId) {
		return new ResponseEntity<>(categoryService.getSingleCategory(categoryId), HttpStatus.OK);
	}

	@GetMapping("all")
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection) {
		return new ResponseEntity<>(categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDirection),
				HttpStatus.OK);
	}
}
