package com.tonyleiva.ufrgs.model;

import java.util.List;

public class LemaWord {

	private int index;
    private String palavra;
    private String lema;
    private String position;
    private String tag;
    private String morfologia;
    private String outros;
    private String dependencia;
    private String rotulo;
    private String contraction;
    private boolean ignore;
    private List<String> simple;
    private List<String> synonyms;
    private List<String> examples;
    private int key;

	public LemaWord(String passportLine) {
		if (passportLine.equals("")) {
			new LemaWord();
		} else {
			String[] passportLineArray = passportLine.split("\\t");
			this.setPalavra(passportLineArray[1]);
			this.setLema(passportLineArray[2]);
		}
	}

	private LemaWord() {
		this.setPalavra("");
		this.setLema("");
	}

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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getMorfologia() {
		return morfologia;
	}

	public void setMorfologia(String morfologia) {
		this.morfologia = morfologia;
	}

	public String getOutros() {
		return outros;
	}

	public void setOutros(String outros) {
		this.outros = outros;
	}

	public String getDependencia() {
		return dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public String getContraction() {
		return contraction;
	}

	public void setContraction(String contraction) {
		this.contraction = contraction;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public List<String> getSimple() {
		return simple;
	}

	public void setSimple(List<String> simple) {
		this.simple = simple;
	}

	public List<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}

	public List<String> getExamples() {
		return examples;
	}

	public void setExamples(List<String> examples) {
		this.examples = examples;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "LemaWord [index=" + index + ", palavra=" + palavra + ", lema=" + lema + "]";
	}

}
