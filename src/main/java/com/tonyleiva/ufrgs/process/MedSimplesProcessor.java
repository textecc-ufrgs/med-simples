package com.tonyleiva.ufrgs.process;

import static com.tonyleiva.ufrgs.util.UtilityClass.compareStrings;
import static com.tonyleiva.ufrgs.util.UtilityClass.initialLetterEqualTo;
import static com.tonyleiva.ufrgs.util.UtilityClass.initialLetterIsLessThan;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tonyleiva.ufrgs.model.LemaWord;
import com.tonyleiva.ufrgs.model.input.DictionaryInput;
import com.tonyleiva.ufrgs.model.input.TermInput;
import com.tonyleiva.ufrgs.process.output.WordDTO;
import com.tonyleiva.ufrgs.service.AppTextFileService;
import com.tonyleiva.ufrgs.service.PassportFileService;
import com.tonyleiva.ufrgs.service.PassportJarService;

@Service
public class MedSimplesProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MedSimplesProcessor.class);
	private static final String BLANK = " ";
	private static final String PU = "PU";

	@Autowired
	private PassportFileService passportFileService;

	@Autowired
	private PassportJarService passportJarService;

	@Autowired
	private AppTextFileService textFileService;

	private List<TermInput> termInput;
	private List<DictionaryInput> dictionaryInput;
	private List<String> easyWordList; //TODO complete - change to input object

	private List<WordDTO> dtoList;
	
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
		loadAllFiles();
		dtoList = new ArrayList<>();

		for (int index = 0; index < lemaWordList.size(); index++) {

			if (!lemaWordList.get(index).isIgnore()) {
				if (isAvailableToProcess(lemaWordList.get(index))) {
		
					findTerms(lemaWordList, index);
					
					if (!lemaWordList.get(index).isIgnore())
						findSimpleDictionary(lemaWordList, index);
					
					if (!lemaWordList.get(index).isIgnore())
						findEasyWords(lemaWordList, index);

					if (!lemaWordList.get(index).isIgnore())
						handleNotMatchedItem(lemaWordList.get(index), index);
				} else {
					handleNotWordItem(lemaWordList.get(index), index);
				}
			}
		}
	}

	/**
	 * Load all the list into the files
	 */
	private void loadAllFiles() {
		long start = System.currentTimeMillis();

		termInput = textFileService.loadTermsInput();
		dictionaryInput = textFileService.loadSimpleDictionaryInput();
		easyWordList = textFileService.loadEasyWords();

		long end = System.currentTimeMillis();
		logger.info("Load all lists - elapsed_time={}", end - start);
	}

	/**
	 * Mark a phrase that exists in the list in file Terms and after the last word
	 * of the phrase will be added the suggestions for that phrase
	 * 
	 * @param lemaWordList
	 */
	private void findTerms(List<LemaWord> lemaWordList, final int index) {
		long start = System.currentTimeMillis();

		LemaWord lema = lemaWordList.get(index);
		boolean shouldBreak = false;

		for (TermInput term : termInput) {
			if (initialLetterIsLessThan(term.getTerm(), lema.getLema())) {
				logger.debug("Ignoring, the first letter are different lema='{}', term='{}'", lema.getLema(), term.getTerm());
			} else if (initialLetterEqualTo(term.getTerm(), lema.getLema()) && (term.getSize() + index) <= lemaWordList.size()) {
				logger.debug("Comparing lema='{}', term='{}'", lema.getLema(), term.getTerm());

				String lemaPhrase = createPhraseToCompare(lemaWordList, term.getSize(), index);
				logger.info("Phrases to compare lemaPhrase='{}', term='{}'", lemaPhrase, term.getTerm());
				if (compareStrings(lemaPhrase, term.getTerm()) == 0) {
					logger.info("### THEY MATCHED ###");
					handleMatchedTerm(lemaWordList, index, term.getSize() + index);
					setIgnoreMatchedItem(lemaWordList, index, term.getSize() + index);
					shouldBreak = true;
				}

			} else {
				logger.debug("Breaking the loop, passed the alphabetical order lema='{}', term='{}' ", lema.getLema(), term.getTerm());
				shouldBreak = true;
			}

			if (shouldBreak)
				break;
		}

		long end = System.currentTimeMillis();
		logger.info("Process Terms - elapsed_time={}", end - start);
	}

	/**
	 * Set simple={@code List} in {@code LemaWord} when {@code LemaWord.getLema()}
	 * is in the Simple Dictionary KeySet list
	 * 
	 * @param lemaWordList
	 */
	private void findSimpleDictionary(List<LemaWord> lemaWordList, final int index) {
		long start = System.currentTimeMillis();

		LemaWord lema = lemaWordList.get(index);
		boolean shouldBreak = false;

		for (DictionaryInput dictionary : dictionaryInput) {
			//TODO complete
		}
		
		long end = System.currentTimeMillis();
		logger.info("Process SimpleDictionary - elapsed_time={}", end - start);
	}
	
	/**
	 * Set ignore={@code true} in {@code LemaWord} when {@code LemaWord.getLema()}
	 * is in the easy words list
	 * 
	 * @param lemaWordList
	 */
	private void findEasyWords(List<LemaWord> lemaWordList, final int index) {
		long start = System.currentTimeMillis();

		//TODO complete

		long end = System.currentTimeMillis();
		logger.info("Load and process EasyWord - elapsed_time={}", end - start);
	}

	/**
	 * Check if Lema has not been processed yet and if it is not a Punctuation and
	 * it is not blank
	 * 
	 * @param lema LemaWord
	 * @return true if should be analyzed
	 */
	private boolean isAvailableToProcess(LemaWord lema) {
		return !lema.isIgnore() && !PU.equals(lema.getPosition()) && StringUtils.isNotBlank(lema.getLema());
	}
	
	/**
	 * Creates a lema phrase to compare with term phrase. The phrase will have size={@code phraseSize}, starting at position={@code index} in {@code lemaWordList}
	 * @param lemaWordList
	 * @param phraseSize
	 * @param index
	 * @return the phrase
	 */
	private String createPhraseToCompare(List<LemaWord> lemaWordList, int phraseSize, int index) {
		StringBuilder lemaPhraseSB = new StringBuilder();
		for (int position = index; position < phraseSize + index; position++) {
			lemaPhraseSB.append(lemaWordList.get(position).getLema()).append(BLANK);
		}
		return lemaPhraseSB.toString().trim();
	}
	
	/**
	 * Set Ignore=true to the matched items in lemaWordList
	 * @param lemaWordList
	 * @param from
	 * @param to
	 */
	private void setIgnoreMatchedItem(List<LemaWord> lemaWordList, int from, int to) {
		for (int position = from; position < to; position++) {
			lemaWordList.get(position).setIgnore(true);
		}
	}
	
	private void addNewItem(WordDTO dto) {
		dtoList.add(dto);
	}

	private void handleNotWordItem(LemaWord lemaWord, int index) {
		//TODO complete
	}

	private void handleNotMatchedItem(LemaWord lemaWord, int index) {
		//TODO complete
	}

	private void handleMatchedTerm(List<LemaWord> lemaWordList, int from, int to) {
		//TODO complete
	}
}
