package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jgraph.graph.Edge;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import cluster.Cluster;
import cluster.Leaf;
import cluster.Node;
import graph.AppGraph;
import graph.OpenDefaultWeightedEdge;
import graph.OpenEdge;
import step2.MethodDeclarationVisitor;
import step2.MethodInvocationVisitor;
import step2.VariableDeclarationFragmentVisitor;

/***
 *	Refactor this shit
 *1.Now, iterate throught the file w/ 1 single visitor
 *2. A single function for each visitor
 *3. A fct for each general things that i want to calculate
 *4. A fct to show it.
 *
 * 
 * @author tony
 *
 */
public class MyParser {
	
	static class CoupleNomData implements Comparable {
		
		private String nom;
		private int nb;
		
		

		public CoupleNomData(String nom, int nb) {
			super();
			this.nom = nom;
			this.nb = nb;
		}

		


		@Override
		public int hashCode() {
			return Objects.hash(nom);
		}




		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof CoupleNomData)) {
				return false;
			}
			CoupleNomData other = (CoupleNomData) obj;
			return Objects.equals(nom, other.nom);
		}




		@Override
		public int compareTo(Object o) {
			if (o instanceof CoupleNomData) {
				CoupleNomData tmp = (CoupleNomData) o;
				return tmp.nb - this.nb;
			}
			return 0;
		}
		
	}
	
	
	public static final String projectPath = "/home/tony/M2/evoLog/evoLog_TP2";
	public static final String projectSourcePath = projectPath + "/src/main/java/dummy";
	public static final String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre";
	public static final int X = 2;

	
	
	
	
	private ArrayList<File> javaFiles;
	private int nbFile;
	private int selectionSize;
	
	public MyParser(String pathToProject) {
		final File folder = new File(pathToProject);
		javaFiles = listJavaFilesForFolder(folder);
		nbFile = javaFiles.size();
		selectionSize = (int) Math.ceil((double)nbFile/10);
	}
	
	private Set<String> getClassNameSet() throws IOException {
		Set<String> classNameSet = new HashSet<String>();
		
		for (File fileEntry : javaFiles) {
			
			String content = FileUtils.readFileToString(fileEntry);
			CompilationUnit parse = parse(content.toCharArray());
			
			Graph1Visitor visitor = new Graph1Visitor();
			parse.accept(visitor);
			
			classNameSet.addAll(visitor.getClassNameSet());
		}
		
		return classNameSet;
	}
	
	public void compute() throws IOException {
		
		int nbClass = 0;
		int nbMethodTotal = 0;
		
		Set<String> packageNameSet = new HashSet<>();
		Set<String> classNameSet = new HashSet<String>();
		
//		Pour le graph
		ArrayList<DefaultDirectedGraph<String, OpenEdge>> listGraph = new ArrayList<DefaultDirectedGraph<String,OpenEdge>>();
		
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			CompilationUnit parse = parse(content.toCharArray());
			
			// compute and extract here
			MasterVisitor visitor = new MasterVisitor(parse);
			Graph1Visitor visitor2 = new Graph1Visitor();
			parse.accept(visitor);
			
			// pour le graph
			parse.accept(visitor2);
			DefaultDirectedGraph<String, OpenEdge> g = visitor2.getG();
			listGraph.add(g);

			
			nbClass += visitor.getTypes().size();
			
			int nbMethod= visitor.getNbMethod();
			nbMethodTotal += nbMethod;
			
			packageNameSet.add(visitor.getPackageName());
			
			
			
			classNameSet.addAll(visitor2.getClassNameSet());
		
//			printMethodInvocationInfo(parse);
			
		}

		// exploitation des infos
		System.out.println("Nombre de classes dans l'application: \n"+nbClass);
		System.out.println("Nombre de méthodes dans l'application: \n"+nbMethodTotal);
		System.out.println("Nombre de pacakge dans l'application: \n"+packageNameSet.size());
		
