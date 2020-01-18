package com.tonyleiva.ufrgs.service;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.PASSPORT_FILES;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class TextFileService {

	//TODO delete
	public static void main(String[] args) {
		TextFileService p = new TextFileService();
		p.createTextFile("newFile.txt", "Doença progressiva. Parkinson é uma doença progressiva do sistema neurológico que afeta principalmente o cérebro. Este é um dos principais e mais comuns distúrbios nervosos da terceira idade e é caracterizado, principalmente, por prejudicar a coordenação motora e provocar tremores e dificuldades para caminhar e se movimentar. \r\n" + 
				"\r\n" + 
				"Não há formas de se prevenir do Parkinson a doença progressiva");
	}

	public boolean createTextFile(String filename, String fileContent) {
		boolean fileCreated = false;
		Path file = Paths.get(PASSPORT_FILES, filename);
		try {
			Files.writeString(file, fileContent, StandardCharsets.ISO_8859_1);
			fileCreated = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileCreated;
	}

}