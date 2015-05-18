package fortytwo.vm.statements;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableTypeRoster;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public class FunctionCall implements Expression, Statement {
	public final FunctionSignature function;
	public final ConcreteType outputType;
	public final List<Expression> arguments;
	public static FunctionCall getInstance(FunctionSignature function,
			ConcreteType outputType, List<Expression> value) {
		return new FunctionCall(function, outputType, value);
	}
	private FunctionCall(FunctionSignature function, ConcreteType outputType,
			List<Expression> arguments) {
		this.function = function;
		this.outputType = outputType;
		this.arguments = arguments;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		literalValue(environment);
	}
	@Override
	public boolean typeCheck(VariableTypeRoster typeRoster) {
		// TODO check that the function applies to the argument types.
		// Everything else is checked by now, hopefully.
		return true;
	}
	@Override
	public LiteralExpression literalValue(LocalEnvironment environment) {
		return environment.global.funcs.get(function).apply(
				environment.global,
				arguments.stream().map(x -> x.literalValue(environment))
						.collect(Collectors.toList()));
	}
	@Override
	public ConcreteType resolveType(VariableTypeRoster typeRoster) {
		return outputType;
	}
}
