package fortytwo.test;

import org.junit.Before;
import org.junit.Test;

import fortytwo.vm.environment.UnorderedEnvironment;

public class OperatorOverloadTest extends EnvironmentTester {
	public static final String TEST_FUNCTIONS = ""
			+ "Define a function called \"x\" + \"y\" that takes a (pair of number and number) called \"x\" and a (pair of number and number) called \"y\" and outputs a pair of number and number.\n"
			+ "	Define a number called \"sumkey\" with a value of (the \"key\" of \"x\") + (the \"key\" of \"y\").\n"
			+ "	Define a number called \"sumval\" with a value of (the \"value\" of \"x\") + (the \"value\" of \"y\").\n"
			+ "	Define a (pair of number and number) called \"sum\" with a \"key\" of \"sumkey\" and a \"value\" of \"sumval\".\n"
			+ "	Exit the function and output \"sum\".\n" //
			+ ""
			+ "Define a function called \"x\" * \"y\" that takes a (pair of number and number) called \"x\" and a (pair of number and number) called \"y\" and outputs a pair of number and number.\n"
			+ "	Define a number called \"prodkey\" with a value of (the \"key\" of \"x\") * (the \"key\" of \"y\").\n"
			+ "	Define a number called \"prodval\" with a value of (the \"value\" of \"x\") * (the \"value\" of \"y\").\n"
			+ "	Define a (pair of number and number) called \"prod\" with a \"key\" of \"prodkey\" and a \"value\" of \"prodval\".\n"
			+ "	Exit the function and output \"prod\".\n";
	UnorderedEnvironment env;
	@Before
	public void init() {
		loadEnvironment(TEST_FUNCTIONS);
		exec("Define a (pair of number and number) called \"a\" with a \"key\" of 1 and a \"value\" of 2.");
		exec("Define a (pair of number and number) called \"b\" with a \"key\" of -1 and a \"value\" of 4.");
		exec("Define a (pair of number and number) called \"c\" with a \"key\" of 1 and a \"value\" of 3.");
	}
	@Test
	public void testOverloads() {
		assertEquivalence(
				"{(pair of number and number): \"key\" <= 0, \"value\" <= 6}",
				"\"a\"+\"b\"");
		assertEquivalence(
				"{(pair of number and number): \"key\" <= -1, \"value\" <= 8}",
				"\"a\"*\"b\"");
		assertEquivalence(
				"{(pair of number and number): \"key\" <= 0, \"value\" <= 11}",
				"\"c\"+\"a\"*\"b\"");
	}
}
