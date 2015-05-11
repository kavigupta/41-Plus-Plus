package fortytwo.compiler.language.functioncall;

import fortytwo.compiler.language.expressions.Expression;

public class FunctionArgument implements FunctionComponent {
	public final Expression value;
	public FunctionArgument(Expression value) {
		this.value = value;
	}
}
