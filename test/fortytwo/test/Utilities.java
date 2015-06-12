package fortytwo.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.StatementParser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.errors.Error42;
import fortytwo.vm.errors.ErrorType;
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
	public static void assertErrorInTokenization(ErrorType type, String msg,
			int start, int end, String toTokenize) {
		Context parent = Context.minimal(toTokenize);
		assertError(type, msg, start, end,
				() -> Tokenizer.tokenize(parent, toTokenize), parent);
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
	public static void assertError(ErrorType type, String msg, int start,
			int end, Runnable run, Context parent) {
		Consumer<Error42> old = VirtualMachine.displayerr;
		VirtualMachine.displayerr = x -> {
			throw new RuntimeException("~Temporary.");
		};
		try {
			run.run();
		} catch (RuntimeException re) {
			if (!re.getMessage().equals("~Temporary.")) throw re;
		}
		if (!VirtualMachine.errorState())
			throw new AssertionError(
					"An error was expected, but none was found.");
		assertEquals(new Error42(type, msg, parent.subContext(start, end)),
				VirtualMachine.popError());
		VirtualMachine.displayerr = old;
	}
	public static void assertParse(ParsedExpression value, String toParse) {
		assertEquals(
				value,
				ExpressionParser.parseExpression(Tokenizer.tokenize(
						Context.minimal(toParse), toParse)));
	}
}
