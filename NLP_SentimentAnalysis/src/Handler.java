import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;


public class Handler {
	
	public Twit twit = new Twit();
	ArrayList<Tweet> result = new ArrayList<>(); 
	
	public int standfordPos = 0;
	public int standfordNeg = 0;
	
	public int snwPos = 0;
	public int snwNeg = 0;
	
	public Handler(){
	}
	
	public void search(String keyword) throws IOException{
		result = twit.search(keyword);
	}
	
	public void printResult(){
		
		for(Tweet t : result){
			System.out.println(t.beforeProcess);
			System.out.println(t.afterProcess);
			System.out.println("corenlp score: "+t.standfordScore);
			System.out.println("sentiword score: "+t.sentiWordScore);
			System.out.println("");
		}
		DecimalFormat df = new DecimalFormat("#%");
		
		String nlpPos = df.format((double)standfordPos/(standfordPos+standfordNeg));
		String nlpNeg = df.format((double)standfordNeg/(standfordPos+standfordNeg));
		String snos  = df.format((double)snwPos/(snwPos+snwNeg));
		String sneg  = df.format((double)snwNeg/(snwPos+snwNeg));
		
		System.out.println("StandfordCoreNLP -- pos: " + standfordPos 
							+ " neg: " +standfordNeg
							+ " positive percentage: " + nlpPos
							+ " negartive percentage: " + nlpNeg
						);
		System.out.println("SentiWordNet -- pos: " + snwPos 
				            +  " neg: " + snwNeg
				            + " positive percentage: " + snos
							+ " negartive percentage: " + sneg
						);
	}
	
	public void calculate(){
		for(Tweet t : result){
			calculate(t);
		}
	}
	
	public void calculate(Tweet tweet){
		double sdfScore = tweet.standfordScore;
		double snwScore = tweet.sentiWordScore;
		
		if(sdfScore >= 2.0)
			this.standfordPos++;
		else
			this.standfordNeg++;
		
		if(snwScore >= 0.0)
			this.snwPos++;
		else
			this.snwNeg++;
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Handler test = new Handler();
		System.out.print("Enter the keyword you wanna search:");
	    Scanner scanIn = new Scanner(System.in);
	    String input = scanIn.nextLine();
	    scanIn.close();      
		test.search(input);
		test.calculate();
		test.printResult();
		
	}

}
