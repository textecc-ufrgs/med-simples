package com.tonyleiva.ufrgs.service;

import static org.junit.Assert.*;

import org.junit.Test;

public class TextFileServiceTest {

	private TextFileService textFileService = new TextFileService();

	@Test
	public void testCreateTextFile() {
		boolean fileCreated = textFileService.createTextFile("testFile.txt", "Doença progressiva. Parkinson é uma doença progressiva do sistema neurológico que afeta principalmente o cérebro. Este é um dos principais e mais comuns distúrbios nervosos da terceira idade e é caracterizado, principalmente, por prejudicar a coordenação motora e provocar tremores e dificuldades para caminhar e se movimentar. \r\n" + 
				"\r\n" + 
				"Não há formas de se prevenir do Parkinson a doença progressiva");
		assertTrue(fileCreated);
		//TODO delete file created
	}

}
