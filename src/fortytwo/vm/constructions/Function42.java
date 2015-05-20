package fortytwo.vm.constructions;

import java.util.List;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralExpression;

public abstract class Function42 {
	public final LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments) {
		FunctionSignature sig = signature();
		if (sig.inputTypes.size() != arguments.size())
			throw new RuntimeException(/* LOWPRI-E */);
		return apply(env, arguments, sig.typeVariables(arguments));
	}
	protected abstract LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster);
	public abstract GenericType outputType();
	public abstract FunctionSignature signature();
}
