package com.dollop.app.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {
	private String roleId;
	@NotBlank(message = "This Field Is Required")
	@Size(min = 3,max = 20,message = "This Field Contain Atleast 3 Character And Max At 20")
	private String roleName;
}
