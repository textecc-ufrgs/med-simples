package com.tonyleiva.ufrgs.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TextFileServiceTest {

	private PassportFileService passportFileService = new PassportFileService();

	@Test
	public void testCreateTextFile() {
		String filename = "testCreateTextFile.txt";
		String fileContent = "Doença progressiva. Parkinson é uma doença progressiva do sistema neurológico que afeta principalmente o cérebro. Este é um dos principais e mais comuns distúrbios nervosos da terceira idade e é caracterizado, principalmente, por prejudicar a coordenação motora e provocar tremores e dificuldades para caminhar e se movimentar. \r\n" + 
				"\r\n" + 
				"Não há formas de se prevenir do Parkinson a doença progressiva";

		assertTrue(passportFileService.createTextFile(filename, fileContent));
		assertTrue(passportFileService.existTextFile(filename));
		assertTrue(passportFileService.deleteTextFile(filename));
		assertFalse(passportFileService.existTextFile(filename));
	}

}
