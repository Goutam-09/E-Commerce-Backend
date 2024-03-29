package com.dollop.app.dtos;

import java.util.List;

import com.dollop.app.validate.ImageNameValid;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	private String userId;
	@Size(min=3,max=30,message = "Invalid Name !")
	private String name;
	@Email(message = "Invalid User Email !")
	@NotBlank(message = "Email is Required !")
	private String email;
	@Size(min=6,max=15)
	@NotBlank(message = "Password is Required !")
	private String password;
	@Size(min=4,max=6,message = "This Field is Required")
	private String gender;
	@NotBlank(message = "This Field is Required")
	private String about;
	@ImageNameValid
	private List<String> imageName;
}
