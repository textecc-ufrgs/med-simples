package com.tonyleiva.ufrgs.util;

import static com.tonyleiva.ufrgs.util.ComparatorUtils.compareStrings;
import static com.tonyleiva.ufrgs.util.ComparatorUtils.initialLetterEqualTo;
import static com.tonyleiva.ufrgs.util.ComparatorUtils.initialLetterIsLessThan;
import static com.tonyleiva.ufrgs.util.ComparatorUtils.localePtBR;
import static com.tonyleiva.ufrgs.util.ComparatorUtils.sort;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ComparatorUtilsTest {

	@Test
	public void sortTest() {
		List<String> collection = new ArrayList<>();
		collection.add("EXACERBADO");
		collection.add("olfato");
		collection.add("decorrência");
		collection.add("DECORRER");

		sort(collection);
		assertTrue(collection.get(0).equals("DECORRER"));
	}

	@Test
	public void sortSimilar() {
		List<String> collection = new ArrayList<>();
		collection.add("moco");
		collection.add("mocó");
		collection.add("maca");
		collection.add("maça");
		collection.add("mocô");
		collection.add("moço");
		collection.add("moçó");
		collection.add("maçã");
		
		sort(collection);
		
		assertTrue(collection.get(0).equals("maca"));
		assertTrue(collection.get(1).equals("maça"));
		assertTrue(collection.get(2).equals("maçã"));
		assertTrue(collection.get(3).equals("moco"));
		assertTrue(collection.get(4).equals("mocó"));
		assertTrue(collection.get(5).equals("mocô"));
		assertTrue(collection.get(6).equals("moço"));
		assertTrue(collection.get(7).equals("moçó"));
	}

	@Test
	public void initialLetterEqualToTest() {
		String firstword = "firstword";
		String entao = "então";

		assertTrue(initialLetterEqualTo("FIRSTWORD", firstword));
		assertTrue(initialLetterEqualTo("firstword", firstword.toUpperCase(localePtBR)));
		assertTrue(initialLetterEqualTo("firstWord", firstword.toLowerCase(localePtBR)));
		assertTrue(initialLetterEqualTo("firstWord", String.valueOf(firstword.toCharArray())));
		assertTrue(initialLetterEqualTo("Então", entao));
	}

	@Test
	public void initialLetterIsLessThanTest() {
		assertTrue(initialLetterIsLessThan("FIRSTWORD", "secondWord"));
		assertTrue(initialLetterIsLessThan("firstword", "secondWord"));
		assertTrue(initialLetterIsLessThan("firstWord", "secondWord"));
		assertTrue(initialLetterIsLessThan("firstWord", "SECONDWORD"));
	}

	@Test
	public void compareStringsEqualsTest() {
		String firstword = "firstword";
		String entao = "então";

		assertTrue(compareStrings("FIRSTWORD", firstword) == 0);
		assertTrue(compareStrings("firstword", firstword.toUpperCase(localePtBR)) == 0);
		assertTrue(compareStrings("firstWord", firstword.toLowerCase(localePtBR)) == 0);
		assertTrue(compareStrings("firstWord", String.valueOf(firstword.toCharArray())) == 0);
		assertTrue(compareStrings("decorrência", "DECORRÊNCIA") == 0);
		assertTrue(compareStrings("decorrência", "Decorrência") == 0);
		assertTrue(compareStrings("Então", entao) == 0);
		assertTrue(compareStrings("ENTÃO", entao) == 0);
		assertTrue(compareStrings("SÉ", "Sé") == 0);
		assertTrue(compareStrings("SÉ", "sé") == 0);
		assertTrue(compareStrings("ELÃ", "elã") == 0);
	}

	@Test
	public void compareStringsNotEqualsTest() {
		assertFalse(compareStrings("SÉ", "SE") == 0);
		assertFalse(compareStrings("Ela", "elã") == 0);
	}

}
