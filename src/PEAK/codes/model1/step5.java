/**
 * 
 */
package model1;

/**
 * @author Qian Yang
 * Purpose of This Class: Do some postprocess.
 * Other Notes Relating to This Class (Optional):
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class step5 {
	public static void step5() throws IOException {

		File logFile2 = new File(
				"step2-out-SimilarityClassAfterAscending.txt");
		PrintStream outputPrintStream2 = new PrintStream(logFile2);
		System.setOut(outputPrintStream2);

		FileReader reader1 = new FileReader(
				"step2-out-SimilarityClassSorted.txt");
		BufferedReader br1 = new BufferedReader(reader1);
		String str1 = null;

		while ((str1 = br1.readLine()) != null) {

			String text1 = str1;
			
			String triple1 = "*";
			String[] splited1 = text1.split("\"");
			

			System.setOut(outputPrintStream2);
			System.out.println(splited1[1] + splited1[3]);
			
		}
	}
}
