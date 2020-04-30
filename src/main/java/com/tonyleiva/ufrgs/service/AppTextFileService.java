package com.tonyleiva.ufrgs.service;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.APP_FILES_PATH;
import static com.tonyleiva.ufrgs.util.UtilityClass.sort;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tonyleiva.ufrgs.model.input.DictionaryInput;
import com.tonyleiva.ufrgs.model.input.EasyInput;
import com.tonyleiva.ufrgs.model.input.TermInput;

@Service
public class AppTextFileService {

	private static final Logger logger = LoggerFactory.getLogger(AppTextFileService.class);
	private static final String BLANK = " ";
	private static final String DELIM = "=";
	private static final String COMMA = ",";
	private static final String TAB = "\t";

	@Value("${application.file.terms}")
	private String termsFilename;

	@Value("${application.file.easyWords}")
	private String easyWordsFilename;

	@Value("${application.file.dictionary}")
	private String dictionaryFilename;

	/**
	 * Load the Terms txt file and load the words into a TermInput
	 * list with the term word or phrase, size of the term phrase and the definition
	 * 
	 * @return TermInput list containing the terms collection
	 */
	public List<TermInput> loadTermsInput() {
		List<String> fileLines = openAppFile(termsFilename);
		List<TermInput> termsList = new ArrayList<>();

		sort(fileLines);

		for (String line : fileLines) {
			if (line.contains(DELIM)) {
				String[] sublines = line.split(DELIM);
				if (sublines.length == 2 && StringUtils.isNotBlank(sublines[0])
						&& StringUtils.isNotBlank(sublines[1])) {
					String term = sublines[0].trim();
					String definition = sublines[1].trim();
					termsList.add(new TermInput(countWords(term), term, definition));
				}
			}
		}

		return termsList;
	}

	/**
	 * Load the Simple Dictionary txt file and load the words into a DictionaryInput
	 * list with the complex word or phrase, size of the complex phrase and a list
	 * of suggestions
	 * 
	 * @return DictionaryInput list containing the simple dictionary collection
	 */
	public List<DictionaryInput> loadDictionaryInput() {
		List<String> fileLines = openAppFile(dictionaryFilename);
		List<DictionaryInput> dictionaryList = new ArrayList<>();
		
		sort(fileLines);
		
		for (String line : fileLines) {
			if (line.contains(DELIM)) {
				String[] sublines = line.split(DELIM);
				if (sublines.length == 2 && StringUtils.isNotBlank(sublines[0])
						&& StringUtils.isNotBlank(sublines[1])) {
					String complex = sublines[0].trim();
					List<String> suggestions = Arrays.stream(sublines[1].split(COMMA))
							.map(String::trim)
							.collect(Collectors.toList());
					dictionaryList.add(new DictionaryInput(countWords(complex), complex, suggestions));
				}
			}
		}

		return dictionaryList;
	}

	/**
	 * Load the Easy Words txt file and load the words into a EasyInput
	 * list with the easy word or phrase and size of it
	 * 
	 * @return EasyInput list containing the easy collection
	 */
	public List<EasyInput> loadEasyWordsInput() {
		List<String> fileLines = openAppFile(easyWordsFilename);
		Set<String> easySet = new HashSet<>();

		for (String line : fileLines) {
			if (StringUtils.isNoneBlank(line) && line.contains(TAB)) {
				String[] sublines = line.split(TAB);
				if (StringUtils.isNotBlank(sublines[0])) {
					easySet.add(sublines[0].trim());
				}
			}
		}

		List<String> easyList = easySet.stream().collect(Collectors.toList());
		sort(easyList);

		return easyList.stream()
				.map(s -> new EasyInput(countWords(s), s))
				.collect(Collectors.toList());
	}
	
	/**
	 * Load the Easy Words txt file and load the words into a simple List
	 * 
	 * @return List containing the easy words list
	 */
	public List<String> loadEasyWords() {
		Set<String> easyWordsSet = new HashSet<>();
		for (String line : this.openAppFile(easyWordsFilename)) {
			String[] lineArray = line.split("\\t");
			if (lineArray != null && lineArray[0] != null && StringUtils.isNotBlank(lineArray[0]))
				easyWordsSet.add(lineArray[0].trim());
		}
		return easyWordsSet.stream().collect(Collectors.toList());
	}

	/**
	 * Load the Simple Dictionary txt file and load the words into a Map with the
	 * word as key and a list of possible substitutions
	 * 
	 * @return Map containing the simple dictionary collection
	 */
	public Map<String, List<String>> loadSimpleDictionary() {
		Map<String, List<String>> simpleDictionaryMap = new HashMap<>();
		for (String line : this.openAppFile(dictionaryFilename)) {
			String[] lineArray = line.split("=");
			if (lineArray != null && lineArray.length > 1 && StringUtils.isNotBlank(lineArray[0])
					&& StringUtils.isNotBlank(lineArray[1])) {
				String keyValue = lineArray[0].trim();
				List<String> valueList = createListOfElements(lineArray[1], ",");
				if (!valueList.isEmpty())
					simpleDictionaryMap.put(keyValue, valueList);
			}
		}
		return simpleDictionaryMap;
	}

	private List<String> createListOfElements(String input, String separator) {
		List<String> elementList = new ArrayList<>();
		for (String value : input.split(separator)) {
			if (StringUtils.isNotBlank(value))
				elementList.add(value.trim());
		}
		return elementList;
	}

	/**
	 * Return a list of the file lines
	 * 
	 * @param filename
	 * @return list of lines
	 */
	public List<String> openAppFile(String filename) {
		File file = new File(APP_FILES_PATH, filename);
		List<String> fileLines = new ArrayList<>();

		try (Scanner sc = new Scanner(file)) {
			while (sc.hasNextLine()) {
				fileLines.add(sc.nextLine());
			}
		} catch (FileNotFoundException e) {
			logger.error("Error handling the file={}, ex={}", filename, e.getMessage());
		}

		return fileLines;
	}

	private int countWords(final String text) {
		return StringUtils.isBlank(text) ? 0 : StringUtils.countMatches(text.trim(), BLANK) + 1;
	}
}
