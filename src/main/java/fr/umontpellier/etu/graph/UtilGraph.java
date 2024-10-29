package fr.umontpellier.etu.graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import fr.umontpellier.etu.cluster.Cluster;
import fr.umontpellier.etu.cluster.Leaf;
import fr.umontpellier.etu.cluster.Node;

public class UtilGraph {

	/**
	 * Calcul le nombre d'appel entre les classes de A et B.
	 */
	public static int nbRelation(DefaultDirectedGraph<String, MyEdge> g, String A, String B) { //TODO delete this & use clusterProche?
		int nbAppel = 0;

		nbAppel += g.edgeSet().stream().filter(e -> e.getClassNameSource().equals(B)).filter(e -> e.getClassNameTarget().equals(A)).count();
		nbAppel += g.edgeSet().stream().filter(e -> e.getClassNameSource().equals(A)).filter(e -> e.getClassNameTarget().equals(B)).count();

		return nbAppel;
	}


	/***
	 *
	 * Calcul le nombre d'appel entre les classes de A et B.
	 *
	 * @param g graph d'appel
	 * @param setA ensemble des noms des classes dans un 1st cluster
	 * @param setB ensemble des noms des classes dans un 2nd cluster
	 * @return int
	 */
	public static int nbRelation(DefaultDirectedGraph<String, MyEdge> g, Set<String> setA, Set<String> setB) {
		int nbAppel = 0;

		nbAppel += g.edgeSet().stream().filter(e -> setB.contains(e.getClassNameSource())).filter(e -> setA.contains(e.getClassNameTarget())).count();
		nbAppel += g.edgeSet().stream().filter(e -> setA.contains(e.getClassNameSource())).filter(e -> setB.contains(e.getClassNameTarget())).count();

		return nbAppel;
	}

	/***
	 *
	 * Create the dendrogramme from the graph of calls
	 *
	 * @param g
	 * @throws IOException
	 */
	public static Cluster clustering_hierarchique(DefaultDirectedGraph<String, MyEdge> g, Set<String> classNameSet) throws IOException {

		// l'arbre dendogramme qui représentes les diffférents clusters
		DefaultDirectedGraph<String, MyEdge> dendroTree = new DefaultDirectedGraph<>(MyEdge.class);
		ArrayList<Cluster> clusters = new ArrayList<>(); // au début, toutes les classes sont des clusters de 1


		classNameSet.stream().forEach(x -> {
			clusters.add(new Leaf(x));
			dendroTree.addVertex(x);
		});

		Node root = null;

		int i = 0;
		while (i < clusters.size()) {
			Node n = Cluster.clusterProche(clusters, g);

			clusters.remove(n.getLeftChild());
			clusters.remove(n.getRightChild());

			clusters.add(n);

			root = n;
			i++;
		}

		return root;//new Pair<>(dendroTree, root);
	}

	/**
	 * À partir de la structure composite Cluster, choisit le partionnement des classes en modules.
	 *
	 * @param cp pour couplage
	 * @param root
	 * @param nbClasses
	 * @return une liste d'ensemble. chaque ensemble représente un module
	 */
	public static ArrayList<Set<String>> getModule(double cp, Cluster root, int nbClasses) {
		//Descendre dans le dendo depuis le noeud racine,
//		Si la cpValue du Noeud courant est inférieur à CP, on essaye de descendre,
		// quand le cp du noeud courant est supérieur, s'arrêter et partitioner en module


		ArrayList<Set<String>> result = new ArrayList<>();
		aux_getModule(root, result);

		return result;
	}
	private static void aux_getModule(Cluster root, List<Set<String>> res) {
		Cluster current = root;

		if (current.check(2.5)) {
			res.add(root.getNames());
		} else {
			aux_getModule(root.getLeftChild(), res);
			aux_getModule(root.getRightChild(), res);
		}
	}

	/***
	 * Crée un graph pondéré représentant le couplage entre les classes de l'application
	 *
	 * @param classNameSet
	 * @param masterGraph
	 * @param nbMethodTotal
	 * @return
	 */
	public static SimpleWeightedGraph<String, MyWeightedEdge> getGraphCouplage(Set<String> classNameSet, DefaultDirectedGraph<String, MyEdge> masterGraph, int nbMethodTotal) {

		// instanciation d'un graph pondéré
		SimpleWeightedGraph <String, MyWeightedEdge> graphPondere =
				new SimpleWeightedGraph<>(MyWeightedEdge.class);

		// ajout des différentes classes de l'application
		classNameSet.stream().forEach(aClassName -> graphPondere.addVertex(aClassName));

		// ---- Produit cartésien de 2 ensembles privé des doublons
		for (String s : classNameSet) {
			for (String t : classNameSet) {
				if (!s.equals(t)) {
					// ----

					aux_createEdge(masterGraph, graphPondere, nbMethodTotal, s, t);
				}
			}
		}

		return graphPondere;
	}
	private static void aux_createEdge(
			DefaultDirectedGraph<String, MyEdge> masterGraph,
			SimpleWeightedGraph <String, MyWeightedEdge> graphPondere,
			int nbMethodTotal,
			String s,
			String t) {

		float n = UtilGraph.nbRelation(masterGraph, t, s);

		float nbTotal = nbMethodTotal*(nbMethodTotal-1);

		float res = n / nbTotal;

		if (n != 0) {
			MyWeightedEdge e = graphPondere.addEdge(s, t);
			if (e != null) { e.setWheight(res); }
		}

	}

}
