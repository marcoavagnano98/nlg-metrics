/**
 * 
 */
package model1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.uniroma1.lcl.adw.ADW;
import it.uniroma1.lcl.adw.DisambiguationMethod;
import it.uniroma1.lcl.adw.LexicalItemType;
import it.uniroma1.lcl.adw.comparison.SignatureComparison;
import it.uniroma1.lcl.adw.comparison.WeightedOverlap;

/**
 * @author Qian Yang 
 * Purpose of This Class: Build a graph. Set the threshold = 0.5, add a
 *         directed edge only between those vertices whose edge's weight is
 *         above this threshold. 
 *  
 * Other Notes Relating to This Class (Optional):
 *        
 */
public class step1 {

	private static final int WeightIntraSentence = 1;
	static double threshold = 0.5;

	public static void step1() throws IOException {
		
		//Please replace these names of input files.
		String file1 = "input/ruthie-out.txt";
		String file2 = "input/ann-out.txt";
		String file3 = "input/marion-out.txt";
		String file4 = "input/mary-out.txt";
		String file5 = "input/heather-out.txt";
		
		ADW pipeLine = new ADW();

		// String path = "student-summaries-ngrams/";

		final DirectedWeightedMultigraph<String, RelationshipEdge> g = new DirectedWeightedMultigraph<String, RelationshipEdge>(
				RelationshipEdge.class);

		File logFile = new File("output/step1-out.txt");
		PrintStream outputPrintStream = new PrintStream(logFile);
		System.setOut(outputPrintStream);

		File logFile2 = new File("output/step1-out-afterHash.txt");
		PrintStream outputPrintStream2 = new PrintStream(logFile2);

		FileReader reader1 = new FileReader(file1);
		BufferedReader br1 = new BufferedReader(reader1);
		String str1 = null;

		// Read every line of one output.txt, compare every line with CUs and
		// print every line with the corresponding CU and the weight and the
		// highest score
		while ((str1 = br1.readLine()) != null) {
			if (str1.equals("\n"))
				str1 = br1.readLine();
			else if (!str1.startsWith("#")) {
				// the two lexical items
				String text1 = str1;
				String[] splited1 = text1.split("\"");

				String lastIterator1 = null;
				for (String iterator1 : splited1) {
					if (iterator1.matches("^[a-zA-Z].*$")) {
						if (lastIterator1 != null) {
							g.addVertex(iterator1);
							g.addEdge(lastIterator1, iterator1,
									new RelationshipEdge<String>(lastIterator1,
											iterator1, "intraSentence",
											WeightIntraSentence));
							lastIterator1 = iterator1;
						} else {
							g.addVertex(iterator1);
							lastIterator1 = iterator1;
						}

					}
				}
			}
		}
		br1.close();

		FileReader reader2 = new FileReader(file2);
		BufferedReader br2 = new BufferedReader(reader2);
		String str2 = null;

		// read every line of one output.txt, compare every line with CUs and
		// print every line with the corresponding CU and the weight and the
		// highest score
		while ((str2 = br2.readLine()) != null) {

			if (str2.equals("\n"))
				str2 = br2.readLine();
			else if (!str2.startsWith("#")) {

				// the two lexical items
				String text2 = str2;
				String[] splited2 = text2.split("\"");
				String lastIterator2 = null;
				for (String iterator2 : splited2) {
					if (iterator2.matches("^[a-zA-Z].*$")) {
						if (lastIterator2 != null) {
							g.addVertex(iterator2);
							g.addEdge(lastIterator2, iterator2,
									new RelationshipEdge<String>(lastIterator2,
											iterator2, "intraSentence",
											WeightIntraSentence));
							lastIterator2 = iterator2;
						} else {
							g.addVertex(iterator2);
							lastIterator2 = iterator2;
						}

					}
				}

			}// end of else
		}
		br2.close();

		FileReader reader3 = new FileReader(file3);
		BufferedReader br3 = new BufferedReader(reader3);
		String str3 = null;

		// read every line of one output.txt, compare every line with CUs and
		// print every line with the corresponding CU and the weight and the
		// highest score
		while ((str3 = br3.readLine()) != null) {

			if (str3.equals("\n"))
				str3 = br3.readLine();
			else if (!str3.startsWith("#")) {

				// the two lexical items
				String text3 = str3;
				String[] splited3 = text3.split("\"");
				String lastIterator3 = null;
				for (String iterator3 : splited3) {
					if (iterator3.matches("^[a-zA-Z].*$")) {
						if (lastIterator3 != null) {
							g.addVertex(iterator3);
							g.addEdge(lastIterator3, iterator3,
									new RelationshipEdge<String>(lastIterator3,
											iterator3, "intraSentence",
											WeightIntraSentence));
							lastIterator3 = iterator3;
						} else {
							g.addVertex(iterator3);
							lastIterator3 = iterator3;
						}

					}
				}

			}// end of else
		}
		br3.close();

		FileReader reader4 = new FileReader(file4);

		BufferedReader br4 = new BufferedReader(reader4);
		String str4 = null;

		// read every line of one output.txt, compare every line with CUs and
		// print every line with the corresponding CU and the weight and the
		// highest score
		while ((str4 = br4.readLine()) != null) {

			if (str4.equals("\n"))
				str4 = br4.readLine();
			else if (!str4.startsWith("#")) {

				// the two lexical items
				String text4 = str4;
				String[] splited4 = text4.split("\"");
				String lastIterator4 = null;
				for (String iterator4 : splited4) {
					if (iterator4.matches("^[a-zA-Z].*$")) {
						if (lastIterator4 != null) {
							g.addVertex(iterator4);
							g.addEdge(lastIterator4, iterator4,
									new RelationshipEdge<String>(lastIterator4,
											iterator4, "intraSentence",
											WeightIntraSentence));
							lastIterator4 = iterator4;
						} else {
							g.addVertex(iterator4);
							lastIterator4 = iterator4;
						}

					}
				}

			}// end of else
		}
		br4.close();

		FileReader reader5 = new FileReader(file5);

		BufferedReader br5 = new BufferedReader(reader5);
		String str5 = null;

		// read every line of one output.txt, compare every line with CUs and
		// print every line with the corresponding CU and the weight and the
		// highest score
		while ((str5 = br5.readLine()) != null) {

			if (str5.equals("\n"))
				str5 = br5.readLine();
			else if (!str5.startsWith("#")) {

				// the two lexical items
				String text5 = str5;
				String[] splited5 = text5.split("\"");
				String lastIterator5 = null;
				for (String iterator5 : splited5) {
					if (iterator5.matches("^[a-zA-Z].*$")) {
						if (lastIterator5 != null) {
							g.addVertex(iterator5);
							g.addEdge(lastIterator5, iterator5,
									new RelationshipEdge<String>(lastIterator5,
											iterator5, "intraSentence",
											WeightIntraSentence));
							lastIterator5 = iterator5;
						} else {
							g.addVertex(iterator5);
							lastIterator5 = iterator5;
						}

					}
				}

			}// end of else
		}
		br5.close();

		// Keep a HashSet to avoid the duplication
		Set<java.lang.String> allNodes = new HashSet<java.lang.String>();

		for (String vertexIterator1 : g.vertexSet()) {
			for (String vertexIterator2 : g.vertexSet()) {
				if (!vertexIterator1.equals(vertexIterator2)) {
					LexicalItemType text1Type = LexicalItemType.SURFACE;
					LexicalItemType text2Type = LexicalItemType.SURFACE;

					SignatureComparison measure = new WeightedOverlap();

					// calculate the similarity of text1 and text2
					double similarity = pipeLine.getPairSimilarity(
							vertexIterator1, vertexIterator2,
							DisambiguationMethod.ALIGNMENT_BASED, measure,
							text1Type, text2Type);
					if (similarity >= threshold) {
						System.setOut(outputPrintStream);
						System.out.println("\"" + vertexIterator1 + "\""
								+ "   " + "\"" + vertexIterator2 + "\"" + "   "
								+ "\"" + similarity + "\"");
						g.addEdge(vertexIterator1, vertexIterator2,
								new RelationshipEdge<String>(vertexIterator1,
										vertexIterator2, "interSentence",
										similarity));
						allNodes.add(vertexIterator1);
					}
				}

			}
		}

		// Use a HashMap to map our vertices into integers
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		HashMap<Integer, String> hashMapReverse = new HashMap<Integer, String>();

		File logFileForHashMapReverseOriginal = new File("step1-out-hashMapReverse.db");
		PrintStream logPrintStreamForHashMapReverseOriginal = new PrintStream(
				logFileForHashMapReverseOriginal);
		System.setOut(logPrintStreamForHashMapReverseOriginal);

		int integerOfHashMap = 0;
		for (String vertex : allNodes) {
			// Put elements to the hashMap
			hashMap.put(vertex, new Integer(integerOfHashMap));
			hashMapReverse.put(integerOfHashMap, vertex);
			System.out.printf(integerOfHashMap + "\n"
					+ hashMapReverse.get(integerOfHashMap) + "\n");
			integerOfHashMap++;
		}

		// preProcessing for hierarchical-clustering
		for (String vertexIterator1 : g.vertexSet()) {
			for (String vertexIterator2 : g.vertexSet()) {
				if (!vertexIterator1.equals(vertexIterator2)) {
					LexicalItemType text1Type = LexicalItemType.SURFACE;
					LexicalItemType text2Type = LexicalItemType.SURFACE;

					// measure for comparing semantic signatures
					SignatureComparison measure = new WeightedOverlap();

					// calculate the similarity of text1 and text2
					double similarity = pipeLine.getPairSimilarity(
							vertexIterator1, vertexIterator2,
							DisambiguationMethod.ALIGNMENT_BASED, measure,
							text1Type, text2Type);
					if (similarity >= 0.5) {
						System.setOut(outputPrintStream2);
						System.out.println("\"" + hashMap.get(vertexIterator1)
								+ "\"" + "   " + "\""
								+ hashMap.get(vertexIterator1) + "\"" + "   "
								+ "\"" + similarity + "\"");
						// g.addEdge(vertexIterator1, vertexIterator2, new
						// RelationshipEdge<String>(vertexIterator1,
						// vertexIterator2,"interSentence",similarity));

					}
				}

			}
		}

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
