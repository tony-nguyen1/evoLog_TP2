/**
 * 
 */
package fr.umontpellier.etu;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.umontpellier.etu.app.MyParser;

/**
 * @author tony
 *
 */
class GraphTest {
	private static MyParser myParser;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		String projectPath = "/home/tony/M2/evoLog/evoLog_TP2";
		String projectSourcePath = projectPath + "/src/main/java";
		String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre";
		
		myParser = new MyParser("src/test/java/fr/umontpellier/etu/dummy", projectPath, projectSourcePath, jrePath);
		
		System.out.println(myParser.getNbMethodOfApplication());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link graph.ImgGraph#showGraphPond(org.jgrapht.graph.SimpleWeightedGraph, java.lang.String, java.lang.String)}.
	 */
	@Test
	final void testShowGraphPond() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link graph.ImgGraph#writeGraphIntoImg(org.jgrapht.Graph, java.lang.String, java.lang.String)}.
	 */
	@Test
	final void testGivenAdaptedGraph_whenWriteBufferedImage_thenFileShouldExist() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link graph.UtilGraph#nbRelation(org.jgrapht.graph.DefaultDirectedGraph, java.lang.String, java.lang.String)}.
	 */
	@Test
	final void testNbRelation() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link graph.UtilGraph#clusterProche(org.jgrapht.graph.DefaultDirectedGraph, java.util.Set, java.util.Set)}.
	 */
	@Test
	final void testClusterProche() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link graph.ImgGraph#writeDirectedGraphIntoImg(org.jgrapht.graph.DefaultDirectedGraph, java.lang.String, java.lang.String)}.
	 */
	@Test
	final void testWriteGraphIntoImg() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link graph.UtilGraph#clustering_hierarchique(org.jgrapht.graph.DefaultDirectedGraph, int, java.util.Set)}.
	 */
	@Test
	final void testClustering_hierarchique() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link graph.UtilGraph#getModule(double, cluster.Cluster, int)}.
	 */
	@Test
	final void testGetModule() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link graph.UtilGraph#getGraphCouplage(java.util.Set, org.jgrapht.graph.DefaultDirectedGraph, int)}.
	 */
	@Test
	final void testGetGraphCouplage() {
		fail("Not yet implemented"); // TODO
	}

}
