package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.ArchTweet;

public interface TweetDao {
	
	public List<ArchTweet> getArchTweets();
	
	public void refreshTweets();
	
	public void tweetLoader();

	void insertTweet(ArchTweet archTweet);

	List<ArchTweet> searchTweets(String query);
	
}
