package com.dollop.app.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.dtos.BadApiRequestException;
import com.dollop.app.dtos.PageableResponse;
import com.dollop.app.dtos.ProductDto;
import com.dollop.app.entities.Category;
import com.dollop.app.entities.Product;
import com.dollop.app.exceptions.ResourceNotFoundException;
import com.dollop.app.helper.Helper;
import com.dollop.app.repositories.CategoryRepository;
import com.dollop.app.repositories.ProductRepository;
import com.dollop.app.services.IFileService;
import com.dollop.app.services.IProductService;
import com.dollop.app.util.Constants;

@Service
public class ProductServiceImpl implements IProductService {

	@Value("${user.profile.image.path}")
	private String imagePath;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private IFileService fileService;

	@Autowired
	private ModelMapper mapper;

	@Override
	public ProductDto createProduct(ProductDto productDto, MultipartFile multipartFile) {

		if (productRepository.existsBytitle(productDto.getTitle()))
			throw new BadApiRequestException(Constants.PRODUCT_ALREADY_EXIST + " " + productDto.getTitle());

		String productId = UUID.randomUUID().toString();
		productDto.setId(productId);

		if (multipartFile != null) {
			try {
				String uploadFile = fileService.uploadFile(multipartFile, imagePath);
				productDto.setImageName(uploadFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Product saveProduct = productRepository.save(mapper.map(productDto, Product.class));
		return mapper.map(saveProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, String productId, MultipartFile multipartFile) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.PRODUCT_NOT_FOUND + " " + productId));

		if (productRepository.existsByTitleAndIdNot(productDto.getTitle(), productId))
			throw new BadApiRequestException(Constants.PRODUCT_ALREADY_EXIST + " " + productDto.getTitle());

		if (multipartFile != null) {
			String fullPath = imagePath + product.getImageName();
			Path path = Paths.get(fullPath);
			try {
				Files.delete(path);
				String uploadFile = fileService.uploadFile(multipartFile, imagePath);
				productDto.setImageName(uploadFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		product.setTitle(productDto.getTitle());
		product.setPrice(productDto.getPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setQuantity(productDto.getQuantity());
		product.setStock(productDto.getStock());
		product.setLive(productDto.getLive());
		product.setImageName(productDto.getImageName());
		return mapper.map(productRepository.save(product), ProductDto.class);
	}

	@Override
	public void deleteProduct(String productId) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.PRODUCT_NOT_FOUND + " " + productId));

		String fullPath = imagePath + product.getImageName();
		Path path = Paths.get(fullPath);
		try {
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		productRepository.deleteById(productId);
	}

	@Override
	public ProductDto getSingleProduct(String productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.PRODUCT_NOT_FOUND + " " + productId));
		return mapper.map(product, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy,
			String sortDirection) {
		Sort sort = (sortDirection.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
				: (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findAll(pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		return response;
	}

	@Override
	public PageableResponse<ProductDto> getAllLiveProduct(int pageNumber, int pageSize, String sortBy,
			String sortDirection) {
		Sort sort = (sortDirection.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
				: (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByLive(pageable, Boolean.TRUE);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		return response;
	}

	@Override
	public PageableResponse<ProductDto> searchByTitle(int pageNumber, int pageSize, String sortBy, String sortDirection,
			String title) {
		Sort sort = (sortDirection.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
				: (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByTitleContaining(pageable, title);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		return response;
	}

	@Override
	public ProductDto createProductWithCategory(ProductDto productDto, MultipartFile multipartFile, String categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.CATEGORY_NOT_FOUND + " " + categoryId));
		if (productRepository.existsBytitle(productDto.getTitle()))
			throw new BadApiRequestException(Constants.PRODUCT_ALREADY_EXIST + " " + productDto.getTitle());

		String productId = UUID.randomUUID().toString();
		productDto.setId(productId);
		productDto.setCategory(category);

		if (multipartFile != null) {
			try {
				String uploadFile = fileService.uploadFile(multipartFile, imagePath);
				productDto.setImageName(uploadFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Product saveProduct = productRepository.save(mapper.map(productDto, Product.class));
		return mapper.map(saveProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProductCategory(ProductDto productDto, MultipartFile multipartFile, String productId,
			String categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.PRODUCT_NOT_FOUND + " " + categoryId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.PRODUCT_NOT_FOUND + " " + productId));

		if (productRepository.existsByTitleAndIdNot(productDto.getTitle(), productId))
			throw new BadApiRequestException(Constants.PRODUCT_ALREADY_EXIST + " " + productDto.getTitle());

		if (multipartFile != null) {
			String fullPath = imagePath + product.getImageName();
			Path path = Paths.get(fullPath);
			try {
				Files.delete(path);
				String uploadFile = fileService.uploadFile(multipartFile, imagePath);
				productDto.setImageName(uploadFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		product.setCategory(category);
		product.setTitle(productDto.getTitle());
		product.setPrice(productDto.getPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setQuantity(productDto.getQuantity());
		product.setStock(productDto.getStock());
		product.setLive(productDto.getLive());
		product.setImageName(productDto.getImageName());
		return mapper.map(productRepository.save(product), ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProductOfCategory(String catogoryId, int pageNumber, int pageSize,
			String sortBy, String sortDirection) {

		if (!(categoryRepository.existsById(catogoryId)))
			throw new ResourceNotFoundException(Constants.CATEGORY_NOT_FOUND + " " + catogoryId);

		Sort sort = (sortDirection.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
				: (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByCategory(pageable, new Category(catogoryId));
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		return response;
	}

}
