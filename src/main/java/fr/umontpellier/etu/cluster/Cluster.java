package fr.umontpellier.etu.cluster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedGraph;

import fr.umontpellier.etu.graph.MyEdge;
import fr.umontpellier.etu.graph.UtilGraph;

public abstract class Cluster {
	protected Cluster leftChild, rightChild;



	/**
	 * @return the leftChild
	 */
	public Cluster getLeftChild() {
		return leftChild;
	}

	/**
	 * @return the rightChild
	 */
	public Cluster getRightChild() {
		return rightChild;
	}

	public abstract String makeDendo(DefaultDirectedGraph<String, MyEdge> g);

	/**
	 *
	 * Compare les clusters 2 à 2. Retourne les 2 clusters les plus couplé
	 *
	 *
	 * @param clusters
	 * @return
	 */
	public static Node clusterProche(
			ArrayList<Cluster> clusters, 
			DefaultDirectedGraph<String, MyEdge> g) {

		assert clusters.size() > 1;

		Set<String> setClassNameA, setClassNameB;
		Node bestCluster = null;
		int max = -1;

		for (Cluster c1 : clusters) {
			for (Cluster c2 : clusters) {
				if (c1 != c2) {
					setClassNameA = new HashSet<>();
					setClassNameB = new HashSet<>();

					//mettre les noms des classes du clusters c1 dans son set
					setClassNameA = c1.getNames();
					setClassNameB = c2.getNames();

					// comparer
					int nbRelation = UtilGraph.nbRelation(g, setClassNameA, setClassNameB);

					if (nbRelation > max) {
						max = nbRelation;
						bestCluster = new Node(c1, c2, nbRelation);
					}
				}
			}
		}

		return bestCluster;
	}

	public Set<String> getNames() {
		Set<String> s = new HashSet<>();

		s.addAll(leftChild.getNames());
		s.addAll(rightChild.getNames());


		return s;
	}

	@Override
	public String toString() {
		return "Cluster [leftChild=" + leftChild + ", rightChild=" + rightChild + "]";
	}


	/**
	 * Vérifie si le cluster this possède une valeur cp acceptable pour pouvoir les unifiés
	 * @param i
	 * @return
	 */
	public abstract boolean check(double i);
}
