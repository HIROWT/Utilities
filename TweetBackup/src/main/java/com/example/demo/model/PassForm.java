package com.example.demo.model;

public class PassForm {
	private String id;
	private String key;

	public PassForm(String id, String key) {
		this.id = id;
		this.key = key;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
