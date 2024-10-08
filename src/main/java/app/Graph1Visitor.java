package app;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Classe servant à construire graph d'appel des méthodes entre elles.
 * But : Trouver les méthodes des différentes classes (uniquement celles qui appartiennent aux projets) et où elles sont appelées.
 */
public class Graph1Visitor extends ASTVisitor {
	List<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
	List<MethodDeclaration> methodsDeclared = new ArrayList<MethodDeclaration>();
	List<MethodInvocation> methods = new ArrayList<MethodInvocation>();
	
	@Override
	public boolean visit(TypeDeclaration node) {
	    if (!node.isInterface()) { types.add(node); }
	    return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		methodsDeclared.add(node);
		return super.visit(node);
	}
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		return super.visit(node);
	}
	
	public List<TypeDeclaration> getTypes() {
		return types;
	}

}
