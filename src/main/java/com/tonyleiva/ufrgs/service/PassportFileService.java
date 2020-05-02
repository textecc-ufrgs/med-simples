package com.tonyleiva.ufrgs.service;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.PASSPORT_FILES_PATH;
import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.PASSPORT_WRITE_FILE_CHARSET;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PassportFileService {

	private static Logger logger = LoggerFactory.getLogger(PassportFileService.class);

	public boolean createTextFile(String filename, String fileContent) {
		boolean fileCreated = false;
		Path path = Paths.get(PASSPORT_FILES_PATH, filename);
		try {
			// Change '\n' for '# ' to detect new line later
			Iterable<String> sc = () -> new Scanner(fileContent.replace("\n", "# "))
					.useDelimiter(System.getProperty("line.separator"));
			Files.write(path, sc, PASSPORT_WRITE_FILE_CHARSET);
			fileCreated = true;
		} catch (IOException e) {
			logger.error(e.getMessage());
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
			logger.error(e.getMessage());
		}

		return fileDeleted;
	}

	public boolean existTextFile(String filename) {
		Path path = Paths.get(PASSPORT_FILES_PATH, filename);
		return Files.exists(path);
	}
}