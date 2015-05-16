package fortytwo.vm.statements;

import java.util.List;

import fortytwo.language.type.ConcreteType;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableTypeRoster;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public class FunctionCall implements Expression, Statement {
	public final Function42 function;
	public final List<Expression> arguments;
	public static FunctionCall getInstance(Function42 function,
			List<Expression> value) {
		return new FunctionCall(function, value);
	}
	private FunctionCall(Function42 function, List<Expression> arguments) {
		this.function = function;
		this.arguments = arguments;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		function.apply(environment, arguments);
	}
	@Override
	public boolean typeCheck(VariableTypeRoster typeRoster) {
		// TODO check that the function applies to the argument types.
		// Everything else is checked by now, hopefully.
		return true;
	}
	@Override
	public LiteralExpression literalValue(LocalEnvironment environment) {
		return function.apply(environment, arguments);
	}
	@Override
	public ConcreteType resolveType(VariableTypeRoster typeRoster) {
		return function.outputType();
	}
}
