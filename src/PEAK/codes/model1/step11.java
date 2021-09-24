/**
 * 
 */
package model1;

/**
 * @author Qian Yang
 * Purpose of This Class:
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
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.alg.ConnectivityInspector;


import it.uniroma1.lcl.adw.ADW;

public class step11 {
	public static void step11() throws IOException {
		ADW pipeLine = new ADW();

		double similarityThreshold = 0.8;

		File logFile = new File("step11-output-labels.txt");
		PrintStream outputPrintStream = new PrintStream(logFile);
		System.setOut(outputPrintStream);

		final DirectedWeightedMultigraph<String, RelationshipEdge> g = new DirectedWeightedMultigraph<String, RelationshipEdge>(
				RelationshipEdge.class);
		final DirectedWeightedMultigraph<String, RelationshipEdge> g2 = new DirectedWeightedMultigraph<String, RelationshipEdge>(
				RelationshipEdge.class);

		HashMap<String, String> allPhrases = new HashMap<String, String>();// all
																			// hit
																			// S/P/O

		// maintain a weight hashSet
		FileReader reader1 = new FileReader(
				"step8-out-PyramidForCombination.txt");
		BufferedReader br1 = new BufferedReader(reader1);
		String str1 = null;

		str1 = br1.readLine();
		while (str1 != null) {
			if (str1.startsWith("FROM"))
				str1 = br1.readLine();
			else {
				String text1 = str1;
				String[] splited1 = text1.split("&");
				String vertex1 = splited1[3];
				String vertex2 = splited1[2];
				allPhrases.put(vertex1, vertex2);

				g2.addVertex(vertex1);

				str1 = br1.readLine();
			}

		}
		br1.close();

		// add all contributors into a graph
		FileReader reader = new FileReader(
				"step9-out-combineLog.txt");
		BufferedReader br = new BufferedReader(reader);
		String str = null;

		str = br.readLine();
		while (str != null) {
			String text = str;
			String[] splited = text.split("&");
			String vertex1 = splited[1];
			String vertex2 = splited[3];
			if (g.containsVertex(vertex1) && g.containsVertex(vertex2))
				;
			else {
				g.addVertex(vertex1);
				g.addVertex(vertex2);
				g.addEdge(vertex1, vertex2, new RelationshipEdge<String>(
						vertex1, vertex2, "any", 1));
			}
			str = br.readLine();
		}

		// start to calculate the similarity score between triples from
		// different sentences
		for (String vertexIterator2 : g2.vertexSet()) {
			if (g.containsVertex(vertexIterator2))
				;
			else {
				g.addVertex(vertexIterator2);
			}

		}
		for (String vertexIterator2 : g2.vertexSet()) {
			for (String vertexIterator1 : g.vertexSet()) {
				if (!vertexIterator2.equals(vertexIterator1)) {
					LexicalItemType text1Type = LexicalItemType.SURFACE;
					LexicalItemType text2Type = LexicalItemType.SURFACE;

					// measure for comparing semantic signatures
					SignatureComparison measure = new WeightedOverlap();

					// calculate the similarity of text1 and text2
					double similarity = pipeLine.getPairSimilarity(
							vertexIterator1, vertexIterator2,
							DisambiguationMethod.ALIGNMENT_BASED, measure,
							text1Type, text2Type);
					if (similarity >= similarityThreshold) {
						g.addEdge(vertexIterator1, vertexIterator2,
								new RelationshipEdge<String>(vertexIterator1,
										vertexIterator2, "any", 1));

					}
				}

			}
		}

		ConnectivityInspector<String, RelationshipEdge> ci = new ConnectivityInspector<String, RelationshipEdge>(
				g);

		for (Set<String> iteratorCi : ci.connectedSets()) {// Returns a list of
															// Set s, where each
															// set contains all
															// vertices that are
															// in the same
															// maximally
															// connected
															// component. All
															// graph vertices
															// occur in exactly
															// one set. For more
															// on maximally
															// connected
															// component
			System.out.print("&");
			int highestScore = 0;
			for (String subIteratorCi : iteratorCi) {
				System.out.print(subIteratorCi + "&");
				if (Integer.parseInt(allPhrases.get(subIteratorCi)) > highestScore)
					highestScore = Integer.parseInt(allPhrases
							.get(subIteratorCi));
			}
			System.out.println(highestScore + "&");
		}
		br.close();

		/*
		 * System.out.println(
		 * "=========================normal SCU=========================");
		 * for(String vertexIterator2:g2.vertexSet()){
		 * if(g.containsVertex(vertexIterator2)); else
		 * System.out.println("&"+vertexIterator2
		 * +"&"+allPhrases.get(vertexIterator2)+"&"); }
		 */

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
