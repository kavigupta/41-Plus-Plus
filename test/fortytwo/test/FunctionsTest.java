package fortytwo.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fortytwo.compiler.Compiler42;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Parser;
import fortytwo.vm.environment.GlobalEnvironment;

public class FunctionsTest {
	public static final String TEST_FUNCTIONS = ""
			+ "Define a function called _x doubled that takes a number called _x and outputs a number. Exit the function and output (_x*2)."
			+ "Define a function called _x is prime that takes a number called _x and outputs a bool. Define a number called _i with a value of 2. Define a bool called _isPrimeForNow with a value of (_x is at least 2). While (_i * _i is at most _x) and _isPrimeForNow: Do the following: Set the value of _i to _i+1. If (_x % _i) is equal to 0: Set the value of _isPrimeForNow to false. That's all. Exit the function and output _isPrimeForNow."
			+ "Define a function called _str with spaces intersperced that takes a string called _str and outputs a string. Define an (array of string) called _strLet with a value of (_str split into individual letters). Define an (array of string) called _strInter with a _length of (2*(the _length of strInter)-1). Exit the function and output _strInter.";
	GlobalEnvironment env;
	String buffer;
	@Before
	public void init() {
		env = Compiler42.compile(TEST_FUNCTIONS, x -> {
			buffer = x;
		});
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
	public void stringManipTest() {}
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
