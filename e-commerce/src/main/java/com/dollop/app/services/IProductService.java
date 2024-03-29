package com.dollop.app.services;

import com.dollop.app.dtos.ProductDto;

import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.dtos.PageableResponse;

public interface IProductService {
	
	public ProductDto createProduct(ProductDto ProductDto,MultipartFile multipartFile);
	public ProductDto updateProduct(ProductDto ProductDto,String productId,MultipartFile multipartFile);
	public void deleteProduct(String productId);
	public ProductDto getSingleProduct(String productId);
	public PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDirection);
	public PageableResponse<ProductDto> getAllLiveProduct(int pageNumber, int pageSize, String sortBy, String sortDirection);
	public PageableResponse<ProductDto> searchByTitle(int pageNumber, int pageSize, String sortBy, String sortDirection,String title);
	public ProductDto createProductWithCategory(ProductDto productDto,MultipartFile multipartFile,String categoryId);
	public ProductDto updateProductCategory(ProductDto productDto,MultipartFile multipartFile,String productId,String categoryId);
	public PageableResponse<ProductDto> getAllProductOfCategory(String catogoryId,int pageNumber, int pageSize, String sortBy, String sortDirection);
}
