package fortytwo.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token42;
import fortytwo.compiler.parsed.statements.ParsedFunctionCall;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralString;

public class GlobalEnvTest {
	GlobalEnvironment env;
	@Before
	public void init() {
		env = GlobalEnvironment.getDefaultEnvironment(StaticEnvironment
				.getDefault());
	}
	@Test
	public void printTest() {
		Utilities.execute("Tell me what 'Hello, World' is.", env);
		assertEquals("'Hello, World'\n", VirtualMachine.popMessage());
		Utilities.execute("Tell me what 'Hello. World' is.", env);
		assertEquals("'Hello. World'\n", VirtualMachine.popMessage());
		Utilities.execute(
				"Tell me what 'a + b = c + d-e+f=g,dsad. asde.3452,2' is.",
				env);
		assertEquals("'a + b = c + d-e+f=g,dsad. asde.3452,2'\n",
				VirtualMachine.popMessage());
	}
	@Test
	public void compareTest() {
		assertEquivalence("true", "1 is equal to 1");
		assertEquivalence("false", "3 is equal to 1");
		assertEquivalence("true", "3 is greater than 1");
		assertEquivalence("false", "3 is less than 1");
		assertEquivalence("false", "3 is less than 3");
		assertEquivalence("false", "3 is greater than 3");
		assertEquivalence("true", "3 is less than or equal to 3");
		assertEquivalence("true", "3 is greater than or equal to 3");
		assertEquivalence("true", "3 is less than or equal to 4");
		assertEquivalence("false", "3 is greater than or equal to 4");
		assertEquivalence("true", "3 is at most 4");
		assertEquivalence("false", "3 is at least 4");
	}
	@Test
	public void logOpTest() {
		assertEquivalence("false", "true and false");
		assertEquivalence("false", "false and false");
		assertEquivalence("false", "not true");
		assertEquivalence("true", "not (true and false)");
	}
	@Test
	public void stringLetterSplitTest() {
		assertEquivalence("['h', 'e', 'l', 'l', 'o']",
				"'hello' split into individual letters");
		assertEquals(
				"'world'",
				ParsedFunctionCall
						.getInstance(
								FunctionName.getInstance("the",
										"letters", "", "combined",
										"to", "form", "a", "string"),
								Arrays.asList(LiteralArray
										.getInstance(
												new PrimitiveType(
														PrimitiveTypeWithoutContext.STRING,
														Context.SYNTHETIC),
												"world".chars()
														.mapToObj(x -> new LiteralString(
																new Token42(
																		Character.toString((char) x),
																		Context.SYNTHETIC)))
														.collect(Collectors
																.toList()),
												Context.SYNTHETIC)))
						.contextualize(env.staticEnv)
						.literalValue(env.minimalLocalEnvironment())
						.toSourceCode());
	}
	public void assertEquivalence(String result, String toEvaluate) {
		assertEquals(
				result,
				ExpressionParser
						.parseExpression(
								Tokenizer.tokenize(Context.SYNTHETIC,
										toEvaluate))
						.contextualize(env.staticEnv)
						.literalValue(env.minimalLocalEnvironment())
						.toSourceCode());
	}
}
