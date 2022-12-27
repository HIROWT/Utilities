package com.example.demo.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArchTweet2 implements Serializable{
	public String tweetText;
	public String tweetId;
	public String quotedTweet;
	public String createdAt;
	
	public ArchTweet2(String tweetText, String tweetId, String createdAt, String quotedTweet) {
		this.tweetId = tweetId;
		this.tweetText = tweetText;
		this.quotedTweet = quotedTweet;
		this.createdAt = createdAt;
	}

	public String getTweetText() {
		return tweetText;
	}

	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}

	public String getTweetId() {
		return tweetId;
	}

	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	public String getQuotedTweet() {
		return quotedTweet;
	}

	public void setQuotedTweet(String quotedTweet) {
		this.quotedTweet = quotedTweet;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	
	
	
}
