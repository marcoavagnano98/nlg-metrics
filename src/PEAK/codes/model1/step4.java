/**
 * 
 */
package model1;

/**
 * @author Qian Yang
 * Purpose of This Class: Do some postprocess.
 * Other Notes Relating to This Class (Optional):
 */
//From sort.java
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;

public class step4 {
	public static void step4() throws IOException {

		String file = "step2-out-SimilarityClass.txt";

		FileReader reader = new FileReader(file);
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
		FileUtils.writeLines(new File("step2-out-SimilarityClass.txt"
				+ "Sorted.txt"), lists);

	}
}
