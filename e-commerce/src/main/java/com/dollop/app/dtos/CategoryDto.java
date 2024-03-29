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
public class CategoryDto {


	private String id;
	@NotBlank
	@Size(min = 3,max = 20)
	private String title;
	@NotBlank
	@Size(max = 500)
	private String discription;
	private String coverImage;
}
