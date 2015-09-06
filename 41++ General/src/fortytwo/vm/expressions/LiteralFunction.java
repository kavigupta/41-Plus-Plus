package fortytwo.vm.expressions;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.vm.constructions.FunctionImplemented;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;

public abstract class LiteralFunction extends LiteralExpression {
	public final FunctionType type;
	public final FunctionImplementation implementation;
	public static interface FunctionImplementation {
		public abstract LiteralExpression apply(GlobalEnvironment env,
				List<LiteralExpression> arguments, TypeVariableRoster roster);
	}
	public LiteralFunction(Context context, FunctionType typeSignature,
			FunctionImplementation implementation) {
		super(context);
		this.type = typeSignature;
		this.implementation = implementation;
	}
	@Override
	public final ConcreteType resolveType() {
		return type;
	}
	public final LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments) {
		return implementation.apply(env, arguments,
				type.typeVariables(arguments, env.staticEnv));
	}
	public FunctionImplemented contextualize(StaticEnvironment environment) {
		// TODO Auto-generated method stub
		return null;
	}
}
