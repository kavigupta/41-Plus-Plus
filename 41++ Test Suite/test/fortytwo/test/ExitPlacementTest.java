package fortytwo.test;

import org.junit.Test;

import fortytwo.compiler.Context;
import fortytwo.vm.errors.ErrorType;

public class ExitPlacementTest extends EnvironmentTester {
	public static final String NO_EXIT_ERROR = ""//
			+ "Define a function called the absolute value of \"x\" that takes a number called \"x\" and outputs a number.\n"
			+ "	If \"x\" is greater than 0:\n"
			+ "		Exit the function and output \"x\".";
	public static final String NO_EXIT_WORKS = ""//
			+ "Define a function called the absolute value of \"x\" that takes a number called \"x\" and outputs a number.\n"
			+ "	If \"x\" is greater than 0:\n"//
			+ "		Exit the function and output \"x\".\n"//
			+ "	Otherwise:\n"//
			+ "		Exit the function and output -\"x\".";
	public static final String CONTROL_PAST_END = ""//
			+ "Define a function called the absolute value of \"x\" that takes a number called \"x\" and outputs a number.\n"
			+ "	Exit the function and output \"x\".\n"
			+ "	Set the value of \"x\" to 2.";
	public static final String VOID_FUNCTION_RETURNING = ""//
			+ "Define a function called the test \"x\" that takes a number called \"x\".\n"
			+ "	Set the value of \"x\" to 35."
			+ "	Exit the function and output \"x\"";
	@Test
	public void testExitFlow() {
		Utilities.assertError(ErrorType.TYPING,
				"The function \"the absolute value of <arg>:(function that takes a number and outputs a number)\" does not output under some cases",
				0, CONTROL_PAST_END.length(),
				() -> loadEnvironment(NO_EXIT_ERROR),
				Context.entire(NO_EXIT_ERROR));
		try {
			loadEnvironment(NO_EXIT_WORKS);
		} catch (Throwable t) {
			throw new AssertionError("Expected the loading to work properly",
					t);
		}
	}
	@Test
	public void testNoControlPastEnd() {
		Utilities.assertError(ErrorType.TYPING,
				"The function \"the absolute value of <arg>:(function that takes a number and outputs a number)\" contains statements past the end",
				0, CONTROL_PAST_END.length(),
				() -> loadEnvironment(CONTROL_PAST_END),
				Context.entire(CONTROL_PAST_END));
	}
	@Test
	public void testVoidReturning() {
		Utilities.assertError(ErrorType.TYPING,
				"The function \"test <arg>:(function that takes a number)\"", 0,
				VOID_FUNCTION_RETURNING.length(),
				() -> loadEnvironment(VOID_FUNCTION_RETURNING),
				Context.entire(CONTROL_PAST_END));
	}
}
