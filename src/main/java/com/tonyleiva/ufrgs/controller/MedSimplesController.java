package com.tonyleiva.ufrgs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tonyleiva.ufrgs.model.LemaWord;
import com.tonyleiva.ufrgs.service.MedSimplesService;
import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.FILE_PREFIX;

import java.util.List;

import static com.tonyleiva.ufrgs.constant.MedSimplesConstants.FILE_FORMAT;
@RestController
@RequestMapping(value = "/med-simples")
public class MedSimplesController {

	@Autowired
	MedSimplesService medSimplesService;

	@GetMapping(value = "/simplify", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> simplify(@RequestHeader(value = "Accept-Language") String acceptLanguage,
			@RequestBody String textBody) {
		ResponseEntity<String> response;
		String filename = FILE_PREFIX + String.valueOf(System.currentTimeMillis()) + FILE_FORMAT;

		try {
			List<LemaWord> lemaWordList = medSimplesService.process(filename, textBody);
			response = ResponseEntity.ok().body(textBody);
		} catch (Exception e) {
			response = ResponseEntity.status(500).body(e.getMessage());
		}
		
		return response;
	}

}