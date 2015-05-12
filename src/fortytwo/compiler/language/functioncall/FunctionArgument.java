package fortytwo.compiler.language.functioncall;

import fortytwo.compiler.language.expressions.ParsedExpression;

public class FunctionArgument implements FunctionComponent {
	public final ParsedExpression value;
	public FunctionArgument(ParsedExpression value) {
		this.value = value;
	}
}
