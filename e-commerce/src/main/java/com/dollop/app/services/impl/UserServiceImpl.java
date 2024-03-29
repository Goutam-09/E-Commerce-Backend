package com.dollop.app.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.dollop.app.dtos.PageableResponse;
import com.dollop.app.dtos.UserDto;
import com.dollop.app.entities.User;
import com.dollop.app.exceptions.ResourceNotFoundException;
import com.dollop.app.helper.Helper;
import com.dollop.app.repositories.UserRepository;
import com.dollop.app.services.ICartService;
import com.dollop.app.services.IUserService;
import com.dollop.app.util.Constants;

@Service
public class UserServiceImpl implements IUserService {

	@Value("${user.profile.image.path}")
	private String imageUploadPath;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ICartService cartService;

	@Override
	public UserDto createUser(UserDto userDto) {
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);

		User user = dtoToEntity(userDto);

		User savedUser = userRepository.save(user);

		if (savedUser.getUserId() != null) {
			cartService.createCartForUser(userId);
		} else
			throw new ResourceNotFoundException(Constants.USER_NOT_CREATED);
		UserDto newDto = entityToDto(savedUser);
		return newDto;
	}

	private UserDto entityToDto(User savedUser) {
//		UserDto userDto = UserDto.builder()
//				.userId(savedUser.getUserId())
//				.name(savedUser.getName())
//				.email(savedUser.getEmail())
//				.password(savedUser.getPassword())
//				.gender(savedUser.getGender())
//				.about(savedUser.getAbout())
//				.imageName(savedUser.getImageName())
//				.build();
		return mapper.map(savedUser, UserDto.class);
	}

	private User dtoToEntity(UserDto userDto) {
//		User user = User.builder()
//				.userId(userDto.getUserId())
//				.name(userDto.getName())
//				.email(userDto.getEmail())
//				.password(userDto.getPassword())
//				.gender(userDto.getGender())
//				.about(userDto.getAbout())
//				.imageName(userDto.getImageName())
//				.build();
		return mapper.map(userDto, User.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND + " " + userId));
		user.setName(userDto.getName());
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setImageName(userDto.getImageName());
		User updatedUser = userRepository.save(user);
		return mapper.map(updatedUser, UserDto.class);
	}

	@Override
	public void deleteUser(String userId) throws IOException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND + " " + userId));
		String fullPath = imageUploadPath + user.getImageName();
		Path path = Paths.get(fullPath);
		Files.delete(path);
		userRepository.delete(user);
	}

	/*
	 * @Override public List<UserDto> getAllusers() { List<UserDto> list =
	 * userRepository.findAll() .stream() .map((user) -> mapper.map(user,
	 * UserDto.class)) .collect(Collectors.toList()); return list; }
	 */
	@Override
	public PageableResponse<UserDto> getAllusers(int pageNumber, int pageSize, String sortBy, String sortDirection) {
		Sort sort = (sortDirection.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
				: (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<User> page = userRepository.findAll(pageable);
		PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
		return response;
	}

	@Override
	public UserDto getOneUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND + " " + userId));
		return mapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> searchUser(String keywords) {
		List<User> list = userRepository.findByNameContaining(keywords);
		return list.stream().map(user -> mapper.map(user, UserDto.class)).collect(Collectors.toList());
	}

}
