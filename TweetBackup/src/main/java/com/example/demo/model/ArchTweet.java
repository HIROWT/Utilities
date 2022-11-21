package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArchTweet {
	public String tweetText;
	public String tweetId;
	
	public ArchTweet(@JsonProperty("text") String tweetText, @JsonProperty("id")String tweetId) {
		this.tweetId = tweetId;
		this.tweetText = tweetText;
	}

	public String getTweetText() {
		return tweetText;
	}
	
	public String getTweetId() {
		return tweetId;
	}
	
	
}
