package com.tonyleiva.ufrgs.service;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.NEW_LINE;
import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.PASSPORT_PATH;
import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.PASSPORT_READ_FILE_CHARSET;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tonyleiva.ufrgs.model.LemaWord;

@Service
public class PassportJarService {

	private static Logger logger = LoggerFactory.getLogger(PassportJarService.class);

	public List<LemaWord> getLemas(String filename) {
		List<String> passportOutput = executePassport(filename);
		return transformToLemaWordList(passportOutput);
	}

	/**
	 * Executes the passport.jar tool over the file with name equals to
	 * {@code filename}
	 * 
	 * @param filename name of the file to be analyzed by passport tool
	 * @return string list containing the output of passport.jar
	 */
	private List<String> executePassport(String filename) {
		long start = System.currentTimeMillis();

		List<String> passportOutput = new ArrayList<>();
		List<String> allLines = new ArrayList<>();
		try {
			String command = "java -jar passport.jar passport.config files/" + filename + " FORM;LEMMA;UPOS ";
			File dir = new File(PASSPORT_PATH);
			Process p = Runtime.getRuntime().exec(command, null, dir);
			p.waitFor();

			// Grab output and print to display
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(p.getInputStream(), PASSPORT_READ_FILE_CHARSET));
			String line;
			boolean newLine = false;
			while ((line = reader.readLine()) != null) {
				if (StringUtils.isNotBlank(line)) {
					allLines.add(line + "\n");
					if (!line.startsWith("##")) {
						if (newLine && line.startsWith("#", 2)) {
							passportOutput.add(NEW_LINE);
						} else {
							passportOutput.add(line);
							newLine = false;
						}
					} else {
						newLine = true;
					}
				}
			}
			logger.info("Output from passport.jar \n {}", allLines);
			p.destroy();
			reader.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			long finish = System.currentTimeMillis();
			logger.info("execute passport elapsed_time={}", finish - start);
		}
		return passportOutput;
	}

	private List<LemaWord> transformToLemaWordList(List<String> stringList) {
		long start = System.currentTimeMillis();

		List<LemaWord> lemaWordList = new ArrayList<>();
		int index = 0;

		for (String passportLine : stringList) {
			lemaWordList.add(new LemaWord(passportLine, index++));
		}

		long finish = System.currentTimeMillis();
		logger.info("transform to LemaWord elapsed_time={}", finish - start);
		return lemaWordList;
	}
}
