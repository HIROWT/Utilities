package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.ArchTweet;

public interface TweetDao {
	
	public void insertTweet(String text, String id);
	
	public List<ArchTweet> getArchTweets();
	
	public void refreshTweets();
}
