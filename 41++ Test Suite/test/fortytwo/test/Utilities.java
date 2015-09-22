package fortytwo.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.Statement;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.Parser;
import fortytwo.compiler.parser.StatementParser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.UnorderedEnvironment;
import fortytwo.vm.errors.Error42;
import fortytwo.vm.errors.ErrorType;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

public class Utilities {
	public static void execute(String statement, UnorderedEnvironment env) {
		((Statement) StatementParser.parseStatement(
				Tokenizer.tokenize(LiteralToken.entire(statement))))
						.execute(env.minimalLocalEnvironment());
	}
	public static LiteralExpression evaluate(String toEvaluate,
			UnorderedEnvironment env) {
		return ExpressionParser
				.parseExpression(
						Tokenizer.tokenize(LiteralToken.entire(toEvaluate)))
				.literalValue(env.minimalLocalEnvironment());
	}
	public static void assertErrorInTokenization(ErrorType type, String msg,
			int start, int end, String toTokenize) {
		LiteralToken token = LiteralToken.entire(toTokenize);
		assertError(type, msg, start, end, () -> Tokenizer.tokenize(token),
				token.context);
	}
	public static void assertCorrectTokenization(String toTokenize,
			String... tokens) {
		assertEquals(Arrays.asList(tokens),
				Tokenizer.tokenize(LiteralToken.entire(toTokenize)).stream()
						.map(x -> x.token).collect(Collectors.toList()));
	}
	public static void assertEquivalence(double result, String toEvaluate,
			UnorderedEnvironment env) {
		assertEquals(result,
				((LiteralNumber) Utilities.evaluate(toEvaluate, env)).contents
						.doubleValue(),
				Math.ulp(result));
	}
	public static void assertError(ErrorType type, String msg, int start,
			int end, Runnable run, Context parent) {
		class CustomException extends RuntimeException {
			private static final long serialVersionUID = 1L;
		}
		Consumer<Error42> old = VirtualMachine.displayerr;
		VirtualMachine.displayerr = x -> {
			throw new CustomException();
		};
		try {
			run.run();
		} catch (CustomException re) {
			// expected.
		}
		if (!VirtualMachine.errorState()) throw new AssertionError(
				"An error was expected, but none was found.");
		assertEquals(new Error42(type, msg, parent.subContext(start, end))
				.toString(), VirtualMachine.popError().toString());
		VirtualMachine.displayerr = old;
	}
	public static void assertParseError(ErrorType type, String msg, int start,
			int end, String toParse) {
		Runnable go;
		if (toParse.charAt(toParse.length() - 1) == '.')
			go = () -> parseStatement(toParse);
		else go = () -> parseExpr(toParse);
		assertError(type, msg, start, end, go, Context.entire(toParse));
	}
	public static void assertParse(Expression value, String toParse) {
		assertEquals(value, ExpressionParser.parseExpression(
				Tokenizer.tokenize(LiteralToken.entire(toParse))));
	}
	public static void validateLineParse(String line) {
		assertEquals(line, cdLoopLine(line));
	}
	public static void validateExprParse(String expr) {
		assertEquals(expr, cdLoopExpr(expr));
	}
	public static void validateTypeParse(String expr) {
		assertEquals(expr, cdLoopType(expr));
	}
	public static String cdLoopLine(String line) {
		Sentence s = Parser.parse(line).get(0);
		return s.toSourceCode() + ".";
	}
	public static String cdLoopType(String line) {
		return ExpressionParser.parseType(LiteralToken.entire(line))
				.toSourceCode();
	}
	public static String cdLoopExpr(String line) {
		return parseExpr(line).toSourceCode();
	}
	public static Expression parseExpr(String line) {
		return ExpressionParser
				.parseExpression(Tokenizer.tokenize(LiteralToken.entire(line)));
	}
	private static Sentence parseStatement(String line) {
		return StatementParser
				.parseStatement(Tokenizer.tokenize(LiteralToken.entire(line)));
	}
}
