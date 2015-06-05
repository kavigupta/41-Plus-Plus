package fortytwo.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.StatementParser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

public class Utilities {
	public static void execute(String statement, GlobalEnvironment env) {
		((ParsedStatement) StatementParser.parseStatement(Tokenizer.tokenize(
				Context.minimal(statement), statement))).contextualize(
				env.staticEnv).execute(env.minimalLocalEnvironment());
	}
	public static LiteralExpression evaluate(String toEvaluate,
			GlobalEnvironment env) {
		return ExpressionParser
				.parseExpression(
						Tokenizer.tokenize(Context.synthetic(),
								toEvaluate))
				.contextualize(env.staticEnv)
				.literalValue(env.minimalLocalEnvironment());
	}
	public static void assertCorrectTokenization(String toTokenize,
			String... tokens) {
		assertEquals(
				Arrays.asList(tokens),
				Tokenizer.tokenize(Context.minimal(toTokenize), toTokenize)
						.stream().map(x -> x.token)
						.collect(Collectors.toList()));
	}
	public static void assertEquivalence(double result, String toEvaluate,
			GlobalEnvironment env) {
		assertEquals(result, ((LiteralNumber) Utilities.evaluate(toEvaluate,
				env)).contents.doubleValue(), Math.ulp(result));
	}
}
