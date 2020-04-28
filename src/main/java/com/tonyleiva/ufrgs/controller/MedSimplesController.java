package com.tonyleiva.ufrgs.controller;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.FILE_FORMAT;
import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.FILE_PREFIX;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tonyleiva.ufrgs.process.MedSimplesProcessor;
import com.tonyleiva.ufrgs.process.output.WordDTO;

@RestController
@RequestMapping(value = "/med-simples")
public class MedSimplesController {

	private static final Logger logger = LoggerFactory.getLogger(MedSimplesController.class);

	@Autowired
	private MedSimplesProcessor medSimplesProcessor;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping(value = "/simplify", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> simplify(@RequestHeader(value = "Content-Type") String contentType,
			@RequestBody String textBody) {
		logger.info("Novo texto a ser analisado - text=\n'{}'", textBody);

		ResponseEntity<String> response;
		String filename = FILE_PREFIX + String.valueOf(System.currentTimeMillis()) + FILE_FORMAT;

		try {
			List<WordDTO> dtoList = medSimplesProcessor.process(filename, textBody);
			response = ResponseEntity.ok().body(serialize(dtoList));
		} catch (Exception e) {
			response = ResponseEntity.status(500).body(e.getMessage());
		}

		return response;
	}

	private String serialize(List<WordDTO> wordDTOList) throws JsonProcessingException {
		return objectMapper.writeValueAsString(wordDTOList);
	}
}