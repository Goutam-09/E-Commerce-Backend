package com.dollop.app.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dollop.app.dtos.CartDto;
import com.dollop.app.entities.Cart;
import com.dollop.app.entities.User;
import com.dollop.app.exceptions.ResourceNotFoundException;
import com.dollop.app.repositories.CartRepository;
import com.dollop.app.repositories.UserRepository;
import com.dollop.app.services.ICartService;
import com.dollop.app.util.Constants;

@Service
public class CartServiceImpl implements ICartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public void createCartForUser(String userId) {

		Optional<User> optional = userRepository.findById(userId);

		if (optional.isPresent()) {
			User user = optional.get();
			if (user.getCart() == null) {
				Cart cart = new Cart();
				String cartId = UUID.randomUUID().toString();
				cart.setCartId(cartId);
				cart.setUser(user);
				cartRepository.save(cart);
				user.setCart(cart);
				userRepository.save(user);
				System.err.println("Cart is created for user successfully");
			} else
				System.err.println("Cart is already created");
		} else
			throw new ResourceNotFoundException(Constants.USER_NOT_FOUND);
	}

	@Override
	public CartDto getCartByUser(String userId) {

		if (!(userRepository.existsById(userId)))
			throw new ResourceNotFoundException(Constants.USER_NOT_FOUND + " " + userId);

		Cart cart = cartRepository.findByUser(new User(userId))
				.orElseThrow(() -> new ResourceNotFoundException(Constants.CART_NOT_FOUND));
		return mapper.map(cart, CartDto.class);
	}

}
