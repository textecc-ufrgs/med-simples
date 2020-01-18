package com.tonyleiva.ufrgs.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tonyleiva.ufrgs.model.LemaWord;
import com.tonyleiva.ufrgs.passport.PassportService;

@Service
public class MedSimplesService {

	@Autowired
	TextFileService textFileService;

	@Autowired
	PassportService passportService;

	//TODO delete
	public static void main(String[] args) {
		TextFileService t = new TextFileService();
		t.createTextFile("mainTest.txt", "//		try (OutputStreamWriter writer =\r\n" + 
				"//	             new OutputStreamWriter(new FileOutputStream(\"fileName\"), StandardCharsets.UTF_8)) {\r\n" + 
				"//			writer.write(fileContent);\r\n" + 
				"//			// TODO	do stuff\r\n" + 
				"//		} catch (FileNotFoundException e) {\r\n" + 
				"//			// TODO Auto-generated catch block\r\n" + 
				"//			e.printStackTrace();\r\n" + 
				"//		} catch (IOException e) {\r\n" + 
				"//			// TODO Auto-generated catch block\r\n" + 
				"//			e.printStackTrace();\r\n" + 
				"//		} finally {\r\n" + 
				"//			writer.close();\r\n" + 
				"//		}");
	}

	public List<LemaWord> process(String filename, String text) throws Exception {
		List<LemaWord> lemaWordList = new ArrayList<>();
		if (textFileService.createTextFile(filename, text)) {
			lemaWordList = passportService.getLemas(filename);
		} else {
			throw new Exception("Erro na criação do arquivo");
		}
		return lemaWordList;
	}
}
