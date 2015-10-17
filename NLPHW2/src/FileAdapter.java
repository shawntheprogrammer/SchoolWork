import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class FileAdapter {
	private HashMap<String,ArrayList<String>> map;
	private int num_words;
	
	public FileAdapter( ){
		map=new HashMap<String, ArrayList<String>>();
		num_words=0;
	}
	
	public void processFile(String filePath) throws IOException{
		File file=new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		String lastWord=null;
		
		while ((line = br.readLine()) != null) {
			
		    line=line.replaceAll("  "," ");   //change double spaces to single space
		    line=line.trim(); 				  //trim the leading and trailing space
		    line=line.toLowerCase();		  //convert to lower case
		    
		    String[] array=line.split(" ");
		    
		    num_words+=array.length;          //update total tokens
		    
		    if(lastWord!=null){
		    	if(!map.containsKey(lastWord))
		    		map.put(lastWord, new ArrayList<String>());
		    	map.get(lastWord).add(array[0]);
		    }
		    	
		    for(int i=0;i<array.length-1;i++){
		    	String word=array[i];
		    	String next=array[i+1];
		    	if(!map.containsKey(word))
		    		map.put(word, new ArrayList<String>());
		    	map.get(word).add(next);
		    }
		    
		    lastWord=array[array.length-1];
		}
		br.close();
	}
	
	public int countBigrams(){
		int result=0;
		
		for(String word : map.keySet()){
			ArrayList<String> words=map.get(word);
			HashMap<String,Boolean> word_map=new HashMap();
			
			for(String s : words){
				if(!word_map.containsKey(s))
					word_map.put(s, true);
			}
			
			result+=word_map.size();
		}
		
		return result;
	}
	
	public int count(){
		int result=0;
		
		for(String word : map.keySet()){
			ArrayList<String> words=map.get(word);
			Collections.sort(words);
			
			for(int i=1;i<words.size();i++)
				if(!words.get(i).equals(words.get(i-1)))
					result++;
			result++;
		}
		
		return result;
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		FileAdapter fa=new FileAdapter();
		fa.processFile("C://Users//Shawn//Desktop//15 Spring//Natural Language Processing//hw//HW2_trainingData.txt");  // filePath
		HashMap<String,ArrayList<String>> map=fa.map;
		
		System.out.println("For the input corpus, num of distinct bigrams is : "+fa.countBigrams());
		System.out.println("num of total bigrams in the corpus is: "+ fa.num_words);
		
		Bigram bg=new Bigram(new String("The company chairman said he will increase the profit next year"), map, fa.num_words);
		AddOne ao=new AddOne(new String("The company chairman said he will increase the profit next year"), map, fa.num_words);
		GoodTuring gt=new GoodTuring(new String("The company chairman said he will increase the profit next year"), map, fa.num_words);
		
		bg.makeCountTable();
		bg.makeProbabilityTable();
		bg.getPossibility();
		
		ao.makeCountTable();
		ao.makeProbabilityTable();
		ao.getPossibility();
		
		gt.makeCountTable();
		gt.makeProbabilityTable();
		gt.getPossibility();
		
		
		Bigram bg1=new Bigram(new String("The president said he believes the last year profit were good"), map, fa.num_words);
		AddOne ao1=new AddOne(new String("The president said he believes the last year profit were good"), map, fa.num_words);
		GoodTuring gt1=new GoodTuring(new String("The president said he believes the last year profit were good"), map, fa.num_words);
		
		bg1.makeCountTable();
		bg1.makeProbabilityTable();
		bg1.getPossibility();
		
		ao1.makeCountTable();
		ao1.makeProbabilityTable();
		ao1.getPossibility();
		
		gt1.makeCountTable();
		gt1.makeProbabilityTable();
		gt1.getPossibility();
	}

}
