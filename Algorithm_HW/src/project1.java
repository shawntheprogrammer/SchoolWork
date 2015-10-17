import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class project1 {
	
	public static void tagOfWar(int[] A){
		int num = A.length;
		int total_weight = 0;
		for(int i = 0; i<num; i++){
			total_weight += A[i];
		}
		boolean[][] dp = new boolean[num+1][total_weight+1];  // row->num of people column-> weight
		dp[0][0] = true;       // 0 people 0 weight is true, base case
		
		for(int i = 0; i<num; i++){  // iterate through the array
			for(int j = num - 1; j >= 0; j--){   //starts from backwards, avoid 
				for(int k = 0; k<dp[0].length; k++){
					if(dp[j][k] == true){
						dp[j+1][k+A[i]] = true;
					}
				}
			}
		}
		
		 int target = total_weight/2;
		 int left = target;
		 int right = total_weight - left;
		 
		 while( !dp[num/2][left] && !dp[num/2][right]){
			left--;
			right++;
		}
		
		if(dp[num/2][left] == true)   // Math.min(left,total_weight-left)
			System.out.println(Math.min(left,total_weight-left) + " " + Math.max(left,total_weight-left));
		else
			System.out.println(Math.min(right,total_weight-right) + " " + Math.max(right,total_weight-right));		
	}

	private static ArrayList<ArrayList<Integer>> getData(ArrayList<String> result) {
		ArrayList<ArrayList<Integer>> list = new ArrayList();
		ArrayList<Integer> input = new ArrayList();
		
		for(String line : result){
			if(line.trim().equals(""))
	    		continue;
	    	String[] array = line.trim().split("\\s+");
	    	if(array.length!=0){
	    		for(String s : array){
	    			if(s.matches("[0-9]+"))
	    				input.add(Integer.parseInt(s));
	    		}
	    	}
		}
		int num_cases = input.get(0);
		int index = 1;
		
		while(num_cases>0){                          // take the input data
			int num = input.get(index);
			ArrayList<Integer> one_list = new ArrayList();
			for(int i = index + 1; i < num+index+1; i++){
				one_list.add(input.get(i));
			}	
			list.add(one_list);
			index = num + index + 1;
			num_cases--;
		}
		return list;
	}

	private static ArrayList<ArrayList<Integer>> getInput(String filePath) throws IOException {
		ArrayList<ArrayList<Integer>> list = new ArrayList();
		ArrayList<Integer> input = new ArrayList();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {  //read input file 
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if(line.trim().equals(""))
		    		continue;
		    	String[] array = line.trim().split("\\s+");
		    	if(array.length!=0){
		    		for(String s : array){
		    			input.add(Integer.parseInt(s));
		    		}
		    	}
		    }
		}
		
		int num_cases = input.get(0);
		int index = 1;
		
		while(num_cases>0){                          // take the input data
			int num = input.get(index);
			ArrayList<Integer> one_list = new ArrayList();
			for(int i = index + 1; i < num+index+1; i++){
				one_list.add(input.get(i));
			}	
			list.add(one_list);
			index = num + index + 1;
			num_cases--;
		}
		return list;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<Integer>> input = new ArrayList();
		
		if(args.length!=0){
			input = getInput(args[0]);
		}else{
		    System.out.println("Enter data, please add a whitespace and keyword 'exit' as the end of input ");
	        
		    Scanner s = new Scanner(System.in);
		    ArrayList<String> result = new ArrayList();
	        while (s.hasNextLine()) {
	        	String line = s.nextLine();
			    result.add(line);
			    if(line.contains("exit"))
			    	break;
	        }
	        s.close(); 
	        input = getData(result);
		}
		//input = getInput("C:\\Users\\Shawn\\Desktop\\test.txt");
		
		for(int i = 0; i < input.size(); i++){
			ArrayList<Integer> list = input.get(i);
			int[] arr = new int[list.size()];
			for(int j = 0; j < arr.length; j++)
				arr[j] = list.get(j);
			tagOfWar(arr);
		}
	}
}
