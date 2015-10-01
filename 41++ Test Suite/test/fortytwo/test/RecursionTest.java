package fortytwo.test;

import org.junit.Test;

public class RecursionTest extends EnvironmentTester {
	public static final String SIMPLE_LINEAR_RECURSION = "" //
			+ "Define a function called \"n\" ! that takes a number called \"n\" and outputs a number.\n"
			+ "	If \"n\" is equal to 0:\n"
			+ "		Exit the function and output 1.\n"//
			+ "	Otherwise:\n" //
			+ "		Exit the function and output \"n\" * ((\"n\"-1) !).\n";
	public static final String SIMPLE_TREE_RECURSION = "" //
			+ "Define a function called slowly compute the \"n\"th fibonacci number that takes a number called \"n\" and outputs a number.\n"
			+ "	If \"n\" is equal to 0:\n"
			+ "		Exit the function and output 0.\n"
			+ "	If \"n\" is equal to 1:"
			+ "		Exit the function and output 1.\n"
			+ "	Exit the function and output (slowly compute the \"n\"-1 th fibonacci number) + (slowly compute the \"n\"-2 th fibonacci number).";
	public static final String MUTUAL_RECURSION = "" //
			+ "Define a function called \"n\" is even that takes a number called  \"n\" and outputs a bool.\n"
			+ "	If \"n\" is equal to 0:\n"
			+ "		Exit the function and output true.\n"
			+ "	Exit the function and output not (\"n\"-1 is odd).\n"
			+ "Define a function called \"n\" is odd that takes a number called \"n\" and outputs a bool.\n"
			+ "	Exit the function and output not (\"n\"-1 is even).";
	@Test
	public void testSimpleLinearRecursion() {
		loadEnvironment(SIMPLE_LINEAR_RECURSION);
		assertEquivalence("1", "0!");
		assertEquivalence("24", "4 !");
		assertEquivalence(
				"93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000",
				"100!");
	}
	@Test
	public void testSimpleTreeRecursion() {
		loadEnvironment(SIMPLE_TREE_RECURSION);
		assertEquivalence("1", "slowly compute the 1 th fibonacci number");
		assertEquivalence("5", "slowly compute the 5th fibonacci number");
		assertEquivalence("55", "slowly compute the 10th fibonacci number");
		assertEquivalence("610", "slowly compute the 15th fibonacci number");
	}
	@Test
	public void testMutualRecursion() {
		loadEnvironment(MUTUAL_RECURSION);
		assertEquivalence("true", "0 is even");
		assertEquivalence("true", "12 is even");
		assertEquivalence("false", "11 is odd");
	}
}
