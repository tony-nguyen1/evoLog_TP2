package graph;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

import java.awt.image.BufferedImage;
import java.awt.Color;

/**
 * 
 * @author tony
 *
 *- path et nom de fichier
 *- ajouter les vertex à la main puis les edges avec leur poids
 *
 */

public class AppGraph {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Graph<String, DefaultEdge> g 
		  = new SimpleGraph<>(DefaultEdge.class);
		g.addVertex("v1");
		g.addVertex("v2");
		g.addEdge("v1","v2");
		
		
//		givenAdaptedGraph_whenWriteBufferedImage_thenFileShouldExist(createGraph());

	}
	
	public static DefaultDirectedGraph<String, DefaultEdge> createGraph() throws IOException {

	    File imgFile = new File("src/test/resources/graph.png");
	    imgFile.createNewFile();

	    DefaultDirectedGraph<String, DefaultEdge> g = 
	      new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

	    String x1 = "x1";
	    String x2 = "x2";
	    String x3 = "x3";

	    g.addVertex(x1);
	    g.addVertex(x2);
	    g.addVertex(x3);

	    g.addEdge(x1, x2);
	    g.addEdge(x2, x3);
	    g.addEdge(x3, x1);
	    
	    return g;
	}
	
	public static <T> void givenAdaptedGraph_whenWriteBufferedImage_thenFileShouldExist(Graph<String, T> g, String path, String nameFile) throws IOException { //FIXME

	    JGraphXAdapter<String, T> graphAdapter = 
	      new JGraphXAdapter<String, T>(g);
	    
	    //utiliser mxgraph ici, puis donner au layout
	    mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
	    layout.execute(graphAdapter.getDefaultParent());
	    
	    BufferedImage image = 
	      mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
	    File imgFile = new File(path+nameFile);
	    ImageIO.write(image, "PNG", imgFile);
	    
	    
//	 // Étape 1: Créer un graphe pondéré avec JGraphT
//        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
//
//        // Ajouter des sommets
//        graph.addVertex("A");
//        graph.addVertex("B");
//        graph.addVertex("C");
//
//        // Ajouter des arêtes avec poids
//        DefaultWeightedEdge edgeAB = graph.addEdge("A", "B");
//        graph.setEdgeWeight(edgeAB, 2.5);
//
//        DefaultWeightedEdge edgeBC = graph.addEdge("B", "C");
//        graph.setEdgeWeight(edgeBC, 1.0);
//
//        DefaultWeightedEdge edgeCA = graph.addEdge("C", "A");
//        graph.setEdgeWeight(edgeCA, 3.5);
//	    
//	    mxGraph mxGraph = new mxGraph();
//	    Object parent = mxGraph.getDefaultParent();
//	    mxGraph.getModel().beginUpdate();
//	    try {
//            // Créer les sommets dans JGraphX
//            Object v1 = mxGraph.insertVertex(parent, null, "A", 0, 0, 80, 30);
//            Object v2 = mxGraph.insertVertex(parent, null, "B", 0, 0, 80, 30);
//            Object v3 = mxGraph.insertVertex(parent, null, "C", 0, 0, 80, 30);
//            
//            // Ajouter les arêtes avec poids affichés automatiquement
//            mxGraph.insertEdge(parent, null, String.valueOf(graph.getEdgeWeight(edgeAB)), v1, v2);
//            mxGraph.insertEdge(parent, null, String.valueOf(graph.getEdgeWeight(edgeBC)), v2, v3);
//            mxGraph.insertEdge(parent, null, String.valueOf(graph.getEdgeWeight(edgeCA)), v3, v1);
//        } finally {
//            mxGraph.getModel().endUpdate();
//        }
//	    
//	 // Étape 3: Disposer automatiquement les sommets (ex: disposition circulaire)
//        mxCircleLayout layout2 = new mxCircleLayout(mxGraph);
//        layout2.execute(parent);
//        
//     // Étape 4: Afficher le graphe dans une fenêtre (optionnel)
//        JFrame frame = new JFrame();
//        mxGraphComponent graphComponent = new mxGraphComponent(mxGraph);
//        frame.getContentPane().add(graphComponent);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(600, 400);
//        frame.setVisible(true);
//
//        // Étape 5: Exporter l'image du graphe sans choisir de style pour les arêtes
//        image = mxCellRenderer.createBufferedImage(mxGraph, null, 1, Color.WHITE, true, null);
//        try {
//            ImageIO.write(image, "PNG", new File("graph_image_auto_style.png"));
//            System.out.println("Image exportée avec succès !");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


	}
	
	
	public static int nbRelation(DefaultDirectedGraph<String, OpenEdge> g, String A, String B) {
		int nbAppel = 0;
		
		nbAppel += g.edgeSet().stream().filter(e -> e.getClassNameSource().equals(B)).filter(e -> e.getClassNameTarget().equals(A)).count();
		nbAppel += g.edgeSet().stream().filter(e -> e.getClassNameSource().equals(A)).filter(e -> e.getClassNameTarget().equals(B)).count();
		
//		System.out.println("Number of call from " + A + " to " + B + ": " + g.edgeSet().stream().filter(e -> e.getClassNameSource().equals(B)).filter(e -> e.getClassNameTarget().equals(A)).count());
//		System.out.println("Number of call from " + B + " to " + A + ": " + g.edgeSet().stream().filter(e -> e.getClassNameSource().equals(A)).filter(e -> e.getClassNameTarget().equals(B)).count());
//		
		return nbAppel;
	}
	
	

}