//		Fusioner les graphs entre eux
		DefaultDirectedGraph<String, OpenEdge> masterGraph = getGraphAppel();
		
		AppGraph.givenAdaptedGraph_whenWriteBufferedImage_thenFileShouldExist(masterGraph,"src/test/resources/","graphAppel.png");
		System.out.println("Nombre de relation: "+ masterGraph.edgeSet().size());
		
		
		SimpleWeightedGraph <String, OpenDefaultWeightedEdge> graphPondere = getGraphCouplage(classNameSet, masterGraph, nbMethodTotal);//new SimpleWeightedGraph<>(OpenDefaultWeightedEdge.class);
		AppGraph.showGraphPond(graphPondere,"src/test/resources/","graphPondereCouplageClass.png");

		
		//TODO récup le cluster puis faire l'arbre
		Pair<DefaultDirectedGraph<String, OpenEdge>, Cluster> result = clustering_hierarchique(masterGraph, nbMethodTotal);
		DefaultDirectedGraph<String, OpenEdge> dendo = result.getValue0();
		Cluster root = result.getValue1();
		System.out.println(root);
		AppGraph.givenAdaptedGraph_whenWriteBufferedImage_thenFileShouldExist(dendo,"src/test/resources/","dendogramme.png");
		
		getModule(0, dendo, root, nbClass);
	}
	
	public SimpleWeightedGraph <String, OpenDefaultWeightedEdge> getGraphCouplage(Set<String> classNameSet, DefaultDirectedGraph<String, OpenEdge> masterGraph, int nbMethodTotal) {
		SimpleWeightedGraph <String, OpenDefaultWeightedEdge> graphPondere = new SimpleWeightedGraph<>(OpenDefaultWeightedEdge.class);
		classNameSet.stream().forEach(aClassName -> graphPondere.addVertex(aClassName));
		for (String s : classNameSet) {
			for (String t : classNameSet) {
				if (!s.equals(t)) {
				
					float n = AppGraph.nbRelation(masterGraph, t, s);
					
					float nbTotal = nbMethodTotal*(nbMethodTotal-1);
					
					float res = n / nbTotal;
					
					if (n != 0) {
						OpenDefaultWeightedEdge e;
						if ((e = graphPondere.addEdge(s, t)) != null) {
						e.setWheight(res);
						}						
					}
				}				
			}
		}
		
		return graphPondere;		
	}
	
	public DefaultDirectedGraph<String, OpenEdge> getGraphAppel() throws IOException {
		ArrayList<DefaultDirectedGraph<String, OpenEdge>> listGraph = new ArrayList<DefaultDirectedGraph<String,OpenEdge>>();
				
		for (File fileEntry : javaFiles) {
			
			String content = FileUtils.readFileToString(fileEntry);
			CompilationUnit parse = parse(content.toCharArray());

			Graph1Visitor visitor = new Graph1Visitor();
			parse.accept(visitor);
			
			DefaultDirectedGraph<String, OpenEdge> g = visitor.getG();
			
			listGraph.add(g);
		}
		
//		Fusioner les graphs entre eux
		DefaultDirectedGraph<String, OpenEdge> masterGraph = new DefaultDirectedGraph<String, OpenEdge>(OpenEdge.class);
		for (DefaultDirectedGraph<String, OpenEdge> defaultDirectedGraph : listGraph) {
			for (String nomNoeud : defaultDirectedGraph.vertexSet()) {
				masterGraph.addVertex(nomNoeud);
			}
			
			for (OpenEdge e: defaultDirectedGraph.edgeSet()) {
				masterGraph.addEdge((String) e.getSource(), (String) e.getTarget(), e);
			}
		}
		
		return masterGraph;
	}
		
	// partitionnement de l'arbre
	public void getModule(double cp, DefaultDirectedGraph<String, OpenEdge> dendo, Cluster root, int nbClasses) {
		System.out.println("modulage");
		List<Cluster> listModule = new ArrayList<>();
//		System.out.println(root);
//		System.out.println();
		
		
//		ArrayList<String> a = dendo.vertexSet().stream().filter(v -> dendo.inDegreeOf(v) == 0).collect(Collectors.toCollection(ArrayList::new));
//		assert a.size() == 1;
		
		
		
		ArrayList<ArrayList<String>> resModule = new ArrayList<>();
		
		//Descendre dans le dendo depuis le noeud racine,
//		Si la cpValue du Noeud courant est inférieur à CP, on essaye de descendre,
		// quand le cp du noeud courant est supérieur, s'arrêter et partitioner en module
		
		
		ArrayList<Set<String>> result = new ArrayList<Set<String>>();
		aux(root, result);
		System.out.println(result);
	}
	
	private void aux(Cluster root, List<Set<String>> res) {
		Cluster current = root;
		
		if (current.check(0.3)) {
			System.out.println("succes");
			System.out.println(root);
			res.add(root.getNames());
		} else {
			aux(root.getLeftChild(), res);
			aux(root.getRightChild(), res);
		}
		
	}
		
	public Pair<DefaultDirectedGraph<String, OpenEdge>, Cluster> clustering_hierarchique(DefaultDirectedGraph<String, OpenEdge> g, int nbMethod) throws IOException {
		// l'arbre dendogramme qui représentes les diffférents clusters
		DefaultDirectedGraph<String, OpenEdge> dendroTree = new DefaultDirectedGraph<String, OpenEdge>(OpenEdge.class);
		// les différentes classes de l'application qu'on analyse
		ArrayList<String> classes;
		
		Set<String> classNameSet = getClassNameSet();
		ArrayList<Cluster> clusters = new ArrayList<>();
		classNameSet.stream().forEach(x -> {
			clusters.add(new Leaf(x));
			dendroTree.addVertex(x);
		});
		Node root = null;// = new Node();
		
		
		int i = 0;
		// while
		
		while (i < clusters.size()) {
			
			// calcul
			Triplet<Cluster, Cluster, Double> pair = Cluster.clusterProche(clusters, g, nbMethod);
			Cluster c1, c2;
			c1 = pair.getValue0();
			c2 = pair.getValue1();
			
			// ajout dans dendogramme
			Node n;
			n = new Node(c1, c2, String.valueOf(i), pair.getValue2());
			

			dendroTree.addVertex(String.valueOf(i));
			dendroTree.addEdge(String.valueOf(i), c1.getName());
			dendroTree.addEdge(String.valueOf(i), c2.getName());
			
			root = n;
			
			clusters.remove(c1);
			clusters.remove(c2);
			
			clusters.add(n);
			
			i++;
		}
		
		// link up the last two clusters
		
//		Pair<Cluster, Cluster> pair = Cluster.clusterProche(clusters, g);
//		
//		Cluster c1, c2;
//		c1 = pair.getValue0();
//		c2 = pair.getValue1();
//		
//		
//		// ajout dans dendogramme
//		
//		Node n;
//		n = new Node(c1, c2, String.valueOf(i));
//
//		dendroTree.addVertex(String.valueOf(i));
//		dendroTree.addEdge(String.valueOf(i), c1.getName());
//		dendroTree.addEdge(String.valueOf(i), c2.getName());
//		root = n;
//		
//		clusters.remove(c1);
//		clusters.remove(c2);
//		
//		clusters.add(n);
		
		return new Pair<>(dendroTree, root);
	}
	

	public static void main(String[] args) throws IOException {
		MyParser myParser = new MyParser("src/main/java/dummy");
		myParser.compute();
	}
	
	public static void printMethodInvocationInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {

			MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
			
			method.accept(visitor2);

			HashSet<String> setMethod = new HashSet();
			for (MethodInvocation methodInvocation : visitor2.getMethods()) {
				
				setMethod.add(methodInvocation.getName().getIdentifier());
			}
//			if (setMethod.isEmpty()) {
//				System.out.println(method.getName() + " n'appel pas de méthode");
//			} else {				
//				System.out.println(method.getName() + " appel : ");		
//				for (String s : setMethod) {
//					System.out.println("  "+s);					
//				}
//			}
		}
	}
	
	

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		parser.setBindingsRecovery(true);
 
		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
 
		parser.setUnitName("");
 
		String[] sources = { projectPath+"/src/main/java" }; // ICI très important
		String[] classpath = {jrePath};
 
		parser.setSource(classSource);
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		
		return (CompilationUnit) parser.createAST(null); // create and parse
	}
}
