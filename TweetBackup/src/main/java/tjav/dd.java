package tjav;

import java.util.*;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.*;

public class dd {

  public static void main(String[] args) {
    /**
     * Set the credentials for the required APIs.
     * The Java SDK supports TwitterCredentialsOAuth2 & TwitterCredentialsBearer.
     * Check the 'security' tag of the required APIs in https://api.twitter.com/2/openapi.json in order
     * to use the right credential object.
     */
    TwitterApi apiInstance = new TwitterApi(new TwitterCredentialsOAuth2(
"",
          "",
          "",
          ""));

    Set<String> tweetFields = new HashSet<>();
    
    Set<String> tweetFields2 = new HashSet<>();
    tweetFields.add("author_id");
    tweetFields.add("id");
    tweetFields.add("created_at");

    try {
     // findTweetById
    	// System.out.println(apiInstance.users().findUserById("173271731").execute().getData().entities(null).); 
    	 
    	 System.out.println(apiInstance.tweets().usersIdTweets("173271731").execute().getData().get(0).getId());
    	
    	
    	
    	Get2TweetsIdResponse result = apiInstance.tweets().findTweetById("1343223757192613888")
       .tweetFields(tweetFields)
       .execute();
     if(result.getErrors() != null && result.getErrors().size() > 0) {
       System.out.println("Error:");

       result.getErrors().forEach(e -> {
         System.out.println(e.toString());
         if (e instanceof ResourceUnauthorizedProblem) {
           System.out.println(((ResourceUnauthorizedProblem) e).getTitle() + " " + ((ResourceUnauthorizedProblem) e).getDetail());
         }
       });
     } else {
       System.out.println("findTweetById - Tweet Text: " + result.toString());
     }
     Set<String> us = new TreeSet<>();

      
     apiInstance.users().findUserById("173271731").tweetFields(tweetFields).execute().toString();
     
     
     
     
    } catch (ApiException e) {
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
