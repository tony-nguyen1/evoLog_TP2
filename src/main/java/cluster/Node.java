package cluster;

public class Node extends Cluster {
	public Double cpValue;
	
	public Node() {}

	public Node(Cluster c1, Cluster c2) {
		super.leftChild = c1;
		super.rightChild = c2;
	}
	
	public Node(Cluster c1, Cluster c2, String s, double cp) {
		super.leftChild = c1;
		super.rightChild = c2;
		super.name = s;
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
	
	public boolean check(double i) {
		return (this.cpValue >= i);
	}
}
