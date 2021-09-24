/**
 * 
 */
package model1;

/**
 * @author Qian Yang
 * Purpose of This Class:
 * Other Notes Relating to This Class (Optional):
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Set;

public class step6 {
	public static void step6() throws IOException {
		
		//Please replace these names of input files.
		String file1 = "input/ruthie-out.txt";
		String file2 = "input/ann-out.txt";
		String file3 = "input/marion-out.txt";
		String file4 = "input/mary-out.txt";
		String file5 = "input/heather-out.txt";

		File logFile = new File("step6-out.txt");
		PrintStream outputPrintStream = new PrintStream(logFile);
		System.setOut(outputPrintStream);

		HashMap<String, Integer> allPhrases = new HashMap<String, Integer>();
		// HashMap<String,String> hashMapReverse = new HashMap<String,String>();

		// add all salient nodes into a HashSet
		FileReader reader = new FileReader("step3-out.txt");
		BufferedReader br = new BufferedReader(reader);
		String str = null;
		while ((str = br.readLine()) != null) {
			allPhrases.put(str, 1);
		}

		// checking referenced files
		FileReader reader1 = new FileReader(file1);
		BufferedReader br1 = new BufferedReader(reader1);
		String str1 = null;
		String sentence1 = null;
		while ((str1 = br1.readLine()) != null) {
			if (str1.equals("\n"))
				str1 = br1.readLine();
			else if (!str1.startsWith("#")) {
				int hits = 0;
				String text1 = str1;
				String triple1 = "*";// for the output
				String hitAhitB1 = "*";// for the output

				String[] splited1 = text1.split("\"");
				;
				// String lastIterator1 = null;
				for (String iterator1 : splited1) {
					if (iterator1.matches("^[a-zA-Z].*$")) {
						triple1 = triple1.concat(" " + iterator1);
						if (allPhrases.get(iterator1) != null) {
							hits++;
							hitAhitB1 = hitAhitB1.concat(" " + "\"" + iterator1
									+ "\"");
						}
					}
				}
				if (hits >= 2) {// if at least two parts of a triple is hitted,
								// then ouput this triple

					System.out.println(hitAhitB1.replace("* ", "#") + "\n"
							+ triple1.replace("* ", "") + "\n" + sentence1
							+ "\n");
				}

			} else if (str1.startsWith("# Line")) {
				sentence1 = str1.replace("# Line", "FROM: ruthie-# Line");
			}
		}
		br1.close();

		// ================================================
		FileReader reader2 = new FileReader(file2);
		BufferedReader br2 = new BufferedReader(reader2);
		String str2 = null;
		String sentence2 = null;
		while ((str2 = br2.readLine()) != null) {
			if (str2.equals("\n"))
				str2 = br2.readLine();
			else if (!str2.startsWith("#")) {
				int hits = 0;
				String text2 = str2;
				String triple2 = "*";// for the output
				String hitAhitB2 = "*";// for the output

				String[] splited2 = text2.split("\"");
				;
				// String lastIterator1 = null;
				for (String iterator2 : splited2) {
					if (iterator2.matches("^[a-zA-Z].*$")) {
						triple2 = triple2.concat(" " + iterator2);
						if (allPhrases.get(iterator2) != null) {
							hits++;
							hitAhitB2 = hitAhitB2.concat(" " + "\"" + iterator2
									+ "\"");
						}
					}
				}
				if (hits >= 2) {

					System.out.println(hitAhitB2.replace("* ", "#") + "\n"
							+ triple2.replace("* ", "") + "\n" + sentence2
							+ "\n");
				}

			} else if (str2.startsWith("# Line")) {
				sentence2 = str2.replace("# Line", "FROM: ann-# Line");
			}
		}
		br2.close();

		// ================================================
		FileReader reader3 = new FileReader(file3);
		BufferedReader br3 = new BufferedReader(reader3);
		String str3 = null;
		String sentence3 = null;
		while ((str3 = br3.readLine()) != null) {
			if (str3.equals("\n"))
				str3 = br3.readLine();
			else if (!str3.startsWith("#")) {
				int hits = 0;
				String text3 = str3;
				String triple3 = "*";// for the output
				String hitAhitB3 = "*";// for the output

				String[] splited3 = text3.split("\"");
				;
				// String lastIterator1 = null;
				for (String iterator3 : splited3) {
					if (iterator3.matches("^[a-zA-Z].*$")) {
						triple3 = triple3.concat(" " + iterator3);
						if (allPhrases.get(iterator3) != null) {
							hits++;
							hitAhitB3 = hitAhitB3.concat(" " + "\"" + iterator3
									+ "\"");
						}
					}
				}
				if (hits >= 2) {

					System.out.println(hitAhitB3.replace("* ", "#") + "\n"
							+ triple3.replace("* ", "") + "\n" + sentence3
							+ "\n");
				}

			} else if (str3.startsWith("# Line")) {
				sentence3 = str3.replace("# Line", "FROM: marion-# Line");
			}
		}
		br3.close();

		// ================================================
		FileReader reader4 = new FileReader(file4);
		BufferedReader br4 = new BufferedReader(reader4);
		String str4 = null;
		String sentence4 = null;
		while ((str4 = br4.readLine()) != null) {
			if (str4.equals("\n"))
				str4 = br4.readLine();
			else if (!str4.startsWith("#")) {
				int hits = 0;
				String text4 = str4;
				String triple4 = "*";// for the output
				String hitAhitB4 = "*";// for the output

				String[] splited4 = text4.split("\"");
				;
				// String lastIterator1 = null;
				for (String iterator4 : splited4) {
					if (iterator4.matches("^[a-zA-Z].*$")) {
						triple4 = triple4.concat(" " + iterator4);
						if (allPhrases.get(iterator4) != null) {
							hits++;
							hitAhitB4 = hitAhitB4.concat(" " + "\"" + iterator4
									+ "\"");
						}
					}
				}
				if (hits >= 2) {

					System.out.println(hitAhitB4.replace("* ", "#") + "\n"
							+ triple4.replace("* ", "") + "\n" + sentence4
							+ "\n");
				}

			} else if (str4.startsWith("# Line")) {
				sentence4 = str4.replace("# Line", "FROM: mary-# Line");
			}
		}
		br4.close();

		// ================================================
		FileReader reader5 = new FileReader(file5);
		BufferedReader br5 = new BufferedReader(reader5);
		String str5 = null;
		String sentence5 = null;
		while ((str5 = br5.readLine()) != null) {
			if (str5.equals("\n"))
				str5 = br5.readLine();
			else if (!str5.startsWith("#")) {
				int hits = 0;
				String text5 = str5;
				String triple5 = "*";// for the output
				String hitAhitB5 = "*";// for the output

				String[] splited5 = text5.split("\"");
				;
				// String lastIterator1 = null;
				for (String iterator5 : splited5) {
					if (iterator5.matches("^[a-zA-Z].*$")) {
						triple5 = triple5.concat(" " + iterator5);
						if (allPhrases.get(iterator5) != null) {
							hits++;
							hitAhitB5 = hitAhitB5.concat(" " + "\"" + iterator5
									+ "\"");
						}
					}
				}
				if (hits >= 2) {

					System.out.println(hitAhitB5.replace("* ", "#") + "\n"
							+ triple5.replace("* ", "") + "\n" + sentence5
							+ "\n");
				}

			} else if (str5.startsWith("# Line")) {
				sentence5 = str5.replace("# Line", "FROM: heather-# Line");
			}
		}
		br5.close();

	}

}
