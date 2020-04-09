package com.tonyleiva.ufrgs.service;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.APP_FILES_PATH;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AppTextFileService {

	private static final Logger logger = LoggerFactory.getLogger(AppTextFileService.class);

	@Value("${application.file.terms}")
	private String termsFilename;

	@Value("${application.file.easyWords}")
	private String easyWordsFilename;

	@Value("${application.file.dictionary}")
	private String dictionaryFilename;

	public List<String> loadTerms() {
		logger.debug("Loading terms file");
		return openAppFile(termsFilename);
	}

	public Set<String> loadEasyWords() {
		Set<String> easyWordsSet = new HashSet<>();
		for (String line : this.openAppFile(easyWordsFilename)) {
			String[] lineArray = line.split("\\t");
			if (lineArray != null && lineArray[0] != null && StringUtils.hasText(lineArray[0]))
				easyWordsSet.add(lineArray[0].trim());
		}
		return easyWordsSet;
	}

	public Map<String, String> loadSimpleDictionary() {
		Map<String, String> simpleDictionaryMap = new HashMap<>();
//		for (String line : this.openAppFile()) {
//			String[] lineArray = line.split("=");
//			if (lineArray != null && lineArray[0] != null && StringUtils.hasText(lineArray[0]))
//				easyWordsSet.add(lineArray[0].trim());
//		}
		return simpleDictionaryMap;
	}

	public List<String> openAppFile(String filename) {
		File file = new File(APP_FILES_PATH, filename);
		List<String> fileLines = new ArrayList<>();

		try (Scanner sc = new Scanner(file)) {
			while (sc.hasNextLine()) {
				fileLines.add(sc.nextLine());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileLines;
	}
}
