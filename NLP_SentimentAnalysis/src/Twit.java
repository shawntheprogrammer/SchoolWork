
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class Twit {
	
   
   public Twit (){  
   }
   
   // a local handler to deal with keyword search and text processing
   public ArrayList<Tweet> search(String keyword) throws IOException{
	   
	   return getTweets(keyword);
   }
   
   // interact with Twitter and return all the found tweets contain keyword
   public ArrayList<Tweet> getTweets (String keyword) throws IOException {
	    
	    ArrayList<Tweet> tweets = new ArrayList<>();
	    
    	ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true)
    	  .setOAuthConsumerKey("nk80Mm2fYvVESqJ0Joxq8Xy91")
    	  .setOAuthConsumerSecret("vMrc0ewyQCiidRS8jJHAiNo6ZdF0RwuHq4xg9wE8aNzComSOvi")
    	  .setOAuthAccessToken("3179156779-H8NX1fPmMoApiVShYrXkbpRUP3RY8bqrOO9TXZ5")
    	  .setOAuthAccessTokenSecret("cb4GAFSVdxzAK01VEk6aP0V71V5Pr2m2Y78s5ISodRR6J");
    	
    	TwitterFactory tf = new TwitterFactory(cb.build());
    	Twitter twitter = tf.getInstance();
    	
    	  Query query = new Query(keyword);
    	  query.setLocale("en");
          query.setLang("en");
    	  int numberOfTweets = 1000;
    	  long lastID = Long.MAX_VALUE;
    	  ArrayList<Status> status = new ArrayList<Status>();
    	  
    	  while (status.size () < numberOfTweets) {
    	    if (numberOfTweets - status.size() > 100)
    	      query.setCount(100);
    	    else 
    	      query.setCount(numberOfTweets - status.size());
    	    
    	    try {
    	      QueryResult result = twitter.search(query);
    	      status.addAll(result.getTweets());
    	      System.out.println("Gathered " + status.size() + " tweets");
    	      for (Status t: status) 
    	        if(t.getId() < lastID) lastID = t.getId();

    	    }catch (TwitterException te) {
    	    	System.out.println("Couldn't connect: " + te);
    	    	numberOfTweets = 0;
    	    }
    	    query.setMaxId(lastID-1);
    	  }
          for (Status tweet : status) {
          	tweets.add(new Tweet(tweet));
         }

        
        return tweets;
    }
}