package fortytwo.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.compiler.parsed.statements.ParsedFunctionCall;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.StatementParser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.type.PrimitiveType;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralString;

public class GlobalEnvTest {
	GlobalEnvironment env;
	String buffer;
	@Before
	public void init() {
		env = GlobalEnvironment.getDefaultEnvironment(
				StaticEnvironment.getDefault(), x -> {
					buffer = x;
				});
	}
	@Test
	public void printTest() {
		((ParsedStatement) StatementParser.parseStatement(Tokenizer.tokenize(
				Context.minimal(), "Tell me what 'Hello, World' is.")))
				.contextualize(env.staticEnv).execute(
						env.minimalLocalEnvironment());
		assertEquals("'Hello, World'", buffer);
		((ParsedStatement) StatementParser.parseStatement(Tokenizer.tokenize(
				Context.minimal(), "Tell me what 'Hello. World' is.")))
				.contextualize(env.staticEnv).execute(
						env.minimalLocalEnvironment());
		assertEquals("'Hello. World'", buffer);
		((ParsedStatement) StatementParser
				.parseStatement(Tokenizer.tokenize(Context.minimal(),
						"Tell me what 'a + b = c + d-e+f=g,dsad. asde.3452,2' is.")))
				.contextualize(env.staticEnv).execute(
						env.minimalLocalEnvironment());
		assertEquals("'a + b = c + d-e+f=g,dsad. asde.3452,2'", buffer);
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
												PrimitiveType.STRING,
												"world".chars()
														.mapToObj(x -> new LiteralString(
																new Token(
																		Character.toString((char) x),
																		Context.synthetic())))
														.collect(Collectors
																.toList()))))
						.contextualize(env.staticEnv)
						.literalValue(env.minimalLocalEnvironment())
						.toSourceCode());
	}
	public void assertEquivalence(String result, String toEvaluate) {
		assertEquals(
				result,
				ExpressionParser
						.parseExpression(
								Tokenizer.tokenize(Context.synthetic(),
										toEvaluate))
						.contextualize(env.staticEnv)
						.literalValue(env.minimalLocalEnvironment())
						.toSourceCode());
	}
}
