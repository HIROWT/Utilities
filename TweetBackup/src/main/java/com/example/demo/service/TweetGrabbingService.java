package com.example.demo.service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.model.ArchTweet;
import com.example.demo.dao.TweetDao;

@Service
public class TweetGrabbingService {
	TweetDao tweetDao;
	
	@Autowired
	public TweetGrabbingService(@Qualifier("text")TweetDao tweetDao) {
		this.tweetDao = tweetDao;
		tweetDao.refreshTweets();
		refreshTweets();
	}

	public List<ArchTweet> getArchTweets() {
		int size = tweetDao.getArchTweets().size();
		List<ArchTweet> reverseTweets = new ArrayList<ArchTweet>();
		
		for(int i = size - 1; i >= 0; i-- ) {
			reverseTweets.add(tweetDao.getArchTweets().get(i));
		}
		//return reverse list
		
		return reverseTweets;
	}
	
	public void refreshTweets() {	
		Runnable tRefresh = () -> this.tweetDao.refreshTweets();
		System.out.println("refreshing tweets");
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(tRefresh, 0, 60, TimeUnit.SECONDS);	
		
	}
	
	public void insertTweet(ArchTweet archTweet) {
		this.tweetDao.insertTweet(archTweet);
	}
	
	public List<ArchTweet> searchTweets(String query){
		return this.tweetDao.searchTweets(query);
	}
	
	
}
