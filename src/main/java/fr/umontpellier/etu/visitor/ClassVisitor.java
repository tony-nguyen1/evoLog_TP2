package fr.umontpellier.etu.visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassVisitor extends ASTVisitor {


	private String currentPackage = "";
	private Set<String> classNameSet = new HashSet<>();

	@Override
    public boolean visit(TypeDeclaration node) {
    	String nomClass = node.getName().getIdentifier();
    	String nomComplet = currentPackage+"."+nomClass;

        if (!node.isInterface()) { classNameSet.add(nomComplet); }

        return super.visit(node);
    }

	@Override
    public boolean visit(PackageDeclaration node) {
        // Récupérer le nom du package
        currentPackage = node.getName().getFullyQualifiedName();
        return super.visit(node);
    }

	/**
	 * @return the classNameSet
	 */
	public Set<String> getClassNameSet() {
		return classNameSet;
	}


}
