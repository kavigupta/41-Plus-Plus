package fortytwo.compiler.language.functioncall;

import java.util.List;

import fortytwo.compiler.language.expressions.Expression;

public class FunctionCall implements Expression {
	public final List<FunctionComponent> function;
	public FunctionCall(List<FunctionComponent> function) {
		this.function = function;
	}
}
