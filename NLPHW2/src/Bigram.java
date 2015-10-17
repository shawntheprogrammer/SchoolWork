import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Bigram {
	
	private String[] sentence;
	private HashMap<String,ArrayList<String>> map;
	private int num_words;
	
	public Bigram(String s, HashMap<String,ArrayList<String>> map,int num_words){
		s=s.toLowerCase();
		this.sentence=s.split(" ");
		this.map=map;
		this.num_words=num_words;
	}
	
	public int getWordCount(String word){
		if(map.containsKey(word))
			return map.get(word).size();
		else
			return 0;
	}
	
	public int getPairCount(String word,String next){
		int num=0;
		
		if(map.containsKey(word)){
			ArrayList<String> list=map.get(word);
			for(String w:list){
				if(w.equals(next)){
					num++;
				}
			}
		}
		
		return num;
	}
	
	public double getPossibility() throws ParseException{
		double result=1.0;
		
		for(int i=0;i<sentence.length-1;i++){
			String word=sentence[i];
			String next=sentence[i+1];
			int pairCount=getPairCount(word,next);
			int wordCount=getWordCount(word);
			result*=(double)pairCount/(double)wordCount;
		}
		
		int firstWordCount=getWordCount(sentence[0]);
		
		result*=(double)firstWordCount/(double)num_words;
		
		System.out.println();
		System.out.print("In digram model, the probability of -- ");		
		for(int i=0;i<sentence.length;i++)
			System.out.print(sentence[i]+" ");
		System.out.print("is  ");		
		
		System.out.println(result);
		System.out.println();
		return result;
	}
	
	public void makeCountTable(){
		String[][] matrix=new String[sentence.length+1][sentence.length+1];
		
		//first row
		matrix[0][0]=" ";
		for(int col=1;col<matrix.length;col++){
			matrix[0][col]=sentence[col-1];
		}
		
		
		for(int i=1;i<matrix.length;i++){
			matrix[i][0]=sentence[i-1];
			for(int j=1;j<matrix.length;j++){
				String word=sentence[i-1];
				String next=sentence[j-1];
				matrix[i][j]=String.valueOf(getPairCount(word,next));
			}
		}
		System.out.println();
		for(int i=0;i<sentence.length;i++)
			System.out.print(sentence[i]+" ");
		System.out.println("  :"+" bigarm count table is below");
		for (String[] row : matrix) {
		    System.out.format("%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s\n", row);
		}
		
		System.out.println();
	}
	
	public void makeProbabilityTable(){
		String[][] matrix=new String[sentence.length+1][sentence.length+1];
		
		//first row
		matrix[0][0]=" ";
		for(int col=1;col<matrix.length;col++){
			matrix[0][col]=sentence[col-1];
		}
		
		
		for(int i=1;i<matrix.length;i++){
			matrix[i][0]=sentence[i-1];
			for(int j=1;j<matrix.length;j++){
				String word=sentence[i-1];
				String next=sentence[j-1];
				int pairCount=getPairCount(word,next);
				int wordCount=getWordCount(word);
				double val=(double)pairCount/(double)(wordCount);
				val = Math.round(val * 100000.0) / 100000.0;
				matrix[i][j]=String.valueOf(val);
			}
		}
		System.out.println();
		for(int i=0;i<sentence.length;i++)
			System.out.print(sentence[i]+" ");
		System.out.println("  :"+" bigarm probability table is below");
		for (String[] row : matrix) {
		    System.out.format("%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s\n", row);
		}
		System.out.println();
	}
}
