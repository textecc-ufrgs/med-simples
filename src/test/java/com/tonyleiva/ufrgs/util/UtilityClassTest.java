package com.tonyleiva.ufrgs.util;

import static com.tonyleiva.ufrgs.util.UtilityClass.compareStrings;
import static com.tonyleiva.ufrgs.util.UtilityClass.initialLetterEqualTo;
import static com.tonyleiva.ufrgs.util.UtilityClass.initialLetterIsLessThan;
import static com.tonyleiva.ufrgs.util.UtilityClass.localePtBR;
import static com.tonyleiva.ufrgs.util.UtilityClass.sort;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UtilityClassTest {

	@Test
	public void sortTest() {
		List<String> collection = new ArrayList<>();
		collection.add("EXACERBADO");
		collection.add("olfato");
		collection.add("decorrência");
		collection.add("DECORRER");

		sort(collection);
		assertTrue(collection.get(0).equals("decorrência"));
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
	public void compareStringsTest() {
		String firstword = "firstword";
		String entao = "então";

		assertTrue(compareStrings("FIRSTWORD", firstword) == 0);
		assertTrue(compareStrings("firstword", firstword.toUpperCase(localePtBR)) == 0);
		assertTrue(compareStrings("firstWord", firstword.toLowerCase(localePtBR)) == 0);
		assertTrue(compareStrings("firstWord", String.valueOf(firstword.toCharArray())) == 0);
		assertTrue(compareStrings("Então", entao) == 0);
		assertTrue(compareStrings("ENTÃO", entao) == 0);
	}

}
