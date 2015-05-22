package fortytwo.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Parser;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralNumber;

public class ArithmeticTest {
	GlobalEnvironment env;
	@Before
	public void init() {
		env = GlobalEnvironment.getDefaultEnvironment(
				StaticEnvironment.getDefault(), VirtualMachine.defaultVM);
	}
	@Test
	public void basicsTest() {
		assertEquivalence(2, "1+1");
		assertEquivalence(.5, "1/2");
		assertEquivalence(1. / 3., "1/3");
		assertEquivalence(3 - 4, "3-4");
		assertEquivalence(1234567890098765432L / 3532234L,
				"1234567890098765432//3532234");
		assertEquivalence(1234567890098765432L % 3532234L,
				"1234567890098765432%3532234");
	}
	@Test
	public void orderOfOperations() {
		assertEquivalence(3 + 4 * 5, "3 + 4*5");
		assertEquivalence(.375, "1/2 * 3/4");
		assertEquivalence(2, "2/3//(1/3)");
		assertEquivalence(3 * 3 + 4 * (3 - 2), "3 * 3 + 4* (3-2)");
		assertEquivalence(0 + -64, "0 + - 64");
		assertEquivalence(0 - +64, "0 - + 64");
		assertEquivalence(1234567890098765432L / 3532234L * (234 + 1234),
				"1234567890098765432//3532234*(234+1234)");
		assertEquivalence(3 * +4, "3*+4");
		assertEquivalence(3 * -4, "3*-4");
	}
	public void assertEquivalence(double result, String toEvaluate) {
		assertEquals(
				result,
				((LiteralNumber) ExpressionParser
						.parseExpression(Parser.tokenize42(toEvaluate))
						.contextualize(env.staticEnv)
						.literalValue(env.minimalLocalEnvironment())).contents
						.doubleValue(), Math.ulp(result));
	}
}
