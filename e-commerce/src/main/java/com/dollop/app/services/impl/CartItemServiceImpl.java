package com.dollop.app.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dollop.app.dtos.CartItemDto;
import com.dollop.app.dtos.PageableResponse;
import com.dollop.app.entities.Cart;
import com.dollop.app.entities.CartItem;
import com.dollop.app.entities.User;
import com.dollop.app.exceptions.ResourceNotFoundException;
import com.dollop.app.helper.Helper;
import com.dollop.app.repositories.CartItemRepository;
import com.dollop.app.repositories.CartRepository;
import com.dollop.app.repositories.ProductRepository;
import com.dollop.app.repositories.UserRepository;
import com.dollop.app.services.ICartItemService;
import com.dollop.app.util.Constants;

@Service
public class CartItemServiceImpl implements ICartItemService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ModelMapper mapper;

	private Map<String, Object> map = null;

	@Override
	public Map<String, Object> addToCart(CartItemDto cartItemDto) {

		productRepository.findById(cartItemDto.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException(
				Constants.PRODUCT_NOT_FOUND + " " + cartItemDto.getProduct().getId()));

		Cart cart = cartRepository.findById(cartItemDto.getCart().getCartId())
				.orElseThrow(() -> new ResourceNotFoundException(Constants.CART_NOT_FOUND));

		CartItem existingCartItem = cart.getItems().stream()
				.filter(item -> item.getProduct().getId().equals(cartItemDto.getProduct().getId())).findFirst()
				.orElse(null);

		if (existingCartItem != null) {
			existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDto.getQuantity());
			existingCartItem.setTotalPrice(existingCartItem.getQuantity() * cartItemDto.getProduct().getPrice());
			existingCartItem = cartItemRepository.save(existingCartItem);
			if (existingCartItem != null) {
				map = new HashMap<>();
				map.put(Constants.MESSAGE, Constants.ITEM_UPDATED_TO_CART);
			} else {
				map = new HashMap<>();
				map.put(Constants.MESSAGE, Constants.ITEM_UPDATED_FAILED);
			}
		} else {
			String cartItemId = UUID.randomUUID().toString();
			cartItemDto.setCartItemId(cartItemId);
			cart.getItems().add(mapper.map(cartItemDto, CartItem.class));
			Cart save = cartRepository.save(cart);
			if (save != null) {
				map = new HashMap<>();
				map.put(Constants.MESSAGE, Constants.ITEM_ADDED_TO_CART);
			} else {
				map = new HashMap<>();
				map.put(Constants.MESSAGE, Constants.ITEM_FAILED_TO_CART);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> removeItemFromCart(String cartItemId) {
		CartItem cartItem = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.CART_NOT_FOUND));

		cartItemRepository.delete(cartItem);
		if (cartItem.getCartItemId() != null)
			throw new ResourceNotFoundException(Constants.ITEM_REMOVE_FAILED);

		else {
			map = new HashMap<>();
			map.put(Constants.MESSAGE, Constants.ITEM_REMOVE);
		}
		return map;
	}

	@Override
	public Map<String, Object> updateCartItem(CartItemDto cartItemDto, String cartItemId) {

		productRepository.findById(cartItemDto.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException(
				Constants.PRODUCT_NOT_FOUND + " " + cartItemDto.getProduct().getId()));

		CartItem cartItem = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.CARTITEM_NOT_FOUND));

		if (cartItemDto.getQuantity() != 0) {
			cartItem.setQuantity(cartItemDto.getQuantity());
			cartItem.setTotalPrice(cartItemDto.getProduct().getPrice() * cartItem.getQuantity());
			CartItem save = cartItemRepository.save(cartItem);
			if (save != null) {
				map = new HashMap<>();
				map.put(Constants.MESSAGE, Constants.ITEM_UPDATED_TO_CART);
			} else {
				map = new HashMap<>();
				map.put(Constants.MESSAGE, Constants.ITEM_REMOVE);
			}
		}
		else {
			map = removeItemFromCart(cartItemId);
		}
		return map;
	}

	@Override
	public CartItemDto getCartItemById(String cartItemId) {

		CartItem cartItem = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.CARTITEM_NOT_FOUND));

		return mapper.map(cartItem, CartItemDto.class);
	}

	@Override
	public PageableResponse<CartItemDto> getAllCartItems(String userId, int pageNumber, int pageSize, String sortBy,
			String sortDirection) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND + " " + userId));

		Cart cart = user.getCart();
		Sort sort = (sortDirection.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
				: (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<CartItem> page = cartItemRepository.findByCart(pageable, cart);
		PageableResponse<CartItemDto> response = Helper.getPageableResponse(page, CartItemDto.class);
		return response;
	}
}
