package com.tonyleiva.ufrgs.util;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class UtilityClass {

	public static final Locale localePtBR = new Locale("pt", "BR");

	public static Collator getCollator() {
		Collator collator = Collator.getInstance(localePtBR);
		collator.setStrength(Collator.PRIMARY);
		return collator;
	}
	
	public static void sort(List<String> collection) {
		Collections.sort(collection, getCollator());
	}

	/**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.
     * <p>
     * This implementation merely returns
     *  <code> compare((String)o1, (String)o2) </code>.
     *
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     * @exception ClassCastException the arguments cannot be cast to Strings.
     * @see java.util.Comparator
     * @since   1.2
     */
	public static int compareFirstLetter(String source, String target) {
		return getCollator().compare(Character.toString(source.charAt(0)), Character.toString(target.charAt(0)));
	}
}
