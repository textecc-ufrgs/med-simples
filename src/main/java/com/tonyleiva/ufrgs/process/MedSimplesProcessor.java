package com.tonyleiva.ufrgs.process;

import static com.tonyleiva.ufrgs.util.UtilityClass.getCollator;
import static com.tonyleiva.ufrgs.util.UtilityClass.compareFirstLetter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tonyleiva.ufrgs.model.LemaWord;
import com.tonyleiva.ufrgs.model.input.TermInput;
import com.tonyleiva.ufrgs.service.AppTextFileService;
import com.tonyleiva.ufrgs.service.PassportFileService;
import com.tonyleiva.ufrgs.service.PassportJarService;

@Service
public class MedSimplesProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MedSimplesProcessor.class);
	private static final String PU = "PU";

	@Autowired
	private PassportFileService passportFileService;

	@Autowired
	private PassportJarService passportJarService;

	@Autowired
	private AppTextFileService textFileService;

	public List<LemaWord> process(String filename, String text) throws Exception {
		logger.info("Start processing");

		List<LemaWord> lemaWordList;
		// create file
		if (passportFileService.createTextFile(filename, text)) {
			// get Lemas into list
			lemaWordList = passportJarService.getLemas(filename);

			// delete file used by passportService
			if (passportFileService.existTextFile(filename))
				passportFileService.deleteTextFile(filename);

			processLists(lemaWordList);

		} else {
			throw new Exception("Erro na criação do arquivo");
		}

		return lemaWordList;
	}

	// TODO load all lists and then process hover lemaWordList
	private void processLists(List<LemaWord> lemaWordList) {
		// find in Terms
		findTerms(lemaWordList);

		// find in EasyWords
		findEasyWords(lemaWordList);

		// find in SimpleDic
		findSimpleDictionary(lemaWordList);

		// find in (Sinonimos)
		// find in (Exemplos)
	}

	/**
	 * Mark a phrase that exists in the list in file Terms and after the last word
	 * of the phrase will be added the suggestions for that phrase
	 * 
	 * @param lemaWordList
	 */
	private void findTerms(List<LemaWord> lemaWordList) {
		long start = System.currentTimeMillis();

		List<TermInput> termInput = textFileService.loadTermsInput();
		for (int i = 0; i < lemaWordList.size(); i++) {
			LemaWord lema = lemaWordList.get(i);

			//check if Lema is not a new line, is not a Punctuation and is not blank
			if (!lema.isNewLine() && !PU.equals(lema.getPosition()) && StringUtils.isNotBlank(lema.getLema())) {
				for (TermInput term : termInput) {
					if (compareFirstLetter(term.getTerm(), lema.getLema()) < 0) {
						logger.debug("Ignoring the first letter are different term={}, lema={}", term.getTerm(), lema.getLema());
						continue;
					} else if (compareFirstLetter(term.getTerm(), lema.getLema()) == 0) {
						logger.debug("Comparing lema={} and term={}", lema.getLema(), term.getTerm());
						
						if ((term.getSize() + i) <= lemaWordList.size()) {
							String lemaPhrase = "";
							for (int t = 0; t < term.getSize(); t++) {
								lemaPhrase += lemaWordList.get(i + t).getLema().concat(" ");
							}
							lemaPhrase = lemaPhrase.trim();
							logger.info("Phrases to compare lemaPhrase='{}', term='{}'", lemaPhrase, term.getTerm());
							if (getCollator().compare(lemaPhrase.trim(), term.getTerm()) == 0) {
								logger.info("### THEY MATCHED ###");
								i++;
								break;
							}
						}
					} else {
						break;
					}
				}
			}
		}

		long end = System.currentTimeMillis();
		logger.info("Load and process Terms - elapsed_time={}", end - start);
	}

	/**
	 * Set ignore={@code true} in {@code LemaWord} when {@code LemaWord.getLema()}
	 * is in the easy words list
	 * 
	 * @param lemaWordList
	 */
	private void findEasyWords(List<LemaWord> lemaWordList) {
		long start = System.currentTimeMillis();

		List<String> easyWordList = textFileService.loadEasyWords();
		for (LemaWord lemaWord : lemaWordList) {
			/* if the Lema is in easyWordsFile then mark it as ignore true */
			if (easyWordList.contains(lemaWord.getLema()))
				lemaWord.setIgnore(true);
		}

		long end = System.currentTimeMillis();
		logger.info("Load and process EasyWord - elapsed_time={}", end - start);
	}

	/**
	 * Set simple={@code List} in {@code LemaWord} when {@code LemaWord.getLema()}
	 * is in the Simple Dictionary KeySet list
	 * 
	 * @param lemaWordList
	 */
	private void findSimpleDictionary(List<LemaWord> lemaWordList) {
		long start = System.currentTimeMillis();

		Map<String, List<String>> simpleDictionary = textFileService.loadSimpleDictionary();
		Set<String> dictionaryWords = simpleDictionary.keySet();
		for (LemaWord lemaWord : lemaWordList) {
			if (dictionaryWords.contains(lemaWord.getLema()))
				lemaWord.setSimple(simpleDictionary.get(lemaWord.getLema()));
		}

		long end = System.currentTimeMillis();
		logger.info("Load and process SimpleDictionary - elapsed_time={}", end - start);
	}

}
