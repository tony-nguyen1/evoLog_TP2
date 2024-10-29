package fr.umontpellier.etu.app;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.javatuples.Pair;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import fr.umontpellier.etu.cluster.Cluster;
import fr.umontpellier.etu.graph.ImgGraph;
import fr.umontpellier.etu.graph.MyEdge;
import fr.umontpellier.etu.graph.MyWeightedEdge;
import fr.umontpellier.etu.graph.UtilGraph;
import fr.umontpellier.etu.visitor.ClassVisitor;
import fr.umontpellier.etu.visitor.GraphVisitor;
import fr.umontpellier.etu.visitor.MethodCountVisitor;
import fr.umontpellier.etu.visitor.PackageVisitor;

public class MyParser {
	
	//// Attributs
	
	public final String projectPath;
	public final String projectSourcePath;
	public final String jrePath;

	private final ArrayList<File> javaFiles;


	
	// Constructeur
	
	public MyParser(String pathToProject, String projectPath, String projectSourcePath, String jrePath) {
		final File folder = new File(pathToProject);
		javaFiles = listJavaFilesForFolder(folder);
		
		this.projectPath = projectPath;
		this.projectSourcePath =  projectPath + projectSourcePath;
		this.jrePath = jrePath;
	}


	
	/***
	 * Parcours l'arbre a l'aide d'ASTVisitor et affiche les résultats calculés.
	 * 
	 * @throws IOException
	 */
	public void computeAndShow() throws IOException {
		// parcours de l'arbre
		int nbClass = this.getClassOfApplication().size();
		int nbMethodTotal = this.getNbMethodOfApplication();
		this.getGraphAppelOfApplication();
		int nbPackage = this.getPackageNameSet().size();
		Set<String> classNameSet = this.getClassNameSet();

		// affichage des infos
		System.out.println("Nombre de classes dans l'application: \n"+nbClass);
		System.out.println("Nombre de méthodes dans l'application: \n"+nbMethodTotal);
		System.out.println("Nombre de pacakge dans l'application: \n"+nbPackage);



		// graph
		DefaultDirectedGraph<String, MyEdge> masterGraph = getGraphAppelOfApplication();
		ImgGraph.writeDirectedGraphIntoImg(masterGraph,"src/test/resources/","graphAppelShort.png");

		ImgGraph.writeGraphIntoImg(masterGraph,"src/test/resources/","graphAppel.png");
		System.out.println("Nombre de relation: "+ masterGraph.edgeSet().size());


		SimpleWeightedGraph <String, MyWeightedEdge> graphPondere = UtilGraph.getGraphCouplage(classNameSet, masterGraph, nbMethodTotal);
		ImgGraph.showGraphPond(graphPondere,"src/test/resources/","graphPondereCouplageClass.png");


		//TODO récup le cluster puis faire l'arbre
		Pair<DefaultDirectedGraph<String, MyEdge>, Cluster> result = UtilGraph.clustering_hierarchique(masterGraph, nbMethodTotal, this.getClassNameSet());
		DefaultDirectedGraph<String, MyEdge> dendo = result.getValue0();
		Cluster root = result.getValue1();
		System.out.println(root);
		ImgGraph.writeGraphIntoImg(dendo,"src/test/resources/","dendogramme.png");

		ArrayList<Set<String>> res = UtilGraph.getModule(0, root, nbClass);
		System.out.println("Mes modules :\n" + res);
	}

	
	
	public DefaultDirectedGraph<String,MyEdge> getGraphAppelOfApplication() throws IOException {
		
		GraphVisitor v = new GraphVisitor();
		this.parseAll(v);

		return v.getG();
	}
	
	/***
	 * Méthode auxiliaire. Prend en paramètre un ASTVisitor et visite toutes les AST des fichiers du programmes analysés.
	 * 
	 * @param v une implémentation d'ASTVisitor
	 * @throws IOException
	 */
	private void parseAll(ASTVisitor v) throws IOException {
		
		// parcours total de la liste des fichiers de l'application
		for (File fileEntry : javaFiles) {

			// conversion d'un fichier source java en un String
			String content = FileUtils.readFileToString(fileEntry, Charset.forName("UTF-8"));
			// conversion du String en un AST
			CompilationUnit parse = parse(content.toCharArray());

			// visite d'un AST par le visiteur v
			parse.accept(v);

		}
	}

	public Set<String> getClassOfApplication() throws IOException {

		ClassVisitor visitorNbClass = new ClassVisitor();				
		this.parseAll(visitorNbClass);

		return visitorNbClass.getClassNameSet();
	}

	public int getNbMethodOfApplication() throws IOException {

		MethodCountVisitor methodCountVisitor = new MethodCountVisitor();
		this.parseAll(methodCountVisitor);

		return methodCountVisitor.getNbMethod();
	}
	
	private Set<String> getClassNameSet() throws IOException {
		
		ClassVisitor visitor = new ClassVisitor();
		this.parseAll(visitor);


		return visitor.getClassNameSet();
	}
	
	private Set<String> getPackageNameSet() throws IOException {

		PackageVisitor visitor = new PackageVisitor();
		this.parseAll(visitor);

		return visitor.getPackageName();
	}

	
	
	//// Méthode de graph
	
	
	
	//// Méthodes utiles
	
	/***
	 * read all java files from specific folder
	 * 
	 * @param folder
	 * @return
	 */
	private static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<>();
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
	
	/***
	 * 
	 * @param classSource
	 * @return
	 */
	private CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.setBindingsRecovery(true);

		@SuppressWarnings("unchecked")
		Hashtable<String, String> options = JavaCore.getOptions();
		parser.setCompilerOptions(options);

		parser.setUnitName("");

		String[] sources = { this.projectPath+"/src/main/java" }; // ICI très important
		String[] classpath = {this.jrePath};

		parser.setSource(classSource);
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);

		return (CompilationUnit) parser.createAST(null); // create and parse
	}
}
