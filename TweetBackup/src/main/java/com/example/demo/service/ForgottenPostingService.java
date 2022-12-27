package com.example.demo.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.model.ArchTweet;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.example.demo.dao.DBConnection;
import com.example.demo.dao.TweetDao;

@Service
public class ForgottenPostingService {
	TweetDao tweetDao;
	List<ArchTweet> forgottenTweets;
	List<ArchTweet> serialForgottenTweets;
	private TwitterApi apiInstance;
	
	@Autowired
	public ForgottenPostingService(@Qualifier("text")TweetDao tweetDao) {
		//make this thread safe later...
	
		this.apiInstance = new TwitterApi(new TwitterCredentialsOAuth2(
				"",
				"",
				"",
				""));
		this.tweetDao = tweetDao;
		this.serialForgottenTweets = new ArrayList<>();
		
		try {
			//dbToArray();
			refSched();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void refSched() {		
		Runnable tRefresh = () -> this.refreshTweets();;
		System.out.println("refreshing forgotten tweets");
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(tRefresh, 1, 60, TimeUnit.MINUTES);
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
	
	public List<ArchTweet> getForgottenTweets() {
		int size = serialForgottenTweets.size();
		List<ArchTweet> reverseTweets = new ArrayList<ArchTweet>();
		
		for(int i = size - 1; i >= 0; i-- ) {
			reverseTweets.add(serialForgottenTweets.get(i));
		}
		//return reverse list
		
		return reverseTweets;
		
		//return forgottenTweets;
	}
	
	public void refreshTweets() {
		System.out.println("Refreshing size: " + tweetDao.getArchTweets().size());
		
		forgottenTweets = new ArrayList<ArchTweet>();
		
		List<ArchTweet> archTweets = this.tweetDao.getArchTweets();
		
		//if API Exception occurs (tweet is no longer available), add to forgotten tweets
		for(int i = 0; i < archTweets.size(); i++) {
			
			try {
					apiInstance.tweets().findTweetById(archTweets.get(i).getTweetId()).execute().getData().getAuthorId();    
						//System.out.println("Not match" + archTweets.get(i).getTweetId());
				} catch (Exception e) {
					//System.out.println(archTweets.get(i).getTweetId());
					//e.printStackTrace();
					forgottenTweets.add(archTweets.get(i));
				}
		}
		
		
		
		//loop through saved forgotten tweets, if not inside, add, serialize, post
		
		/*
		for(int i = 0; i < forgottenTweets.size(); i++) {
			//System.out.println("Comparing lists");
			
			boolean isIn = false;
			
			for(int j = 0; j < serialForgottenTweets.size(); j++) {
				if(forgottenTweets.get(i).getTweetId().equals((serialForgottenTweets.get(j).getTweetId())) ) {
					isIn = true;
					break;
				}
			}
			//check if updated list == old list. If elements aren't present, update old list, post tweet
			
			if(!isIn) {
				//System.out.println("Found forgotten tweet!");
				serialForgottenTweets.add(forgottenTweets.get(i));
				try {
					updateDB(forgottenTweets.get(i));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		*/
		
		this.serialForgottenTweets = forgottenTweets;
		System.out.println("Done refrehing");
	}

	public void updateDB(ArchTweet archTweet) throws Exception {
		DBConnection dbc = new DBConnection();
		PreparedStatement stmnt;
		stmnt = dbc.getConnection().prepareStatement("Insert into ForgottenTweets Values (?,?,?,?)");
		
		stmnt.setString(1, archTweet.tweetText);
		stmnt.setString(2, archTweet.tweetId);
		stmnt.setString(3, archTweet.createdAt);
		stmnt.setString(4, archTweet.quotedTweet);
		
		
		stmnt.executeUpdate();
	}
	
	public void dbToArray() throws Exception {
		DBConnection dbc = new DBConnection();
		PreparedStatement stmnt;
		String query = "select * from ForgottenTweets";
		
		Connection connection = dbc.getConnection();
		stmnt = connection.prepareStatement(query);
		ResultSet resultSet = stmnt.executeQuery();
		
		while(resultSet.next()) {
			ArchTweet tba = new ArchTweet(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
			this.serialForgottenTweets.add(tba);
		}
		
	}
	
	
}
