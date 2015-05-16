package fortytwo.compiler.parsed.declaration;

import fortytwo.compiler.parsed.expressions.ParsedExpression;

public class FunctionReturn implements Declaration {
	public final ParsedExpression output;
	public FunctionReturn(ParsedExpression output) {
		this.output = output;
	}
	@Override
	public SentenceType type() {
		return SentenceType.FUNCTION_RETURN;
	}
}
