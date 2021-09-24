/**
 * 
 */
package model2;

/**
 * @author Qian Yang
 * Purpose of This Class: Generate the details when scoring.
 * Other Notes Relating to This Class (Optional):
 */

import java.io.IOException;

import it.uniroma1.lcl.adw.ADW;
import it.uniroma1.lcl.adw.DisambiguationMethod;
import it.uniroma1.lcl.adw.LexicalItemType;
import it.uniroma1.lcl.adw.comparison.SignatureComparison;
import it.uniroma1.lcl.adw.comparison.WeightedOverlap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class step12 {
	public static void step12() throws IOException {
		
		HashMap<String, Integer> Pyramid = new HashMap<String, Integer>();
		HashMap<String, Integer> PyramidDivided = new HashMap<String, Integer>();
		
		Set<String> ADWSet = new HashSet<String>();

		FileReader reader2 = new FileReader(
				"step11-output-labels.txt");// 0.8backup
		BufferedReader br2 = new BufferedReader(reader2);
		String str2 = null;
		int indexOfPyramid = 0;

		while ((str2 = br2.readLine()) != null) {
			if (str2.equals("\n") || str2.startsWith("===")) {
				str2 = br2.readLine();
			} else {
				indexOfPyramid++;

				String text2 = str2;
				String[] splited2 = text2.split("&");

				// first find the weigth: tempW1
				int i1 = -1;
				int tempW1 = 0;
				for (String testString1 : splited2) {
					i1++;
					if (splited2[i1].matches("^(\\d+)(.*)"))
						tempW1 = Integer.parseInt(splited2[i1]);
				}
				// then build the hashSet
				int i = -1;
				String tempS = "*";
				int tempW = 0;
				for (String testString : splited2) {
					i++;
					if (splited2[i].startsWith("\"")) {
						tempS = splited2[i];

						// delete the "\"" of "Matter" "is"
						// "all the objects and substances"
						String[] splited3 = tempS.split("\"");
						String concate = "*";
						for (String iterator3 : splited3) {

							concate = concate.concat(iterator3);
						}
						tempS = concate.replace("*", "");

						// end of deleting
						Pyramid.put(tempS, tempW1);
						PyramidDivided.put(tempS, indexOfPyramid);
						ADWSet.add(tempS);
					}
				}

			}
		}
		// end of creating Pyramid

		String path = "output/student-summaries-afterproAfterClauseIE/";
		File file = new File(path);
		String[] files = file.list();
		for (String f : files) {
			if (f.matches("^[0-9].*$")) {

				// output
				String path1 = "output/student-summaries-afterproAfterClauseIEAfterScoringLables/";
				File logFile = new File(path1 + f.replace(".txt", "")
						+ "ForScoring.txt");
				PrintStream outputPrintStream = new PrintStream(logFile);

				File logFile1 = new File(path1 + f.replace(".txt", "")
						+ "ForDetails.txt");
				PrintStream outputPrintStream1 = new PrintStream(logFile1);

				FileReader reader1 = new FileReader(path + f);
				BufferedReader br1 = new BufferedReader(reader1);
				String str1 = null;
				String sentence1 = null;
				int currentLineNum = 0;
				int lineNum = 0;
				while ((str1 = br1.readLine()) != null) {

					if (str1.equals("\n")) {
						str1 = br1.readLine();
					} else if (!str1.startsWith("#")) {
						String text1 = str1;
						String triple1 = "*";

						String[] splited1 = text1.split("\"");
						// String lastIterator1 = null;
						for (String iterator1 : splited1) {
							if (iterator1.matches("^[a-zA-Z].*$")) {
								triple1 = triple1.concat(" " + iterator1);
							} else if (iterator1.matches("^(\\d+)(.*)")) {// start
																			// with
																			// numbers
								lineNum = Integer
										.parseInt(getNumbers(iterator1));
								if (lineNum != currentLineNum)
									System.out.println(lineNum);
							}
						}

						triple1 = triple1.replace("* ", "");
						// ADW part
						ADW pipeLine = new ADW();
						LexicalItemType text1Type = LexicalItemType.SURFACE;
						LexicalItemType text2Type = LexicalItemType.SURFACE;

						// measure for comparing semantic signatures
						SignatureComparison measure = new WeightedOverlap();

						for (String iterator : ADWSet) {
							if (iterator != null) {
								double similarity = pipeLine.getPairSimilarity(
										triple1, iterator,
										DisambiguationMethod.ALIGNMENT_BASED,
										measure, text1Type, text2Type);
								if (similarity >= 0.5) {
									System.setOut(outputPrintStream);

									System.out.println("\"" + lineNum + "\" "
											+ "\"" + similarity + "\" " + "\""
											+ PyramidDivided.get(iterator)
											+ "\" " + "\""
											+ Pyramid.get(iterator) + "\" ");
									System.setOut(outputPrintStream1);
									// System.out.println("\""+lineNum+"\" "+"\""+Pyramid.get(iterator)+"\"");
									System.out.println("\"" + lineNum + "\" "
											+ "\"" + triple1 + "\" " + "\""
											+ iterator + "\" " + "\""
											+ PyramidDivided.get(iterator)
											+ "\" " + "\"" + similarity + "\" "
											+ "\"" + Pyramid.get(iterator)
											+ "\"");
								}
							}
						}

					} else if (str1.startsWith("# Line")) {

						sentence1 = str1.replace("# Line", "ruthie-# Line");
						currentLineNum = Integer.parseInt(getNumbers(str1));
					}

				}
				br1.close();

			}
		}
	}
	public static String getNumbers(String content) {  
	       Pattern pattern = Pattern.compile("\\d+");  
	       Matcher matcher = pattern.matcher(content);  
	       while (matcher.find()) {  
	           return matcher.group(0);  
	       }  
	       return "";  
	   }  
}
