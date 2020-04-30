package com.tonyleiva.ufrgs.util;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class UtilityClass {

	public static final Locale localePtBR = new Locale("pt", "BR");

	private UtilityClass() {
		throw new IllegalStateException("Utility class");
	}

	private static Collator getCollator() {
		Collator collator = Collator.getInstance(localePtBR);
		collator.setStrength(Collator.IDENTICAL);
		return collator;
	}

	public static void sort(List<String> collection) {
		Collections.sort(collection, getCollator());
	}

	/**
	 * Check if firstWord's initial letter is less than secondWord's initial letter
	 * 
	 * @return true if first argument's first letter is less than the second's first
	 *         letter, false otherwise
	 */
	public static boolean initialLetterIsLessThan(String firstWord, String secondWord) {
		return compareFirstLetter(firstWord, secondWord) < 0;
	}

	/**
	 * Check if firstWord's initial letter is equal to secondWord's initial letter
	 * 
	 * @return true if the initial letter of both arguments are equals, false
	 *         otherwise
	 */
	public static boolean initialLetterEqualTo(String firstWord, String secondWord) {
		return compareFirstLetter(firstWord, secondWord) == 0;
	}

	/**
	 * Compares its two arguments for order. Returns a negative integer, zero, or a
	 * positive integer as the first argument is less than, equal to, or greater
	 * than the second.
	 * <p>
	 * This implementation merely returns
	 * <code> compare((String)o1, (String)o2) </code>.
	 *
	 * @return a negative integer, zero, or a positive integer as the first argument
	 *         is less than, equal to, or greater than the second.
	 * @exception ClassCastException the arguments cannot be cast to Strings.
	 * @see java.util.Comparator
	 * @since 1.2
	 */
	public static int compareStrings(String source, String target) {
		return getCollator().compare(source.toLowerCase(localePtBR), target.toLowerCase(localePtBR));
	}

	/**
	 * Compares its two arguments for order. Returns a negative integer, zero, or a
	 * positive integer as the first argument is less than, equal to, or greater
	 * than the second.
	 * <p>
	 * This implementation merely returns
	 * <code> compare((String)o1, (String)o2) </code>.
	 *
	 * @return a negative integer, zero, or a positive integer as the first argument
	 *         is less than, equal to, or greater than the second.
	 * @exception ClassCastException the arguments cannot be cast to Strings.
	 * @see java.util.Comparator
	 * @since 1.2
	 */
	private static int compareFirstLetter(String source, String target) {
		return getCollator().compare(
				Character.toString(source.toLowerCase(localePtBR).charAt(0)),
				Character.toString(target.toLowerCase(localePtBR).charAt(0)));
	}
}
