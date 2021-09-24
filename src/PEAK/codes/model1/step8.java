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
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class step8 {
	public static void step8() throws IOException {

		File logFile = new File("step8-out-contributor.txt");
		PrintStream outputPrintStream = new PrintStream(logFile);
		System.setOut(outputPrintStream);

		File logFile2 = new File(
				"step8-out-PyramidForCombination.txt");
		PrintStream outputPrintStream2 = new PrintStream(logFile2);
		System.setOut(outputPrintStream2);

		HashMap<String, Integer> allPhrases = new HashMap<String, Integer>();// all
																				// hit
																				// S/P/O
		HashMap<String, Integer> allPhrases1 = new HashMap<String, Integer>();
		HashMap<String, String> allPhrases2forContributor = new HashMap<String, String>();
		HashMap<String, String> allPhrases2forContributorTriple = new HashMap<String, String>();
		// HashMap<String,String> hashMapReverse = new HashMap<String,String>();

		// add all hit phrases into a HashSet
		FileReader reader = new FileReader("step3-out.txt");
		BufferedReader br = new BufferedReader(reader);
		String str = null;

		final DirectedWeightedMultigraph<String, RelationshipEdge> g = new DirectedWeightedMultigraph<String, RelationshipEdge>(
				RelationshipEdge.class);

		while ((str = br.readLine()) != null) {
			allPhrases.put(str, 1);
			g.addVertex(str);
		}

		for (String vertexIterator1 : g.vertexSet()) {
			for (String vertexIterator2 : g.vertexSet()) {
				if (!vertexIterator1.equals(vertexIterator2)) {
					int weight = 0;
					String contributorTriple = "*";// for the output1
					String contributorSenten = "*";// for the output2

					// Find g1 for vertexIterator1, g2 for vertexIterator2, they
					// are all similarity class
					FileReader readerESet = new FileReader(
							"step2-out-SimilarityClassAfterAscending.txt");
					BufferedReader brESet = new BufferedReader(readerESet);
					String strESet = null;
					DirectedWeightedMultigraph<String, RelationshipEdge> g1 = new DirectedWeightedMultigraph<String, RelationshipEdge>(
							RelationshipEdge.class);
					DirectedWeightedMultigraph<String, RelationshipEdge> g2 = new DirectedWeightedMultigraph<String, RelationshipEdge>(
							RelationshipEdge.class);

					// add the element itself to the equivalent set
					g1.addVertex(vertexIterator1);
					g2.addVertex(vertexIterator2);

					while ((strESet = brESet.readLine()) != null) {
						if (strESet.startsWith(vertexIterator1))
							g1.addVertex(strESet.replaceFirst(vertexIterator1,
									""));
						else if (strESet.startsWith(vertexIterator2))
							g2.addVertex(strESet.replaceFirst(vertexIterator2,
									""));
					}
					brESet.close();

					// ////////ruthie
					FileReader reader1 = new FileReader("ruthie-out.txt");
					BufferedReader br1 = new BufferedReader(reader1);
					String str1 = null;
					String sentence1 = null;
					int flag1 = 1;
					while ((str1 = br1.readLine()) != null && flag1 == 1) {
						if (str1.equals("\n"))
							str1 = br1.readLine();
						else if (!str1.startsWith("#")) {
							String text1 = str1;
							String triple1 = "*";// for the output

							String[] splited1 = text1.split("\"");
							;
							// get the triple in a proper format (without "1. ")
							for (String iterator1 : splited1) {
								if (iterator1.matches("^[a-zA-Z].*$")) {
									triple1 = triple1.concat(" " + "\""
											+ iterator1 + "\"");
								}
							}
							triple1 = triple1.replace("* ", "");

							for (String vertexIteratorG1 : g1.vertexSet()) {
								if (flag1 == 1) {
									for (String vertexIteratorG2 : g2
											.vertexSet()) {
										if (flag1 == 1) {
											if (triple1
													.contains(vertexIteratorG1)
													&& triple1
															.contains(vertexIteratorG2)) {
												weight++;
												contributorTriple = contributorTriple
														.concat("#\"ruthie:\" "
																+ triple1
																+ "\n"
																+ "*"
																+ "\""
																+ vertexIteratorG1
																+ "\" \""
																+ vertexIteratorG2
																+ "\"" + "\n");
												contributorSenten = contributorSenten
														.concat("*Sen-ruthie: "
																+ sentence1
																+ "\n");
												flag1 = 0;
											}
										}
									}
								}
							}

						} else if (str1.startsWith("# Line")) {
							sentence1 = str1.replace("# Line",
									"FROM: ruthie-# Line");
						}
					}
					br1.close();

					// //////mary
					FileReader reader2 = new FileReader("mary-out.txt");
					BufferedReader br2 = new BufferedReader(reader2);
					String str2 = null;
					String sentence2 = null;
					int flag2 = 1;
					while ((str2 = br2.readLine()) != null && flag2 == 1) {
						if (str2.equals("\n"))
							str2 = br2.readLine();
						else if (!str2.startsWith("#")) {
							String text2 = str2;
							String triple2 = "*";// for the output

							String[] splited2 = text2.split("\"");
							;
							// get the triple in a proper format (without "1. ")
							for (String iterator2 : splited2) {
								if (iterator2.matches("^[a-zA-Z].*$")) {
									triple2 = triple2.concat(" " + "\""
											+ iterator2 + "\"");
								}
							}
							triple2 = triple2.replace("* ", "");

							for (String vertexIteratorG1 : g1.vertexSet()) {
								if (flag2 == 1) {
									for (String vertexIteratorG2 : g2
											.vertexSet()) {
										if (flag2 == 1) {
											if (triple2
													.contains(vertexIteratorG1)
													&& triple2
															.contains(vertexIteratorG2)) {
												weight++;
												contributorTriple = contributorTriple
														.concat("#\"mary:\" "
																+ triple2
																+ "\n"
																+ "*"
																+ "\""
																+ vertexIteratorG1
																+ "\" \""
																+ vertexIteratorG2
																+ "\"" + "\n");
												contributorSenten = contributorSenten
														.concat("*Sen-mary: "
																+ sentence2
																+ "\n");
												flag2 = 0;
											}
										}
									}
								}
							}

						} else if (str2.startsWith("# Line")) {
							sentence2 = str2.replace("# Line",
									"FROM: mary-# Line");
						}
					}
					br2.close();

					// /////marion
					FileReader reader3 = new FileReader("marion-out.txt");
					BufferedReader br3 = new BufferedReader(reader3);
					String str3 = null;
					String sentence3 = null;
					int flag3 = 1;
					while ((str3 = br3.readLine()) != null && flag3 == 1) {
						if (str3.equals("\n"))
							str3 = br3.readLine();
						else if (!str3.startsWith("#")) {
							String text3 = str3;
							String triple3 = "*";// for the output

							String[] splited3 = text3.split("\"");
							;
							// get the triple in a proper format (without "1. ")
							for (String iterator3 : splited3) {
								if (iterator3.matches("^[a-zA-Z].*$")) {
									triple3 = triple3.concat(" " + "\""
											+ iterator3 + "\"");
								}
							}
							triple3 = triple3.replace("* ", "");

							for (String vertexIteratorG1 : g1.vertexSet()) {
								if (flag3 == 1) {
									for (String vertexIteratorG2 : g2
											.vertexSet()) {
										if (flag3 == 1) {
											if (triple3
													.contains(vertexIteratorG1)
													&& triple3
															.contains(vertexIteratorG2)) {
												weight++;
												contributorTriple = contributorTriple
														.concat("#\"marion:\" "
																+ triple3
																+ "\n"
																+ "*"
																+ "\""
																+ vertexIteratorG1
																+ "\" \""
																+ vertexIteratorG2
																+ "\"" + "\n");
												contributorSenten = contributorSenten
														.concat("*Sen-marion: "
																+ sentence3
																+ "\n");
												flag3 = 0;
											}
										}
									}
								}
							}

						} else if (str3.startsWith("# Line")) {
							sentence3 = str3.replace("# Line",
									"FROM: marion-# Line");
						}
					}
					br3.close();
					// ////heather
					FileReader reader4 = new FileReader("heather-out.txt");
					BufferedReader br4 = new BufferedReader(reader4);
					String str4 = null;
					String sentence4 = null;
					int flag4 = 1;
					while ((str4 = br4.readLine()) != null && flag4 == 1) {
						if (str4.equals("\n"))
							str4 = br4.readLine();
						else if (!str4.startsWith("#")) {
							String text4 = str4;
							String triple4 = "*";// for the output

							String[] splited4 = text4.split("\"");
							;
							// get the triple in a proper format (without "1. ")
							for (String iterator4 : splited4) {
								if (iterator4.matches("^[a-zA-Z].*$")) {
									triple4 = triple4.concat(" " + "\""
											+ iterator4 + "\"");
								}
							}
							triple4 = triple4.replace("* ", "");

							for (String vertexIteratorG1 : g1.vertexSet()) {
								if (flag4 == 1) {
									for (String vertexIteratorG2 : g2
											.vertexSet()) {
										if (flag4 == 1) {
											if (triple4
													.contains(vertexIteratorG1)
													&& triple4
															.contains(vertexIteratorG2)) {
												weight++;
												contributorTriple = contributorTriple
														.concat("#\"heather:\" "
																+ triple4
																+ "\n"
																+ "*"
																+ "\""
																+ vertexIteratorG1
																+ "\" \""
																+ vertexIteratorG2
																+ "\"" + "\n");
												contributorSenten = contributorSenten
														.concat("*Sen-heather: "
																+ sentence4
																+ "\n");
												flag4 = 0;
											}
										}
									}
								}
							}

						} else if (str4.startsWith("# Line")) {
							sentence4 = str4.replace("# Line",
									"FROM: heather-# Line");
						}
					}
					br4.close();
					// ////ann
					FileReader reader5 = new FileReader("ann-out.txt");
					BufferedReader br5 = new BufferedReader(reader5);
					String str5 = null;
					String sentence5 = null;
					int flag5 = 1;
					while ((str5 = br5.readLine()) != null && flag5 == 1) {
						if (str5.equals("\n"))
							str5 = br5.readLine();
						else if (!str5.startsWith("#")) {
							String text5 = str5;
							String triple5 = "*";// for the output

							String[] splited5 = text5.split("\"");
							;
							// get the triple in a proper format (without "1. ")
							for (String iterator5 : splited5) {
								if (iterator5.matches("^[a-zA-Z].*$")) {
									triple5 = triple5.concat(" " + "\""
											+ iterator5 + "\"");
								}
							}
							triple5 = triple5.replace("* ", "");

							for (String vertexIteratorG1 : g1.vertexSet()) {
								if (flag5 == 1) {
									for (String vertexIteratorG2 : g2
											.vertexSet()) {
										if (flag5 == 1) {
											if (triple5
													.contains(vertexIteratorG1)
													&& triple5
															.contains(vertexIteratorG2)) {
												weight++;
												contributorTriple = contributorTriple
														.concat("#\"ann:\" "
																+ triple5
																+ "\n"
																+ "*"
																+ "\""
																+ vertexIteratorG1
																+ "\" \""
																+ vertexIteratorG2
																+ "\"" + "\n");
												contributorSenten = contributorSenten
														.concat("*Sen-ann: "
																+ sentence5
																+ "\n");
												flag5 = 0;
											}
										}
									}
								}
							}

						} else if (str5.startsWith("# Line")) {
							sentence5 = str5.replace("# Line",
									"FROM: ann-# Line");
						}
					}
					br5.close();

					allPhrases1.put("*\"" + vertexIterator1 + "\"" + " " + "\""
							+ vertexIterator2 + "\"" + " ", weight);
					allPhrases2forContributor.put("*\"" + vertexIterator1
							+ "\"" + " " + "\"" + vertexIterator2 + "\"" + " ",
							contributorSenten.replace("\\*", ""));
					allPhrases2forContributorTriple.put("*\"" + vertexIterator1
							+ "\"" + " " + "\"" + vertexIterator2 + "\"" + " ",
							contributorTriple.replace("\\*", ""));

					// System.out.println("***"+"\""+vertexIterator1+"\""+" "+"\""+vertexIterator2+"\""+" "+"\""+weight+"\"");
					System.setOut(outputPrintStream);
					System.out.println("\"" + vertexIterator1 + "\"" + " "
							+ "\"" + vertexIterator2 + "\"" + " " + "\""
							+ weight + "\"");
					System.out.println(contributorTriple.replace("\\*", ""));
				}
			}
		}

		// checking referenced files
		FileReader reader1 = new FileReader("ruthie-out.txt");
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

				String twoPhrases1 = "*";// for the weight

				String[] splited1 = text1.split("\"");
				// String lastIterator1 = null;
				for (String iterator1 : splited1) {
					if (iterator1.matches("^[a-zA-Z].*$")) {
						triple1 = triple1.concat(" " + "\"" + iterator1 + "\"");
						if (allPhrases.get(iterator1) != null) {
							hits++;
							twoPhrases1 = twoPhrases1.concat("\"" + iterator1
									+ "\"" + " ");
						}
					}
				}
				if (hits == 2) {
					System.setOut(outputPrintStream2);
					System.out.println("&" + twoPhrases1.replace("*", "") + "&"
							+ allPhrases1.get(twoPhrases1) + "&"
							+ triple1.replace("* ", "") + "&");

				} else if (hits == 3) {
					System.setOut(outputPrintStream2);
					String[] splited1Second = twoPhrases1.replace("*", "")
							.split("\"");
					/*
					 * for (String iteratorTest:splited1Second)
					 * System.out.println("yangcy"+iteratorTest);
					 */
					String string12 = "*\"" + splited1Second[1] + "\"" + " "
							+ "\"" + splited1Second[3] + "\"" + " ";
					String string13 = "*\"" + splited1Second[1] + "\"" + " "
							+ "\"" + splited1Second[5] + "\"" + " ";
					String string23 = "*\"" + splited1Second[3] + "\"" + " "
							+ "\"" + splited1Second[5] + "\"" + " ";
					String string21 = "*\"" + splited1Second[3] + "\"" + " "
							+ "\"" + splited1Second[1] + "\"" + " ";
					String string31 = "*\"" + splited1Second[5] + "\"" + " "
							+ "\"" + splited1Second[1] + "\"" + " ";
					String string32 = "*\"" + splited1Second[5] + "\"" + " "
							+ "\"" + splited1Second[3] + "\"" + " ";

					String highestString = null;
					int highest = 0;
					if (allPhrases1.get(string12) != null
							&& allPhrases1.get(string12) > highest) {
						twoPhrases1 = string12;
						highest = allPhrases1.get(string12);
					} else if (allPhrases1.get(string13) != null
							&& allPhrases1.get(string13) > highest) {
						twoPhrases1 = string13;
						highest = allPhrases1.get(string13);
					} else if (allPhrases1.get(string23) != null
							&& allPhrases1.get(string23) > highest) {
						twoPhrases1 = string23;
						highest = allPhrases1.get(string23);
					} else if (allPhrases1.get(string21) != null
							&& allPhrases1.get(string21) > highest) {
						twoPhrases1 = string21;
						highest = allPhrases1.get(string21);
					} else if (allPhrases1.get(string31) != null
							&& allPhrases1.get(string31) > highest) {
						twoPhrases1 = string31;
						highest = allPhrases1.get(string31);
					} else if (allPhrases1.get(string32) != null
							&& allPhrases1.get(string32) > highest) {
						twoPhrases1 = string32;
						highest = allPhrases1.get(string32);
					}
					System.setOut(outputPrintStream2);
					System.out.println("&" + twoPhrases1.replace("*", "") + "&"
							+ allPhrases1.get(twoPhrases1) + "&"
							+ triple1.replace("* ", "") + "&");
					
				}

			} else if (str1.startsWith("# Line")) {
				sentence1 = str1.replace("# Line", "FROM: ruthie-# Line");
				System.setOut(outputPrintStream2);
				System.out.println(sentence1);
			}
		}
		br1.close();

		// ================================================
		FileReader reader2 = new FileReader("ann-out.txt");
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

				String twoPhrases2 = "*";// for the weight

				String[] splited2 = text2.split("\"");
				;
				// String lastIterator1 = null;
				for (String iterator2 : splited2) {
					if (iterator2.matches("^[a-zA-Z].*$")) {
						triple2 = triple2.concat(" " + "\"" + iterator2 + "\"");
						if (allPhrases.get(iterator2) != null) {
							hits++;
							twoPhrases2 = twoPhrases2.concat("\"" + iterator2
									+ "\"" + " ");
						}
					}
				}
				if (hits == 2) {
					System.setOut(outputPrintStream2);
					System.out.println("&" + twoPhrases2.replace("*", "") + "&"
							+ allPhrases1.get(twoPhrases2) + "&"
							+ triple2.replace("* ", "") + "&");
				} else if (hits == 3) {
					System.setOut(outputPrintStream2);
					String[] splited1Second = twoPhrases2.replace("*", "")
							.split("\"");
					String string12 = "*\"" + splited1Second[1] + "\"" + " "
							+ "\"" + splited1Second[3] + "\"" + " ";
					String string13 = "*\"" + splited1Second[1] + "\"" + " "
							+ "\"" + splited1Second[5] + "\"" + " ";
					String string23 = "*\"" + splited1Second[3] + "\"" + " "
							+ "\"" + splited1Second[5] + "\"" + " ";
					String string21 = "*\"" + splited1Second[3] + "\"" + " "
							+ "\"" + splited1Second[1] + "\"" + " ";
					String string31 = "*\"" + splited1Second[5] + "\"" + " "
							+ "\"" + splited1Second[1] + "\"" + " ";
					String string32 = "*\"" + splited1Second[5] + "\"" + " "
							+ "\"" + splited1Second[3] + "\"" + " ";

					String highestString = null;
					int highest = 0;
					if (allPhrases1.get(string12) != null
							&& allPhrases1.get(string12) > highest) {
						twoPhrases2 = string12;
						highest = allPhrases1.get(string12);
					} else if (allPhrases1.get(string13) != null
							&& allPhrases1.get(string13) > highest) {
						twoPhrases2 = string13;
						highest = allPhrases1.get(string13);
					} else if (allPhrases1.get(string23) != null
							&& allPhrases1.get(string23) > highest) {
						twoPhrases2 = string23;
						highest = allPhrases1.get(string23);
					} else if (allPhrases1.get(string21) != null
							&& allPhrases1.get(string21) > highest) {
						twoPhrases2 = string21;
						highest = allPhrases1.get(string21);
					} else if (allPhrases1.get(string31) != null
							&& allPhrases1.get(string31) > highest) {
						twoPhrases2 = string31;
						highest = allPhrases1.get(string31);
					} else if (allPhrases1.get(string32) != null
							&& allPhrases1.get(string32) > highest) {
						twoPhrases2 = string32;
						highest = allPhrases1.get(string32);
					}
					System.setOut(outputPrintStream2);
					System.out.println("&" + twoPhrases2.replace("*", "") + "&"
							+ allPhrases1.get(twoPhrases2) + "&"
							+ triple2.replace("* ", "") + "&");
				}

			} else if (str2.startsWith("# Line")) {
				sentence2 = str2.replace("# Line", "FROM: ann-# Line");
				System.setOut(outputPrintStream2);
				System.out.println(sentence2);
			}
		}
		br2.close();

		// ================================================
		FileReader reader3 = new FileReader("marion-out.txt");
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

				String twoPhrases3 = "*";// for the weight

				String[] splited3 = text3.split("\"");
				;
				// String lastIterator1 = null;
				for (String iterator3 : splited3) {
					if (iterator3.matches("^[a-zA-Z].*$")) {
						triple3 = triple3.concat(" " + "\"" + iterator3 + "\"");
						if (allPhrases.get(iterator3) != null) {
							hits++;
							twoPhrases3 = twoPhrases3.concat("\"" + iterator3
									+ "\"" + " ");
						}
					}
				}
				if (hits == 2) {
					System.setOut(outputPrintStream2);
					System.out.println("&" + twoPhrases3.replace("*", "") + "&"
							+ allPhrases1.get(twoPhrases3) + "&"
							+ triple3.replace("* ", "") + "&");

				} else if (hits == 3) {
					System.setOut(outputPrintStream2);
					String[] splited1Second = twoPhrases3.replace("*", "")
							.split("\"");
					String string12 = "*\"" + splited1Second[1] + "\"" + " "
							+ "\"" + splited1Second[3] + "\"" + " ";
					String string13 = "*\"" + splited1Second[1] + "\"" + " "
							+ "\"" + splited1Second[5] + "\"" + " ";
					String string23 = "*\"" + splited1Second[3] + "\"" + " "
							+ "\"" + splited1Second[5] + "\"" + " ";
					String string21 = "*\"" + splited1Second[3] + "\"" + " "
							+ "\"" + splited1Second[1] + "\"" + " ";
					String string31 = "*\"" + splited1Second[5] + "\"" + " "
							+ "\"" + splited1Second[1] + "\"" + " ";
					String string32 = "*\"" + splited1Second[5] + "\"" + " "
							+ "\"" + splited1Second[3] + "\"" + " ";

					String highestString = null;
					int highest = 0;
					if (allPhrases1.get(string12) != null
							&& allPhrases1.get(string12) > highest) {
						twoPhrases3 = string12;
						highest = allPhrases1.get(string12);
					} else if (allPhrases1.get(string13) != null
							&& allPhrases1.get(string13) > highest) {
						twoPhrases3 = string13;
						highest = allPhrases1.get(string13);
					} else if (allPhrases1.get(string23) != null
							&& allPhrases1.get(string23) > highest) {
						twoPhrases3 = string23;
						highest = allPhrases1.get(string23);
					} else if (allPhrases1.get(string21) != null
							&& allPhrases1.get(string21) > highest) {
						twoPhrases3 = string21;
						highest = allPhrases1.get(string21);
					} else if (allPhrases1.get(string31) != null
							&& allPhrases1.get(string31) > highest) {
						twoPhrases3 = string31;
						highest = allPhrases1.get(string31);
					} else if (allPhrases1.get(string32) != null
							&& allPhrases1.get(string32) > highest) {
						twoPhrases3 = string32;
						highest = allPhrases1.get(string32);
					}
					System.setOut(outputPrintStream2);
					System.out.println("&" + twoPhrases3.replace("*", "") + "&"
							+ allPhrases1.get(twoPhrases3) + "&"
							+ triple3.replace("* ", "") + "&");

				}

			} else if (str3.startsWith("# Line")) {
				sentence3 = str3.replace("# Line", "FROM: marion-# Line");
				System.setOut(outputPrintStream2);
				System.out.println(sentence3);
			}
		}
		br3.close();

		// ================================================
		FileReader reader4 = new FileReader("mary-out.txt");
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

				String twoPhrases4 = "*";// for the weight

				String[] splited4 = text4.split("\"");
				;
				// String lastIterator1 = null;
				for (String iterator4 : splited4) {
					if (iterator4.matches("^[a-zA-Z].*$")) {
						triple4 = triple4.concat(" " + "\"" + iterator4 + "\"");
						if (allPhrases.get(iterator4) != null) {
							hits++;
							twoPhrases4 = twoPhrases4.concat("\"" + iterator4
									+ "\"" + " ");
						}
					}
				}
				if (hits == 2) {
					System.setOut(outputPrintStream2);
					System.out.println("&" + twoPhrases4.replace("*", "") + "&"
							+ allPhrases1.get(twoPhrases4) + "&"
							+ triple4.replace("* ", "") + "&");

				} else if (hits == 3) {
					System.setOut(outputPrintStream2);
					String[] splited1Second = twoPhrases4.replace("*", "")
							.split("\"");
					String string12 = "*\"" + splited1Second[1] + "\"" + " "
							+ "\"" + splited1Second[3] + "\"" + " ";
					String string13 = "*\"" + splited1Second[1] + "\"" + " "
							+ "\"" + splited1Second[5] + "\"" + " ";
					String string23 = "*\"" + splited1Second[3] + "\"" + " "
							+ "\"" + splited1Second[5] + "\"" + " ";
					String string21 = "*\"" + splited1Second[3] + "\"" + " "
							+ "\"" + splited1Second[1] + "\"" + " ";
					String string31 = "*\"" + splited1Second[5] + "\"" + " "
							+ "\"" + splited1Second[1] + "\"" + " ";
					String string32 = "*\"" + splited1Second[5] + "\"" + " "
							+ "\"" + splited1Second[3] + "\"" + " ";

					String highestString = null;
					int highest = 0;
					if (allPhrases1.get(string12) != null
							&& allPhrases1.get(string12) > highest) {
						twoPhrases4 = string12;
						highest = allPhrases1.get(string12);
					} else if (allPhrases1.get(string13) != null
							&& allPhrases1.get(string13) > highest) {
						twoPhrases4 = string13;
						highest = allPhrases1.get(string13);
					} else if (allPhrases1.get(string23) != null
							&& allPhrases1.get(string23) > highest) {
						twoPhrases4 = string23;
						highest = allPhrases1.get(string23);
					} else if (allPhrases1.get(string21) != null
							&& allPhrases1.get(string21) > highest) {
						twoPhrases4 = string21;
						highest = allPhrases1.get(string21);
					} else if (allPhrases1.get(string31) != null
							&& allPhrases1.get(string31) > highest) {
						twoPhrases4 = string31;
						highest = allPhrases1.get(string31);
					} else if (allPhrases1.get(string32) != null
							&& allPhrases1.get(string32) > highest) {
						twoPhrases4 = string32;
						highest = allPhrases1.get(string32);
					}
					System.setOut(outputPrintStream2);
					System.out.println("&" + twoPhrases4.replace("*", "") + "&"
							+ allPhrases1.get(twoPhrases4) + "&"
							+ triple4.replace("* ", "") + "&");
				}

			} else if (str4.startsWith("# Line")) {
				sentence4 = str4.replace("# Line", "FROM: mary-# Line");
				System.setOut(outputPrintStream2);
				System.out.println(sentence4);
			}
		}
		br4.close();

		// ================================================
		FileReader reader5 = new FileReader("heather-out.txt");
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

				String twoPhrases5 = "*";// for the weight

				String[] splited5 = text5.split("\"");
				;
				// String lastIterator1 = null;
				for (String iterator5 : splited5) {
					if (iterator5.matches("^[a-zA-Z].*$")) {
						triple5 = triple5.concat(" " + "\"" + iterator5 + "\"");
						if (allPhrases.get(iterator5) != null) {
							hits++;
							twoPhrases5 = twoPhrases5.concat("\"" + iterator5
									+ "\"" + " ");
						}
					}
				}
				if (hits == 2) {
					System.setOut(outputPrintStream2);
					System.out.println("&" + twoPhrases5.replace("*", "") + "&"
							+ allPhrases1.get(twoPhrases5) + "&"
							+ triple5.replace("* ", "") + "&");

				} else if (hits == 3) {
					System.setOut(outputPrintStream2);
					String[] splited1Second = twoPhrases5.replace("*", "")
							.split("\"");
					String string12 = "*\"" + splited1Second[1] + "\"" + " "
							+ "\"" + splited1Second[3] + "\"" + " ";
					String string13 = "*\"" + splited1Second[1] + "\"" + " "
							+ "\"" + splited1Second[5] + "\"" + " ";
					String string23 = "*\"" + splited1Second[3] + "\"" + " "
							+ "\"" + splited1Second[5] + "\"" + " ";
					String string21 = "*\"" + splited1Second[3] + "\"" + " "
							+ "\"" + splited1Second[1] + "\"" + " ";
					String string31 = "*\"" + splited1Second[5] + "\"" + " "
							+ "\"" + splited1Second[1] + "\"" + " ";
					String string32 = "*\"" + splited1Second[5] + "\"" + " "
							+ "\"" + splited1Second[3] + "\"" + " ";

					String highestString = null;
					int highest = 0;
					if (allPhrases1.get(string12) != null
							&& allPhrases1.get(string12) > highest) {
						twoPhrases5 = string12;
						highest = allPhrases1.get(string12);
					} else if (allPhrases1.get(string13) != null
							&& allPhrases1.get(string13) > highest) {
						twoPhrases5 = string13;
						highest = allPhrases1.get(string13);
					} else if (allPhrases1.get(string23) != null
							&& allPhrases1.get(string23) > highest) {
						twoPhrases5 = string23;
						highest = allPhrases1.get(string23);
					} else if (allPhrases1.get(string21) != null
							&& allPhrases1.get(string21) > highest) {
						twoPhrases5 = string21;
						highest = allPhrases1.get(string21);
					} else if (allPhrases1.get(string31) != null
							&& allPhrases1.get(string31) > highest) {
						twoPhrases5 = string31;
						highest = allPhrases1.get(string31);
					} else if (allPhrases1.get(string32) != null
							&& allPhrases1.get(string32) > highest) {
						twoPhrases5 = string32;
						highest = allPhrases1.get(string32);
					}
					System.setOut(outputPrintStream2);
					System.out.println("&" + twoPhrases5.replace("*", "") + "&"
							+ allPhrases1.get(twoPhrases5) + "&"
							+ triple5.replace("* ", "") + "&");

				}

			} else if (str5.startsWith("# Line")) {
				sentence5 = str5.replace("# Line", "FROM: heather-# Line");
				System.setOut(outputPrintStream2);
				System.out.println(sentence5);
			}
		}
		br5.close();

	}

	public static class RelationshipEdge<V> extends DefaultWeightedEdge {
		private V v1;
		private V v2;
		private String label;
		private double weight;

		public RelationshipEdge(V v1, V v2, String label, double weight) {
			this.v1 = v1;
			this.v2 = v2;
			this.label = label;
			this.weight = weight;

		}

		public V getV1() {
			return v1;
		}

		public V getV2() {
			return v2;
		}

		public String toString() {
			return label;
		}

		public double getWeight() {
			return weight;
		}
	}
}
