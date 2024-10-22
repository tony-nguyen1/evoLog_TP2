/**
 * 
 */
package app;

import java.io.IOException;

/**
 * @author tony
 *
 */
public class ParserApplication {
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		String projectPath = "/home/tony/M2/evoLog/evoLog_TP2";
		String projectSourcePath = projectPath + "/src/main/java/dummy";
		String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre";
		
		MyParser myParser = new MyParser("src/main/java/dummy", projectPath, projectSourcePath, jrePath);
		myParser.computeAndShow();
		
	}
}
