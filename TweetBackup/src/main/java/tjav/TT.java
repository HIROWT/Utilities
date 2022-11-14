package tjav;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.User;

public class TT {
	public static void main(String[] args) throws ApiException {
		TwitterApi apiInstance = new TwitterApi(new TwitterCredentialsOAuth2(
				"P7euyqZGjYoNjr0WSzGKEJSYs",
				"2Xc7exfXuZeO8Ss5BbtY4oGc9UhNX08MJCdFuNKGN6q4Db02mc",
				"AAAAAAAAAAAAAAAAAAAAAJ8tjAEAAAAAEESm3mfq9e18NtzY7enPWN87auY%3DOmVyWIxzqYT6u69ElOOqlnCIQT5gup0G0oZegHtxbs4evUeaKm",
				System.getenv("TWITTER_OAUTH2_REFRESH_TOKEN")));

		// System.out.println(apiInstance.tweets().listsIdTweets("1343223757192613888").execute());
		 
		Set<String> tweetFields = new HashSet<>();
	    tweetFields.add("author_id");
	    tweetFields.add("id");
	    tweetFields.add("created_at");
	    tweetFields.add("description");

		
	    System.out.println(apiInstance.lists().listIdGet("1589714448587046912").userFields(tweetFields).execute());
	    
		 //System.out.println(apiInstance.users().findUserById("173271731").execute().toString());
		 
		// System.out.println(apiInstance.users().findUserById("173271731").tweetFields(tweetFields).execute());
			
		
		//User   user   = twitterClient.getUserFromUserName("RedouaneBali");
		
		//user.get
	}
}
