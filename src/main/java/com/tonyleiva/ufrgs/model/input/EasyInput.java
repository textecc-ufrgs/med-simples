package com.tonyleiva.ufrgs.model.input;

public class EasyInput {

	private int size;
	private String easy;

	public EasyInput() {
		super();
	}

	public EasyInput(int size, String easy) {
		super();
		this.size = size;
		this.easy = easy;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getEasy() {
		return easy;
	}

	public void setEasy(String easy) {
		this.easy = easy;
	}

}
