package com.tonyleiva.ufrgs.process;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tonyleiva.ufrgs.model.LemaWord;
import com.tonyleiva.ufrgs.service.AppTextFileService;
import com.tonyleiva.ufrgs.service.PassportFileService;
import com.tonyleiva.ufrgs.service.PassportJarService;

@Service
public class MedSimplesProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MedSimplesProcessor.class);

	@Autowired
	private PassportFileService passportFileService;

	@Autowired
	private PassportJarService passportJarService;

	@Autowired
	private AppTextFileService textFileService;

	public List<LemaWord> process(String filename, String text) throws Exception {
		logger.info("Start processing");

		List<LemaWord> lemaWordList;
		//create file
		if (passportFileService.createTextFile(filename, text)) {
			//get Lemas into list
			lemaWordList = passportJarService.getLemas(filename);

			//delete file used by passportService
			if (passportFileService.existTextFile(filename))
				passportFileService.deleteTextFile(filename);

			processLists(lemaWordList);
			
		} else {
			throw new Exception("Erro na criação do arquivo");
		}

		return lemaWordList;
	}

	//TODO load all lists and then process hover lemaWordList
	private void processLists(List<LemaWord> lemaWordList) {
		List<String> termsContent = textFileService.loadTerms();
		logger.debug("Terms file lines={}", termsContent.size());
		//find in Terms
		//find in EasyWords
		//find in SimpleDic
		//find in (Sinonimos)
        //find in (Exemplos)
	}
	
/*
ListIterator<String> it = al.listIterator();
System.out.println("Forward direction:-");
while(it.hasNext())
    System.out.println(it.next());
System.out.println("\nBackward direction:-");
while(it.hasPrevious())
	System.out.println(it.previous());
*/
}
