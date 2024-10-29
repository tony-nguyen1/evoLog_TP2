package fr.umontpellier.etu.spoon;

import spoon.MavenLauncher;
import spoon.reflect.CtModel;

public class SponApplication {

	public static void main(String[] args) {

		System.out.println("Spoon");
		System.out.println(System.getProperty("java.specification.version"));

		MavenLauncher launcher = new MavenLauncher(
                "/home/tony/M2/evoLog/evoLog_TP2",
                 MavenLauncher.SOURCE_TYPE.APP_SOURCE);

        CtModel model = launcher.buildModel();

        System.out.println(model);
	}
}
