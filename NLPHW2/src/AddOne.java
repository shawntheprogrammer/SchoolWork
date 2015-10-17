import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;


public class AddOne {
	
	private String[] sentence;
	private HashMap<String,ArrayList<String>> map;
	private int num_words;
	private double v;
	
	public AddOne(String s, HashMap<String,ArrayList<String>> map,int num_words){
		s=s.toLowerCase();
		this.sentence=s.split(" ");
		this.map=map;
		this.num_words=num_words;
		this.v=(double)map.size();
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
			result*=((double)pairCount+1.0)/((double)wordCount+v);
		}
		
		int firstWordCount=getWordCount(sentence[0]);
		
		result*=(double)((double)firstWordCount+1.0)/((double)num_words+v);
		
		NumberFormat formatter = new DecimalFormat("0.00000000000000000000000000000000000000000");  
		String f = formatter.format(result); 
		
		System.out.println();
		System.out.print("In add-one smothing model, the probability of --");		
		for(int i=0;i<sentence.length;i++)
			System.out.print(sentence[i]+" ");
		System.out.print("is  ");		
		
		System.out.println(f);
		System.out.println("In scientifitic notation, it is "+result);
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
				int pairCount=getPairCount(word,next);
				int wordCount=getWordCount(word);
				double val=(((double)pairCount+1.0)*(double)wordCount)/((double)(wordCount)+v);
				val = Math.round(val * 100000.0) / 100000.0;
				matrix[i][j]=String.valueOf(val);
			}
		}
		System.out.println();
		for(int i=0;i<sentence.length;i++)
			System.out.print(sentence[i]+" ");
		System.out.println("  :"+" add-one smothing bigarm count table is below");
		
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
				double val=((double)pairCount+1.0)/((double)(wordCount)+v);
				val = Math.round(val * 100000.0) / 100000.0;
				
				NumberFormat formatter = new DecimalFormat("0.00000");  
				String f = formatter.format(val);  
				matrix[i][j]=f;
			}
		}
		System.out.println();
		for(int i=0;i<sentence.length;i++)
			System.out.print(sentence[i]+" ");
		System.out.println("  :"+" add-one smothing bigarm probability table is below");
		for (String[] row : matrix) {
		    System.out.format("%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s\n", row);
		}
		System.out.println();
	}
}
