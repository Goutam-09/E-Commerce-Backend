package com.dollop.app.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.dtos.BadApiRequestException;
import com.dollop.app.services.IFileService;

@Service
public class FileServiceImpl implements IFileService {

	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {
		String originalFilename = file.getOriginalFilename();
		logger.info("File Name : {}", originalFilename);
		String fileName = UUID.randomUUID().toString();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String fileNameWithExtension = fileName + extension;
		String fullPathWithFileName = path + fileNameWithExtension;
		logger.info("Full Image Path : {}", fullPathWithFileName);
		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")
				|| extension.equalsIgnoreCase(".jpeg")) {
			logger.info("Extention is : {}", extension);
			File folder = new File(path);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
		} else {
			throw new BadApiRequestException("File With this " + extension + " not allowed");
		}
		return fileNameWithExtension;
	}

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {
		String fullPath = path + File.separator + name;
		InputStream inputStream = new FileInputStream(fullPath);
		return inputStream;
	}

}
