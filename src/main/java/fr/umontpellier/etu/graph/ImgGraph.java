package fr.umontpellier.etu.graph;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

public class ImgGraph {

	public static void showGraphPond(SimpleWeightedGraph<String, MyWeightedEdge> g, String path, String nameFile) throws IOException {
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
		    mxGraphLayout layout2 = new mxCircleLayout(mxGraph);
		    layout2.execute(parent);


		    aux_writeIntoFileTheImg(layout2.getGraph(), path, nameFile);

		}


	/***
	 *
	 *
	 * @param g
	 * @param path
	 * @param nameFile
	 * @throws IOException
	 */
	public static void writeGraphIntoImgTopToBottomNoLabel(DefaultDirectedGraph<String, MyEdge> g, String path, String nameFile) throws IOException { // TODO rename

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
	    mxGraphLayout layout2 = new mxHierarchicalLayout(mxGraph);
	    layout2.execute(parent);

	    aux_writeIntoFileTheImg(layout2.getGraph(), path, nameFile);

	}

	private static void aux_writeIntoFileTheImg(mxGraph layout, String path, String nameFile) throws IOException {
		// Étape 5: Exporter l'image du graphe sans choisir de style pour les arêtes
	    BufferedImage image = mxCellRenderer.createBufferedImage(layout, null, 1, Color.LIGHT_GRAY, true, null);

    	ImageIO.write(image, "PNG", new File(path+nameFile));
    	System.out.println("Image exportée avec succès !");
	}


}
