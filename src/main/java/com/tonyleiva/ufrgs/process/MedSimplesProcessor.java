package com.tonyleiva.ufrgs.process;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.PASSPORT_FILES_PATH;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tonyleiva.ufrgs.model.LemaWord;
import com.tonyleiva.ufrgs.service.PassportJarService;
import com.tonyleiva.ufrgs.service.PassportFileService;

@Service
public class MedSimplesProcessor {

	@Autowired
	PassportFileService passportFileService;

	@Autowired
	PassportJarService passportJarService;

	public List<LemaWord> process(String filename, String text) throws Exception {
		List<LemaWord> lemaWordList;
		//create file
		if (passportFileService.createTextFile(filename, text)) {
			//get Lemas into list
			lemaWordList = passportJarService.getLemas(filename);

			//delete file used by passportService
			if (passportFileService.existTextFile(filename))
				passportFileService.deleteTextFile(filename);

			//find in Terms
			//find in EasyWords
			//find in SimpleDic
			//find in (Sinonimos)
	        //find in (Exemplos)
			
		} else {
			throw new Exception("Erro na criação do arquivo");
		}

		return lemaWordList;
	}

}
