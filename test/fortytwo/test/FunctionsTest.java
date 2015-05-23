package fortytwo.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fortytwo.compiler.Compiler42;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Parser;
import fortytwo.compiler.parser.StatementParser;
import fortytwo.vm.environment.GlobalEnvironment;

public class FunctionsTest {
	public static final String TEST_FUNCTIONS = ""
			+ "Define a function called _x doubled that takes a number called _x and outputs a number. Exit the function and output (_x*2)."
			+ "Define a function called _x is prime that takes a number called _x and outputs a bool. Define a number called _i with a value of 2. Define a bool called _isPrimeForNow with a value of (_x is at least 2). While (_i * _i is at most _x) and _isPrimeForNow: Do the following: Set the value of _i to _i+1. If (_x % _i) is equal to 0: Set the value of _isPrimeForNow to false. That's all. Exit the function and output _isPrimeForNow."
			+ "Define a function called _str with spaces intersperced that takes a string called _str and outputs a string. "
			+ "	Define an (array of string) called _strLet with a value of (_str split into individual letters). "
			+ "	Define an (array of string) called _strInter with a _length of (2*(the _length of _strLet)-1)."
			+ "	Define a number called _i with a value of 1."
			+ "	While _i is less than (the _length of _str): Do the following:"
			+ "		Set the (2*_i-1) th element of _strInter to (the _i th element of _strLet)."
			+ "		Set the (2*_i) th element of _strInter to ' '."
			+ "		Set the value of _i to (_i+1)."
			+ "	That's all."
			+ "	Set the (2*_i-1) th element of _strInter to (the _i th element of _strLet)."
			+ "	Exit the function and output (the letters _strInter combined to form a string)."
			+ "Define a function called Test procedures. Define a (pair of number and number) called _pair with a _key of 2 and a _value of 3. Tell me what _pair is. Exit the function.";
	GlobalEnvironment env;
	String buffer;
	@Before
	public void init() {
		env = Compiler42.compile(TEST_FUNCTIONS, x -> {
			buffer = x;
			System.out.println(x);
		});
	}
	@Test
	public void pairTest() {
		((ParsedStatement) StatementParser.parseStatement(Parser
				.tokenize42("Test procedures."))).contextualize(
				env.staticEnv).execute(env.minimalLocalEnvironment());
		assertEquals("{(pair of number and number): _key <= 2, _value <= 3}",
				buffer);
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
	public void assertEquivalence(String result, String toEvaluate) {
		assertEquals(
				result,
				ExpressionParser
						.parseExpression(Parser.tokenize42(toEvaluate))
						.contextualize(env.staticEnv)
						.literalValue(env.minimalLocalEnvironment())
						.toSourceCode());
	}
}
