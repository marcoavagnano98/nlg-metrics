/**
 * 
 */
package model1;

/**
 * @author Qian Yang
 * Purpose of This Class: Get the similarity class.
 * Other Notes Relating to This Class (Optional):
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class step2 {
	public static void step2() throws IOException {
		
		File logFile1 = new File("step2-out.txt");
		PrintStream outputPrintStream1 = new PrintStream(logFile1);
		System.setOut(outputPrintStream1);

		File logFile2 = new File("step2-out-SimilarityClass.txt");
		PrintStream outputPrintStream2 = new PrintStream(logFile2);
		System.setOut(outputPrintStream2);

		FileReader reader1 = new FileReader("step1-out.txt");
		BufferedReader br1 = new BufferedReader(reader1);
		String str1 = null;

		while ((str1 = br1.readLine()) != null) {

			String text1 = str1;
			
			String triple1 = "*";
			String[] splited1 = text1.split("\"");
			

			System.setOut(outputPrintStream1);
			System.out.println(splited1[1]);

			System.setOut(outputPrintStream2);
			System.out.println(splited1[5] + " " + "\"" + splited1[1] + "\""
					+ " " + "\"" + splited1[3] + "\"");
			
		}
	}
}
