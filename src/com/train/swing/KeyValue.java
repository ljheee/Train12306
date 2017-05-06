package com.train.swing;
/**
 * key value 对象
 * @author lijintao
 *
 */
public class KeyValue {
	private String name;
	private String value;

	public KeyValue() {
		super();
	}

	/**
	 * @param name
	 * @param value
	 */
	public KeyValue(String name, String value) {
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
