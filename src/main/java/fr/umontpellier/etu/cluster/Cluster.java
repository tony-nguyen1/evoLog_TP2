package fr.umontpellier.etu.cluster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.javatuples.Triplet;
import org.jgrapht.graph.DefaultDirectedGraph;

import fr.umontpellier.etu.graph.MyEdge;
import fr.umontpellier.etu.graph.UtilGraph;

public abstract class Cluster {
	protected Cluster precedent;
	protected Cluster leftChild, rightChild;
	protected String name;
	
	
	
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

	/**
	 * 
	 * Compare les clusters 2 à 2. Retourne les 2 clusters les plus couplé
	 * 
	 * 
	 * @param clusters
	 * @return
	 */
	public static Triplet<Cluster, Cluster, Double> clusterProche(ArrayList<Cluster> clusters, DefaultDirectedGraph<String, MyEdge> g, int nbMethod) {
		
		assert clusters.size() > 1;
		
		Set<String> setClassNameA, setClassNameB;
		Triplet<Cluster, Cluster, Double> best = new Triplet<Cluster, Cluster, Double>(clusters.get(0), clusters.get(1), (double) -1);
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
					int nbRelation = UtilGraph.clusterProche(g, setClassNameA, setClassNameB);
					
					if (nbRelation > max) {
						max = nbRelation;
						best = new Triplet<Cluster, Cluster, Double>(c1, c2, (double) ( (double) nbRelation / (double) nbMethod));
					}								
				}
			}
		}
//		System.out.println(best);
	
		return best;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	
	public abstract boolean check(double i);

//	/**
//	 * @param name the name to set
//	 */
//	public void setName(String name) {
//		this.name = name;
//	}
	
	
	
	
}
