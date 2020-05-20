package com.tonyleiva.ufrgs.constant;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

public enum ReaderType {

	FUNDAMENTAL, MEDIO;

	private static boolean contains(String reader) {
		for (ReaderType r : ReaderType.values()) {
			if (r.name().equals(reader)) {
				return true;
			}
		}
		return false;
	}

	public static ReaderType getReaderType(String reader) {
		reader = trimToEmpty(reader).toUpperCase();
		return contains(reader) ? valueOf(reader) : FUNDAMENTAL;
	}
}
