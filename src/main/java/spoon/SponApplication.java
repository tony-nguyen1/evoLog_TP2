package spoon;

public class SponApplication {

	public static void main(String[] args) {

		System.out.println("Spoon");
		System.out.println(System.getProperty("java.specification.version"));
		
//		MavenLauncher launcher = new MavenLauncher("/home/tony/M2/evoLog/evoLog_TP2", MavenLauncher.SOURCE_TYPE.APP_SOURCE);
//		launcher.buildModel();
//		CtModel model = launcher.getModel();
//
//		// list all packages of the model
//		for(CtPackage p : model.getAllPackages()) {
//		  System.out.println("package: " + p.getQualifiedName());
//		}
//		// list all classes of the model
//		for(CtType<?> s : model.getAllTypes()) {
//		  System.out.println("class: " + s.getQualifiedName());
//		}
		
//		MavenLauncher launcher = new MavenLauncher(
//                "/home/tony/M2/evoLog/evoLog_TP2",
//                 MavenLauncher.SOURCE_TYPE.APP_SOURCE);
//
//        CtModel model = launcher.buildModel();
		
//		CtClass l = Launcher.parseClass("class A { void m() { System.out.println(\"yeah\");} }");
		
		// Initialise Spoon

        MavenLauncher launcher = new MavenLauncher(
                "/home/tony/M2/evoLog/evoLog_TP2",
                 MavenLauncher.SOURCE_TYPE.APP_SOURCE);
//        Launcher launcher = new Launcher();
//        launcher.getEnvironment().setComplianceLevel(17);
        
//        // Définit le chemin du code source à analyser
//        launcher.addInputResource("src/main/java");
//        
//        // Construit le modèle (AST)
        launcher.buildModel();
	}
}
