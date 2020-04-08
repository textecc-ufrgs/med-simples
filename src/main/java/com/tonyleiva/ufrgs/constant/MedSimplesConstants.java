package com.tonyleiva.ufrgs.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MedSimplesConstants {
	private MedSimplesConstants() {
	}

	public static final String PASSPORT_PATH = "src/main/resources/passport";
	public static final String PASSPORT_FILES_PATH = "src/main/resources/passport/files";
	public static final String APP_FILES_PATH = "src/main/resources/appfiles";
	public static final String FILE_PREFIX = "text-";
	public static final String FILE_FORMAT = ".txt";

	public static final Charset PASSPORT_WRITE_FILE_CHARSET = StandardCharsets.UTF_8;
	public static final Charset PASSPORT_READ_FILE_CHARSET = StandardCharsets.UTF_8;
}
