package fortytwo.vm.statements;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
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
	public boolean typeCheck() {
		if (function.inputTypes.size() != arguments.size())
			throw new RuntimeException(/* LOWPRI-E */);
		for (int i = 0; i < function.inputTypes.size(); i++) {
			TypeVariableRoster tvr = function.inputTypes.get(i).match(
					arguments.get(i).resolveType());
			if (tvr == null) throw new RuntimeException(/* LOWPRI-E */);
		}
		return true;
	}
	@Override
	public LiteralExpression literalValue(LocalEnvironment environment) {
		System.out.println(this);
		Function42 f = environment.global.funcs.get(function, arguments);
		if (f == null) throw new RuntimeException(function.name.toString());
		return f.apply(environment.global,
				arguments.stream().map(x -> x.literalValue(environment))
						.collect(Collectors.toList()));
	}
	@Override
	public ConcreteType resolveType() {
		return outputType;
	}
	@Override
	public String toString() {
		return "FunctionCall [function=" + function + ", outputType="
				+ outputType + ", arguments=" + arguments + "]";
	}
}
