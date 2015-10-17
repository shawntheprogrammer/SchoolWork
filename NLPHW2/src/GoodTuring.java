import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class GoodTuring {
	
	private String[] sentence;
	private HashMap<String,ArrayList<String>> map;
	private int num_words;
	private double v;
	private int distinct_bigram=0;
	private HashMap<Integer,Integer> frequency_map;
	private double[][] count_table;
	private double[][] probability_table;
	
	public GoodTuring(String s, HashMap<String,ArrayList<String>> map,int num_words){
		s=s.toLowerCase();
		this.sentence=s.split(" ");
		this.map=map;
		this.num_words=num_words;
		this.v=(double)map.size();
		this.frequency_map=getFrequencyMap(map);
		count_table=new double[sentence.length+1][sentence.length+1];
		probability_table=new double[sentence.length+1][sentence.length+1];
	}
	
	private HashMap<Integer, Integer> getFrequencyMap(HashMap<String, ArrayList<String>> map) {
		HashMap<Integer,Integer> frequency_map=new HashMap<Integer, Integer>();
		
		for(String s : map.keySet()){
			ArrayList<String> list=map.get(s);
			Collections.sort(list);
			
			int frequency=0;
			String last=list.get(0);
			
			for(int i=0;i<list.size();i++){
				String str=list.get(i);
				if(str.equals(last))
					frequency++;
				else{
					last=str;
					if(!frequency_map.containsKey(frequency))
						frequency_map.put(frequency, 0);
					frequency_map.put(frequency, frequency_map.get(frequency)+1);
					frequency=1;
					distinct_bigram++;
				}
			}
			
			if(!frequency_map.containsKey(frequency))
				frequency_map.put(frequency, 0);
			frequency_map.put(frequency, frequency_map.get(frequency)+1);
			distinct_bigram++;
		}
		
		return frequency_map;
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
	
		int firstWordCount=getWordCount(sentence[0]);
		
		for(int i=1;i<probability_table.length-1;i++){
			int j=i+1;
			result*=probability_table[i][j];
		}
		
		// formatter = new DecimalFormat("0.00000000000000000000000000000000000000000");  
		//String f = formatter.format(result); 
		System.out.println();
		System.out.print("In good turing model, the probability of --");		
		for(int i=0;i<sentence.length;i++)
			System.out.print(sentence[i]+" ");
		System.out.print("is  ");		
		System.out.println(result);
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
				
				if(pairCount>=6){
					matrix[i][j]=String.valueOf(pairCount);
					count_table[i][j]=(double)pairCount;
				}else if(pairCount==0){
					int n1=frequency_map.get(1);
					double n0= Math.pow(v, 2) - distinct_bigram; 
					double val=((double)n1)/(double)n0;
					NumberFormat formatter = new DecimalFormat("0.000000");  
					String f = formatter.format(val); 
					matrix[i][j]=f;
					count_table[i][j]=val;
				}else{
					int Nc=frequency_map.get(pairCount);
					int Nc1=frequency_map.get(pairCount+1);
					double val=((double)pairCount+1.0)*((double)Nc1/(double)Nc);
					NumberFormat formatter = new DecimalFormat("0.000000");  
					String f = formatter.format(val); 
					matrix[i][j]=f;
					count_table[i][j]=val;
				}
			}
		}
		
		System.out.println();
		for(int i=0;i<sentence.length;i++)
			System.out.print(sentence[i]+" ");
		System.out.println("  :"+" Good-Turing bigarm count table is below");		
		
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
				
				double val;
				
				val=count_table[i][j]/(double)num_words;
				probability_table[i][j]=val;
				NumberFormat formatter = new DecimalFormat("0.0000000000");  
				String f = formatter.format(val);  
				matrix[i][j]=f;
			}
		}
		System.out.println();
		for(int i=0;i<sentence.length;i++)
			System.out.print(sentence[i]+" ");
		System.out.println("  :"+" Good-Turing bigarm probability table is below");
		
		for (String[] row : matrix) {
		    System.out.format("%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s%15s\n", row);
		}
		System.out.println();
	}
}
