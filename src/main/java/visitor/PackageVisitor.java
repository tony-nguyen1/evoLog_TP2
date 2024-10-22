package visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

public class PackageVisitor extends ASTVisitor {
	private Set<String> packageNameSet = new HashSet<>();

    @Override
    public boolean visit(PackageDeclaration node) {

    	// Récupérer le nom du packages
        packageNameSet.add(node.getName().getFullyQualifiedName());

        return false;
    }

    public Set<String> getPackageName() {
        return packageNameSet;
    }
}
