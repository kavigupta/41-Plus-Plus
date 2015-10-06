package fortytwo.vm.expressions;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.environment.UnorderedEnvironment;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;

public abstract class LiteralFunction extends LiteralExpression {
	public final FunctionType type;
	public final FunctionImplementation implementation;
	public static interface FunctionImplementation {
		public abstract LiteralExpression apply(List<LiteralExpression> arguments, TypeVariableRoster roster);
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
	public final LiteralExpression apply(AbstractTypeEnvironment typeEnv,
			List<LiteralExpression> arguments) {
		return implementation.apply(arguments,
				type.typeVariables(arguments, typeEnv));
	}
	public LiteralFunction contextualize(AbstractTypeEnvironment environment) {
		return this;
	}
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((implementation == null) ? 0 : implementation.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
}
