package com.tonyleiva.ufrgs.service;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.APP_FILES_PATH;
import static com.tonyleiva.ufrgs.util.ComparatorUtils.sort;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tonyleiva.ufrgs.constant.ReaderType;
import com.tonyleiva.ufrgs.constant.Subject;
import com.tonyleiva.ufrgs.model.input.DictionaryInput;
import com.tonyleiva.ufrgs.model.input.EasyInput;
import com.tonyleiva.ufrgs.model.input.TermInput;

@Service
public class AppTextFileService {

	private static final Logger logger = LoggerFactory.getLogger(AppTextFileService.class);
	private static final String BLANK = " ";
	private static final String COMMA = ",";
	private static final String TAB = "\t";

	@Value("${application.file.terms.fundamental}")
	private String termsFilenameFundamental;

	@Value("${application.file.terms.medio}")
	private String termsFilenameMedio;

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
	public List<TermInput> loadTermsInput(Subject subject, ReaderType reader) {
		final String termsFilename = getTermsInputFilename(subject, reader);
		List<String> fileLines = openAppFile(termsFilename);
		List<TermInput> termsList = new ArrayList<>();

		sort(fileLines);

		for (String line : fileLines) {
			if (line.contains(TAB)) {
				String[] sublines = line.split(TAB);
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

	private String getTermsInputFilename(Subject subject, ReaderType reader) {
		String filename;
		switch (reader) {
		case FUNDAMENTAL:
			filename = termsFilenameFundamental;
			break;
		case MEDIO:
			filename = termsFilenameMedio;
			break;
		default:
			filename = termsFilenameFundamental;
			break;
		}
		return filename;
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
			if (line.contains(TAB)) {
				String[] sublines = line.split(TAB);
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
	 * Return a list of the file lines
	 * 
	 * @param filename
	 * @return list of lines
	 */
	public List<String> openAppFile(String filename) {
		logger.info("Open file - Filename={}", filename);

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

	/** 
	 * Count blank space-separated words in the {@code text} String
	 * 
	 * @param text
	 * @return number of words
	 */
	private int countWords(final String text) {
		return StringUtils.isBlank(text) ? 0 : StringUtils.countMatches(text.trim(), BLANK) + 1;
	}
}
