package fortytwo.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fortytwo.vm.VirtualMachine;

public class FunctionsTest extends EnvironmentTester {
	public static final String TEST_FUNCTIONS = ""
			+ "Define a function called \"x\" doubled that takes a number called \"x\" and outputs a number. Exit the function and output (\"x\"*2).\n"
			+ "Define a function called \"x\" is prime that takes a number called \"x\" and outputs a bool.\n"
			+ "	Define a number called \"i\" with a value of 2. Define a bool called \"isPrimeForNow\" with a value of (\"x\" is at least 2).\n"
			+ "	While (\"i\" * \"i\" is at most \"x\") and \"isPrimeForNow\":\n"
			+ "		Set the value of \"i\" to \"i\"+1.\n"
			+ "		If (\"x\" % \"i\") is equal to 0: Set the value of \"isPrimeForNow\" to false.\n"
			+ "	Exit the function and output \"isPrimeForNow\".\n"
			+ "Define a function called \"str\" with spaces intersperced that takes a string called \"str\" and outputs a string.\n"
			+ "	Define an (array of string) called \"strLet\" with a value of (\"str\" split into individual letters).\n"
			+ "	Define an (array of string) called \"strInter\" with a \"length\" of (2*(the \"length\" of \"strLet\")-1).\n"
			+ "	Define a number called \"i\" with a value of 1.\n"
			+ "	While \"i\" is less than (the \"length\" of \"str\"):\n"
			+ "		Set the (2*\"i\"-1) th element of \"strInter\" to (the \"i\" th element of \"strLet\").\n"
			+ "		Set the (2*\"i\") th element of \"strInter\" to ' '.\n"
			+ "		Set the value of \"i\" to (\"i\"+1).\n"
			+ "	Set the (2*\"i\"-1) th element of \"strInter\" to (the \"i\" th element of \"strLet\").\n"
			+ "	Exit the function and output (the letters \"strInter\" combined to form a string).\n"
			+ "Define a function called Test procedures.\n"
			+ "	Define a (pair of number and number) called \"pair\" with a \"key\" of 2 and a \"value\" of 3.\n"
			+ "	Tell me what \"pair\" is.\n" + "	Exit the function.\n"
			+ "Define a function called multiple return on \"branch\" that takes a bool called \"branch\" and outputs a number.\n"
			+ "	If \"branch\":\n" + "\t\tExit the function and output 1.\n"
			+ "	Otherwise:\n" + "		Exit the function and output 0.";
	@Before
	public void init() {
		loadEnvironment(TEST_FUNCTIONS);
	}
	@Test
	public void pairTest() {
		exec("Test procedures.");
		assertEquals(
				"{(pair of number and number): \"key\" <= 2, \"value\" <= 3}\n",
				VirtualMachine.popMessage());
	}
	@Test
	public void xdoubleTest() {
		assertEquivalence("5.0", "2.5 doubled");
		assertEquivalence("10", "2+3 doubled");
		assertEquivalence("5.0", "5/2 doubled");
	}
	@Test
	public void isPrimeTest() {
		assertEquivalence("true", "2 is prime");
		assertEquivalence("false", "200 is prime");
		assertEquivalence("true", "199 is prime");
		assertEquivalence("false", "(199*199) is prime");
	}
	@Test
	public void stringManipTest() {
		assertEquivalence("'a b c'", "'abc' with spaces intersperced");
		assertEquivalence("'H e l l o ,   W o r l d'",
				"'Hello, World' with spaces intersperced");
	}
}
