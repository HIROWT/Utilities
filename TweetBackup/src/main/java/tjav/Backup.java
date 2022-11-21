
package tjav;

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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Tweet;

public class Backup {
	public static void main(String[] args) throws FileNotFoundException, ApiException {
		//settingusername
		
		System.out.println("Starting tweet retrieval");
		
		String tUserName = "brutedeforce";
		
		
		File idFile = new File("TweetIDs.txt");
		File twFile = new File("TweetBackups.txt");
		
		//user id
		
		TwitterApi apiInstance = new TwitterApi(new TwitterCredentialsOAuth2(
				"",
				"",
				"",
				""));
		
		Runnable helloRunnable = new Runnable() {
		    public void run() {
		    	try {
		    	String userTarg = apiInstance.users().findUserByUsername(tUserName).execute().getData().getId();
				
				//getting ID of most recent tweet
				String recentID = apiInstance.tweets().usersIdTweets(userTarg).execute().getData().get(0).getId();
				//checking if it's already been archived
				if(getLineNumber(recentID, idFile ) < 0){
					//adding to IDs file and main file if not present
					//System.out.println(apiInstance.users().findUserById("173271731").execute().getData());
					
					
					Tweet rec = apiInstance.tweets().usersIdTweets(userTarg).execute().getData().get(0);
					writeIDTo(rec, idFile);
					writeTweetTo(rec, twFile);
					System.out.println(rec.getText());
					System.out.println("Copying over " + recentID + " " + rec.getId() );
				}
				
		    	}catch(Exception e) {
		    		System.out.println("Trouble retrieving tweet");
		    	}
		    }
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(helloRunnable, 0, 100, TimeUnit.SECONDS);

		
		
		
		
		//Tweet rec = apiInstance.tweets().usersIdTweets(userTarg).execute().getData().get(0);
		//System.out.println(rec.getText());
		//String tw = apiInstance.tweets().findTweetById(recentID).tweetFields(tweetFields).execute().toString();
		//System.out.println(tw);
	
	}
	
	private static int getLineNumber(String userName, File usernameFile) {
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
	
	public static void writeTweetTo(Tweet t, File tw) {
		try {
            //Whatever the file path is.
            File statText = tw;
            FileOutputStream is = new FileOutputStream(statText, true);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            
            //w.write(t.getText());
            //write tweet to file
            w.write(t.getText() + " \n_________________" + t.getCreatedAt().toString() +  "\n");
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file ");
        }
	}
}

