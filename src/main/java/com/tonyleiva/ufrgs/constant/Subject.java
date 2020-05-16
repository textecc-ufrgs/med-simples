package com.tonyleiva.ufrgs.constant;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

public enum Subject {

	PARKINSON;

	private static boolean contains(String theme) {
		for (Subject t : Subject.values()) {
			if (t.name().equals(theme)) {
				return true;
			}
		}
		return false;
	}

	public static Subject getSubject(String subject) {
		subject = trimToEmpty(subject).toUpperCase();
		return contains(subject) ? valueOf(subject) : PARKINSON;
	}
}
