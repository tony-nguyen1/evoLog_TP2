package fr.umontpellier.etu.graph;

import org.jgrapht.graph.DefaultEdge;

@SuppressWarnings("serial")
public class MyEdge extends DefaultEdge {
	private String classNameSource;
	private String methodNameSource;
	private String classNameTarget;
	private String methodNameTarget;
	
	public String getSource() {
		return (String) super.getSource();
	}
	
	public String getTarget() {
		return (String) super.getTarget();
	}

	/**
	 * @return the classNameSource
	 */
	public String getClassNameSource() {
		return classNameSource;
	}

	/**
	 * @param classNameSource the classNameSource to set
	 */
	public void setClassNameSource(String classNameSource) {
		this.classNameSource = classNameSource;
	}

	/**
	 * @return the methodNameSource
	 */
	public String getMethodNameSource() {
		return methodNameSource;
	}

	/**
	 * @param methodNameSource the methodNameSource to set
	 */
	public void setMethodNameSource(String methodNameSource) {
		this.methodNameSource = methodNameSource;
	}

	/**
	 * @return the classTargetSource
	 */
	public String getClassNameTarget() {
		return classNameTarget;
	}

	/**
	 * @param classTargetSource the classTargetSource to set
	 */
	public void setClassTargetSource(String classNameTarget) {
		this.classNameTarget = classNameTarget;
	}

	/**
	 * @return the methodTargetSource
	 */
	public String getMethodNameTarget() {
		return methodNameTarget;
	}

	/**
	 * @param methodTargetSource the methodTargetSource to set
	 */
	public void setMethodNameTarget(String methodNameTarget) {
		this.methodNameTarget = methodNameTarget;
	}
	
	
}
