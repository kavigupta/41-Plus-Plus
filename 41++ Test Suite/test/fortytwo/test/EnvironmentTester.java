package fortytwo.test;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;

import fortytwo.compiler.Compiler42;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.statements.Statement;
import fortytwo.compiler.parser.ExpressionParser;
import fortytwo.compiler.parser.StatementParser;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

public abstract class EnvironmentTester {
	OrderedEnvironment curr;
	public void assertEquivalence(String result, String toEvaluate) {
		Assert.assertEquals(result,
				ExpressionParser
						.parseExpression(Tokenizer
								.tokenize(LiteralToken.entire(toEvaluate)))
				.literalValue(curr).toSourceCode());
	}
	public void exec(String sentence) {
		((Statement) StatementParser.parseStatement(
				Tokenizer.tokenize(LiteralToken.entire(sentence))))
						.execute(curr);
	}
	public LiteralExpression evaluate(String toEvaluate) {
		return ExpressionParser
				.parseExpression(
						Tokenizer.tokenize(LiteralToken.entire(toEvaluate)))
				.literalValue(curr);
	}
	public void assertEquivalence(double result, String toEvaluate) {
		assertEquals(result,
				((LiteralNumber) evaluate(toEvaluate)).contents.doubleValue(),
				Math.ulp(result));
	}
	public void loadEnvironment(String initial) {
		curr = Compiler42.compile(initial).minimalLocalEnvironment();
	}
}
