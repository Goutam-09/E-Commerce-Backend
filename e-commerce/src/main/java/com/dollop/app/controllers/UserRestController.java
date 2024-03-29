package com.dollop.app.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.dtos.ApiResponseMessage;
import com.dollop.app.dtos.ImageResponse;
import com.dollop.app.dtos.PageableResponse;
import com.dollop.app.dtos.UserDto;
import com.dollop.app.services.IFileService;
import com.dollop.app.services.IUserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("e-commerce/user/")
public class UserRestController {

	@Autowired
	private IUserService service;

	@Autowired
	private IFileService fileService;

	@Value("${user.profile.image.path}")
	private String imageUploadPath;

	private Logger logger = LoggerFactory.getLogger(UserRestController.class);

	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		return new ResponseEntity<>(service.createUser(userDto), HttpStatus.CREATED);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable String userId) throws IOException {
		System.err.println(userId);
		service.deleteUser(userId);
		ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("User is Deleted").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<>(responseMessage, HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
		return new ResponseEntity<>(service.getOneUser(userId), HttpStatus.OK);
	}

	/*
	 * @GetMapping public ResponseEntity<?> getAllUsers(@PathVariable Integer
	 * pageNo,@PathVariable Integer pageSize){ return new
	 * ResponseEntity<>(service.getAllusers(),HttpStatus.OK); }
	 */
	@GetMapping
	public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection) {
		return new ResponseEntity<>(service.getAllusers(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable String userId, @Valid @RequestBody UserDto userDto) {
		return new ResponseEntity<>(service.updateUser(userDto, userId), HttpStatus.OK);
	}

	@GetMapping("/search/{keywords}")
	public ResponseEntity<?> serchByKeywords(@PathVariable String keywords) {
		return ResponseEntity.ok(service.searchUser(keywords));
	}

//	@PostMapping("/image/{userId}")
//	public ResponseEntity<ImageResponse> uploadUserImage(
//			@RequestParam("userImage") MultipartFile image,
//			@PathVariable String userId
// 			) throws IOException{
//		String imageName = fileService.uploadFile(image, imageUploadPath);
//		UserDto user = service.getOneUser(userId);
//		user.setImageName(Arrays.asList(imageName));
//		UserDto userDto = service.updateUser(user, userId);
//		ImageResponse imageResponse = ImageResponse.builder()
//				.imageName(Arrays.asList(imageName))
//				.message("image is uploaded")
//				.success(true)
//				.status(HttpStatus.CREATED).build();
//			return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
//	}

	@GetMapping("/image/{userId}")
	public void serverUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
//		UserDto userDto = service.getOneUser(userId);
//		System.err.println(userDto.getImageName());
//	    List<String> list = userDto.getImageName();
//		for (String imageName : list) {
//			InputStream resource = fileService.getResource(imageUploadPath,imageName);
//			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//			StreamUtils.copy(resource, response.getOutputStream());
//		}
		UserDto userDto = service.getOneUser(userId);
		List<String> imageNames = userDto.getImageName();

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=images.zip");

		try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
			for (String imageName : imageNames) {
				InputStream resource = fileService.getResource(imageUploadPath, imageName);
				zipOut.putNextEntry(new ZipEntry(imageName));
				StreamUtils.copy(resource, zipOut);

				resource.close();
				zipOut.closeEntry();
			}
		}

	}

	@PostMapping("/upload/{userId}")
	public ResponseEntity<ImageResponse> uploadFiles(@PathVariable String userId,
			@RequestParam("files") MultipartFile[] files) {
		UserDto user = service.getOneUser(userId);
		List<String> imageNames = new ArrayList<>();
		Arrays.asList(files).stream().forEach(file -> {
			String imageName;
			try {
				imageName = fileService.uploadFile(file, imageUploadPath);
				imageNames.add(imageName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		user.setImageName(imageNames);
//	      UserDto userDto = service.updateUser(user, userId);
		service.updateUser(user, userId);
		ImageResponse imageResponse = ImageResponse.builder().imageName(imageNames).message("image is uploaded")
				.success(true).status(HttpStatus.CREATED).build();
		return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
	}

}