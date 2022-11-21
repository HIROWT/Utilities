package com.example.demo.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {
	private final UUID ID;
	private final String name;
	
	public Person(@JsonProperty("id") UUID iD, @JsonProperty("name")String name) {
		System.out.println("Posted " + name);
		ID = iD;
		this.name = name;
	}

	public UUID getID() {
		return ID;
	}

	public String getName() {
		return name;
	}


	
}
