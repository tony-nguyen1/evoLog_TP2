package graph;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

public class ImgGraph {

	public static void showGraphPond(SimpleWeightedGraph<String, MyWeightedEdge> g, String path, String nameFile) {
			HashMap<String, Object> map = new HashMap<>();
	
			mxGraph mxGraph = new mxGraph();
		    Object parent = mxGraph.getDefaultParent();
		    mxGraph.getModel().beginUpdate();
		    try {
	            Object o;
	            for (String s : g.vertexSet()) {
					o = mxGraph.insertVertex(parent, null, s, 0, 0, 80, 30);
					map.put(s, o);
				}
	
	            for (MyWeightedEdge e : g.edgeSet()) {
	            	mxGraph.insertEdge(parent, null, String.valueOf(e.getWheight()), map.get(e.getSource()), map.get(e.getTarget()));
	            }
	        } finally {
	            mxGraph.getModel().endUpdate();
	        }
	
	
		//	  Étape 3: Disposer automatiquement les sommets (ex: disposition circulaire)
		    mxCircleLayout layout2 = new mxCircleLayout(mxGraph);
		    layout2.execute(parent);
	
		   // Étape 4: Afficher le graphe dans une fenêtre (optionnel)
		  JFrame frame = new JFrame();
		  mxGraphComponent graphComponent = new mxGraphComponent(mxGraph);
		  frame.getContentPane().add(graphComponent);
		  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		  frame.setSize(600, 400);
	//	  frame.setVisible(true);
	
		  // Étape 5: Exporter l'image du graphe sans choisir de style pour les arêtes
		  BufferedImage image = mxCellRenderer.createBufferedImage(mxGraph, null, 1, Color.WHITE, true, null);
		  try {
		      ImageIO.write(image, "PNG", new File(path+nameFile));
		  System.out.println("Image exportée avec succès !");
		  } catch (IOException e) {
		      e.printStackTrace();
		  }
	
		}

	public static <T> void writeGraphIntoImg(Graph<String, T> g, String path, String nameFile) throws IOException { // TODO rename
	
	    JGraphXAdapter<String, T> graphAdapter =
	      new JGraphXAdapter<>(g);
	
	    //utiliser mxgraph ici, puis donner au layout
	    mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
	    layout.execute(graphAdapter.getDefaultParent());
	
	    BufferedImage image =
	      mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
	    File imgFile = new File(path+nameFile);
	    ImageIO.write(image, "PNG", imgFile);
	}

	/***
	 * Crée une image d'un graph dirigé sans étiquettes sur disque à partir d'un graph représentant les appels entre méthodes de l'application analysé.
	 *
	 * @param g graph d'appel
	 * @param path
	 * @param nameFile
	 */
	public static void writeDirectedGraphIntoImg(DefaultDirectedGraph<String, MyEdge> g, String path, String nameFile) {
		HashMap<String, Object> map = new HashMap<>();
	
		mxGraph mxGraph = new mxGraph();
	    Object parent = mxGraph.getDefaultParent();
	    mxGraph.getModel().beginUpdate();
	    try {
	        Object o;
	        for (String s : g.vertexSet()) {
				o = mxGraph.insertVertex(parent, null, s, 0, 0, 80, 30);
				map.put(s, o);
			}
	
	        for (MyEdge e : g.edgeSet()) {
	        	mxGraph.insertEdge(parent, null, "", map.get(e.getSource()), map.get(e.getTarget()));
	        }
	    } finally {
	        mxGraph.getModel().endUpdate();
	    }
	
	
	    //	  Étape 3: Disposer automatiquement les sommets (ex: disposition circulaire)
	    mxCircleLayout layout2 = new mxCircleLayout(mxGraph);
	    layout2.execute(parent);
	
	    // Étape 4: Afficher le graphe dans une fenêtre (optionnel)
	    JFrame frame = new JFrame();
	    mxGraphComponent graphComponent = new mxGraphComponent(mxGraph);
	    frame.getContentPane().add(graphComponent);
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    frame.setSize(600, 400);
		//	  frame.setVisible(true);
	
	    // Étape 5: Exporter l'image du graphe sans choisir de style pour les arêtes
	    BufferedImage image = mxCellRenderer.createBufferedImage(mxGraph, null, 1, Color.WHITE, true, null);
	    try {
	    	ImageIO.write(image, "PNG", new File(path+nameFile));
	    	System.out.println("Image exportée avec succès !");
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}

}
