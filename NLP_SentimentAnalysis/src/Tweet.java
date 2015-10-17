import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Status;
import twitter4j.User;


public class Tweet {
	public Status sta;
	
	public int numOfRetweet;
	public int numOfFavorited;
	
	public int numOfUserFollower;
	
	public String beforeProcess;
	public String afterProcess;
	
	public double sentiWordScore = 0.0;
	public double standfordScore = 0.0;
	
	public Tweet(Status sta) throws IOException{
		this.sta = sta;
		
		if(sta.isRetweet())
			numOfRetweet = sta.getRetweetCount();
		else
			numOfRetweet = 1;
		
		User usr = sta.getUser();
		numOfUserFollower = usr.getFollowersCount();
				
		if(sta.isFavorited())
			numOfFavorited = sta.getFavoriteCount();
		else
			numOfFavorited = 1;
		
		this.beforeProcess = sta.getText();
		this.afterProcess  = tweetToWords(beforeProcess);
		
		SentiWordNetDemoCode swn = SentiWordNetDemoCode.getInstance();
		SentimentAnalyzer    nlp = SentimentAnalyzer.getInstance();
		
		sentiWordScore = swn.getSentimentScore(afterProcess);
		standfordScore= nlp.findSentiment(afterProcess);    // from core nlp, score and error
	}
	
	   
	   public static String tweetToWords(String tweet){
	      // String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|"
	      //         + "(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
	       String urlPattern = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	       Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
	       Matcher m = p.matcher(tweet);

	       int i=0;
	       while (m.find()) {
	    	   try{
	           tweet = tweet.replaceAll(m.group(i),"");
	           i++;
	    	   }catch(Exception e){
	    		   
	    	   }finally{
	    		   break;
	    	   }
	       }

	       tweet = tweet.replaceAll("@\\w+|#\\w+|\\bRT\\b", "");
	       tweet = tweet.replaceAll("\n", " ");
	       tweet = tweet.replaceAll("[^\\p{L}\\p{N} ]+", "");
	       tweet = tweet.replaceAll(" +", " ").trim();

	       return tweet;
	   }
}
