import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class ABOpening {
	int depth;
	Board inputBoard;
	String outputBoard;
	static int positionEvaluated=0;
	
	public ABOpening(){
		inputBoard=new Board();
	}
	
	class Board{
		boolean isWhite;
		char[] position;
		ArrayList<Board> nextDepth;
		int estimation;
		Board next;
		
		Board(){
			position=new char[23];
			nextDepth=new ArrayList<Board>();
		}
		
		Board(char[] position,ArrayList<Board> nextDepth){
			this.position=position;
			this.nextDepth=nextDepth;
		}
	}
	
	public void GeneratesMovesOpening(Board board){
		board.nextDepth=GenerateAdd(board);
	}
	
	public void GeneratesBlackMoves(Board board){  //black move
		swapColor(board);
		board.nextDepth=GenerateAdd(board);
		swapColor(board);
		for(Board arr:board.nextDepth)
			swapColor(arr);
	}
	
	public void swapColor(Board board){   //swap colors
		for(int i=0;i<board.position.length;i++){
			if(board.position[i]=='W')
				board.position[i]='B';
			else if(board.position[i]=='B')
				board.position[i]='W';
		}
	}
	
	public ArrayList<Board> GenerateAdd(Board board){
		ArrayList<Board> list=new ArrayList<Board>();
		for(int i=0;i<board.position.length;i++){
			if(board.position[i]=='x'){
				Board copyBoard=copyBoard(board);
				copyBoard.position[i]='W';
				if(closeMill(i,copyBoard))
					generateRemove(copyBoard,list);
				else
					list.add(copyBoard);
			}
		}
		return list;
	}
	
	public Board copyBoard(Board original){
		Board copy=new Board();
		for(int i=0;i<original.position.length;i++)
			copy.position[i]=original.position[i];
		return copy;
	}
	
	public boolean closeMill(int location,Board board){
		switch(location){
			case 0:  return checkMill(0,1,2,board)||checkMill(0,3,6,board)||checkMill(0,8,20,board);
			case 1:  return checkMill(0,1,2,board);
			case 2:  return checkMill(0,1,2,board)||checkMill(2,5,7,board)||checkMill(2,13,22,board);
			case 3:  return checkMill(0,3,6,board)||checkMill(3,9,17,board)||checkMill(3,4,5,board);
			case 4:  return checkMill(3,4,5,board);
			case 5:  return checkMill(3,4,5,board)||checkMill(5,12,19,board)||checkMill(2,5,7,board);
			case 6:  return checkMill(0,3,6,board)||checkMill(6,10,14,board);
			case 7:  return checkMill(2,5,7,board)||checkMill(7,11,16,board);
			case 8:  return checkMill(0,8,20,board)||checkMill(8,9,10,board);
			case 9:  return checkMill(8,9,10,board)||checkMill(3,9,17,board);
			case 10: return checkMill(8,9,10,board)||checkMill(6,10,14,board);
			case 11: return checkMill(7,11,16,board)||checkMill(11,12,13,board);
			case 12: return checkMill(11,12,13,board)||checkMill(5,12,19,board);
			case 13: return checkMill(11,12,13,board)||checkMill(2,13,22,board);
			case 14: return checkMill(6,10,14,board)||checkMill(14,15,16,board)||checkMill(14,17,20,board);
			case 15: return checkMill(14,15,16,board)||checkMill(15,18,21,board);
			case 16: return checkMill(14,15,16,board)||checkMill(7,11,16,board)||checkMill(16,19,22,board);
			case 17: return checkMill(14,17,20,board)||checkMill(3,9,17,board)||checkMill(17,18,19,board);
			case 18: return checkMill(17,18,19,board)||checkMill(15,18,21,board);
			case 19: return checkMill(16,19,22,board)||checkMill(17,18,19,board)||checkMill(5,12,19,board);
			case 20: return checkMill(0,8,20,board)||checkMill(14,17,20,board)||checkMill(20,21,22,board);
			case 21: return checkMill(15,18,21,board)||checkMill(20,21,22,board);
			case 22: return checkMill(16,19,22,board)||checkMill(2,13,22,board)||checkMill(20,21,22,board);
			default: return false;
		}
	}
	
	public boolean checkMill(int i,int j,int k,Board board){
		char c=board.position[i];
		char c1=board.position[j];
		char c2=board.position[k];
		if(c==c1&&c==c2)
			return true;
		else
			return false;
	}
	
	public void generateRemove(Board board, ArrayList<Board> positions){
		int count=0;
		for(int i=0;i<board.position.length;i++){
			if(board.position[i]=='B'){
				if(!closeMill(i,board)){
					Board copy=copyBoard(board);
					copy.position[i]='x';
					positions.add(copy);
					count++;
				}
			}
		}
		if(count==0)
			positions.add(board);
	}
	public int getBlackMoves(Board board){
		Board copy=copyBoard(board);
		swapColor(copy);
		GeneratesMovesOpening(copy);
		return copy.nextDepth.size();
	}
	public int getWhiteMoves(Board board){
		Board copy=copyBoard(board);
		GeneratesMovesOpening(copy);
		return copy.nextDepth.size();
	}
	public int staticEstimation(Board board){
		int numWhite=0;
		int numBlack=0;
		for(int i=0;i<board.position.length;i++){
			if(board.position[i]=='W')
				numWhite++;
			else if(board.position[i]=='B')
				numBlack++;
		}
		int numWhiteMoves=getWhiteMoves(board);
		int numBlackMoves=getBlackMoves(board);
		
		
			return 4*(numWhite-numBlack)+(numWhiteMoves-numBlackMoves);
		
		/*int numWhite=0;
		int numBlack=0;
		for(int i=0;i<board.position.length;i++){
			if(board.position[i]=='W')
				numWhite++;
			else if(board.position[i]=='B')
				numBlack++;
		}
		board.estimation=numWhite-numBlack;
		return numWhite-numBlack;*/
	}
	
	public String readFile(String filename)
	{
	   String content = null;
	   File file = new File("C:/Users/Shawn/Desktop/"+filename); //for ex foo.txt
	   try {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}
	
	public void writing(String s,Board board) {
		// get output fileName
		int start=0,end=0,count=0,index=0;
		while(index<s.length()){
			if(count==0&&s.charAt(index)==' '){  //first ' '
				count++;
				start=index;
			}
			else if(count==1&&s.charAt(index)==' '){ // second ' '
				end=index;
				break;
			}
			index++;
		}
		String fileName=s.substring(start+1,end);
		
	    try {
	//What ever the file path is.
	        File statText = new File("C:/Users/Shawn/Desktop/"+fileName);
	        FileOutputStream is = new FileOutputStream(statText);
	        OutputStreamWriter osw = new OutputStreamWriter(is);    
	        Writer w = new BufferedWriter(osw);
	        w.write(board.position);
	        w.close();
	    } catch (IOException e) {
	        System.err.println("Problem writing to the file.txt");
	    }
	}

	//set inputBoard && depth
	public void setUpOpening(ABOpening opening,String string){
		String fileName=null;
		String openingPosition=null;
		int spaceCount=0;
		for(int i=0;i<string.length();i++){
			if(string.charAt(i)==' '&&spaceCount==0){
				fileName=string.substring(0,i);
				openingPosition=readFile(fileName);
				opening.inputBoard.position=openingPosition.toCharArray();
			}
			
			if(spaceCount==2 && Character.isDigit(string.charAt(i))){
				int j=i;
				while(j<string.length() &&  Character.isDigit(string.charAt(j)))
					j++;
				opening.depth=Integer.parseInt(string.substring(i,j));
				return;
			}
			
			if(string.charAt(i)==' ')
				spaceCount++;
		}
	}
	
	public void alphaBeta(Board board,int depth,boolean isWhite){
		if(isWhite)
			MaxMin(board,Integer.MIN_VALUE,Integer.MAX_VALUE,depth);
		else
			MinMax(board,Integer.MIN_VALUE,Integer.MAX_VALUE,depth);
	}
	
	public int MaxMin(Board board,int alpha,int beta,int depth){
		if(depth==0){
			board.estimation=staticEstimation(board);
			return board.estimation;
		}
		
		board.estimation=Integer.MIN_VALUE;
		GeneratesMovesOpening(board);
		for(Board boa:board.nextDepth){
			positionEvaluated++;
			int minmax=MinMax(boa,alpha,beta,depth-1);
			if(minmax>board.estimation)
				board.next=boa;
			board.estimation=Math.max(board.estimation,minmax);			
		//	if(board.estimation==boa.estimation)
		//		board.next=boa;
			if(board.estimation>=beta)
				return board.estimation;
			else
				alpha=Math.max(board.estimation, alpha);
		}
		return board.estimation;
	}
	
	public int MinMax(Board board,int alpha,int beta,int depth){
		if(depth==0){
			board.estimation=staticEstimation(board);
			return board.estimation;
		}
		
		board.estimation=Integer.MAX_VALUE;
		GeneratesBlackMoves(board);
		for(Board boa:board.nextDepth){
			positionEvaluated++;
			int maxmin=MaxMin(boa,alpha,beta,depth-1);
			if(maxmin<board.estimation)
				board.next=boa;
			board.estimation=Math.min(board.estimation,maxmin);			
		//	if(board.estimation==boa.estimation)
		//		board.next=boa;
			if(board.estimation<=alpha)
				return board.estimation;
			else
				beta=Math.min(board.estimation, beta);
		}
		return board.estimation;
	}
	

	public String setOutput(char[] arr){
		String string="";
		for(int i=0;i<arr.length;i++)
			string+=arr[i];
		return string;
	}
	
	public static void main(String[] args){
		ABOpening opening=new ABOpening();
		Board board=opening.inputBoard;
		String output=opening.outputBoard;
		Scanner scanner=new Scanner(System.in);
		System.out.print("Enter input:");
	    String input = scanner.nextLine(); 			 //get input
	    
	    opening.setUpOpening(opening,input);		 //set opening board and depth
	    opening.alphaBeta(board,opening.depth,true); //true means white is next move, set Boards and all the ArrayList, next pointer
	                                                                //board1.txt board2.txt 23
	    
	    opening.writing(input,board.next);
	    output=opening.setOutput(board.next.position);
	    System.out.println();
	    System.out.println("Board Position: "+output);
	    System.out.println("Positions evaluated by static estimation: "+positionEvaluated+".");
	    System.out.println("MINIMAX estimate: "+board.estimation);
	}
}