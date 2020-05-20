package com.tonyleiva.ufrgs.controller;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.FILE_FORMAT;
import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.FILE_PREFIX;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tonyleiva.ufrgs.constant.ReaderType;
import com.tonyleiva.ufrgs.constant.Subject;
import com.tonyleiva.ufrgs.process.MedSimplesProcessor;
import com.tonyleiva.ufrgs.process.output.SimplifyDTO;
import com.tonyleiva.ufrgs.process.output.WordDTO;

@RestController
@RequestMapping(value = "/med-simples")
public class MedSimplesController {

	private static final Logger logger = LoggerFactory.getLogger(MedSimplesController.class);

	@Autowired
	private MedSimplesProcessor medSimplesProcessor;

	@Autowired
	private ObjectMapper objectMapper;

	@CrossOrigin
	@PostMapping(value = "/simplify", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> simplify(
			@RequestHeader(value = "Content-Type") String contentType,
			@RequestHeader(value = "Subject", required = false) String subjectHeader,
			@RequestHeader(value = "Reader", required = false) String readerHeader, 
			@RequestBody String textBody) {
		logger.info("Novo texto a ser analisado - text=\n'{}'", textBody);

		ResponseEntity<String> response;
		String filename = FILE_PREFIX + String.valueOf(System.currentTimeMillis()) + FILE_FORMAT;

		try {
			Subject subject = Subject.getSubject(subjectHeader);
			ReaderType reader = ReaderType.getReaderType(readerHeader);
			SimplifyDTO simplifyDto = medSimplesProcessor.process(filename, textBody, subject, reader);
			response = ResponseEntity.ok().body(serialize(simplifyDto.getWordDtoList()));
		} catch (Exception e) {
			response = ResponseEntity.status(500).body(e.getMessage());
		}

		return response;
	}

	private String serialize(List<WordDTO> wordDTOList) throws JsonProcessingException {
		return objectMapper.writeValueAsString(wordDTOList);
	}
}