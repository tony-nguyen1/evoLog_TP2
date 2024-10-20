package cluster;

import java.util.HashSet;
import java.util.Set;

public class Leaf extends Cluster {

	public Leaf(String name) {
		super();
		super.name = name;
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
	
	
	public boolean check(double i) {
		return true;
	}
}
