package fortytwo.compiler;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.parsed.expressions.ParsedExpression;

public class Context {
	public static Context construct(Context parent, int start, int end) {
		// TODO 0 implement this
		return new Context();
	}
	public static Context minimal() {
		// TODO 0 implement this
		return new Context();
	}
	public static Context synthetic() {
		// TODO 0 implement this
		return new Context();
	}
	public static Context tokenSum(List<Token> tokens) {
		return sum(tokens.stream().map(t -> t.context)
				.collect(Collectors.toList()));
	}
	public static Context exprSum(List<? extends ParsedExpression> arguments) {
		return sum(arguments.stream().map(ParsedExpression::context)
				.collect(Collectors.toList()));
	}
	public static Context sum(List<Context> asList) {
		// TODO 0 implement this
		return new Context();
	}
	public Context inParen() {
		// TODO 0 implement this
		return new Context();
	}
	public Context subContext(int i, int j) {
		// TODO 0 implement this
		return new Context();
	}
	public Context unary() {
		// TODO 0 implement this
		return null;
	}
}
