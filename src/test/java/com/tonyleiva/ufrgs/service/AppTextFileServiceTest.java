package com.tonyleiva.ufrgs.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;

public class AppTextFileServiceTest {

	AppTextFileService appTextFileService = new AppTextFileService();

	@Test
	public void openAppFileTest() {
		List<String> contentLines = appTextFileService.openAppFile("easy_words.txt");
		assertTrue(contentLines != null);
		assertFalse(contentLines.isEmpty());
	}

	@Test
	public void loadEasyWordsTest() {
		Set<String> easyWordsSet = appTextFileService.loadEasyWords();
		assertTrue(easyWordsSet != null);
		assertFalse(easyWordsSet.isEmpty());
	}

}
