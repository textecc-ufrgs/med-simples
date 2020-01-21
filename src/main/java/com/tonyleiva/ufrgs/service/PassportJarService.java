package com.tonyleiva.ufrgs.service;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.PASSPORT_PATH;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tonyleiva.ufrgs.model.LemaWord;

@Service
public class PassportJarService {

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
		List<String> passportOutput = new ArrayList<>();
		try {
			String command = "java -jar passport.jar passport.config files/" + filename + " FORM;LEMMA;UPOS ";
			File dir = new File(PASSPORT_PATH);
			Process p = Runtime.getRuntime().exec(command, null, dir);
			p.waitFor();

			// Grab output and print to display
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(p.getInputStream(), StandardCharsets.ISO_8859_1));
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("##"))
					passportOutput.add(line);
			}
			p.destroy();
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return passportOutput;
	}

	private List<LemaWord> transformToLemaWordList(List<String> stringList) {
		List<LemaWord> lemaWordList = new ArrayList<>();
		int index = 0;

		for (String passportLine : stringList) {
			lemaWordList.add(new LemaWord(passportLine, index++));
		}

		return lemaWordList;
	}
}
