package fortytwo.compiler.language.statements;

import java.util.List;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.statements.constructions.FunctionComponent;
import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;

public class FunctionCall implements ParsedExpression {
	public final List<FunctionComponent> function;
	public static FunctionCall getInstance(List<FunctionComponent> function) {
		return new FunctionCall(function);
	}
	private FunctionCall(List<FunctionComponent> function) {
		this.function = function;
	}
	@Override
	public Expression contextualize(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
