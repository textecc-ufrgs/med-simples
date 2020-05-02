package com.tonyleiva.ufrgs.process.output;

import java.util.List;

public class WordDTO {

	private int index;
	private String palavra;
	private String lema;
	private boolean term;
	private boolean complex;
	private boolean filter;
	private boolean easy;
	private List<String> suggestions;
	private int position;
	private boolean punctuation;
	private boolean contraction;
	private boolean newline;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getPalavra() {
		return palavra;
	}

	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}

	public String getLema() {
		return lema;
	}

	public void setLema(String lema) {
		this.lema = lema;
	}

	public boolean isTerm() {
		return term;
	}

	public void setTerm(boolean term) {
		this.term = term;
	}

	public boolean isComplex() {
		return complex;
	}

	public void setComplex(boolean complex) {
		this.complex = complex;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public boolean isEasy() {
		return easy;
	}

	public void setEasy(boolean easy) {
		this.easy = easy;
	}

	public List<String> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<String> suggestions) {
		this.suggestions = suggestions;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isPunctuation() {
		return punctuation;
	}

	public void setPunctuation(boolean punctuation) {
		this.punctuation = punctuation;
	}

	public boolean isContraction() {
		return contraction;
	}

	public void setContraction(boolean contraction) {
		this.contraction = contraction;
	}

	public boolean isNewline() {
		return newline;
	}

	public void setNewline(boolean newline) {
		this.newline = newline;
	}
	
}
