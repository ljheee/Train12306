package com.train.swing;

public class KeyValuePair {
	private String name;
	private String value;

	public KeyValuePair() {
		super();
	}

	/**
	 * @param name
	 * @param value
	 */
	public KeyValuePair(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
