package app;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
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
	
	List<Placeholder> methodInfo = new ArrayList<>();
	
	@Override
	public boolean visit(TypeDeclaration node) {
	    if (!node.isInterface()) { 
	    	types.add(node);
	    	System.out.println("visiting class "+node.getName().getIdentifier());
	    }
	    return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		Placeholder p;
		
		methodsDeclared.add(node);
		System.out.println("visiting méthod "+node.getName().getFullyQualifiedName());
		String nomClass, nomMethod;
		nomClass = node.resolveBinding().getDeclaringClass().getQualifiedName();
		nomMethod = node.getName().getFullyQualifiedName();
		p = new Placeholder(nomClass, nomMethod);
		
		// Parcourir le corps de la méthode pour trouver les MethodInvocation
        node.getBody().accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodInvocation methodInvocation) {
                // Récupérer le nom de la méthode appelée
                String calledMethodName = methodInvocation.getName().getFullyQualifiedName();
                System.out.println("calling "+calledMethodName);
                
//                if (methodInvocation.getExpression() != null) {
//                    ITypeBinding typeBinding = methodInvocation.getExpression().resolveTypeBinding();
//                    
//                    if (typeBinding != null) {
//                        System.out.println("Type statique de l'objet sur lequel la méthode est appelée: " + typeBinding.getQualifiedName());
//                    }
//                } else {
//                    System.out.println("Méthode appelée sans objet (probablement une méthode statique).");
//                }
                
                
                IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();

                if (methodBinding != null) {
                    // Récupérer le nom de la méthode appelée
                    String methodName = methodBinding.getName();

                    // Récupérer la classe ou l'interface dans laquelle la méthode est définie
                    ITypeBinding declaringClass = methodBinding.getDeclaringClass();
                    if (declaringClass != null) {
                        String declaringClassName = declaringClass.getQualifiedName();
                        System.out.println("Méthode appelée: " + methodName +
                                " est définie dans la classe/interface: " + declaringClassName);
                        Placeholder tmpP = new Placeholder(declaringClassName, methodName);
                        p.add(tmpP);
                        System.out.println("    "+p);
                    }
                } else {
                	System.out.println("resolveMethodBinding not succesful");
                }
                
                return super.visit(methodInvocation);
            }
        });
        
        
		return super.visit(node);
	}
	
	public List<TypeDeclaration> getTypes() {
		return types;
	}

}
