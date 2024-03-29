package com.dollop.app.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
	public String uploadFile(MultipartFile file,String path) throws IOException;
	public InputStream getResource(String path,String name) throws FileNotFoundException;
	
}
