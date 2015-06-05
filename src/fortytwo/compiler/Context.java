package fortytwo.compiler;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.parsed.expressions.ParsedExpression;

public class Context {
	public static final Context SYNTHETIC = new Context(null, -1, -1);
	public final String in;
	public final int start, end;
	private Context(String in, int start, int end) {
		this.in = in;
		this.start = start;
		this.end = end;
	}
	private boolean isSynthetic() {
		return this == SYNTHETIC;
	}
	public Context inParen() {
		if (isSynthetic()) return SYNTHETIC;
		assert start - 1 >= 0;
		return new Context(in, start - 1, end + 1);
	}
	public Context withUnaryApplied() {
		if (isSynthetic()) return SYNTHETIC;
		assert start - 1 >= 0;
		return new Context(in, start - 1, end);
	}
	public static Context construct(Context parent, int start, int end) {
		if (parent.isSynthetic()) return SYNTHETIC;
		assert start >= 0;
		assert end >= 0;
		assert parent.start + end < parent.end;
		return new Context(parent.in, parent.start + start, parent.start
				+ end);
	}
	public static Context minimal(String text) {
		return new Context(text, 0, text.length());
	}
	public static Context synthetic() {
		return SYNTHETIC;
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
		if (asList.size() == 0) return SYNTHETIC;
		return asList.stream().reduce(Context::sum).get();
	}
	public static Context sum(Context a, Context b) {
		if (a.isSynthetic() || b.isSynthetic()) return SYNTHETIC;
		assert a.in.equals(b.in);
		return new Context(a.in, Math.min(a.start, b.start), Math.max(a.end,
				b.end));
	}
}
