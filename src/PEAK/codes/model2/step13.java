/**
 * 
 */
package model2;

/**
 * @author Qian Yang
 * Purpose of This Class:
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
import java.util.List; 
import java.util.Collections;  
import java.util.Comparator;  

import org.apache.commons.io.FileUtils;

public class step13 {
	public static void step13() throws IOException {

		// String path = "student-summaries-afterproAfterClauseIEAfterScoring/";
		String path = "student-summaries-afterproAfterClauseIEAfterScoringLables/";
		File file = new File(path);
		String[] files = file.list();
		for (String f : files) {
			if (f.matches("^[0-9].*$") && f.endsWith("ForScoring.txt")) {

				FileReader reader = new FileReader(path + f);
				BufferedReader br = new BufferedReader(reader);

				String str = null;
				List<String> lists = new ArrayList<String>();

				str = br.readLine();
				while (str != null) {
					lists.add(str);
					str = br.readLine();
				}
				br.close();
				reader.close();

				// Collections.sort(lists);
				Collections.sort(lists, Collections.reverseOrder());

				// output
				String path1 = "student-summaries-afterproAfterClauseIEAfterScoringSorting/";
				FileUtils.writeLines(new File(path1 + f.replace(".txt", "")
						+ "Scoring.txt"), lists);
			}
		}
	}
}
