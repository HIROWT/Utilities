package com.example.demo.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ArchTweet;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Tweet;

@Repository("text")
public class TweetToTXT implements TweetDao {
	ArrayList<ArchTweet> archTweets = new ArrayList<>();
	//in the future this arraylist will be used solely for recent tweets. The other tweets will be only stored in txt file
	//get mapping that returns and arraylist, but it compiles ALL tweets prior.
	TwitterApi apiInstance; 
	File idFile; //file with IDs
	File twFile; //file with Tweets
	String tUserName;
	String tUserId;
	boolean initiated = true;
	
    
	@Autowired
	public TweetToTXT() {
		
		this.apiInstance = new TwitterApi(new TwitterCredentialsOAuth2(
				"",
				"",
				"",
				""));
		this.twFile = new File("TweetBackups.txt");;
		this.tUserName = "";
		try {
			this.tUserId = apiInstance.users().findUserByUsername(tUserName).execute().getData().getId();
		} catch (ApiException e) {
			System.out.println("Couldn't set username");
			//e.printStackTrace();
		}
		
		if(initiated) {
			//txtToArray();
			try {
				dbToArray();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		//TODO 
		//method to get tweet objects from the DB (text file) and put into archTweets
	}
	
	public void dbToArray() throws Exception {
		DBConnection dbc = new DBConnection();
		PreparedStatement stmnt;
		String query = "select * from Tweets";
		
		Connection connection = dbc.getConnection();
		stmnt = connection.prepareStatement(query);
		ResultSet resultSet = stmnt.executeQuery();
		
		while(resultSet.next()) {
			ArchTweet tba = new ArchTweet(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
			this.archTweets.add(tba);
		}
		
		this.initiated = false;
		
	}
	
	
	public void updateDB(ArchTweet archTweet) throws Exception {
		DBConnection dbc = new DBConnection();
		PreparedStatement stmnt;
		stmnt = dbc.getConnection().prepareStatement("Insert into Tweets Values (?,?,?,?)");
		
		stmnt.setString(1, archTweet.tweetText);
		stmnt.setString(2, archTweet.tweetId);
		stmnt.setString(3, archTweet.createdAt);
		stmnt.setString(4, archTweet.quotedTweet);
		
		
		stmnt.executeUpdate();
	}

	

	@Override
	public void insertTweet(ArchTweet archTweet) {
		try {
			updateDB(archTweet);
			dbToArray();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ArchTweet> getArchTweets() {
		// TODO Auto-generated method stub
		return archTweets;
	}
	
	@Override
	public void refreshTweets() {
		System.out.println("Beginning check @ " + LocalTime.now().getMinute() + " " +  LocalTime.now().getSecond());
		if(initiated) {
			try {
				dbToArray();
			}catch(Exception e){
				System.out.println("Couldn't initialize!");
			}
		} 
		//check txt for tweet id etc. if not present write to txt, write to archTweets
		try {
			Set<String> tweetParams = new HashSet<>();
			tweetParams.add("text"); tweetParams.add("created_at"); tweetParams.add("referenced_tweets");
			//set parameters for tweet
			Tweet rec = apiInstance.tweets().usersIdTweets(tUserId).tweetFields(tweetParams).execute().getData().get(0);
			//Retrieve tweet
			//getting ID of most recent tweet
			String recentID = rec.getId();
			//checking if it's already been archived
			if(!listHas(recentID)){
				
				if(isRefTweet(rec)) {
					String refId = rec.getReferencedTweets().get(0).getId();
					Tweet ref = apiInstance.tweets().findTweetById(refId).tweetFields(tweetParams).execute().getData();
					
					ref.getText();
					//add referenced tweet to recent tweet
					archTweets.add(new ArchTweet(rec.getText(), rec.getId(), rec.getCreatedAt(), ref.getText()));
					updateDB(archTweets.get(archTweets.size() - 1));
				}else {
					archTweets.add(new ArchTweet(rec.getText(), rec.getId(), rec.getCreatedAt()));
					updateDB(archTweets.get(archTweets.size() - 1));
				}
				
				System.out.println("Copying over " + recentID + " " + rec.getId() );
				
			}
			
	    }catch(Exception e) {
	    	e.printStackTrace();
	    	System.out.println("Trouble refreshing tweet");
	   	}
	    //serialize here	
		
	}
			
	
	public boolean listHas(String iD) {
		for(int i = 0; i< archTweets.size(); i++) {
			if(archTweets.get(i).tweetId.equals(iD) ) {
				//System.out.println("Recent is duplicate!");
				return true;
			}
		}
		
		return false;
	}
	
	public void purgeDuplicates() {
		HashSet<ArchTweet> remDuplicates = new HashSet<>();
		
		for(int i = 0; i< archTweets.size(); i++) {
			remDuplicates.add(archTweets.get(i));
		}
		
		this.archTweets = new ArrayList<>(remDuplicates);
		Collections.reverse(archTweets);
		
		//updateSerial();
	}
	
	
	public boolean isRefTweet(Tweet tweet) {
		if(tweet.getReferencedTweets() != null) {
			if(tweet.getReferencedTweets().get(0).getType().getValue() != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void tweetLoader() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<ArchTweet> searchTweets(String query){
		List<ArchTweet> tweets = new ArrayList<>();
		for(int i = 0; i < this.archTweets.size(); i++) {
			if(archTweets.get(i).tweetText.indexOf(query) != -1) {
				tweets.add(archTweets.get(i));
			}
		}
		
		return tweets;
	}
	
	
}
