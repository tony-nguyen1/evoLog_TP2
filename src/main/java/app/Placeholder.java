package app;

import java.util.ArrayList;

public class Placeholder {
	
	// nom de la classe qui contient method
	private String classHolder;
	
	// nom de la méthode avec signature
	private String methodName;
	
	// les methodes qui sont appelés par method
	private ArrayList<Placeholder> methodCalledList;

	public Placeholder() {
		super();
		this.methodCalledList = new ArrayList<>();
	}

	public Placeholder(String classHolder, String methodName) {
		this();
		this.classHolder = classHolder;
		this.methodName = methodName;
	}
	
	void add(Placeholder p) {
		methodCalledList.add(p);
	}

	@Override
	public String toString() {
		String s = classHolder + "::" + methodName + " calls : ";
		
		for (Placeholder p : this.methodCalledList) {
			s+= p.classHolder+"::"+p.methodName;
		}
		return s;
	}
}
