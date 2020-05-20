package com.tonyleiva.ufrgs.process.output;

import java.io.Serializable;
import java.util.List;

public class SimplifyDTO implements Serializable {

	private static final long serialVersionUID = 8184911638052853610L;

	private int loadFiles;
	private int parser;
	private int processLists;
	private List<WordDTO> wordDtoList;

	public int getLoadFiles() {
		return loadFiles;
	}

	public void setLoadFiles(int loadFiles) {
		this.loadFiles = loadFiles;
	}

	public int getParser() {
		return parser;
	}

	public void setParser(int parser) {
		this.parser = parser;
	}

	public int getProcessLists() {
		return processLists;
	}

	public void setProcessLists(int processLists) {
		this.processLists = processLists;
	}

	public List<WordDTO> getWordDtoList() {
		return wordDtoList;
	}

	public void setWordDtoList(List<WordDTO> wordDtoList) {
		this.wordDtoList = wordDtoList;
	}

}
