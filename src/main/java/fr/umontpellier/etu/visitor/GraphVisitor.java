package fr.umontpellier.etu.visitor;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jgrapht.graph.DefaultDirectedGraph;

import fr.umontpellier.etu.graph.MyEdge;

/**
 * Classe servant à construire graph d'appel des méthodes entre elles.
 * But : Trouver les méthodes des différentes classes (uniquement celles qui appartiennent aux projets) et où elles sont appelées.
 */
public class GraphVisitor extends ASTVisitor {
	private String currentPackage = "";

	DefaultDirectedGraph<String, MyEdge> g = new DefaultDirectedGraph<>(MyEdge.class);
	HashSet<String> classNameSet = new HashSet<>();

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


	@Override
	public boolean visit(MethodDeclaration node) {
		// Récupérer le nom de la méthode
        String methodName = node.getName().getIdentifier();

        // Récupérer le nom de la classe en parent
        TypeDeclaration typeDecl = (TypeDeclaration) node.getParent();
        String className = typeDecl.getName().getIdentifier();

        // Combiner le package, la classe et la méthode
        String fullMethodName = currentPackage + "." + className + "::" + methodName;
//        System.out.println("Nom complet de la méthode: " + fullMethodName);

		g.addVertex(fullMethodName);
//		System.out.println(fullMethodName);

		// Parcourir le corps de la méthode pour trouver les MethodInvocation
        node.getBody().accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodInvocation methodInvocation) {
                // Récupérer le nom de la méthode appelée
//                String calledMethodName = methodInvocation.getName().getFullyQualifiedName();



                IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();

                if (methodBinding != null) {
                    // Récupérer le nom de la méthode appelée
                    String methodName2 = methodBinding.getName();
//                    System.out.println("->"+methodName2);

                    // Récupérer la classe ou l'interface dans laquelle la méthode est définie
                    ITypeBinding declaringClass = methodBinding.getDeclaringClass();
                    if (declaringClass != null) { // isExternalMethod
                        String declaringClassName = declaringClass.getQualifiedName();
                        String source = fullMethodName;
                        String target = declaringClassName+"::"+methodName2;

                        // trick : ajout de la source et la cible -> ajout de l'arête plus facile
                        g.addVertex(source);
                        g.addVertex(target);


                        MyEdge e = g.addEdge(source, target);
                        
                        if (e!=null) {
                        	e.setClassNameSource(currentPackage + "." + className);
                        	e.setMethodNameSource(methodName);
                        	
                        	e.setClassTargetSource(declaringClassName);
                        	e.setMethodNameTarget(methodName2);                        	
                        }

                    }
                } else {
//                	System.out.println("resolveMethodBinding not succesful");
                }

                return super.visit(methodInvocation);
            }
        });


		return super.visit(node);
	}

	/**
	 * @return the g
	 */
	public DefaultDirectedGraph<String, MyEdge> getG() {
		return g;
	}

	/**
	 * @return the classNameSet
	 */
	public HashSet<String> getClassNameSet() {
		return classNameSet;
	}



}
