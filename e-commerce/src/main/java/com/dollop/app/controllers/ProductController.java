package com.dollop.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.dtos.ApiResponseMessage;
import com.dollop.app.dtos.PageableResponse;
import com.dollop.app.dtos.ProductDto;
import com.dollop.app.services.IProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("e-commerce/product/")
public class ProductController {

	@Autowired
	private IProductService productService;

	@PostMapping("create")
	public ResponseEntity<ProductDto> createProduct(@Valid @RequestPart(value = "product") String product,
			@RequestPart(value = "imageName", required = false) MultipartFile multipartFiles) {
		ProductDto productDto = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			productDto = mapper.readValue(product, ProductDto.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(productService.createProduct(productDto, multipartFiles), HttpStatus.CREATED);
	}

	@PutMapping("update/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId,
			@Valid @RequestPart(value = "product") String product,
			@RequestPart(value = "imageName", required = false) MultipartFile multipartFiles) {
		ProductDto productDto = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			productDto = mapper.readValue(product, ProductDto.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(productService.updateProduct(productDto, productId, multipartFiles), HttpStatus.OK);
	}

	@DeleteMapping("delete/{productId}")
	public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {
		ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is Deleted").success(true)
				.status(HttpStatus.OK).build();
		productService.deleteProduct(productId);
		return new ResponseEntity<ApiResponseMessage>(responseMessage, HttpStatus.OK);
	}

	@GetMapping("single/{productId}")
	public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId) {
		return new ResponseEntity<>(productService.getSingleProduct(productId), HttpStatus.OK);
	}

	@GetMapping("all/products")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection) {
		return new ResponseEntity<>(productService.getAllProduct(pageNumber, pageSize, sortBy, sortDirection),
				HttpStatus.OK);
	}

	@GetMapping("live/products")
	public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProduct(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection) {
		return new ResponseEntity<>(productService.getAllProduct(pageNumber, pageSize, sortBy, sortDirection),
				HttpStatus.OK);
	}

	@GetMapping("bykey/{search}")
	public ResponseEntity<PageableResponse<ProductDto>> searchByTitle(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection,
			@PathVariable String search) {
		return new ResponseEntity<>(productService.searchByTitle(pageNumber, pageSize, sortBy, sortDirection, search),
				HttpStatus.OK);
	}

	@PostMapping("withCategory/{categoryId}")
	public ResponseEntity<?> createProductWithCategory(@PathVariable String categoryId,
			@Valid @RequestPart(value = "product") String product,
			@RequestPart(value = "imageName", required = false) MultipartFile multipartFiles) {
		ProductDto productDto = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			productDto = mapper.readValue(product, ProductDto.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(productService.createProductWithCategory(productDto, multipartFiles, categoryId),
				HttpStatus.CREATED);
	}

	@PutMapping("update/product/category/{productId}/{categoryId}")
	public ResponseEntity<?> updateProductCategory(@PathVariable String productId, @PathVariable String categoryId,
			@Valid @RequestPart(value = "product") String product,
			@RequestPart(value = "imageName", required = false) MultipartFile multipartFiles) {
		ProductDto productDto = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			productDto = mapper.readValue(product, ProductDto.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(
				productService.updateProductCategory(productDto, multipartFiles, productId, categoryId), HttpStatus.OK);
	}

	@GetMapping("byCategory/{categoryId}")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProductsOfCategory(@PathVariable String categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection) {
		return new ResponseEntity<>(
				productService.getAllProductOfCategory(categoryId, pageNumber, pageSize, sortBy, sortDirection),
				HttpStatus.OK);
	}
}
