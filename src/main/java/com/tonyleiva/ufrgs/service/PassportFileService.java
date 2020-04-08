package com.tonyleiva.ufrgs.service;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.PASSPORT_FILES_PATH;
import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.PASSPORT_WRITE_FILE_CHARSET;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class PassportFileService {

	public boolean createTextFile(String filename, String fileContent) {
		boolean fileCreated = false;
		Path path = Paths.get(PASSPORT_FILES_PATH, filename);
		try {
			Files.writeString(path, fileContent, PASSPORT_WRITE_FILE_CHARSET);
			fileCreated = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileCreated;
	}

	public boolean deleteTextFile(String filename) {
		boolean fileDeleted = false;
		Path path = Paths.get(PASSPORT_FILES_PATH, filename);

		try {
			if (existTextFile(filename)) {
				Files.delete(path);
				fileDeleted = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileDeleted;
	}

	public boolean existTextFile(String filename) {
		Path path = Paths.get(PASSPORT_FILES_PATH, filename);
		return Files.exists(path);
	} 
}