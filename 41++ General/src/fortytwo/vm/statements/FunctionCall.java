package fortytwo.vm.statements;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public class FunctionCall implements Expression, Statement {
	public final FunctionSignature function;
	public final ConcreteType outputType;
	public final List<Expression> arguments;
	private final Context context;
	public static FunctionCall getInstance(FunctionSignature function,
			ConcreteType outputType, List<Expression> value, Context context) {
		return new FunctionCall(function, outputType, value, context);
	}
	private FunctionCall(FunctionSignature function, ConcreteType outputType,
			List<Expression> arguments, Context context) {
		this.function = function;
		this.outputType = outputType;
		this.arguments = arguments;
		this.context = context;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		literalValue(environment);
	}
	@Override
	public boolean typeCheck() {
		// this has already been checked.
		return true;
	}
	@Override
	public LiteralExpression literalValue(LocalEnvironment environment) {
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
	@Override
	public Context context() {
		return context;
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this.function.name, this.arguments);
	}
}
