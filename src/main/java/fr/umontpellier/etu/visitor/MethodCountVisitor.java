package fr.umontpellier.etu.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;


public class MethodCountVisitor extends ASTVisitor {

	private int nbMethod = 0;

	@Override
    public boolean visit(MethodDeclaration node) {
    	this.nbMethod++;

		return super.visit(node);
    }

	/**
	 * @return the nbMethod
	 */
	public int getNbMethod() {
		return nbMethod;
	}

}
