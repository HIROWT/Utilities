package com.example.demo.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ArchTweet;
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
	boolean initiated = true;
	
	
	@Autowired
	public TweetToTXT() {
		
		this.apiInstance = new TwitterApi(new TwitterCredentialsOAuth2(
				//API keys here
				"****************",
				"****************",
				"****************",
				"****************"));
		this.idFile = new File("TweetIDs.txt");;
		this.twFile = new File("TweetBackups.txt");;
		this.tUserName = "brutedeforce";
		//name of twitter account
		//TODO 
		//method to get tweet objects from the DB (text file) and put into archTweets
	}
	
	public void txtToArray() {
		
		try {
			Scanner scan = new Scanner(twFile);
			scan = scan.useDelimiter("_T_TWEET_T_");
			
			while(scan.hasNext()) {
				//seperate Text from ID, add to ArchTweets list
				String format = scan.next();
				String[] seperated = format.split("_T_TEXT_T_");
				//System.out.println(seperated[0]);
				this.archTweets.add(new ArchTweet(seperated[1], seperated[0]));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.initiated = false;
	}
	
	
	

	@Override
	public void insertTweet(String text, String id) {
		archTweets.add(new ArchTweet(text, id));
		//code to add a tweet to the file as well?
	}

	@Override
	public List<ArchTweet> getArchTweets() {
		// TODO Auto-generated method stub
		return archTweets;
	}

	@Override
	public void refreshTweets() {
		if(initiated) {
			txtToArray();
		}

		//check txt for tweet id etc. if not present write to txt, write to archTweets
		try {
	    	String userTarg = apiInstance.users().findUserByUsername(tUserName).execute().getData().getId();
			
			//getting ID of most recent tweet
			String recentID = apiInstance.tweets().usersIdTweets(userTarg).execute().getData().get(0).getId();
			//checking if it's already been archived
			if(TextDB.getLineNumber(recentID, idFile ) < 0){
				Set<String> tweetParams = new HashSet<>();
				tweetParams.add("text"); tweetParams.add("created_at"); tweetParams.add("referenced_tweets");
				//set parameters for tweet
				Tweet rec = apiInstance.tweets().usersIdTweets(userTarg).tweetFields(tweetParams).execute().getData().get(0);
				//Retrieve tweet
				
				//if(isRefTweet(rec))
					
				TextDB.writeIDTo(rec, idFile);
				TextDB.writeTweetTo(rec, twFile);
				
				archTweets.add(new ArchTweet(rec.getText(), rec.getId()));
		
				System.out.println("Copying over " + recentID + " " + rec.getId() );
				
			}else {
				//System.out.println("Tweet already exists! " + apiInstance.tweets().usersIdTweets(userTarg).execute().getData().get(0).getId());
				//System.out.println(archTweets.size());
			}
			
	    }catch(Exception e) {
	    	//e.printStackTrace();
	    	System.out.println("Trouble retrieving tweet");
	   	}
	    	
		//archTweets.add(new ArchTweet("ddd", "k"));
	}
	
	public boolean isRefTweet(Tweet tweet) {
		if(tweet.getReferencedTweets().get(0).getType().getValue() != null) {
			return true;
		}
		return false;
	}
	
	
}

class TextDB{
	public static int getLineNumber(String userName, File usernameFile) {
	    boolean found = false;
	    int lineCount = -1;
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(usernameFile)))) {            
	        String line;
	        while ((line = reader.readLine()) != null && !found) {
	            ++lineCount; // increment and get (start at 0)
	            found = line.trim().equals(userName); // found it?
	        }
	    } catch (IOException ex) {
	       // Logger.getLogger(TestMain.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    if (!found) {
	        // we didn't find it... return -1 (an impossible valid value) to handle that scenario.
	        lineCount = -1;
	    }
	    return lineCount; // found it, what line?
	    
	}
	
	public static void writeTweetTo(Tweet t, File tw) {
		try {
            
			System.out.println("Writing tweet");
			//Whatever the file path is.
            File statText = tw;
            FileOutputStream is = new FileOutputStream(statText, true);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            
            //w.write(t.getText());
            //write tweet to file
            w.write("_T_TWEET_T_" + t.getId()  + "_T_TEXT_T_" + t.getText() + "\n");
            w.close();
        } catch (IOException e) {
        	System.err.println("Problem writing to the file ");
        }
	}
	
	public static void writeIDTo(Tweet t, File tw) {
		try {
            //Whatever the file path is.
            File statText = tw;
            FileOutputStream is = new FileOutputStream(statText, true);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            
            //w.write(t.getText());
            w.write(t.getId() + " \n");
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file ");
        }
	}
}
