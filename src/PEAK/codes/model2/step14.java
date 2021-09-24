/**
 * 
 */
package model2;

/**
 * @author Qian Yang
 * Purpose of This Class: Based on Munkres-Kuhn algorithm to generate the final score
 * Other Notes Relating to This Class (Optional):
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List; 
import java.util.Collections;  
import java.util.Comparator;  

import org.apache.commons.io.FileUtils;

public class step14 {
	public static void step14() throws IOException{
		
		
		double SIMILARITY = 0.6;
		
		//String path1 = "student-summaries-afterproAfterClauseIEAfterScoringSortingHighestAssignment/";
		String path1 = "student-summaries-afterproAfterClauseIEAfterScoringSortingHighest/";
 		
		File logFile1 = new File(path1+"forR.txt");
		PrintStream outputPrintStream1 = new PrintStream(logFile1);
		
      String path = "student-summaries-afterproAfterClauseIEAfterScoringSorting/";
		File file = new File(path);
		String[] files = file.list();
		for(String f :files){
			if(f.matches("^[0-9].*$")){
				
				int[][] costs = new int[1000][1000];
				//output
	        		
	        		
	        		//File logFile = new File(path1+f.replace(".txt", "")+"HighestAssignment.txt");
	        		File logFile = new File(path1+f.replace(".txt", "")+"Highest.txt");
	        		
	        		PrintStream outputPrintStream = new PrintStream(logFile);
	        		System.setOut(outputPrintStream);
	 		
	        	//input
			 FileReader reader = new FileReader(path+f);
			 BufferedReader br = new BufferedReader(reader);
			 
			 
			 
			 
			 
	         String str = null;
	         String strCurrent = null;
	         
	         while((str = br.readLine()) != null) {
	        	 
	        	 	String[] splitedStr = str.split("\"");
	        	 	double similarity = Double.parseDouble(splitedStr[3]);
	        	 	if(similarity >= SIMILARITY){
	        	 		int row = Integer.parseInt(splitedStr[1]);
	        	 		int col = Integer.parseInt(splitedStr[5]);
	        	 		int weight = Integer.parseInt(splitedStr[7]);
	        	 		costs[row][col] = weight;
	        	 	}       	 
	         }
	         
	         int score = HungarianAlgorithm.hgAlgorithm(costs, "max");
	         //int score[][] = HungarianAlgorithm.hgAlgorithm(costs, "max");
	         System.setOut(outputPrintStream);
	         System.out.println(score);
	         //System.out.println(Arrays.deepToString(score));
	         
	         System.setOut(outputPrintStream1);
	         System.out.print(score+",");
	          
	    
	         br.close();
	         reader.close();
       
		}
		}
	}
}
