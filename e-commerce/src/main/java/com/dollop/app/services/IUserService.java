package com.dollop.app.services;

import java.io.IOException;
import java.util.List;

import com.dollop.app.dtos.PageableResponse;
import com.dollop.app.dtos.UserDto;


public interface IUserService {
	
	public UserDto createUser(UserDto userDto);
	
	
	public UserDto updateUser(UserDto userDto,String userId);

	public void deleteUser(String userId) throws IOException;
	
	public UserDto getOneUser(String userId);

	public List<UserDto> searchUser(String keyword);

	public PageableResponse<UserDto> getAllusers(int pageNumber, int pageSize, String sortBy, String sortDirection);
}


