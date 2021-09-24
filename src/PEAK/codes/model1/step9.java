/**
 * 
 */
package model1;

/**
 * @author Qian Yang
 * Purpose of This Class: Combine all the possible labels which come from one sentence
 * 						(via calculating the similarity score between two phrases)
 * Other Notes Relating to This Class (Optional):
 */

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
import java.util.Set;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.uniroma1.lcl.adw.ADW;
import it.uniroma1.lcl.adw.DisambiguationMethod;
import it.uniroma1.lcl.adw.LexicalItemType;
import it.uniroma1.lcl.adw.comparison.SignatureComparison;
import it.uniroma1.lcl.adw.comparison.WeightedOverlap;


public class step9 {
	public static void step9() throws IOException {
		ADW pipeLine = new ADW();

		double similarityThreshold = 0.8;

		File logFile = new File("step9-output-combineLog.txt");
		PrintStream outputPrintStream = new PrintStream(logFile);
		System.setOut(outputPrintStream);

		HashMap<String, Integer> allPhrases = new HashMap<String, Integer>();// all
																				// salient
																				// S/P/O
		HashMap<String, Integer> allPhrases1 = new HashMap<String, Integer>();
		HashMap<String, String> allPhrases2forContributor = new HashMap<String, String>();
		HashMap<String, String> allPhrases2forContributorTriple = new HashMap<String, String>();
		// HashMap<String,String> hashMapReverse = new HashMap<String,String>();

		
		FileReader reader = new FileReader(
				"step8-out-PyramidForCombination.txt");
		BufferedReader br = new BufferedReader(reader);
		String str = null;
		String nextStr = null;

		str = br.readLine();
		while (str != null) {
			if (str.startsWith("FROM")) {
				final DirectedWeightedMultigraph<String, RelationshipEdge> g = new DirectedWeightedMultigraph<String, RelationshipEdge>(
						RelationshipEdge.class);

				while ((str = br.readLine()) != null && !str.startsWith("FROM")) {
					String text = str;
					String[] splited = text.split("&");
					String twoPhrases = splited[3];

					String text1 = twoPhrases;
					String[] splited1 = text.split("\"");
					String triple1 = "*";// for the output
					for (String iterator1 : splited1) {
						triple1 = triple1.concat(" " + iterator1);
					}
					triple1 = triple1.replace("* ", "");
					g.addVertex(twoPhrases);
				}

				for (String vertexIterator1 : g.vertexSet()) {
					// g.removeVertex(vertexIterator1);

					for (String vertexIterator2 : g.vertexSet()) {
						if (!vertexIterator2.equals(vertexIterator1)) {
							if (vertexIterator1.contains(vertexIterator2)
									|| vertexIterator2
											.contains(vertexIterator1)) {
								System.setOut(outputPrintStream);
								System.out.println("&" + vertexIterator1 + "&"
										+ "   " + "&" + vertexIterator2 + "&"
										+ "   " + "&" + 1 + "&");
							} else {
								LexicalItemType text1Type = LexicalItemType.SURFACE;
								LexicalItemType text2Type = LexicalItemType.SURFACE;

								// measure for comparing semantic signatures
								SignatureComparison measure = new WeightedOverlap();

								// calculate the similarity of text1 and text2
								double similarity = pipeLine.getPairSimilarity(
										vertexIterator1, vertexIterator2,
										DisambiguationMethod.ALIGNMENT_BASED,
										measure, text1Type, text2Type);
								if (similarity >= similarityThreshold) {
									System.setOut(outputPrintStream);
									System.out.println("&" + vertexIterator1
											+ "&" + "   " + "&"
											+ vertexIterator2 + "&" + "   "
											+ "&" + similarity + "&");
								}
							}
						}// inner for
					}// if
				}// outer for

			}

		}

		br.close();

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
