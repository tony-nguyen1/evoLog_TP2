package fr.umontpellier.etu.cluster;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedGraph;

import fr.umontpellier.etu.graph.MyEdge;

public class Leaf extends Cluster {
	private String name;

	public Leaf(String name) {
		this.name = name;
	}
	@Override
	public Set<String> getNames() {
		Set<String> s = new HashSet<>();
		s.add(name);

		return s;
	}

	@Override
	public String toString() {
		return "Leaf [name=" + name + "]";
	}


	@Override
	public boolean check(double i) {
		return true;
	}

	@Override
	public String makeDendo(DefaultDirectedGraph<String, MyEdge> g) { g.addVertex(name); return name; }
}
