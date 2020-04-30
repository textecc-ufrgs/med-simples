package com.tonyleiva.ufrgs.model.input;

public class TermInput {

	private int size;
	private String term;
	private String definition;

	public TermInput() {
		super();
	}

	public TermInput(int size, String term, String definition) {
		super();
		this.size = size;
		this.term = term;
		this.definition = definition;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	@Override
	public String toString() {
		return "TermInput [term=" + term + ", size=" + size + ", definition=" + definition + "]";
	}
}
