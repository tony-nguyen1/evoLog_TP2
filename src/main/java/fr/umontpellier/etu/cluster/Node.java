package fr.umontpellier.etu.cluster;

import org.jgrapht.graph.DefaultDirectedGraph;

import fr.umontpellier.etu.graph.MyEdge;

public class Node extends Cluster {
	public Double cpValue;

	private static int i = 0;

	public Node(Cluster c1, Cluster c2, double cp) {
		super.leftChild = c1;
		super.rightChild = c2;
		this.cpValue = cp;
	}

	public void setLeftChild(Cluster c) {
		super.leftChild = c;
	}

	public void setRightChild(Cluster c) {
		super.rightChild = c;
	}

	@Override
	public String toString() {
		return "Node [cpValue=" + cpValue + ", leftChild=" + leftChild + ", rightChild=" + rightChild + "]";
	}

	@Override
	public boolean check(double i) {
		return (this.cpValue >= i);
	}

	@Override
	public String makeDendo(DefaultDirectedGraph<String, MyEdge> g) {
		String nameNewVertex = String.valueOf(i);
		g.addVertex(nameNewVertex); i++;

		g.addEdge(nameNewVertex, leftChild.makeDendo(g));
		g.addEdge(nameNewVertex, rightChild.makeDendo(g));

		return nameNewVertex;
	}
}
