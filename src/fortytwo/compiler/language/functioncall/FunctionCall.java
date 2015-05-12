package fortytwo.compiler.language.functioncall;

import java.util.List;

import fortytwo.compiler.language.expressions.Expression;
import fortytwo.compiler.language.expressions.LiteralExpression;
import fortytwo.vm.environment.Environment;

public class FunctionCall implements Expression {
	public final List<FunctionComponent> function;
	public static FunctionCall getInstance(List<FunctionComponent> function) {
		return new FunctionCall(function);
	}
	private FunctionCall(List<FunctionComponent> function) {
		this.function = function;
	}
	@Override
	public String type(Environment environment) {
		// TODO
		return null;
	}
	@Override
	public LiteralExpression evaluate(Environment environment) {
		// TODO Auto-generated method stub
		return null;
	}
}
