package fr.umontpellier.etu.graph;

import org.jgrapht.graph.DefaultWeightedEdge;

@SuppressWarnings("serial")
public class MyWeightedEdge extends DefaultWeightedEdge {

	private float wheight;

	/**
	 * @return the wheight
	 */
	public float getWheight() {
		return wheight;
	}

	/**
	 * @param wheight the wheight to set
	 */
	public void setWheight(float wheight) {
		this.wheight = wheight;
	}
	
	public String getSource() {
		return (String) super.getSource();
	}
	
	public String getTarget() {
		return (String) super.getTarget();
	}
	
	public Object getSourceObject() {
		return super.getSource();
	}
	
	public Object getTargetObject() {
		return super.getTarget();
	}
}
