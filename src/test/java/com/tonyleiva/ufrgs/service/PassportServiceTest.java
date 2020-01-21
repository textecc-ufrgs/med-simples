package com.tonyleiva.ufrgs.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.tonyleiva.ufrgs.model.LemaWord;

public class PassportServiceTest {

	@Test
	public void testGetLemas() {
		PassportJarService p = new PassportJarService();
		List<LemaWord> lemaWordList = p.getLemas("newFile.txt");

		assertTrue(lemaWordList.size() == 72);
	}

}
