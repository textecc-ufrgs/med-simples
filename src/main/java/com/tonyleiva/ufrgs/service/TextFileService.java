package com.tonyleiva.ufrgs.service;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.PASSPORT_FILES;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class TextFileService {

	public boolean createTextFile(String filename, String fileContent) {
		boolean fileCreated = false;
		Path file = Paths.get(PASSPORT_FILES, filename);
		try {
			Files.writeString(file, fileContent, StandardCharsets.ISO_8859_1);
			fileCreated = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileCreated;
	}

}