package com.tonyleiva.ufrgs.model.input;

import java.util.List;

public class DictionaryInput {

	private int size;
	private String complex;
	private List<String> suggestions;

	public DictionaryInput() {
		super();
	}

	public DictionaryInput(int size, String complex, List<String> suggestions) {
		super();
		this.size = size;
		this.complex = complex;
		this.suggestions = suggestions;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getComplex() {
		return complex;
	}

	public void setComplex(String complex) {
		this.complex = complex;
	}

	public List<String> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<String> suggestions) {
		this.suggestions = suggestions;
	}

	@Override
	public String toString() {
		return "DictionaryInput [complex=" + complex + ", size=" + size + 
				", suggestionsSize=" + suggestions.size() + "]";
	}
}
