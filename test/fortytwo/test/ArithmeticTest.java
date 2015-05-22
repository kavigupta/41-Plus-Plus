package fortytwo.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Parser;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;

public class ArithmeticTest {
	GlobalEnvironment env;
	@Before
	public void init() {
		env = GlobalEnvironment.getDefaultEnvironment(
				StaticEnvironment.getDefault(), VirtualMachine.defaultVM);
	}
	@Test
	public void basicsTest() {
		assertEquivalence("2", "1+1");
		assertEquivalence("0.5", "1/2");
		assertEquivalence("0.3333", "1/3");
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
