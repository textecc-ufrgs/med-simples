package com.tonyleiva.ufrgs.process;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.POS_FILTER;
import static com.tonyleiva.ufrgs.util.UtilityClass.compareStrings;
import static com.tonyleiva.ufrgs.util.UtilityClass.initialLetterEqualTo;
import static com.tonyleiva.ufrgs.util.UtilityClass.initialLetterIsLessThan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tonyleiva.ufrgs.model.LemaWord;
import com.tonyleiva.ufrgs.model.input.DictionaryInput;
import com.tonyleiva.ufrgs.model.input.EasyInput;
import com.tonyleiva.ufrgs.model.input.TermInput;
import com.tonyleiva.ufrgs.process.output.WordDTO;
import com.tonyleiva.ufrgs.service.AppTextFileService;
import com.tonyleiva.ufrgs.service.PassportFileService;
import com.tonyleiva.ufrgs.service.PassportJarService;

@Service
public class MedSimplesProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MedSimplesProcessor.class);

	private static final String CONTRACTION = "contraction";
	private static final String PUNCTUATION = "PU";
	private static final String BLANK = " ";

	@Autowired
	private PassportFileService passportFileService;

	@Autowired
	private PassportJarService passportJarService;

	@Autowired
	private AppTextFileService textFileService;

	private List<TermInput> termInput;
	private List<DictionaryInput> dictionaryInput;
	private List<EasyInput> easyWordList;

	private List<WordDTO> dtoList;

	@PostConstruct
    public void init() {
        loadAllFiles();
    }

	public List<WordDTO> process(String filename, String text) throws Exception {
		logger.info("Start processing");

		List<LemaWord> lemaWordList;
		//create file
		if (passportFileService.createTextFile(filename, text)) {
			//get Lemas into list
			lemaWordList = passportJarService.getLemas(filename);

			//delete file used by passportService
			if (passportFileService.existTextFile(filename))
				passportFileService.deleteTextFile(filename);

			//load and process if the lema list was properly set
			if (lemaWordList != null && !lemaWordList.isEmpty()) {
				dtoList = new ArrayList<>();
				processLists(lemaWordList);
			}

		} else {
			throw new Exception("Erro na criação do arquivo");
		}

		return dtoList;
	}

	/**
	 * Process the comparison in all given lists
	 * @param lemaWordList
	 */
	private void processLists(List<LemaWord> lemaWordList) {
		long start = System.currentTimeMillis();

		for (int index = 0; index < lemaWordList.size(); index++) {
			if (!lemaWordList.get(index).isIgnore()) {
				if (isAvailableWord(lemaWordList.get(index))) {

					processItem(lemaWordList, index);
					
				} else {
					addNotWordItem(lemaWordList.get(index), index);
				}
			}
		}
		
		long end = System.currentTimeMillis();
		logger.info("Process lists - elapsed_time={}", end - start);
	}

	/**
	 * Process the comparison in all given lists for item at index={@code index}
	 * @param lemaWordList
	 * @param index
	 */
	private void processItem(List<LemaWord> lemaWordList, final int index) {
		if (isNotMarkedAsIgnore(lemaWordList, index))
			findTerms(lemaWordList, index);

		if (isNotMarkedAsIgnore(lemaWordList, index))
			findDictionary(lemaWordList, index);

		if (isNotMarkedAsIgnore(lemaWordList, index))
			findPosFilter(lemaWordList, index);

		if (isNotMarkedAsIgnore(lemaWordList, index))
			findEasyWords(lemaWordList, index);

		if (isNotMarkedAsIgnore(lemaWordList, index))
			addNotMatchedItem(lemaWordList.get(index), index);
	}

	/**
	 * Load all the list into the files
	 */
	private void loadAllFiles() {
		long start = System.currentTimeMillis();

		termInput = textFileService.loadTermsInput();
		dictionaryInput = textFileService.loadDictionaryInput();
		easyWordList = textFileService.loadEasyWordsInput();

		long end = System.currentTimeMillis();
		logger.info("Load all lists - elapsed_time={}", end - start);
	}

	/** 
	 * Check if LemaWord at index is not marked as ignore
	 * @param lemaWordList
	 * @param index
	 * @return true if LemaWord should be process, false to ignore
	 */
	private boolean isNotMarkedAsIgnore(List<LemaWord> lemaWordList, final int index) {
		return !lemaWordList.get(index).isIgnore();
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
			} else if (initialLetterEqualTo(term.getTerm(), lema.getLema())
					&& (term.getSize() + index) <= lemaWordList.size()) {
				logger.debug("Comparing lema='{}', term='{}'", lema.getLema(), term.getTerm());

				String lemaPhrase = createPhraseToCompare(lemaWordList, term.getSize(), index);
				logger.debug("Phrases to compare lemaPhrase='{}', term='{}'", lemaPhrase, term.getTerm());
				if (compareStrings(lemaPhrase, term.getTerm()) == 0) {
					logger.info("### TERM ### - lemaPhrase='{}', term='{}'", lemaPhrase, term.getTerm());
					addMatchedTerm(lemaWordList, term.getDefinition(), index, term.getSize() + index);
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
		logger.debug("Process Terms - elapsed_time={}", end - start);
	}

	/**
	 * Set simple={@code List} in {@code LemaWord} when {@code LemaWord.getLema()}
	 * is in the Simple Dictionary KeySet list
	 * 
	 * @param lemaWordList
	 */
	private void findDictionary(List<LemaWord> lemaWordList, final int index) {
		long start = System.currentTimeMillis();

		LemaWord lema = lemaWordList.get(index);
		boolean shouldBreak = false;

		for (DictionaryInput dictionary : dictionaryInput) {
			if (initialLetterIsLessThan(dictionary.getComplex(), lema.getLema())) {
				logger.debug("Ignoring, the first letter are different lema='{}', complex='{}'", lema.getLema(), dictionary.getComplex());
			}  else if (initialLetterEqualTo(dictionary.getComplex(), lema.getLema())
					&& (dictionary.getSize() + index) <= lemaWordList.size()) {
				logger.debug("Comparing lema='{}', complex='{}'", lema.getLema(), dictionary.getComplex());

				String lemaPhrase = createPhraseToCompare(lemaWordList, dictionary.getSize(), index);
				logger.debug("Phrases to compare lemaPhrase='{}', complex='{}'", lemaPhrase, dictionary.getComplex());
				if (compareStrings(lemaPhrase, dictionary.getComplex()) == 0) {
					logger.info("### DICTIONARY ### - lemaPhrase='{}', complex='{}'", lemaPhrase, dictionary.getComplex());
					addMatchedDictionary(lemaWordList, dictionary.getSuggestions(), index, dictionary.getSize() + index);
					setIgnoreMatchedItem(lemaWordList, index, dictionary.getSize() + index);
					shouldBreak = true;
				}
			} else {
				logger.debug("Breaking the loop, passed the alphabetical order lema='{}', complex='{}' ", lema.getLema(), dictionary.getComplex());
				shouldBreak = true;
			}

			if (shouldBreak)
				break;
		}

		long end = System.currentTimeMillis();
		logger.debug("Process Dictionary - elapsed_time={}", end - start);
	}

	private void findPosFilter(List<LemaWord> lemaWordList, final int index) {
		if (POS_FILTER.contains(lemaWordList.get(index).getPosition())) {
			addMatchedFilter(lemaWordList, index);
			setIgnoreMatchedItem(lemaWordList, index, 1 + index);
		}
	}

	/**
	 * Set ignore={@code true} in {@code LemaWord} when {@code LemaWord.getLema()}
	 * is in the easy words list
	 * 
	 * @param lemaWordList
	 */
	private void findEasyWords(List<LemaWord> lemaWordList, final int index) {
		long start = System.currentTimeMillis();

		LemaWord lema = lemaWordList.get(index);
		boolean shouldBreak = false;

		for (EasyInput easy : easyWordList) {
			if (initialLetterIsLessThan(easy.getEasy(), lema.getLema())) {
				logger.debug("Ignoring, the first letter are different lema='{}', easy='{}'", lema.getLema(), easy.getEasy());
			}  else if (initialLetterEqualTo(easy.getEasy(), lema.getLema()) && (easy.getSize() + index) <= lemaWordList.size()) {
				logger.debug("Comparing lema='{}', easy='{}'", lema.getLema(), easy.getEasy());

				String lemaPhrase = createPhraseToCompare(lemaWordList, easy.getSize(), index);
				logger.debug("Phrases to compare lemaPhrase='{}', easy='{}'", lemaPhrase, easy.getEasy());
				if (compareStrings(lemaPhrase, easy.getEasy()) == 0) {
					logger.info("### EASY ### - lemaPhrase='{}', easy='{}'", lemaPhrase, easy.getEasy());
					addMatchedEasy(lemaWordList, index, easy.getSize() + index);
					setIgnoreMatchedItem(lemaWordList, index, easy.getSize() + index);
					shouldBreak = true;
				}
			} else {
				logger.debug("Breaking the loop, passed the alphabetical order lema='{}', easy='{}' ", lema.getLema(), easy.getEasy());
				shouldBreak = true;
			}

			if (shouldBreak)
				break;
		}

		long end = System.currentTimeMillis();
		logger.debug("Load and process EasyWord - elapsed_time={}", end - start);
	}

	/**
	 * Check if Lema has not been processed yet and if it is not a Punctuation and
	 * it is not blank
	 * 
	 * @param lema LemaWord
	 * @return true if should be analyzed
	 */
	private boolean isAvailableWord(LemaWord lema) {
		return !lema.isNewLine() && !PUNCTUATION.equals(lema.getPosition()) && StringUtils.isNotBlank(lema.getLema());
	}

	/**
	 * Creates a lema phrase to compare with term phrase. The phrase will have
	 * size={@code phraseSize}, starting at position={@code index} in
	 * {@code lemaWordList}
	 * 
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
	 * 
	 * @param lemaWordList
	 * @param from
	 * @param to
	 */
	private void setIgnoreMatchedItem(List<LemaWord> lemaWordList, final int from, final int to) {
		for (int position = from; position < to; position++) {
			lemaWordList.get(position).setIgnore(true);
		}
	}

	private void addNewItem(final WordDTO dto, final int index) {
		dto.setIndex(index);
		dtoList.add(dto);
	}

	private void addNotWordItem(LemaWord lemaWord, final int index) {
		WordDTO dto = new WordDTO();

		dto.setPalavra(lemaWord.getPalavra());
		dto.setLema(lemaWord.getLema());

		dto.setNewline(lemaWord.isNewLine());
		dto.setPunctuation(isPunctuation(lemaWord));
		dto.setContraction(isContraction(lemaWord));

		addNewItem(dto, index);
	}

	private void addNotMatchedItem(LemaWord lemaWord, final int index) {
		WordDTO dto = new WordDTO();

		dto.setPalavra(lemaWord.getPalavra());
		dto.setLema(lemaWord.getLema());

		dto.setPunctuation(isPunctuation(lemaWord));
		dto.setContraction(isContraction(lemaWord));

		addNewItem(dto, index);
	}

	private void addMatchedTerm(List<LemaWord> lemaWordList, String suggestion, int from, int to) {
		int position = 1;
		for (int index = from; index < to; index++) {
			LemaWord lemaWord = lemaWordList.get(index);
			WordDTO dto = new WordDTO();
			dto.setPalavra(lemaWord.getPalavra());
			dto.setLema(lemaWord.getLema());
			dto.setTerm(true);
			if (position == 1)
				dto.setSuggestions(Arrays.asList(suggestion));
			dto.setPosition(position);
			position++;

			dto.setPunctuation(isPunctuation(lemaWord));
			dto.setContraction(isContraction(lemaWord));

			addNewItem(dto, index);
		}
	}

	private void addMatchedDictionary(List<LemaWord> lemaWordList, List<String> suggestions, int from, int to) {
		int position = 1;
		for (int index = from; index < to; index++) {
			LemaWord lemaWord = lemaWordList.get(index);
			WordDTO dto = new WordDTO();
			dto.setPalavra(lemaWord.getPalavra());
			dto.setLema(lemaWord.getLema());
			dto.setComplex(true);
			if (position == 1)
				dto.setSuggestions(suggestions);
			dto.setPosition(position);
			position++;

			dto.setPunctuation(isPunctuation(lemaWord));
			dto.setContraction(isContraction(lemaWord));

			addNewItem(dto, index);
		}
	}

	private void addMatchedFilter(List<LemaWord> lemaWordList, final int index) {
		LemaWord lemaWord = lemaWordList.get(index);
		WordDTO dto = new WordDTO();
		dto.setPalavra(lemaWord.getPalavra());
		dto.setLema(lemaWord.getLema());
		dto.setFilter(true);

		dto.setPunctuation(isPunctuation(lemaWord));
		dto.setContraction(isContraction(lemaWord));

		addNewItem(dto, index);
	}

	private void addMatchedEasy(List<LemaWord> lemaWordList, int from, int to) {
		int position = 1;
		for (int index = from; index < to; index++) {
			LemaWord lemaWord = lemaWordList.get(index);
			WordDTO dto = new WordDTO();
			dto.setPalavra(lemaWord.getPalavra());
			dto.setLema(lemaWord.getLema());
			dto.setEasy(true);
			dto.setPosition(position);
			position++;

			dto.setPunctuation(isPunctuation(lemaWord));
			dto.setContraction(isContraction(lemaWord));

			addNewItem(dto, index);
		}
	}

	private boolean isContraction(LemaWord lemaWord) {
		return StringUtils.isNotBlank(lemaWord.getContraction()) && CONTRACTION.equals(lemaWord.getContraction());
	}

	private boolean isPunctuation(LemaWord lemaWord) {
		return StringUtils.isNotBlank(lemaWord.getPosition()) && PUNCTUATION.equals(lemaWord.getPosition());
	}

}
