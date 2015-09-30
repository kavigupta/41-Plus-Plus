package fortytwo.test.parser;

import static fortytwo.test.Utilities.assertError;
import static fortytwo.test.Utilities.parseSentence;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fortytwo.compiler.Context;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.errors.ErrorType;

@SuppressWarnings("static-method")
public class OperatorOverloadTest {
	public static final String PROPER_OVERLOAD = "Define a function called \"x\" + \"y\" that takes a vector called \"x\" and a vector called \"y\" and outputs a vector.";
	public static final String IMPROPER_OVERLOAD = "Define a function called a \"x\" + \"y\" that takes a vector called \"x\" and a vector called \"y\" and outputs a vector.";
	@Test
	public void successfulOOTest() {
		VirtualMachine.clean();
		parseSentence(PROPER_OVERLOAD);
		assertTrue("Proper overload function definition",
				!VirtualMachine.errorState());
	}
	@Test
	public void failingOOTest() {
		VirtualMachine.clean();
		assertError(ErrorType.SYNTAX,
				"~a \"x\" + \"y\"~ is not a valid function signature", 25, 36,
				() -> parseSentence(IMPROPER_OVERLOAD),
				Context.entire(IMPROPER_OVERLOAD));
	}
}
