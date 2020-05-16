package com.tonyleiva.ufrgs.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tonyleiva.ufrgs.model.input.EasyInput;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AppTextFileServiceTest {

	@Autowired
	private AppTextFileService appTextFileService;

	@Test
	public void openAppFileTest() {
		List<String> contentLines = appTextFileService.openAppFile("easy_words.txt");
		assertTrue(contentLines != null);
		assertFalse(contentLines.isEmpty());
	}

	@Test
	public void loadEasyWordsTest() {
		List<EasyInput> easyWordsSet = appTextFileService.loadEasyWordsInput();
		assertTrue(easyWordsSet != null);
		assertFalse(easyWordsSet.isEmpty());
	}

}
